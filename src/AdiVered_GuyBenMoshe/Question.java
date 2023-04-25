package AdiVered_GuyBenMoshe;

public class Question {

	// Static Members
	private static int counter;
	protected enum TypeOfQuestion {OPEN_QUESTION, CLOSED_QUESTION};
	protected enum Difficulity {EASY, MEDIUM, HARD};

	// Members
	protected int questionID = 0;
	protected String questionText;
	protected TypeOfQuestion questionType;
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
	public Question() {
		counter++;
		setQuestionID(counter);
		questionText = "";
	}
	
	public Question(String questionText, TypeOfQuestion questionType, Difficulity difficulity ) {
		counter++;
		setQuestionID(counter);
		setQuestionText(questionText);
		setTypeOfQuestion(questionType);
		setDifficulity(difficulity);
	}
	
	public TypeOfQuestion getTypeOfQuestion() {
		return questionType;
	}
	
	public void setTypeOfQuestion(TypeOfQuestion questionType) {
		this.questionType = questionType;

	}
	
	public Difficulity getDifficulity() {
		return difficulity;
	}
	
	public void setDifficulity(Difficulity difficulity) {
		this.difficulity = difficulity;
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
	
	public void setQuestionText(String newName) {
		questionText = newName;
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
	
	@Override
	public String toString() {
		String message = "Question ID #" + getQuestionID() + ":\nQuestion Text: " + getQuestionText() + ".\n";
		String correctAnswers = "Answers: ";
		/*for(int i=0; i < currentNumberOfAnswer; i++) {
			message += (char)(i+65) +") " + allAnswers[i].getAnswer()  + ".\n";
			if (allAnswers[i].getIsCorrect()) {
				correctAnswers += (char)(i+65) + ", ";
			}
		}*/
		message += correctAnswers + ".\n------------\n";
		return message;
	}
}
