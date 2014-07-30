package Logic;
import Game.*;
import java.util.ArrayList;

public class MoveGenerator
{
    // Returns all the valid moves for a specific pawn
    public static Move[] getMoves(byte[][] board, int x, int y, boolean isBlack)
    {
        ArrayList<Move> moves = new ArrayList<Move>();
        if (board[x][y] == 0) return null;

        // Count the amount of pawns in every direction
        int hCount = horizontalCount(board, y);
        int vCount = verticalCount(board, x);
        int adCount = ascendingDiagonalCount(board, x, y);
        int ddCount = descendingDiagonalCount(board, x, y);

        // Get the pawn color
        int playerPawn = 4;
        int enemyPawn = 2;
        if (board[x][y] == 2) { playerPawn = 2; enemyPawn = 4; }
        else if (board[x][y] == 4) { playerPawn = 4; enemyPawn = 2; }

        if (board[x][y] == 2) enemyPawn = 4;

        // Find the valid moves
        // ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯

        // Left
        if (x - hCount >= 0 && board[x - hCount][y] != playerPawn)
        {
            boolean blocked = false;
            for (int i = 0; i < hCount; i++)
            {
                if ((board[x - i][y]) == enemyPawn) blocked = true;
            }
            if (!blocked) moves.add(new Move(x, y, x - hCount, y));
        }

        // Right
        if (x + hCount <= 7 && board[x + hCount][y] != playerPawn)
        {
            boolean blocked = false;
            for (int i = 0; i < hCount; i++)
            {
                if ((board[x + i][y]) == enemyPawn) blocked = true;
            }
            if (!blocked) moves.add(new Move(x, y, x + hCount, y));
        }

        // Up
        if (y - vCount >= 0 && board[x][y - vCount] != playerPawn)
        {
            boolean blocked = false;
            for (int i = 0; i < vCount; i++)
            {
                if ((board[x][y - i]) == enemyPawn) blocked = true;
            }
            if (!blocked) moves.add(new Move(x, y, x, y - vCount));
        }

        // Down
        if (y + vCount <= 7 && board[x][y + vCount] != playerPawn)
        {
            boolean blocked = false;
            for (int i = 0; i < vCount; i++)
            {
                if ((board[x][y + i]) == enemyPawn) blocked = true;
            }
            if (!blocked) moves.add(new Move(x, y, x, y + vCount));
        }

        // Down-Left
        if (y + adCount <= 7 && x - adCount >= 0 && board[x - adCount][y + adCount] != playerPawn)
        {
            boolean blocked = false;
            for (int i = 0; i < adCount; i++)
            {
                if ((board[x - i][y + i]) == enemyPawn) blocked = true;
            }
            if (!blocked) moves.add(new Move(x, y, x - adCount, y + adCount));
        }

        // Top-Left
        if (y - ddCount >= 0 && x - ddCount >= 0 && board[x - ddCount][y - ddCount] != playerPawn)
        {
            boolean blocked = false;
            for (int i = 0; i < ddCount; i++)
            {
                if ((board[x - i][y - i]) == enemyPawn) blocked = true;
            }
            if (!blocked) moves.add(new Move(x, y, x - ddCount, y - ddCount));
        }

        // Down-Right
        if (y + ddCount <= 7 && x + ddCount <= 7 && board[x + ddCount][y + ddCount] != playerPawn)
        {
            boolean blocked = false;
            for (int i = 0; i < ddCount; i++)
            {
                if ((board[x + i][y + i]) == enemyPawn) blocked = true;
            }
            if (!blocked) moves.add(new Move(x, y, x + ddCount, y + ddCount));
        }

        // Top-Right
        if (y - adCount >= 0 && x + adCount <= 7 && board[x + adCount][y - adCount] != playerPawn)
        {
            boolean blocked = false;
            for (int i = 0; i < adCount; i++)
            {
                if ((board[x + i][y - i]) == enemyPawn) blocked = true;
            }
            if (!blocked) moves.add(new Move(x, y, x + adCount, y - adCount));
        }

        return moves.toArray(new Move[0]);
    }

    // Count the number of pawns in an horizontal line
    private static int horizontalCount(byte[][] byteBoard, int pawnY)
    {
        int pawnCount = 0;
        for (int x = 0; x < byteBoard.length; x++)
        {
            if (byteBoard[x][pawnY] != 0) pawnCount++;
        }
        return pawnCount;
    }

    // Count the number of pawns in a vertical line
    private static int verticalCount(byte[][] byteBoard, int pawnX)
    {
        int pawnCount = 0;
        for (int y = 0; y < byteBoard.length; y++)
        {
            if (byteBoard[pawnX][y] != 0) pawnCount++;
        }
        return pawnCount;
    }

    // Count the number of pawns in an ascending diagonal (/)
    private static int ascendingDiagonalCount(byte[][] byteBoard, int pawnX, int pawnY)
    {
        int pawnCount = 0;

        while((pawnX < 7) && (pawnY > 0))
        {
            pawnX++;
            pawnY--;
        }

        while((pawnX >= 0) && (pawnY <= 7))
        {
            if (byteBoard[pawnX][pawnY] != 0) pawnCount++;

            pawnX--;
            pawnY++;
        }

        return pawnCount;
    }

    // Count the number of pawns in an descending diagonal (\)
    private static int descendingDiagonalCount(byte[][] byteBoard, int pawnX, int pawnY)
    {
        int pawnCount = 0;

        while((pawnX > 0) && (pawnY > 0))
        {
            pawnX--;
            pawnY--;
        }

        while((pawnX <= 7) && (pawnY <= 7))
        {
            if (byteBoard[pawnX][pawnY] != 0) pawnCount++;

            pawnX++;
            pawnY++;
        }

        return pawnCount;
    }
}
