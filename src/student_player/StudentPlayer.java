package student_player;

import boardgame.Move;
import Saboteur.SaboteurMove;
import Saboteur.SaboteurPlayer;
import Saboteur.cardClasses.SaboteurBonus;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurDrop;
import Saboteur.cardClasses.SaboteurMalus;
import Saboteur.cardClasses.SaboteurMap;
import Saboteur.SaboteurBoardState;

import java.util.ArrayList;

/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260945126");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...

    	SaboteurMove myMove = null;
    	
    	ArrayList<SaboteurMove> legalMoves = boardState.getAllLegalMoves();
    	ArrayList<SaboteurCard> myDeck = boardState.getCurrentPlayerCards();
    	ArrayList<Integer> revealedHiddens = MyTools.revealedHiddenTiles(boardState);
    	    	
    	boolean nuggetDiscovered = MyTools.nuggetFound(boardState);
    	
    	/* ALGORITHM:
    	 * If you have a MAP card AND don't know where the objective(nugget) is: PLAY A MAP CARD
    	 * If you are blocked by a MALUS, play BONUS
    	 * If opponent is close to the objective and we have MALUS, play MALUS
    	 * 	if no malus and opponent about to win, play a card with a DEAD END
    	 * If close to the objective or else: play a TILE CARD
    	 * 	THEN pick best Tile card to play
    	 */
    	
    	// If we were blocked by MALUS, play BONUS
		if((boardState.getNbMalus(boardState.getTurnPlayer()) > 0) && MyTools.hasCard("Bonus", myDeck)) {
			myMove = new SaboteurMove (new SaboteurBonus(), 0, 0, boardState.getTurnPlayer());
		}
		
		else if((boardState.getNbMalus(boardState.getTurnPlayer()) > 0) && !(MyTools.hasCard("Bonus", myDeck))) {
			myMove = new SaboteurMove (new SaboteurDrop(), 0, 0, boardState.getTurnPlayer());
		}
		
		else if(MyTools.hasCard("Malus", myDeck)) {
			myMove = new SaboteurMove (new SaboteurMalus(), 0, 0, boardState.getTurnPlayer());

		}
    	/**********************************/
    	
    	if (!nuggetDiscovered) {
    		
    		// If nugget not discovered and we have a MAP card
    		if (MyTools.hasCard("Map", myDeck)) {
    			
    			int nextHidden = MyTools.getUnrevealedHidden(revealedHiddens);
    			myMove = new SaboteurMove(new SaboteurMap(),boardState.hiddenPos[nextHidden][0], boardState.hiddenPos[nextHidden][1],boardState.getTurnPlayer());
    		}
    		
    		
    		else if ((MyTools.numTilesLeftToNugget(boardState) == 2) && MyTools.tileHasContinuousPath(boardState.getAllLegalMoves())){
    			myMove = MyTools.playContinuousTile(boardState.getAllLegalMoves());
    		}
    		
    		else {
    			
    			myMove = Minimax.evaluate(boardState);    			
    			 
    		}
    	}
    	
    	else {
    		

    		myMove = Minimax.evaluate(boardState);
    	}


        // Return your move to be processed by the server.
        return myMove;
    }
}