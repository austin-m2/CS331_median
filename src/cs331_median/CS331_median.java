/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs331_median;

import java.util.Arrays;

/**
 *
 * @author Austin
 */
public class CS331_median {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //int[] a = new int[] {2, 1, 5, 6, 3, 7, 8, 4, 10, 20}; // 1, 2, 3, 4, 5, 6, 7, 8, 10, 20
        int size = 10;
        int[] a = new int[size];
        
        //while(true) {
            for (int i = 0; i < size; i++) {
                a[i] = (int) (Math.random() * 100);
            }
            int[] aCopy = Arrays.copyOf(a, a.length);
            
            long startTime = System.nanoTime();
            int median = superMedian(aCopy);
            double superTime = System.nanoTime() - startTime;
            superTime /= 1000000;

            System.out.println("Median from supermedian: " + median);

            startTime = System.nanoTime();
            Arrays.sort(a);
            median = a[size / 2];
            double sortTime = System.nanoTime() - startTime;
            sortTime /= 1000000;
            
            System.out.println("Median from sorting: " + median);
            
            System.out.println("\nTime for supermedian: " + superTime + " ms");
            System.out.println("Time for sorting:     " + sortTime + " ms");

//
//            for (int i = 0; i < size; i++) {
//                System.out.print("\n" + a[i]);
//                if (i == size / 2 - 1) {
//                    System.out.print(" <");
//                }
//            }

        
            
        //}
        
        
        
    }
    
    public static int superMedian(int[] array) {
        //FIX?
        return Select(array, (array.length - 1) / 2);
        //return Select(array, array.length / 2);
    }

    private static int Select(int[] array, int selection) {
        if (array.length <= 5) {
            return AdhocSelect(array, selection);
        }
        
        //FIX?
        //int medSize = (int) Math.ceil((double)array.length / 5.0);
        int medSize = (array.length % 5 == 0) ? array.length / 5 : array.length / 5 + 1;
        int[] medians = new int[medSize];
        
        for (int i = 0; i < medSize; i++) {
            int end = (5 * i + 4 < array.length) ? (5 * i + 4) : array.length - 1;
            int tempSize = end - 5*i + 1;
            int[] temp = new int[tempSize];
            for (int j = 0; j < tempSize; j++) {
                temp[j] = array[5 * i + j];
            }
            
            //int[] temp = {array[5*i], array[5*i+1], array[5*i+2], array[5*i+3], array[5*i+4]};
            //FIX?
            //medians[i] = AdhocSelect(temp, array.length / 2);
            medians[i] = AdhocSelect(temp, (temp.length - 1) / 2);
        }
        
        //FIX?
        int pivot = Select(medians, (medSize - 1) / 2 );
        int[] partition = partition(array, pivot);
        
        if (selection < partition[0]) {
            return Select(Arrays.copyOfRange(array, 0, partition[0]), selection);
        } else if (selection > partition[1]) {
            return Select(Arrays.copyOfRange(array, partition[1] + 1, array.length), selection - partition[1] - 1);
        } else {
            return pivot;
        }
    }

    private static int AdhocSelect(int[] array, int selection) {
        int[] sortedList = AdhocSort(array);
        return sortedList[selection];
    }

//    private static int[] Partition(int[] array, int pivot) {
//        int i = 0;
//        int j = array.length - 1;
//        int p = i, q = j;
//        int temp;
//        
//        while (i <= j) {
//            while (array[i] < pivot) {
//                i++;
//                p = i;
//            }
//            while (array[j] > pivot) {
//                j--;
//                q = j;
//            }
//            if (i <= j) {
//                temp = array[i];
//                array[i] = array[j];
//                array[j] = temp;
//                i++;
//                j--;
//                //p = i;
//                //q = j;
//            }
//        }
//        
//        return new int[] {p, q};
//        
//    }
    
    private static int[] partition(int[] array, int pivot) {
        int i = -1;
        int temp;
        
        for (int j = 0; j < array.length - 1; j++) {
            if (array[j] <= pivot) {
                i += 1;
                temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        temp = array[i + 1];
        array[i + 1] = array[array.length - 1];
        array[array.length - 1] = temp;
        
        int frontPivotIndex = i + 1;
        //int frontPivotIndex = i;
        int backPivotIndex = frontPivotIndex;
        while (backPivotIndex < array.length && array[frontPivotIndex] == array[backPivotIndex]) {
            backPivotIndex += 1;
        }
        
        return new int[] {frontPivotIndex, backPivotIndex - 1};
    }
    
    private static int[] AdhocSort(int[] array) {
        switch (array.length) {
            case 1:
                return array;
            case 2:
                return AdhocSort2(array);
            case 3:
                return AdhocSort3(array);
            case 4:
                return AdhocSort4(array);
            case 5:
                return AdhocSort5(array);
            default:
                break;
        }
        return null;
    }
    
    private static int[] AdhocSort2(int[] array) {
        if (array[0] > array[1]) {
            return swap(array, 0, 1);
        } else {
            return array;
        }
    }
    
    private static int[] AdhocSort3(int[] array) {
        if (array[0] > array[1]) {
            array = swap(array, 0, 1);
        }
        return insert(new int[] {array[0], array[1]}, array[2]);
    }
    
    private static int[] AdhocSort4(int[] array) {
        if (array[0] > array[1]) {
            array = swap(array, 0, 1);
        }
        if (array[2] > array[3]) {
            array = swap(array, 2, 3);
        }
        if (array[0] > array[2]) {
            array = new int[] {array[2], array[3], array[0], array[1]};
        }
        int[] temp = insert(new int[] {array[2], array[3]}, array[1]);
        return new int[] {array[0], temp[0], temp[1], temp[2]};
    }
    
    private static int[] AdhocSort5(int[] array) {
        if (array[0] > array[1]) {
            array = swap(array, 0, 1);
        }
        if (array[2] > array[3]) {
            array = swap(array, 2, 3);
        }
        if (array[0] > array[2]) {
            array = new int[] {array[2], array[3], array[0], array[1], array[4]};
        }
        int[] xList = insert(new int[] {array[0], array[2], array[3]}, array[4]);
        int[] temp = insert(new int[] {xList[1], xList[2], xList[3]}, array[1]);
        return new int[] {xList[0], temp[0], temp[1], temp[2], temp[3]};
    }
    
    private static int[] swap(int[] array, int a, int b) {
        int temp;
        temp = array[a];
        array[a] = array[b];
        array[b] = temp;
        return array;
    }
    
    private static int[] insert(int[] array, int x) {
        if (x < array[1]) {
            if (x < array[0]) {
                return (array.length == 2) ? new int[] {x, array[0], array[1]} : new int[] {x, array[0], array[1], array[2]};
            } else {
                return (array.length == 2) ? new int[] {array[0], x, array[1]} : new int[] {array[0], x, array[1], array[2]};
            }
        } else {
            if (array.length == 2 || x > array[2]) {
                return (array.length == 2) ? new int[] {array[0], array[1], x} : new int[] {array[0], array[1], array[2], x};
            } else {
                return new int[] {array[0], array[1], x, array[2]};
            }
        }
    }

}
