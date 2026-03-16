package afikEshel_guyFogel;

import java.io.*;
import java.util.ArrayList;

public class CollegeManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private String collegeName;
    private ArrayList<Lecturer> lecturers;
    private ArrayList<Department> departments;
    private ArrayList<Committee> committees;

    public CollegeManager(String collegeName){
        this.collegeName = collegeName;
        this.lecturers = new ArrayList<>();
        this.departments = new  ArrayList<>();
        this.committees = new  ArrayList<>();

    }

    public String getCollegeName() {
        return collegeName;
    }

    public void addLecturer(Lecturer lecturer) throws LecturerAlreadyExistsException {
        for (Lecturer lecturer_check : lecturers){
            if (lecturer_check.getId().equals(lecturer.getId())){
                throw new LecturerAlreadyExistsException("Lecturer with ID " + lecturer.getId() + "already exits.");
            }
        }
        lecturers.add(lecturer);
    }

    public Lecturer findLecturerById(String id){
        for(Lecturer lecturer : lecturers){
            if(lecturer.getId().equals(id)){
                return lecturer;
            }
        }
        return null;
    }

    public void addDepartment(Department department) throws DepartmentAlreadyExistsException {
        for (Department department_check : departments){
            if (department_check.getName().equals(department.getName())){
                throw new DepartmentAlreadyExistsException("Department: " + department.getName() + " already exists." );
            }
        }
        departments.add(department);
    }

    public Department findDepartmentByName(String name){
        for(Department department : departments){
            if(department.getName().equals(name)){
                return department;
            }
        }
        return null;
    }

    public void addCommittee(Committee committee) throws CommitteeAlreadyExistsException,InvalidChairpersonException {
        for (Committee committee_check : committees){
            if (committee_check.getName().equals(committee.getName())){
                throw new CommitteeAlreadyExistsException("Committee: " + committee.getName() + "already exists.");
            }
        }
        if (committee.getChairperson().getDegree().ordinal() < AcademicDegree.DR.ordinal()){
            throw new InvalidChairpersonException("chairperson must be DR or above.");
        }
        committees.add(committee);
    }

    public Committee findCommitteeByName(String name){
        for(Committee committee : committees){
            if(committee.getName().equals(name)){
                return committee;
            }
        }
        return null;
    }

    public void addLecturerToCommittee(String lecturerId, String committeeName) throws LecturerNotFoundException, CommitteeNotFoundException, InvalidArgumentException {
        Lecturer lecturer = findLecturerById(lecturerId);
        Committee committee = findCommitteeByName(committeeName);

        if (lecturer == null) {
            throw new LecturerNotFoundException("Lecturer with ID " + lecturerId + " not found.");
        }

        if (committee == null) {
            throw new CommitteeNotFoundException("Committee '" + committeeName + "' not found.");
        }

        if (lecturer.getDegree() != committee.getMembersDegree()){
            throw new InvalidArgumentException("Lecturer degree does not match committee's required degree");
        }

        committee.addMember(lecturer);
        lecturer.addCommittee(committee);

    }
    public void updateCommitteeChairperson(String committeeName,String lecturerId) throws CommitteeNotFoundException,LecturerNotFoundException,InvalidChairpersonException {
        Committee committee = findCommitteeByName(committeeName);
        Lecturer lecturer = findLecturerById(lecturerId);

        if (committee == null) {
            throw new CommitteeNotFoundException("Committee '" + committeeName + "' not found.");
        }
        if (lecturer == null) {
            throw new LecturerNotFoundException("Lecturer with ID " + lecturerId + " not found.");
        }
        committee.setChairperson(lecturer);
    }
    public void removeLecturerFromCommittee(String committeeName, String lecturerId) throws CommitteeNotFoundException,LecturerNotFoundException,LecturerNotInCommitteeException {
        Committee committee = findCommitteeByName(committeeName);
        Lecturer lecturer = findLecturerById(lecturerId);

        if (committee == null) {
            throw new CommitteeNotFoundException("Committee '" + committeeName + "' not found.");
        }

        if (lecturer == null) {
            throw new LecturerNotFoundException("Lecturer with ID " + lecturerId + " not found.");
        }

        boolean removed = committee.removeMember(lecturer);
        if (!removed) {
            throw new LecturerNotInCommitteeException("Lecturer is not a member of the committee.");
        }

        lecturer.removeCommittee(committee);
        committee.removeMember(lecturer);
    }

    public double getAverageSalaryAllLecturers(){
        if (lecturers.isEmpty()){
            return 0.0;
        }
        double total = 0;
        for (Lecturer lecturer : lecturers){
            total += lecturer.getSalary();
        }
        return total / lecturers.size();
    }
    public double getAverageSalaryByDepartment(String departmentName){
        Department department = findDepartmentByName(departmentName);
        if (department == null || department.getLecturers().isEmpty()){
            return 0.0;
        }

        double total = 0;
        for(Lecturer lecturer : department.getLecturers()){
            total += lecturer.getSalary();
        }
        return total/department.getLecturers().size();
    }
    public ArrayList<Lecturer> getAllLecturersDetails(){
        return new ArrayList<>(lecturers);
    }
    public ArrayList<Committee> getAllCommitteesDetails(){
        return new ArrayList<>(committees);
    }

    public void duplicateCommittee(String originalName)
            throws CommitteeNotFoundException, CommitteeAlreadyExistsException, InvalidChairpersonException {

        Committee original = findCommitteeByName(originalName);
        if (original == null) {
            throw new CommitteeNotFoundException("Committee '" + originalName + "' not found.");
        }

        String newName = "new-" + originalName;
        if (findCommitteeByName(newName) != null) {
            throw new CommitteeAlreadyExistsException("A committee named '" + newName + "' already exists.");
        }

        Committee copy = original.duplicate();
        addCommittee(copy);
    }

    public void saveToFile(String fileName){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))){
            out.writeObject(this);
            System.out.println("Data saved successfully to " + fileName);
        } catch (IOException e){
            System.out.println("Failed to save data: " + e.getMessage());
        }
    }

    public static CollegeManager loadFromFile(String fileName){
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (CollegeManager) in.readObject();
        } catch (IOException | ClassNotFoundException e){
            System.out.println("No previous data loaded: " + e.getMessage());
            return null;
        }
    }
}
