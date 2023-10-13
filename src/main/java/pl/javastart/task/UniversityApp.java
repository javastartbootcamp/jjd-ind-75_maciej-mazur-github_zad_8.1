package pl.javastart.task;

import java.util.Arrays;
import java.util.Optional;

public class UniversityApp {
    static final int STUDENTS_PER_GROUP = 15;
    private static final int LECTURERS_PER_UNIVERSITY = 20;
    private static final int STUDENTS_PER_UNIVERSITY = 100;
    private static final int GROUPS_PER_UNIVERSITY = 20;

    private int currentStudentsNumber = 0;
    private int currentLecturersNumber = 0;
    private int currentGroupsNumber = 0;
    private int currentGradesNumber = 0;

    private Student[] students = new Student[STUDENTS_PER_UNIVERSITY];
    private Lecturer[] lecturers = new Lecturer[LECTURERS_PER_UNIVERSITY];
    private Group[] groups = new Group[GROUPS_PER_UNIVERSITY];
    private Grade[] grades = new Grade[GROUPS_PER_UNIVERSITY * STUDENTS_PER_GROUP];

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
        if (findLecturer(id).isPresent()) {
            System.out.println("Prowadzący z id " + id + " już istnieje");
            return;
        }

        if (currentLecturersNumber >= LECTURERS_PER_UNIVERSITY) {
            extendLecturers();
        }

        lecturers[currentLecturersNumber++] = new Lecturer(firstName, lastName, id, degree);
    }

    private Optional<Lecturer> findLecturer(int id) {
        for (int i = 0; i < currentLecturersNumber; i++) {
            if (lecturers[i].getId() == id) {
                return Optional.of(lecturers[i]);
            }
        }

        return Optional.empty();
    }

    private void extendLecturers() {
        lecturers = Arrays.copyOf(lecturers, lecturers.length + LECTURERS_PER_UNIVERSITY);
    }

    private Optional<Student> findStudentInStudentsArray(int index) {
        for (int i = 0; i < currentStudentsNumber; i++) {
            if (students[i].getIndex() == index) {
                return Optional.of(students[i]);
            }
        }

        return Optional.empty();
    }

    private void addStudentToStudentsInUniversity(Student student) {
        if (currentStudentsNumber >= STUDENTS_PER_UNIVERSITY) {
            extendStudents();
        }

        students[currentStudentsNumber++] = student;
    }

    private void extendStudents() {
        students = Arrays.copyOf(students, students.length + STUDENTS_PER_UNIVERSITY);
    }

    private Optional<Student> findStudentInGradesArray(int index, String groupCode) {
        for (int i = 0; i < currentGradesNumber; i++) {
            if (grades[i].getStudent().getIndex() == index && grades[i].getGroup().getCode().equals(groupCode)) {
                return Optional.of(grades[i].getStudent());
            }
        }

        return Optional.empty();
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
        Optional<Lecturer> lecturer = findLecturer(lecturerId);

        if (lecturer.isEmpty()) {
            System.out.println("Prowadzący o id " + lecturerId + " nie istnieje");
            return;
        }

        if (findGroupInGroupsArray(code).isPresent()) {
            System.out.println("Grupa " + code + " już istnieje");
            return;
        }

        if (currentGroupsNumber >= GROUPS_PER_UNIVERSITY) {
            extendGroups();
        }

        groups[currentGroupsNumber++] = new Group(code, name, lecturer.get());
    }

    private Optional<Group> findGroupInGroupsArray(String code) {
        for (int i = 0; i < currentGroupsNumber; i++) {
            if (groups[i].getCode().equals(code)) {
                return Optional.of(groups[i]);
            }
        }

        return Optional.empty();
    }

    private void extendGroups() {
        groups = Arrays.copyOf(groups, groups.length + GROUPS_PER_UNIVERSITY);
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
        Optional<Group> group = findGroupInGroupsArray(groupCode);

        if (group.isEmpty()) {
            System.out.println("Grupa " + groupCode + " nie istnieje");
            return;
        }

        Optional<Student> studentSoughtInGroup = group.get().findStudentInGroup(index);

        if (studentSoughtInGroup.isPresent()) {
            System.out.println("Student o indeksie " + index + " jest już w grupie " + groupCode);
            return;
        }

        /*
        Do tego miejsca dociera program w sytuacji, w której przeszukanie danej grupy w poszukiwaniu studenta zwróciło null.
        Referencja student wskazuje już zatem na tym etapie na bezużyteczny obszar, wykorzystam więc ją nieco niżej do wskazania na inny obszar.
        Dopisywany student mógł natomiast nie być jeszcze zapisany do danej grupy,
         ale może być zarejestrowany w jakiejś innej grupie, a w takim wypadku będzie też obecny w tablicy Students[] students
         zrzeszającej wszystkich studentów ze wszystkich grup uniwersytetu (tyle że bez powtórzeń). Jeśli ów student zostanie znaleziony w tablicy
         Students[] students, to znalezioną w ten sposób referencję do niego użyję do finalnego dodania tego studenta do danej grupy, unikając tym
         samym konieczności tworzenia nowego obiektu klasy Student. Konieczność ta zaistnieje tylko w razie nieznalezienia danego studenta nawet
         w zbiorczej tablicy students
        */
        Optional<Student> studentSoughtInStudentsArray = findStudentInStudentsArray(index);

        Student brandNewStudent;

        if (studentSoughtInStudentsArray.isEmpty()) {
            brandNewStudent = new Student(firstName, lastName, index);
            addStudentToStudentsInUniversity(brandNewStudent);
        } else {
            brandNewStudent = studentSoughtInStudentsArray.get();
        }

        group.get().addStudent(brandNewStudent);
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
        Optional<Group> group = findGroupInGroupsArray(groupCode);

        if (group.isEmpty()) {
            System.out.println("Grupa " + groupCode + " nie znaleziona");
            return;
        }

        System.out.println(group.get().printGroupInfo());
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
        Optional<Group> group = findGroupInGroupsArray(groupCode);

        if (group.isEmpty()) {
            System.out.println("Grupa " + groupCode + " nie istnieje");
            return;
        }

        Optional<Student> studentInGroup = group.get().findStudentInGroup(studentIndex);

        if (studentInGroup.isEmpty()) {
            System.out.println("Student o indeksie " + studentIndex + " nie jest zapisany do grupy " + groupCode);
            return;
        }

        /*
        Oceny wyciągnięte zostały z klasy Group i zamiast tego umieszczone w klasie University jako tablica obiektów klasy Grade.
        Z tego powodu na tym etapie konieczne jest wyszukanie studenta ponownie, tym razem jednak właśnie w tablicy Grade[]
        grades, by sprawdzić, czy znajduje się on tam już z danym numerem indeksu i z danym kodem grupy, czy nie (czyli by sprawdzić,
        czy ma już wystawioną ocenę, czy jeszcze nie).
         */
        Optional<Student> studentInGradesArray = findStudentInGradesArray(studentIndex, groupCode);

        if (studentInGradesArray.isPresent()) {
            System.out.println("Student o indeksie " + studentIndex + " ma już wystawioną ocenę dla grupy " + groupCode);
            return;
        }

        if (currentGradesNumber >= GROUPS_PER_UNIVERSITY * STUDENTS_PER_GROUP) {
            extendGrades();
        }

        grades[currentGradesNumber++] = new Grade(studentInGroup.get(), group.get(), grade);
    }

    private void extendGrades() {
        grades = Arrays.copyOf(grades, grades.length + GROUPS_PER_UNIVERSITY * STUDENTS_PER_GROUP);
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
        Optional<Group> group = findGroupInGroupsArray(groupCode);

        /*
        Scenariusz nie wymagany w treści zadania, ale wymagany przez testy. Test void shouldNotPrintGradesWhenGroupDoesntExits()
        failuje, jeśli przy próbie wydruku ocen nieistniejącej grupy program nie zwraca poniższego komunikatu błędu
         */
        if (group.isEmpty()) {
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
