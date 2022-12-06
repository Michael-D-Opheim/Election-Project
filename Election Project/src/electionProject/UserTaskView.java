package electionProject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;

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

public class UserTaskView extends VBox implements EventHandler<ActionEvent> {

	private TextArea topTask_TA;

	private TextField topPriority_TF;

	private TextField userTask_TF;

	private TextField userPriority_TF;

	private Button addTask_Button;

	private Button removeTask_Button;

	private Main mainReference;

	private MinHeap heapReference;

	public UserTaskView(Main mainReference) {
		super();
		this.mainReference = mainReference;

		heapReference = new MinHeap();

		Label topTaskTitle = new Label("Top Task");
		topTaskTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		topTaskTitle.setPadding(new Insets(5, 0, 30, 110));

		HBox topTask_HB = new HBox();
		Label taskLabel = new Label("Top Priority: ");
		taskLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		topTask_TA = new TextArea();
		topTask_TA.setEditable(false);
		topTask_TA.setFont(Font.font("Arial", 14));
		topTask_TA.setPrefHeight(10);
		topTask_TA.setPrefWidth(200);
		topTask_HB.getChildren().add(taskLabel);
		topTask_HB.getChildren().add(topTask_TA);
		topTask_HB.setPadding(new Insets(10, 0, 10, 0));
		topTask_HB.setTranslateX(10);

		HBox topPriority_HB = new HBox();
		Label topPriorityLabel = new Label("Priority Number: ");
		topPriorityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		topPriority_TF = new TextField();
		topPriority_TF.setEditable(false);
		topPriority_TF.setFont(Font.font("Arial", 14));
		topPriority_TF.setMaxWidth(45);
		topPriority_HB.getChildren().add(topPriorityLabel);
		topPriority_HB.getChildren().add(topPriority_TF);
		topPriority_HB.setPadding(new Insets(10, 0, 10, 0));
		topPriority_HB.setTranslateX(10);

		displayTopTask();

		Separator topTaskSeparator = new Separator();
		topTaskSeparator.setPadding(new Insets(30, 0, 30, 0));
		
		Label addTaskLabel = new Label("Add Task: ");
		addTaskLabel.setFont(Font.font("Arial", 18));
		addTaskLabel.setPadding(new Insets(0, 10, 10, 0));
		addTaskLabel.setTranslateX(10);

		HBox userTask_HB = new HBox();
		Label userTaskTFLabel = new Label("Task: ");
		userTaskTFLabel.setFont(Font.font("Arial", 18));
		userTask_TF = new TextField();
		userTask_TF.setFont(Font.font("Arial", 14));
		userTask_HB.getChildren().add(userTaskTFLabel);
		userTask_HB.getChildren().add(userTask_TF);
		userTask_HB.setPadding(new Insets(10, 0, 5, 0));
		userTask_HB.setTranslateX(10);

		HBox userPriority_HB = new HBox();
		Label userPriorityTFLabel = new Label("Priority: ");
		userPriorityTFLabel.setFont(Font.font("Arial", 18));
		userPriority_TF = new TextField();
		userPriority_TF.setFont(Font.font("Arial", 14));
		userPriority_TF.setMaxWidth(45);
		userPriority_HB.getChildren().add(userPriorityTFLabel);
		userPriority_HB.getChildren().add(userPriority_TF);
		userPriority_HB.setPadding(new Insets(5, 0, 10, 0));
		userPriority_HB.setTranslateX(10);

		addTask_Button = new Button("Add Task");
		addTask_Button.setOnAction(this);
		addTask_Button.setPrefSize(90, 40);
		addTask_Button.setTranslateX(10);
		
		Separator buttonSeparator = new Separator();
		buttonSeparator.setPadding(new Insets(30, 0, 30, 0));
		
		removeTask_Button = new Button("Remove Top Task");
		removeTask_Button.setOnAction(this);
		removeTask_Button.setPrefSize(130, 40);
		removeTask_Button.setTranslateX(10);
		
		this.getChildren().add(topTaskTitle);
		this.getChildren().add(topTask_HB);
		this.getChildren().add(topPriority_HB);
		this.getChildren().add(topTaskSeparator);
		this.getChildren().add(addTaskLabel);
		this.getChildren().add(userTask_HB);
		this.getChildren().add(userPriority_HB);
		this.getChildren().add(addTask_Button);
		this.getChildren().add(buttonSeparator);
		this.getChildren().add(removeTask_Button);

	}

	public void displayTopTask() {
		try {
			heapReference.readFile();
			String[] topTask = heapReference.getTopTask();
			topTask_TA.setText(topTask[1]);
			topPriority_TF.setText(topTask[0]);
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
		}
	}

	@Override
	public void handle(ActionEvent event) {
		try {
			if (event.getSource() == addTask_Button) {
				if (userTask_TF.getText().equals("") || userPriority_TF.getText().equals("")) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Input Error");
					alert.setContentText(
							"Input Error! You must have a valid task and corresponding priority number to input.");
					alert.showAndWait();
				} else {
					String userTask = String.valueOf(userTask_TF.getText());
					String userPriority = String.valueOf(userPriority_TF.getText());
					heapReference.addTask(userTask, userPriority);
					userTask_TF.clear();
					userPriority_TF.clear();
					displayTopTask();
				}
			} else if (event.getSource() == removeTask_Button) {
				heapReference.deleteTopTask();
				displayTopTask();
			}
			
		} catch (InputMismatchException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Input Error");
			alert.setContentText("Priority number must be an integer!");
			alert.showAndWait();
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Remove Task Error");
			alert.setContentText("There are no more tasks to delete!");
			alert.showAndWait();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
		}
	}
}
