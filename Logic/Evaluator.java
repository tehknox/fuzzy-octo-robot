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
        value += boardRating(playerPawns, board.getBoard(), board.isPlayerIsBlack(), playerCOM);
        value -= boardRating(opponentPawns, board.getBoard(), !board.isPlayerIsBlack(), opponentCOM);

        return value;
    }

    // This function evaluates the board
    // Optimization of the functions below
    private static int boardRating(Coordinate[] pawns, byte[][] byteBoard, boolean isBlack, Coordinate centerOfMass)
    {
        int value = 0;
        byte colorCode = Util.getPawnColorCode(isBlack);

        int connectednessRating = 0;
        int centerOfMassRating = 0;
        int uniformityRating = 0;
        int connectedPawns = 0;

        int minX = 8;
        int minY = 8;
        int maxX = -1;
        int maxY = -1;
        // Count the number of connected pawns
        for (int i = 0; i < pawns.length; i++)
        {
            // Store the coordinates
            int x = pawns[i].X;
            int y = pawns[i].Y;

            // Count the connected pawns
            if      (x < 7 && byteBoard[x + 1][y] == colorCode) connectedPawns++;
            else if (x > 0 && byteBoard[x - 1][y] == colorCode) connectedPawns++;
            else if (y > 0 && byteBoard[x][y - 1] == colorCode) connectedPawns++;
            else if (y < 7 && byteBoard[x][y + 1] == colorCode) connectedPawns++;
            else if (x < 7 && y < 7 && byteBoard[x + 1][y + 1] == colorCode) connectedPawns++;
            else if (x > 0 && y < 7 && byteBoard[x - 1][y + 1] == colorCode) connectedPawns++;
            else if (x < 7 && y > 0 && byteBoard[x + 1][y - 1] == colorCode) connectedPawns++;
            else if (x > 0 && y > 0 && byteBoard[x - 1][y - 1] == colorCode) connectedPawns++;

            // Check if the pawn is on a border or in a corner
            if (x == 0 || x == 7) connectednessRating -= 2;
            if (y == 0 || y == 7) connectednessRating -= 2;

            // Count the average number of connection per pawn
            if (x < 7 && byteBoard[x + 1][y] == colorCode) connectednessRating++;
            if (x > 0 && byteBoard[x - 1][y] == colorCode) connectednessRating++;
            if (y > 0 && byteBoard[x][y - 1] == colorCode) connectednessRating++;
            if (y < 7 && byteBoard[x][y + 1] == colorCode) connectednessRating++;
            if (x < 7 && y < 7 && byteBoard[x + 1][y + 1] == colorCode) connectednessRating++;
            if (x > 0 && y < 7 && byteBoard[x - 1][y + 1] == colorCode) connectednessRating++;
            if (x < 7 && y > 0 && byteBoard[x + 1][y - 1] == colorCode) connectednessRating++;
            if (x > 0 && y > 0 && byteBoard[x - 1][y - 1] == colorCode) connectednessRating++;

            // Calculate the average distance from the center of mass
            int distanceX = Math.abs(x - centerOfMass.X);
            int distanceY = Math.abs(y - centerOfMass.Y);
            int distanceD = (int)Math.floor(Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2)));
            int distance = Math.max(Math.max(distanceX, distanceY), distanceD);
            centerOfMassRating += (int)Math.pow(distance, 4);

            // Calculate the rectangle
            if      (x > maxX) maxX = x;
            else if (x < minX) minX = x;
            if      (y > maxY) maxY = y;
            else if (y < minY) minY = y;
        }

        // Check the winning condition
        if (connectedPawns == pawns.length)
        {
            if (isWinning(pawns, byteBoard, colorCode)) return 100000;
        }

        // Average the number of connection per pawn
        connectednessRating /= pawns.length;
        connectednessRating *= 4; // Weight

        // Average the COM rating
        centerOfMassRating /= pawns.length;
        centerOfMassRating *= -1; // Weight

        // Calculate the uniformity rating
        uniformityRating = (maxX - minX) * (maxY - minY);
        uniformityRating *= -2; // Weight

        // Calculate the value of the board
        value += connectednessRating;
        value += centerOfMassRating;
        value += uniformityRating;
        return value;
    }

    // Returns a value based on the average number of connection per pawn (positive)
    // -> The AI will try to keep its pawns connected, and disconnect opponent's pawns
    private static int connectednessRating(Coordinate[] pawns, byte[][] byteBoard, byte colorCode)
    {
        int score = 0;
        int connectedPawns = 0;

        // Count the number of connected pawns
        for (int i = 0; i < pawns.length; i++)
        {
            int x = pawns[i].X;
            int y = pawns[i].Y;

            if      (x < 7 && byteBoard[x + 1][y] == colorCode) connectedPawns++;
            else if (x > 0 && byteBoard[x - 1][y] == colorCode) connectedPawns++;
            else if (y > 0 && byteBoard[x][y - 1] == colorCode) connectedPawns++;
            else if (y < 7 && byteBoard[x][y + 1] == colorCode) connectedPawns++;

            else if (x < 7 && y < 7 && byteBoard[x + 1][y + 1] == colorCode) connectedPawns++;
            else if (x > 0 && y < 7 && byteBoard[x - 1][y + 1] == colorCode) connectedPawns++;
            else if (x < 7 && y > 0 && byteBoard[x + 1][y - 1] == colorCode) connectedPawns++;
            else if (x > 0 && y > 0 && byteBoard[x - 1][y - 1] == colorCode) connectedPawns++;

            if (x == 0 || x == 7) score -= 2;
            if (y == 0 || y == 7) score -= 2;

            if (x < 7 && byteBoard[x + 1][y] == colorCode) score++;
            if (x > 0 && byteBoard[x - 1][y] == colorCode) score++;
            if (y > 0 && byteBoard[x][y - 1] == colorCode) score++;
            if (y < 7 && byteBoard[x][y + 1] == colorCode) score++;

            if (x < 7 && y < 7 && byteBoard[x + 1][y + 1] == colorCode) score++;
            if (x > 0 && y < 7 && byteBoard[x - 1][y + 1] == colorCode) score++;
            if (x < 7 && y > 0 && byteBoard[x + 1][y - 1] == colorCode) score++;
            if (x > 0 && y > 0 && byteBoard[x - 1][y - 1] == colorCode) score++;
        }

        // Check if the player has won
        if (connectedPawns == pawns.length)
        {
            if (isWinning(pawns, byteBoard, colorCode)) return 100000;
        }

        score /= pawns.length;// System.out.println("CON = " + score * 4);
        return score * 4;
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
            score += (int)Math.pow(distance, 4); // The distance is cubed to force the AI to move the farthest pawns
        }

        score *= -1; //System.out.println("COM = " + score/pawns.length);
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
        int area = (maxX - minX) * (maxY - minY);// System.out.println("UNI = " + area * -2);
        return area * -2;
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
