import java.io.Serializable;

public class Student implements Serializable {
    private int studentId;
    private String studentCode;
    private String fullName;
    private int yearOfBirth;
    private String hometown;
    private float GPA;

    // Constructor, Getters, and Setters
    public Student(int studentId, String studentCode, String fullName, int yearOfBirth, String hometown, float GPA) {
        this.studentId = studentId;
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
        this.hometown = hometown;
        this.GPA = GPA;
    }

    public String getFullName() {
        return fullName;
    }

    public float getGPA() {
        return GPA;
    }

    // Override toString to display student information
    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentCode='" + studentCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                ", hometown='" + hometown + '\'' +
                ", GPA=" + GPA +
                '}';
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public void setGPA(float GPA) {
        this.GPA = GPA;
    }
}
