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
    public boolean isLeaf;

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
        this.isLeaf = leaf;
        this.keys = new long[2 * t];
        this.children = new BTreeNode[2 * t + 1];
        this.n = 0;
        this.next = null;
        this.values = new long[2 * t];
    }

    /**
     * A function to search a key in the subtree rooted with this node.
     * 
     * @param listOfRecordID List of recordID
     */
    public void traverse(List<Long> listOfRecordID) {
        int i;
        for (i = 0; i < n; i++) {
            if (!isLeaf) {
                children[i].traverse(listOfRecordID);
            }
            if (isLeaf) {
                listOfRecordID.add(values[i]);
            }
        }
        if (!isLeaf) {
            children[i].traverse(listOfRecordID);
        }
    }

    /**
     * A function to search a key in the subtree rooted with this node.
     * 
     * @param student student to search
     */
    public boolean insertNonFull(Student student) {
        int idx = this.n - 1;
        if (isLeaf) {
            // Find the location where new key should be inserted
            while (idx >= 0 && this.keys[idx] > student.getStudentId()) {
                this.keys[idx + 1] = this.keys[idx];
                this.values[idx + 1] = this.values[idx];
                idx--;
            }
            // Check if the key already exists
            if (idx >= 0 && this.keys[idx] == student.getStudentId()) {
                System.out.println("The studentId " + student.getStudentId() + " already exists in the tree.");
                return false;
            } else {
                // Insert the new key at found location
                this.keys[idx + 1] = student.getStudentId();
                this.values[idx + 1] = student.getRecordId();
                n++;
                return true;
            }
        } else {
            while (idx >= 0 && this.keys[idx] > student.getStudentId()) {
                idx--;
            }
            if (children[idx + 1].n == 2 * t) { // If the child is full, then split it
                this.splitChild(idx + 1, this.children[idx + 1]);
                if (this.keys[idx + 1] < student.getStudentId()) {
                    idx++;
                }
            }
            return this.children[idx + 1].insertNonFull(student);
        }
    }

    /**
     * A utility function to split the child y of this node
     * 
     * @param idx index of the child to be split
     * @param node child to be split
     */
    public void splitChild(int idx, BTreeNode node) {
        BTreeNode newNode = new BTreeNode(node.t, node.isLeaf);
        newNode.n = t - 1;
        for (int j = 0; j < t - 1; j++) {
            newNode.keys[j] = node.keys[j + t];
        }
        if (!node.isLeaf) {
            for (int j = 0; j < t; j++) {
                newNode.children[j] = node.children[j + t];
            }
        }
        node.n = t - 1;
        for (int j = n; j >= idx + 1; j--) {
            children[j + 1] = children[j];
        }
        children[idx + 1] = newNode;
        for (int j = n - 1; j >= idx; j--) {
            keys[j + 1] = keys[j];
        }
        keys[idx] = node.keys[t - 1];
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
        if (this.keys[i] == studentId) {
            return this.values[i];
        }
        if (this.isLeaf) {
            return -1;
        }
        return this.children[i].search(studentId);
    }

    /**
     * A function to find the first key greater than or equal to a given key
     * 
     * @param key the key to find
     * @return the index of the first key greater than or equal to k
     */
    private int findKey(long key) {
        int start = 0;
        int end = n - 1;

        while (start <= end) {
            int mid = (start + end) / 2;
            if (keys[mid] < key) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        return start;
    }

    /**
     * Deletes a studentId from the subtree rooted with this node.
     * 
     * @param studentId the studentId to delete
     */
    public void delete(long studentId) {
        int idx = findKey(studentId);

        if (idx < n && keys[idx] == studentId) {
            if (isLeaf) {
                this.removeFromLeaf(idx);
            } else {
                this.removeFromNonLeaf(idx);
            }
        } else {
            if (isLeaf) {
                System.out.println("The studentId " + studentId + " does not exist in the tree.");
                return;
            }
            // check if the key is present in the sub-tree rooted at the last child of this
            // node
            boolean flag = ((idx == n) ? true : false);
            if (children[idx].n < t) {
                this.fill(idx);
            }
            if (flag && idx > n) {
                children[idx - 1].delete(studentId);
            } else {
                children[idx].delete(studentId);
            }
        }
    }

    /**
     * Method to remove the key from this node - which is a leaf node
     * 
     * @param idx the index of the key to remove
     */
    private void removeFromLeaf(int idx) {
        for (int i = idx + 1; i < n; i++) {
            keys[i - 1] = keys[i];
            values[i - 1] = values[i];
        }
        this.n--;
    }

    /**
     * Method to remove the key from this node - which is a non-leaf node
     * 
     * @param idx the index of the key to remove
     */
    private void removeFromNonLeaf(int idx) {
        long k = keys[idx];

        // If the child that precedes k (children[idx]) has atleast t keys,
        // find the predecessor 'pred' of k in the subtree rooted at children[idx].
        // Replace k by pred. Recursively delete pred in children[idx].
        if (children[idx].n >= t) {
            BTreeNode pred = this.prefOf(idx);
            keys[idx] = pred.keys[pred.n - 1];
            values[idx] = pred.values[pred.n - 1];
            children[idx].delete(pred.keys[pred.n - 1]);
        }
        // If children[idx] has less that t keys, examine children[idx+1].
        // If children[idx+1] has atleast t keys, find the successor 'succ' of k in
        // the subtree rooted at children[idx+1]. Replace k by succ.
        // Recursively delete succ in children[idx+1].
        else if (children[idx + 1].n >= t) {
            BTreeNode succ = this.succOf(idx);
            keys[idx] = succ.keys[0];
            values[idx] = succ.values[0];
            children[idx + 1].delete(succ.keys[0]);
        }
        // If both children[idx] and children[idx+1] has less than t keys,merge k and
        // all of children[idx+1]
        // into children[idx]. Now children[idx] contains 2t-1 keys.
        // Recursively delete k from children[idx].
        else {
            this.merge(idx);
            children[idx].delete(k);
        }
    }

    /**
     * A function to get predecessor of keys[idx]
     * 
     * @param idx the index of the key
     * @return the predecessor of keys[idx]
     */
    private BTreeNode prefOf(int idx) {
        // Keep moving to the right most node until we reach a leaf
        BTreeNode cur = children[idx];
        while (!cur.isLeaf) {
            cur = cur.children[cur.n];
        }
        return cur;
    }

    /**
     * A function to get successor of keys[idx]
     * 
     * @param idx the index of the key
     * @return the successor of keys[idx]
     */
    private BTreeNode succOf(int idx) {
        // Keep moving the left most node starting from children[idx+1] until we reach a
        // leaf
        BTreeNode cur = children[idx + 1];
        while (!cur.isLeaf) {
            cur = cur.children[0];
        }
        return cur;
    }

    /**
     * A function to fill up the child node present at idx which has less than t-1
     * keys
     * 
     * @param idx the index of the child node to fill up
     */
    private void fill(int idx) {
        // If the previous child(children[idx-1]) has more than t-1 keys, borrow a key
        // from that child
        if (idx != 0 && children[idx - 1].n >= t) {
            this.borrowPrev(idx);
        }
        // If the next child(children[idx+1]) has more than t-1 keys, borrow a key from
        // that child
        else if (idx != n && children[idx + 1].n >= t) {
            borrowNext(idx);
        }
        // Merge children[idx] with its sibling
        // If children[idx] is the last child, merge it with with its previous sibling
        // Otherwise merge it with its next sibling
        else {
            if (idx != n) {
                this.merge(idx);
            } else {
                this.merge(idx - 1);
            }
        }
    }

    /**
     * A function to borrow a key from the children[idx-1]th node and place
     * 
     * @param idx the index of the child node to borrow from
     */
    private void borrowPrev(int idx) {
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx - 1];
        for (int i = child.n - 1; i >= 0; --i) {
            child.keys[i + 1] = child.keys[i];
        }

        if (!child.isLeaf) {
            for (int i = child.n; i >= 0; --i) {
                child.children[i + 1] = child.children[i];
            }
        }

        // Setting child's first key equal to keys[idx-1] from the current node
        child.keys[0] = keys[idx - 1];

        if (!child.isLeaf) {
            child.children[0] = sibling.children[sibling.n];
        }

        // Moving sibling's last key to parent
        keys[idx - 1] = sibling.keys[sibling.n - 1];

        child.n += 1;
        sibling.n -= 1;
    }

    /**
     * A function to borrow a key from the children[idx+1]th node and place it in
     * children[idx]
     * 
     * @param idx the index of the child node to borrow from
     */
    private void borrowNext(int idx) {
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx + 1];

        // keys[idx] is inserted as the last key in children[idx]
        child.keys[child.n] = keys[idx];
        if (!child.isLeaf) {
            child.children[child.n + 1] = sibling.children[0];
        }

        // Sibling's first key is inserted into keys[idx]
        keys[idx] = sibling.keys[0];

        // Moving all keys in sibling one step behind
        for (int i = 1; i < sibling.n; ++i) {
            sibling.keys[i - 1] = sibling.keys[i];
        }

        if (!sibling.isLeaf) {
            for (int i = 1; i <= sibling.n; ++i) {
                sibling.children[i - 1] = sibling.children[i];
            }
        }

        // Increasing and decreasing the key count of children[idx] and children[idx+1]
        // respectively
        child.n += 1;
        sibling.n -= 1;
    }

    /**
     * A function to merge children[idx] with children[idx+1]
     * 
     * @param idx the index of the child node to merge
     */
    private void merge(int idx) {
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx + 1];

        // Pulling a key from the current node and inserting it into (t-1)th
        // position of children[idx]
        child.keys[t - 1] = keys[idx];

        // Copying the keys from children[idx+1] to children[idx] at the end
        for (int i = 0; i < sibling.n; ++i) {
            child.keys[i + t] = sibling.keys[i];
        }

        // Copying the child pointers from children[idx+1] to children[idx]
        if (!child.isLeaf) {
            for (int i = 0; i <= sibling.n; ++i) {
                child.children[i + t] = sibling.children[i];
            }
        }

        // Moving all keys after idx in the current node one step before -
        // to fill the gap created by moving keys[idx] to children[idx]
        for (int i = idx + 1; i < n; ++i) {
            keys[i - 1] = keys[i];
        }

        // Moving the child pointers after (idx+1) in the current node one
        // step before
        for (int i = idx + 2; i <= n; ++i) {
            children[i - 1] = children[i];
        }

        // Updating the key count of child and the current node
        child.n += sibling.n + 1;
        n--;

        // Freeing the memory occupied by sibling
        sibling = null;
    }

    /**
     * Function to print all the keys in a subtree rooted with this node
     * 
     * @param prefix Prefix to be printed
     * @param isTail true if the current node is the last child of the parent node
     */
    public void print(String prefix, boolean isTail) {
        // Print the current node's keys
        System.out.print(prefix + (isTail ? "└── " : "├── "));

        for (int i = 0; i < n; i++) {
            System.out.print(keys[i]);
            if (isLeaf) {
                System.out.print(" (" + values[i] + ")");
            }
            if (i < n - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        // Recursively print each child
        if (!isLeaf) {
            for (int i = 0; i <= n; i++) {
                children[i].print(prefix + (isTail ? "    " : "│   "), i == n);
            }
        }
    }
}