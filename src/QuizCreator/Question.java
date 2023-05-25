package AdiVered_GuyBenMoshe;

import java.io.Serializable;

public class Question implements Serializable {

	// Static Members
	private static int counter;
	
	// Members
	protected int questionID = 0;
	protected String questionText;
	protected enum Difficulity {EASY, MEDIUM, HARD};
	protected Difficulity difficulity;
	
	// Methods
	// Constructors
	/*
	 * Question Class can be created in two ways:
	 * 1. Empty question
	 * 2. Question with text
	 * Each question gets unique id.
	 * Each question consist of question text, current number of answers filled so we can keep
	 * track of how many answers have been added, and array of Answer class (max 10)
	 */	
	public Question(String questionText, Difficulity difficulity ) {
		counter++;
		setQuestionID(counter);
		setQuestionText(questionText);
		setDifficulity(difficulity);
	}
	
	// Getters
	public Difficulity getDifficulity() {
		return difficulity;
	}
	
	public int getQuestionID() {
		return questionID;
	}
	
	public String getQuestionText() {
		return questionText;
	}
	
	//Setters
	public void setDifficulity(Difficulity difficulity) {
		this.difficulity = difficulity;
	}
	
	public void setQuestionID(int num) {
		this.questionID = num;
	}
	
	public void setQuestionText(String newName) {
		questionText = newName;
	}
	
	
	// Functions
	public static void reduceCount() {
		// If we remove question, we need to reduce counter by 1;
		counter--;
	}
	
	public static void resetCounter() {
		/*
		 * We call this method only when we want to create/load a new bank,
		 * to make sure question static counter reset
		 */
		counter = 0;
	}
	
	
	@Override
	public String toString() {
		return "Question ID #" + getQuestionID() + ":\nQuestion Text: " + 
				getQuestionText() + "\nDifficulity: " + difficulity + "\n";
	}
}
