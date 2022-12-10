package electionProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class is responsible for data implementation and removal in Headlines.
 * It reads a text file and creates a circularly doubly-linked list using the
 * data in the file itself. Additionally it handles action events (button
 * presses) from the front-end. If data is to be removed from the linked list,
 * this class will handle deleting it from a relevant text file, or, if data is
 * to be added to the linked list, this class will handle inputting it into the
 * same relevant text file
 * 
 * @author Michael Opheim
 * @version 12/09/2022
 */
public class CDLinkedList {

	/** The size of the circularly, doubly-linked list */
	private int size;

	/** Pointer to the head node of the linked list */
	private ListNode headNode;

	/** Pointer to the tail node of the linked list */
	private ListNode tailNode;

	/** Keeps track of the current headline that is being displayed in the front-end */
	private ListNode currentHeadlineNode;

	/**
	 * Constructor that initalizes a circularly, doubly-linked list
	 */
	public CDLinkedList() {

		// Sets the head node at the head of the linked list
		headNode = new ListNode(null, tailNode, tailNode);

		// Sets the tail node at the end of the linked list
		tailNode = new ListNode(null, headNode, headNode);

		// Instantiate currentHeadlineNode, putting it at the start of the linked list
		currentHeadlineNode = headNode;

		// Set size to zero since there are no valid elements in the linked list yet
		size = 0;
	}

	/**
	 * Reads a file and converts its contents into nodes that are then incorporated in the circularly, doubly-linked list
	 * @throws FileNotFoundException if a text file is not found by Scanner
	 */
	public void readFile() throws FileNotFoundException {
		Scanner headlinesFile = new Scanner(new File("RotatingMessagesFile.txt"));
		
		// Create a node for looping
		ListNode current = headNode;
		
		// Read through the text file
		while (headlinesFile.hasNext()) {
			String nextLine = headlinesFile.nextLine();

			// If a line in the text file is not null
			if (!nextLine.equals("")) {
				
				// Create a node and put it at the end of the linked list (incrementing size in the process)
				ListNode nodeToInsert = new ListNode(nextLine, current.next, current);
				current.next = nodeToInsert;
				current = current.next;
				size++;
			}
		}
	}

	/**
	 * Upon updating the linked list, this method will read the resulting list and use it to update a text file for future reference
	 * @throws IOException if errors are thrown when writing to the text file
	 */
	private void writeLLToFile() throws IOException {
		
		// If the linked list is not empty...
		if (headNode.next.data != null) {
			ListNode current = headNode.next; // start at the head node
			BufferedWriter file = new BufferedWriter(new FileWriter("RotatingMessagesFile.txt"));
			String outputText = "";
			
			// Create output text for each node in the linked list
			while (current != null) {
				outputText += current.data + "\n";
				current = current.next;
			}
			file.write(outputText); // write the collection of output text to the file
			file.close();
		}
	}

	/**
	 * Retrieves the current headline in the thread (setting the currentHeadlineNode attribute in the process) and returns it to the front-end for display
	 * @return the current headline in the thread
	 */
	public String getHeadline() {
		
		// Keep track of which headline is the current headline in the thread
		currentHeadlineNode = currentHeadlineNode.next;
		if (currentHeadlineNode == null) {
			currentHeadlineNode = headNode.next;
		}
		
		// Return the current headline for display
		String curentHeadline = currentHeadlineNode.data;
		return curentHeadline;
	}

	/**
	 * Adds a user headline to the linked list.
	 * @param userHeadline The 'headline' the user has inputted in Headlines
	 * @throws IOException if errors are thrown when writing to the text file
	 */
	public void addHeadline(String userHeadline) throws IOException {
		
		// Create a node for the user's headline and add it to the linked list
		ListNode userHeadlineNode = new ListNode(userHeadline, currentHeadlineNode.next, currentHeadlineNode);
		currentHeadlineNode.next = userHeadlineNode;
		size++;
		
		// Clear the file and update it with thew new headline
		clearAndWriteFile();
	}

	/**
	 * Removes the current headline in the thread
	 * @throws IOException if errors are thrown when writing to the text file
	 */
	public void removeHeadline() throws IOException {
		
		//Make sure there are at least two headlines in the linked list (to keep at least one headline in the display at all times)
		if (size <= 1) {
			throw new IllegalArgumentException();
			
		} else {
			
			size--;
				
			// If the removed headline was the last node in the linked list...
			if (currentHeadlineNode.next == null) {
				
				// Connect the new last node to the first node in the linked list
				currentHeadlineNode.previous.next = currentHeadlineNode.next;
				
			// If the removed headline was the first node in the linked list...
			} else if (currentHeadlineNode.previous == null) {
				
				// Have the head sentinel node point to the next node in the linked list
				headNode.next = currentHeadlineNode.next;
				
			// Otherwise, we are removing a headnode that is located in the middle of the linked list
			} else {
				
				// Set pointers accordingly
				currentHeadlineNode.previous.next = currentHeadlineNode.next; 
				currentHeadlineNode.next.previous = currentHeadlineNode.previous;
			}
		}
		
		// Rewrite the file using the updated linked list
		clearAndWriteFile();
	}

	/**
	 * Clears a text file so that it can be rewritten
	 * @throws IOException if errors are thrown when writing to the text file
	 */
	private void clearAndWriteFile() throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter("RotatingMessagesFile.txt"));
		file.write("");
		file.close();
		
		// Call the method that writes a linked list to the text file
		writeLLToFile();
	}

	/**
	 * Class that creates a node which can then be attached to a linked list
	 * 
	 * @author Michael Opheim
	 */
	private class ListNode {

		/** The data to store in this node */
		private String data;

		/** A pointer to the next node in the list */
		private ListNode next;

		/** A pointer to the previous node in the list */
		private ListNode previous;

		/**
		 * Constructor that builds an object from the class
		 * 
		 * @param data The actual data item to store
		 * @param next A pointer to the node that should go next in the linked list
		 */
		public ListNode(String data, ListNode next, ListNode previous) {
			this.data = data;
			this.next = next;
			this.previous = previous;
		}
	}
}
