package AdiVered_GuyBenMoshe;

public class ClosedQuestion extends Question implements Cloneable {
	// Finals
	private static final int MAX_NUMBER_OF_ANSWERS = 10;

	// Members
	private int currentNumberOfAnswer = 0;
	private Answer[] allAnswers;

	public ClosedQuestion(String questionText, Difficulity difficulity){
		super(questionText, difficulity);
		allAnswers = new Answer[MAX_NUMBER_OF_ANSWERS];
	} 
	
	public ClosedQuestion clone() throws CloneNotSupportedException {
		return (ClosedQuestion)super.clone();
	}
	
	// Methods

	//Getters
	public static int getMaxNumberOfAnswers() {
		// Maximum is 8 because last two answers are for quiz must have answers
		return MAX_NUMBER_OF_ANSWERS - 2;
	}

	public int getCurrentNumberOfAnswers() {
		return currentNumberOfAnswer;
	}

	public Answer[] getAnswers() {
		return allAnswers;
	}

	public Answer getAnswerByID(int id) {
		// Answer "ID" refers to the answer index
		return allAnswers[id - 1];
	}
	
	// Setters
	
	public void setAnswers(Answer[] answers, int currentNumberOfAnswers) {
		this.currentNumberOfAnswer = currentNumberOfAnswers;
		allAnswers = answers;
	}

	public void addAnswer(String answer, boolean isCorrect) {
		allAnswers[currentNumberOfAnswer++] = new Answer(answer, isCorrect);
	}

	// Functions

	public boolean isFull() {
		return (currentNumberOfAnswer == MAX_NUMBER_OF_ANSWERS - 2) ? true : false;
	}

	public boolean isAnswerExist(int id) {
		// Checks if answer exists
		if (id > 0 && id <= currentNumberOfAnswer) {
			return true;
		}
		return false;
	}

	public void removeAnswerByID(int id) {
		// If answer is last in array
		if (currentNumberOfAnswer == id) {
			allAnswers[id - 1] = null;
		}else { // Copy the last answer to the removed block
			allAnswers[id - 1] = allAnswers[currentNumberOfAnswer - 1];
			allAnswers[currentNumberOfAnswer - 1] = null;
		}
		currentNumberOfAnswer--;
	}

	public void addDefultAnswers() {
		// This function adds the must-have two answers to a question
		int count = 0;
		boolean moreThanOne = false, notRightAnswer = false;
		for (int i = 0; i < currentNumberOfAnswer; i++) {
			if(allAnswers[i].getIsCorrect())
				count++;
		}
		if (count > 1) { // Count > 1 --> More than one answer is correct
			moreThanOne = true;
		}else if (count == 0) { // Count == 0 --> All answers are wrong
			notRightAnswer = true;
		}		
		allAnswers[currentNumberOfAnswer++] = new Answer("There is more then one correct answer", moreThanOne);
		allAnswers[currentNumberOfAnswer++] = new Answer("All answers are wrong", notRightAnswer);
	}
	
	public void addAutoDefultAnswers() {
		// This function adds the must-have two answers to a question
		int count = 0;
		boolean notRightAnswer = false;
		for (int i = 0; i < currentNumberOfAnswer; i++) {
			if(allAnswers[i].getIsCorrect())
				count++;
		}
		if (count == 0) { // Count == 0 --> All answers are wrong
			notRightAnswer = true;
		}		
		allAnswers[currentNumberOfAnswer++] = new Answer("All answers are wrong", notRightAnswer);
	}
	

	public String toString() {
		String message = super.toString();
		String correctAnswers = "Answers: ";
		for(int i=0; i < currentNumberOfAnswer; i++) {
			message += (char)(i+65) +") " + allAnswers[i].getAnswer()  + ".\n";
			if (allAnswers[i].getIsCorrect()) {
				correctAnswers += (char)(i+65) + ", ";
			}
		}
		message += correctAnswers + "\n";
		return message;
	}
}
