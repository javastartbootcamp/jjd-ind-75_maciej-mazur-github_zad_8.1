package pl.javastart.task;

class Grade {
    private final Student student;
    private final Group group;
    private final double grade;

    public Grade(Student student, Group group, double grade) {
        this.student = student;
        this.group = group;
        this.grade = grade;
    }

    public Student getStudent() {
        return student;
    }

    public Group getGroup() {
        return group;
    }

    public double getGrade() {
        return grade;
    }
}
