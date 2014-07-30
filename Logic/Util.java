package Logic;

public class Util
{
    // Clones a jagged byte array
    public static byte[][] cloneJaggedArray(byte[][] input)
    {
        byte[][] output = new byte[8][8];
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                output[x][y] = input[x][y];
            }
        }
        return output;
    }

    // Returns the numeric value of a pawn color
    public static byte getPawnColorCode(boolean black)
    {
        byte pawnColor = 4; if (black) pawnColor = 2;
        return pawnColor;
    }
}
