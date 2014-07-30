package Logic;
import Game.*;
import java.util.ArrayList;

public class Node
{
    public int value;
    public Move move;
    public ArrayList<Node> children = new ArrayList<Node>();
}
