
public class moveZeros {
	
	static void pushZerosToTheEnd(int[] arr, int n){
		int position = 0;
		
		for (int i = 0; i < arr.length; i++){
			if (arr[i] != 0){
				arr[position] = arr[i];
				position++;
			}
		}
		
		while (position < arr.length){
			arr[position++] = 0;
		}
	}
	
	
	/*
    public static void main (String[] args)
    {
        int arr[] = {1, 9, 8, 4, 0, 0, 2, 7, 0, 6, 0, 9};
        int n = arr.length;
        pushZerosToTheEnd(arr, n);
        System.out.println("Array after pushing zeros to the back: ");
        for (int i=0; i<n; i++)
            System.out.print(arr[i]+" ");
    }
	*/
	

}
