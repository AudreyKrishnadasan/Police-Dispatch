//package project3;

import java.util.Arrays;



public class MinHeap {

    /**
     * A min heap node that stores an HeapElement and its priority.
     */
    class Node {
        HeapElement value;
    	
        int priority;

        public Node(HeapElement value, int priority) {
            this.value = value;
            this.priority = priority;
        }
    }

    private Node[] array;
    private int size;

    /**
     * Initialize the min heap.
     */
    public MinHeap() {
        // start with space for ten strings
        this.array = new Node[10];
        this.size = 0;
    }

    // UTILITY METHODS

    /**
     * Double the size of the Node array.
     */
    private void resize() {
        Node[] newArray = new Node[2 * this.array.length];
        for (int i = 0; i < this.array.length; i++) {
            newArray[i] = this.array[i];
        }
        this.array = newArray;
    }

    /**
     * Calculate the index of the parent node.
     *
     * This method assumes the child index is valid,
     * and does not need to perform error checking.
     *
     * @param index The index of the child node.
     */
    // index - 1 / 2 * rounded down to nearest whole
    private int parent(int index) {
    	return (index - 1)/2; // (Challenge Q2)
    }

    /**
     * Calculate the index of the left child.
     *
     * This method assumes the parent index is valid,
     * and does not need to perform error checking.
     *
     * @param index The index of the parent node.
     * @return The index of the left child node.
     */
    // 2(index) +1
    private int leftChild(int index) {
        return (2* index) +1; // (Challenge Q2)
    }

    /**
     * Calculate the index of the right child.
     *
     * This method assumes the parent index is valid,
     * and does not need to perform error checking.
     *
     * @param index The index of the parent node.
     * @return The index of the right child node.
     */
    // 2(index) + 2
    private int rightChild(int index) {
        return (2 * index) + 2; // (Challenge Q2)
    }

    /**
     * Swap the array contents of the given indices.
     *
     * @param index1 The first index.
     * @param index2 The second index.
     */
    private void swap(int index1, int index2) {
        // FIXME (Challenge Q2)
    	Node temp = this.array[index1];
    	this.array[index1] = this.array[index2];
    	this.array[index2] = temp; 
    }

    // ADD

    /**
     * Add an HeapElement to the min heap.
     *
     * @param HeapElement The HeapElement to add.
     * @param priority The priority of the HeapElement.
     */
    public void add(HeapElement HeapElement, int priority) {
        Node curr  = new Node(HeapElement, priority);
    	//resize if needed
        if(this.array[array.length-1] != null) { // check if array is full
        	this.resize(); 
        }
        //put the new node in the array
        this.array[this.size()] = curr;
        int currIndex = this.size();
        Node parent = array[parent(currIndex)];
        // percolate up (Challenge Q3)

        while(curr.priority < parent.priority && currIndex != 0) {
        	int parentIndex = parent(currIndex);
        	swap(currIndex, parentIndex); 
        	currIndex = parentIndex; //current actual index
        	parent = array[parent(currIndex)]; // update the parent
        }
        // increment size
        size++;
    }

    // POLL

    /**
     * Remove and return the HeapElement with the lowest priority number.
     *
     * @return The HeapHeapElement with the lowest priority number.
     */
    public HeapElement poll() {
        // save the return value to a temporary variable
        // replace the root with the last HeapElement
        // decrement the size
      
    	Node rootVal = array[0];
    	
    	array[0] = array[size-1];
    	array[size-1] = null;
    	size -- ;
    	int currNodeIndex = 0;
    	
    	// percolate down
        while (true) {
        	 // break if there is no children (Challenge Q4a)
        	if(leftChild(currNodeIndex) >= size) {
        		break;
        	}
            // find the appropriate child to compare with (Challenge Q4b)
            int childNodeIndex = -1; // initialize with dummy value
            if(size == 2) {
            	childNodeIndex = 1;
            }
            else if(array[leftChild(currNodeIndex)] != null && array[rightChild(currNodeIndex)] != null && array[leftChild(currNodeIndex)].priority> array[rightChild(currNodeIndex)].priority){
            	childNodeIndex = rightChild(currNodeIndex); 
            }
            else {
            	childNodeIndex = leftChild(currNodeIndex);
            }
            // break if the child is lower priority (Challenge Q4c)
            if(array[currNodeIndex].priority < array[childNodeIndex].priority) {
            	break;
            }
            // swap (Challenge Q4d)
            swap(currNodeIndex,childNodeIndex);
            currNodeIndex = childNodeIndex;
            
            
        }
        return rootVal.value; // FIXME
    }

    // OTHER HEAP METHODS

    /**
     * Get the size of the min heap.
     *
     * @return The size of the min heap.
     */
    public int size() {
        return this.size;
    }

    /**
     * Return (but not remove) the HeapElement with the lowest priority number.
     *
     * @return The HeapHeapElement with the lowest priority number.
     */
    public HeapElement peek() {
    	if(this.array[0] == null) {
    		return null;
    	}
        return this.array[0].value;
    }

    // DEBUG METHODS

    /**
     * Print the array of the min heap, as is.
     */
    public void printArray() {
        if (this.size == 0) {
            System.out.println("{}");
        }
        String result = "{" + this.array[0].value;
        for (int i = 1; i < this.size; i++) {
            result += ", " + this.array[i].value;
        }
        System.out.println(result + "}");

    }

    /**
     * Print the min heap as a binary tree.
     */
    public void printTree() {
        this.printTree(0, "");
    }

    private void printTree(int index, String indent) {
        if (index >= this.size) {
            return;
        }
        this.printTree(this.rightChild(index), indent + "    ");
        System.out.println(indent + this.array[index].value);
        this.printTree(this.leftChild(index), indent + "    ");
    }

    // MAIN

    public static void main(String[] args) {
    	 // create the heap
       /** MinHeap heap = new MinHeap();
        // add some numbers
        int[] numbers = {56, 28, 7, 5, 51, 16, 79, 83, 97, 37, 75, 69, 24, 90};
        for (int i =0; i < numbers.length; i++) {
            String numberString = new Integer(numbers[i]).toString();
            heap.add(new HeapElement(numberString), numbers[i]);
        }
        // print the heap as an array
        heap.printArray();
        // print the heap as a binary tree
        heap.printTree();
        // poll everything out
        for (int i = 0; i < numbers.length; i++) {
            System.out.println(heap.poll());
        }*/
    }
    }


