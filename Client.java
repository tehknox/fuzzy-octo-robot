import Game.*;
import Logic.MoveGenerator;
import Logic.*;

import java.io.*;
import java.net.Socket;

public class Client
{
    public static void main(String[] args)
    {
        //testMoves(); if (1 == 1) return;

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        Board board = new Board();
        Board rollbackBoard = new Board(); // Roll-back board in case there's an error

        try
        {
            // Connect to the server
            MyClient = new Socket("localhost", 8888);
            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while(1 < 2)
            {
                // Read a command
                char cmd = (char)input.read();

                // The game begins as the white player
                if(cmd == '1')
                {
                    System.out.println("Player: White");

                    // Read the input data
                    byte[] aBuffer = new byte[1024];
                    int size = input.available();
                    input.read(aBuffer, 0, size);

                    // Set the player as white
                    board.setPlayerIsBlack(false);

                    // Generate the board
                    board.generateBoard(new String(aBuffer));
                    rollbackBoard = new Board(board);

                    // Generate a game tree
                    GameTree tree = new GameTree(board.getBoard(), board.isPlayerIsBlack(), 4);

                    // Rock on
                    Move nextMove = tree.findBestMove(board.isPlayerIsBlack());
                    board.movePawn(nextMove);
                    String move = nextMove.getMoveAsString();
                    output.write(move.getBytes(), 0, move.length());
                    output.flush();

                    // Display the game
                    board.printBoard();
                }

                // The game begins as the black player
                if(cmd == '2')
                {
                    System.out.println("Player: Black");

                    // Read the input data
                    byte[] aBuffer = new byte[1024];
                    int size = input.available();
                    input.read(aBuffer, 0, size);

                    // Set the player as black
                    board.setPlayerIsBlack(true);

                    // Generate the board
                    board.generateBoard(new String(aBuffer));
                    rollbackBoard = new Board(board);

                    // Print the board
                    board.printBoard();
                }


                // The server requests the next move, and send the other player's move
                if(cmd == '3')
                {
                    // Keep the roll-back in memory
                    rollbackBoard = new Board(board);

                    // Read the input data
                    byte[] aBuffer = new byte[16];
                    int size = input.available();
                    input.read(aBuffer, 0, size);

                    // Update the board
                    String s = new String(aBuffer);
                    board.movePawn(new Move(s));

                    // Generate a game tree
                    GameTree tree = new GameTree(board.getBoard(), board.isPlayerIsBlack(), 4);

                    // Rock on
                    Move nextMove = tree.findBestMove(board.isPlayerIsBlack());
                    board.movePawn(nextMove);
                    String move = nextMove.getMoveAsString();
                    output.write(move.getBytes(), 0, move.length());
                    output.flush();

                    // Display the game
                    board.printBoard();
                }

                // The specified move is invalid
                if(cmd == '4')
                {
                    System.out.println("Invalid move!");

                    // Roll-back
                    board = new Board(rollbackBoard);

                    // Generate a game tree
                    GameTree tree = new GameTree(board.getBoard(), board.isPlayerIsBlack(), 4);

                    // Rock on
                    Move nextMove = tree.findBestMove(board.isPlayerIsBlack());
                    board.movePawn(nextMove);
                    System.out.println("Next move: {" + nextMove.x1 + ", " + nextMove.y1 + "} -> {" + nextMove.x2 + ", " + nextMove.y2 + "}");
                    String move = nextMove.getMoveAsString();
                    System.out.println(move);
                    output.write(move.getBytes(), 0, move.length());
                    output.flush();

                    // Send the command
                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                    // Display the game
                    board.printBoard();
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Move generator tester
    private static void testMoves()
    {
        byte[] a = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] b = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] c = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] d = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] e = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] f = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] g = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] h = { 0, 0, 0, 0, 0, 0, 0, 0 };

        byte[][] byteBoard = { a, b, c, d, e, f, g, h };

        Move[] moves = MoveGenerator.getMoves(byteBoard, 7, 7, true);
        for (int m = 0; m < moves.length; m++)
        {
            Move move = moves[m];
            byteBoard[move.x2][move.y2] = 9;

        }

        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                System.out.print(byteBoard[x][y] + " ");
            }

            System.out.println("");
        }
    }

}