
public class stringSwapN {
	public String findMaximumNum(String str, int k, String max){
		  if (k == 0){
		    return max;
		  }
		  int length = str.length();
		  for (int i =0; i < str.length() - 1; i++){
		    for (int j = i + 1; j < str.length(); j++){
		      if (str.charAt(i) > str.charAt(j)){
		        str = strswaper(str, i, j);
		        if (str.compareTo(max) > 0)
		          max = str;
		        findMaximumNum(str, k - 1, max);
		        str = strswaper(str, j, i);
		      }
		    }
		  }
		  
		  return max;
		}
	
	
	public String strswaper(String str, int i, int j){
		return str.substring(0, i) + str.charAt(j) + str.substring(i, j+1) + str.charAt(i) + str.substring(j+1, str.length());
	}
}
