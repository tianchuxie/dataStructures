import java.util.Stack;

public class BST {
	
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode (int x){
			this.val = x;
		}
	}
	
	Stack<TreeNode> stacks;
	public BST(TreeNode root){
		stacks = new Stack<TreeNode>();
		while (root != null){
			stacks.push(root);
			root = root.left;
		}
		
	}
	
	public boolean hasNext(){
		return ! stacks.isEmpty();
	}
	
	public int next(){
		TreeNode current = stacks.pop();
		int result = current.val;
		if (current.right != null){
			current = current.right;
			while (current != null){
				stacks.push(current);
				current = current.left;
			}
		} 
		return result;
	}
	

}
