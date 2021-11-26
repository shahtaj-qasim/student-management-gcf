package Models;

public class Student {
    private String firstName;
    private String lastName;
    private int studentNumber;
    private String dob;
    private String email;
    private int semester;
    private String degree;
    private String address;

    public Student(String firstName, String lastName, int studentNumber, String dob, String email, int semester, String degree, String  address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentNumber =studentNumber;
        this.dob = dob;
        this.email = email;
        this.semester = semester;
        this.degree = degree;
        this.address = address;
    }
    public Student(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
