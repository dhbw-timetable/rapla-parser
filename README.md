# rapla-parser
This is a library for crawling timetable events from the rapla website of DHBWs. It parses the appointments into its own data structure. rapla-parser is available on [maven central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.dhbw-timetable%22%20a%3A%22rapla-parser%22).

## Import
Gradle (Maven Import):
```
compile 'com.github.dhbw-timetable:rapla-parser:0.3.0'
```

Maven:
```
<dependency>
   <groupId>com.github.dhbw-timetable</groupId>
   <artifactId>rapla-parser</artifactId>
   <version>0.3.0</version>
</dependency>
```

## Usage
You can import a range of weeks via using:
```
Map<LocalDate, ArrayList<Appointment>> data = DataImporter.ImportWeekRange(start, end, url)
```
If you have to use java.util.Date API for a good reason (e.g. Android JDK and NDK not available) we provide backport methods you can use:
```
Map<TimelessDate, ArrayList<Appointment>> data = DataImporter.Backport.ImportWeekRange(start, end, url)

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
Same as build but with deploy to ossrh:
```
mvn clean deploy -Possrh
```
