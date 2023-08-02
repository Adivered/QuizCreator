package QuizCreator;

import java.io.Serializable;

public class Answer implements Serializable {
	// Members
	private String answer;
	private boolean isCorrect;
	
	// Methods
	// Constructors	
	public Answer(String answer) { // Answer to Open question
		setAnswer(answer);
	}
	
	public Answer(String answer, Boolean isCorrect) { // Answer to Closed question
		setAnswer(answer);
		setIsCorrect(isCorrect);
	}
	// Methods
	
	// Getters
	
	public String getAnswer() {
		return answer;
	}
	
	public Boolean getIsCorrect() {
		return isCorrect;
	}
	
	// Setters
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
}
