package pl.javastart.task;

class Lecturer extends Person {
    private final int id;
    private final String degree;

    Lecturer(String firstName, String lastName, int id, String degree) {
        super(firstName, lastName);
        this.id = id;
        this.degree = degree;
    }

    int getId() {
        return id;
    }

    String getInfo() {
        return degree + " " + getFirstName() + " " + getLastName();
    }
}
