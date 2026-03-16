package afikEshel_guyFogel;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String DATA_FILE = "college_data.bin";
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        CollegeManager collegeManager = CollegeManager.loadFromFile(DATA_FILE);
        if (collegeManager == null){
            System.out.print("Enter college name: ");
            String collegeName = scan.nextLine();
            collegeManager = new CollegeManager(collegeName);
        }

        int choice = -1;

        while (choice !=0){
            System.out.println("---" + collegeManager.getCollegeName() + " Menu---");
            System.out.println("0 - Exit");
            System.out.println("1 - add lecturer");
            System.out.println("2 - add committee");
            System.out.println("3 - add member to committee");
            System.out.println("4 - update chairperson");
            System.out.println("5 - remove member from committee");
            System.out.println("6 - add department");
            System.out.println("7 - show all lecturers salary average");
            System.out.println("8 - show all lecturers salary average by department");
            System.out.println("9 - show all lecturers details");
            System.out.println("10 - show all committees details");
            System.out.println("11 - add lecturer to department");
            System.out.println("12 - compare 2 DR/PROF lecturers by number of articles");
            System.out.println("13 - compare 2 committees by number of members");
            System.out.println("14 - compare 2 committees by total number of articles");
            System.out.println("15 - duplicate an existing committee");

            System.out.print("choose an option: ");
            choice = Integer.parseInt(scan.nextLine());

            switch (choice){
                case 1:
                    addLecturer(scan,collegeManager);
                    break;
                case 2:
                    addCommittee(scan,collegeManager);
                    break;
                case 3:
                    addLecturerToCommitteeFlow(scan,collegeManager);
                    break;
                case 4:
                    updateChairpersonFlow(scan,collegeManager);
                    break;
                case 5:
                    removeLecturerFromCommitteeFlow(scan,collegeManager);
                    break;
                case 6:
                    addDepartmentFlow(scan,collegeManager);
                    break;
                case 7:
                    showOverallAverageSalary(collegeManager);
                    break;
                case 8:
                    showAverageSalaryByDepartment(scan,collegeManager);
                    break;
                case 9:
                    showAllLecturers(collegeManager);
                    break;
                case 10:
                    showAllCommittees(collegeManager);
                    break;
                case 11:
                    addLecturerToDepartment(scan,collegeManager);
                    break;
                case 12:
                    compareLecturersByArticles(scan,collegeManager);
                    break;
                case 13:
                    compareCommitteesBySize(scan,collegeManager);
                    break;
                case 14:
                    compareCommitteesByArticles(scan, collegeManager);
                    break;
                case 15:
                    duplicateCommitteeFlow(scan,collegeManager);
                    break;
                case 0:
                    System.out.println("exiting...");
                    break;
                default:
                    System.out.println("Invalid input");
            }

        }
        collegeManager.saveToFile(DATA_FILE);
        scan.close();

    }

    private static void addLecturerToDepartment(Scanner scan, CollegeManager collegeManager) {
        System.out.println("enter department name: ");
        String departmentName = scan.nextLine();

        Department department = collegeManager.findDepartmentByName(departmentName);

        if (department == null){
            System.out.println("department not found. enter another name.");
            return;
        }
        System.out.println("enter id of lecturer: ");
        String lecturerId = scan.nextLine();

        Lecturer lecturer = collegeManager.findLecturerById(lecturerId);

        if (lecturer == null){
            System.out.println("lecturer not found. enter another id. ");
            return;
        }
        if (lecturer.getDepartment() != null){
            System.out.println("lecturer already assigned to a department (" + lecturer.getDepartment().getName() + ").");
            return;
        }
        department.addLecturer(lecturer);
        lecturer.setDepartment(department);
        System.out.println("lecturer has been added to department successfully.");


    }

    private static void compareLecturersByArticles(Scanner scanner, CollegeManager collegeManager){
        System.out.print("Enter ID of first lecturer: ");
        String id1 = scanner.nextLine();

        System.out.print("Enter ID of second lecturer: ");
        String id2 = scanner.nextLine();

        try {
            Lecturer lecturer1 = collegeManager.findLecturerById(id1);
            Lecturer lecturer2 = collegeManager.findLecturerById(id2);

            if (lecturer1 == null ) throw new LecturerNotFoundException("Lecturer with ID " + id1 + " not found.");
            if (lecturer2 == null ) throw new LecturerNotFoundException("Lecturer with ID " + id2 + " not found.");

            if (lecturer1.getDegree().ordinal() < AcademicDegree.DR.ordinal()){
                System.out.println(lecturer1.getFullName() + " is not DR/PROF");
                return;
            }
            if (lecturer2.getDegree().ordinal() < AcademicDegree.DR.ordinal()){
                System.out.println(lecturer2.getFullName() + " is not DR/PROF");
                return;
            }

            int numArticlesLecturer1 = lecturer1.getNumOfArticles();
            int numArticlesLecturer2 = lecturer2.getNumOfArticles();

            System.out.println(lecturer1.getFullName() + " has " + numArticlesLecturer1 + "articles.");
            System.out.println(lecturer2.getFullName() + " has " + numArticlesLecturer2 + "articles.");

            if (numArticlesLecturer1 > numArticlesLecturer2) {
                System.out.println(lecturer1.getFullName() + " has more articles.");
            } else if (numArticlesLecturer2 > numArticlesLecturer1) {
                System.out.println(lecturer2.getFullName() + " has more articles.");
            } else {
                System.out.println("Both have same number of articles.");
            }

        } catch (LecturerNotFoundException e){
            System.out.println(e.getMessage());
        }

    }

    private static void compareCommitteesBySize(Scanner scanner, CollegeManager collegeManager){
        System.out.print("Enter name of first committee: ");
        String committeeName1 = scanner.nextLine();

        System.out.print("Enter name of second committee: ");
        String committeeName2 = scanner.nextLine();

        try {
            Committee committee1 = collegeManager.findCommitteeByName(committeeName1);
            Committee committee2 = collegeManager.findCommitteeByName(committeeName2);

            if (committee1 == null ) throw new CommitteeNotFoundException("Committee " + committeeName1 + " not found.");
            if (committee2 == null ) throw new CommitteeNotFoundException("Committee " + committeeName2 + " not found.");

            int sizeOfCommittee1 = committee1.getNumOfMembers();
            int sizeOfCommittee2 = committee2.getNumOfMembers();

            System.out.println("Members in " + committeeName1 + ": " + sizeOfCommittee1);
            System.out.println("Members in " + committeeName2 + ": " + sizeOfCommittee2);

            if (sizeOfCommittee1 > sizeOfCommittee2) {
                System.out.println(committeeName1 + " has more members.");
            } else if (sizeOfCommittee2 > sizeOfCommittee1) {
                System.out.println(committeeName2 + " has more members.");
            } else {
                System.out.println("Both committees have the same number of members.");
            }

        } catch (CommitteeNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void compareCommitteesByArticles(Scanner scanner, CollegeManager collegeManager){
        System.out.print("Enter name of first committee: ");
        String committeeName1 = scanner.nextLine();

        System.out.print("Enter name of second committee: ");
        String committeeName2 = scanner.nextLine();

        try {
            Committee committee1 = collegeManager.findCommitteeByName(committeeName1);
            Committee committee2 = collegeManager.findCommitteeByName(committeeName2);

            if (committee1 == null) throw new CommitteeNotFoundException("Committee" + committeeName1 + " not found.");
            if (committee2 == null) throw new CommitteeNotFoundException("Committee" + committeeName2 + " not found.");

            int totalArticles1 = committee1.getTotalArticlesByMembers();
            int totalArticles2 = committee2.getTotalArticlesByMembers();

            System.out.println("Total articles in " + committeeName1 + ": " + totalArticles1);
            System.out.println("Total articles in " + committeeName2 + ": " + totalArticles2);

            if (totalArticles1> totalArticles2) {
                System.out.println(committeeName1 + " has more research articles.");
            } else if (totalArticles2 > totalArticles1) {
                System.out.println(committeeName2 + " has more research articles.");
            } else {
                System.out.println("Both committees have the same research output.");
            }

        } catch (CommitteeNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    private static void addLecturer(Scanner scanner, CollegeManager collegeManager){
        System.out.print("enter id of lecturer: ");
        String id = scanner.nextLine();

        if (collegeManager.findLecturerById(id) != null){
            System.out.println("id already exist. please enter new id. ");
            return;
        }

        System.out.println("enter full name: ");
        String fullName = scanner.nextLine();

        System.out.println("enter academic degree (FIRST,SECOND,DR,PROF): ");
        String degreeInput = scanner.nextLine().toUpperCase();

        AcademicDegree degree = AcademicDegree.FIRST;
        boolean valid = false;

        for (AcademicDegree d : AcademicDegree.values()){
            if (d.name().equals(degreeInput)){
                degree = d;
                valid = true;
                break;
            }
        }
        if (!valid){
            System.out.println("invalid degree. set to default: FIRST");

        }

        System.out.println("enter degree name: ");
        String degreeName = scanner.nextLine();

        System.out.print("enter salary: ");
        double salary = Double.parseDouble(scanner.nextLine());

        if (salary <= 0){
            System.out.println("salary must be positive.");
            return;
        }

        Lecturer lecturer = new Lecturer(fullName, id, degree, degreeName, salary);

        try {
            collegeManager.addLecturer(lecturer);
            System.out.println("lecturer has been added successfully.");
        } catch (LecturerAlreadyExistsException e){
            System.out.println(e.getMessage());
        }
    }

    private static void addCommittee(Scanner scanner,CollegeManager collegeManager){
        System.out.print("enter committee name: ");
        String committeeName = scanner.nextLine();

        if (collegeManager.findCommitteeByName(committeeName) != null){
            System.out.println("name already exists. please enter another name. ");
            return;

        }

        System.out.print("enter id of chairperson: ");
        String chairpersonId = scanner.nextLine();

        Lecturer chairperson = collegeManager.findLecturerById(chairpersonId);

        if (chairperson == null){
            System.out.println("lecturer not found. please enter another id. ");
            return;
        }

        if (chairperson.getDegree().ordinal() < AcademicDegree.DR.ordinal()){
            System.out.println("chairperson has to be DR or above.");
            return;
        }

        System.out.print("enter academic degree for committee members(FIRST,SECOND,DR,PROF): ");
        String membersDegreeInput = scanner.nextLine().toUpperCase();
        AcademicDegree membersDegree = null;
        boolean validMembersDegree = false;


        for (AcademicDegree degree : AcademicDegree.values()){
            if (degree.name().equals(membersDegreeInput)){
                membersDegree = degree;
                validMembersDegree = true;
                break;
            }
        }
        if (!validMembersDegree){
            System.out.println("invalid degree. committee not added.");
            return;
        }

        Committee committee = new Committee(committeeName, chairperson, membersDegree);
        try {
            collegeManager.addCommittee(committee);
            System.out.println("committee has been added successfully.");
        } catch (CommitteeAlreadyExistsException | InvalidChairpersonException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void addLecturerToCommitteeFlow(Scanner scanner, CollegeManager collegeManager){
        System.out.print("enter committee name: ");
        String committeeName = scanner.nextLine();

        Committee committee = collegeManager.findCommitteeByName(committeeName);

        if (committee == null){
            System.out.println("committee not found. enter another name. ");
            return;
        }

        System.out.print("enter lecturer id: ");
        String lecturerId = scanner.nextLine();

        Lecturer lecturer = collegeManager.findLecturerById(lecturerId);

        if (lecturer == null){
            System.out.println("id not found. enter another id. ");
            return;
        }

        try {
            collegeManager.addLecturerToCommittee(lecturerId, committeeName);
            System.out.println("Lecturer has been added successfully.");
        } catch (LecturerNotFoundException | CommitteeNotFoundException | InvalidArgumentException e){
            System.out.println(e.getMessage());
        }

    }

    private static void updateChairpersonFlow(Scanner scanner, CollegeManager collegeManager){
        System.out.println("enter name of committee: ");
        String committeeName = scanner.nextLine();

        Committee committee = collegeManager.findCommitteeByName(committeeName);

        if (committee == null){
            System.out.println("committee not found. enter another name. ");
            return;

        }
        System.out.println("enter lecturer id: ");
        String lecturerId = scanner.nextLine();

        Lecturer lecturer = collegeManager.findLecturerById(lecturerId);

        if (lecturer == null){
            System.out.println("lecturer not found. enter another id. ");
            return;
        }

        try {
            collegeManager.updateCommitteeChairperson(committeeName, lecturerId);
            System.out.println("Chairperson updated successfully.");
        } catch (CommitteeNotFoundException | LecturerNotFoundException | InvalidChairpersonException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeLecturerFromCommitteeFlow(Scanner scanner, CollegeManager collegeManager){
        System.out.print("enter committee name: ");
        String committeeName = scanner.nextLine();

        Committee committee = collegeManager.findCommitteeByName(committeeName);

        if (committee == null){
            System.out.println("committee not found. please enter another name. ");
            return;
        }

        System.out.println("enter id of lecturer to remove: ");
        String lecturerId = scanner.nextLine();

        Lecturer lecturer = collegeManager.findLecturerById(lecturerId);

        if (lecturer == null){
            System.out.println("lecturer not found. please enter another id. ");
            return;
        }

        try {
            collegeManager.removeLecturerFromCommittee(committeeName, lecturerId);
            System.out.println("Lecturer removed successfully.");
        } catch (CommitteeNotFoundException | LecturerNotFoundException | LecturerNotInCommitteeException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void addDepartmentFlow(Scanner scanner, CollegeManager collegeManager){
        System.out.print("enter department name: ");
        String departmentName = scanner.nextLine();

        if (collegeManager.findDepartmentByName(departmentName) != null){
            System.out.println("department already exists. enter another name. ");
            return;
        }

        System.out.println("enter number of students in department: ");
        int numberOfStudents = Integer.parseInt(scanner.nextLine());

        if (numberOfStudents <= 0){
            System.out.println("number of students only above 0.");
            return;
        }

        Department department = new Department(departmentName,numberOfStudents);

        try {
            collegeManager.addDepartment(department);
            System.out.println("Department added successfully.");
        } catch (DepartmentAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showOverallAverageSalary(CollegeManager collegeManager){
        double average = collegeManager.getAverageSalaryAllLecturers();
        System.out.println("overall salary: " + average);
    }

    private static void showAverageSalaryByDepartment(Scanner scanner, CollegeManager collegeManager){
        System.out.println("enter department name: ");
        String departmentName = scanner.nextLine();

        Department department = collegeManager.findDepartmentByName(departmentName);

        if (department == null){
            System.out.println("department not found");
            return;
        }

        double average = collegeManager.getAverageSalaryByDepartment(departmentName);
        System.out.println("average salary in department - " + departmentName + " is: " + average);
    }

    private static void showAllLecturers(CollegeManager collegeManager){
        ArrayList<Lecturer> lecturers = collegeManager.getAllLecturersDetails();
        if (lecturers.isEmpty()){
            System.out.println("no lecturers found");
        }else{
            for (Lecturer lecturer : lecturers){
                System.out.println(lecturer);
            }
        }
    }

    private static void showAllCommittees(CollegeManager collegeManager){
        ArrayList<Committee> committees = collegeManager.getAllCommitteesDetails();
        if (committees.isEmpty()){
            System.out.println("no committees found");
        }else{
            for (Committee committee : committees){
                System.out.println(committee);
            }
        }
    }

    private static void duplicateCommitteeFlow(Scanner scanner, CollegeManager collegeManager) {
        System.out.print("Enter the name of the committee to duplicate: ");
        String originalName = scanner.nextLine();

        try {
            collegeManager.duplicateCommittee(originalName);
            System.out.println("Committee duplicated successfully.");
        } catch (CommitteeNotFoundException | CommitteeAlreadyExistsException | InvalidChairpersonException e) {
            System.out.println(e.getMessage());
        }

    }
}
