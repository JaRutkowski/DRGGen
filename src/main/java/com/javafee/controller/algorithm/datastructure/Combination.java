package com.javafee.controller.algorithm.datastructure;

import java.util.ArrayList;
import java.util.List;

public class Combination {
	/* arr[]  ---> Input Array
    data[] ---> Temporary array to store current combination
    start & end ---> Staring and Ending indexes in arr[]
    index  ---> Current index in data[]
    r ---> Size of a combination to be printed */
	static void combinationUtil(int arr[], int data[], int start, int end, int index, int r) {
		// Current combination is ready to be printed, print it
		if (index == r) {
			for (int j = 0; j < r; j++)
				System.out.print(data[j] + " ");
			System.out.println("");
			return;
		}

		// replace index with all possible elements. The condition
		// "end-i+1 >= r-index" makes sure that including one element
		// at index will make a combination with remaining elements
		// at remaining positions
		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data[index] = arr[i];
			combinationUtil(arr, data, i + 1, end, index + 1, r);
		}
	}

	// The main function that prints all combinations of size r
	// in arr[] of size n. This function mainly uses combinationUtil()
	static void printCombination(int arr[], int n, int r) {
		// A temporary array to store all combination one by one
		int data[] = new int[r];
		//List<Integer> data = new ArrayList<>();

		// Print all combination using temprary array 'data[]'
		combinationUtil(arr, data, 0, n - 1, 0, r);
	}

	static void combinationUtilL(List<Integer> arr, List<Integer> data, int start, int end, int index, int r) {
		if (index == r) {
			culdata.add(data);
			return;
		}

		for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
			data.set(index, arr.get(i));
			combinationUtilL(arr, data, i + 1, end, index + 1, r);
		}
	}

	static void printCombinationL(List<Integer> arr, int n, int r) {
		data = new ArrayList<>();
		combinationUtilL(arr, data, 0, n - 1, 0, r);
	}

	private static List<List<Integer>> culdata = new ArrayList<>();
	private static List<Integer> data = null;

	/*Driver function to check for above function*/
	public static void main(String[] args) {
		int arr[] = {1, 2, 3};
		int r = 2;
		int n = arr.length;
		printCombination(arr, n, r);
		System.out.println("\n");

		List<Integer> arrL = new ArrayList<>();
		arrL.add(1);
		arrL.add(2);
		arrL.add(3);
		int rL = 2;
		int nL = arr.length;
		printCombinationL(arrL, nL, rL);
		System.out.println("\n");
		culdata.forEach(e -> System.out.println(e));
	}
}
