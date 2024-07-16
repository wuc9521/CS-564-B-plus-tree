import java.util.List;

public class BTreeNode {

    /**
     * Array of the keys stored in the node.
     */
    public long[] keys;
    /**
     * Array of the values[recordID] stored in the node. This will only be filled
     * when the node is a leaf node.
     */
    public long[] values;
    /**
     * Minimum degree (defines the range for number of keys)
     **/
    public int t;
    /**
     * Pointers to the children, if this node is not a leaf. If
     * this node is a leaf, then null.
     */
    public BTreeNode[] children;
    /**
     * number of key-value pairs in the B-tree
     */
    public int n;
    /**
     * true when node is leaf. Otherwise false
     */
    public boolean leaf;

    /**
     * point to other next node when it is a leaf node. Otherwise null
     */
    public BTreeNode next;

    /**
     * Constructor
     * 
     * @param t    minimum degree
     * @param leaf true when node is leaf. Otherwise false
     */
    public BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        this.keys = new long[2 * t - 1];
        this.children = new BTreeNode[2 * t];
        this.n = 0;
        this.next = null;
        this.values = new long[2 * t - 1];
    }

    /**
     * A function to search a key in the subtree rooted with this node.
     * 
     * @param listOfRecordID List of recordID
     */
    public void traverse(List<Long> listOfRecordID) {
        int i;
        for (i = 0; i < n; i++) {
            if (!leaf) {
                children[i].traverse(listOfRecordID);
            }
            if (leaf) {
                listOfRecordID.add(values[i]);
            }
        }
        if (!leaf) {
            children[i].traverse(listOfRecordID);
        }
    }

    /**
     * A function to search a key in the subtree rooted with this node.
     * 
     * @param student student to search
     */
    public void insertNonFull(Student student) {
        int i = n - 1;
        if (leaf) {
            while (i >= 0 && keys[i] > student.getStudentId()) {
                keys[i + 1] = keys[i];
                values[i + 1] = values[i];
                i--;
            }
            keys[i + 1] = student.getStudentId();
            values[i + 1] = student.getRecordId();
            n++;
        } else {
            while (i >= 0 && keys[i] > student.getStudentId()) {
                i--;
            }
            if (children[i + 1].n == 2 * t - 1) {
                splitChild(i + 1, children[i + 1]);
                if (keys[i + 1] < student.getStudentId()) {
                    i++;
                }
            }
            children[i + 1].insertNonFull(student);
        }
    }

    /**
     * A utility function to split the child y of this node
     * 
     * @param i index of the child to be split
     * @param y child to be split
     */
    public void splitChild(int i, BTreeNode y) {
        BTreeNode z = new BTreeNode(y.t, y.leaf);
        z.n = t - 1;
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
        }
        if (!y.leaf) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
            }
        }
        y.n = t - 1;
        for (int j = n; j >= i + 1; j--) {
            children[j + 1] = children[j];
        }
        children[i + 1] = z;
        for (int j = n - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
        }
        keys[i] = y.keys[t - 1];
        n++;
    }

    /**
     * A function to search a key in the subtree rooted with this node.
     * 
     * @param studentId studentId to search
     * @return recordID of the student
     */
    public long search(long studentId) {
        int i = 0;
        while (i < n && studentId > keys[i]) {
            i++;
        }
        if (keys[i] == studentId) {
            return values[i];
        }
        if (leaf) {
            return -1;
        }
        return children[i].search(studentId);
    }

    /**
     * A function that returns the recordID of the student
     * 
     * @param studentId studentId to search
     */
    public void delete(long studentId) {
        // TODO: Implement delete logic
    }

}
