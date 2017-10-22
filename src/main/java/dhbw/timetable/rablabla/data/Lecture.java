package dhbw.timetable.rablabla.data;

public abstract class Lecture {

    protected String course, info;

    public Lecture(String course, String info) {
        this.course = course;
        this.info = info;
    }

    public String getCourse() {
        return course;
    }

    public String getInfo() {
        return info;
    }
}
