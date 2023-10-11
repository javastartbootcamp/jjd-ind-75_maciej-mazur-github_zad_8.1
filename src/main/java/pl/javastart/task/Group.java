package pl.javastart.task;

class Group {
    private final String code;
    private final String name;
    private Lecturer lecturer;
    private int currentStudentsNumber = 0;
    private Student[] students;
    private double[] grades;

    Group(String code, String name, Lecturer lecturer) {
        this.code = code;
        this.name = name;
        this.lecturer = lecturer;
        students = new Student[University.STUDENTS_PER_GROUP];
        grades = new double[University.STUDENTS_PER_GROUP];
    }

    void printGrades() {
        for (int i = 0; i < students.length; i++) {
            if (grades[i] != 0) {
                System.out.println(students[i].getInfo() + ": " + grades[i]);
            }
        }
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
        if (currentStudentsNumber >= University.STUDENTS_PER_GROUP) {
            extendStudentsAndGrades();
        }

        students[currentStudentsNumber++] = student;
    }

    void printStudentGrade(int studentPositionInGroup) {
        System.out.println(name + ": " + grades[studentPositionInGroup]);
    }

    private void extendStudentsAndGrades() {
        Student[] extendedArray = new Student[students.length + University.STUDENTS_PER_GROUP];
        System.arraycopy(students, 0, extendedArray, 0, students.length);
        students = extendedArray;

        double[] extendedGrades = new double[students.length + University.STUDENTS_PER_GROUP];
        System.arraycopy(grades, 0, extendedGrades, 0, students.length);
        grades = extendedGrades;
    }

    String getCode() {
        return code;
    }

    int addGrade(int studentIndex, double grade) {
        int studentArrayPosition = findStudent(studentIndex);

        if (studentArrayPosition == University.STUDENT_DOESNT_EXIST_IN_GROUP) {
            return University.STUDENT_DOESNT_EXIST_IN_GROUP;
        }

        if (grades[studentArrayPosition] != 0) {
            return University.GRADE_EXISTS;
        }

        grades[studentArrayPosition] = grade;
        return University.OPERATION_SUCCESSFUL;
    }

    int findStudent(int index) {
        for (int i = 0; i < currentStudentsNumber; i++) {
            if (students[i].getIndex() == index) {
                return i;
            }
        }

        return University.STUDENT_DOESNT_EXIST_IN_GROUP;
    }
}
