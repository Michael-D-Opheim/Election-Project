package electionProject;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Driver for electionProjet. Creates and runs an interactive GUI for the
 * project.
 * 
 * @author Michael Opheim
 * @version 12/09/2022
 */
public class Main extends Application {

	/**
	 * Creates the election GUI
	 * 
	 * @oaram primaryStage The window where the GUI will be displayed
	 * @throws Exception if an error occurs
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {

			// Creates the window and scene for the election GUI
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 1200, 850);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Election GUI");

			// Adds a header/title to the GUI
			HBox titleBox = new HBox(); // create HBox to hold the title
			titleBox.setPrefSize(1200, 100);
			titleBox.setStyle("-fx-background-color: #B0E0E6");
			titleBox.setAlignment(Pos.CENTER); // center header text
			root.setTop(titleBox);

			Label title = new Label("Opheim Versus Albing - Math/CS RunOff Election 2022");
			title.setFont(Font.font("Arial", FontWeight.BOLD, 34));
			titleBox.getChildren().add(title); // add title to HBox

			// Adds a section in the GUI for user task view
			// This is the front-end for the priority task queue
			UserTaskView userTaskViewBox = new UserTaskView();
			userTaskViewBox.setPrefSize(350, 600);
			userTaskViewBox.setStyle("-fx-background-color: #F08080");
			root.setLeft(userTaskViewBox);

			// Adds a section in the GUI for the results pie chart
			ResultsPie pieBox = new ResultsPie();
			pieBox.setPrefSize(500, 600);
			pieBox.setAlignment(Pos.TOP_CENTER); // allign all of the text in the section about the top center
			root.setCenter(pieBox);

			// Adds a section in the GUI for the ballots view
			// This is the front-end for the stack; here, the user will be able to add and
			// count ballots
			BallotView ballotBox = new BallotView(pieBox);
			ballotBox.setPrefSize(350, 600);
			ballotBox.setStyle("-fx-background-color: #F08080;");
			root.setRight(ballotBox);

			// Adds a section in the GUI for the headlines
			// This is the front-end for the circularly, doubly-linked list.
			Headlines headlinesBox = new Headlines();
			headlinesBox.setStyle("-fx-background-color: #B0E0E6");
			headlinesBox.setPrefSize(1200, 200);
			root.setBottom(headlinesBox);

			// Shows window
			primaryStage.show();

			// Catch any exceptions that might be produced from the GUI
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Launches GUI
	 * 
	 * @param args All provided arguments
	 */
	public static void main(String[] args) {
		launch(args);

	}

}
