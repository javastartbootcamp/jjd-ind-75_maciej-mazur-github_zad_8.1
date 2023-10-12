package pl.javastart.task;

public class UniversityApp {
    static final int STUDENTS_PER_GROUP = 15;
    private static final int LECTURERS_PER_UNIVERSITY = 20;
    private static final int STUDENTS_PER_UNIVERSITY = 100;
    private static final int GROUPS_PER_UNIVERSITY = 20;

    static final int NOT_FOUND = -1;

    private int currentStudentsNumber = 0;
    private int currentLecturersNumber = 0;
    private int currentGroupsNumber = 0;
    private int currentGradesNumber = 0;

    private Student[] students = new Student[STUDENTS_PER_UNIVERSITY];
    private Lecturer[] lecturers = new Lecturer[LECTURERS_PER_UNIVERSITY];
    private Group[] groups = new Group[GROUPS_PER_UNIVERSITY];
    private Grade[] grades = new Grade[GROUPS_PER_UNIVERSITY * STUDENTS_PER_GROUP];

    private int findLecturer(int id) {
        for (int i = 0; i < currentLecturersNumber; i++) {
            if (lecturers[i].getId() == id) {
                return i;
            }
        }

        return NOT_FOUND;
    }

    private void extendLecturers() {
        Lecturer[] extendedArray = new Lecturer[lecturers.length + LECTURERS_PER_UNIVERSITY];
        System.arraycopy(lecturers, 0, extendedArray, 0, lecturers.length);
        lecturers = extendedArray;
    }

    private void extendGrades() {
        Grade[] extendedArray = new Grade[grades.length + GROUPS_PER_UNIVERSITY * STUDENTS_PER_GROUP];
        System.arraycopy(grades, 0, extendedArray, 0, grades.length);
        grades = extendedArray;
    }

    /**
     * Tworzy prowadzącego zajęcia.
     * W przypadku gdy prowadzący z zadanym id już istnieje, wyświetlany jest komunikat:
     * "Prowadzący z id [id_prowadzacego] już istnieje"
     *
     * @param id        - unikalny identyfikator prowadzącego
     * @param degree    - stopień naukowy prowadzącego
     * @param firstName - imię prowadzącego
     * @param lastName  - nazwisko prowadzącego
     */
    public void createLecturer(int id, String degree, String firstName, String lastName) {
        if (findLecturer(id) != NOT_FOUND) {
            System.out.println("Prowadzący z id " + id + " już istnieje");
        }

        if (currentLecturersNumber >= LECTURERS_PER_UNIVERSITY) {
            extendLecturers();
        }

        lecturers[currentLecturersNumber++] = new Lecturer(firstName, lastName, id, degree);
    }

    private int findGroup(String code) {
        for (int i = 0; i < currentGroupsNumber; i++) {
            if (groups[i].getCode().equals(code)) {
                return i;
            }
        }

        return NOT_FOUND;
    }

    private void extendGroups() {
        Group[] extendedArray = new Group[groups.length + GROUPS_PER_UNIVERSITY];
        System.arraycopy(groups, 0, extendedArray, 0, groups.length);
        groups = extendedArray;
    }

    private int findStudentInGroup(int groupPositionInArray, int index) {
        Group group = groups[groupPositionInArray];
        return group.findStudent(index);
    }

    private Student findStudentInUniversity(int index) {
        for (int i = 0; i < currentStudentsNumber; i++) {
            if (students[i].getIndex() == index) {
                return students[i];
            }
        }

        return null;
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

    private Student findStudentInGrades(int index, String groupCode) {
        for (int i = 0; i < currentGradesNumber; i++) {
            if (grades[i].getStudent().getIndex() == index && grades[i].getGroup().getCode().equals(groupCode)) {
                return grades[i].getStudent();
            }
        }

        return null;
    }

    /**
     * Tworzy grupę zajęciową.
     * W przypadku gdy grupa z zadanym kodem już istnieje, wyświetla się komunikat:
     * "Grupa [kod grupy] już istnieje"
     * W przypadku gdy prowadzący ze wskazanym id nie istnieje wyświetla się komunikat:
     * "Prowadzący o id [id prowadzacego] nie istnieje"
     *
     * @param code       - unikalny kod grupy
     * @param name       - nazwa przedmiotu (np. "Podstawy programowania")
     * @param lecturerId - identyfikator prowadzącego. Musi zostać wcześniej utworzony za pomocą metody {@link #createLecturer(int, String, String, String)}
     */
    public void createGroup(String code, String name, int lecturerId) {
        int lecturerPositionInArray = findLecturer(lecturerId);

        if (lecturerPositionInArray == NOT_FOUND) {
            System.out.println("Prowadzący o id " + lecturerId + " nie istnieje");
            return;
        }

        if (findGroup(code) != NOT_FOUND) {
            System.out.println("Grupa " + code + " już istnieje");
            return;
        }

        if (currentGroupsNumber >= GROUPS_PER_UNIVERSITY) {
            extendGroups();
        }

        groups[currentGroupsNumber++] = new Group(code, name, lecturers[lecturerPositionInArray]);
    }

    /**
     * Dodaje studenta do grupy zajęciowej.
     * W przypadku gdy grupa zajęciowa nie istnieje wyświetlany jest komunikat:
     * "Grupa [kod grupy] nie istnieje
     *
     * @param index     - unikalny numer indeksu studenta
     * @param groupCode - kod grupy utworzonej wcześniej za pomocą {@link #createGroup(String, String, int)}
     * @param firstName - imię studenta
     * @param lastName  - nazwisko studenta
     */
    public void addStudentToGroup(int index, String groupCode, String firstName, String lastName) {
        int groupPositionInArray = findGroup(groupCode);
        if (groupPositionInArray == NOT_FOUND) {
            System.out.println("Grupa " + groupCode + " nie istnieje");
            return;
        }

        if (findStudentInGroup(groupPositionInArray, index) != NOT_FOUND) {
            System.out.println("Student o indeksie " + index + " jest już w grupie " + groupCode);
            return;
        }

        Student addedStudent = findStudentInUniversity(index);

        if (addedStudent == null) {
            addedStudent = new Student(firstName, lastName, index);
            addStudentToStudentsInUniversity(addedStudent);
        }

        groups[groupPositionInArray].addStudent(addedStudent);
    }

    /**
     * Wyświetla informacje o grupie w zadanym formacie.
     * Oczekiwany format:
     * Kod: [kod_grupy]
     * Nazwa: [nazwa przedmiotu]
     * Prowadzący: [stopień naukowy] [imię] [nazwisko]
     * Uczestnicy:
     * [nr indeksu] [imie] [nazwisko]
     * [nr indeksu] [imie] [nazwisko]
     * [nr indeksu] [imie] [nazwisko]
     * W przypadku gdy grupa nie istnieje, wyświetlany jest komunikat w postaci: "Grupa [kod] nie znaleziona"
     *
     * @param groupCode - kod grupy, dla której wyświetlić informacje
     */
    public void printGroupInfo(String groupCode) {
        int groupPositionInArray = findGroup(groupCode);

        if (groupPositionInArray == NOT_FOUND) {
            System.out.println("Grupa " + groupCode + " nie znaleziona");
            return;
        }

        Group group = groups[groupPositionInArray];
        group.printGroupInfo();
    }

    /**
     * Dodaje ocenę końcową dla wskazanego studenta i grupy.
     * Student musi być wcześniej zapisany do grupy za pomocą {@link #addStudentToGroup(int, String, String, String)}
     * W przypadku, gdy grupa o wskazanym kodzie nie istnieje, wyświetlany jest komunikat postaci:
     * "Grupa pp-2022 nie istnieje"
     * W przypadku gdy student nie jest zapisany do grupy, wyświetlany jest komunikat w
     * postaci: "Student o indeksie 179128 nie jest zapisany do grupy pp-2022"
     * W przypadku gdy ocena końcowa już istnieje, wyświetlany jest komunikat w postaci:
     * "Student o indeksie 179128 ma już wystawioną ocenę dla grupy pp-2022"
     *
     * @param studentIndex - numer indeksu studenta
     * @param groupCode    - kod grupy
     * @param grade        - ocena
     */
    public void addGrade(int studentIndex, String groupCode, double grade) {
        int groupArrayPosition = findGroup(groupCode);

        if (groupArrayPosition == NOT_FOUND) {
            System.out.println("Grupa " + groupCode + " nie istnieje");
            return;
        }

        Group group = groups[groupArrayPosition];
        int studentArrayPosition = group.findStudent(studentIndex);

        if (studentArrayPosition == NOT_FOUND) {
            System.out.println("Student o indeksie " + studentIndex + " nie jest zapisany do grupy " + groupCode);
            return;
        }

        Student student = findStudentInGrades(studentIndex, groupCode);

        if (student != null) {
            System.out.println("Student o indeksie " + studentIndex + " ma już wystawioną ocenę dla grupy " + groupCode);
            return;
        }

        Student studentInUniversity = findStudentInUniversity(studentIndex);

        if (studentInUniversity == null) {
            System.out.println("Student o indeksie " + studentIndex + " nie jest zapisany do żadnej grupy");
            return;
        }

        if (currentGradesNumber >= GROUPS_PER_UNIVERSITY * STUDENTS_PER_GROUP) {
            extendGrades();
        }

        grades[currentGradesNumber++] = new Grade(studentInUniversity, group, grade);
    }

    /**
     * Wyświetla wszystkie oceny studenta.
     * Przykładowy wydruk:
     * Podstawy programowania: 5.0
     * Programowanie obiektowe: 5.5
     *
     * @param index - numer indesku studenta dla którego wyświetlić oceny
     */
    public void printGradesForStudent(int index) {
        for (int i = 0; i < currentGradesNumber; i++) {
            if (grades[i].getStudent().getIndex() == index) {
                System.out.println(grades[i].getGroup().getName() + ": " + grades[i].getGrade());
            }
        }
    }

    /**
     * Wyświetla oceny studentów dla wskazanej grupy.
     * Przykładowy wydruk:
     * 179128 Marcin Abacki: 5.0
     * 179234 Dawid Donald: 4.5
     * 189521 Anna Kowalska: 5.5
     *
     * @param groupCode - kod grupy, dla której wyświetlić oceny
     */
    public void printGradesForGroup(String groupCode) {
        int groupPositionInArray = findGroup(groupCode);

        if (groupPositionInArray == NOT_FOUND) {
            System.out.println("Grupa " + groupCode + " nie istnieje");
            return;
        }

        for (int i = 0; i < currentGradesNumber; i++) {
            if (grades[i].getGroup().getCode().equals(groupCode)) {
                System.out.println(grades[i].getStudent().getInfo() + ": " + grades[i].getGrade());
            }
        }
    }

    /**
     * Wyświetla wszystkich studentów. Każdy student powinien zostać wyświetlony tylko raz.
     * Każdy student drukowany jest w nowej linii w formacie [nr_indesku] [imie] [nazwisko]
     * Przykładowy wydruk:
     * 179128 Marcin Abacki
     * 179234 Dawid Donald
     * 189521 Anna Kowalska
     */
    public void printAllStudents() {
        for (int i = 0; i < currentStudentsNumber; i++) {
            System.out.println(students[i].getInfo());
        }
    }
}
