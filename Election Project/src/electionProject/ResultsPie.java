package electionProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * This class is responsible for keeping track of how a particular “election
 * cycle” is going. It contains the individual vote counts and percentages for
 * Michael and Dr. Albing and creates a large pie chart to track of the vote
 * tallies using JavaFX’s PieChart API
 * 
 * @author Michael Opheim
 * @version 12/07/2022
 */
public class ResultsPie extends VBox {

	/** Keep track of my vote tally */
	private int myVotes = 1;

	/** Keep track of my opponent’s vote tally */
	private int opponentVotes = 1;

	/** A Data object that holds my vote data */
	private Data myChartData;

	/** A Data object that holds my opponent’s vote data */
	private Data opponentChartData;

	/**
	 * A list that takes in Data objects so that they can be implemented in a JavaFX
	 * PieChart
	 */
	private ObservableList<PieChart.Data> pieData;

	/** The pie chart containing all vote data */
	private PieChart pie;

	/** A TextField keeping track of my opponent’s vote count */
	private TextField opponentVoteCount_TF;

	/** A TextField keeping track of my opponent’s percentage of the votes */
	private TextField opponentVotePercent_TF;

	/** A TextField keeping track of my vote count */
	private TextField myVoteCount_TF;

	/** A TextField keeping track of my percentage of the votes */
	private TextField myVotePercent_TF;

	/**
	 * A TextField keeping track of the total number of votes that have been counted
	 */
	private TextField totalVote_TF;

	/**
	 * Constructor that builds the class in the GUI
	 */
	public ResultsPie() {
		super();

		// The title of this section of the GUI
		Label resultsTitle = new Label("Election Results");
		resultsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		resultsTitle.setPadding(new Insets(10, 0, 10, 0));

		// Instantiate the vote data
		myChartData = new Data("Opheim", myVotes);
		opponentChartData = new Data("Albing", opponentVotes);
		String totalVoteCount = Integer.toString(myVotes + opponentVotes);

		// Add the vote data to a pie chart
		pieData = FXCollections.observableArrayList(myChartData, opponentChartData);
		pie = new PieChart(pieData);
		pie.setLegendVisible(false);

		// Style the pie chart
		pieData.get(0).getNode().setStyle("-fx-pie-color: #E00000");
		pieData.get(1).getNode().setStyle("-fx-pie-color: #0089D9");

		// The title of the section of the GUI responsible for displaying election data
		Label countTitle = new Label("Count");
		countTitle.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		countTitle.setPadding(new Insets(15, 0, 10, 0));

		// The HBox for displaying the total number of votes counted in a given election
		HBox totalVote_HB = new HBox();
		Label totalVoteLabel = new Label("Total Votes: ");
		totalVoteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		totalVote_TF = new TextField(totalVoteCount);
		totalVote_TF.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		totalVote_TF.setMaxWidth(55);
		totalVote_TF.setEditable(false);
		totalVote_HB.getChildren().add(totalVoteLabel);
		totalVote_HB.getChildren().add(totalVote_TF);
		totalVote_HB.setTranslateX(10);

		// The HBox for displaying my opponent's vote data for a given election
		HBox opponentVotes_HB = new HBox();
		Label opponentVotesLabel = new Label("Albing:    Votes: ");
		opponentVotesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

		// Instantiate my opponent's vote count display
		String opponentVotesString = Integer.toString(opponentVotes);
		opponentVoteCount_TF = new TextField(opponentVotesString);
		opponentVoteCount_TF.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		opponentVoteCount_TF.setMaxWidth(55);
		opponentVoteCount_TF.setEditable(false);

		// Instantiate my opponent's vote percentage display
		Label opponentPercentLabel = new Label("	Percent: ");
		opponentPercentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		opponentVotePercent_TF = new TextField(percentageCalculator(opponentVotes) + "%");
		opponentVotePercent_TF.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		opponentVotePercent_TF.setMaxWidth(90);
		opponentVotePercent_TF.setEditable(false);

		// Create the HBox for my opponent's vote data
		opponentVotes_HB.getChildren().add(opponentVotesLabel);
		opponentVotes_HB.getChildren().add(opponentVoteCount_TF);
		opponentVotes_HB.getChildren().add(opponentPercentLabel);
		opponentVotes_HB.getChildren().add(opponentVotePercent_TF);
		opponentVotes_HB.setPadding(new Insets(5, 0, 5, 0));
		opponentVotes_HB.setTranslateX(10);

		// The HBox for displaying my vote data for a given election
		HBox myVoteCount_HB = new HBox();
		Label myVotesLabel = new Label("Opheim:  Votes: ");
		myVotesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));

		// Instantiate my vote count display
		String myVotesString = Integer.toString(myVotes);
		myVoteCount_TF = new TextField(myVotesString);
		myVoteCount_TF.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		myVoteCount_TF.setMaxWidth(55);
		myVoteCount_TF.setEditable(false);

		// Instantiate my opponent's vote percentage display
		Label myPercentLabel = new Label("	Percent: ");
		myPercentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		myVotePercent_TF = new TextField(percentageCalculator(myVotes) + "%");
		myVotePercent_TF.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		myVotePercent_TF.setMaxWidth(90);
		myVotePercent_TF.setEditable(false);

		// Create the HBox for my vote data
		myVoteCount_HB.getChildren().add(myVotesLabel);
		myVoteCount_HB.getChildren().add(myVoteCount_TF);
		myVoteCount_HB.getChildren().add(myPercentLabel);
		myVoteCount_HB.getChildren().add(myVotePercent_TF);
		myVoteCount_HB.setPadding(new Insets(5, 0, 5, 0));
		myVoteCount_HB.setTranslateX(10);

		// Populate this section of the GUI
		this.getChildren().add(resultsTitle);
		this.getChildren().add(pie);
		this.getChildren().add(countTitle);
		this.getChildren().add(totalVote_HB);
		this.getChildren().add(opponentVotes_HB);
		this.getChildren().add(myVoteCount_HB);
	}

	/**
	 * A method that takes in a candidate’s vote count and transforms it into a
	 * percentage using the total vote count
	 * 
	 * @param votes A candidate's vote count
	 * @return a percentage of votes
	 */
	private double percentageCalculator(int votes) {
		double votesPercent = Double.valueOf(votes) / (myVotes + opponentVotes) * 100;
		return Math.round(votesPercent * 100.0) / 100.0; // round the percentage for formatting purposes
	}

	/**
	 * A method that listens for countBallot() calls in the BallotStack class and
	 * takes in the random integers that method creates to give either election
	 * candidate a particular number of votes, updating GUI elements in the process
	 * to reflect new election standings.
	 * 
	 * @param candidate The candidate to receive votes
	 * @param votes     The votes to be given to a candidate
	 */
	public void setPieData(int candidate, int votes) {

		// If I am the candidate randomly selected, give me the votes
		if (candidate == 0) {
			myVotes += votes;
			myChartData.setPieValue(myVotes); // update my pie chart data

			// Otherwise, if my opponent is selected, give him the votes
		} else if (candidate == 1) {
			opponentVotes += votes;
			opponentChartData.setPieValue(opponentVotes); // update my opponent's pie chart data
		}

		// Update the pie chart
		pie.setData(pieData);

		// Update vote counts
		String newTotalVoteCount = Integer.toString(myVotes + opponentVotes);
		String opponentNewVoteCount = Integer.toString(opponentVotes);
		String myNewVoteCount = Integer.toString(myVotes);

		// Reflect the election updates in the GUI
		totalVote_TF.setText(newTotalVoteCount);
		opponentVoteCount_TF.setText(opponentNewVoteCount);
		myVoteCount_TF.setText(myNewVoteCount);
		myVotePercent_TF.setText(percentageCalculator(myVotes) + "%");
		opponentVotePercent_TF.setText(percentageCalculator(opponentVotes) + "%");

	}
}
