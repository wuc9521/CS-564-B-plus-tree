import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * Main Application.
 */
public class BTreeMain {

    public static void main(String[] args) {

        /** Read the input file -- input.txt */
        Scanner scan = null;
        try {
            scan = new Scanner(new File("src/input.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

        int degree = scan.nextInt(); // Read the minimum degree of B+Tree first
        BTree bTree = new BTree(degree);

        /** Reading the database student.csv into B+Tree Node */
        List<Student> studentsDB = getStudents();
        for (Student s : studentsDB) {
            bTree.insert(s);
        }

        /** Start reading the operations now from input file */
        try {
            while (scan.hasNextLine()) {
                Scanner s2 = new Scanner(scan.nextLine());
                while (s2.hasNext()) {
                    String operation = s2.next();
                    switch (operation) {
                        case "insert":
                            long studentId = Long.parseLong(s2.next());
                            String studentName = s2.next() + " " + s2.next();
                            String major = s2.next();
                            String level = s2.next();
                            int age = Integer.parseInt(s2.next());
                            long recordID = s2.hasNext() ? Long.parseLong(s2.next()) : generateRecordID();
                            Student s = new Student(studentId, age, studentName, major, level, recordID);
                            if(bTree.insert(s) != null) {
                                writeBackToDB(s);
                            }
                            break;
                        case "delete":
                            studentId = Long.parseLong(s2.next());
                            if (bTree.delete(studentId)) {
                                System.out.println("Student deleted successfully.");
                            }
                            else {
                                System.out.println("Student deletion failed.");
                            }
                            break;
                        case "search":
                            studentId = Long.parseLong(s2.next());
                            long recordIDSearch = bTree.search(studentId);
                            if (recordIDSearch != -1){
                                System.out.println("Student exists in the database at " + recordIDSearch);
                            }
                            else {
                                System.out.println("Student does not exist.");
                            }
                            break;
                        case "print":
                            List<Long> listOfRecordID = bTree.print();
                            System.out.println("List of recordIDs in B+Tree " + listOfRecordID.toString());
                            break;
                        default:
                            System.out.println("Wrong Operation");
                            break;
                    }
                }
                s2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the student.csv file and return the list of students.
     * 
     * @return list of students
     */
    private static List<Student> getStudents() {
        Scanner scan = null;
        try {
            scan = new Scanner(new File("src/Student.csv"));
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(0);
        }
        List<Student> studentList = new ArrayList<>();
        assert scan != null;
        while (scan.hasNextLine()) {
            String[] studentInfo = scan.nextLine().split(",");
            long studentId = Long.parseLong(studentInfo[0]);
            int age = Integer.parseInt(studentInfo[4]);
            String studentName = studentInfo[1];
            String major = studentInfo[2];
            String level = studentInfo[3];
            long recordID = Long.parseLong(studentInfo[5]);
            studentList.add(
                    new Student(
                            studentId,
                            age,
                            studentName,
                            major,
                            level,
                            recordID));
        }
        scan.close();
        return studentList;
    }

    /**
     * Generate a unique recordID for a student.
     * @return recordID
     */
    private static long generateRecordID() {
        Set<Long> existingRecordIDs = new HashSet<>();
        for (Student student : getStudents()) {
            existingRecordIDs.add(student.getRecordId());
        }

        Random random = new Random();
        long recordID;
        do {
            recordID = random.nextLong();
        } while (existingRecordIDs.contains(recordID) || recordID <= 0);

        return recordID;
    }

    /**
     * Write back the student to the database.
     * @param student student to write back
     */
    private static void writeBackToDB(Student student) {
        // Write back to the database
        File file = new File("src/Student.csv");
        try {
            java.io.FileWriter fileWriter = new java.io.FileWriter(file, true);
            fileWriter.write(student.getStudentId() + "," + student.getStudentName() + "," + student.getMajor() + ","
                    + student.getLevel() + "," + student.getAge() + "," + student.getRecordId() + "\n");
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
