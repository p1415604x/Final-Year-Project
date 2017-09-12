package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Profile;
import model.Questions;

public class GridView extends VBox {

	private Button generate, hint, check;
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
        myGridPane.setHgap(7); myGridPane.setVgap(7);
        myGridPane.setMinSize(200, 200);

		generate = new Button("GENERATE");
		generate.setMinSize(150, 27);

		check = new Button("CHECK");
		check.setMinSize(300, 27);
		check.setStyle("-fx-background-color: #e6b800;");
		Image CheckImage = new Image(getClass().getResourceAsStream("check.png"));
        check.setGraphic(new ImageView(CheckImage));
        check.setAlignment(Pos.BASELINE_LEFT);

	    VBox hb1 = new VBox();
	    VBox hb2 = new VBox();
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
	    hb2.getChildren().addAll(questionBox, check);
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

	public void setHintCount(Profile m) {
		hintcount.setText(m.getHintNumber());
	}

	public void decrementHintCount(Profile m) {
		if(m.getHintNumber().equals("Unlimited")) {
			//DO COMPLETELY NOTHING
		} else {
			int count = Integer.parseInt(hintcount.getText());
			if(count<=0) {
				hintfield.setText("YOU RAN OUT OF HINTS FOR THIS CROSSWORD");
				m.setHintNumber("YOU RAN OUT OF HINTS FOR THIS CROSSWORD");
			} else {
				count--;
				hintcount.setText(String.valueOf(count));
				m.setHintNumber(String.valueOf(count));
			}
		}
	}



	public String getSelectedItem() {
		return questionBox.getSelectionModel().getSelectedItem();
	}

    public void Crossword()
    {
    	int letters;							//HOLDS NUMBER OF LETTERS IN WORD
        int words = questionPool.size();
        int GridX = 1, GridY = 0;

    	Collections.sort(questionPool, new Comparator<Questions>() {	//SORTING ALGORITHM TO GET FROM SHORTEST TO LONGEST WORD
  		  public int compare(Questions o1, Questions o2) {
  		    return Integer.compare(o1.getAnswer().length(), o2.getAnswer().length());
  		  }
  		});
    Collections.reverse(questionPool); 									//MAKING DISCENDING - LONGEST FIRST
    for (int W=0; W<words; W++)				//RUN THROUGH WORDS
    	{
        	letters=questionPool.get(W).getAnswer().length();
        	textfieldlist.add(W, new ArrayList<TextField>());

        	textfieldlist.get(W).add(0, new TextField());
        	textfieldlist.get(W).get(0).setMaxSize(30, 40);
        	textfieldlist.get(W).get(0).setText(String.valueOf(questionPool.get(W).getNumber()+1));
        	textfieldlist.get(W).get(0).setStyle("-fx-text-inner-color: white; -fx-background-color: black;");
        	textfieldlist.get(W).get(0).setDisable(true);
        	myGridPane.add(textfieldlist.get(W).get(0), 0, GridY);

            		for (int L=0; L<letters; L++) {
            			textfieldlist.get(W).add(L+1, new TextField());
            			textfieldlist.get(W).get(L+1).setMaxSize(40, 40);
            			if (textfieldlist.get(W).get(L+1) != null) //IF TEXT FIELD IS CREATED
            			{
            				char c = questionPool.get(W).getAnswer().charAt(L); //GET LETTER
            				if ((c > 'A') || (c < 'Z'))  { //CHECK IF IT IS STILL NOT EMPTY
            				//	textfieldlist.get(W).get(L+1).setText(String.valueOf(c));  //ADDS ANSWER LETTERS ON GENERATION
                				myGridPane.add(textfieldlist.get(W).get(L+1), GridX, GridY);
                				GridX++;
            				}
            			}
            		}
            		GridY++;
            		GridX=1;
            		for(int i=0; i<=textfieldlist.get(W).size()-2; i++) {
            			this.focusListener(textfieldlist.get(W).get(i), textfieldlist.get(W).get(i+1));
            		}
            		this.focusListenerLast(textfieldlist.get(W).get(textfieldlist.get(W).size()-1));
            }
    }

    public Boolean checkInput() {
    	Boolean v = false;
    	int letters;
        int words = questionPool.size();
    	for (int W=0; W<words; W++) {
    			TextField tfx = (TextField) this.getNodeByRowColumnIndex(W, 0, myGridPane);
    			for(Questions c : questionPool) {
    				if(String.valueOf(c.getNumber()+1).equals(tfx.getText())) {
    					letters = c.getAnswer().length();
    					for(int L=0; L<letters; L++) {
    						tfx = (TextField) this.getNodeByRowColumnIndex(W, L+1, myGridPane);
    						if(String.valueOf(c.getAnswer().charAt(L)).toUpperCase().equals(tfx.getText().toUpperCase())) {
    							v= true;
    						} else {
    							v= false;
    							return v;
    						}
    					}
    				}
    			}
    	}
		return v;
    }

    public void populateGrid(Profile model) {
    	int letters;
        int words = questionPool.size();
        int d = 0;
        this.Clear();
        this.Crossword();
    	for (int W=0; W<words; W++) {
    			TextField tfx = (TextField) this.getNodeByRowColumnIndex(W, 0, myGridPane);
    			if(tfx!=null) {
    					letters = questionPool.get(0).getAnswer().length();
    					for(int L=0; L<letters; L++) {
    						if(this.getNodeByRowColumnIndex(W, L+1, myGridPane)!=null && model.getGridInput().get(d)!=null) {
    							textfieldlist.get(W).get(L+1).setText(model.getGridInput().get(d).getChar());
    							d++;
    						}
    					}
    			}
    	}
    }

    public void getGridInputView(Profile model) {
    	int letters;
        int words = questionPool.size();
    	for (int W=0; W<words; W++) {
    			TextField tfx = (TextField) this.getNodeByRowColumnIndex(W, 0, myGridPane);
    					letters = questionPool.get(0).getAnswer().length();
    					for(int L=0; L<letters; L++) {
    						if(this.getNodeByRowColumnIndex(W, L+1, myGridPane)!=null) {
    							tfx = (TextField) this.getNodeByRowColumnIndex(W, L+1, myGridPane);
    							model.setGridInput(tfx.getText(),W,L+1);
    						}
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


/*    private int letterMatchX(Questions q) { //WHICH POSITION MATCHES ON WORD GIVEN
    	int n = -1;
    	for (Questions qs : questionPool) {
    		if (q != qs) {
    			char[] charArray1 = qs.getAnswer().toCharArray();

    			for (char ch: charArray1) {
    				if (q.getAnswer().indexOf(ch) != -1) {
    					return q.getAnswer().indexOf(ch);
    				} else {
    					n = -1;
    				}
    			}
    		}
    	}
		return n;
    }

    private String whichWord(Questions q) {  //GET THE MATCHING WORD
    	String s = null;
    	for (Questions qs : questionPool) {
    		if (q != qs) {
    			char[] charArray1 = q.getAnswer().toCharArray();

    			for (char ch: charArray1) {
    				if (qs.getAnswer().indexOf(ch) != -1) {
    					return qs.getAnswer();
    				} else {
    					s = null;
    				}
    			}
    		}
    	}
		return s;
    }

    private int letterMatchY(Questions q) { //WHICH POSITION MATCHES ON WORD FROM LIST
    	int n = -1;
    	for (Questions qs : questionPool) {
    		if (q != qs) {
    			char[] charArray1 = q.getAnswer().toCharArray();

    			for (char ch: charArray1) {
    				if (qs.getAnswer().indexOf(ch) != -1) {
    					return qs.getAnswer().indexOf(ch);
    				} else {
    					n = -1;
    				}
    			}
    		}
    	}
		return n;
    }
*/

    @SuppressWarnings("static-access")
	public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }


	// HANDLER
	public void addGenerateHandler(EventHandler<ActionEvent> handler) {
		generate.setOnAction(handler);
	}

	public void addHintHandler(EventHandler<ActionEvent> handler) {
		hint.setOnAction(handler);
	}


	public void addCheckHandler(EventHandler<ActionEvent> handler) {
		check.setOnAction(handler);
	}

	private void focusListener(TextField tf1, TextField tf2) {
        tf1.textProperty().addListener((obs, oldText, newText) -> {
            if (oldText.length() < 1 && newText.length() >= 1) {
                tf2.requestFocus();
                tf2.setText("");
            }
            if (newText.length() > 1) {
                tf1.setText(newText.substring(1));
                tf2.requestFocus();
                tf2.setText("");
            }
        });
    }

	private void focusListenerLast(TextField tf) {
		tf.textProperty().addListener((obs, oldText, newText) -> {
			if (newText.length() > 1) {
                tf.setText(newText.substring(1));
			}
		});
	}


}


