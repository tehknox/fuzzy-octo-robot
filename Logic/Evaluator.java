package Logic;
import Game.*;
import java.util.ArrayList;

public class Evaluator
{
    // PAS OPTIMISÉ FUCKALL!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static int evaluate(Board board)
    {
        int value = 0;

        // Get all the pawns for the current player
        Coordinate[] playerPawns = board.getPawns(board.isPlayerIsBlack());
        Coordinate[] opponentPawns = board.getPawns(!board.isPlayerIsBlack());

        // Get the center of mass (COM) of both players
        Coordinate playerCOM = getCenterOfMass(playerPawns);
        Coordinate opponentCOM = getCenterOfMass(opponentPawns);

        // Calculate the total score of the board
        value += centerOfMassRating(playerPawns, playerCOM);
        value += uniformityRating(playerPawns);
        value += connectednessRating(playerPawns, board.getBoard(), Util.getPawnColorCode(board.isPlayerIsBlack()));
        value -= centerOfMassRating(opponentPawns, opponentCOM);
        value -= uniformityRating(opponentPawns);
        value -= connectednessRating(opponentPawns, board.getBoard(), Util.getPawnColorCode(!board.isPlayerIsBlack()));

        return value;
    }

    // Returns a value based on the average number of connection per pawn (positive)
    // -> The AI will try to keep its pawns connected
    private static int connectednessRating(Coordinate[] pawns, byte[][] byteBoard, byte colorCode)
    {
        int score = 0;

        // Count the number of connected pawns
        for (int i = 0; i < pawns.length; i++)
        {
            int x = pawns[i].X;
            int y = pawns[i].Y;

            if      (x < 7 && byteBoard[x + 1][y] == colorCode) score++;
            if      (x > 0 && byteBoard[x - 1][y] == colorCode) score++;
            if      (y > 0 && byteBoard[x][y - 1] == colorCode) score++;
            if      (y < 7 && byteBoard[x][y + 1] == colorCode) score++;

            if      (x < 7 && y < 7 && byteBoard[x + 1][y + 1] == colorCode) score++;
            if      (x > 0 && y < 7 && byteBoard[x - 1][y + 1] == colorCode) score++;
            if      (x < 7 && y > 0 && byteBoard[x + 1][y - 1] == colorCode) score++;
            if      (x > 0 && y > 0 && byteBoard[x - 1][y - 1] == colorCode) score++;
        }

        score /= pawns.length;

        return score;
    }

    // Get the center of mass
    private static Coordinate getCenterOfMass(Coordinate[] pawns)
    {
        int avgX = 0;
        int avgY = 0;
        for (int i = 0; i < pawns.length; i++)
        {
            avgX += pawns[i].X;
            avgY += pawns[i].Y;
        }
        avgX = Math.round((float)avgX/pawns.length);
        avgY = Math.round((float)avgY/pawns.length);
        return new Coordinate(avgX, avgY);
    }

    // Returns a score based on the average distance from the center of mass (negative)
    // -> The AI will try to bring its pawns close to the center of mass
    private static int centerOfMassRating(Coordinate[] pawns, Coordinate centerOfMass)
    {
        int score = 0;

        // Calculate the distance of each pawn from the center of mass
        for (int i = 0; i < pawns.length; i++)
        {
            int distanceX = Math.abs(pawns[i].X - centerOfMass.X);
            int distanceY = Math.abs(pawns[i].Y - centerOfMass.Y);
            int distanceD = (int)Math.floor(Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2)));

            int distance = Math.max(Math.max(distanceX, distanceY), distanceD);
            score += (int)Math.pow(distance, 2); // The distance is squared so the AI focuses on the farthest pawns
        }

        score *= -1;
        return score / pawns.length; // The score is divided by the amount of pawn so the pawn count doesn't affect the evaluation result.
    }

    // This function bounds all the pawns into a rectangle, and generates a score based on the area of the rectangle. (negative)
    // -> The AI will avoid to keep single pawns away from the others
    private static int uniformityRating(Coordinate[] pawns)
    {
        int minX = 8;
        int minY = 8;
        int maxX = -1;
        int maxY = -1;
        for (int i = 0; i < pawns.length; i++)
        {
            int X = pawns[i].X;
            int Y = pawns[i].Y;

            if      (X > maxX) maxX = X;
            else if (X < minX) minX = X;
            if      (Y > maxY) maxY = Y;
            else if (Y < minY) minY = Y;
        }
        int area = (maxX - minX) * (maxY - minY);
        return area * -1;
    }
}
