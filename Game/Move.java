package Game;

public class Move
{
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public Move(int x1, int y1, int x2, int y2)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public Move(String input)
    {
        // String format: 'A1B2' -> x1 y1 x2 y2
        // The x axis is goes from A to H
        // The y axis goes from 8 to 1
        input = input.replace(" ", "").replace("-", "");
        this.x1 = CharToInteger(input.charAt(0));
        this.y1 = 8 - Character.getNumericValue(input.charAt(1));
        this.x2 = CharToInteger(input.charAt(2));
        this.y2 = 8 - Character.getNumericValue(input.charAt(3));
    }

    public String getMoveAsString()
    {
        String move = "";
        move += IntegerToChar(x1);
        move += "" + ((7 - y1) + 1);
        move += IntegerToChar(x2);
        move += "" + ((7 - y2) + 1);
        return move;
    }

    private char IntegerToChar(int i)
    {
        char c = '0';

        switch (i) {
            case 0 : c = 'A'; break;
            case 1 : c = 'B'; break;
            case 2 : c = 'C'; break;
            case 3 : c = 'D'; break;
            case 4 : c = 'E'; break;
            case 5 : c = 'F'; break;
            case 6 : c = 'G'; break;
            case 7 : c = 'H'; break;
        }

        return c;
    }

    private int CharToInteger(char c)
    {
        int i = -1;

        switch (c) {
            case 'A' : i = 0; break;
            case 'B' : i = 1; break;
            case 'C' : i = 2; break;
            case 'D' : i = 3; break;
            case 'E' : i = 4; break;
            case 'F' : i = 5; break;
            case 'G' : i = 6; break;
            case 'H' : i = 7; break;
        }

        return i;
    }
}
