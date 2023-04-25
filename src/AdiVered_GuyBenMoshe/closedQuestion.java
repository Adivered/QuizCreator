package AdiVered_GuyBenMoshe;

import AdiVered_GuyBenMoshe.Question.Difficulity;
import AdiVered_GuyBenMoshe.Question.TypeOfQuestion;

public class closedQuestion extends Question {
	// Finals
	private static final int MAX_NUMBER_OF_ANSWERS = 10;
	
	// Members
	private int currentNumberOfAnswer = 0;
	private Answer[] allAnswers;
	
	
	public closedQuestion(String questionText, TypeOfQuestion questionType, Difficulity difficulity){
		super(questionText, questionType, difficulity);
		allAnswers = new Answer[MAX_NUMBER_OF_ANSWERS];
	} 
	// Methods
	
	public int getMaxNumberOfAnswers() {
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
}
