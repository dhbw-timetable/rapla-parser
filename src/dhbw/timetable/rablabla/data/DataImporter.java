package dhbw.timetable.rablabla.data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.sun.xml.internal.rngom.parse.host.Base;
import dhbw.timetable.rablabla.data.excpetions.NoConnectionException;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created by Hendrik Ulbrich (C) 2017
 */
public final class DataImporter {

	private DataImporter() {}

    public static Map<LocalDate, ArrayList<Appointment>> ImportDateRange(LocalDate startDate, LocalDate endDate, String url) throws MalformedURLException, NoConnectionException, IllegalAccessException {
		if(!NetworkUtilities.URLIsValid(url)) {
			throw new MalformedURLException();
        } else if (!NetworkUtilities.TestConnection(url)) {
			throw new NoConnectionException(url);
		}
		final String deSuffix = ".de/rapla?";
        int urlSplit = url.indexOf(deSuffix);
		return ImportDateRange(startDate, endDate, BaseURL.valueOf(url.substring(19, urlSplit).toUpperCase()), url.substring(urlSplit + deSuffix.length()));
    }

    public static Map<LocalDate, ArrayList<Appointment>> ImportDateRange(LocalDate startDate, LocalDate endDate, BaseURL baseURL, String args) throws IllegalAccessException {
		Map<LocalDate, ArrayList<Appointment>> appointments = new HashMap<>();
		startDate = DateUtilities.Normalize(startDate);
		endDate = DateUtilities.Normalize(endDate);
		
		do {
            try {
                appointments.put(startDate, ImportWeek(startDate, baseURL, args));
            } catch (SAXException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e ) {
                throw e;
            }
            startDate = startDate.plusDays(7);
		} while (!startDate.isAfter(endDate));

		return appointments;
	}

	public static ArrayList<Appointment> ImportWeek(LocalDate localDate, BaseURL baseURL, String args) throws SAXException, IOException, ParserConfigurationException, IllegalAccessException {
		localDate = DateUtilities.Normalize(localDate);
		
		ArrayList<Appointment> weekAppointments = new ArrayList<>();
		StringBuilder pageContentBuilder = new StringBuilder();
		StringBuilder connectionURL = new StringBuilder(baseURL.complete()).append("?");
		String line, pageContent;

		// TODO The parameters here are the same each week. define them and just pass connectionURL to ImportWeek
		Map<String, String> params = new HashMap<String, String>();
		String[] paramsStrings = args.split("&");
		for (int i = 0; i < paramsStrings.length; i++) {
		    String[] kvStrings = paramsStrings[i].split("=");
		    for (int j = 0; j < kvStrings.length; j++) {
		        params.put(kvStrings[0], kvStrings[1]);
            }
        }

		// Establish connection to date
        // key=txB1FOi5xd1wUJBWuX8lJhGDUgtMSFmnKLgAG_NVMhA_bi91ugPaHvrpxD-lcejo&today=Heute
		if (params.containsKey("key")) {
            connectionURL.append("key=").append(params.get("key"));
        // page=calendar&user=vollmer&file=tinf15b3&today=Heute
        } else if (params.containsKey("page") && params.get("page").equalsIgnoreCase("calendar") && params.containsKey("user") && params.containsKey("file")) {
            connectionURL.append("page=calendar&user=").append(params.get("user")).append("&file=").append(params.get("file"));
        } else {
            throw new IllegalAccessException();
        }

        connectionURL.append("&day=").append(localDate.getDayOfMonth()).append("&month=").append(localDate.getMonthValue()).append("&year=").append(localDate.getYear());

        URLConnection webConnection = new URL(connectionURL.toString()).openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(webConnection.getInputStream(), StandardCharsets.UTF_8));

		// Read the whole page
		while ((line = br.readLine()) != null) {
			pageContentBuilder.append(line).append("\n");
		}
		br.close();
		pageContent = pageContentBuilder.toString();

		// Trim and filter to correct tbody inner HTML
		pageContent = ("<?xml version=\"1.0\"?>\n" + pageContent.substring(pageContent.indexOf("<tbody>"), pageContent.lastIndexOf("</tbody>") + 8)).replaceAll("&nbsp;", "&#160;").replaceAll("<br>", "<br/>");

		// Parse the document
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(new ByteArrayInputStream(pageContent.getBytes("utf-8"))));
		doc.getDocumentElement().normalize();

		// Scan the table row by row
		NodeList nList = doc.getDocumentElement().getChildNodes();
		Node tableRow;
		for (int temp = 0; temp < nList.getLength(); temp++) {
			tableRow = nList.item(temp);
			if (tableRow.getNodeType() == Node.ELEMENT_NODE)
				importTableRow(weekAppointments, tableRow, DateUtilities.Clone(localDate));
		}

		return weekAppointments;
	}

	private static void importTableRow(ArrayList<Appointment> appointments, Node tableRow, LocalDate currDate) {
		// For each <td> in row
		NodeList cells = tableRow.getChildNodes();
		for (int i = 0; i < cells.getLength(); i++) {
			Node cell = cells.item(i);
			// Filter <th> and other crap
			if (cell.getNodeType() == Node.ELEMENT_NODE && cell.getNodeName().equals("td")) {
				Element element = (Element) cell;
				String type = element.getAttribute("class");
				if (type.startsWith("week_block")) {
					appointments.add(importAppointment(cell, currDate));
				} else if (type.startsWith("week_separatorcell")) {
					currDate = currDate.plusDays(1);
				}
			}
		}
	}

	private static Appointment importAppointment(Node block, LocalDate date) {
		Node aNode = block.getFirstChild();
		NodeList aChildren = aNode.getChildNodes();

		int correctShift = 0;
		// If no time is provided, appointment is whole working day
		String timeData, time;
		if (aChildren.item(0).getNodeType() == Node.ELEMENT_NODE) {
			time = "08:00-18:00";
			correctShift = -1;
		} else {
			timeData = ((CharacterData) aChildren.item(0)).getData();
			// Filter &#160; alias &nbsp;
			time = timeData.substring(0, 5).concat(timeData.substring(6));
		}

		// If no course is provieded it may be holiday or special event
		String course, info;
		if (aChildren.item(2 + correctShift).getNodeType() == Node.ELEMENT_NODE) {
			course = "No course specified";
			info = importInfoFromSpan(aChildren.item(2 + correctShift).getChildNodes().item(4).getChildNodes());
		} else {
			course = ((CharacterData) aChildren.item(2 + correctShift)).getData();
			info = importInfoFromSpan(aChildren.item(3 + correctShift).getChildNodes().item(4).getChildNodes());
		}
		
		LocalDateTime[] times = DateUtilities.ConvertToTime(date, time);
		
		Appointment a = new Appointment(times[0], times[1], course, info);

		return a;
	}

	private static String importInfoFromSpan(NodeList spanTableRows) {
		String tutor = "";
		String resource = "";
		for (int i = 0; i < spanTableRows.getLength(); i++) {
			Node row = spanTableRows.item(i);
			if (row.getNodeType() == Node.ELEMENT_NODE) {
				NodeList cells = row.getChildNodes();
				for (int x = 0; x < cells.getLength(); x++) {
					Node cell = cells.item(x);
					if (cell.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) cell;
						String type = element.getAttribute("class");
						if (type.contains("label")) {
							if (element.getTextContent().equalsIgnoreCase("Ressourcen:")) {
								resource = "Ressourcen: " + cell.getNextSibling().getNextSibling().getTextContent().trim().split(" ")[0];
							} else if (element.getTextContent().equalsIgnoreCase("Personen:")) {
								tutor = "Personen: " + cell.getNextSibling().getNextSibling().getTextContent();
							}
							// Ignore Bemerkung, zuletzt geändert, Veranstaltungsname
						} else if (type.contains("value")) {
							// ignore
						} else {
							// TODO Remove warnings after intense (release) testing
							System.out.println("Unidentified classname of row found in span table: " + type);
							System.out.println("row nodeName " + row.getNodeName());
							System.out.println("cell nodeName " + cell.getNodeName());
							System.out.println("element nodeName " + element.getNodeName());
						}
					}
				}
			}
		}
		return (resource + " " + tutor).trim();
	}

}
