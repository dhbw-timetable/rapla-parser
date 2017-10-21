# rapla-parser
This is a library for crawling timetable events from the rapla website of DHBWs. It parses the appointments into its own data structure. rapla-parser is available on [maven central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.dhbw-timetable%22%20a%3A%22rapla-parser%22).

## Import
Gradle (Maven Import):
```
compile 'com.github.dhbw-timetable:rapla-parser:0.2.1'
```

Maven:
```
<dependency>
   <groupId>com.github.dhbw-timetable</groupId>
   <artifactId>rapla-parser</artifactId>
   <version>0.2.1</version>
</dependency>
```

## Usage
You can import a range of weeks via using:
```
Map<LocalDate, ArrayList<Appointment>> data = DataImporter.ImportWeekRange(start, end, url)
```

# Contribute

You are free to customize this library for your own under the given MIT License.

## Build
Generate GPG keys and login to ossrh.
Use the maven profile to build the library:
```
mvn clean install -Possrh
```

## Deploy
Same es build but with deploy to ossrh:
```
mvn clean deploy -Possrh
```
