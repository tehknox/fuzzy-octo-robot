package game;

public class Board {
	

	private static Board instance;
	private int[][] board;
	private boolean playerIsBlack;
	
	public static Board getInstance(){
		if(instance == null){
			instance = new Board();
		}
		return instance;
	}
	
	private Board(){
		board = new int[8][8];
	}
	
	//todo: delete
	public void printBoard() {
		for (int x = 7; x != -1; --x) {
			for (int y = 0; y != 8; ++y) {
				System.out.print(board[x][y] + " ");
			}
			System.out.println("");
		}
	}
	
	public void setCase(int x, int y, int type){
		board[x][y] = type;
	}

	public boolean isPlayerIsBlack() {
		return playerIsBlack;
	}

	public void setPlayerIsBlack(boolean playerIsBlack) {
		this.playerIsBlack = playerIsBlack;
	}

	public int[][] getBoard() {
		return board;
	}
	
	public void updateBoard(Action action)
	{
		/*if (this.board[action.getX2()][action.getY2()] != 0) {
			// Capture une pièce
		} else {
			// Ne capture pas de pièce
		}*/	
		
		this.board[action.getX2()][action.getY2()] = this.board[action.getX()][action.getY()];
		this.board[action.getX()][action.getY()] = 0;
	}
	
	public boolean isActionValid(Action action,int type)
	{
		int homePiece = -1;
		int ennemyPiece = -1;
		if (action.isPlayerIsBlack()) {
			homePiece = 2;
			ennemyPiece = 4;
		}
		else {
			homePiece = 4;
			ennemyPiece = 2;
		}
		if((action.getX2() < 0) || (action.getX2() > 7) || (action.getY2() < 0) || (action.getY2() > 7)){
			return false;
		}
		System.out.println("Nouveau move");
		System.out.println(action.getX() + "" + action.getY());
		System.out.println(action.getX2() + "" + action.getY2());

		int homePieceCount = 0;
		if (type == 1) {
			// vers l'est
			for (int i = action.getY(); i <= action.getY2()-1; i++) {
				if(board[action.getX()][i] == ennemyPiece || (i < 0 ) || (i > 7)){
					return false;
				}
				else{
					if(board[action.getX()][i] == homePiece){
						homePieceCount++;
					}
				}					
			}
		}
		if (type == 5) {
			homePieceCount=0;
			// vers l'ouest
			for (int i = action.getY(); i >= action.getY2()+1; i--) {
				if(board[action.getX()][i] == ennemyPiece || (i < 0 ) || (i > 7)){
					return false;
				}
				else{
					if(board[action.getX()][i] == homePiece){
						homePieceCount++;
					}
				}					
			}
		}

		if (type == 3) {
			homePieceCount=0;
			// vers sud
			for (int i = action.getX(); i >= action.getX2()+1; i--) {
				if(board[i][action.getY()] == ennemyPiece || (i < 0 ) || (i > 7)){
					return false;
				}
				else{
					if(board[i][action.getY()] == homePiece){
						homePieceCount++;
					}
				}					
			}
		}
		if (type == 7) {
			homePieceCount=0;
			// vers nord
			for (int i = action.getX(); i <= action.getX2()-1; i++) {
				if(board[i][action.getY()] == ennemyPiece || (i < 0 ) || (i > 7)){
					return false;
				}
				else{
					if(board[i][action.getY()] == homePiece){
						homePieceCount++;
					}
				}					
			}
		}

		if (type == 2 || type == 6) {
			return false;
			// sud-est a nord-ouest
			/*if (x >= y) {
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
			}*/
		}

		if (type == 4 || type == 8) {
			// nord est a sud-ouest
			return false;
			/*while((x > 0) && (y > 0))
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
			}*/
		}
		
		
		if ((homePieceCount < 2) && (this.board[action.getX2()][action.getY2()] != homePiece)) {
			return true;
		}
		else {
			return false;
		}

	}
}
