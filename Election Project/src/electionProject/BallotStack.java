package electionProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/**
 * This class is responsible for data implementation and removal in BallotView.
 * It reads a text file and creates a stack using the data in the file itself.
 * Additionally it handles action events (button presses) from the front-end. If
 * data is to be removed from the stack, this class will handle deleting it from
 * a relevant text file, or, if data is to be added to the stack, this class
 * will handle inputting it into the same relevant text file.
 * 
 * @author Michael Opheim
 * @version 12/07/2022
 *
 */
public class BallotStack {

	/** The stack to be created and interacted with. It holds ballots */
	private Stack<String> ballotStack;

	/** A reference to the ResultsPie class. */
	private ResultsPie pieReference;

	/**
	 * Constructor that establishes a connection to the ResultsPie class in the GUI
	 * 
	 * @param pieReference A reference to ResultsPie
	 */
	public BallotStack(ResultsPie pieReference) {
		super();
		this.pieReference = pieReference;
	}

	/**
	 * Reads a file and places its contents into a stack (ballotStack)
	 * 
	 * @throws FileNotFoundException if a text file is not found by Scanner
	 */
	public void readFile() throws FileNotFoundException {
		Scanner ballotsFile = new Scanner(new File("StackFile.txt")); // read text file
		ballotStack = new Stack<String>(); // insantiate stack

		// While the text file has text...
		while (ballotsFile.hasNext()) {
			String nextLine = ballotsFile.nextLine();
			
			// Add each valid (non-blank) line to the stack
			if (!nextLine.equals("")) {
				ballotStack.push(nextLine);
			}
		}
	}

	/**
	 * Reads through ballotStack and concatenates its data into a single, formatted
	 * String, which can then be displayed in the BallotView class
	 * 
	 * @return the String to be outputted in BallotView
	 */
	public String createOutput() {
		String outputString = "";
		Stack<String> tempStack = ballotStack; // preserve the 'official' stack by using a clone

		// While there are items in the stack...
		while (!tempStack.isEmpty()) {
			outputString += tempStack.pop() + "\n"; // add each item to the output String
		}
		return outputString;
	}

	/**
	 * Adds a user ballot to a corresponding text file for future reference
	 * 
	 * @param userBallot The 'ballot' the user has inputted in BallotView
	 * @throws IOException if errors are thrown when writing to the text file
	 */
	public void addBallot(String userBallot) throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter("StackFile.txt", true));
		file.newLine();
		file.write(userBallot); // add the user's ballot to the bottom of the text file
		file.close();
	}

	/**
	 * Removes the top ballot in the ballot stack. Afterwards, this method creates a
	 * random number of “votes”, which will be given to a random candidate and
	 * transferred to ResultsPie (so vote counts will be updated)
	 * 
	 * @return a new collection of output text that can be displayed in BallotView
	 * @throws IOException if errors are thrown when writing to the text file
	 */
	public String countBallot() throws IOException {
		readFile(); // read the text file and ensure that we have a stack (even if a text file is
					// empty)

		// Clear the text file
		BufferedWriter file = new BufferedWriter(new FileWriter("StackFile.txt"));
		file.write("");

		// Since we're counting a ballot, reflect the results of the ballot in the
		// election results section of the GUI
		if (!ballotStack.isEmpty()) {
			ballotStack.pop();

			// Instantiate Random objects to make random integers
			Random chooseCandidate = new Random();
			Random chooseVoteCount = new Random();

			int candidate = chooseCandidate.nextInt(2); // pick a random candidate
			int voteCount = chooseVoteCount.nextInt(11); // pick a random number (of votes)

			// Give the random candidate the random number of votes chosen
			pieReference.setPieData(candidate, voteCount);

		// Throw an Exception if an empty ballot stack is being 'counted' from
		} else {
			throw new IllegalArgumentException();
		}

		// Remove the counted ballot from the text file and create some new output text
		// for the front-end
		String outputText = createOutput();
		file.write(outputText);
		file.close();
		return outputText;
	}
}
