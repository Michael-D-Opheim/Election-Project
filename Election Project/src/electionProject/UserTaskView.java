package electionProject;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserTaskView extends VBox implements EventHandler<ActionEvent> {

	private TextField topTask_TF;

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

		HBox topTaskHB = new HBox();
		Label taskLabel = new Label("Top Priority: ");
		topTask_TF = new TextField();
		topTask_TF.setEditable(false);
		topTaskHB.getChildren().add(taskLabel);
		topTaskHB.getChildren().add(topTask_TF);

		HBox topPriorityHB = new HBox();
		Label topPriorityLabel = new Label("Priority Number: ");
		topPriority_TF = new TextField();
		topPriority_TF.setEditable(false);
		topPriorityHB.getChildren().add(topPriorityLabel);
		topPriorityHB.getChildren().add(topPriority_TF);

		displayTopTask();

		Label addTaskLabel = new Label("Add Task");

		HBox userTaskHB = new HBox();
		Label userTaskTFLabel = new Label("Task: ");
		userTask_TF = new TextField();
		userTaskHB.getChildren().add(userTaskTFLabel);
		userTaskHB.getChildren().add(userTask_TF);

		HBox userPriorityHB = new HBox();
		Label userPriorityTFLabel = new Label("Priority: ");
		userPriority_TF = new TextField();
		userPriorityHB.getChildren().add(userPriorityTFLabel);
		userPriorityHB.getChildren().add(userPriority_TF);

		addTask_Button = new Button("Add Task");
		addTask_Button.setOnAction(this);

		Label removeTaskLabel = new Label("Remove Task");
		removeTask_Button = new Button("Remove Task");
		removeTask_Button.setOnAction(this);

		this.getChildren().add(topTaskTitle);
		this.getChildren().add(topTaskHB);
		this.getChildren().add(topPriorityHB);
		this.getChildren().add(addTaskLabel);
		this.getChildren().add(userTaskHB);
		this.getChildren().add(userPriorityHB);
		this.getChildren().add(addTask_Button);
		this.getChildren().add(removeTaskLabel);
		this.getChildren().add(removeTask_Button);

	}

	public void displayTopTask() {
		try {
			heapReference.readFile();
			String[] topTask = heapReference.getTopTask();
			topTask_TF.setText(topTask[1]);
			topPriority_TF.setText(topTask[0]);
		} catch (FileNotFoundException ex) {
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
					displayTopTask();
				}
			} else if (event.getSource() == removeTask_Button) {
				heapReference.deleteTopTask();
				displayTopTask();
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
		}
	}
}
