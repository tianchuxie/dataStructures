import java.util.ArrayList;
import java.util.Arrays;

public class numberOfIsland {
	
	
	/*
	public int numIslands(char[][] grid) {
        int count = 0;
        if (grid == null ||grid.length == 0)
            return count;
        int height = grid.length;
        int wid = grid[0].length;
        
        boolean[][] check = new boolean[height][wid];
        for (int i = 0; i < height; i++){
            for (int j = 0; j < wid; j++){
                check[i][j] = false;
            }
        }
        for (int i = 0; i < height; i++){
            for (int j = 0; j < wid; j++){
                if (!check[i][j]){
                    count += helper (grid, grid[i][j], i, j, check);
                }
            }
        }
        
        return count;
        
    }
    
    public int helper(char[][] grid, char k, int kx, int ky, boolean[][] check){
        ArrayList<Integer> hs = new ArrayList<Integer> ();
        ArrayList<Integer> ws = new ArrayList<Integer> ();
        hs.add(kx);
        ws.add(ky);
        while (!hs.isEmpty()){
            ArrayList<Integer> nhs = new ArrayList<Integer> ();
            ArrayList<Integer> nws = new ArrayList<Integer> ();
            for (int i = 0; i < hs.size(); i++){
                int x = hs.get(i);
                int y = ws.get(i);
                if ((check[x][y] == false) && (grid[x][y] == k)){
                    check[x][y] = true;
                    if (x + 1 < grid.length && (check[x+1][y] == false) && grid[x+1][y] == k){
                        nhs.add(x+1);
                        nws.add(y);
                    } 
                    if (x - 1 >= 0 && check[x-1][y] == false && grid[x-1][y] == k){
                        nhs.add(x-1);
                        nws.add(y);
                    }
                    if (y + 1 < grid[0].length && check[x][y+1] == false && grid[x][y+1] == k){
                        nhs.add(x);
                        nws.add(y+1);
                    }
                    if (y -1 >= 0 && check[x][y-1] == false && grid[x][y-1] == k){
                        nhs.add(x);
                        nws.add(y-1);
                    }
                  
                }
                hs = nhs;
                ws = nws;
            } 
        }
        
        if (k == '0'){
            return 0;
        } else {
            return 1;
        }
        
        *That's my tested solution */
	
	
	public class Solution {

		private int n;
		private int m;

		public int numIslands(char[][] grid) {
		    int count = 0;
		    n = grid.length;
		    if (n == 0) return 0;
		    m = grid[0].length;
		    for (int i = 0; i < n; i++){
		        for (int j = 0; j < m; j++)
		            if (grid[i][j] == '1') {
		                DFSMarking(grid, i, j);
		                ++count;
		            }
		    }    
		    return count;
		}

		private void DFSMarking(char[][] grid, int i, int j) {
		    if (i < 0 || j < 0 || i >= n || j >= m || grid[i][j] != '1') return;
		    grid[i][j] = '0';
		    DFSMarking(grid, i + 1, j);
		    DFSMarking(grid, i - 1, j);
		    DFSMarking(grid, i, j + 1);
		    DFSMarking(grid, i, j - 1);
		}
	
	
	
	
	
        
        
        
    }
   /* 
    public static void main(String[] args) {
    	char[][] grid = null;
    	["11000","11000","100100","00011"]
    	grid[0] = "11000".toCharArray();
    	
    	grid[1] = "11000".toCharArray();
    	grid[2] = "11000".toCharArray();
    	grid[3] = "11000".toCharArray();
    	
    	
    }
    */
    
    
}
