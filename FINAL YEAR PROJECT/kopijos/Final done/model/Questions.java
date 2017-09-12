package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Questions implements Serializable {

	private int Number;
	private String Question;
	private String Answer;
	private String Hint;

	public Questions(int Number, String Question, String Answer, String Hint)  {
		this.Number = Number;
		this.Question = Question;
		this.Answer = Answer;
		this.Hint = Hint;
	}

	public int getNumber() {
		return Number;
	}

	public void setNumber(int n) {
		this.Number = n;
	}

	public String getQuestion() {
		return Question;
	}

	public void setQuestion(String n) {
		this.Question = n;
	}

	public String getAnswer() {
		return Answer;
	}

	public void setAnswer(String n) {
		this.Answer = n;
	}

	public String getHint() {
		return Hint;
	}

	public void setHint(String n) {
		this.Hint = n;
	}

	public String getNumberandQuestion() {
		int s = Number + 1;
		return s + ". " + Question;
	}

	@Override
	public String toString() {
		return Number + ". " + Question + " || " + Answer + " || " + Hint;

	}

	//public class answer_comparator implements Comparator<Questions>

}