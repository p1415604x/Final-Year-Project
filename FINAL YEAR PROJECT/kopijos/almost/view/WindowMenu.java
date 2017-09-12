package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;

public class WindowMenu extends MenuBar {

	private MenuItem loadItem, saveItem, exitItem, helpItem, aboutItem;

	public WindowMenu() {

		//Menu tabs
		Menu fileMenu = new Menu("File");
		Menu helpMenu = new Menu("Help");

		//Menu items

		saveItem = new MenuItem("_Save");
		saveItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+S"));
		fileMenu.getItems().add(saveItem);

		loadItem = new MenuItem("_Load");
		loadItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+L"));
		fileMenu.getItems().add(loadItem);

		fileMenu.getItems().add( new SeparatorMenuItem());

		exitItem = new MenuItem("E_xit");
		exitItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+X"));
		fileMenu.getItems().add(exitItem);


		helpItem = new MenuItem("_Help");
		helpItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+H"));
		helpMenu.getItems().add(helpItem);

		aboutItem = new MenuItem("_About");
		aboutItem.setAccelerator(KeyCombination.keyCombination("SHORTCUT+A"));
		helpMenu.getItems().add(aboutItem);

		this.getMenus().addAll(fileMenu, helpMenu);

	}

	//these methods allow handlers to be externally attached to this view and used by the controller
		public void addLoadHandler(EventHandler<ActionEvent> handler) {
			loadItem.setOnAction(handler);
		}

		public void addSaveHandler(EventHandler<ActionEvent> handler) {
			saveItem.setOnAction(handler);
		}

		public void addExitHandler(EventHandler<ActionEvent> handler) {
			exitItem.setOnAction(handler);
		}

		public void addAboutHandler(EventHandler<ActionEvent> handler) {
			aboutItem.setOnAction(handler);
		}

		public void addHelpHandler(EventHandler<ActionEvent> handler) {
			helpItem.setOnAction(handler);
		}


}
