package AdiVered_GuyBenMoshe;

public class Question {
	// Finals
	private static final int MAX_NUMBER_OF_ANSWERS = 10;
	// Static Members
	private static int counter;	
	// Members
	private int questionID = 0;
	private String questionText;
	private int currentNumberOfAnswer = 0;
	private Answer[] allAnswers;
	
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
	public Question() {
		counter++;
		setQuestionID(counter);
		questionText = "";
		allAnswers = new Answer[MAX_NUMBER_OF_ANSWERS];
	}
	
	public Question(String questionText) {
		counter++;
		setQuestionID(counter);
		setQuestionText(questionText);
		allAnswers = new Answer[MAX_NUMBER_OF_ANSWERS];
	}
	
	public int getQuestionID() {
		return questionID;
	}
	
	public void setQuestionID(int num) {
		this.questionID = num;
	}
	
	public String getQuestionText() {
		return questionText;
	}
	
	public int getMaxNumberOfAnswers() {
		// Maximum is 8 because last two answers are for quiz must have answers
		return MAX_NUMBER_OF_ANSWERS - 2;
	}
	
	public void setQuestionText(String newName) {
		questionText = newName;
	}
	
	public int getCurrentNumberOfAnswers() {
		return currentNumberOfAnswer;
	}
	
	public Answer[] getAnswers() {
		return allAnswers;
	}
	
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
	
	public Answer getAnswerByID(int id) {
		// Answer "ID" refers to the answer index
		return allAnswers[id - 1];
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

	@Override
	public String toString() {
		String message = "Question ID #" + getQuestionID() + ":\nQuestion Text: " + getQuestionText() + ".\n";
		String correctAnswers = "Answers: ";
		for(int i=0; i < currentNumberOfAnswer; i++) {
			message += (char)(i+65) +") " + allAnswers[i].getAnswer()  + ".\n";
			if (allAnswers[i].getIsCorrect()) {
				correctAnswers += (char)(i+65) + ", ";
			}
		}
		message += correctAnswers + ".\n------------\n";
		return message;
	}
}
