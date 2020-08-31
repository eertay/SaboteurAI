package student_player;

import java.util.ArrayList;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurTile;
import boardgame.Board;

public class Minimax {
	
	/** 
	 * This method basically works as the findBestMove method in minimax algorithm, as the normal evaluate function would require the agent to deduce if a game
	 * will result in a win or loss. It is modified according to the Saboteur game.
	 * @param boardState
	 * @return 
	 */
	public static SaboteurMove evaluate(SaboteurBoardState boardState) {
		
		ArrayList<SaboteurMove> legalMoves = boardState.getAllLegalMoves();
		SaboteurMove bestMove = legalMoves.get(0);
		boolean pathFound = MyTools.hasPathToHidden(legalMoves, boardState);
				
		for (SaboteurMove nextMove : legalMoves) {
			
			if (MyTools.nuggetFound(boardState)) {
			
				if(MyTools.distanceFromGoldenNugget(boardState, nextMove) < MyTools.distanceFromGoldenNugget(boardState, bestMove)) {
					
					bestMove = nextMove;
					
					/* 
					if ((MyTools.numTilesLeftToNugget(boardState) < 5) && MyTools.tileHasContinuousPath(boardState.getAllLegalMoves())){
		    			bestMove = MyTools.playContinuousTile(boardState.getAllLegalMoves());
		    		}  */
					
				}
			}
			
			else {
				
		    	ArrayList<Integer> revealedHiddens = MyTools.revealedHiddenTiles(boardState);
    			int nextHidden = MyTools.getUnrevealedHidden(revealedHiddens);
    			int[] obj = {12, nextHidden};
		    					
				if(MyTools.getDistance(obj, nextMove) < MyTools.getDistance(obj, bestMove)) {
					
					bestMove = nextMove;

					
				}
				
			}
		}
		
		if (pathFound) {
			for (SaboteurMove nextMove : legalMoves) {
				if (MyTools.pathToHidden(boardState, nextMove)) {
					bestMove = nextMove;
				}
			}
		}
		
		System.out.println("?????????????? BEST DISTANCE FROM GOLDEN NUGGET : " + MyTools.distanceFromGoldenNugget(boardState, bestMove));
		System.out.println("!!!!!!!!!!!!!!!!!");
		
		return bestMove;
	}
	
	
	
	// We are player 1
	/**
	 * 
	 * @param boardState
	 * @return Returns +10 agent wins, -10 if agent loses, 0 if it's a draw
	 */
	public static int oldEvaluate(SaboteurBoardState boardState) {
		
		
		int winner = boardState.getWinner();
		
		if(boardState.getTurnPlayer() == 1) {
			
			if(winner == 1) {
				return +10;
			}
			
			else if(winner == Board.NOBODY) {
				return 0;
			}
			
			else {
				return -10;
			}
			
		}
		
		else {
			
			if(winner == 1) {
				return +10;
			}
			
			else if(winner == Board.NOBODY) {
				return 0;
			}
			
			else {
				return -10;
			}
		}
	}
	
	

}
