import java.util.ArrayList;
import java.util.List;

/**
 * B+Tree Structure
 * Key - StudentId
 * Leaf Node should contain [ key,recordId ]
 */
public class BTree {

    /**
     * Pointer to the root node.
     */
    private BTreeNode root;

    /**
     * Number of key-value pairs allowed in the tree/the minimum degree of B+Tree
     **/
    private int t;

    /**
     * Constructor
     * 
     * @param t minimum degree of B+Tree
     */
    public BTree(int t) {
        this.root = null;
        this.t = t;
    }

    /**
     * Search the studentId in the B+Tree
     * 
     * @param studentId studentId to search
     * @return recordID for the given StudentID. Otherwise, -1
     */
    public long search(long studentId) {
        if (this.root == null) {
            System.out.println("The studentId " + studentId + " has not been found in the table.");
            return -1;
        }
        return this.root.search(studentId);
    }


    /**
     * Insert a new student in the B+Tree
     * 
     * @param student student to insert
     * @return B+Tree after inserting the student
     */
    public BTree insert(Student student) {
        if (this.root == null) { // Tree is empty
            this.root = new BTreeNode(t, true);
            this.root.keys[0] = student.getStudentId();
            this.root.values[0] = student.getRecordId();
            this.root.n = 1;
            return this;
        } 
        // Tree is not empty
        if (this.root.n == 2 * t) { // root is full, then tree grows in height
            BTreeNode s = new BTreeNode(t, false);
            s.children[0] = this.root;
            s.splitChild(0, this.root);
            int i = 0;
            if (s.keys[0] < student.getStudentId()) {
                i++;
            }
            s.children[i].insertNonFull(student);
            this.root = s;
            return this;
        } 
        // tree is not empty and root is not full
        this.root.insertNonFull(student);
        return this;
    }

    /**
     * Delete a student from the B+Tree
     * 
     * @param studentId studentId to delete
     * @return true if the student is deleted successfully otherwise, return false.
     */
    public boolean delete(long studentId) {
        if (this.root == null) {
            System.out.println("The tree is empty");
            return false;
        }
        this.root.delete(studentId);
        if (this.root.n == 0) {
            if (!this.root.isLeaf) {
                this.root = this.root.children[0];
            } else {
                this.root = null;
            }
        }
        return true;
    }

    /**
     * Print the B+Tree
     * 
     * @return a list of recordIDs from left to right of leaf nodes.
     */
    public List<Long> print() {
        List<Long> listOfRecordID = new ArrayList<>();
        if (this.root != null) {
            this.root.traverse(listOfRecordID);
        }
        return listOfRecordID;
    }
}
