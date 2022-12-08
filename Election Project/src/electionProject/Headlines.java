package electionProject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This class is responsible for creating the front-end of the headlines section of the GUI
 * @author Michael Opheim
 * @version 12/07/2022
 */
public class Headlines extends VBox implements EventHandler<ActionEvent> {

	/** A TextField displaying headlines (where the headlines from a text file will be shown) */
	private TextField headline_TF;

	/** A TextField that allows the user to input their own headlines */
	private TextField addHeadline_TF;

	/** Button that allows the user to submit their headline */
	private Button addHeadline_Button;

	/** Button that allows the user to remove the currently displayed headline */
	private Button removeCurrentHeadline_Button;

	/** Reference to the circularly, doubly-linked list being used to create the headline carousel */
	private CDLinkedList LLReference;

	/** Runnable object that creates threads for the headline carousel */
	private Runnable displayThread;

	/**
	 * Constructor that builds the class in the GUI
	 * @throws FileNotFoundException if a text file to be read cannot be found by Scanner
	 */
	public Headlines() throws FileNotFoundException {
		super();

		// Create a reference to CDLinkedList (the back-end of this class)
		LLReference = new CDLinkedList();

		// Create a title for this section of this GUI
		Label headlinesTitle = new Label("Current Headlines");
		headlinesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		headlinesTitle.setPadding(new Insets(5, 0, 5, 500));

		// Instantiate the TextField for headlines
		headline_TF = new TextField("");
		headline_TF.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		headline_TF.setEditable(false);
		headline_TF.setMaxWidth(500);
		headline_TF.setAlignment(Pos.CENTER);
		headline_TF.setTranslateX(350);

		// Display headlines in the headline TextField
		displayHeadline();

		// Create an area in the GUI for the user to input their own headlines 
		HBox addHeadline_HB = new HBox();
		Label addHeadlineLabel = new Label("New Headline: ");
		addHeadlineLabel.setFont(Font.font("Arial", 18));
		addHeadline_TF = new TextField();
		addHeadline_TF.setMinWidth(200);
		addHeadline_HB.getChildren().add(addHeadlineLabel);
		addHeadline_HB.getChildren().add(addHeadline_TF);
		addHeadline_HB.setPadding(new Insets(0, 0, 5, 0));
		addHeadline_HB.setTranslateX(10);

		// Instantiate a Button that allows the user to input their own headlines
		addHeadline_Button = new Button("Add Headline");
		addHeadline_Button.setOnAction(this);
		addHeadline_Button.setPrefSize(120, 40);
		addHeadline_Button.setTranslateX(10);

		// Add a separator to the GUI to aid readability
		Separator buttonSeparator = new Separator();
		buttonSeparator.setPadding(new Insets(5, 0, 5, 0));

		// Instantiate a Button that allows the user to remove the currently displayed headline
		removeCurrentHeadline_Button = new Button("Remove Current Headline");
		removeCurrentHeadline_Button.setOnAction(this);
		removeCurrentHeadline_Button.setPrefSize(160, 40);
		removeCurrentHeadline_Button.setTranslateX(10);

		// Populate this section of the GUI
		this.getChildren().add(headlinesTitle);
		this.getChildren().add(headline_TF);
		this.getChildren().add(addHeadline_HB);
		this.getChildren().add(addHeadline_Button);
		this.getChildren().add(buttonSeparator);
		this.getChildren().add(removeCurrentHeadline_Button);
	}

	/**
	 * Starts and runs a new thread for the headline carousel
	 */
	public void setThread() {
		
		// Insantiate a Runnable object to run threads
		displayThread = new Runnable() {
			
			@Override
			/**
			 * Start cycling through headlines
			 */
			public void run() {
				
				// use the back-end method, getHeadline(), to retreive the current headline in the thread and display it in the GUI
				String currentHeadline = LLReference.getHeadline();
				headline_TF.setText(currentHeadline);
			}
		};
	}

	/**
	 * A method to display a headline in the GUI
	 * @throws FileNotFoundException if a text file to be read cannot be found by Scanner
	 */
	public void displayHeadline() throws FileNotFoundException {
		try {
			
			// Read a file
			LLReference.readFile();

			// Start a new thread for the headline carousel
			setThread();

			// Switch which headline in the back-end linked list gets displayed every three seconds
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(displayThread, 0, 3, TimeUnit.SECONDS);

		// Create an alert if a text file cannot be found by Scanner
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
		}
	}

	@Override
	/**
	 * Handles button presses for adding and removing headlines
	 * 
	 * @param event The registered event
	 */
	public void handle(ActionEvent event) {
		try {
			
			// If the user is adding a headline to the linked list...
			if (event.getSource() == addHeadline_Button) {
				
				// If the user has not inputted a headline, alert them
				if (addHeadline_TF.getText().equals("")) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Input Error");
					alert.setContentText("Input Error! You must have a valid headline to input.");
					alert.showAndWait();
					
				// Otherwise, add the user's headline to the linked list and reset the thread
				} else {
					
					// Stop the thread
					displayThread = null; 
					String userHeadline = String.valueOf(addHeadline_TF.getText());
					
					// Pass the user's headline to the back-end to add it to the linked list 
					LLReference.addHeadline(userHeadline);
					
					// Clear out user input from the 'Add headlines' TextField
					addHeadline_TF.clear();
					
					// Restart the thread
					setThread();
				}
			
			// If the user is removing the current headline from the display...
			} else if (event.getSource() == removeCurrentHeadline_Button) {
				
				// Remove the headline in the back-end and restart the thread
				displayThread = null;
				LLReference.removeHeadline();
				setThread();
			}
		
		// If the user is attempting to remove a headline from an empty linked list, alert them
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Headline Error");
			alert.setContentText("There are no headlines left to remove!");
			alert.showAndWait();
			
		// Create an alert if there are errors within a user's text file
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Error");
			alert.setContentText("An thrown with your file. Please use a valid text file");
			alert.showAndWait();
		}
	}

}
