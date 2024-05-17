import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TicTacToeClient {
    static int port = 12345;
    static String ip = "localhost";

    public static void main(String[] args) throws IOException {
        Socket clientSocket = new Socket(ip, port);
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        int playerID = in.readInt();
        char playerSymbol = (playerID == 0) ? 'X' : 'O';
        System.out.println("Connected as Player " + (playerID + 1) + " (" + playerSymbol + ")");

        boolean gameRunning = true;
        while (gameRunning) {
            String message = in.readUTF();
            if (message.equals("Your move")) {
                System.out.print("Enter your move (0-8): ");
                int move = scanner.nextInt();
                out.writeInt(move);
            } else {
                System.out.println(message);
                gameRunning = false;
            }
        }

        in.close();
        out.close();
        clientSocket.close();
        scanner.close();
    }
}
