package pl.javastart.task;

class Group {
    private final String code;
    private final String name;
    private Lecturer lecturer;
    private int currentStudentsNumber = 0;
    private Student[] students;

    Group(String code, String name, Lecturer lecturer) {
        this.code = code;
        this.name = name;
        this.lecturer = lecturer;
        students = new Student[UniversityApp.STUDENTS_PER_GROUP];
    }

    void printGroupInfo() {
        System.out.printf("Kod: %s%nNazwa: %s%nProwadzący: %s%nUczestnicy:%n%s",
                code, name, lecturer.getInfo(), getStudentsInfo());
    }

    private String getStudentsInfo() {
        StringBuilder list = new StringBuilder();
        Student student;

        for (int i = 0; i < currentStudentsNumber; i++) {
            student = students[i];
            list.append(student.getIndex() + " " + student.getFirstName() + " " + student.getLastName() + "\n");
        }

        return list.toString();
    }

    void addStudent(Student student) {
        if (currentStudentsNumber >= UniversityApp.STUDENTS_PER_GROUP) {
            extendStudents();
        }

        students[currentStudentsNumber++] = student;
    }

    private void extendStudents() {
        Student[] extendedArray = new Student[students.length + UniversityApp.STUDENTS_PER_GROUP];
        System.arraycopy(students, 0, extendedArray, 0, students.length);
        students = extendedArray;
    }

    String getCode() {
        return code;
    }

    int findStudent(int index) {
        for (int i = 0; i < currentStudentsNumber; i++) {
            if (students[i].getIndex() == index) {
                return i;
            }
        }

        return UniversityApp.NOT_FOUND;
    }

    public String getName() {
        return name;
    }
}
