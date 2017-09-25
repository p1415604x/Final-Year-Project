import java.util.Random;

public class Players {

	private int nturns;
	private String hand;

	public Players() {

	}


	public Players(int nturns) {
		this.nturns=nturns;
	}

	private static Random rand = new Random();

	public void playgame() {
		int val = rand.nextInt(3)+1;
		  if (val == 1) {
			hand = "ROCK";
		} else if (val == 2) {
			hand = "PAPER";
		} else {
			hand = "SCISSORS"; }
		}

	public int getTurns() {
		return this.nturns;
	}

	public String getScore() {
		return hand;
	}

	}


