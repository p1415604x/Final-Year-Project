package view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Questions;

public class GridView extends VBox {

	private Button generate;
	private GridPane myGridPane;
	private ArrayList<ArrayList<TextField>> textfieldlist = new ArrayList<ArrayList<TextField>>();
	private ArrayList<Questions> questionPool = new ArrayList<Questions>();

	public GridView() {


	    myGridPane = new GridPane();
        myGridPane.setPadding(new Insets(40, 40, 20, 20));
        myGridPane.setHgap(7); myGridPane.setVgap(7);
        myGridPane.setMinSize(200, 200);

		generate = new Button("Generate");

	    FlowPane flow = new FlowPane();
	    flow.setPadding(new Insets(20, 50, 60, 50));
	    flow.setVgap(40);
	    flow.setHgap(4);
	    flow.setPrefWrapLength(170); // preferred width allows for two columns

	    HBox hb1 = new HBox();

	    hb1.getChildren().add(generate);

	    flow.getChildren().addAll(myGridPane, hb1);

		this.getChildren().addAll(flow);

	}
	//METHODS/FUNCTIONALITY
    public void Crossword()
    {
    	Collections.sort(questionPool, new Comparator<Questions>() {	//SORTING ALGORITHM TO GET LONGEST WORDS ASCENDING
  		  public int compare(Questions o1, Questions o2) {
  		    return Integer.compare(o1.getAnswer().length(), o2.getAnswer().length());
  		  }
  		});
    Collections.reverse(questionPool); 									//MAKING DISCENDING
    	int letters;
        int words = questionPool.size();
        for (int y=0; y<words; y++)
        {
        	letters=questionPool.get(y).getAnswer().length();
        	textfieldlist.add(y, new ArrayList<TextField>());
            for (int x=0; x<letters; x++)
            {
            	if(x<questionPool.get(y).getAnswer().length()) {
            	textfieldlist.get(y).add(x, new TextField());
            	textfieldlist.get(y).get(x).setMaxSize(40, 40);
                	if (textfieldlist.get(y).get(x) != null) //IF TEXT FIELD IS CREATED
                	{
                		char c = questionPool.get(y).getAnswer().charAt(x); //GET LETTER
                		if ((c > 'A') || (c < 'Z'))  { //CHECK IF IT IS STILL NOT EMPTY
                			textfieldlist.get(y).get(x).setText(String.valueOf(c));
                			myGridPane.add(textfieldlist.get(y).get(x), x, y);
                			/*if(x==letters-1) {
                				textfieldlist.get(y).add(x+1, new TextField());
                            	textfieldlist.get(y).get(x+1).setMaxSize(40, 40);
                            	textfieldlist.get(y).get(x+1).setStyle("-fx-background-color: black;");
                            	textfieldlist.get(y).get(x+1).setDisable(true);
                    			myGridPane.add(textfieldlist.get(y).get(x+1), x+1, y);
                			}*/
                		} else {
                			myGridPane.add(new Label(), x, y); //MAKE BLIND SPOT
                		}
                	}
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



	// HANDLER
	public void addGenerateHandler(EventHandler<ActionEvent> handler) {
		generate.setOnAction(handler);
	}

}


