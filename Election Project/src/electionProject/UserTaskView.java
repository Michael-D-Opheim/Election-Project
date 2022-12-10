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

/**
 * This class creates the front-end for the task list. This is the to-do list
 * for Michael Opheim’s campaign. It also has options for the user to add a new
 * task to the task list or delete the top task from the task list.
 * 
 * @author Michael Opheim
 * @version 12/09/2022
 */
public class UserTaskView extends VBox implements EventHandler<ActionEvent> {

	/** An area to display the top priority task */
	private TextArea topTask_TA;

	/** A TextField to display the top task’s integer priority */
	private TextField topPriority_TF;

	/** A TextField that allows the user to input their task into the task list */
	private TextField userTask_TF;

	/** A TextField that allows the user to input their task’s corresponding integer priority */
	private TextField userPriority_TF;

	/** A Button that allows the user to submit their task */
	private Button addTask_Button;

	/** A Button that removes the top priority task from the task list */
	private Button removeTask_Button;

	/** Reference to the minHeap that creates the task list */
	private MinHeap heapReference;

	/**
	 * Constructor that builds the class in the GUI
	 */
	public UserTaskView() {
		super();

		// Create a reference to BallotStack (the back-end of this class)
		heapReference = new MinHeap();

		// Create a title for this section of this GUI
		Label topTaskTitle = new Label("Task List");
		topTaskTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
		topTaskTitle.setPadding(new Insets(5, 0, 30, 110));

		// Create an area in the GUI to display the top priority task
		HBox topTask_HB = new HBox();
		Label taskLabel = new Label("Top Task: ");
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

		// Create an area in the GUI to display the priority number of the top priority task
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

		// Display the top priority task and its task number in the GUI
		displayTopTask();

		// Add a separator to the GUI to aid readability
		Separator topTaskSeparator = new Separator();
		topTaskSeparator.setPadding(new Insets(30, 0, 30, 0));

		// Header of the section of the GUI that allows the user to input their own tasks
		Label addTaskLabel = new Label("Add Task: ");
		addTaskLabel.setFont(Font.font("Arial", 18));
		addTaskLabel.setPadding(new Insets(0, 10, 10, 0));
		addTaskLabel.setTranslateX(10);
		
		// Create an area in the GUI for the user to input their own tasks
		HBox userTask_HB = new HBox();
		Label userTaskTFLabel = new Label("Task: ");
		userTaskTFLabel.setFont(Font.font("Arial", 18));
		userTask_TF = new TextField();
		userTask_TF.setFont(Font.font("Arial", 14));
		userTask_HB.getChildren().add(userTaskTFLabel);
		userTask_HB.getChildren().add(userTask_TF);
		userTask_HB.setPadding(new Insets(10, 0, 5, 0));
		userTask_HB.setTranslateX(10);

		// Create an area in the GUI for the user to input their own tasks' priority numbers
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

		// Instantiate a Button that allows the user to input their own tasks
		addTask_Button = new Button("Add Task");
		addTask_Button.setOnAction(this);
		addTask_Button.setPrefSize(90, 40);
		addTask_Button.setTranslateX(10);

		// Add a separator to the GUI to aid readability
		Separator buttonSeparator = new Separator();
		buttonSeparator.setPadding(new Insets(30, 0, 30, 0));

		// Instantiate a Button that allows the user to remove the top priority task
		removeTask_Button = new Button("Remove Top Task");
		removeTask_Button.setOnAction(this);
		removeTask_Button.setPrefSize(130, 40);
		removeTask_Button.setTranslateX(10);

		// Populate this section of the GUI
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

	/**
	 * Calls on the back-end to find the top priority task in a text file and displays that task in the front-end
	 */
	public void displayTopTask() {
		try {
			
			// Read a text file and get the top priority task from it
			heapReference.readFile();
			String[] topTask = heapReference.getTopTask();
			
			// Set the task and its corresponding priority number in the GUI
			topTask_TA.setText(topTask[1]);
			topPriority_TF.setText(topTask[0]);
			
		// Alert the user if a text file cannot be found by Scanner
		} catch (FileNotFoundException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
			
		// Create an alert if there are errors within a user's text file
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
		}
	}

	@Override
	/**
	 * Handles button presses for adding and removing tasks
	 * 
	 * @param event The registered event
	 */
	public void handle(ActionEvent event) {
		try {
			
			// If the user is adding a task to the minHeap...
			if (event.getSource() == addTask_Button) {
				
				// If the user has not inputted a task or a task priority number, alert them
				if (userTask_TF.getText().equals("") || userPriority_TF.getText().equals("")) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Input Error");
					alert.setContentText(
							"Input Error! You must have a valid task and corresponding priority number to input.");
					alert.showAndWait();
					
				// If the users priority number is less than or equal to 0 or greater than 100, alert them
				} else if (Integer.parseInt(userPriority_TF.getText()) <= 0 || Integer.parseInt(userPriority_TF.getText()) > 100) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setTitle("Input Error");
					alert.setContentText(
							"Input Error! Your priority number must be between 1 and 100!");
					alert.showAndWait();
					
				// Otherwise pass the task and its priority number to the back-end and create some new text for outputting in the GUI
				} else {
					String userTask = String.valueOf(userTask_TF.getText());
					String userPriority = String.valueOf(userPriority_TF.getText());
					heapReference.addTask(userTask, userPriority);
					
					// Clear the TextFields and create new output text
					userTask_TF.clear();
					userPriority_TF.clear();
					displayTopTask();
				}
				
			// If the user is deleting the top priority task from the minHeap...
			} else if (event.getSource() == removeTask_Button) {
				
				// Make a call to the back-end and create some new text for outputting in the GUI
				heapReference.deleteTopTask();
				displayTopTask();
			}

		// If the user did not input an integer for a priority number, alert them
		} catch (InputMismatchException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Input Error");
			alert.setContentText("Priority number must be an integer!");
			alert.showAndWait();
			
		// If the user is trying to delete the last task in the minHeap, alert them
		} catch (IllegalArgumentException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Remove Task Error");
			alert.setContentText("There are no more tasks to delete!");
			alert.showAndWait();
			
		// Create an alert if there are errors within a user's text file
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("File Not Found");
			alert.setContentText("File was not found. Please use a valid text file");
			alert.showAndWait();
		}
	}
}
