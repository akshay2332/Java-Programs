import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TicTacToe {


    /*
    Assignment Group - 45
    CWID -  AKSHAY RANE (10442740)
            NEESHIT DANGI (10439010)

    */
    private final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private static final String NEW_GAME = "1";
    private static final String SAVE_GAME = "2";
    private int numberOfPlayers = 0;
    private int boardSize = 0;
    private int winSequence = 0;
    private final String[] palyersSymbols = {"X", "O", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "P", "Q",
            "R", "S", "T", "U", "V", "W", "Y", "Z"};
    private String[] currentPlayerSymbols = null;

    private String[][] gameBoard = null;
    private final int maxBoardSizeLimit = 999;
    private final int maxPlayerLimit = 26;
    private int maxMoves = 0;
    private int currentMoveCounter = 0;
    private int currentPlayerCounter = 0;
    private static final String FILE_EXTENSION = ".csv";
    private boolean gameContinueFlag = true;
    private int minimumMoveToWin = 0;


    public static void main(String args[]) {

        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.initializeGame();


    } //Main Function


    public void initializeGame() {
        String option = null;


        do {
            System.out.println("WELCOME TO TIC TAC TOE");
            System.out.println("SELECT THE GAME TYPE.");
            System.out.println("ENTER 1 FOR NEW GAME.");
            System.out.println("ENTER 2 FOR SAVED GAME.");
            try {
                option = bufferedReader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!TicTacToe.NEW_GAME.equals(option)
                    && !TicTacToe.SAVE_GAME.equals(option)) {
                System.out.println("WRONG INPUT");
                option = null;
            }
        }
        while (option == null);


        switch (option.charAt(0)) {
            case '1':
                startNewGame();
                if (minimumMoveToWin != 0) {
                    playGame();
                } else {
                    System.out.println("Kindly enter the game details again.");
                    initializeGame();
                }
                break;
            case '2':
                /*
                 Load the saved game
                 Load the saved game from xls file by asking the input name of the file
                */
                loadGame();
                playGame();
                break;
        }
    }

    public void startNewGame() {

        //Load a fresh game
        System.out.println("Enter the number of players");
        try {
            do {
                numberOfPlayers = Integer.parseInt(bufferedReader.readLine());
            }
            while (numberOfPlayers > maxPlayerLimit);

            populatePlayingPlayerSymbols(numberOfPlayers);

            do {
                System.out.println("Enter the board size");
                boardSize = Integer.parseInt(bufferedReader.readLine());
            }
            while (boardSize < numberOfPlayers || boardSize > maxBoardSizeLimit);

            maxMoves = boardSize * boardSize;

            gameBoard = new String[boardSize][boardSize];

            do {
                System.out.println("Enter the winning Sequence");
                winSequence = Integer.parseInt(bufferedReader.readLine());
            }
            while (winSequence > boardSize);

            minimumMoveToWin = (numberOfPlayers * (winSequence - 1));

            displayGameBoard();

        } catch (Exception e) {
            e.printStackTrace();
        }

    } //startNewGame

    public void populatePlayingPlayerSymbols(int numberOfPlayers) {
        currentPlayerSymbols = new String[numberOfPlayers];
        /*
         *   Copy the symbols from final structure to
         *   current players symbols
         *
         */
    } // populatePlayingPlayerSymbols

    public String fetchCurrentPlayerSymbols(int playerNumber) {
        return palyersSymbols[playerNumber];
    } // fetchCurrentPlayerSymbols

    public void displayGameBoard() {
        /*
         *   Display Board from the Matrice
         */


        /*
         *   Bad code need to replace
         *
         */
        System.out.print(String.format("%2s", ""));
        for (int k = 0; k < boardSize; k++) {
            System.out.print(String.format("%4s", String.format("%-2s", (k + 1))));
        }
        System.out.println();


        for (int i = 0; i < boardSize; i++) {

            System.out.print(String.format("%3s", String.format("%-2s", (i + 1))));

            for (int j = 0; j < boardSize; j++) {
                if (gameBoard[i][j] == null) {
                    System.out.print(String.format("%3s", String.format("%-2s", " ")));
                } else {
                    System.out.print(String.format("%3s", String.format("%-2s", gameBoard[i][j])));
                }

                if (j != boardSize - 1) {
                    System.out.print("|");
                }
            }
            /*
             *   Code to be removed
             *   Bad code for displaying purpose only
             */
            System.out.println();
            if (i != boardSize - 1) {
                System.out.print(String.format("%3s", ""));
                for (int k = 0; k < boardSize; k++) {
                    System.out.print("---");
                    if (k != boardSize - 1) {
                        System.out.print("+");
                    }
                }
            }
            System.out.println();


        }
    } //display game


    public void playGame() {

        int playerCounter = 0;
        if (currentPlayerCounter != 0) {
            playerCounter = currentPlayerCounter;
        }

        String playerGameOption = readGameOption(playerCounter);

        boolean askQuestions = false;
        int row = 0;
        int column = 0;
        String rowColumn = null;
        boolean gameWon = false;

        while (currentMoveCounter < maxMoves
                && !gameWon
                && gameContinueFlag
                && "C".equalsIgnoreCase(playerGameOption)) {
            currentMoveCounter++;
            do {
                System.out.println("Enter the row and column separated by space for player - " + (playerCounter + 1));
                try {
                    rowColumn = bufferedReader.readLine();
                    String[] inputs = rowColumn.split(" ", -1);
                    row = Integer.parseInt(inputs[0]);
                    column = Integer.parseInt(inputs[1]);

                    if (row > boardSize || column > boardSize) {
                        System.out.println("Enter proper coordinates");
                        throw new IllegalArgumentException();
                    }

                    boolean cellEmptyCheck = checkCellEmpty(row, column);

                    if (!cellEmptyCheck) {
                        System.out.println("Enter the coordinates that are not occupied.");
                        throw new IllegalArgumentException();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    /*
                     *   Doing this as game cannot be continued without input being proper
                     */
                    rowColumn = null;
                }
            }
            while (rowColumn == null);

            String userSymbol = fetchCurrentPlayerSymbols(playerCounter);
            gameBoard[row - 1][column - 1] = userSymbol;
            displayGameBoard();

            if (currentMoveCounter > minimumMoveToWin) {
                gameWon = checkGameWon(userSymbol);
            }


            if (!gameWon) {
                askQuestions = true;
                if (playerCounter == (numberOfPlayers - 1)) {
                    playerCounter = 0;
                } else {
                    playerCounter++;
                }
                currentPlayerCounter = playerCounter;
            } else {
                askQuestions = false;
            }

            if (askQuestions && gameContinueFlag) {
                playerGameOption = readGameOption(playerCounter);
            }
        }

        if ("Q".equalsIgnoreCase(playerGameOption)) {
            System.out.println("YOU HAVE EXITED THE GAME.");
        } else if ("S".equalsIgnoreCase(playerGameOption)) {
            saveGame();
        } else {
            System.out.println("------------------------------------------------------------------");
            if (gameWon) {

                displayGameBoard();
                System.out.println();
                System.out.println("The game is won by player " + (playerCounter + 1) + " with symbol " + fetchCurrentPlayerSymbols(playerCounter));
            } else {
                displayGameBoard();
                System.out.println("The game is drawn.");
            }
        }

    }


    public String readGameOption(int playerNumber) {

        String gameOption = null;

        do {
            System.out.println("Enter Q to quit the game.");    //Changes made in this version  (in the previous version, user was asked to press '1') //
            System.out.println("Enter S to save the game.");    //Changes made in this version (in the previous version, user was asked to press '2') //
            System.out.println("Enter C to continue to enter the cell details for Player[" + (playerNumber + 1) + "].");   //Changes made in this version  (in the previous version, user was asked to press '3') //
            try {
                gameOption = bufferedReader.readLine();
                gameOption = String.valueOf(gameOption.charAt(0)).trim().toUpperCase();
            } catch (Exception e) {
                e.printStackTrace();
                gameOption = null;
            }

        } while (gameOption == null);

        return gameOption;
    }


    public boolean checkCellEmpty(int row, int column) {
        if (gameBoard[row - 1][column - 1] == null || gameBoard[row - 1][column - 1].isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkGameDrawn() {

        boolean gameContinuedFlag = true;
        /*
         *  Check if game can be won by any player
         *  if gameContinuedFlag = true then there is no need to perform any check just continue the game
         */


        for (int player = 0; player < numberOfPlayers; player++) {

            for (int rowLine = 0; rowLine < boardSize; rowLine++) {
                for (int columnLine = 0; columnLine < boardSize; columnLine++) {
                    String currentPlayerSymbol = fetchCurrentPlayerSymbols(player);
                    if (currentPlayerSymbol.equalsIgnoreCase(gameBoard[rowLine][columnLine])) {
                        /*
                         *   checking the whole game board for win condition
                         *
                         */

                        int lenghtOfCheck = columnLine + winSequence;

                        if (lenghtOfCheck <= boardSize) {

                            int winningCounter = 0;
                            /*
                             *   check first row
                             */

                            for (int firstRow = columnLine; firstRow < lenghtOfCheck; firstRow++) {

                                if (gameBoard[rowLine][firstRow] == null || gameBoard[rowLine][firstRow].isEmpty() || currentPlayerSymbol.equalsIgnoreCase(gameBoard[rowLine][firstRow])) {
                                    winningCounter++;
                                } else {
                                    break;
                                }

                                if (winningCounter == winSequence) {
                                    return true;
                                } else {
                                    gameContinuedFlag = false;
                                }

                            }



                            /*
                             *   first column
                             *
                             */
                            winningCounter = 0;
                            for (int firstColumn = rowLine; firstColumn < lenghtOfCheck; firstColumn++) {

                                if (gameBoard[firstColumn][columnLine] == null

                                        || gameBoard[firstColumn][columnLine].isEmpty()

                                        || currentPlayerSymbol.equalsIgnoreCase(gameBoard[firstColumn][columnLine])) {
                                    winningCounter++;
                                } else {
                                    break;
                                }

                                if (winningCounter == winSequence) {
                                    return true;
                                } else {
                                    gameContinuedFlag = false;
                                }
                            }


                            /*
                             *   first diagonal
                             *
                             */
                            winningCounter = 0;
                            int tempRowIndex = rowLine;
                            for (int diagonal = columnLine; diagonal < lenghtOfCheck && tempRowIndex < lenghtOfCheck; diagonal++, tempRowIndex++) {

                                if (gameBoard[tempRowIndex][diagonal] == null
                                        || gameBoard[tempRowIndex][diagonal].isEmpty()
                                        || currentPlayerSymbol.equalsIgnoreCase(gameBoard[tempRowIndex][diagonal])) {
                                    winningCounter++;
                                } else {
                                    break;
                                }

                                if (winningCounter == winSequence) {
                                    return true;
                                } else {
                                    gameContinuedFlag = false;
                                }

                            }

                            /*
                             *   reverse diagonal1
                             *
                             */
                            tempRowIndex = rowLine;
                            winningCounter = 0;
                            for (int revDiagonal = (columnLine + winSequence) - 1; revDiagonal >= rowLine && revDiagonal < lenghtOfCheck; revDiagonal--, tempRowIndex++) {

                                if (gameBoard[tempRowIndex][revDiagonal] == null

                                        || gameBoard[tempRowIndex][revDiagonal].isEmpty()

                                        || currentPlayerSymbol.equalsIgnoreCase(gameBoard[tempRowIndex][revDiagonal])) {
                                    winningCounter++;
                                } else {
                                    break;
                                }

                                if (winningCounter == winSequence) {
                                    return true;
                                } else {
                                    gameContinuedFlag = false;
                                }

                            }


                        } else {

                            if ((rowLine + winSequence) <= boardSize) {
                                continue;
                            } else {
                                gameContinuedFlag = false;
                            }

                        }

                    }
                }
            }
        }
        return gameContinuedFlag;
    }


    public boolean checkGameWon(String currentPlayerSymbol) {

        /*
         *   checking the whole game board for win condition
         *
         */


        for (int rowLine = 0; rowLine < boardSize; rowLine++) {


            for (int columnLine = 0; columnLine < boardSize; columnLine++) {
                int winningCounter = 0;
                /*
                 *   check first row
                 */
                for (int firstRow = columnLine; firstRow < boardSize; firstRow++) {

                    if (currentPlayerSymbol.equalsIgnoreCase(gameBoard[rowLine][firstRow])) {
                        winningCounter++;
                    } else {
                        break;
                    }

                    if (winningCounter == winSequence) {
                        return true;
                    }
                }

                /*
                 *   first column
                 *
                 */
                winningCounter = 0;
                for (int firstColumn = rowLine; firstColumn < boardSize; firstColumn++) {

                    if (currentPlayerSymbol.equalsIgnoreCase(gameBoard[firstColumn][columnLine])) {
                        winningCounter++;
                    } else {
                        break;
                    }

                    if (winningCounter == winSequence) {
                        return true;


                    }
                }

                /*
                 *   first diagonal
                 *
                 */
                winningCounter = 0;
                int tempRow = rowLine;
                for (int diagonal = columnLine; diagonal < boardSize && tempRow < boardSize; diagonal++, tempRow++) {

                    if (currentPlayerSymbol.equalsIgnoreCase(gameBoard[tempRow][diagonal])) {
                        winningCounter++;
                    } else {
                        break;
                    }

                    if (winningCounter == winSequence) {
                        return true;

                    }
                }

                /*
                 *   reverse diagonal1
                 *
                 */
                winningCounter = 0;
                int temp = rowLine;
                for (int revDiagonal = (columnLine + winSequence) - 1; revDiagonal >= columnLine && revDiagonal < boardSize && temp < boardSize; revDiagonal--, temp++) {


                    if (currentPlayerSymbol.equalsIgnoreCase(gameBoard[temp][revDiagonal])) {
                        winningCounter++;
                    } else {
                        break;
                    }

                    if (winningCounter == winSequence) {
                        return true;
                    }

                }

            }


        }

        /*
         *   Check for draw only in case the current player has not won after making moves
         *
         */

        gameContinueFlag = checkGameDrawn();


        return false;

    }


    public void saveGame() {
        /*
         *   Saving the game in CSV format as easy viewing and reading
         *   XLS also an option but external jars required
         */
        String fileName = null;
        BufferedWriter writer = null;
        do {
            System.out.println("Enter the file Name to store the game.");
            try {
                fileName = bufferedReader.readLine();
                writer = new BufferedWriter(new FileWriter(fileName.concat(TicTacToe.FILE_EXTENSION)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (fileName == null);




        /*
         *   First storing the metadata of the game
         *   Number of players , Winning sequence , BoardSize , Moves made , Current Player
         */


        try {
            writer.write(String.valueOf(numberOfPlayers));
            writer.write(",");
            writer.write(String.valueOf(winSequence));
            writer.write(",");
            writer.write(String.valueOf(boardSize));
            writer.write(",");
            writer.write(String.valueOf(currentMoveCounter));
            writer.write(",");
            writer.write(String.valueOf(currentPlayerCounter));
            writer.write("\n");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        for (String[] elements : gameBoard) {
            try {
                int len = elements.length;
                for (int eLen = 0; eLen < len; eLen++) {
                    if (elements[eLen] == null) {
                        writer.write("");
                    } else {
                        writer.write(elements[eLen]);
                    }

                    if (eLen != len - 1)
                        writer.write(",");
                }
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        String fileName = null;
        String currentElementRow = null;
        BufferedReader fileBufferedReader = null;
        do {
            System.out.println("Enter the file Name to load the game.");
            try {
                fileName = bufferedReader.readLine();
                FileReader fileReader = new FileReader(fileName.concat(TicTacToe.FILE_EXTENSION));
                fileBufferedReader = new BufferedReader(fileReader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (fileName == null);

        List<String[]> cellElements = new ArrayList<String[]>();


        try {

            /*
             *   Fetching first row for meta data
             *
             */

            currentElementRow = fileBufferedReader.readLine();
            String[] metaData = currentElementRow.split(",", -1);

            numberOfPlayers = Integer.parseInt(metaData[0]);
            winSequence = Integer.parseInt(metaData[1]);
            boardSize = Integer.parseInt(metaData[2]);
            currentMoveCounter = Integer.parseInt(metaData[3]);
            currentPlayerCounter = Integer.parseInt(metaData[4]);

            minimumMoveToWin = (numberOfPlayers * (winSequence - 1));
            maxMoves = boardSize * boardSize;

            /*

            System.out.println("number of players" + numberOfPlayers);
            System.out.println("board size" + boardSize);
            System.out.println("win sequence" + winSequence);
            System.out.println("currentMoveCounter" + currentMoveCounter);
            System.out.println("number of players" + numberOfPlayers);
            System.out.println("currentPlayerCounter" + currentPlayerCounter);
            */


            /*
             *   Fetching other rows
             *
             */
            while ((currentElementRow = fileBufferedReader.readLine()) != null) {
                cellElements.add(currentElementRow.split(",", -1));
            }

            // convert our list to a String array.
            gameBoard = new String[boardSize][boardSize];
            cellElements.toArray(gameBoard);


            displayGameBoard();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
