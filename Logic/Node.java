package Logic;
import Game.*;
import java.util.ArrayList;

public class Node
{
    public int value;
    public Move move;
    public ArrayList<Node> children;
    public Node parent;

    public void generateTree(byte[][] board, boolean isTurnBlack, boolean playerIsBlack, int depth, int maxDepth)
    {
        // Get the children
        if (depth < maxDepth)
        {
            // Get the pawn color code
            byte pawnColor = 4; if (isTurnBlack) pawnColor = 2;
            children = new ArrayList<Node>();

            // Find the pawns with the current color code on the board
            for (int x = 0; x < 8; x++)
            {
                for (int y = 0; y < 8; y++)
                {
                    if (board[x][y] == pawnColor)
                    {
                        // Get the moves available for the pawn
                        Move[] childrenMove = MoveGenerator.getMoves(board, x, y, isTurnBlack);
                        if (childrenMove != null)
                        {
                            for (int n = 0; n < childrenMove.length; n++)
                            {
                                // Create a new child node
                                Node newChild = new Node();
                                newChild.value = (int)Math.ceil(Math.random() * 100); // RANDOM EVALUATION FUNCTION
                                newChild.move = childrenMove[n];
                                newChild.parent = this;
                                children.add(newChild);

                                // Create a new board
                                byte[][] newBoard = new byte[8][8];
                                System.arraycopy(board, 0, newBoard, 0, 8);

                                // Recursive call
                                newChild.generateTree(newBoard, !isTurnBlack, playerIsBlack, depth + 1, maxDepth);
                            }
                        }
                    }
                }
            }
        }
    }



    public boolean isLeaf()
    {
        if (children == null) return true;
        else
        {
            if (children.size() == 0) return true;
            else return false;
        }
    }
}
