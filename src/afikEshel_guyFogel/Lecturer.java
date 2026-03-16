// afik eshel 209512961 , guy fogel
package afikEshel_guyFogel;

import java.io.Serializable;
import java.util.ArrayList;

public class Lecturer implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fullName;
    private String id;
    private AcademicDegree degree;
    private String degreeName;
    private double salary;
    private Department department;
    private ArrayList<Committee> committees;
    private ArrayList<String> articles;


    public Lecturer(String fullName, String id , AcademicDegree degree,String degreeName, double salary){
        this.fullName = fullName;
        this.id = id;
        this.degree = degree;
        this.salary = salary;
        this.department = null;
        this.committees = new ArrayList<>();
        this.degreeName = degreeName;
        this.articles = new ArrayList<>();
    }
    // getters


    public String getFullName() {
        return fullName;
    }

    public String getId() {
        return id;
    }

    public AcademicDegree getDegree() {
        return degree;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public double getSalary() {
        return salary;
    }

    public Department getDepartment() {
        return department;
    }


    public ArrayList<Committee> getCommittees() {
         return new ArrayList<>(committees);


    }

    public int getNumOfArticles() {
        return articles.size();
    }

    // setters
    public void setDepartment(Department department){
        this.department = department;

    }
    public void addCommittee(Committee committee){
        if (!committees.contains(committee)){
            committees.add(committee);
        }

    }

    public void removeCommittee(Committee committee){
        committees.remove(committee);
    }

    public void addArticles(String articlesTitle){
        articles.add(articlesTitle);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Lecturer other = (Lecturer) obj;
        return this.id.equals(other.id);
    }

    public String toString(){
        StringBuilder committeesStr = new StringBuilder();
        for (Committee committee : committees){
            committeesStr.append(committee.getName()).append(", ");
        }
        if (committeesStr.length() > 0){
            committeesStr.setLength(committeesStr.length() - 2);
        }
        return degree + " " + fullName + "(ID:" + id + "), salary: " + salary + ", degree name: " + degreeName +
                (department != null ? ", department: " + department.getName() : "") +
                (committees.isEmpty() ? ", committees: none" : ", committees: " + committeesStr);

    }
}
