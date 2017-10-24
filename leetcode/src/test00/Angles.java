
package test00;

import java.lang.*;
import java.util.Arrays;
import java.io.*;
import java.awt.geom.*;

public class Angles  {
	
  public static void printArr(int[] arr){
	  for (int i=0 ; i< arr.length; i++){
		  System.out.print(arr[i]);
	  }
	  System.out.println();
  }

  public static void main(String[] args) {
    /*
    Point2D.Double p1 = new Point2D.Double(0,0);
    Point2D.Double p2 = new Point2D.Double(0,2);
    Point2D.Double p3 = new Point2D.Double(-2,2);
    
    Arc2D.Double arc = new Arc2D.Double();
    
    arc.setArcByTangent(p1,p2,p3,1);
    
    System.out.println( arc.getAngleExtent() );
    Point2D.Double t = new Point2D.Double(1,2);
    System.out.println(Line2D.linesIntersect(4,0,16,16,4.5,0,4,99.5));
    System.out.println(Line2D.linesIntersect(1,2,1,3,1,3,1,4));
    
    String str = "012345";
    System.out.println(str.substring(2,3));
    */ /*
    double j = 15.0; 
    double a = 10.0;
    double r = 1.0;
    for (int i = 350; i >=310; i --){
    	boolean b = false;
    	int n = i;
    	int k = 0;
    	while (n - k*17 >= 0){
    		if ((n - k*17) % 23 == 0){
    			System.out.println(i+ "= " + k +"*17   * "+ (n - k*17) / 23 + "*23    mod " +(n - k*17) % 23);
    			b = true;
    		}
    		k++;
    	}
    
    	if (!b){
    		System.out.println(i);
    	}
    	
    }
    */
	  
	  int k = 25;
	  int[] arr = new int [k]; 
	  Arrays.fill(arr, 1);
	  printArr(arr);
	  for (int i=1; i < k; i++){
		 
			  int s = i + 1;
			  int l = i;
			  while (l < k){
				 
			  if (arr[l] == 1) {
				  arr[l]=0; 
			  } else {
				  arr[l] = 1;
			  }
			  l += s;
			  }
		 
		  printArr(arr);
	  }
	  

  }
  
}