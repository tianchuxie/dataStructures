import java.util.Arrays;

public class find2Popluar {
	public int find2Popluar(int[] arr){
		  Arrays.sort(arr);
		  int pop = -1; 
		  int maxValue = arr[0];
		  int sedCont = 0;
		  int maxCont = 0;
		  int previous = arr[0];
		  int cont = 1;
		  for (int i = 1; i < arr.length; i++){
		    if (arr[i] == previous){
		      cont ++;
		    } else {
		      if (cont > maxCont){
		        sedCont = maxCont;
		        maxCont = cont;
		        pop = maxValue;
		        maxValue = previous; 
		    
		      } else if (cont > sedCont){
		        pop = arr[i];
		        sedCont = cont;
		      
		      } 
		      
		        previous = arr[i];
		        cont = 1;
		      
		        
		      }
		    }
		  
		  return cont > maxCont ? maxValue : pop ;
		  // if return -1, then there is no such 2 frequencey 
		  }
}
