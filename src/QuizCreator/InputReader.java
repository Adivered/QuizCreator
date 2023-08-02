package QuizCreator;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputReader {
	
	// Members
	private static Scanner scan = new Scanner(System.in);
	
	// No need in Constructor, static class which only be used for reading and validating inputs
	
	// Functions
	public static void closeScanner() {
		scan.close();
	} // closeScanner
	
	public static String readString(String prompt) {
		/*
		 * Reads a string from user
		 */
		System.out.println(prompt);
		return scan.nextLine();
	} // readString
	
	public static Question.Difficulity readDifficulty(String prompt){
		/*
		 * Reads Question difficulty from user
		 */
		while(true) {
			System.out.println(prompt);
			try {
				return Question.Difficulity.valueOf(scan.nextLine().toUpperCase());
			} catch (IllegalArgumentException e) {
				System.out.println("Invalid input.");
			}
		} // While
	} // readDifficulty
	
	public static String readType(String prompt) {
		/*
		 * Reads Question type from user
		 */
		String type;
		while (true) {
			System.out.println(prompt);
			type = scan.nextLine().toUpperCase();
			if (type.equals("OPEN") || type.equals("CLOSED")) {
				return type;
			} else {
                System.out.println("Invalid input.");
			}
		}// While
	} // readType
	
	public static String readQuizType() {
		/*
		 * Reads Quiz type from user
		 */
		String type;
		while (true) {
			System.out.println("Please Enter Type of Quiz ('AUTOMATIC' or 'MANUAL')");
			type = scan.nextLine().toUpperCase();
			if (type.equals("MANUAL") || type.equals("AUTOMATIC") || type.equals("AUTO")) {
				return type;
			} else {
                System.out.println("Invalid input.");
			}
		}// While
	} // readType
	
	public static boolean readBoolean(String prompt) {
		/*
		 * Reads boolean value from user
		 */
		boolean isCorrect;
		while (true) {
			System.out.print(prompt);
			try {
				isCorrect = scan.nextBoolean();
				scan.nextLine();
				return isCorrect;
			} catch (InputMismatchException e) {
                System.out.println("Invalid input.");
                scan.nextLine();
			}
		} // While
	} // readBoolean
	
	public static int readInt(String prompt) {
		/*
		 * Reads an integer from user
		 */
		int n;
		while(true) {
			System.out.print(prompt);
			try { // VALIDATE INPUT
				n = Integer.parseInt(scan.next()); // CASTING
				scan.nextLine();
				if (n < 0) { // Negative Number
					throw new IllegalArgumentException("Please enter positive number!"); 
				}
				return n;
			}catch (NumberFormatException ex){ // INPUT IS NOT A NUMBER
				System.out.println("Please enter a proper number");
			}catch (IllegalArgumentException ex) { // INPUT IS NEGATIVE
				System.out.println(ex.getMessage());
			}
		} // While
	} // readInt
	
	public static void pressEnterToContinue() {
		try {
			InputReader.readString("Press Enter key to continue...");
		} catch (Exception e) {
		}
	}
	
	//---------------------------------
}
