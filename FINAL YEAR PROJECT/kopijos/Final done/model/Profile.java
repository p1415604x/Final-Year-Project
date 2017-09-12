package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

@SuppressWarnings("serial")
public class Profile implements Serializable {

	private String name;
	private String surname;
	private String email;
	private LocalDate date;
	private ArrayList<Questions> questions;
	private ArrayList<GridData> GridInput;
	private String RBt;
	private String hintNumber;

	public Profile() {
		name = "";
		surname = "";
		email = "";
		RBt ="";
		hintNumber="";
		date = null;

		questions = new ArrayList<Questions>();
		GridInput = new ArrayList<GridData>();
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getRBt() {
		return RBt;
	}

	public ArrayList<GridData> getGridInput() {
		return GridInput;
	}

	public String getHintNumber() {
		return hintNumber;
	}

	public void setName(String n) {
		this.name = n;
	}

	public void setSurname(String n) {
		this.surname = n;
	}

	public void setEmail(String n) {
		this.email = n;
	}

	public void setData(LocalDate n) {
		this.date = n;
	}


	public void setRbt(String b) {
		this.RBt = b;
	}

	public void setGridInput(String l, int r, int c) {
		GridInput.add(new GridData(l,r,c));
	}

	public void setHintNumber(String n) {
		hintNumber =n;
	}

	public boolean addQuestionsToProfile(ArrayList<Questions> questionlist) {
		return questions.addAll(questionlist);
	}

	public void addSingleQuestion(String q, String a, String h) {
		questions.add(new Questions(questions.size(),q,a,h));
	}


	public void deleteSingleQuestion(String s) {
		for(Iterator<Questions> itr = questions.iterator(); itr.hasNext();){
			Questions q = itr.next();
			if(q.getQuestion().toUpperCase().equals(s.toUpperCase())){
				itr.remove();
			}
		}

	}

	public ArrayList<Questions> getQuestionsFromProfile() {
		return questions;
	}

	public void clearProfileQuestions() {
		questions.clear();
	}

	public void clearGridInput() {
		GridInput.clear();
	}

	public class GridData implements Serializable {
		private String letter;
		private int row;
		private int column;
	    public GridData(String l, int r, int c) {
	    	this.letter = l;
	    	this.row = r;
	    	this.column = c;
	    }
	    public String getChar() {
	    	return letter;
	    }
	    public int getRow() {
	    	return row;
	    }
	    public int getColumn() {
	    	return column;
	    }
	}

}
