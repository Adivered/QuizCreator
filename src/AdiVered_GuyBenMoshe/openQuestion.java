package AdiVered_GuyBenMoshe;

import AdiVered_GuyBenMoshe.Question.Difficulity;
import AdiVered_GuyBenMoshe.Question.TypeOfQuestion;

public class openQuestion extends Question {
	// Members
	private Answer answer;

	public openQuestion(String questionText, TypeOfQuestion questionType, Difficulity difficulity){
		super(questionText, questionType, difficulity);
	}
	
	public void addAnswer(String answer) {
		this.answer = new Answer(answer);
	}

}
