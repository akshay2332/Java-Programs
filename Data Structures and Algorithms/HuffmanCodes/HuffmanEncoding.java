 import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

//--------------------------------------------------Assignment Group 45---------------------------------------------------//

public class HuffmanEncoding {

    private static final String INPUT_FILE_NAME = "infile.dat";
    private static final String OUTPUT_FILE_NAME = "outfile.dat";

    private BufferedReader inputFileReader = null;
    private BufferedWriter outputFileWriter = null;
    private static final String REGEX_FOR_REPLACEMENT = "[^A-Za-z0-9]";

    private Map<Character, Integer> frequencyCountMap = null;

    private Map<Character, String> huffmanCodes = null;
    private int totalCharacters;
    private Integer frequencies[] = null;
    private Character symbols[] = null;

    public HuffmanEncoding() {
    }

    public static void main(String args[]) {

        HuffmanEncoding huffmanEncoding = new HuffmanEncoding();
        String fileContent = huffmanEncoding.readTextFromFile();

        System.out.println("file content before replacement = " + fileContent);

        fileContent = fileContent.replaceAll(HuffmanEncoding.REGEX_FOR_REPLACEMENT, "");

        System.out.println("file content after replacement = " + fileContent);

        huffmanEncoding.generateFrequencyMap(fileContent);

        int sizeOfMap = huffmanEncoding.frequencyCountMap.size();
        huffmanEncoding.symbols = huffmanEncoding.frequencyCountMap.keySet().toArray(new Character[sizeOfMap]);

        huffmanEncoding.frequencies = huffmanEncoding.frequencyCountMap.values().toArray(new Integer[sizeOfMap]);

        huffmanEncoding.generateHuffmanCode();

    }


    /**
     * @param inputString
     */
    private void generateFrequencyMap(String inputString) {

        frequencyCountMap = new HashMap<Character, Integer>();

        char inputStringChar[] = inputString.toCharArray();
        int totalCount = 0;
        for (char c : inputStringChar) {
            Integer frequency = frequencyCountMap.get(c);
            totalCount++;
            if (frequency != null) {

                frequency++;
            } else {
                frequency = 1;
            }


            frequencyCountMap.put(c, frequency);
        }


        this.setTotalCharacters(totalCount);
    }


    private String readTextFromFile() {

        String fileContent = "";
        try {
            inputFileReader = new BufferedReader(new FileReader(HuffmanEncoding.INPUT_FILE_NAME));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        try {
            do{
                String input=inputFileReader.readLine();
                if(input==null){
                    break;
                }
                fileContent += input;
            }while(true);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }


    private void writeTextToFile(String stringToWrite) {
        try {
            outputFileWriter = new BufferedWriter(new FileWriter(HuffmanEncoding.OUTPUT_FILE_NAME, true));
            outputFileWriter.write(stringToWrite);
            outputFileWriter.write("\n\n");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String generateFrequencyFileOutput() {


        String frequencyStringToWrite = "Frequency-Table:\n\nSymbol\tFrequency\n";
        DecimalFormat numberFormat=new DecimalFormat("#.00");

        for (Character symbol : symbols) {
            Integer frequencyCount = this.frequencyCountMap.get(symbol);
            double frequencyCountVariable=((double)frequencyCount * 100 / (double)totalCharacters);
            frequencyStringToWrite = frequencyStringToWrite.concat(String.format("%3s",
                    String.format("%2s", symbol))).concat("\t\t").
                    concat(String.format("%6s", String.format("%4s",
                            numberFormat.format(frequencyCountVariable)).concat(" %"))).concat("\n");

        }
        System.out.println(frequencyStringToWrite);
        return frequencyStringToWrite;
    }

    private String generatePathFileOutput() {


        String huffmanCodeStringToWrite = "Code-Table:\n\nSymbol\tHuffman Codes\n";

        int totalBits = 0;
        for (Character symbol : symbols) {
            String huffmanCode = this.huffmanCodes.get(symbol);
            huffmanCodeStringToWrite = huffmanCodeStringToWrite.concat(String.format("%3s",
                    String.format("%2s", symbol))).concat("\t\t\t").concat(huffmanCode).concat("\n");
            totalBits += huffmanCode.length();
        }

        huffmanCodeStringToWrite = huffmanCodeStringToWrite.concat("\nTotal Bits : " + totalBits + "\n");
        System.out.println(huffmanCodeStringToWrite);
        return huffmanCodeStringToWrite;
    }


    public int getTotalCharacters() {
        return totalCharacters;
    }

    public void setTotalCharacters(int totalCharacters) {
        this.totalCharacters = totalCharacters;
    }

    private void generateHuffmanCode() {
        huffmanCodes = new HashMap<Character, String>();

        int totalSymbolsCount = frequencies.length;
        CustomHeap customHeap = new CustomHeap(totalSymbolsCount);

        for (int i = 0; i < totalSymbolsCount; i++) {
            HuffmanNode huffmanNode = new HuffmanNode();
            huffmanNode.setSymbol(symbols[i]);
            huffmanNode.setFrequencyCount(frequencies[i]);

            customHeap.heapify(huffmanNode, -1);
        }

        System.out.println("After adding all frequencies in Min Heap.");
        customHeap.displayHeap();
        sortDescending();
        writeTextToFile(generateFrequencyFileOutput());

        HuffmanNode rootNode = null;

        while (customHeap.getHeapSize() > 1) {

            HuffmanNode firstMinimumNode = customHeap.poll();
            HuffmanNode secondMinimumNode = customHeap.poll();

            HuffmanNode mergeNode = new HuffmanNode();
            mergeNode.setLeft(firstMinimumNode);
            mergeNode.setRight(secondMinimumNode);
            mergeNode.setSymbol('-');
            mergeNode.setFrequencyCount(firstMinimumNode.getFrequencyCount() + secondMinimumNode.getFrequencyCount());

            rootNode = mergeNode;

            customHeap.heapify(mergeNode, -1);
        }

        rootNode.traverseNode(rootNode, "", huffmanCodes);

        writeTextToFile(generatePathFileOutput());
    }


    public void sortDescending() {
        
        int tempInt = 0;
        char tempChar;
        int totalSymbolsCount = frequencies.length;
        for (int j = 0; j < (totalSymbolsCount - 1); j++) {
            for (int i = 0; i < (totalSymbolsCount - 1); i++) {
                if (frequencies[i] < frequencies[i + 1]) {
                    tempInt = frequencies[i];
                    frequencies[i] = frequencies[i + 1];
                    frequencies[i + 1] = tempInt;
                    tempChar = symbols[i];
                    symbols[i] = symbols[i + 1];
                    symbols[i + 1] = tempChar;
                }
            }
        }
    }
}


class HuffmanNode {
    private int frequencyCount;
    private char symbol;
    private HuffmanNode left;
    private HuffmanNode right;

    public int getFrequencyCount() {
        return frequencyCount;
    }

    public void setFrequencyCount(int frequencyCount) {
        this.frequencyCount = frequencyCount;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public void setRight(HuffmanNode right) {
        this.right = right;
    }

    public Map<Character, String> traverseNode(HuffmanNode nodeToTraverse, String path, Map<Character, String> codes) {
        if (nodeToTraverse != null) {

            if (nodeToTraverse.getLeft() != null) {
                codes = traverseNode(nodeToTraverse.getLeft(), path.concat("0"), codes);
            }

            if (nodeToTraverse.getRight() != null) {
                codes = traverseNode(nodeToTraverse.getRight(), path.concat("1"), codes);
            }

            if (!"-".equalsIgnoreCase(String.valueOf(nodeToTraverse.getSymbol()))) {
                codes.put(nodeToTraverse.getSymbol(), path);
            }
        }

        return codes;
    }

}


class CustomHeap {


    private Object[] customHeapArray;
    private int heapSize = 0;
    private int insertionPointer = 0;
    private int customArraySize = 0;
    // ideal grow size of array
    private int fudgeFactor = 2;

    public int getHeapSize() {
        return heapSize;
    }

    public int getInsertionPointer() {
        return insertionPointer;
    }

    public CustomHeap() {
        this(1);

    }

    public CustomHeap(int heapSize) {
        customArraySize = heapSize;
        customHeapArray = new Object[heapSize];
    }

    public int newSize() {
        return this.customArraySize * this.fudgeFactor;
    }

    public void heapify(HuffmanNode element, int heapifyCase) {

        switch (heapifyCase) {
            case -1: {

                /*
                 *   Insertion of elements into heap
                 *   validating and construction of max heap in the insertion case scenario
                 * */
                int parentIndex = 0;

                if (this.insertionPointer == this.customArraySize) {
                    /*
                     *   Its a growing array
                     * */
                    this.customArraySize = newSize();
                    this.customHeapArray = Arrays.copyOf(customHeapArray, customArraySize);
                }

                if (isHeapEmpty()) {
                    this.customHeapArray = new Object[this.customArraySize];
                    this.customHeapArray[this.insertionPointer] = element;
                    this.insertionPointer++;
                } else {
                    this.customHeapArray[insertionPointer] = element;
                    parentIndex = findParent(insertionPointer);
                    this.insertionPointer++;
                    int tempIndex = this.insertionPointer - 1;
                    while (tempIndex != 0 && ((HuffmanNode) this.customHeapArray[parentIndex]).getFrequencyCount() > ((HuffmanNode) this.customHeapArray[tempIndex]).getFrequencyCount()) {
                        swap(parentIndex, tempIndex);
                        tempIndex = parentIndex;
                        parentIndex = findParent(tempIndex);
                    }

                }
                heapSize++;

                break;
            }
            default: {
                /*
                 *   deletion at a specified node( including the root node)
                 *   deletion of node from the last node always
                 *   validating and construction of max heap in the insertion case scenario
                 * */
                int leftChild = -1;
                int rightChild = -1;
                do {
                    leftChild = findLeftChild(heapifyCase);
                    rightChild = findRightChild(heapifyCase);


                    if ((leftChild < this.insertionPointer && ((HuffmanNode) this.customHeapArray[leftChild]).getFrequencyCount() < element.getFrequencyCount())
                            && ((rightChild < this.insertionPointer
                            && ((HuffmanNode) this.customHeapArray[rightChild]).getFrequencyCount() >= ((HuffmanNode) this.customHeapArray[leftChild]).getFrequencyCount())
                            || (rightChild == this.insertionPointer))) {
                        swap(leftChild, heapifyCase);
                        heapifyCase = leftChild;
                    } else if (rightChild < this.insertionPointer && ((HuffmanNode) this.customHeapArray[rightChild]).getFrequencyCount() < element.getFrequencyCount()) {
                        swap(rightChild, heapifyCase);
                        heapifyCase = rightChild;
                    } else {
                        break;
                    }

                    // displayHeap();
                }
                while ((((HuffmanNode) this.customHeapArray[leftChild]).getFrequencyCount() < element.getFrequencyCount()
                        || ((HuffmanNode) this.customHeapArray[rightChild]).getFrequencyCount() <= element.getFrequencyCount())
                        && heapifyCase != this.insertionPointer);
            }
        }

    }

    public void displayHeap() {
        for (int i = 0; i < getHeapSize(); i++) {
            HuffmanNode huffmanNode = (HuffmanNode) this.customHeapArray[i];
            System.out.print(huffmanNode.getSymbol() + " : " + huffmanNode.getFrequencyCount() + "  ");
        }
        System.out.println();
    }

    private int findParent(int index) {
        return (index - 1) / 2;
    }

    private int findLeftChild(int index) {
        return ((2 * index) + 1);
    }

    private int findRightChild(int index) {
        return ((2 * index) + 2);
    }

    private void swap(int lower, int higher) {
        Object temp = this.customHeapArray[higher];
        this.customHeapArray[higher] = this.customHeapArray[lower];
        this.customHeapArray[lower] = temp;
    }

    private boolean isHeapEmpty() {
        return this.heapSize == 0;
    }

    public HuffmanNode poll() {

        /*
         *   deletion from root node
         * */
        HuffmanNode elementToReturn = (HuffmanNode) this.customHeapArray[0];
        this.insertionPointer--;
        this.heapSize--;
        this.customHeapArray[0] = this.customHeapArray[this.insertionPointer];
        this.customHeapArray[this.insertionPointer] = elementToReturn;
        heapify((HuffmanNode) this.customHeapArray[0], 0);
        return elementToReturn;
    }

    public Object[] getReverseCustomHeapArray() {

        Object[] reverseArray = new Object[this.getHeapSize()];
        int i = this.getHeapSize() - 1;
        for (Object object : customHeapArray) {
            reverseArray[i] = (HuffmanNode) object;
            i--;
        }
        return reverseArray;
    }
}
