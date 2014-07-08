package game;

public class Board {
	

	private static Board instance;
	private int[][] board;
	private boolean playerIsBlack;
	
	public final static Board getInstance(){
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
		for (int x = 0; x != 8; ++x) {
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
	
	
	
	
}
