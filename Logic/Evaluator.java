package Logic;
import Game.*;
import java.util.ArrayList;

public class Evaluator
{
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
        //value += mobilityRating(playerPawns, board.getBoard(), Util.getPawnColorCode(board.isPlayerIsBlack()), board.isPlayerIsBlack());
        value -= centerOfMassRating(opponentPawns, opponentCOM);
        value -= uniformityRating(opponentPawns);
        value -= connectednessRating(opponentPawns, board.getBoard(), Util.getPawnColorCode(!board.isPlayerIsBlack()));
        //value -= mobilityRating(opponentPawns, board.getBoard(), Util.getPawnColorCode(!board.isPlayerIsBlack()), !board.isPlayerIsBlack());

        return value;
    }

    // Returns a value based on the average number of connection per pawn (positive)
    // -> The AI will try to keep its pawns connected, and disconnect opponent's pawns
    private static int connectednessRating(Coordinate[] pawns, byte[][] byteBoard, byte colorCode)
    {
        int score = 0;

        // Count the number of connected pawns
        for (int i = 0; i < pawns.length; i++)
        {
            int x = pawns[i].X;
            int y = pawns[i].Y;

            if      (x < 7 && byteBoard[x + 1][y] == colorCode) score++;
            else if (x > 0 && byteBoard[x - 1][y] == colorCode) score++;
            else if (y > 0 && byteBoard[x][y - 1] == colorCode) score++;
            else if (y < 7 && byteBoard[x][y + 1] == colorCode) score++;

            else if (x < 7 && y < 7 && byteBoard[x + 1][y + 1] == colorCode) score++;
            else if (x > 0 && y < 7 && byteBoard[x - 1][y + 1] == colorCode) score++;
            else if (x < 7 && y > 0 && byteBoard[x + 1][y - 1] == colorCode) score++;
            else if (x > 0 && y > 0 && byteBoard[x - 1][y - 1] == colorCode) score++;
        }

        // Check if the player has won
        if (score == pawns.length)
        {
            if (isWinning(pawns, byteBoard, colorCode)) return 100000;
        }

        return score * score;
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

        score *= -score;
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
        return area * -area;
    }

    // This function evaluates the mobility of the pawns
    private static int mobilityRating(Coordinate[] pawns, byte[][] byteBoard, byte pawnColor, boolean isBlack)
    {
        int score = 0;

        for (Coordinate pawn : pawns)
        {
            // If the current pawn on a border, remove 1; if it's on a corner, remove 2
            if (pawn.X == 0 || pawn.X == 7) score -= 1;
            if (pawn.Y == 0 || pawn.Y == 7) score -= 1;

            Move[] moves = MoveGenerator.getMoves(byteBoard, pawn.X, pawn.Y, isBlack);
            for (Move move : moves)
            {
                // If the pawn can capture an enemy pawn, add 4 to the score
                if (byteBoard[move.x2][move.y2] != pawnColor && byteBoard[move.x2][move.y2] != 0) score += 4;
                // Else, add 2 to the score
                else score += 2;
                // If the pawn can move to a border, remove 1; if it can move to a corner, remove 2
                if (move.x2 == 0 || move.x2 == 7) score -= 1;
                if (move.y2 == 0 || move.y2 == 7) score -= 1;
            }
        }

        return score / pawns.length; // The score is divided by the pawn length so the AI doesn't focus on capturing pawns
    }

    // Check if a player is winning.
    public static boolean isWinning(Coordinate[] pawns, byte[][] byteBoard, byte colorCode)
    {
        int connectedPawnCount = findConnectedPawns(pawns, pawns[0], byteBoard, colorCode, new ArrayList<Coordinate>());
        if (connectedPawnCount == pawns.length) return true;
        return false;
    }
    // This iterative function counts the number of connected pawns
    private static int findConnectedPawns(Coordinate[] pawns, Coordinate pawn, byte[][] byteBoard, byte colorCode, ArrayList<Coordinate> visitedPawns)
    {
        if (visitedPawns.contains(pawn)) return 0;
        int pawnCount = 1;
        visitedPawns.add(pawn);
        int x = pawn.X;
        int y = pawn.Y;

        // Check for neighbours
        if (x < 7 && byteBoard[x + 1][y] == colorCode) pawnCount += findConnectedPawns(pawns, Util.getPawnFromArray(pawns, x + 1, y), byteBoard, colorCode, visitedPawns);
        if (x > 0 && byteBoard[x - 1][y] == colorCode) pawnCount += findConnectedPawns(pawns, Util.getPawnFromArray(pawns, x - 1, y), byteBoard, colorCode, visitedPawns);
        if (y > 0 && byteBoard[x][y - 1] == colorCode) pawnCount += findConnectedPawns(pawns, Util.getPawnFromArray(pawns, x, y - 1), byteBoard, colorCode, visitedPawns);
        if (y < 7 && byteBoard[x][y + 1] == colorCode) pawnCount += findConnectedPawns(pawns, Util.getPawnFromArray(pawns, x, y + 1), byteBoard, colorCode, visitedPawns);

        if (x < 7 && y < 7 && byteBoard[x + 1][y + 1] == colorCode) pawnCount += findConnectedPawns(pawns, Util.getPawnFromArray(pawns, x + 1, y + 1), byteBoard, colorCode, visitedPawns);
        if (x > 0 && y < 7 && byteBoard[x - 1][y + 1] == colorCode) pawnCount += findConnectedPawns(pawns, Util.getPawnFromArray(pawns, x - 1, y + 1), byteBoard, colorCode, visitedPawns);
        if (x < 7 && y > 0 && byteBoard[x + 1][y - 1] == colorCode) pawnCount += findConnectedPawns(pawns, Util.getPawnFromArray(pawns, x + 1, y - 1), byteBoard, colorCode, visitedPawns);
        if (x > 0 && y > 0 && byteBoard[x - 1][y - 1] == colorCode) pawnCount += findConnectedPawns(pawns, Util.getPawnFromArray(pawns, x - 1, y - 1), byteBoard, colorCode, visitedPawns);

        return pawnCount;
    }
}
