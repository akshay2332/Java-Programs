import java.util.Scanner;
import java.util.regex.Pattern;


public class ReversePolishNotationCalculator {


    /*
     *   Assignment group 45 Member Id
     *   Akshay Rane (10442740)
     *   Neeshit Dangi (10439010)
     *   Amit Vadnere (10442085)
     * */


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String choice = "";
        do {
            System.out.println("Input Infix Expression");
            String inputString = sc.nextLine();
            inputString = inputString.replaceAll(" ", "");
            inputString = inputString.toLowerCase();
            inputString = inputString.replaceAll("pow", "^");

            boolean validExpression = true;

            if(inputString==null || inputString.isEmpty()){
                /*
                *   String invalid
                * */
                validExpression=false;
            }else {
                isValidExpression2(inputString);
            }


            if (!validExpression) {
                System.out.println("Invalid Expression. Please enter new valid expression.");
            } else {
                System.out.println();
                ReversePolishNotationCalculator reversePolishNotationCalculator = new ReversePolishNotationCalculator();
                Stack evaluatorStack = reversePolishNotationCalculator.conversionInfixPostfix(inputString);
                try {
                    System.out.println("Postfix Stack for evaluation");
                    evaluatorStack.displayStack();
                    evaluatorStack = reversePolishNotationCalculator.evaluateExpression(evaluatorStack, null, new String[evaluatorStack.getSize()], 0);
                    System.out.println("The result of infix notation  " + inputString + " is " + evaluatorStack.pop());
                } catch (ArithmeticException ae) {
                    System.out.println("The infix notation cannot be evaluated " + ae.getMessage());
                }
                System.out.println("Enter 'QUIT' to exit. Press any other key to continue");
                choice = sc.nextLine();
            }
        } while (!"QUIT".equalsIgnoreCase(choice));


    }


    //-------------Code snippet taken from https://codereview.stackexchange.com/questions/181606/determine-if-simple-arithmetic-expression-is-valid------------------//


    private static boolean isAnOperator(char c) {
        switch (c) {
            case '*':
            case '/':
            case '+':
            case '-':
            case '%':
            case '.':
            case '^':

                return true;
            default:
                return false;
        }
    }


    private static boolean isANumber(char c) {
        return ((int) c) >= 48 && ((int) c) <= 57;
    }

    private static boolean isValidExpression2(String expression) {
        // TEST 1
        if (isAnOperator(expression.charAt(0)) || isAnOperator(expression.charAt(expression.length() - 1))) {
            return false;
        }

        int openParentsCount = 0;
        boolean lastWasOp = false;
        boolean lastWasOpen = false;

        for (char c : expression.toCharArray()) {
            if (c == ' ')
                continue;
            if (c == '(') {
                openParentsCount++;
                lastWasOpen = true;
                continue;
            } else if (c == ')') {
                if (openParentsCount <= 0 || lastWasOp) {
                    return false;
                }
                openParentsCount--;
            } else if (isAnOperator(c)) {
                if (lastWasOp || lastWasOpen)
                    return false;
                lastWasOp = true;
                continue;
            } else if (!isANumber(c)) {
                System.out.println("PROBLEM:" + c);
                return false;
            }
            lastWasOp = false;
            lastWasOpen = false;
        }
        if (openParentsCount != 0) return false;
        if (lastWasOp || lastWasOpen) return false;
        return true;
    }

    //---------------------------------------------------------------------------Code snippet Ends----------------------------------------------------------------//


    private Stack evaluateExpression(Stack evaluatorStack, String operation, String[] poppedElement, int poppedElementCounter) {

        double operand[] = new double[2];
        int operandCounter = 0;
        String currentChar;

        while (evaluatorStack.getSize() != 0) {

            if (Pattern.compile("[0-9]").matcher(evaluatorStack.getStackPointer().getData()).find()) {
                currentChar = evaluatorStack.pop();
                poppedElement[poppedElementCounter] = currentChar;
                operand[operandCounter] = Double.parseDouble(String.valueOf(currentChar));
                operandCounter++;
                poppedElementCounter++;

                if (evaluatorStack.getSize() == 0 && poppedElementCounter == 1) {
                    evaluatorStack.push(currentChar);
                    return evaluatorStack;
                }

            } else {
                currentChar = evaluatorStack.pop();
                poppedElement[poppedElementCounter] = currentChar;
                poppedElementCounter++;
                operation = currentChar;
                evaluatorStack = evaluateExpression(evaluatorStack, operation, poppedElement, poppedElementCounter);
                poppedElementCounter = 0;
                operandCounter = 0;
                if (evaluatorStack.getSize() == 1) {
                    return evaluatorStack;
                }
            }

            if (operandCounter == 2) {

                poppedElementCounter--;
                poppedElement[poppedElementCounter] = "";
                poppedElementCounter--;
                poppedElement[poppedElementCounter] = "";
                poppedElementCounter--;
                //  remove the element from stack

                double result = 0;
                switch (operation.charAt(0)) {

                    case '+':
                        result = add(operand[0], operand[1]);
                        break;

                    case '-':

                        result = subtract(operand[1], operand[0]);
                        break;

                    case '*':

                        result = multiply(operand[0], operand[1]);
                        break;

                    case '/':

                        result = divide(operand[1], operand[0]);
                        break;

                    case '^':
                        result = power(operand[1], operand[0]);
                        break;
                    case '%':
                        result = modulus(operand[1], operand[0]);
                        break;
                }
                evaluatorStack.push(String.valueOf(result));

                if (poppedElementCounter != 0) {
                    for (int i = poppedElementCounter - 1; i >= 0; i--) {
                        evaluatorStack.push(poppedElement[i]);
                    }
                }
                System.out.println("After pushing the result on the stack");
                evaluatorStack.displayStack();

                return evaluatorStack;
            }
        }
        return evaluatorStack;

    }


    private double add(double operand1, double operand2) throws ArithmeticException {
        try {
            return operand1 + operand2;
        } catch (Exception e) {
            throw new ArithmeticException(e.getMessage());
        }
    }

    private double subtract(double operand1, double operand2) throws ArithmeticException {
        try {
            return operand1 - operand2;
        } catch (Exception e) {
            throw new ArithmeticException(e.getMessage());
        }
    }

    private double multiply(double operand1, double operand2) throws ArithmeticException {
        try {
            return operand1 * operand2;
        } catch (Exception e) {
            throw new ArithmeticException(e.getMessage());
        }
    }

    private double divide(double operand1, double operand2) throws ArithmeticException {
        try {
            return operand1 / operand2;
        } catch (Exception e) {
            throw new ArithmeticException(e.getMessage());
        }

    }

    private double power(double operand1, double operand2) throws ArithmeticException {
        try {
            return Math.pow(operand1, operand2);
        } catch (Exception e) {
            throw new ArithmeticException(e.getMessage());
        }
    }

    private double modulus(double operand1, double operand2) throws ArithmeticException {
        try {
            return operand1 % operand2;

        } catch (Exception e) {
            throw new ArithmeticException(e.getMessage());
        }
    }

    /*
    *   Code written for standardizing the input string for undisturbed evaluation and conversion of expression
    */
    private LinearQueue formatInputString(String inputString) {

        String infixFormattedSprig = "";
        char[] infixElements = inputString.toCharArray();

        for (char currentChar : infixElements) {
            if (String.valueOf(currentChar).matches("[0-9]")) {
                infixFormattedSprig = infixFormattedSprig.concat(String.valueOf(currentChar));
            } else {
                String temp = " " + currentChar + " ";
                infixFormattedSprig = infixFormattedSprig.concat(temp);
            }
        }
        infixFormattedSprig = infixFormattedSprig.trim();
        infixFormattedSprig = infixFormattedSprig.replaceAll(" {2}", " ");

        String[] infixExpression = infixFormattedSprig.split(" ", -1);
        LinearQueue linearQueue = new LinearQueue();

        for (String infixString : infixExpression) {
            linearQueue.enqueue(infixString);
        }
        return linearQueue;
    }

    /*
    *   Converting the Infix expression to Postfix expression
    *
    * */
    private Stack conversionInfixPostfix(String inputString) {
        Stack postfixStackPointer = new Stack();
        LinearQueue infixExpressionQueue = formatInputString(inputString);
        infixExpressionQueue.displayQueue();
        LinearQueue postFixExpressionQueue = new LinearQueue();

        Node front = infixExpressionQueue.getFront();
        while (front != null) {
            String currentElement = front.getData();

            front = front.getNext();
            if (Pattern.compile("[0-9]").matcher(currentElement).find()) {

                postFixExpressionQueue.enqueue(currentElement);


            } else if ("(".equalsIgnoreCase(currentElement)) {

                postfixStackPointer.push(currentElement);
            } else if (")".equalsIgnoreCase(currentElement)) {

                // pop all the elements till we get first opening bracket
                String currentPoppedElement;
                do {
                    currentPoppedElement = postfixStackPointer.pop();
                    if (!("(".equalsIgnoreCase(currentPoppedElement))) {
                        postFixExpressionQueue.enqueue(currentPoppedElement);
                    }
                }
                while (!"(".equalsIgnoreCase(currentPoppedElement));
            } else {
                while (postfixStackPointer.getStackPointer() != null && precedence(currentElement) <= precedence(postfixStackPointer.peek())) {
                    postFixExpressionQueue.enqueue(postfixStackPointer.pop());
                }
                postfixStackPointer.push(currentElement);
            }
        }

        String currentElementPopped;

        while (postfixStackPointer.getStackPointer() != null) {
            currentElementPopped = postfixStackPointer.pop();
            postFixExpressionQueue.enqueue(currentElementPopped);
        }

        System.out.println("The postfix Representation of the expression is ");

        front = postFixExpressionQueue.getFront();

        while (front != null) {
            String currentElement = front.getData();
            System.out.print(currentElement + " ");
            postfixStackPointer.push(currentElement);
            front = front.getNext();
        }

        System.out.println();

        return postfixStackPointer;
    }

    private int precedence(String character) {
        switch (character.charAt(0)) {
            case '+':
                return 1;

            case '-':
                return 1;

            case '*':
                return 2;

            case '/':
                return 2;
                
            case '%':
                return 2;
                
            case '^':
                return 3;

            default:
                return -1;
        }
    }
}


class Node {
    private String data;
    private Node next;

    public Node() {
        this.data = null;
        this.next = null;
    }

    public Node(String data) {
        this.data = data;
        this.next = null;
    }

    public Node(String data, Node next) {
        this.data = data;
        this.next = next;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}

class Stack {
    private Node stackPointer;
    private int size;

    Stack() {
    }


    public void push(String data) {
        Node newNode = new Node(data);
        if (stackPointer == null) {
            stackPointer = newNode;
        } else {
            newNode.setNext(stackPointer);
            stackPointer = newNode;
        }
        size++;
    }

    public void displayStack() {
        if (size != 0) {
            /*
             *  The stack always start from stack pointer
             */
            Node pointerForDisplay = stackPointer;
            System.out.print("StackPointer-->");
            do {
                System.out.print("'" + pointerForDisplay.getData() + "'");
                pointerForDisplay = pointerForDisplay.getNext();
                if (pointerForDisplay != null) {
                    System.out.print("--->");

                }
            } while (pointerForDisplay != null);
            System.out.println("");
        }
    }

    public String pop() {
        if (stackPointer == null) {
            return null;
        } else if (stackPointer.getNext() == null) {
            size--;
            String temp = stackPointer.getData();
            stackPointer = null;
            return temp;
        } else {
            String temp = stackPointer.getData();
            stackPointer = stackPointer.getNext();
            size--;
            return temp;
        }

    }

    public String peek() {
        if (stackPointer == null) {
            return null;
        } else {
            return stackPointer.getData();
        }
    }

    public Node getStackPointer() {
        return stackPointer;
    }

    public void setStackPointer(Node stackPointer) {
        this.stackPointer = stackPointer;
    }

    public int getSize() {
        return size;
    }
}


class LinearQueue {

    private Node front;
    private Node rear;
    // size will be initialised to zero
    private int size;

    /*
     *   insert element into queue
     */
    public void enqueue(String data) {
        Node currentLinkedListNode = new Node(data, null);
        if (rear == null) {
            front = currentLinkedListNode;
            rear = currentLinkedListNode;
        } else {
            rear.setNext(currentLinkedListNode);
            rear = currentLinkedListNode;
        }
        // after adding the element increment the queue size
        size++;
    }

    public void displayQueue() {
        if (size != 0) {
            /*
             *  The queue always start from front pointer
             */
            Node pointerForDisplay = front;
            do {
                System.out.print(pointerForDisplay.getData());
                pointerForDisplay = pointerForDisplay.getNext();
                if (pointerForDisplay != null) {
                    System.out.print("");

                }
            } while (pointerForDisplay != null);
            System.out.println();
        }
    }

    public void peek() {
        if (front == null) {
            System.out.println("The queue is empty.");
        } else {
            System.out.println("Element of queue " + rear.getData());
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Node getFront() {
        return front;
    }

    public void setFront(Node front) {
        this.front = front;
    }

    public Node getRear() {
        return rear;
    }

    public void setRear(Node rear) {
        this.rear = rear;
    }
}


