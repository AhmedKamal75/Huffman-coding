import java.util.ArrayList;
import java.util.Map;

public class State implements Comparable<State> {
    private State left;
    private State right;
    private final ArrayList<Byte> sequence;
    private final int count;

    public State(ArrayList<Byte> sequence, int count) {
        this.sequence = sequence;
        this.count = count;
    }

    public State(State left, State right, ArrayList<Byte> sequence, int count) {
        this.left = left;
        this.right = right;
        this.sequence = sequence;
        this.count = count;
    }


    public int getCount() {
        return count;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public int compareTo(State other) {
        return this.count - other.count;
    }

    public void buildCode(String code, Map<ArrayList<Byte>, String> map) {
        if (isLeaf()) {
            map.put(this.sequence, code);
        } else {
            this.left.buildCode(code + '0', map);
            this.right.buildCode(code + '1', map);
        }
    }

    public void printTree(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + sequence + ":" + count);
        if (right != null) {
            right.printTree(prefix + (isTail ? "    " : "│   "), left == null);
        }
        if (left != null) {
            left.printTree(prefix + (isTail ? "    " : "│   "), true);
        }
    }

    @Override
    public String toString() {
        return "[" + right + ", " + left + ", " + sequence + ", " + count + ']';
    }
}
