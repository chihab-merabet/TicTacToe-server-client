# Tic Tac Toe with Java (Terminal & GUI) 🆚

This repository implements a Tic Tac Toe game with two versions and multiplayer support, built entirely in Java!

## Features

- **Terminal-based**: Play directly in the terminal for a classic experience.
- **Graphical User Interface (GUI)**: Built with Swing for a user-friendly graphical experience.
- **Multiplayer Support**: Both versions utilize a server-client architecture with sockets, allowing you to challenge friends across devices!

## Ideal for:

- **Beginners**: Learn about socket programming, network communication, and building multiplayer games in Java.
- **Developers**: A well-structured Tic Tac Toe implementation with both command-line and GUI options.

## Getting Started

### Prerequisites:

- Latest Java Development Kit (JDK) - [Download from Oracle](https://www.oracle.com/java/technologies/downloads/)

### Instructions:

#### Run the Server:

1. Open a terminal.
2. Navigate to the project directory.
3. Execute the server script:
   ```bash
   java TicTacToeServer.java

#### Run the Client:

1. On the same device or another computer, navigate to the project directory.
2. Run the client script:
   ````bash
   java TicTacToeClient.java
(Run twice on the same device to play against yourself!)

### Playing on Different Devices:
By default, the client connects to the server on the same machine (localhost). To play on a different device, modify the server's IP address within the client script before running it. The port is pre-defined, but can be modified if necessary.

## Feel free to explore, contribute, and build upon this code!
