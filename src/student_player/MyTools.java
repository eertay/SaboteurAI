package student_player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurTile;
import boardgame.Board;

public class MyTools {
	
	private static final String[] allCards = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "1gold", "2gold", "2_flip",
			"3gold", "3_flip", "4_flip", "5_flip", "6_flip", "7_flip", "9_flip", "11_flip", "12_flip", "14_flip" };
	private static final String[] noDeadEnd = { "0", "5", "5_flip", "6", "6_flip", "7", "7_flip", "8", "9", "9_flip", "10" };
	
	public static final int BOARD_SIZE = 14;
	public static final int originPos = 5;

	public static final int EMPTY = -1;
	public static final int TUNNEL = 1;
	public static final int WALL = 0;
	
	/**
	 * 
	 * @param cardName the card to look for in the deck, as a string
	 * @param myDeck player's deck
	 * @return true if card is found within the player's deck, false if not
	 */
	public static boolean hasCard(String cardName, ArrayList<SaboteurCard> myDeck) {
		
		for(SaboteurCard deckCard : myDeck) {
			if (deckCard.getName().equals(cardName)) {
				return true;
			}
		}
		return false;
	}
	
	// public HOW FAR AWAY ARE WE FROM OBJECTIVE
	// if nuggetFound && distance == 2 
	
	
	/**
	 * 
	 * @param nuggetPos the position of the 
	 * @param currentMove
	 * @return returns the distance from the given position
	 */
	public static int getDistance(int[] nuggetPos, SaboteurMove currentMove) {
		
		int[] currentPos = currentMove.getPosPlayed();
		return Math.abs(currentPos[0] - nuggetPos[0]) + Math.abs(currentPos[1] - nuggetPos[1]);
		
	}
	
	
	/**
	 * 
	 * @param boardState
	 * @param currentMove
	 * @return returns the distance from the nugget given that the location of the nugget has been discovered
	 */
	public static int distanceFromGoldenNugget(SaboteurBoardState boardState, SaboteurMove currentMove) {
		
		int[] nuggetPos = {12, getNugget(boardState)};
		
		
		return getDistance(nuggetPos, currentMove);
	
	}
	
	public static int[] getRandomObjective(SaboteurBoardState boardState) {
		
        Random rand = new Random(); 
        
		int objective = rand.nextInt(3);
	    objective = objective * 2 + 1;
	    
	    int[] objectivePos = {12, objective};

	    
	    return objectivePos;
	}
	
	/**
	 * 
	 * @param boardState
	 * @return returns an arrayList of all goal tiles which has been discovered by a Map card
	 */
	public static ArrayList<Integer> revealedHiddenTiles(SaboteurBoardState boardState) {
		
   
		ArrayList<Integer> revealed = new ArrayList<>();
		SaboteurTile[][] board = boardState.getHiddenBoard();
		
		for (int i = 3; i <= 7; i = i + 2) {
			if (!board[12][i].getIdx().contains("8")) {
				
				revealed.add(i);
			}
			

		}
		
		return revealed;
	
	}
	
	/** 
	 * 
	 * @param revealed
	 * @return returns a next hidden tile to play the Map card
	 */
	public static int getUnrevealedHidden(ArrayList<Integer> revealed) {
		
		for (int i = 0; i < 3; i++) {
			if (!revealed.contains(i)) {
				return i;
			}
		}
		
		return -1;
		
	}
	
	/**
	 * 
	 * @param boardState
	 * @return returns the y position of the nugget
	 */
	public static int getNugget(SaboteurBoardState boardState) {
		
		SaboteurTile[][] board = boardState.getHiddenBoard();

		
		for(int i = 3; i <= 7; i = i + 2) {
			
			if(board[12][i].getIdx().contains( "nugget")) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 
	 * @param boardState
	 * @return returns true if the nugget has been found, false if not
	 */
	public static boolean nuggetFound(SaboteurBoardState boardState) {
		
		ArrayList<Integer> revealed = revealedHiddenTiles(boardState);		
		
		if ((revealed.size() == 1) && (revealed.get(0) == getNugget(boardState))) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * THE FOLLOWING METHODS HAVE BEEN TAKEN AND ADAPTED FROM SaboteurBoardState, the provided code for the project.
	 * @param a
	 * @param o
	 * @return
	 */
	public static boolean containsIntArray(ArrayList<int[]> a,int[] o){ //the .equals used in Arraylist.contains is not working between arrays..
        if (o == null) {
            for (int i = 0; i < a.size(); i++) {
                if (a.get(i) == null)
                    return true;
            }
        } else {
            for (int i = 0; i < a.size(); i++) {
                if (Arrays.equals(o, a.get(i)))
                    return true;
            }
        }
        return false;
    }
	
	/** 
	 * @param pos
	 * @param queue
	 * @param visited
	 * @param maxSize
	 * @param usingCard
	 * @param boardState
	 */
	public static void addUnvisitedNeighborToQueue(int[] pos,ArrayList<int[]> queue, ArrayList<int[]> visited,int maxSize,boolean usingCard, SaboteurBoardState boardState){
		int[][] moves = {{0, -1},{0, 1},{1, 0},{-1, 0}};
        int i = pos[0];
        int j = pos[1];
        for (int m = 0; m < 4; m++) {
            if (0 <= i+moves[m][0] && i+moves[m][0] < maxSize && 0 <= j+moves[m][1] && j+moves[m][1] < maxSize) { //if the hypothetical neighbor is still inside the board
                int[] neighborPos = new int[]{i+moves[m][0],j+moves[m][1]};
                if(!containsIntArray(visited,neighborPos)){
                    if(usingCard && boardState.getBoardForDisplay()[neighborPos[0]][neighborPos[1]]!=null) queue.add(neighborPos);
                    else if(!usingCard && boardState.getHiddenIntBoard_corrected()[neighborPos[0]][neighborPos[1]]==1) queue.add(neighborPos);
                }
            }
        }
    }

	
	public static Boolean cardPath(ArrayList<int[]> originTargets,int[] targetPos,Boolean usingCard, SaboteurBoardState boardState){
        // the search algorithm, usingCard indicate weither we search a path of cards (true) or a path of ones (aka tunnel)(false).
        ArrayList<int[]> queue = new ArrayList<>(); //will store the current neighboring tile. Composed of position (int[]).
        ArrayList<int[]> visited = new ArrayList<int[]>(); //will store the visited tile with an Hash table where the key is the position the board.
        visited.add(targetPos);
        if(usingCard) addUnvisitedNeighborToQueue(targetPos,queue,visited,BOARD_SIZE,usingCard, boardState);
        else addUnvisitedNeighborToQueue(targetPos,queue,visited,BOARD_SIZE*3,usingCard, boardState);
        while(queue.size()>0){
            int[] visitingPos = queue.remove(0);
            if(containsIntArray(originTargets,visitingPos)){
                return true;
            }
            visited.add(visitingPos);
            if(usingCard) addUnvisitedNeighborToQueue(visitingPos,queue,visited,BOARD_SIZE,usingCard, boardState);
            else addUnvisitedNeighborToQueue(visitingPos,queue,visited,BOARD_SIZE*3,usingCard, boardState);
            System.out.println(queue.size());
        }
        return false;
    }
   
    public static boolean pathToHidden(SaboteurBoardState boardState, SaboteurMove move){
        /* This function look if a path is linking the starting point to the states among objectives.
            :return: if there exists one: true
                     if not: false
                     In Addition it changes each reached states hidden variable to true:  self.hidden[foundState] <- true
            Implementation details:
            For each hidden objectives:
                We verify there is a path of cards between the start and the hidden objectives.
                    If there is one, we do the same but with the 0-1s matrix!

            To verify a path, we use a simple search algorithm where we propagate a front of visited neighbor.
               TODO To speed up: The neighbor are added ranked on their distance to the origin... (simply use a PriorityQueue with a Comparator)
        */
    	ArrayList<int[]> originTargets = new ArrayList<>();
        originTargets.add(new int[]{originPos,originPos}); //the starting points
        int[] targetPos = {move.getPosPlayed()[0], move.getPosPlayed()[1]};
        if (cardPath(originTargets, targetPos, true, boardState)) {
        	ArrayList<int[]> originTargets2 = new ArrayList<>();
            //the starting points
            originTargets2.add(new int[]{originPos*3+1, originPos*3+1});
            originTargets2.add(new int[]{originPos*3+1, originPos*3+2});
            originTargets2.add(new int[]{originPos*3+1, originPos*3});
            originTargets2.add(new int[]{originPos*3, originPos*3+1});
            originTargets2.add(new int[]{originPos*3+2, originPos*3+1});
            //get the target position in 0-1 coordinate
            int[] targetPos2 = {targetPos[0]*3+1, targetPos[1]*3+2};
            int[] targetPos3 = {targetPos[0]*3+2, targetPos[1]*3+1};
            int[] targetPos4 = {targetPos[0]*3+1, targetPos[1]*3};
            int[] targetPos5 = {targetPos[0]*3, targetPos[1]*3+1};
            if (cardPath(originTargets2, targetPos2, false, boardState) ||
            		cardPath(originTargets2, targetPos3, false, boardState) ||
            		cardPath(originTargets2, targetPos4, false, boardState) ||
            		cardPath(originTargets2, targetPos5, false, boardState)) {
                return true;

            }

        }
			
		return false;	
	}
    
    public static boolean hasPathToHidden(ArrayList<SaboteurMove> legalMoves, SaboteurBoardState boardState) {
    	for (SaboteurMove move : legalMoves) {
    		if (pathToHidden(boardState, move)) {
    			return true;
    		}
    	}
    	
    	return false;
    	
    }
	/** 
	 * @param boardState
	 * @return Returns the number of tiles left to reach the golden nugget.
	 */
	public static int numTilesLeftToNugget(SaboteurBoardState boardState) {
		
		
		boolean nuggetDiscovered = nuggetFound(boardState);
		int count = 0;

		if (nuggetDiscovered) {
			int nuggetPos = getNugget(boardState);
			int[][] board = boardState.getHiddenIntBoard_corrected();

			
			// COLUMN SET ROW SEARCH
			for(int i = 11; i > 0; i--) {
				if (board[i][nuggetPos] == EMPTY) {
					count ++;
				}
				
				if(board[i][nuggetPos] == TUNNEL) {
					break;
				}
			}
			
			count = 0;
			
			for(int i = 13; i <= BOARD_SIZE; i++) {
				if (board[i][nuggetPos] == EMPTY) {
					count ++;
				}
				
				if(board[i][nuggetPos] == TUNNEL) {
					break;
				}
			}
			
			count = 0;
			
			// ROW SET COL SEARCH
			for(int i = nuggetPos; i <= BOARD_SIZE; i++) {
				if (board[12][i] == EMPTY) {
					count ++;
				}
				
				if(board[12][i] == TUNNEL) {
					break;
				}
			}
			
			count = 0;

			// ROW SET COL SEARCH
			for(int i = nuggetPos; i >= 0; i--) {
				if (board[12][i] == EMPTY) {
					count ++;
				}
				
				if(board[12][i] == TUNNEL) {
					break;
				}
			}

			
		}

		return count;
		
	}
	
	/**
	 * 
	 * @param legalMoves
	 * @return returns true if the player has a card with no dead ends
	 */
	public static boolean tileHasContinuousPath(ArrayList<SaboteurMove> legalMoves) {
    	
    	for (int i = 0; i < legalMoves.size(); i++) {
    		if (legalMoves.get(i).getCardPlayed() instanceof SaboteurTile) {
    			
    			SaboteurTile tile = (SaboteurTile) legalMoves.get(i).getCardPlayed();
    			
    			if(Arrays.asList(noDeadEnd).contains(tile.getIdx())) {
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
	
	/**
	 * 
	 * @param legalMoves
	 * @return Returns the move that plays a continuous tile.
	 */
	public static SaboteurMove playContinuousTile(ArrayList<SaboteurMove> legalMoves) {
		
		for (int i = 0; i < legalMoves.size(); i++) {
    		if (legalMoves.get(i).getCardPlayed() instanceof SaboteurTile) {
    			
    			SaboteurTile tile = (SaboteurTile) legalMoves.get(i).getCardPlayed();
    			
    			if(Arrays.asList(noDeadEnd).contains(tile.getIdx())) {
    				return legalMoves.get(i);
    			}
    		}
    	}
		
		return legalMoves.get(0);
	}
	
	
	
	
    public static double getSomething() {
        return Math.random();
    }
}