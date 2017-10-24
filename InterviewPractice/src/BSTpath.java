import java.awt.List;
import java.util.ArrayList;

import BST.TreeNode;


public class BSTpath {
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode (int x){
			this.val = x;
		}
	}
	public List<String> binaryTreePaths(TreeNode root) {
        List<String> paths = new ArrayList<>();
        if (root == null) {
            return paths;
        }
        
        List<String> leftPaths = binaryTreePaths(root.left);
        List<String> rightPaths = binaryTreePaths(root.right);
        for (String path : leftPaths) {
            paths.add(root.val + "->" + path);
        }
        for (String path : rightPaths) {
            paths.add(root.val + "->" + path);
        }
        
        // root is a leaf
        if (paths.size() == 0) {
            paths.add("" + root.val);
        }
        
        return paths;
    }
	
	/*
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<String>();
        if (root == null) {
            return result;
        }
        helper(root, String.valueOf(root.val), result);
        return result;
    }
    
    private void helper(TreeNode root, String path, List<String> result) {
        if (root == null) {
            return;
        }
        
        if (root.left == null && root.right == null) {
            result.add(path);
            return;
        }
        
        if (root.left != null) {
            helper(root.left, path + "->" + String.valueOf(root.left.val), result);
        }
        
        if (root.right != null) {
            helper(root.right, path + "->" + String.valueOf(root.right.val), result);
        }
    }
}*/
}
