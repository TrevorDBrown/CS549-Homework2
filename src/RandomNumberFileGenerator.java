/*
 * CS 549 - Homework 2
 * Trevor D. Brown
 * 
 * Developed for:
 * 		CS 549-001 (Fall 2019)
 * 		Instructor: Dr. Qi Li
 * 
 * Description:
 * 		This application is designed to generate a list of random integers
 * 		for use in the Homework2.java application.
 * 
 */

// Import java.io to append to the specified file.
import java.io.*;

public class RandomNumberFileGenerator {

	public static void main(String[] args) {
		
		try {
			// Append to the specified file.
			File newFile = new File("input1000000.txt");
			FileWriter fw = new FileWriter(newFile, true);
			
			// Parameters for integer range and size
			int desiredInputSize = 1000000;
			int min = 0;
			int max = 1000000;
			
			// For each iteration of the loop, generate a random value
			// between min and max and write it to the file. 
			for (int i = 0; i < desiredInputSize; i++) {
				int newValue = generateRandomValue(min, max);
				fw.write(newValue + "\n");
			}
			
			// Close the FileWriter object.
			fw.close();
		}catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	// generateRandomValue - generates a random value between the min and max value.
	public static int generateRandomValue(int min, int max) {
		int rand = (int)((Math.random() * (max - min + 1)) + min);
		
		return rand;
	}

}
