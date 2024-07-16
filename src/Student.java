/**
 * Represents a simple student class.
 * <p>
 * You do not need to change this class.
 */

public class Student {

    private long studentId;
    private long recordId;
    private int age;
    private String studentName;
    private String major;
    private String level;

    /**
     * Constructor
     * @param studentId 
     * @param age
     * @param studentName
     * @param major
     * @param level
     * @param recordId
     */
    public Student(long studentId, int age, String studentName, String major, String level, long recordId) {
        this.studentId = studentId;
        this.age = age;
        this.studentName = studentName;
        this.major = major;
        this.level = level;
        this.recordId = recordId;
    }

    public long getStudentId() {
        return this.studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}
