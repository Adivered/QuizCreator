package QuizCreator;

public class OpenQuestion extends Question {
	// Members
	private Answer answer;
	
	//Constructor
	public OpenQuestion(String questionText, Difficulity difficulity){
		super(questionText, difficulity);
	}
	
	// Getters
	public Answer getAnswer() {
		return answer;
	}
	
	// Functions
	public void addAnswer(String answer) {
		this.answer = new Answer(answer);
	}
	
	public String toString() {
		return super.toString() + "Answer:\n" +  answer.getAnswer()  + "\n";
	}

}
