package electionProject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MinHeap {

	private ArrayList<String[]> taskHeap;

	/** Number of items in the heap, doubles as index of next open spot in array */
	private int size = 0;

	public void readFile() throws IOException {
		size = 0;
		Scanner taskFile = new Scanner(new File("PriorityQueueFile.txt"));
		taskHeap = new ArrayList<String[]>();
		while (taskFile.hasNext()) {
			Integer priority = taskFile.nextInt();
			String task = taskFile.nextLine();
			String[] tempArray = { priority.toString(), task };
			taskHeap.add(tempArray);
			size++;

		}
		buildHeap();
	}

	private void buildHeap() throws IOException {
		int lastItem = size - 1;
		int firstparent = (lastItem - 1) / 2; // Last parent node in heap

		for (int i = firstparent; i >= 0; i--) {
			percDown(i);
		}

		heapSort();
	}

	/**
	 * Percolate the item at the specified index as far as appropriate
	 * 
	 * @param curIndex index of item that needs to be percolated down
	 */
	private void percDown(int curIndex) {
		int swapIndex;
		int leftChild; // index of i's left child
		int rightChild; // index of i's right child

		while (true) {
			leftChild = curIndex * 2 + 1;
			rightChild = leftChild + 1; // i.e., curIndex * 2 + 2
			if (leftChild >= size) {
				// out of range - no children
				break;
			}
			swapIndex = curIndex; // parent node to be switched
			// there is a left child or we would have stopped already
			if (Integer.parseInt(taskHeap.get(leftChild)[0]) > (Integer.parseInt(taskHeap.get(curIndex)[0]))) { // if
																												// left
																												// child
																												// is
																												// greater
																												// than
																												// parent
				swapIndex = leftChild;
			}

			if (rightChild < size) { // is there a right child?
				if (Integer.parseInt(taskHeap.get(rightChild)[0]) > Integer.parseInt((taskHeap.get(swapIndex)[0]))) { // if
																														// right
																														// child
																														// is
																														// graeter
																														// than
																														// parent
																														// or
																														// left
																														// (if
																														// left
																														// is
																														// greater
																														// than
																														// parent)
					swapIndex = rightChild;
				}
			}
			if (swapIndex == curIndex) {
				break;
			}
			// else, do the swap
			String[] temp = taskHeap.get(curIndex);
			taskHeap.set(curIndex, taskHeap.get(swapIndex));
			taskHeap.set(swapIndex, temp);

			curIndex = swapIndex; // get ready for next iteration
		}
	}

	// TODO maybe improve this
	private void heapSort() throws IOException {
		ArrayList<String[]> sortedArray = new ArrayList<String[]>();
		int savedSize = size;
		int index = 0;
		while (size != 0) {
			String[] lastNode = taskHeap.get(size - 1);
			String[] maxNode = taskHeap.get(0);
			sortedArray.add(index, maxNode);
			taskHeap.set(0, lastNode);
			taskHeap.remove(size - 1);
			size--;
			index++;
			percDown(0);
		}
		Collections.reverse(sortedArray);
		size = savedSize;
		taskHeap = sortedArray;

		writeHeapToFile();
	}

	private void writeHeapToFile() throws IOException {
		BufferedWriter file = new BufferedWriter(new FileWriter("PriorityQueueFile.txt"));
		file.write("");
		String outputText = "";
		for (int i = 0; i < size; i++) {
			outputText += taskHeap.get(i)[0] + taskHeap.get(i)[1] + "\n";
		}
		file.write(outputText);
		file.close();
	}

	public String[] getTopTask() {
		return taskHeap.get(0);
	}

	public void addTask(String task, String priority) throws IOException {
		String[] userTask = {priority, " " + task};
		taskHeap.add(userTask);
		size++;
		writeHeapToFile();
		
	}
	
	public void deleteTopTask() throws IOException {
		String[] lastNode = taskHeap.get(size - 1);
		taskHeap.set(0, lastNode);
		taskHeap.remove(size - 1);
		size--;
		writeHeapToFile();
	}
}
