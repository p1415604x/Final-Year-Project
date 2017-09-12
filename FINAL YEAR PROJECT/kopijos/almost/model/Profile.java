package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.scene.control.Toggle;

@SuppressWarnings("serial")
public class Profile implements Serializable {

	private String name;
	private String surname;
	private String email;
	private LocalDate date;
	private ArrayList<Questions> questions;
	private Toggle toggle;

	public Profile() {
		name = "";
		surname = "";
		email = "";
		date = null;
		toggle = null;
		questions = new ArrayList<Questions>();
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

	public Toggle getToggle() {
		return toggle;
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

	public void setToggle(Toggle n) {
		this.toggle = n;
	}

	public boolean addQuestionsToProfile(ArrayList<Questions> questionlist) {
		return questions.addAll(questionlist);
	}

	public ArrayList<Questions> getQuestionsFromProfile() {
		return questions;
	}

	public void clearProfileQuestions() {
		questions.clear();
	}
}
