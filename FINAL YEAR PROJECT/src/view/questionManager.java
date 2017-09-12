package view;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Profile;
import model.Questions;

public class questionManager extends VBox {
	private Button add, delete, refresh;
	private ArrayList<Questions> questionPool;
	private ListView<String> questionBox;
	private ObservableList<String> questions;
	private TextField questionTextfield, answerTextfield, hintTextfield;

	@SuppressWarnings("static-access")
	public questionManager() {

		questionPool = new ArrayList<Questions>();

		add = new Button("Add");
		add.setMinSize(150, 27);

		delete = new Button("Delete");
		delete.setMinSize(150, 27);

		refresh = new Button("Refresh");
		refresh.setMinSize(150, 27);

	    VBox hb1 = new VBox();
	    VBox hb2 = new VBox();
	    HBox hb3 = new HBox();

	    questionBox = new ListView<String>();
	    questionBox.setMaxSize(1000, 300);
	    questionBox.setMinSize(500,150);
	    questions = FXCollections.observableArrayList();

	    Label question = new Label("QUESTION");
	    questionTextfield = new TextField();
	    questionTextfield.setMinSize(250, 27);

	    Label answer = new Label("ANSWER");
	    answerTextfield = new TextField();
	    answerTextfield.setMinSize(75, 27);

	    Label hint = new Label("HINT");
	    hintTextfield = new TextField();
	    hintTextfield.setMinSize(150, 27);


	    hb1.getChildren().addAll(question, questionTextfield, answer, answerTextfield, hint, hintTextfield);
	    hb2.getChildren().addAll(questionBox);
	    hb3.getChildren().addAll(add,delete,refresh);


	    AnchorPane myAnchorPane = new AnchorPane(hb2,hb1, hb3);

	    myAnchorPane.setLeftAnchor(hb2, 300.0);
	    myAnchorPane.setTopAnchor(hb2, 20.0);
	    myAnchorPane.setRightAnchor(hb2, 300.0);
	    myAnchorPane.setBottomAnchor(hb2, 50.0);

        myAnchorPane.setBottomAnchor(hb1, -85.0);
        myAnchorPane.setLeftAnchor(hb1, 300.0);
        myAnchorPane.setRightAnchor(hb1, 300.0);

	    myAnchorPane.setBottomAnchor(hb3, -150.0);
	    myAnchorPane.setLeftAnchor(hb3, 450.0);
	    myAnchorPane.setRightAnchor(hb3, 50.0);




		this.getChildren().addAll(myAnchorPane);

	}

	public void setQuestions(Profile model) {
		questions.clear();
		model.getQuestionsFromProfile().stream().map(Questions::getNumberandQuestion).forEach(e->questions.add(e));
		questionBox.setItems(questions);
	}

    public ArrayList<Questions> getQuestions(ArrayList<Questions> list) {
    	questionPool = list;
		return questionPool;
    }

	public void addAddQuestionHandler(EventHandler<ActionEvent> handler) {
		add.setOnAction(handler);
	}

	public void addDeleteQuestionHandler(EventHandler<ActionEvent> handler) {
		delete.setOnAction(handler);
	}

	public void addRefreshQuestionHandler(EventHandler<ActionEvent> handler) {
		refresh.setOnAction(handler);
	}

	public String getQuestionfieldvalue() {
		return new String(questionTextfield.getText());
	}

	public String getAnswerfieldvalue() {
		return new String(answerTextfield.getText());
	}

	public String getHintfieldvalue() {
		return new String(hintTextfield.getText());
	}

	public String getSeletedQuestion() {
		return questionBox.getSelectionModel().getSelectedItem();
	}


}
