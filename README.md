# rapla-parser
This is a library for crawling timetable events from the rapla website of DHBWs. It parses the appointments into its own data structure. rapla-parser is available on [maven central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.dhbw-timetable%22%20a%3A%22rapla-parser%22).

## Build
Use the maven profile to build the library:
```
mvn clean install -Possrh
```

## Deploy
Same es build but with deploy to ossrh:
```
mvn deploy install -Possrh
```
