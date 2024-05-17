import java.io.*;
import java.net.*;

public class TicTacToeServer {
    static int port = 12345;
    private static char[] board = new char[9];
    private static int currentPlayer = 0;
    private static DataOutputStream[] playerOutputs = new DataOutputStream[2];
    private static DataInputStream[] playerInputs = new DataInputStream[2];
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port: " + port);
        System.out.println("Waiting for players");

        // Initialize the game board
        for (int i = 0; i < board.length; i++) {
            board[i] = '-';
        }

        // Accept player connections
        for (int i = 0; i < 2; i++) {
            Socket playerSocket = serverSocket.accept();
            System.out.println("Player " + (i + 1) + " connected!");
            playerOutputs[i] = new DataOutputStream(playerSocket.getOutputStream());
            playerInputs[i] = new DataInputStream(playerSocket.getInputStream());
            playerOutputs[i].writeInt(i); // Send player ID (0 or 1)
        }

        // Game loop
        boolean gameRunning = true;
        while (gameRunning) {
            // Notify current player to make a move
            playerOutputs[currentPlayer].writeUTF("Your move");
            int move = playerInputs[currentPlayer].readInt();
            board[move] = (currentPlayer == 0) ? 'X' : 'O';
            sendBoardState();
            displayBoard();

            // Check for a winner or a tie
            if (checkWinner()) {
                playerOutputs[currentPlayer].writeUTF("You win!");
                playerOutputs[1 - currentPlayer].writeUTF("You lose!");
                gameRunning = false;
            } else if (isBoardFull()) {
                for (DataOutputStream out : playerOutputs) {
                    out.writeUTF("It's a tie!");
                }
                gameRunning = false;
            }

            // Switch player
            currentPlayer = 1 - currentPlayer;
        }

        // Close connections
        for (int i = 0; i < 2; i++) {
            playerOutputs[i].close();
            playerInputs[i].close();
        }
        serverSocket.close();
    }

    private static void sendBoardState() throws IOException {
        for (DataOutputStream out : playerOutputs) {
            out.writeUTF("Board");
            for (char c : board) {
                out.writeChar(c);
            }
        }
    }

    private static void displayBoard() {
        System.out.println("Current board:");
        for (int i = 0; i < board.length; i++) {
            System.out.print(board[i] + " ");
            if ((i + 1) % 3 == 0) System.out.println();
        }
    }

    private static boolean checkWinner() {
        int[][] winPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns
            {0, 4, 8}, {2, 4, 6}             // diagonals
        };

        for (int[] wp : winPositions) {
            if (board[wp[0]] != '-' && board[wp[0]] == board[wp[1]] && board[wp[1]] == board[wp[2]]) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBoardFull() {
        for (char c : board) {
            if (c == '-') return false;
        }
        return true;
    }
}
