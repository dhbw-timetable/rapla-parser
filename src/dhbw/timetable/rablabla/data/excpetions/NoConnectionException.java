package dhbw.timetable.rablabla.data.excpetions;

public class NoConnectionException extends Exception {
    String url;

    public NoConnectionException(String url) {
        this.url = url;
    }

    @Override
    public String getMessage() {
        return "No internet connection or rapla server is down. Check URL: " + url;
    }
}
