package electionProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ResultsPie extends VBox {

	private int myVotes = 1;

	private int opponentVotes = 1;

	private Data myChartData;

	private Data opponentChartData;

	private ObservableList<PieChart.Data> pieData;

	private PieChart pie;

	private TextField opponentVotes_TF;

	private TextField myVotes_TF;

	private Main mainReference;

	public ResultsPie(Main mainReference) {
		super();
		this.mainReference = mainReference;

		Label resultsTitle = new Label("Election Results");
		resultsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
		resultsTitle.setPadding(new Insets(10, 0, 10, 0));

		myChartData = new Data("Opheim", myVotes);
		opponentChartData = new Data("Albing", opponentVotes);

		pieData = FXCollections.observableArrayList(myChartData, opponentChartData);

		pie = new PieChart(pieData);
		pie.setLegendVisible(false);

		pieData.get(0).getNode().setStyle("-fx-pie-color: #E00000");
		pieData.get(1).getNode().setStyle("-fx-pie-color: #0089D9"); // Good 6495ED

		Label countTitle = new Label("Count");
		countTitle.setFont(Font.font("Arial", FontWeight.BOLD, 26));
		countTitle.setPadding(new Insets(15, 0, 10, 0));

		HBox opponentVotesCount_HB = new HBox();
		Label opponentVotesLabel = new Label("Albing: ");
		opponentVotesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		opponentVotes_TF = new TextField(percentageCalculator(opponentVotes) + "%");
		opponentVotes_TF.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		opponentVotes_TF.setMaxWidth(90);
		opponentVotes_TF.setEditable(false);
		opponentVotesCount_HB.getChildren().add(opponentVotesLabel);
		opponentVotesCount_HB.getChildren().add(opponentVotes_TF);
		opponentVotesCount_HB.setTranslateX(30);

		HBox myVotesCount_HB = new HBox();
		Label myVotesLabel = new Label("Opheim: ");
		myVotesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
		myVotes_TF = new TextField(percentageCalculator(myVotes) + "%");
		myVotes_TF.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		myVotes_TF.setMaxWidth(90);
		myVotes_TF.setEditable(false);
		myVotesCount_HB.getChildren().add(myVotesLabel);
		myVotesCount_HB.getChildren().add(myVotes_TF);
		myVotesCount_HB.setTranslateX(30);

		HBox voteCounts_HB = new HBox();
		voteCounts_HB.getChildren().add(opponentVotesCount_HB);
		voteCounts_HB.getChildren().add(myVotesCount_HB);
		voteCounts_HB.setSpacing(85);

		this.getChildren().add(resultsTitle);
		this.getChildren().add(pie);
		this.getChildren().add(countTitle);
		this.getChildren().add(voteCounts_HB);
	}

	private double percentageCalculator(int votes) {
		double votesPercent = Double.valueOf(votes) / (myVotes + opponentVotes) * 100;
		return Math.round(votesPercent * 100.0) / 100.0;
	}

	public void setPieData(int candidate, int votes) {
		if (candidate == 0) {
			myVotes += votes;
			myChartData.setPieValue(myVotes);

		} else if (candidate == 1) {
			opponentVotes += votes;
			opponentChartData.setPieValue(opponentVotes);
		}
		pie.setData(pieData);
		myVotes_TF.setText(percentageCalculator(myVotes) + "%");
		opponentVotes_TF.setText(percentageCalculator(opponentVotes) + "%");

	}
}
