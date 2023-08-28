import java.util.Comparator;

public class Node {
	char character;
    int frequency;
    Node left = null;
    Node right = null;
    
    public Node() {
    	
    }
    
    public Node(char character, int frequency, Node left, Node right) {
        this.character = character;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
    
    public boolean isLeaf() {
    	return (this.left == null && this.right == null);
    }
  
}
