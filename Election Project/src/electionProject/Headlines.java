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

public class Headlines extends VBox implements EventHandler<ActionEvent> {

	private TextField headline_TF;

	private TextField addHeadline_TF;

	private Button addHeadline_Button;

	private Button removeCurrentHeadline_Button;

	private Main mainReference;

	private DCLinkedList linkedListRef;

	private Runnable displayThread;

	public Headlines(Main mainReference) throws FileNotFoundException {
		super();
		this.mainReference = mainReference;

		linkedListRef = new DCLinkedList();

		Label headlinesTitle = new Label("Current Headlines");
		headlinesTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		headlinesTitle.setPadding(new Insets(5, 0, 5, 500));

		headline_TF = new TextField("");
		headline_TF.setFont(Font.font("Arial", FontWeight.BOLD, 16));
		headline_TF.setEditable(false);
		headline_TF.setMaxWidth(500);
		headline_TF.setAlignment(Pos.CENTER);
		headline_TF.setTranslateX(350);

		displayHeadline();

		HBox addHeadline_HB = new HBox();
		Label addHeadlineLabel = new Label("New Headline: ");
		addHeadlineLabel.setFont(Font.font("Arial", 18));
		addHeadline_TF = new TextField();
		addHeadline_TF.setMinWidth(200);
		addHeadline_HB.getChildren().add(addHeadlineLabel);
		addHeadline_HB.getChildren().add(addHeadline_TF);
		addHeadline_HB.setPadding(new Insets(0, 0, 5, 0));
		addHeadline_HB.setTranslateX(10);

		addHeadline_Button = new Button("Add Headline");
		addHeadline_Button.setOnAction(this);
		addHeadline_Button.setPrefSize(120, 40);
		addHeadline_Button.setTranslateX(10);

		Separator buttonSeparator = new Separator();
		buttonSeparator.setPadding(new Insets(5, 0, 5, 0));

		removeCurrentHeadline_Button = new Button("Remove Current Headline");
		removeCurrentHeadline_Button.setOnAction(this);
		removeCurrentHeadline_Button.setPrefSize(160, 40);
		removeCurrentHeadline_Button.setTranslateX(10);

		this.getChildren().add(headlinesTitle);
		this.getChildren().add(headline_TF);
		this.getChildren().add(addHeadline_HB);
		this.getChildren().add(addHeadline_Button);
		this.getChildren().add(buttonSeparator);
		this.getChildren().add(removeCurrentHeadline_Button);
	}

	public void setThread() {
		displayThread = new Runnable() {
			@Override
			public void run() {
				String currentHeadline = linkedListRef.getHeadline();
				headline_TF.setText(currentHeadline);
			}
		};
	}

	public void displayHeadline() throws FileNotFoundException {
		try {
			linkedListRef.readFile();

			setThread();

			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.scheduleAtFixedRate(displayThread, 0, 3, TimeUnit.SECONDS);

		} catch (FileNotFoundException ex) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
		}
	}

	@Override
	public void handle(ActionEvent event) {
		try {
			if (event.getSource() == addHeadline_Button) {
				if (addHeadline_TF.getText().equals("")) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Input Error");
					alert.setContentText("Input Error! You must have a valid headline to input.");
					alert.showAndWait();
				} else {
					displayThread = null;
					String userHeadline = String.valueOf(addHeadline_TF.getText());
					linkedListRef.addHeadline(userHeadline);
					addHeadline_TF.clear();
					setThread();
				}
			} else if (event.getSource() == removeCurrentHeadline_Button) {
				displayThread = null;
				linkedListRef.removeHeadline();
				setThread();
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Error");
			alert.setContentText("An thrown with your file. Please use a valid text file");
			alert.showAndWait();
		}
	}

}
