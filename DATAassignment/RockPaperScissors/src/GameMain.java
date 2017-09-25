import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*Created by: P1415604x
 * Laurynas Mykolaitis
 */


/*-------------------------------------------------------------------------------------
 * Creates an inner class (Could achieve same results by dividing program into separate classes and joining them up later.
 *  Separating program would make it easier to re-use code)
 *  This class is responsible for communication between threads. It has 4 methods:
 *  1. Allows thread to pass only one value at a time instead of putting and overwriting value, which is needed by other thread
 *  2. Allows to take the value
 *  3. Allows to pass the result back to thread without overwriting
 *  4. Allows to take the results
 */
class Chat<E>	//Class called Chat<E> of type Element
{
    private E state;					//Variable of type element, it can take any type of value
    private boolean isSet = false;		//isSet used as a traffic light for values
    private boolean isSetSc = false;	//isSetSc used as a traffic light for results
    private String playerName;			//Used to hold player names
    private String result;				//Used to hold player results

    /*
     * Takes 2 parameters, name of player and his hand(rock/paper/scissors)
     * Until it hold the value - wait
     * Otherwise, notify that it hold a value so no one else could overwrite
     * set variable t as a player`s name
     * set variable state as a player`s hand
     * and notify all threads if there are any waiting/sleeping
     */
    public synchronized void putMVar(String pn, E s) {
        while (isSet) {
            try {
                wait();
            } catch (InterruptedException e) {
            	System.out.println("putMVar error: Interrupted");
            }
        }
        isSet = true;
        playerName=pn;
        state = s;
        notifyAll();
    }

    /*
     *While there is no value held - wait
     *else notify that it doesn`t hold any values
     *return the value
     */
    public synchronized E takeMVar() {
        while (!isSet) {
            try {
                wait();
            } catch (InterruptedException e) {
            	System.out.println("putMVar error: Interrupted");
            }
        }
        isSet = false;
        notifyAll();
        return state;
    }

    /*
     * Takes 2 parameters, name of player and his result (Win/Lose/Draw)
     * Until it hold the value - wait
     * Otherwise, notify that it hold a value so no one else could overwrite
     * set variable t as a player`s name
     * set variable rez as a player`s result
     * and notify all threads if there are any waiting/sleeping
     */
    public synchronized void putScore(String pn, String hand) {
        while (isSetSc) {
            try {
                wait();
            } catch (InterruptedException e) {
            	System.out.println("putMVar error: Interrupted");
            }
        }
        isSetSc = true;
        playerName=pn;
        this.result = hand;
        notifyAll();
    }

    /*
     *While there is no result held - wait
     *else notify that it doesn`t hold any values
     *return the result
     */
    public synchronized String takeScore() {
        while (!isSetSc) {
            try {
                wait();
            } catch (InterruptedException e) {
            	System.out.println("putMVar error: Interrupted");
            }
        }
        isSetSc = false;
        notifyAll();
        return result;
    }

    //Returns the name of the current player
    public synchronized String getPlayer() {
    	return playerName;
    }
}



/*-------------------------------------------------------------------------------------
 * Thread player instructions:
 *
 * Randomises the number 0-2 (inclusive)
 * and sets variable hand with value (ROCK/PAPER/SCISSORS) - it does that for every player's turn
 * after that it creates an object and gives it a name and a hand
 * Sends an object through "Chat" communication channel to another thread
 *
 * If 2 players have already been sent to referee thread it records results of their game
 *
 * After all players hands exhausted it prints out the score of all of them
 */
class PrepparingHand implements Runnable {
	  private final int nturns;
	  private final int nplayers;
	  private Chat<String> chat;
	  private int playerNameIndex;			//Incrementing variable to set player names (Player 1, Player 2 ,..)
	  private int readyPlayerIndex=-2;		//Initialised with value -2 so it could be incremented by 2 and hold the first out of two players position in array
	  private String scoreHolder="";		//Holds score taken from referee thread
	  private String playername="";			//Holds created players' names
	  private List<Scoreboard> players = new ArrayList<Scoreboard>();	//Array to hold all players


	  /*
	   * Constructor which allows to pass 3 values.
	   */
	  PrepparingHand(int nturns, Chat<String> chat, int nplayers) {
	    this.nturns = nturns;
	    this.chat=chat;
	    this.nplayers= nplayers;
	    System.out.println("Creating [Thread-Player]");
	  }

	  @Override
	  public void run() {
		  Random rand = new Random();
			System.out.println("Running [Thread-Player]");
			int i=0;
			try {
				while(i != nturns) {							//While all turns are not exhausted
					for (int k = 0; k < nplayers; k++) {		//If turns are not exhausted, go through all players
						String hand="";							//Hold value for player hand
						int val = rand.nextInt(3)+1;			//Randomises a value
						if (val == 1) {
							hand = "ROCK";
						} else if (val == 2) {
							hand = "PAPER";
						} else {
							hand = "SCISSORS"; }
						if(i == 0) {							//If it is a first turn for all players
							playerNameIndex+=1;									//Creates player number 0,1,2,3,4,5,6....
						playername="Player " + playerNameIndex;				//Creates name Player 1, Player 2.....
						Scoreboard p = new Scoreboard(playername, hand);	//Creates an object "p" to hold Player information
						players.add(p);										//Adds the object to player array
						chat.putMVar(p.getPlayerName(), hand);					//Sends player's value to referee
						Thread.sleep(50);									//Waits for fixed amount of time
						} else {		//If it is not the first turn for all players - DO NOT create new objects
							chat.putMVar(players.get(k).getPlayerName(), hand);	//Gets players hand and value
						Thread.sleep(50);
						}
						if(players.size()%2==0 && k%2==1) {		//If array size is even and player number is even
							readyPlayerIndex+=2;	//Increment z to select the couple of players who recently played
							scoreHolder=chat.takeScore();	//Take the result of game
							incPlayerResults(i);		//Use this method (Updates each players end results)
						}
						if(i == nturns-1 && k%2==1) {	//If all turns have been exhausted - output results
								System.out.println(players.get(k-1).getPlayerName() + "| Scored: [W: " + players.get(k-1).getWin() + " |L: " + players.get(k-1).getLose() + " |D: " + players.get(k-1).getDraw() + "]");
								System.out.println(players.get(k).getPlayerName() + "| Scored: [W: " + players.get(k).getWin() + " |L: " + players.get(k).getLose() + " |D: " + players.get(k).getDraw() + "]");
							}
					}
					i++;
				}
			} catch (InterruptedException e) {
					System.out.println("[Thread-Player] INTERRUPTED.");
				}
					System.out.println("[Thread-Player] exiting.");
		}

	  /*
	   * Method which increments Win, Lose or Draw for relevant players
	   */
	  public void incPlayerResults(int i) {
		  if(i!=0 && readyPlayerIndex>=nplayers-1) {
			  readyPlayerIndex=0;
		  }
			if(scoreHolder=="Win") {
				players.get(readyPlayerIndex).incWin();
				players.get(readyPlayerIndex+1).incLose();
				System.out.println(players.get(readyPlayerIndex).getPlayerName() + " WON!");
				System.out.println(players.get(readyPlayerIndex+1).getPlayerName() + " LOST!");
			} else if(scoreHolder=="Draw") {
				players.get(readyPlayerIndex).incDraw();
				players.get(readyPlayerIndex+1).incDraw();
				System.out.println(players.get(readyPlayerIndex).getPlayerName() + " DRAW!");
				System.out.println(players.get(readyPlayerIndex+1).getPlayerName() + " DRAW!");
			} else if(scoreHolder=="Lose") {
				players.get(readyPlayerIndex).incLose();
				players.get(readyPlayerIndex+1).incWin();
				System.out.println(players.get(readyPlayerIndex).getPlayerName() + " LOST!");
				System.out.println(players.get(readyPlayerIndex+1).getPlayerName() + " WON!");
			} else {
				System.out.println("Error getting Score");
			}
	  }
}

/*
 * Class created in order to create structure for objects in array
 * It has to hold a player name, his last hand and his score
 */
class Scoreboard {
	private String player;
	private String hand;
	private int win,lose,draw;

	Scoreboard(String p, String h) {
		this.player=p;
		this.hand=h;
	}

	@Override
	public String toString(){
		return player + " : " + hand;

	}

	//Compares first player's hand, with another player's hand and returns the outcome
	public String equalsGame(Scoreboard o2) {
		String result = "";
  				if (this.hand == "ROCK") {
  					if (o2.hand == "ROCK") {
  						result="Draw";
  					} else if (o2.hand == "PAPER") {
  						result="Lose";
  					} else if (o2.hand == "SCISSORS") {
  						result="Win";
  					}
  				} else if (this.hand == "PAPER") {
  					if (o2.hand == "ROCK") {
  						result="Win";
  					} else if (o2.hand == "PAPER") {
  						result="Draw";
  					} else if (o2.hand == "SCISSORS") {
  						result="Lose";
  					}

  				} else if (this.hand == "SCISSORS") {
  					if (o2.hand == "ROCK") {
  						result="Lose";
  					} else if (o2.hand == "PAPER") {
  						result="Win";
  					} else if (o2.hand == "SCISSORS") {
  						result="Draw";
  					}
  				}
			return result;
	}

	/*
	 * Block of simple methods which are essential to give an access to different values for other classes
	 */
	public String getPlayerName() {
		return player;
	}

	public String getPlayerHand() {
		return hand;
	}

	public int incWin() {
		return win++;
	}

	public int incDraw() {
		return draw++;
	}

	public int incLose() {
		return lose++;
	}

	public int getWin() {
		return win;
	}

	public int getDraw() {
		return draw;
	}

	public int getLose() {
		return lose;
	}

}

/*-------------------------------------------------------------------------------------
 * Thread referee instructions:
 *
 * For all players and their turns compares one player with another and sends result back to player thread
 *
 * This thread is allowed to exit as soon as the last comparison of players is done and result is sent to thread player
 * (Better results could have been achieved by using executorService class to gracefully stop the thread)
 */
	  	class RefereeJudge implements Runnable {
	  		private Chat<String> c;
	  		private String value="";
	  	    private List<Scoreboard> gameScore = new ArrayList<Scoreboard>();	//Holds All players and all their hands
			private int p1=-2, p2=-1, nplayers, nturns;	//p1 - will hold first player, p2 - will hold second player

	  		RefereeJudge(int np, Chat<String> c, int nt) {
	  	    this.c=c;
	  	    this.nplayers=np;
	  	    this.nturns=nt;
	  	    System.out.println("Creating [Thread-Referee]");
	  	  }

	  	  @Override
	  	  public void run() {
	  			System.out.println("Running [Thread-Referee]");
	  			try {
	  				while(gameScore.size() != nplayers*nturns) { //Do the following until all players have played their turns
		  			value=c.takeMVar();			//takes value
		  			Scoreboard score = new Scoreboard(c.getPlayer(), value);	//Creates an object in array with Player name and his value
		  			gameScore.add(score);	//Adds it to array
					if(gameScore.size()%2==0) {	//If array has even amount of players
						p1+=2;	//Get one before last player from array
						p2+=2;	//Get last player from array
 						System.out.println(gameScore.get(p1).getPlayerName() +"["+ gameScore.get(p1).getPlayerHand() + "] <--> ["+gameScore.get(p2).getPlayerHand() + "]" +gameScore.get(p2).getPlayerName());
 						//Plays the game and puts the score for player thread to record the result
 						c.putScore(gameScore.get(p1).getPlayerName(), gameScore.get(p1).equalsGame(gameScore.get(p2)));
					}
	  				Thread.sleep(50);
	  				}
	  			} catch (InterruptedException e) {
	  					System.out.println("[Thread-Referee] INTERRUPTED.");
	  				}
	  			System.out.println("[Thread-Referee] exiting.");
	  		}
	  	}

/*-------------------------------------------------------------------------------------
 * Main program, which initialises number of players and their number of turns.
 * Creates an object Chat, 2 Threads (player & referee)  and starts them
 */
public class GameMain {
	public static void main(String[] args) {
		final int nplayers=6;					//Number of players
		final int nturns=6;						//Number of turns
		Chat<String> chat = new Chat<String>();    //Communication between threads object creation

	    Runnable task = new PrepparingHand(nturns, chat, nplayers);	//Creates a "task" for thread of type Runnable and passes parameters
	    Thread player = new Thread(task);		//Creates a thread "player" and passes the "task" object
	    player.start();							//Start player thread

		Runnable reftask = new RefereeJudge(nturns, chat, nplayers);	//Creates a "reftask" for thread of type Runnable and passes parameters
		Thread referee = new Thread(reftask);	//Creates a thread "referee" and passes "reftask" object
		referee.start();						//Start referee thread
	}
}
