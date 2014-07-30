import Game.*;
import Logic.MoveGenerator;
import Logic.*;

import java.io.*;
import java.net.Socket;

public class Client
{
    public static void main(String[] args)
    {
        //if (1 == 1) return;

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
                    Tree tree = new Tree(board, 4, 11f);

                    // Rock on
                    Move nextMove = tree.getBestMove();
                    board.movePawn(nextMove);
                    String move = nextMove.getMoveAsString();
                    output.write(move.getBytes(), 0, move.length());
                    output.flush();

                    // Display the game
                    //board.printBoard();
                    System.out.println("Current score: " + Evaluator.evaluate(board));

                    // Run the garbage collector
                    tree = null;
                    System.gc();
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
                    Tree tree = new Tree(board, 4, 11f);

                    // Rock on
                    Move nextMove = tree.getBestMove();
                    board.movePawn(nextMove);
                    String move = nextMove.getMoveAsString();
                    output.write(move.getBytes(), 0, move.length());
                    output.flush();

                    // Display the game
                    //board.printBoard();
                    System.out.println("Current score: " + Evaluator.evaluate(board));

                    // Run the garbage collector
                    tree = null;
                    System.gc();
                }

                // The specified move is invalid
                if(cmd == '4')
                {
                    System.out.println("Invalid move!");

                    // Roll-back
                    board = new Board(rollbackBoard);

                    // Generate a game tree
                    Tree tree = new Tree(board, 4, 11f);

                    // Rock on
                    Move nextMove = tree.getBestMove();
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
                    //board.printBoard();

                    // Run the garbage collector
                    tree = null;
                    System.gc();
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // Testing function
    private static void test()
    {
        byte p = Util.getPawnColorCode(false);
        byte q = Util.getPawnColorCode(true);

        byte[] a = { 0, 0, 0, 0, 0, 0, q, q };
        byte[] b = { 0, p, 0, 0, 0, 0, 0, q };
        byte[] c = { p, 0, 0, 0, 0, 0, 0, q };
        byte[] d = { p, 0, 0, p, p, 0, 0, q };
        byte[] e = { p, 0, 0, p, p, 0, 0, q };
        byte[] f = { 0, p, 0, 0, q, 0, 0, q };
        byte[] g = { 0, 0, p, 0, 0, q, q, 0 };
        byte[] h = { 0, 0, 0, 0, 0, 0, q, q };

        byte[][] byteBoard = { a, b, c, d, e, f, g, h };
        Board board = new Board(byteBoard, false);

        board.printBoard();
        int value = Evaluator.evaluate(board);

        System.out.println(value);
    }

}