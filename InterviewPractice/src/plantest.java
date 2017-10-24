import java.util.ArrayList;
import java.util.Arrays;

public class plantest {
	public static ArrayList<ArrayList<Integer>> threeSume(int[] arr, int k){
		  Arrays.sort(arr);
		  ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		  for (int i = 0; i < arr.length; i++){
		    int sum;
		    int p1 = i + 1;
		    int p2 = arr.length - 1;
		    while (p1 < p2 ){
		      sum = arr[i] + arr[p1] + arr[p2];
		      if (sum == k){
		        ArrayList<Integer> r = new ArrayList<Integer>();
		        r.add(arr[i]);
		        r.add(arr[p1]);
		        r.add(arr[p2]);
		        result.add(r);
		      } else if (sum < k){
		        p1++;
		      } else if (sum > k){
		        p2--;
		      }
		        
		      
		    }
		    
		  }
		  
		  return result;
		}
	
    public static void main (String[] args)
    {
        int arr[] = {1, 9, 8, 4, 0, 0, 2, 7, 0, 6, 0, 9};
        ArrayList<ArrayList<Integer>> result = threeSume(arr, 14);
        System.out.println("Array after pushing zeros to the back: ");
        for (ArrayList<Integer> r: result){
        	System.out.println(r.get(0) + " " + r.get(1) + " " + r.get(2));
        }
            
    }

}
