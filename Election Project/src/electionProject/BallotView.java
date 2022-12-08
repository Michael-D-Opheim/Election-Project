package electionProject;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This class is responsible for creating the front-end of the ballot count
 * section of the GUI. It also has options for the user to add a new ballot to
 * or delete a ballot from a corresponding ballot stack.
 * 
 * @author Michael Opheim
 * @version 12/07/2022
 *
 */
public class BallotView extends VBox implements EventHandler<ActionEvent> {

	/** An area to display the collected ballots */
	private TextArea ballotStack_TA;

	/**
	 * A TextField that allows the user to input their own ballots into the ballot
	 * stack
	 */
	private TextField addBallots_TF;

	/** Button that allows the user to submit their ballot */
	private Button addBallots_Button;

	/**
	 * Button that allows the user to remove (or “count”) a ballot from the ballot
	 * stack
	 */
	private Button countBallots_Button;

	@SuppressWarnings("unused")
	/** A reference to the ResultsPie class */
	private ResultsPie pieReference;

	/** A reference to the BallotStack class (the back-end of this class) */
	private BallotStack stackReference;

	/**
	 * Constructor that builds the class in the GUI
	 * 
	 * @param pieReference A reference to ResultsPie
	 * @throws Exception if something goes wrong with file reading or file writing
	 */
	public BallotView(ResultsPie pieReference) throws Exception {
		super();
		this.pieReference = pieReference;

		// Create a reference to BallotStack (the back-end of this class)
		stackReference = new BallotStack(pieReference);

		// Create a title for this section of this GUI
		Label ballotTitle = new Label("Ballot Count");
		ballotTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		ballotTitle.setPadding(new Insets(5, 0, 30, 100));

		// Instantiate the TextArea for ballots
		ballotStack_TA = new TextArea();
		ballotStack_TA.setEditable(false);
		ballotStack_TA.setFont(Font.font("Arial", 16));
		fillTA(); // fill the TextArea

		// Create an area in the GUI for the user to input their own ballots
		HBox addBallots_HB = new HBox();
		Label addBallotsLabel = new Label("Add Ballots: ");
		addBallotsLabel.setFont(Font.font("Arial", 18));
		addBallots_TF = new TextField();
		addBallots_HB.getChildren().add(addBallotsLabel);
		addBallots_HB.getChildren().add(addBallots_TF);
		addBallots_HB.setPadding(new Insets(20, 10, 10, 0));
		addBallots_HB.setTranslateX(10);

		// Instantiate a Button that allows the user to input their own ballots
		addBallots_Button = new Button("Add Ballots");
		addBallots_Button.setOnAction(this);
		addBallots_Button.setPrefSize(100, 40);
		addBallots_Button.setTranslateX(10);

		// Add a separator to the GUI to aid readability
		Separator buttonSeparator = new Separator();
		buttonSeparator.setPadding(new Insets(30, 0, 30, 0));

		// Instantiate a Button that allows the user to count/remove ballots
		countBallots_Button = new Button("Count Ballots");
		countBallots_Button.setOnAction(this);
		countBallots_Button.setPrefSize(110, 40);
		countBallots_Button.setTranslateX(10);

		// Populate this section of the GUI
		this.getChildren().add(ballotTitle);
		this.getChildren().add(ballotStack_TA);
		this.getChildren().add(addBallots_HB);
		this.getChildren().add(addBallots_Button);
		this.getChildren().add(buttonSeparator);
		this.getChildren().add(countBallots_Button);
	}

	/**
	 * Makes calls to the back-end (BallotStack) in order to accumulate ballots to
	 * display to the user
	 * 
	 * @throws FileNotFoundException if a text file to be read is not found
	 */
	public void fillTA() throws FileNotFoundException {
		try {

			// Read a text file and put its contents the TextArea
			stackReference.readFile();
			String ballotList = stackReference.createOutput();
			ballotStack_TA.setText(ballotList);

		// If a text file is not found by Scanner, create an alert
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
		}
	}

	@Override
	/**
	 * Handles button presses for adding and removing ballots
	 * 
	 * @param event The registered event
	 */
	public void handle(ActionEvent event) {
		try {
			
			// If the user is adding a ballot to the stack...
			if (event.getSource() == addBallots_Button) {
				
				// If the user has not inputted a ballot, alert them
				if (addBallots_TF.getText().equals("")) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Input Error");
					alert.setContentText("Input Error! You must have a valid ballot to input.");
					alert.showAndWait();
					
				// Otherwise pass the ballot to the back-end and create some new text for outputting in the GUI
				} else {
					String userBallot = String.valueOf(addBallots_TF.getText());
					
					// Add the ballot to a stack in the back-end
					stackReference.addBallot(userBallot);
					
					// Re-fill the TextArea in the GUI
					fillTA(); 
					
					// Clear out user input from the 'Add ballots' TextField
					addBallots_TF.clear();
				}
				
			// If the user is removing a ballot from the stack...
			} else if (event.getSource() == countBallots_Button) {
				
				// Count the ballot in the back-end and create some new text for outputting in the GUI
				String outputText = stackReference.countBallot();
				ballotStack_TA.setText(outputText);
			}
			
		// If the user is attempting to remove a ballot from an empty stack, alert them
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ballot Error");
			alert.setContentText("There are no ballots left to count!");
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
