import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Chat<E>
{
    private E state;
    private boolean isSet = false;
    private boolean isSetSc = false;
    private String t;
    private String rez;

    public synchronized void putMVar(E s, String thname) {
        while (isSet) {
            try {
                wait();
            } catch (InterruptedException e) {
            	System.out.println("putMVar error: Interrupted");
            }
        }
        System.out.println("Value: [" + s + "] PUT by ["+thname+"]");
        isSet = true;
        t=thname;
        state = s;
        notifyAll();
    }

    public synchronized E takeMVar() {
        while (!isSet) {
            try {
                wait();
            } catch (InterruptedException e) {
            	System.out.println("putMVar error: Interrupted");
            }
        }
        System.out.println(t +"`s value: [" + state + "] TAKEN");
        isSet = false;
        notifyAll();
        return state;
    }

    public synchronized void putScore(String string, String thname) {
        while (isSetSc) {
            try {
                wait();
            } catch (InterruptedException e) {
            	System.out.println("putMVar error: Interrupted");
            }
        }
        System.out.println("RESULT [" + string + "] PUT FOR ["+thname+"]");
        isSetSc = true;
        t=thname;
        this.rez = string;
        notifyAll();
    }

    public synchronized String takeScore() {
        while (!isSetSc) {
            try {
                wait();
            } catch (InterruptedException e) {
            	System.out.println("putMVar error: Interrupted");
            }
        }
        System.out.println(t +"`s GOT SCORE: [" + rez + "]");
        isSetSc = false;
        notifyAll();
        return rez;
    }

    public synchronized String getPlayer() {
    	return t;
    }
}



//THREAD Player`s instructions
class PrepparingHand implements Runnable {
	  private long nturns;
	  private int nplayers;
	  private Chat<String> c;
	  private int win,lose,draw;
	  String scoreHolder="";

	  PrepparingHand(long nturns, Chat<String> c, int nplayers) {
	    this.nturns = nturns;
	    this.c=c;
	    this.nplayers= nplayers;
	    System.out.println("Creating [Thread-Player]");
	  }

	  @Override
	  public void run() {
		  Random rand = new Random();
			System.out.println("Running [Thread-Player]");
			try {
				int i=0,j=0;
				while(i != nturns) {
					for (int k = 0; k < nplayers; k++) {
						Thread.currentThread().setName("Player " + (k+1));
						String hand="";
						int val = rand.nextInt(3)+1;
						if (val == 1) {
							hand = "ROCK";
						} else if (val == 2) {
							hand = "PAPER";
						} else {
							hand = "SCISSORS"; }
						System.out.println((i+1)+"-Hand-["+ Thread.currentThread().getName() +"]-" + hand);
						c.putMVar(hand, Thread.currentThread().getName());
						Thread.sleep(50);
						if(((i+1)*(k+1))%2==0) {
							scoreHolder=c.takeScore();
							if(scoreHolder=="Win") {
								win+=1;
							} else if(scoreHolder=="Draw") {
								draw+=1;
							} else if(scoreHolder=="Lose") {
								lose+=1;
							} else {
								System.out.println("Error getting Score");
							}
						}
					}
					i++;
					}
			/*	while(j != nturns) {
					for (int k = 0; k < nplayers; k++) {

					}
					j++;
					} */
			} catch (InterruptedException e) {
					System.out.println("["+ Thread.currentThread().getName() +"] INTERRUPTED.");
				}
					System.out.println("[Thread-"+ Thread.currentThread().getName() +"] exiting. Score[W:"+win+"][D:"+draw+"][L:"+lose+"]");
		}
}

class Scoreboard {
	private String player;
	private String hand;

	Scoreboard(String p, String h) {
		this.player=p;
		this.hand=h;
	}

	@Override
	public String toString(){
		return player + " : " + hand;

	}
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

	public String getPlayerName() {
		return player;
	}

	public String getPlayerHand() {
		return hand;
	}

}

	  //THREAD Referee`s instructions
	  	class RefereeJudge implements Runnable {
	  		private Chat<String> c;
	  		private String value="";
	  	    private List<Scoreboard> gameScore = new ArrayList<Scoreboard>();
			int c1=-2;
			int c2=-1;
			String p1,p2;

	  		RefereeJudge(Chat<String> c) {
	  	    this.c=c;
	  	    System.out.println("Creating [Thread-Referee]");
	  	  }

	  	  @Override
	  	  public void run() {
	  			System.out.println("Running [Thread-"+ Thread.currentThread().getName() +"]");
	  			try {
	  				while(gameScore.size() != 4) {
		  			value=c.takeMVar();
		  			System.out.println(c.getPlayer() + " VALUE ADDED TO QUEUE: " + value);
		  			Scoreboard p = new Scoreboard(c.getPlayer(), value);
		  			gameScore.add(p);
					if(gameScore.size()%2==0) {
						c1+=2;
						c2+=2;
 						System.out.println(gameScore.get(c1).getPlayerName() +"["+ gameScore.get(c1).getPlayerHand() + "] <--> ["+gameScore.get(c2).getPlayerHand() + "]" +gameScore.get(c2).getPlayerName() + " --->>> " + gameScore.get(c1).equalsGame(gameScore.get(c2)));
 						c.putScore(gameScore.get(c1).equalsGame(gameScore.get(c2)), gameScore.get(c1).getPlayerName());
					}
	  				Thread.sleep(50);
	  				}
	  			} catch (InterruptedException e) {
	  					System.out.println("[Thread-"+ Thread.currentThread().getName() +"] INTERRUPTED.");
	  				}
	  			System.out.println("[Thread-"+ Thread.currentThread().getName() +"] exiting.");
	  		}
	  	}


public class GameMain {
	public static void main(String[] args) {
		final int nplayers=4;
		final int nturns=1;
		Chat<String> c = new Chat<String>();

	    Runnable task = new PrepparingHand(nturns, c, nplayers);
	    Thread player = new Thread(task);
	    player.start();

		Runnable reftask = new RefereeJudge(c);
		Thread referee = new Thread(reftask);
		referee.setName("Referee");
		referee.start();
	}
}
