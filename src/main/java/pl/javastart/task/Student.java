package pl.javastart.task;

class Student extends Person {
    private final int index;

    Student(String firstName, String lastName, int index) {
        super(firstName, lastName);
        this.index = index;
    }

    int getIndex() {
        return index;
    }

    String getInfo() {
        return index + " " + getFirstName() + " " + getLastName();
    }
}
