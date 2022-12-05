package electionProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DCLinkedList {

	// The size of the circularly, doubly-linked list
	private int size;

	// Pointer to the head node of the linked list
	private ListNode headNode;

	// Pointer to the tail node of the linked list
	private ListNode tailNode;

	private ListNode currentHeadlineNode;

	/**
	 * Constructor that initalizes a circularly, doubly-linked list
	 */
	public DCLinkedList() {

		// Sets the head node at the head of the linked list
		headNode = new ListNode(null, tailNode, tailNode);

		// Sets the tail node at the end of the linked list
		tailNode = new ListNode(null, headNode, headNode);

		currentHeadlineNode = headNode;

		// Set size to zero since there are no valid elements in the linked list yet
		size = 0;
	}

	public void readFile() throws FileNotFoundException {
		Scanner headlinesFile = new Scanner(new File("RotatingMessagesFile.txt"));
		ListNode current = headNode;
		while (headlinesFile.hasNext()) {
			String nextLine = headlinesFile.nextLine();
			if (!nextLine.equals("")) {
				ListNode nodeToInsert = new ListNode(nextLine, current.next, current);
				current.next = nodeToInsert;
				current = current.next;
				size++;
			}
		}
	}

	private void writeLLToFile() throws IOException {
		if (headNode.next.data != null) {
			ListNode current = headNode.next;
			BufferedWriter file = new BufferedWriter(new FileWriter("RotatingMessagesFile.txt"));
			String outputText = "";
			while (current != null) {
				outputText += current.data + "\n";
				current = current.next;
			}
			file.write(outputText);
			file.close();
		}
	}

	public String getHeadline() {
		if (size == 0) {
			return "No New Headlines";
		}
		currentHeadlineNode = currentHeadlineNode.next;
		if (currentHeadlineNode == null) {
			currentHeadlineNode = headNode.next;
		}
		String curentHeadline = currentHeadlineNode.data;
		return curentHeadline;
	}

	public void addHeadline(String userHeadline) throws IOException {
		ListNode userHeadlineNode = new ListNode(userHeadline, currentHeadlineNode.next, currentHeadlineNode);
		currentHeadlineNode.next = userHeadlineNode;
		size++;
		clearFile();
	}

	public void removeHeadline() throws IOException {
		if (size != 0) {
			size--;
			if (size == 0) {
				headNode.next = tailNode;
				tailNode.previous = headNode;
			} else if (currentHeadlineNode.next == null) {
				currentHeadlineNode.previous.next = currentHeadlineNode.next;
			} else if (currentHeadlineNode.previous == null) {
				headNode.next = currentHeadlineNode.next;
			} else {
				currentHeadlineNode.previous.next = currentHeadlineNode.next;
				currentHeadlineNode.next.previous = currentHeadlineNode.previous;
			}
		}
		clearFile();
	}

	private void clearFile() throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter("RotatingMessagesFile.txt"));
		file.write("");
		file.close();
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
