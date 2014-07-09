package logic;

import game.*;

public class ActionGenerator {

	private int homePiece;
	private Board board;
	private int tempCount;

	public Action generateAction(boolean playerIsBlack) {
		if (playerIsBlack)
			homePiece = 2;
		else
			homePiece = 4;
		int[][] board = Board.getInstance().getBoard();

		for (int x = 0; x != 8; ++x) {
			for (int y = 0; y != 8; ++y) {
				if (board[x][y] == homePiece) {
					// nous devons trouver un mouvement
					//System.out.println("Piece: (" + x + ", " + y + ")");
					// 1 déplacement a est
					this.tempCount = this.firstGoodMoveBitch(board, x, y, 1);
					if (this.tempCount != 0) {
						Action newAction = new Action(x, y, x, y + this.tempCount, Board.getInstance().isPlayerIsBlack());
						if (Board.getInstance().isActionValid(newAction,1)) {
							return newAction;
						}
					
					}
					// 2 déplacement a sud-est
					this.tempCount = this.firstGoodMoveBitch(board, x, y, 2);
					if (this.tempCount != 0) {
						Action newAction = new Action(x, y, x - this.tempCount, y + this.tempCount , Board.getInstance().isPlayerIsBlack());
						if (Board.getInstance().isActionValid(newAction,2)) {
							return newAction;
						}
				
					}
					// 3 déplacement a sud
					this.tempCount = this.firstGoodMoveBitch(board, x, y, 3);
					if (this.tempCount != 0) {
						Action newAction = new Action(x, y, x - this.tempCount, y, Board.getInstance().isPlayerIsBlack());
						if (Board.getInstance().isActionValid(newAction,3)) {
							return newAction;
						}
					
					}
					// 4 déplacement a sud-ouest
					this.tempCount = this.firstGoodMoveBitch(board, x, y, 4);
					if (this.tempCount != 0) {
						Action newAction = new Action(x, y, x - this.tempCount, y - this.tempCount, Board.getInstance().isPlayerIsBlack());
						if (Board.getInstance().isActionValid(newAction,4)) {
							return newAction;
						}
					
					}
					// 5 déplacement a ouest
					this.tempCount = this.firstGoodMoveBitch(board, x, y, 5);
					if (this.tempCount != 0) {
						Action newAction = new Action(x, y, x, y - this.tempCount, Board.getInstance().isPlayerIsBlack());
						if (Board.getInstance().isActionValid(newAction,5)) {
							return newAction;
						}
					
					}
					// 6 déplacement a nord-ouest
					this.tempCount = this.firstGoodMoveBitch(board, x, y, 6);
					if (this.tempCount != 0) {
						Action newAction = new Action(x, y, x + this.tempCount, y - this.tempCount, Board.getInstance().isPlayerIsBlack());
						if (Board.getInstance().isActionValid(newAction,6)) {
							return newAction;
						}
					
					}
					// 7 déplacement a nord
					this.tempCount = this.firstGoodMoveBitch(board, x, y, 7);
					if (this.tempCount != 0) {
						Action newAction = new Action(x, y, x + this.tempCount, y, Board.getInstance().isPlayerIsBlack());
						if (Board.getInstance().isActionValid(newAction,7)) {
							return newAction;
						}
					
					}
					// 8 déplacement a nord-est
					this.tempCount = this.firstGoodMoveBitch(board, x, y, 8);
					if (this.tempCount != 0) {
						Action newAction = new Action(x, y, x + this.tempCount, y + this.tempCount, Board.getInstance().isPlayerIsBlack());
						if (Board.getInstance().isActionValid(newAction,8)) {
							return newAction;
						}
						
					}
				}
				//System.out.println("");
				//System.out.print(board[x][y] + " ");
			}
			//System.out.println("");
		}
		return null;
	}

	public int firstGoodMoveBitch(int[][] board, int x, int y, int action) {
		int count = 0;

		if (action == 1 || action == 5) {
			// est en ouest
			for (int i = 0; i <= 7; i++) {
				if (board[x][i] != 0)
					count++;
			}
		}

		if (action == 3 || action == 7) {
			// nord en sud
			for (int i = 0; i <= 7; i++) {
				if (board[i][y] != 0)
					count++;
			}
		}

		if (action == 2 || action == 6) {
			// sud-est a nord-ouest
			if (x >= y) {
				for (int i = 0; i != 7 - (x - y); i++) {
					if (board[x - y + i][i] != 0) {
						count++;
					}
				}
			} else {
				for (int i = 0; i != 7 - (y - x); i++) {
					if (board[i][y - x + i] != 0) {
						count++;
					}
				}
			}
		}

		if (action == 4 || action == 8) {
			// nord est a sud-ouest
			while((x > 0) && (y > 0))
			{
				x--;
				y--;
			}
			
			while((x <= 7) && (y <= 7))
			{
				if (board[x][y] != 0) {
					count++;
				}
				
				x++;
				y++;
			}
		}

		return count;
	}
}
