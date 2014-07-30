package Logic;
import Game.*;
import java.util.ArrayList;

public class Tree
{
    Node root;
    Board currentBoard;
    float timeOut;
    int bestValue = 0;

    // Tree constructor
    public Tree(Board currentBoard, int maxDepth, float timeOut)
    {
        this.currentBoard = currentBoard;
        this.timeOut = timeOut;

        int alpha = -99999999; // - Infinity
        int beta  =  99999999; // + Infinity

        root = new Node();
        root.value = alpha;
        root.move = null;
        root.children = new ArrayList<Node>();

        bestValue = buildTree(root, alpha, beta, true, currentBoard, 0, maxDepth);
    }

    // This function builds the tree, and finds the best move using the Minimax/Alpha-beta algorithm
    private boolean interrupt = false;
    private int buildTree(Node n, int alpha, int beta, boolean maximizing, Board board, int depth, int maxDepth)
    {
        // If the function is interrupted
        if (interrupt) return 0;
        // If the node is a leaf
        if (depth == maxDepth)
        {
            n.value = Evaluator.evaluate(board);
            return n.value;
        }
        // If the node is a maximizer
        if (maximizing)
        {
            boolean player = board.isPlayerIsBlack();
            Coordinate[] pawns = board.getPawns(player);
            loop:
            for (Coordinate pawn : pawns)
            {
                Move[] moves = MoveGenerator.getMoves(board.getBoard(), pawn.X, pawn.Y, player);
                for (Move move : moves)
                {
                    Node child = new Node(); // Create a new child node
                    child.move = move;       // Assign the move to the node
                    Board newBoard = new Board(board);  // Clone the board
                    newBoard.movePawn(move);            // Move the pawn on the new board
                    alpha = Math.max(alpha, buildTree(child, alpha, beta, !maximizing, newBoard, depth + 1, maxDepth)); // Re-iterate and get the alpha value
                    child.value = alpha;
                    n.children.add(child);
                    if (beta <= alpha) break loop; // Beta cut-off
                }
            }
            return alpha;
        }
        // If the node is a minimizer
        else
        {
            boolean player = !board.isPlayerIsBlack();
            Coordinate[] pawns = board.getPawns(player);
            loop:
            for (Coordinate pawn : pawns)
            {
                Move[] moves = MoveGenerator.getMoves(board.getBoard(), pawn.X, pawn.Y, player);
                for (Move move : moves)
                {
                    Node child = new Node(); // Create a new child node
                    child.move = move;       // Assign the move to the node
                    Board newBoard = new Board(board);  // Clone the board
                    newBoard.movePawn(move);            // Move the pawn on the new board
                    beta = Math.min(beta, buildTree(child, alpha, beta, !maximizing, newBoard, depth + 1, maxDepth)); // Re-iterate and get the beta value
                    child.value = beta;
                    n.children.add(child);
                    if (beta <= alpha) break loop;  // Alpha cut-off
                }
            }
            return beta;
        }
    }

    // This function returns the best move found by the tree
    public Move getBestMove()
    {
        Move bestMove = null;

        // Search for the root's child that has the same value than the best tree value
        for (int x = 0; x < root.children.size(); x++)
        {
            if (root.children.get(x).value == bestValue)
            {
                bestMove = root.children.get(x).move;
                break;
            }
        }

        // If there's an error, pick a random move
        if (bestMove == null)
        {
            System.out.println("An error has occurred. A random move has been selected.");
            for (int x = 0; x < root.children.size(); x++)
            {
                System.out.print(root.children.get(x).value + "; ");
            }
            System.out.println(" ");

            int randomChildIndex = (int)Math.floor(Math.random() * (double)root.children.size());
            bestMove = root.children.get(randomChildIndex).move;
        }

        return  bestMove;
    }
}
