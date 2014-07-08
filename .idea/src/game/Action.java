package game;

public class Action {
	private int x;
	private int y;
	private int x2;
	private int y2;
	private boolean playerIsBlack;
	
	public Action(int x, int y, int x2, int y2, boolean playerIsBlack) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
		this.playerIsBlack = playerIsBlack;
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
	
	
	
}
