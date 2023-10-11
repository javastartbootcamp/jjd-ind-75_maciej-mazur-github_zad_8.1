package pl.javastart.task;

class University {
    static final int LECTURER_DOESNT_EXIST = -1;
    static final int LECTURER_EXISTS = 1;
    //static final int STUDENT_DOESNT_EXIST_IN_UNIVERSITY = -2;
    //static final int STUDENT_EXISTS_IN_UNIVERSITY = 2;
    static final int STUDENT_DOESNT_EXIST_IN_GROUP = -3;
    static final int STUDENT_EXISTS_IN_GROUP = 3;
    static final int GROUP_DOESNT_EXIST = -4;
    static final int GROUP_EXISTS = 4;
    static final int GRADE_EXISTS = 5;
    static final int OPERATION_SUCCESSFUL = 0;

    static final int STUDENTS_PER_GROUP = 15;
    private static final int LECTURERS_PER_UNIVERSITY = 20;
    private static final int STUDENTS_PER_UNIVERSITY = 100;
    private static final int GROUPS_PER_UNIVERSITY = 20;

    private int currentStudentsNumber = 0;
    private int currentLecturersNumber = 0;
    private int currentGroupsNumber = 0;

    private Student[] students = new Student[STUDENTS_PER_UNIVERSITY];
    private Lecturer[] lecturers = new Lecturer[LECTURERS_PER_UNIVERSITY];
    private Group[] groups = new Group[GROUPS_PER_UNIVERSITY];

    void printGradesForGroup(String groupCode) {
        int groupPosition = findGroup(groupCode);

        if (groupPosition == GROUP_DOESNT_EXIST) {
            System.out.println("Grupa " + groupCode + " nie istnieje");
            return;
        }

        groups[groupPosition].printGrades();
    }

    void printAllStudents() {
        for (int i = 0; i < currentStudentsNumber; i++) {
            System.out.println(students[i].getInfo());
        }
    }

    void printGradesForStudent(int index) {
        if (findStudentInUniversity(index) == null) {   // w razie próby wyświetlenia listy ocen studenta, który w ogóle nie
            return;                                 // został zarejestrowany na uniwersytecie w żadnej grupie. Przypadek nieprzewidziany w treści zadania
        }

        int studentPositionInGroup;

        for (int i = 0; i < currentGroupsNumber; i++) {
            studentPositionInGroup = findStudentInGroup(i, index);
            if (studentPositionInGroup != STUDENT_DOESNT_EXIST_IN_GROUP) {
                groups[i].printStudentGrade(studentPositionInGroup);
            }
        }
    }

    int addStudentToGroup(int index, String groupCode, String firstName, String lastName) {
        int groupPositionInArray = findGroup(groupCode);
        if (groupPositionInArray == GROUP_DOESNT_EXIST) {
            return GROUP_DOESNT_EXIST;
        }

        if (findStudentInGroup(groupPositionInArray, index) != STUDENT_DOESNT_EXIST_IN_GROUP) {
            return STUDENT_EXISTS_IN_GROUP;
        }

        Student addedStudent = findStudentInUniversity(index);

        if (addedStudent == null) {
            addedStudent = new Student(firstName, lastName, index);
            addStudentToStudentsInUniversity(addedStudent);
        }

        groups[groupPositionInArray].addStudent(addedStudent);
        return OPERATION_SUCCESSFUL;
    }

    int addGrade(int studentIndex, String groupCode, double grade) {
        int groupArrayPosition = findGroup(groupCode);

        if (groupArrayPosition == GROUP_DOESNT_EXIST) {   // przypadek nieprzewidziany w treści zadania
            return GROUP_DOESNT_EXIST;
        }

        Group group = groups[groupArrayPosition];
        return group.addGrade(studentIndex, grade);
    }

    void printGroupInfo(String groupCode) {
        int positionInArray = findGroup(groupCode);

        if (positionInArray == University.GROUP_DOESNT_EXIST) {
            System.out.println("Grupa " + groupCode + " nie znaleziona");
            return;
        }

        Group group = groups[positionInArray];
        group.printGroupInfo();
    }

    private void addStudentToStudentsInUniversity(Student student) {
        if (currentStudentsNumber >= STUDENTS_PER_UNIVERSITY) {
            extendStudents();
        }

        students[currentStudentsNumber++] = student;
    }

    private void extendStudents() {
        Student[] extendedArray = new Student[students.length + STUDENTS_PER_UNIVERSITY];
        System.arraycopy(students, 0, extendedArray, 0, students.length);
        students = extendedArray;
    }

    private int findStudentInGroup(int groupPositionInArray, int index) {
        Group group = groups[groupPositionInArray];
        return group.findStudent(index);
    }

    int addGroup(String code, String name, int lecturerId) {
        int lecturerPositionInArray = findLecturer(lecturerId);

        if (lecturerPositionInArray == LECTURER_DOESNT_EXIST) {
            return LECTURER_DOESNT_EXIST;
        }

        if (findGroup(code) != GROUP_DOESNT_EXIST) {
            return GROUP_EXISTS;
        }

        if (currentGroupsNumber >= GROUPS_PER_UNIVERSITY) {
            extendGroups();
        }

        groups[currentGroupsNumber++] = new Group(code, name, lecturers[lecturerPositionInArray]);
        return OPERATION_SUCCESSFUL;
    }

    private void extendGroups() {
        Group[] extendedArray = new Group[groups.length + GROUPS_PER_UNIVERSITY];
        System.arraycopy(groups, 0, extendedArray, 0, groups.length);
        groups = extendedArray;
    }

    int addLecturer(int id, String degree, String firstName, String lastName) {
        if (findLecturer(id) != LECTURER_DOESNT_EXIST) {
            return LECTURER_EXISTS;
        }

        if (currentLecturersNumber >= LECTURERS_PER_UNIVERSITY) {
            extendLecturers();
        }

        lecturers[currentLecturersNumber++] = new Lecturer(firstName, lastName, id, degree);
        return OPERATION_SUCCESSFUL;
    }

    private void extendLecturers() {
        Lecturer[] extendedArray = new Lecturer[lecturers.length + LECTURERS_PER_UNIVERSITY];
        System.arraycopy(lecturers, 0, extendedArray, 0, lecturers.length);
        lecturers = extendedArray;
    }

    private Student findStudentInUniversity(int index) {
        for (int i = 0; i < currentStudentsNumber; i++) {
            if (students[i].getIndex() == index) {
                return students[i];
            }
        }

        return null;
    }

    private int findLecturer(int id) {
        for (int i = 0; i < currentLecturersNumber; i++) {
            if (lecturers[i].getId() == id) {
                return i;
            }
        }

        return LECTURER_DOESNT_EXIST;
    }

    int findGroup(String code) {
        for (int i = 0; i < currentGroupsNumber; i++) {
            if (groups[i].getCode().equals(code)) {
                return i;
            }
        }

        return GROUP_DOESNT_EXIST;
    }
}
