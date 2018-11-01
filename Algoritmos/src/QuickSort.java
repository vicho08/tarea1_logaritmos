
import java.util.ArrayList;

public class QuickSort {
     
    private ArrayList<String> array;
    private int length;
 
    public void sort(ArrayList<String> inputArr, int ind) {
         
        if (inputArr == null || inputArr.size() == 0) {
            return;
        }
        this.array = inputArr;
        length = inputArr.size();
        quickSort(0, length - 1, ind);
    }
    
    private String getVal(String line, int ind) {
    	String[] line_aux = line.split(";");
    	return line_aux[ind];
    }
 
    private void quickSort(int lowerIndex, int higherIndex, int attr_ind) {
         
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        String pivot_line = array.get(lowerIndex+(higherIndex-lowerIndex)/2);
        String pivot = getVal(pivot_line, attr_ind);
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which 
             * is greater then the pivot value, and also we will identify a number 
             * from right side which is less then the pivot value. Once the search 
             * is done, then we exchange both numbers.
             */
            while (getVal(array.get(i), attr_ind).compareTo(pivot) < 0) {
                i++;
            }
            while (getVal(array.get(j), attr_ind).compareTo(pivot) > 0) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j, attr_ind);
        if (i < higherIndex)
            quickSort(i, higherIndex, attr_ind);
    }
 
    private void exchangeNumbers(int i, int j) {
        String temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
        //array[i] = array[j];
        //array[j] = temp;
    }
     
}	
		
