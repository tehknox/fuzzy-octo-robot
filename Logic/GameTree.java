package Logic;
import Game.*;

public class GameTree
{
    public Node root;

    // Generate a game tree
    public GameTree(byte[][] board, boolean isPlayerBlack, int maxDepth)
    {
        root = new Node();
        root.move = null;
        root.value = 0;
        root.generateTree(board, isPlayerBlack, isPlayerBlack, 0, maxDepth);
    }

    // Find the best move
    public Move findBestMove(boolean isPlayerBlack)
    {
        int bestValue = 0;
        Move bestMove = null;
        for (int x = 0; x < root.children.size(); x++)
        {
            int childValue = minimaxAlphaBeta(root.children.get(x), -999999, 999999, !isPlayerBlack, isPlayerBlack);
            if (childValue > bestValue)
            {
                bestValue = childValue;
                bestMove = root.children.get(x).move;
            }
        }

        return bestMove;
    }

    // Minimax algorithm ~ NO ALPHA/BETA!!!
    private int minimax(Node n, boolean isPlayerBlack, boolean isTurnBlack)
    {
        if (n.isLeaf()) return n.value;

        // If it's the player turn, maximize
        if (isPlayerBlack == isTurnBlack)
        {
            int bestValue = -99999;
            for (int x = 0; x < n.children.size(); x++)
            {
                int childValue = minimax(n.children.get(x), isPlayerBlack, !isTurnBlack);
                bestValue = Math.max(bestValue, childValue);
            }
            return bestValue;
        }
        // If if the opponent turn, minimize
        else
        {
            int bestValue = 99999;
            for (int x = 0; x < n.children.size(); x++)
            {
                int childValue = minimax(n.children.get(x), isPlayerBlack, !isTurnBlack);
                bestValue = Math.min(bestValue, childValue);
            }
            return bestValue;
        }
    }

    // Minimax with Alpha-beta pruning
    private int minimaxAlphaBeta(Node n, int alpha, int beta, boolean isPlayerBlack, boolean isTurnBlack)
    {
        if (n.isLeaf()) return n.value;

        // If it's the player turn, maximize
        if (isPlayerBlack == isTurnBlack)
        {
            for (int x = 0; x < n.children.size(); x++)
            {
                int childValue = minimaxAlphaBeta(n.children.get(x), alpha, beta, isPlayerBlack, !isTurnBlack);
                alpha = Math.max(alpha, childValue);
                if (beta <= alpha) break;
            }
            return alpha;
        }
        // If if the opponent turn, minimize
        else
        {
            for (int x = 0; x < n.children.size(); x++)
            {
                int childValue = minimaxAlphaBeta(n.children.get(x), alpha, beta, isPlayerBlack, !isTurnBlack);
                beta = Math.min(beta, childValue);
                if (beta <= alpha) break;
            }
            return beta;
        }
    }
}
