package afikEshel_guyFogel;

import java.io.Serializable;
import java.util.ArrayList;

public class Committee implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Lecturer chairperson;
    private ArrayList<Lecturer> members;
    private AcademicDegree membersDegree;

    public Committee(String name, Lecturer chairperson, AcademicDegree membersDegree){
        this.name = name;
        this.chairperson = chairperson;
        this.members = new ArrayList<>();
        this.membersDegree = membersDegree;
    }

    public Committee duplicate() {
        String newName = "new-" + name;
        Committee copy = new Committee(newName, chairperson, membersDegree);

        for (Lecturer member : members) {
            copy.addMember(member);
            member.addCommittee(copy);
        }
        return copy;
    }

    public String getName() {
        return name;
    }

    public Lecturer getChairperson(){
        return chairperson;
    }

    public int getNumOfMembers() {
        return members.size();
    }

    public AcademicDegree getMembersDegree() {
        return membersDegree;
    }

    public int getTotalArticlesByMembers() {
        int sum = 0;
        for (Lecturer member : members) {
            if (member.getDegree().ordinal() >= AcademicDegree.DR.ordinal()) {
                sum += member.getNumOfArticles();
            }
        }
        return sum;
    }

    public void addMember(Lecturer lecturer){
        if (lecturer.equals(chairperson)){
            return;
        }

        if(members.contains(lecturer)){
            return;
        }
        members.add(lecturer);

    }

    public boolean removeMember(Lecturer lecturer){
        if (lecturer.equals(chairperson)){
            return false;
        }
        return members.remove(lecturer);
    }

    public void setChairperson(Lecturer chairperson) throws  InvalidChairpersonException {
        if (chairperson.getDegree().ordinal() < AcademicDegree.DR.ordinal()){
            throw new InvalidChairpersonException("Chairperson must be DR or above");
        }
        this.chairperson = chairperson;
        removeMember(chairperson);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Committee other = (Committee) obj;
        return this.name.equals(other.name);
    }

    public String toString(){
        StringBuilder membersStr = new StringBuilder();
        for (Lecturer member : members){
            membersStr.append(member.getFullName()).append(" (Id: ").append(member.getId()).append("), ");
        }
        if (membersStr.length() > 0){
            membersStr.setLength(membersStr.length()-2);
        }
        return "committee: " + name + ", chairman: " + chairperson.getFullName() + "(Id: " + chairperson.getId() + ")" +
                (members.isEmpty() ? ", no members in committee." : ", members: " + membersStr );
    }
}
