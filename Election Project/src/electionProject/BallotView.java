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

public class BallotView extends VBox implements EventHandler<ActionEvent> {

	private TextArea ballotStack_TA;

	private TextField addBallots_TF;

	private Button addBallots_Button;

	private Button countBallots_Button;

	private Main mainReference;
	
	private ResultsPie pieReference;

	private BallotStack stackReference;

	public BallotView(Main mainReference, ResultsPie pieReference) throws Exception {
		super();
		this.mainReference = mainReference;
		this.pieReference = pieReference;
		
		stackReference = new BallotStack(mainReference, pieReference);

		Label ballotTitle = new Label("Ballot Count");
		ballotTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		ballotTitle.setPadding(new Insets(5, 0, 30, 100));

		ballotStack_TA = new TextArea();
		ballotStack_TA.setEditable(false);
		ballotStack_TA.setFont(Font.font("Arial", 16));
		fillTA();

		HBox addBallots_HB = new HBox();
		Label addBallotsLabel = new Label("Add Ballots: ");
		addBallotsLabel.setFont(Font.font("Arial", 18));
		addBallots_TF = new TextField();
		addBallots_HB.getChildren().add(addBallotsLabel);
		addBallots_HB.getChildren().add(addBallots_TF);
		addBallots_HB.setPadding(new Insets(20, 10, 10, 0));
		addBallots_HB.setTranslateX(10);

		addBallots_Button = new Button("Add Ballots");
		addBallots_Button.setOnAction(this);
		addBallots_Button.setPrefSize(100, 40);
		addBallots_Button.setTranslateX(10);

		Separator buttonSeparator = new Separator();
		buttonSeparator.setPadding(new Insets(30, 0, 30, 0));

		countBallots_Button = new Button("Count Ballots");
		countBallots_Button.setOnAction(this);
		countBallots_Button.setPrefSize(110, 40);
		countBallots_Button.setTranslateX(10);

		this.getChildren().add(ballotTitle);
		this.getChildren().add(ballotStack_TA);
		this.getChildren().add(addBallots_HB);
		this.getChildren().add(addBallots_Button);
		this.getChildren().add(buttonSeparator);
		this.getChildren().add(countBallots_Button);
	}

	public void fillTA() throws FileNotFoundException {
		try {
			stackReference.readFile();
			String ballotList = stackReference.createOutput();
			ballotStack_TA.setText(ballotList);
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
		}
	}

	@Override
	public void handle(ActionEvent event) {
		try {
			if (event.getSource() == addBallots_Button) {
				if (addBallots_TF.getText().equals("")) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Input Error");
					alert.setContentText("Input Error! You must have a valid ballot to input.");
					alert.showAndWait();
				} else {
					String userBallot = String.valueOf(addBallots_TF.getText());
					stackReference.addBallot(userBallot);
					fillTA();
					addBallots_TF.clear();
				}
			} else if (event.getSource() == countBallots_Button) {
				String outputText = stackReference.countBallot(mainReference);
				ballotStack_TA.setText(outputText);
			}
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Ballot Error");
			alert.setContentText("There are no ballots left to count!");
			alert.showAndWait();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Error");
			alert.setContentText("An thrown with your file. Please use a valid text file");
			alert.showAndWait();
		}
	}
}
