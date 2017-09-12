package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Profile;
import model.Questions;

public class GridView extends VBox {

	private Button generate, hint;
	private GridPane myGridPane;
	private ArrayList<ArrayList<TextField>> textfieldlist;
	private ArrayList<Questions> questionPool;
	private ListView<String> questionBox;
	private ObservableList<String> questions;
	private TextField hintfield, hintcount;

	@SuppressWarnings("static-access")
	public GridView() {

		textfieldlist = new ArrayList<ArrayList<TextField>>();
		questionPool = new ArrayList<Questions>();

	    myGridPane = new GridPane();
     //   myGridPane.setPadding(new Insets(40, 40, 20, 20));
        myGridPane.setHgap(7); myGridPane.setVgap(7);
        myGridPane.setMinSize(200, 200);

		generate = new Button("GENERATE");
		generate.setMinSize(150, 27);

	    VBox hb1 = new VBox();
	    HBox hb2 = new HBox();
	    HBox hb3 = new HBox();

	    questionBox = new ListView<String>();
	    questionBox.setMinSize(300, 150);
	    questions = FXCollections.observableArrayList();

	    hint = new Button("HINT");
	    hintfield = new TextField();
	    hintcount = new TextField();
	    hintfield.setDisable(true);
	    hintfield.setMinSize(400, 27);
	    hintcount.setMaxSize(70, 40);
	    hintcount.setDisable(true);


	    hb1.getChildren().add(generate);
	    hb2.getChildren().add(questionBox);
	    hb3.getChildren().addAll(hint, hintfield, hintcount);


	    AnchorPane myAnchorPane = new AnchorPane(myGridPane,hb1,hb2,hb3);

        myAnchorPane.setBottomAnchor(hb1, -75.0);
        myAnchorPane.setRightAnchor(hb1, 250.0);
	    myAnchorPane.setLeftAnchor(hb2, 50.0);
	    myAnchorPane.setTopAnchor(hb2, 50.0);
	    myAnchorPane.setBottomAnchor(hb3, -75.0);
	    myAnchorPane.setLeftAnchor(hb3, 50.0);
	    myAnchorPane.setRightAnchor(myGridPane, 50.0);
	    myAnchorPane.setTopAnchor(myGridPane, 50.0);

		this.getChildren().addAll(myAnchorPane);



	}
	//METHODS/FUNCTIONALITY
	public void setQuestions(Profile model) {
		questions.clear();
		model.getQuestionsFromProfile().stream().map(Questions::getNumberandQuestion).forEach(e->questions.add(e));
		questionBox.setItems(questions);
	}

	public void setHintTextBox(Profile model) {

		model.getQuestionsFromProfile().forEach(e-> {
			if(e.getQuestion().contains(this.getSelectedItem().substring(3))) {
				hintfield.setText(e.getHint());
			}
		});
	}

	public void setHintCount(String x) {
		hintcount.setText(x);
	}

	public void decrementHintCount() {
		if(hintcount.getText().equals("Unlimited")) {
			//DO COMPLETELY NOTHING
		} else {
			int count = Integer.parseInt(hintcount.getText());
			if(count<=0) {
				hintfield.setText("YOU RAN OUT OF HINTS FOR THIS CROSSWORD");
			} else {
				count--;
				hintcount.setText(String.valueOf(count));
			}
		}
	}



	public String getSelectedItem() {
		return questionBox.getSelectionModel().getSelectedItem();
	}

    public void Crossword()
    {
    	Collections.sort(questionPool, new Comparator<Questions>() {	//SORTING ALGORITHM TO GET FROM SHORTEST TO LONGEST WORD
  		  public int compare(Questions o1, Questions o2) {
  		    return Integer.compare(o1.getAnswer().length(), o2.getAnswer().length());
  		  }
  		});
    Collections.reverse(questionPool); 									//MAKING DISCENDING - LONGEST FIRST
    int letters;							//HOLDS NUMBER OF LETTERS IN WORD
    int words = questionPool.size();
    int GridX, GridY = 0;
    for (int W=0; W<words; W++)
    	{
        	letters=questionPool.get(W).getAnswer().length();
        	textfieldlist.add(W, new ArrayList<TextField>());
        	if((W>=1) && (this.wordmatch(questionPool.get(W-1)) != -1)) {
				GridX = this.Xmatch(questionPool.get(W-1), this.wordmatch(questionPool.get(W-1)));
			//	System.out.println(this.wordmatch(questionPool.get(W-1)));
			//	System.out.println(this.Xmatch(questionPool.get(W-1), this.wordmatch(questionPool.get(W-1))));
				GridY = W;
        		for (int L=0; L<letters-1; L++)	{
            			textfieldlist.get(W).add(L, new TextField());
            			textfieldlist.get(W).get(L).setMaxSize(40, 40);
            			if (textfieldlist.get(W).get(L) != null) { //IF TEXT FIELD IS CREATED
            				char c = questionPool.get(W).getAnswer().charAt(L+1); //GET LETTER not including first
            				if ((c > 'A') || (c < 'Z'))  { //CHECK IF IT IS STILL NOT EMPTY
            					textfieldlist.get(W).get(L).setText(String.valueOf(c));
            	     //   		System.out.println(c);
                				myGridPane.add(textfieldlist.get(W).get(L), GridX, GridY);
                				GridY++;
            				}
            			}
            			if(L==letters-1) {
            				questionPool.remove(W);
            				words--;
            			}
            	}
        	} else {
        		GridX=0;
            		for (int L=0; L<letters; L++) {
            			textfieldlist.get(W).add(L, new TextField());
            			textfieldlist.get(W).get(L).setMaxSize(40, 40);
            			if (textfieldlist.get(W).get(L) != null) //IF TEXT FIELD IS CREATED
            			{
            				char c = questionPool.get(W).getAnswer().charAt(L); //GET LETTER
            				if ((c > 'A') || (c < 'Z'))  { //CHECK IF IT IS STILL NOT EMPTY
            					textfieldlist.get(W).get(L).setText(String.valueOf(c));
                				myGridPane.add(textfieldlist.get(W).get(L), GridX, GridY);
                				GridX++;
                				/*if(L==letters-1) {
                				textfieldlist.get(W).add(L+1, new TextField());
                            	textfieldlist.get(W).get(L+1).setMaxSize(40, 40);
                            	textfieldlist.get(W).get(L+1).setStyle("-fx-background-color: black;");
                            	textfieldlist.get(W).get(L+1).setDisable(true);
                    			myGridPane.add(textfieldlist.get(W).get(L+1), L+1, W);
                			}*/
            				} else {
                				myGridPane.add(new Label(), GridX, GridY); //MAKE BLIND SPOT
                			}
            			}
            		}
            		GridY++;
            }
        }
    }

    public void Clear() {
    	myGridPane.getChildren().clear();
    	textfieldlist.clear();
    }

    public ArrayList<Questions> getQuestions(ArrayList<Questions> list) {
    	questionPool = list;
		return questionPool;
    }

   /* private int Xmatching(Questions q) { //FOR ANY LETTER IN THE WORD
    	int n = -1;
    	for (Questions qs : questionPool) {
    		for(char c : qs.getAnswer().toCharArray()){
    			if(q.getAnswer().indexOf(c) != -1) {
    				n = q.getAnswer().indexOf(c);
    			} else {
    				n = -1;
    			}
    		}
    	}
		return n;
    } */

    private int wordmatch(Questions q) { //FOR FIRST LETTER IN THE WORD
    	int n = -1;
    	for (Questions qs : questionPool) {
    		if (q != qs) {
    			if(q.getAnswer().indexOf(qs.getAnswer().charAt(0)) != -1) {
    				return n = questionPool.indexOf(qs);
    			} else {
    				n = -1;
    			}
    		}
    	}
		return n;
    }


    private int Xmatch(Questions q, int i) { //FOR FIRST LETTER IN THE WORD
     	return q.getAnswer().indexOf(questionPool.get(i).getAnswer().charAt(0));
    }




	// HANDLER
	public void addGenerateHandler(EventHandler<ActionEvent> handler) {
		generate.setOnAction(handler);
	}

	public void addHintHandler(EventHandler<ActionEvent> handler) {
		hint.setOnAction(handler);
	}

}


