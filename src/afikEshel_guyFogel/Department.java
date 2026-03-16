package afikEshel_guyFogel;

import java.io.Serializable;
import java.util.ArrayList;

public class Department implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int numOfStudents;
    private ArrayList<Lecturer> lecturers;

    public Department(String name, int numOfStudents){
        this.name = name;
        this.numOfStudents = numOfStudents;
        this.lecturers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getNumOfStudents() {
        return numOfStudents;
    }

    public ArrayList<Lecturer> getLecturers() {
        return lecturers;
    }

    public void addLecturer(Lecturer lecturer){
        if (!lecturers.contains(lecturer)){
            lecturers.add(lecturer);
        }
    }

    public void removeLecturer(Lecturer lecturer){
        lecturers.remove(lecturer);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Department other = (Department) obj;
        return this.name.equals(other.name);
    }

    public String toString(){
        StringBuilder lecturerStr = new StringBuilder();
        for (Lecturer lecturer : lecturers){
            lecturerStr.append(lecturer.getFullName()).append(" (Id: ").append(lecturer.getId()).append("), ");
        }
        if (lecturerStr.length() > 0){
            lecturerStr.setLength(lecturerStr.length() - 2);
        }
        return "department: " + name + ", number of students: " + numOfStudents +
                (lecturers.isEmpty() ? ", no lecturers in department."  : ", lecturers: " + lecturerStr);
    }
}
