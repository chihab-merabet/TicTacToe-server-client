import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToeClient {
    static int port = 12345;
    static String ip = "localhost";
    private static int playerID;
    private static char playerSymbol;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static TicTacToeGUI gui;

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket(ip, port);
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            playerID = in.readInt();
            playerSymbol = (playerID == 0) ? 'X' : 'O';

            SwingUtilities.invokeLater(() -> {
                gui = new TicTacToeGUI();
                gui.createAndShowGUI();
            });

            new Thread(TicTacToeClient::gameLoop).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void gameLoop() {
        try {
            boolean gameRunning = true;
            while (gameRunning) {
                String message = in.readUTF();
                if (message.equals("Your move")) {
                    gui.setStatus("Your move");
                    gui.setButtonsEnabled(true);
                } else if (message.equals("Board")) {
                    char[] board = new char[9];
                    for (int i = 0; i < 9; i++) {
                        board[i] = in.readChar();
                    }
                    gui.updateBoard(board);
                } else {
                    gui.setStatus(message);
                    gameRunning = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class TicTacToeGUI {
        private JFrame frame;
        private JButton[] buttons = new JButton[9];
        private JLabel statusLabel;

        public void createAndShowGUI() {
            frame = new JFrame("Tic Tac Toe - Player " + (playerID + 1) + " (" + playerSymbol + ")");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JPanel boardPanel = new JPanel();
            boardPanel.setLayout(new GridLayout(3, 3));
            for (int i = 0; i < 9; i++) {
                buttons[i] = new JButton("");
                buttons[i].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i].setFocusPainted(false);
                buttons[i].addActionListener(new ButtonClickListener(i));
                boardPanel.add(buttons[i]);
            }
            frame.add(boardPanel, BorderLayout.CENTER);

            statusLabel = new JLabel("Waiting for opponent...", SwingConstants.CENTER);
            frame.add(statusLabel, BorderLayout.SOUTH);

            frame.setSize(400, 400);
            frame.setVisible(true);
        }

        public void updateBoard(char[] board) {
            SwingUtilities.invokeLater(() -> {
                for (int i = 0; i < board.length; i++) {
                    buttons[i].setText(board[i] == '-' ? "" : String.valueOf(board[i]));
                    buttons[i].setEnabled(board[i] == '-');
                }
            });
        }

        public void setStatus(String status) {
            SwingUtilities.invokeLater(() -> statusLabel.setText(status));
        }

        public void setButtonsEnabled(boolean enabled) {
            SwingUtilities.invokeLater(() -> {
                for (JButton button : buttons) {
                    button.setEnabled(enabled && button.getText().isEmpty());
                }
            });
        }

        private class ButtonClickListener implements ActionListener {
            private int index;

            public ButtonClickListener(int index) {
                this.index = index;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    out.writeInt(index);
                    buttons[index].setText(String.valueOf(playerSymbol));
                    buttons[index].setEnabled(false);
                    setButtonsEnabled(false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
