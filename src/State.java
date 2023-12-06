import java.util.Map;

public class State implements Comparable<State> {
    private State left;
    private State right;
    private final char character;
    private final int count;

    public State(char character, int count) {
        this.character = character;
        this.count = count;
    }

    public State(State left, State right, char character, int count) {
        this.left = left;
        this.right = right;
        this.character = character;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    @Override
    public int compareTo(State other) {
        return this.count - other.count;
    }

    public void buildCode(String code, Map<Character, String> map) {
        if (isLeaf()) {
            map.put(character, code);
        } else {
            left.buildCode(code + '0', map);
            right.buildCode(code + '1', map);
        }
    }

    public void printTree(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + character + ":" + count);
        if (right != null) {
            right.printTree(prefix + (isTail ? "    " : "│   "), left == null);
        }
        if (left != null) {
            left.printTree(prefix + (isTail ? "    " : "│   "), true);
        }
    }

    @Override
    public String toString() {
        return "[" + right + ", " + left + ", " + character + ", " + count + ']';
    }
}
