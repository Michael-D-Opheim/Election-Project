package electionProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * This class is responsible for data implementation and removal in
 * UserTaskView. It reads a text file and creates a minHeap using the data in
 * the file itself. Additionally it handles action events (button presses) from
 * the front-end. If data is to be removed from the queue, this class will
 * handle deleting it from a relevant text file, or, if data is to be added to
 * the queue, this class will handle inputting it into the same relevant text
 * file.
 * 
 * @author Michael Opheim
 * @version 12/07/2022
 */
public class MinHeap {

	/**
	 * The minHeap to be created and interacted with. It holds tasks and their
	 * corresponding priority numbers via arrays.
	 */
	private ArrayList<String[]> taskHeap;

	/** Number of items in the heap, doubles as index of next open spot in array */
	private int size = 0;

	/**
	 * Reads a text file filled with task data
	 * 
	 * @throws IOException if errors are thrown when later writing to the text file
	 */
	public void readFile() throws IOException {

		// Set size to zero for later method calls and heap instantiations
		size = 0;
		Scanner taskFile = new Scanner(new File("PriorityQueueFile.txt"));

		// Insantiate the heap
		taskHeap = new ArrayList<String[]>();

		// Add tasks and their priorities to the heap
		while (taskFile.hasNext()) {
			Integer priority = taskFile.nextInt();
			String task = taskFile.nextLine();
			String[] tempArray = { priority.toString(), task };
			taskHeap.add(tempArray);
			size++;

		}

		// Build out the heap using the data in taskHeap
		buildHeap();
	}

	/**
	 * Takes file data (stored in taskHeap) and builds a maxHeap from it
	 * 
	 * @throws IOException if errors are thrown when writing to the text file
	 */
	private void buildHeap() throws IOException {

		// Find the last element in taskHeap
		int lastItem = size - 1;

		// Find the last parent node in taskHeap
		int firstparent = (lastItem - 1) / 2;

		// Build out a maxHeap
		for (int i = firstparent; i >= 0; i--) {
			percDown(i);
		}

		// Sort the maxHeap into a minHeap
		heapSort();
	}

	/**
	 * Percolates data down in taskHeap in order to create a maxHeap
	 * 
	 * @param curIndex index of item that needs to be percolated down
	 */
	private void percDown(int curIndex) {

		int swapIndex;
		int leftChild; // index of i's left child
		int rightChild; // index of i's right child

		// Start the percolation process
		while (true) {

			// Find the children of the current node
			leftChild = curIndex * 2 + 1;
			rightChild = leftChild + 1;

			// out of range - no children
			if (leftChild >= size) {
				break;
			}
			swapIndex = curIndex; // parent node to be switched

			// If the left child node is greater than the parent node...
			if (Integer.parseInt(taskHeap.get(leftChild)[0]) > (Integer.parseInt(taskHeap.get(curIndex)[0]))) {
				swapIndex = leftChild;
			}

			// If there is a right child and the child is greater than the parent node or
			// left child node (if the left child node is also greater than the parent node)
			if (rightChild < size) {
				if (Integer.parseInt(taskHeap.get(rightChild)[0]) > Integer.parseInt((taskHeap.get(swapIndex)[0]))) {
					swapIndex = rightChild;
				}
			}

			// If the current node is greater than both of its child nodes...
			if (swapIndex == curIndex) {
				break;
			}

			// Otherwise, do the node swap to build a maxHeap
			String[] temp = taskHeap.get(curIndex);
			taskHeap.set(curIndex, taskHeap.get(swapIndex));
			taskHeap.set(swapIndex, temp);

			// Prepare for the next iteration
			curIndex = swapIndex;
		}
	}

	/**
	 * A method that gets called by buildHeap after a maxHeap has been created. This
	 * method transforms the maxHeap into a sorted minHeap
	 * 
	 * @throws IOException if errors are thrown when writing to the text file
	 */
	private void heapSort() throws IOException {

		// Create a new ArrayList to hold the sorted minHeap
		ArrayList<String[]> sortedArray = new ArrayList<String[]>();

		// Save the size for the upcoming operation
		int savedSize = size;
		int index = 0;

		// Add the max node to the array and rebuild the maxHeap (putting the max node
		// at the top of the heap)
		while (size != 0) {
			String[] lastNode = taskHeap.get(size - 1);
			String[] maxNode = taskHeap.get(0);
			sortedArray.add(index, maxNode); // put the max node in the temporary ArrayList
			taskHeap.set(0, lastNode); // put the last node at the top of the heap
			taskHeap.remove(size - 1);
			size--;
			index++;
			percDown(0); // percolate the last node down to get a new max node at the top of the heap
		}

		// Once the heap has been fully sorted, reverse it to get a sorted minHeap
		Collections.reverse(sortedArray);

		// Set the minHeap and its size to the below attributes for later
		size = savedSize;
		taskHeap = sortedArray;

		// Save the minHeap in a text file
		writeHeapToFile();
	}

	/**
	 * Writes a minHeap to a file so it is saved for later
	 * 
	 * @throws IOException if errors are thrown when writing to the text file
	 */
	private void writeHeapToFile() throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter("PriorityQueueFile.txt"));

		// Clear the text file
		file.write("");
		String outputText = "";

		// Create output text from the minHeap
		for (int i = 0; i < size; i++) {
			outputText += taskHeap.get(i)[0] + taskHeap.get(i)[1] + "\n";
		}

		// Write to the text file using the output text generated from the minHeap
		file.write(outputText);
		file.close();
	}

	/**
	 * Returns the top priority task for the front-end display
	 * 
	 * @return the top priority task and its corresponding priority number
	 */
	public String[] getTopTask() {
		return taskHeap.get(0);
	}

	/**
	 * Adds a user task to the taskHeap
	 * 
	 * @param task     The user's provided task
	 * @param priority The priority number of the user's provided task
	 * @throws IOException if errors are thrown when later writing to the text file
	 */
	public void addTask(String task, String priority) throws IOException {
		String[] userTask = { priority, " " + task };
		taskHeap.add(userTask);
		size++;

		// After the user's task has been added, sort the heap and rewrite the text file
		writeHeapToFile();

	}

	/**
	 * Deletes the top priority task from the taskHeap
	 * 
	 * @throws IOException if errors are thrown when later writing to the text file
	 */
	public void deleteTopTask() throws IOException {

		// If the taskHeap contains at least two tasks...
		if (size != 1) {

			// Delete the top node in the heap and replace it with the last node in the heap
			String[] lastNode = taskHeap.get(size - 1);
			taskHeap.set(0, lastNode);
			taskHeap.remove(size - 1);
			size--;

			// After the top task has been deleted, sort the heap and rewrite the text file
			writeHeapToFile();

			// Otherwise, if the taskHeap contains only one task, throw an error
		} else {
			throw new IllegalArgumentException();
		}
	}
}
