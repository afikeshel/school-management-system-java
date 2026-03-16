package afikEshel_guyFogel;

public class Professor extends Lecturer {

    private  String grantingInstitution;

    public Professor(String fullName, String id, AcademicDegree degree, String degreeName, double salary, String grantingInstitution) {
        super(fullName, id, degree, degreeName, salary);
        this.grantingInstitution = grantingInstitution;
    }

    public String getGrantingInstitution() {
        return grantingInstitution;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Professor other = (Professor) obj;
        return this.grantingInstitution.equals(other.grantingInstitution);
    }

    public String toString() {
        return super.toString() + ", granted by: " + grantingInstitution;
    }
}
