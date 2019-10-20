/*
 * CS 549 - Homework 2
 * Trevor D. Brown
 * 
 * Developed for:
 * 		CS 549-001 (Fall 2019)
 * 		Instructor: Dr. Qi Li
 * 
 * Description:
 * 		This application is designed to show an implementation of 
 * 		the SELECT algorithm.
 * 
 */

// Use these packages to import text file with integer data
import java.util.Scanner;
import java.io.*;

public class Homework2 {

	public static void main(String[] args) {
		
		// Change this parameter to change inputs used
		String fileName = "input1000000.txt";
		
		// Read the specified file into the array for processing.
		int[] A = readFileIntoArray(fileName);
		
		// If a successful read (i.e. A has data), proceed to process
		if (A.length > 0) {
			int groupSize = 3;			// Group Size
			int ithValue = 512;			// Desired i-th value
			
			// Begin logging runtime
			long startTime = System.currentTimeMillis();
			int ithSmallest = select (groupSize, A, ithValue, 0, A.length - 1);
			// End logging runtime
			long endTime = System.currentTimeMillis();
			
			// Determine duration of execution.
			long duration = (endTime - startTime);
			
			// If the ith smallest value is returned to be the minimum value of an Integer,
			// the algorithm failed to run due to:
			// 		- A ithValue was provided that was out of the array boundaries.
			//		- The algorithm incorrectly modifying the i-th value on a recursive call
			if (ithSmallest == Integer.MIN_VALUE) {
				System.out.println("Error: i-th value provided is out of array bounds. Valid i-th values are 1 to " + (A.length) + ".");
			}else {
				// Print data regarding the run: 
				//i.e. Number of elements processed, i-th value result, verification via sorted array access, and runtime
				System.out.println("Number of Elements: " + A.length);
				System.out.print("The i-th = " + ithValue + " value in the array is: " + ithSmallest + ".");
				System.out.println("\nDirect Verification: A[" + (ithValue - 1) + "]: " + A[ithValue - 1]);
				System.out.print("Running time: " + duration + "ms");
			}	
		}else {
			System.out.println("Error reading file.");
		}

	}
	
	public static void insertionSort(int[] A, int start, int end) {
		for (int j = start + 1; j <= end; j++) {
			int key = A[j];
			int i = j - 1;
			
			while ((i >= start) && (A[i] > key)) {
				A[i + 1] = A[i];
				i = i - 1;
			}
			
			A[i + 1] = key;
		}
	}
	
	public static int partition (int[] A, int p, int r, int x) {
		
		int i;
		
		// Move the x value to A[r] (i.e. the last index in the specified array)
		for (i = p; i < r; i++) {
			if (A[i] == x) {
				exchange(A, i, r);
				break;
			}
		}
		
		// Partition the rest of the array around this value.
		i = p - 1;
		
		for (int j = p; j < r - 1; j++) {
			if (A[j] <= x) {
				i++;
				exchange(A, i, j);
			}
		}
		
		exchange(A, i + 1, r);
		
		return i + 1;
	}
	
	// findMedian - finds the median of a specified range on a provided array
	public static int findMedian (int[] A, int start, int end) {
		int midpoint = (int)Math.floor((start + end) / 2);
		return A[midpoint];
	}
	
	// exchange - swaps the values at the two specified indices (i and j)
	public static void exchange (int[] A, int i, int j) {
		int temp = A[i];
		A[i] = A[j];
		A[j] = temp;
	}
	
	
	public static int select (int groupSize, int[] A, int i, int l, int r) {
		
		// Determine size of input
		int n = (r - l) + 1;
		
		// If the i-th value is within 0 and n, proceed. Otherwise, return a value
		// that will not generally be used to represent a failed condition.
		if (i > 0 && i <= n) {
			
			// Determine number of partitions needed.
			int groups = (int)Math.floor(n / groupSize);
			
			// If there exists a remainder, add one to the total group count.
			if ((n % groupSize) > 0) {
				groups++;
			}
			
			// Initialize an array to store the found medians
			int[] medians = new int[groups];
			
			int j;	// Group counter
			
			for (j = 0; j < groups; j++) {
				// Determine Bounds for Group
				int start = l + (j * groupSize);		// Start bound: j * groupSize
				int end = start + (groupSize - 1);	// End bound: start + (group Size - 1)
				
				// If end exceeds array bounds, reset end to be A.length - 1
				// to prevent OutOfBoundException
				if (end >= r) {
					end = r;
				}
				
				// Insertion Sort the current group
				insertionSort(A, start, end);
				
				// Get Median of Group
				medians[j] = findMedian(A, start, end);

			}

			// Find the median of medians
			int x;	// Median of Medians
			
			// If the array containing median of medians is size 1,
			// store the median of medians as as the single element
			if (j == 1) {
				x = medians[0]; 
			}else {
				// Else, recurse select with medians array, finding the medians of this set.
				x = select (groupSize, medians, (int)Math.floor(j / 2), 0, medians.length - 1);
			}
			
			// Once the median of medians is found, partition the entire array against it.
			int q = partition (A, l, r, x);
			int k = (q - l) + 1;
			
			// If i == k, return the median (or i-th value)
			// If i < k, recurse select with lower bounds, and i-th value
			// If i > k, recurse select with upper bounds, and (i-k)-th value
			if (i == k) {
				return x;
			}else if (i < k) {
				return select (groupSize, A, i, l, q - 1);
			}else {
				return select (groupSize, A, i - k,q + 1, r);
			}
		}else {
			// Return the minimum possible integer value, if the provided i-th value is
			// not in the bounds of l and r.
			return Integer.MIN_VALUE;
		}

	}
	
	// printArrayContent - prints a formatted array to the console, for testing
	// and verification purposes. Prints only specified start and end ranges.
	public static void printArrayContent(int[] A, int start, int end) {

		System.out.print("[ ");
		
		for (int i = start; i <= end; i++) {
			System.out.print(A[i]);
			
			if (i + 1 <= end) {
				System.out.print(" | ");
			}
		}
		
		System.out.println(" ]");
	}
	
	// readFileIntoArray = determines size of file, creates array of that size,
	// and populated the array with the data from the file.
	public static int[] readFileIntoArray(String fileName) {
		File inputFile = new File(fileName);
		int count = 0;
		
		// Get Count
		try {
			Scanner fc = new Scanner(inputFile);
			
			while (fc.hasNextLine()) {
				fc.nextLine();
				count++;
			}
			
			// Close the scanner object counting the lines in the file.
			fc.close();
			
			int[] A = new int[count];
			
			Scanner fr = new Scanner(inputFile);
			
			// Get and store data from file into array
			for (int i = 0; i < count; i++) {
				A[i] = fr.nextInt();
			}
			
			// Close the Scanner object reading the file
			fr.close();
			
			return A;
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// If the read fails, return an integer array of size 0.
		return new int[0];
		
	}

}
