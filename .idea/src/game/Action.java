package game;

public class Action {
	private int x;
	private int y;
	private int x2;
	private int y2;
	private boolean playerIsBlack;
	private String action;
	
	public Action(int x, int y, int x2, int y2, boolean playerIsBlack) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		this.playerIsBlack = playerIsBlack;
	}
	
	public Action(String action, boolean isBlackPlayer) {
		this.action = action.replace(" ", "").replace("-", "");
		this.y = CharToInteger(this.action.charAt(0));
		this.x = this.action.charAt(1) - 49;
		this.y2 = CharToInteger(this.action.charAt(2));
		this.x2 = this.action.charAt(3) - 49;
		this.playerIsBlack = isBlackPlayer;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public boolean isPlayerIsBlack() {
		return playerIsBlack;
	}

	public void setPlayerIsBlack(boolean playerIsBlack) {
		this.playerIsBlack = playerIsBlack;
	}
	
	public String getMoveAsString(Action action)
	{
		/*char test;
		test = IntegerToChar(action.getX());
		return String.valueOf(test) + action.getY() +  " - " + String.valueOf(this.IntegerToChar(action.getX2())) + action.getY2();*/
		
		return String.valueOf(IntegerToChar(action.getY())) + (action.getX()+1) +  " - " + String.valueOf(this.IntegerToChar(action.getY2())) + (action.getX2()+1);
	}
	
	private char IntegerToChar(int i) {
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

	private int CharToInteger(char c) {
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
