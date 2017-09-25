package view;

import java.util.Collection;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Module;

public class selectModulesForm extends FlowPane {

	private ListView<Module> unselectedModules, selectedModules;
	private ObservableList<Module> items, items2;
	private Button btAdd, btRemove, btSubmit, btReset;
	private TextField tfCurrentCredit;

	selectModulesForm() {

		FlowPane myFlowPane = new FlowPane();

		unselectedModules = new ListView<Module>();
		unselectedModules.setMaxHeight(250);
		unselectedModules.setDisable(true);
		Label lbunselectedModules = new Label("Unselected Modules:");
		items = FXCollections.observableArrayList();

		selectedModules = new ListView<Module>();
		selectedModules.setMaxHeight(250);
		selectedModules.setDisable(true);
		Label lbselectedModules = new Label("Selected Modules:");
		items2 = FXCollections.observableArrayList();

		HBox myLabelBox = new HBox();
		myLabelBox.getChildren().addAll(lbunselectedModules, lbselectedModules);
		myLabelBox.setPadding(new Insets(20,0,0,20));
		myLabelBox.setSpacing(158);

		HBox myHbox = new HBox();
		myHbox.setSpacing(20);
		myHbox.setPadding(new Insets(0,20,20,20));
		myHbox.getChildren().addAll(unselectedModules, selectedModules);

		HBox buttonBox = new HBox();
		buttonBox.setSpacing(20);
		buttonBox.setPadding(new Insets(20,20,20,130));

		//BUTTONS
		btReset = new Button("Reset");
		btRemove = new Button("Remove");
		btAdd = new Button("Add");
		btSubmit = new Button("Submit");

		btReset.setDisable(true);
		btRemove.setDisable(true);
		btAdd.setDisable(true);
		btSubmit.setDisable(true);

		btReset.setMinWidth(60);
		btRemove.setMinWidth(60);
		btAdd.setMinWidth(60);
		btSubmit.setMinWidth(60);

        VBox credits = new VBox();
        Label lbCurrentCredit = new Label("Current Credits:");
        tfCurrentCredit = new TextField();
        tfCurrentCredit.setDisable(true);

        credits.getChildren().addAll(lbCurrentCredit, tfCurrentCredit);
        credits.setPadding(new Insets(0,20,0,200));

		buttonBox.getChildren().addAll(btReset, btRemove, btAdd, btSubmit);

		myFlowPane.getChildren().addAll(myLabelBox, myHbox, credits, buttonBox);

		this.getChildren().add(myFlowPane);
	}

	//these methods allow handlers to be externally attached to this view

	public void addCreditChangeListener(ChangeListener<String> listener) {
		tfCurrentCredit.textProperty().addListener(listener);
	}

	public void addAddHandler(EventHandler<ActionEvent> handler) {
		btAdd.setOnAction(handler);
	}

	public void addResetHandler(EventHandler<ActionEvent> handler) {
		btReset.setOnAction(handler);
	}

	public void addRemoveHandler(EventHandler<ActionEvent> handler) {
		btRemove.setOnAction(handler);
	}

	public void addSubmitHandler(EventHandler<ActionEvent> handler) {
		btSubmit.setOnAction(handler);
	}

	public void setUnselectedItemsCollection(Collection<Module> collection) {
		items.clear();
		items.addAll(collection);
		unselectedModules.setItems(items);
	}

	public void setUnselectedItems(Module m) {
		items.add(m);
		unselectedModules.setItems(items);
	}

	public Module getUnselectedItem() {
		return unselectedModules.getSelectionModel().getSelectedItem();
	}

	public Module getSelectedItem() {
		return selectedModules.getSelectionModel().getSelectedItem();
	}

	public ObservableList<Module> getUnselectedItems() {
		return items;
	}

	public void setSelectedItems(Module m) {
		items2.add(m);
		selectedModules.setItems(items2);
	}

	public void setSelectedItemsCollection(Collection<Module> collection) {
		items2.clear();
		items2.addAll(collection);
		selectedModules.setItems(items2);
	}

	public ObservableList<Module> getSelectedItems() {
		return items2;
	}

	public void removeSelectedItemsFocusedItem() {
		items2.remove(selectedModules.getSelectionModel().getSelectedItem());
	}

	public void removeUnselectedItemsFocusedItem() {
		items.remove(unselectedModules.getSelectionModel().getSelectedItem());
	}

	public void enableListviews() {
		selectedModules.setDisable(false);
		unselectedModules.setDisable(false);
		btReset.setDisable(false);
		btRemove.setDisable(false);
		btAdd.setDisable(false);
	}

	public void enableSubmit() {
		btSubmit.setDisable(false);
	}

	public void disableSubmit() {
		btSubmit.setDisable(true);
	}

	public void addCredits(Module m) {
		int c = Integer.parseInt(tfCurrentCredit.getText());
		c += m.getCredits();
		tfCurrentCredit.setText(String.valueOf(c));
	}

	public void subtractCredits(Module m) {
		int c = Integer.parseInt(tfCurrentCredit.getText());
		c -= m.getCredits();
		tfCurrentCredit.setText(String.valueOf(c));
	}

	public void resetCredits() {
		tfCurrentCredit.setText(String.valueOf(0));
	}

	public int getCredits() {
		return Integer.parseInt(tfCurrentCredit.getText());
	}

	public void refreshCredits() {
		int value = 0;
		for(Module p : items2) {
			value += p.getCredits();
		}
		tfCurrentCredit.setText(String.valueOf(value));
	}

}
