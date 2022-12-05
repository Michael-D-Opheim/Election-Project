package electionProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class BallotStack {

	private Stack<String> ballotStack;
	
	private ResultsPie pieReference;
	
	private Main mainReference;
	
	public BallotStack(Main mainReference, ResultsPie pieReference) {
		super();
		this.mainReference = mainReference;
		this.pieReference = pieReference;
	}

	public void readFile() throws FileNotFoundException {
		Scanner ballotsFile = new Scanner(new File("StackFile.txt"));
		ballotStack = new Stack<String>();
		while (ballotsFile.hasNext()) {
			String nextLine = ballotsFile.nextLine();
			if (!nextLine.equals("")) {
				ballotStack.push(nextLine);
			}
		}
	}

	public String createOutput() {
		String outputString = "";
		Stack<String> tempStack = ballotStack;
		while (!tempStack.isEmpty()) {
			outputString += tempStack.pop() + "\n";
		}
		return outputString;
	}

	public void addBallot(String userBallot) throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter("StackFile.txt", true));
		file.newLine();
		file.write(userBallot);
		file.close();
	}

	public String countBallot(Main mainReference) throws IOException {
		readFile();
		BufferedWriter file = new BufferedWriter(new FileWriter("StackFile.txt"));
		file.write("");
		
		//TODO Randomize
		if (!ballotStack.isEmpty()) {
			ballotStack.pop();
			pieReference.setPieData();
		}
		String outputText = createOutput();
		file.write(outputText);
		file.close();
		return outputText;
	}
}
