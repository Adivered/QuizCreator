package AdiVered_GuyBenMoshe;

public class Answer {
	// Members
	private String answer;
	private boolean isCorrect;
	
	// Methods
	// Constructors	
	public Answer(String answer) {
		setAnswer(answer);
	}
	
	public Answer(String answer, Boolean isCorrect) {
		setAnswer(answer);
		setIsCorrect(isCorrect);
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
