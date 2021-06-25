package net.tylermurphy.Minecraft.UI;

import java.util.List;

public class UILayerQuickSort {
 
    public static void quickSort(List<UIComponent> arr, int start, int end){
 
        int partition = partition(UIMaster.componentBatch, start, end);
 
        if(partition-1>start) {
            quickSort(UIMaster.componentBatch, start, partition - 1);
        }
        if(partition+1<end) {
            quickSort(UIMaster.componentBatch, partition + 1, end);
        }
    }
 
    public static int partition(List<UIComponent> arr, int start, int end){
    	
    	UIComponent pivot = arr.get(end);
 
        for(int i=start; i<end; i++){
            if(arr.get(i).getZIndex()<pivot.getZIndex()){
            	UIComponent temp= arr.get(start);
                arr.set(start, arr.get(i));
                arr.set(i, temp);
                start++;
            }
        }
 
        UIComponent temp = arr.get(start);
        arr.set(start, pivot);
        arr.set(end, temp);
 
        return start;
    }
}