package AdiVered_GuyBenMoshe;

public class OpenQuestion extends Question {
	// Members
	private Answer answer;

	public OpenQuestion(String questionText, Difficulity difficulity){
		super(questionText, difficulity);
	}
	
	public void addAnswer(String answer) {
		this.answer = new Answer(answer);
	}
	
	public Answer getAnswer() {
		return answer;
	}
	
	public String toString() {
		return super.toString() + "Answer:\n" +  answer.getAnswer()  + "\n";
	}

}
