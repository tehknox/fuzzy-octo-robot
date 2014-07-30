package Game;

import Logic.Util;
import java.util.ArrayList;

public class Board
{
    private byte[][] board;
    private boolean playerIsBlack;

    // Constructor
    public Board()
    {
        board = new byte[8][8];
    }

    // Copy a board
    public Board(Board input)
    {
        playerIsBlack = input.isPlayerIsBlack();
        board = Util.cloneJaggedArray(input.getBoard());
    }

    // Copy a byte board
    public Board(byte[][] inputBoard, boolean isPlayerBlack)
    {
        playerIsBlack = isPlayerBlack;
        board = Util.cloneJaggedArray(inputBoard);
    }

    // Print the board in the console (Debug feature)
    public void printBoard()
    {
        for (int y = 0; y < 8; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                System.out.print(board[x][y] + " ");
            }

            System.out.println("");
        }

        System.out.println(" ");
    }

    // Move a pawn
    public void movePawn(Move move)
    {
        board[move.x2][move.y2] = board[move.x1][move.y1];
        board[move.x1][move.y1] = 0;
    }

    // Generate the board content, from string to int[][]
    public void generateBoard(String input)
    {
        // Remove the space characters
        input = input.replace(" ", "");

        // Convert the string into int[][]
        int x=0, y=0;
        for (int i = 0; i < input.length(); i++)
        {
            board[x][y] = Byte.parseByte("" + input.charAt(i));

            x++;
            if (x == 8) { x = 0; y++; } if (y == 8) { break; }
        }
    }

    // Returns the coordinate of every pawn owned by a played
    public Coordinate[] getPawns(boolean isPlayerBlack)
    {
        byte colorCode = Util.getPawnColorCode(isPlayerBlack);
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (int x = 0; x < 8; x++) {
        for (int y = 0; y < 8; y++) {
        if (board[x][y] == colorCode)
        {
            coordinates.add(new Coordinate(x,y));
        }}}
        return coordinates.toArray(new Coordinate[0]);
    }

    // Get the player color
    public boolean isPlayerIsBlack() {
        return playerIsBlack;
    }

    // Set the player color
    public void setPlayerIsBlack(boolean playerIsBlack)
    {
        this.playerIsBlack = playerIsBlack;
    }

    // Get the board in numeric format
    public byte[][] getBoard()
    {
        return board;
    }
}
