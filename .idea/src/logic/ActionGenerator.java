package logic;
import game.*;

public class ActionGenerator {
	
	private int homePiece;
	private Board board;
	
	public Action generateAction(boolean playerIsBlack){
		if(playerIsBlack) homePiece = 4; else homePiece = 2;
		int[][] board = Board.getInstance().getBoard();
		
		for (int x = 0; x != 8; ++x) {
			for (int y = 0; y != 8; ++y) {
				if(board[x][y] == homePiece){
					//nous devons trouver un mouvement
					//1 déplacement a est
					//2 déplacement a sud-est
					//3 déplacement a sud
					//4 déplacement a sud-ouest
					//5 déplacement a ouest
					//6 déplacement a nord-ouest
					//7 déplacement a nord
					//8 déplacement a nord-est
					
					
				}
				
				System.out.print(board[x][y] + " ");
			}
			System.out.println("");
		}
		return null;	
	}
	
	public int amplitudeDeMouvement(int[][] board,int x,int y, int action){
		int count = 0;
		
		if(action == 1 || action == 5){
			//est en ouest
			for(int i = 0;i<=8;i++){
				if(board[i][y] != 0) count++;
			}
		}
		if(action == 3 || action == 7){
			//nord en sud
			for(int i = 0;i<=8;i++){
				if(board[x][i] != 0) count++;
			}
		}
		if(action == 4 || action == 6){
			//sud-est a nord-ouest
			for(int i = 0;i<=8;i++){
				for(int ii = 0;i<=8;i++){
					if(board[i][ii] != 0) count++;
				}
			}
		}
		if(action == 4 || action == 8){
			//nord est a sud-ouest
			for(int i = 8;i>=0;i--){
				for(int ii = 0;i<=8;i++){
					if(board[i][ii] != 0) count++;
				}
			}
		}
		
			
		return 0;
	}
}
