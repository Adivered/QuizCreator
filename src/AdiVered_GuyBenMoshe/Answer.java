package AdiVered_GuyBenMoshe;

public class Answer {
	// Members
	private String answer;
	private boolean isCorrect;
	
	// Methods
	// Constructors	
	public Answer(String answer, Boolean isCorrect) {
		this.answer = answer;
		this.isCorrect = isCorrect;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public Boolean getIsCorrect() {
		return isCorrect;
	}
	
	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
}
