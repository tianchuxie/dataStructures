import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class wordBreak {
	public boolean wordBreak(String s, Set<String> wordDict) {
	    int[] pos = new int[s.length()+1];
	 
	    Arrays.fill(pos, -1);
	    
	    pos[0]=0;
	 
	    for(int i=0; i<s.length(); i++){
	        if(pos[i]!=-1){
	            for(int j=i+1; j<=s.length(); j++){
	                String sub = s.substring(i, j);
	                if(wordDict.contains(sub)){
	                    pos[j]=i;
	                }
	            } 
	        }
	    }
	 
	    return pos[s.length()]!=-1;
	}
	
	
	
	
	public static ArrayList<Integer> maxSubArraySum(int[] a, int k){
		  ArrayList<Integer> result = new ArrayList<Integer> ();
		  for (int i = 0; i < a.length; i++){
		    int sum = 0;
		    ArrayList<Integer> temp = new ArrayList<Integer> ();
		    for (int j = i; j < a.length; j ++){
		      temp.add(a[j]);
		      if (sum == k && result.size() < (temp.size())){
		        result = temp;
		        
		      }
		    }
		  }
		  return result;
		}
	
}


