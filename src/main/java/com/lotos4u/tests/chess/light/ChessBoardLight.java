package com.lotos4u.tests.chess.light;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.lotos4u.tests.chess.general.Log;


public class ChessBoardLight {
	public static final char KING = 'K';
	public static final char QUEEN = 'Q';
	public static final char KNIGHT = 'N';
	public static final char ROOK = 'R';
	public static final char BISHOP = 'B';
	
	public static final char KING_TAKES = 'k';
	public static final char QUEEN_TAKES = 'q';
	public static final char KNIGHT_TAKES = 'n';
	public static final char ROOK_TAKES = 'r';
	public static final char BISHOP_TAKES = 'b';
	
	public static final char NONAME = ' ';
	public static final int EMPTY = -1;

	private Set<ChessBoardLight> uniqueVariants = new HashSet<ChessBoardLight>();
	private List<ChessBoardLight> allVariants = new LinkedList<ChessBoardLight>();
	
	private char[] boardView; //Letters (pieces names) at the points
	private int[] board; //numbers (pieces indexes) at the points
	private int[] boardTakeble; //incremental takebility data. If some piece take point value++
	private int[][] boardsGray;
	private char[] pieces; //all pieces names
	private int[] piecesPoints;
	private int xSize;
	private int ySize;
	private int nPieces;
	private int nPoints;
	
	private int puttedPiecesCounter;
	
	private long recursionStart;
	private long recursionFinish;
	private long sortStart;
	private long sortFinish;
	private int variantsCounter;
	private int uniqueCounter;
	private int recursiveCallCounter;
	private int tryToPutCounter;
	private static int equalsCounter;
	private static int hashCounter;
	private int rc;
	private boolean updateGray = false;
	private boolean updateTakeble = true;
	private boolean testBeforePut = true;
	private boolean sortAfter = true;
	private boolean updateEqualsCounter = true;
	private boolean updateHashCounter = true;
	
	

	public int[] getPointForIndex(int index) {
		int x = index / ySize;
		int y = index - x*ySize;
		return new int[]{x, y};
	}
	public int getIndexForPoint(int x, int y) {
		return y + ySize*x;//=index
	}
	public ChessBoardLight(ChessBoardLight input) {
		copyFromBoard(input);
	}
	
	private void copyFromBoard(ChessBoardLight input) {
		xSize = input.xSize;
		ySize = input.ySize;
		nPoints = xSize*ySize;
		nPieces = input.nPieces;
		boardView = new char[nPoints];
		board = new int[nPoints];
		boardTakeble = new int[nPoints];
		boardsGray = new int[nPieces][nPoints];
		pieces = new char[nPieces];
		piecesPoints = new int[nPieces];
		for (int i = 0; i < nPieces; i++) {
			pieces[i] = input.pieces[i];
			piecesPoints[i] = input.piecesPoints[i];
		}
		for (int point = 0; point < nPoints; point++) {
			boardView[point] = input.boardView[point];
			board[point] = input.board[point];
			boardTakeble[point] = input.boardTakeble[point];
			for (int p = 0; p < nPieces; p++)
				boardsGray[p][point] = input.boardsGray[p][point];
		}		
	}
	
	public ChessBoardLight(int x, int y, char[] inPieces) {
		xSize = x;
		ySize = y;
		nPieces = inPieces.length;
		nPoints = x*y;
		boardView = new char[nPoints];
		board = new int[nPoints];
		boardTakeble = new int[nPoints];
		boardsGray = new int[nPieces][nPoints];
		pieces = new char[nPieces];
		piecesPoints = new int[nPieces];
		for (int i = 0; i < nPieces; i++) {
			pieces[i] = inPieces[i];
			piecesPoints[i] = EMPTY;
		}
		for (int point = 0; point < nPoints; point++) {
			boardView[point] = NONAME;
			board[point] = EMPTY;
			boardTakeble[point] = EMPTY;
			for (int p = 0; p < nPieces; p++)
				boardsGray[p][point] = EMPTY;
		}
	}
	
	
	public Set<ChessBoardLight> getUniqueVariants() {
		return uniqueVariants;
	}
	
	public List<ChessBoardLight> getAllVariants() {
		return allVariants;
	}

	public int getArrangementVariants(boolean log, boolean logInner, boolean logExtra, boolean pauses) {
		recursionStart = System.currentTimeMillis();
		recursiveCallCounter = 0;
		tryToPutCounter = 0;
		variantsCounter = 0;
		uniqueCounter = 0;
		equalsCounter = 0;
		puttedPiecesCounter = 0;
		uniqueVariants.clear();
		allVariants.clear();
		arrangeRecursively(log, logInner, logExtra, pauses);
		recursionFinish = System.currentTimeMillis();
		long recTime = recursionFinish-recursionStart;
		long sortTime = 0;		
		if (sortAfter) {
			sortStart = System.currentTimeMillis();
			uniqueVariants.addAll(allVariants);
			sortFinish = System.currentTimeMillis();
			sortTime = sortFinish - sortStart;
		}

		if (logExtra) System.out.println("Chess complexity is = " + nPoints*nPieces);
		if (logExtra) System.out.println("Number of all variants = " + variantsCounter);
		if (logExtra) System.out.println("Number of unique variants = " + uniqueVariants.size());
		if (sortAfter) {
			if (logExtra) System.out.println("Arragements time " + recTime + " ms");
			if (logExtra) System.out.println("Sorting time = " + sortTime + " ms");
		} 
		if (logExtra) System.out.println("Full time = " + (recTime + sortTime) + " ms");
		if (logExtra) System.out.println("Number of recursive calls = " + recursiveCallCounter);
		if (recTime > 0) 
			if (logExtra) System.out.println("Number of recursive calls per ms = " + (float)(recursiveCallCounter/recTime));
		if (logExtra) System.out.println("Number of put tries = " + tryToPutCounter);		
		if (logExtra) System.out.println("Number of Equals calls = " + equalsCounter);
		if (logExtra) System.out.println("Number of HashCode calls = " + hashCounter);

		if (logExtra) System.out.println();
		return uniqueVariants.size();		
	}

	public boolean tryToPut(int pieceIndex, int pointIndex, boolean log) {
		boolean putted = false;
		tryToPutCounter++;
		if (testBeforePut) {
			char piece = pieces[pieceIndex];
			for (int point = 0; point < nPoints; point++) {
				if ((board[point] != EMPTY) && canPieceTakePoint(piece, pointIndex, point))
					return false;
			}
			putted = true;
			setPiecePosition(pieceIndex, pointIndex);
		} else {
			setPiecePosition(pieceIndex, pointIndex);
			putted = isValidQuick();
		}
		if (putted) {
			if(log)System.out.println("[" + rc + "] Putted " + pieces[pieceIndex]);
		} else {
			if(log)System.out.println("[" + rc + "] Not Putted " + pieces[pieceIndex]);
			dropPiece(pieceIndex);
		}
		return putted;
	}
		
	public List<Integer> getFreePoints() {
		List<Integer> res = new ArrayList<Integer>();
		for (int index = 0; index < nPoints; index++)
			if (isEmpty(index) && !isTakeble(index))
				res.add(Integer.valueOf(index));
		return res;
	}
	
	private boolean isTakeble(int pointIndex) {
		if (updateTakeble)
			return boardTakeble[pointIndex] != EMPTY;
		else {
			for (int p = 0; p < nPieces; p++) {
				char piece = pieces[p];
				int point = piecesPoints[p];
				if (point != EMPTY) {
					if (canPieceTakePoint(piece, point, pointIndex))
						return true;
				}
			}
			return false;
		}
	}

	public int getFistUnpositionedIndex() {
		for (int index = 0; index < nPieces; index++) {
			if (piecesPoints[index] == EMPTY) {
				return index;
			}
		}
		return EMPTY;
	}

	public boolean arrangeRecursively(boolean log, boolean logInner, boolean logExtra, boolean pauses){
		recursiveCallCounter++;
		rc++;
		boolean res = false;
		boolean putted = false;
		//long sec = (System.currentTimeMillis() - recursionStart)/1000;
		//if (logExtra) System.out.println("[" + rc  + "] N full = " + variantsCounter + ", N unique = " + uniqueCounter + ", CallCounter = " + callCounter  + ", "  + sec + " sec" );
		//if (logExtra) System.out.println("[" + rc  + "] N full = " + variantsCounter + ", N unique = " + uniqueCounter + ", CallCounter = " + recursiveCallCounter  + ", Equals="  + equalsCounter );
		//if (logExtra) System.out.println("[" + rc  + "] N full = " + variantsCounter);
		
		int pieceIndex = getFistUnpositionedIndex();
		
		if (pieceIndex == EMPTY) {
			if (log) System.out.println("[" + rc + "] No pieces! All arranged!");
			rc--;
			return true;
		}
		
		if (log) System.out.println("[" + rc + "] We have to put " + pieces[pieceIndex] + "(" + pieceIndex +  ")");
		
		List<Integer> free = getFreePoints();
		
		if (free.size() < 1) {
			if (log) System.out.println("[" + rc + "] No free points for " + pieces[pieceIndex] + "(" + pieceIndex +  ")!");
			rc--;
			return false;
		}
		
		if (log) System.out.println("[" + rc + "] We have " + free.size() + " points for " + pieces[pieceIndex]);
		//if (pauses) drawBoardAndBoardViewAndTakebleAndFree();
		//if (pauses) Log.pause();
		//if (log) drawBoardAndTakeble();
		//if (log) System.out.println("[" + rc + "] Pieces: " + getPiecesAsString());
		//if (log) System.out.println("[" + rc + "] Free: " + getPointsAsString(free));
		
		for (Integer pointI : free) {
		//int[] point = getFirstFreePoint();
		//while (point[0] != EMPTY) {
			int pointIndex = pointI.intValue();
			int[] point = getPointForIndex(pointIndex);
			//if ((boardTakeble[x][y] != EMPTY) || (board[x][y] != EMPTY))
			//	continue;
			res = false;
			if (log) System.out.println("[" + rc + "] Try " + pieces[pieceIndex] + " at (" + point[0] + ", " + point[1] + ")");
			putted = tryToPut(pieceIndex, pointIndex, logInner);
			if (putted) {
				//if (pauses) System.out.println("Putted " + pieces[pieceIndex] + "(" + pieceIndex + ")");
				if (pauses) drawBoardAndBoardViewAndTakebleAndFree();
				//if (pauses) drawBoard();
				if (pauses) Log.pause();
				res = isArranged() || arrangeRecursively(log, logInner, logExtra, pauses); 
				if (res) {
					variantsCounter++;
					if (log) System.out.println("[" + rc + "] No pieces!");
					ChessBoardLight b = new ChessBoardLight(this);
					//boolean isUnique = false;
					if (sortAfter) {
						allVariants.add(b);	
					} else {
						boolean isUnique = uniqueVariants.add(b);
						
						//if (pauses) System.out.println("Success!!!");
						if (pauses) drawBoardAndBoardViewAndTakebleAndFree();
						//if (pauses) drawBoard();
						if (pauses) Log.pause();
						if (isUnique) {
							uniqueCounter++;
							//System.out.println(b.hashCode());
							//drawBoard();
							//Log.pause();
						}
					}
				} 				
			}
			dropPiece(pieceIndex);
			//point = getFirstFreePoint();
		}
		res = isArranged();
		if (!res) {
			dropPiece(pieceIndex);
		}
		if (log) System.out.println("[" + rc + "] Tested " + free.size() + " free points for " + pieces[pieceIndex] + "(" + pieceIndex +  "). Result = " + res);
		rc--;
		
		return res;				
	}

	public List<Integer> getTakebleForPiece(char piece, int newPointIndex) {
		List<Integer> res = new ArrayList<Integer>();
		for (int index = 0; index < nPoints; index++)
			if (canPieceTakePoint(piece, newPointIndex, index))
				res.add(Integer.valueOf(index));
		return res;
	}

	public boolean isArrangedAndValid() {
		return isArranged() && isValid();
	}
	
	public boolean canPieceTakePoint(char piece, int hisPointIndex, int testPointIndex) {
		int[] his = getPointForIndex(hisPointIndex);
		int[] test = getPointForIndex(testPointIndex);
		int hisX = his[0];
		int hisY = his[1];
		int testX = test[0];
		int testY = test[1];
		if ((isBishop(piece) && canBishopTakePoint(hisX, hisY, testX, testY)) ||
				   (isKing(piece) && canKingTakePoint(hisX, hisY, testX, testY)) ||
				   (isKnight(piece) && canKnightTakePoint(hisX, hisY, testX, testY)) ||
				   (isRook(piece) && canRookTakePoint(hisX, hisY, testX, testY)) ||
				   (isQueen(piece) && canQueenTakePoint(hisX, hisY, testX, testY))) {
					return true;
					}	
		return false;
	}
	
	public boolean isValidQuick() {
		if (!updateTakeble)
			return isValid();
		for (int index = 0; index < nPoints; index++) {
			if ((board[index] != EMPTY) && (boardTakeble[index] != EMPTY))
				return false;
		}
		return true;
	}
	public boolean isValid() {
		for (int p1 = 0; p1 < nPieces; p1++) {
			if (piecesPoints[p1] != EMPTY)
				for (int p2 = 0; p2 < nPieces; p2++) {
					int point1 = piecesPoints[p1];
					int point2 = piecesPoints[p2];
					if (!(piecesPoints[p2] != EMPTY) && canPieceTakePoint(pieces[p1], point1, point2))
						return false;
				}
		}
		return true;
	}
	
	public boolean isArranged() {
		return puttedPiecesCounter == nPieces;
			/*
		for (int i = 0; i < nPieces; i++)
			if (piecesPoints[i] == EMPTY)
				return false;
		return true;
		*/
	}
	private void updateGrayForPut(int pieceIndex, int newPointIndex) {
		if (!updateGray)return;
		char piece = pieces[pieceIndex];
		for (int index = 0; index < nPoints; index++) {
			if (canPieceTakePoint(piece, newPointIndex, index))
				boardsGray[pieceIndex][index]++;
		}
	}
	private void updateGrayForDrop(int pieceIndex, int oldPointIndex) {
		if (!updateGray)return;
		char piece = pieces[pieceIndex];
		for (int index = 0; index < nPoints; index++) {
			if (canPieceTakePoint(piece, oldPointIndex, index))
				boardsGray[pieceIndex][index]--;
		}
	}
	private void updateTakebleForPut(char piece, int newPointIndex) {
		if(!updateTakeble)return;
		for (int index = 0; index < nPoints; index++) {
			if (canPieceTakePoint(piece, newPointIndex, index))
				boardTakeble[index]++;
		}
	}
	
	private void updateTakebleForDrop(char piece, int oldPointIndex) {
		if(!updateTakeble)return;
		for (int index = 0; index < nPoints; index++) {
			if (canPieceTakePoint(piece, oldPointIndex, index))
				boardTakeble[index]--;
		}
	}
	
	private void clearBoardPoint(int pointIndex) {
		int pieceIndex = board[pointIndex];
		if (pieceIndex != EMPTY) {
			puttedPiecesCounter--;
			piecesPoints[pieceIndex] = EMPTY;
			updateTakebleForDrop(pieces[pieceIndex], pointIndex);
			updateGrayForDrop(pieceIndex, pointIndex);
		}
		boardView[pointIndex] = NONAME;
		board[pointIndex] = EMPTY;
	}
	
	public void dropPieces() {
		for (int point = 0; point < nPoints; point++)
			clearBoardPoint(point);
	}
	
	public void dropPiece(int pieceIndex) {
		int point = piecesPoints[pieceIndex];
		if (point != EMPTY) {
			clearBoardPoint(point);	
		}
	}

	public void setPiecePosition(int pieceIndex, int pointIndex) {
		clearBoardPoint(pointIndex);
		if (piecesPoints[pieceIndex] != EMPTY)
			clearBoardPoint(piecesPoints[pieceIndex]);
		boardView[pointIndex] = pieces[pieceIndex];
		board[pointIndex] = pieceIndex;
		piecesPoints[pieceIndex] = pointIndex;
		puttedPiecesCounter++;
		updateTakebleForPut(pieces[pieceIndex], pointIndex);
		updateGrayForPut(pieceIndex, pointIndex);
	}
	
	private boolean isEmpty(int pointIndex) {
		return board[pointIndex] == EMPTY;
	}
	private boolean isBishop(char pieceType) {
		return BISHOP == pieceType;
	}
	private boolean isRook(char pieceType) {
		return ROOK == pieceType;
	}
	private boolean isKing(char pieceType) {
		return KING == pieceType;
	}
	private boolean isQueen(char pieceType) {
		return QUEEN == pieceType;
	}
	private boolean isKnight(char pieceType) {
		return KNIGHT == pieceType;
	}
	
	
	private boolean canBishopTakePoint(int hisX, int hisY, int testX, int testY) {
		int diffX = Math.abs(hisX - testX);
		int diffY = Math.abs(hisY - testY);
		return (diffY == diffX) && (diffX > 0);
	}
	private boolean canRookTakePoint(int hisX, int hisY, int testX, int testY) {
		return (hisY == testY) ^ (hisX == testX);
	}
	private boolean canKingTakePoint(int hisX, int hisY, int testX, int testY) {
		int diffX = Math.abs(hisX - testX);
		int diffY = Math.abs(hisY - testY);
		return (diffY <= 1) && (diffX <= 1) && ((diffX + diffY) > 0);
	}
	private boolean canKnightTakePoint(int hisX, int hisY, int testX, int testY) {
		int diffX = Math.abs(hisX - testX);
		int diffY = Math.abs(hisY - testY);
		return ((diffX == 1) && (diffY == 2)) || ((diffX == 2) && (diffY == 1));
	}
	private boolean canQueenTakePoint(int hisX, int hisY, int testX, int testY) {
		return canRookTakePoint(hisX, hisY, testX, testY) ||
				canBishopTakePoint(hisX, hisY, testX, testY);
	}
	
	public int[][] getBoardFree() {
		int[][] b = new int[xSize][ySize];
		for (int pointIndex = 0; pointIndex < nPoints; pointIndex++) {
			boolean empty = board[pointIndex] == EMPTY;
			boolean notTakeble;
			if(updateTakeble) {
				notTakeble = boardTakeble[pointIndex] == EMPTY;	
			} else {
				notTakeble = true;
				for (int p = 0; p < nPieces; p++) {
					char piece = pieces[p];
					if (canPieceTakePoint(piece, piecesPoints[p], pointIndex)) {
						notTakeble = false;
						break;
					}
				}
			}
			int[] point = getPointForIndex(pointIndex);
			int x = point[0];
			int y = point[1];
			if (empty && notTakeble)
				b[x][y] = 0;
			else
				b[x][y] = EMPTY;
		}
		return b;
	}
	/*
    private int[][] getBoardTakeble() {
		int[][] b = new int[xSize][ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				for (int p = 0; p < nPieces; p++) {
					char piece = pieces[p];
					int hisX = piecesXY[p][0];
					int hisY = piecesXY[p][1];
					if (canPieceTakePoint(piece, hisX, hisY, x, y))
						b[x][y]++;
				}
			}
		return b;
	}
	*/
	public char[][] getBoardView() {
		char[][] b = new char[xSize][ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				int pointIndex = getIndexForPoint(x, y);
				int index = board[pointIndex];
				if (index != EMPTY)
					b[x][y] = pieces[index];
				else
					b[x][y] = NONAME;
			}
		return b;
	}
	public int[][] getBoardIndexes() {
		int[][] b = new int[xSize][ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				int pointIndex = getIndexForPoint(x, y);
				int index = board[pointIndex];
				if (index != EMPTY)
					b[x][y] = pieces[index];
				else
					b[x][y] = NONAME;
			}
		return b;
	}
	public int[][] getBoardTakeble() {
		int[][] b = new int[xSize][ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				int pointIndex = getIndexForPoint(x, y);
				int index = boardTakeble[pointIndex];
				if (index != EMPTY)
					b[x][y] = pieces[index];
				else
					b[x][y] = NONAME;
			}
		return b;
	}
    /**
     * Draw a board into System.out
     */
    public void drawBoard() {
    	System.out.println(getArrayAsString(getBoardView()));
    }
    public void drawTakeble() {
    	if (updateTakeble)
    		System.out.println(getArrayAsString(getBoardTakeble()));
    	else
    		System.out.println(getArrayAsString(getBoardTakeble()));
    }
	public void drawBoardViewAndTakeble() {
    	System.out.println(getBoardViewAndTakebleAsString());
    }
    public void drawBoardAndTakeble() {
    	System.out.println(getBoardAndTakebleAsString());
    }
    public void drawBoardAndBoardViewAndTakeble() {
    	System.out.println(getBoardAndBoardViewAndTakebleAsString());
    }
    public void drawBoardAndBoardViewAndTakebleAndFree() {
    	System.out.println(getBoardAndBoardViewAndTakebleAndFreeAsString());
    }
    private String getBoardAndBoardViewAndTakebleAndFreeAsString() {
    	String res = "";
    	List<String> boardViewStrings = getArrayAsStrings(getBoardView());
    	List<String> boardStrings = getArrayAsStrings(getBoardIndexes());
    	List<String> takebleStrings = getArrayAsStrings(getBoardTakeble());
    	List<String> freeStrings = getArrayAsStrings(getBoardFree());
    	for (int i = 0; i < boardStrings.size(); i++) {
    		res += boardViewStrings.get(i) + "    " + boardStrings.get(i) + "    " + takebleStrings.get(i) + "    " + freeStrings.get(i) + "\n";
		}
    	res += "\n";
    	return res;
    }
    private String getBoardAndBoardViewAndTakebleAsString() {
    	String res = "";
    	List<String> boardViewStrings = getArrayAsStrings(getBoardView());
    	List<String> boardStrings = getArrayAsStrings(getBoardIndexes());
    	List<String> takebleStrings = getArrayAsStrings(getBoardTakeble());
    	for (int i = 0; i < boardStrings.size(); i++) {
    		res += boardViewStrings.get(i) + "    " + boardStrings.get(i) + "    " + takebleStrings.get(i) + "\n";
		}
    	res += "\n";
    	return res;
    }

    private String getBoardViewAndTakebleAsString() {
    	String res = "";
    	List<String> boardStrings = getArrayAsStrings(getBoardView());
    	List<String> takebleStrings = getArrayAsStrings(getBoardTakeble());
    	for (int i = 0; i < boardStrings.size(); i++) {
    		res += boardStrings.get(i) + "    " + takebleStrings.get(i) + "\n";
		}
    	res += "\n";
    	return res;
    }
    private String getBoardAndTakebleAsString() {
    	String res = "";
    	List<String> boardStrings = getArrayAsStrings(getBoardIndexes());
    	List<String> takebleStrings = getArrayAsStrings(getBoardTakeble());
    	for (int i = 0; i < boardStrings.size(); i++) {
    		res += boardStrings.get(i) + "    " + takebleStrings.get(i) + "\n";
		}
    	res += "\n";
    	return res;
    }
    private String getArrayAsString(int[][] input) {
    	String res = "";
    	List<String> pre = getArrayAsStrings(input);
    	for (Iterator<String> iterator = pre.iterator(); iterator.hasNext();) {
			 res += (iterator.next() + "\n");
		}
    	return res;
    }
    private String getArrayAsString(char[][] input) {
    	String res = "";
    	List<String> pre = getArrayAsStrings(input);
    	for (Iterator<String> iterator = pre.iterator(); iterator.hasNext();) {
			 res += (iterator.next() + "\n");
		}
    	return res;
    }
    private List<String> getArrayAsStrings(int[][] input) {
    	int xMax = input.length;
    	int yMax = (xMax > 0) ? input[0].length : 0;
    	List<String> resList = new ArrayList<String>();
    	String res1 = "  ";
    	for (int y = 0; y < yMax; y++) {
    		res1 += (" " + y + " ");
    	}
    	resList.add(res1);
    	String res2[] = new String[xMax];
    	for (int x = 0; x < xMax; x++) {
    		res2[x] = "";
    		String num = "" + x;
    		num = (x < 10) ? " " + num : num;
    		res2[x] += num;
    		for (int y = 0; y < yMax; y++) {
    			String v = (input[x][y] >= 0) ? ("" + input[x][y]) : " ";
    			res2[x] += ("[" + v + "]");
    		}
    		resList.add(res2[x]);
    	}
    	return resList;
    }    
    private List<String> getArrayAsStrings(char[][] input) {
    	int xMax = input.length;
    	int yMax = (xMax > 0) ? input[0].length : 0;
    	List<String> resList = new ArrayList<String>();
    	String res1 = "  ";
    	for (int y = 0; y < yMax; y++) {
    		res1 += (" " + y + " ");
    	}
    	resList.add(res1);
    	String res2[] = new String[xMax];
    	for (int x = 0; x < xMax; x++) {
    		res2[x] = "";
    		String num = "" + x;
    		num = (x < 10) ? " " + num : num;
    		res2[x] += num;
    		for (int y = 0; y < yMax; y++) {
    			res2[x] += ("[" + input[x][y] + "]");
    		}
    		resList.add(res2[x]);
    	}
    	return resList;
    }
    
    public String getPointsAsString(List<int[]> pts) {
    	String res = "";
    	for (Iterator<int[]> iterator = pts.iterator(); iterator.hasNext();) {
			int[] pt = iterator.next();
			res += "(" + pt[0] + ", " + pt[1] + ") ";
		}
    	return res;
    }
    public String getPiecesAsString() {
    	String res = "";
    	int[] p;
    	for (int i = 0; i < nPieces; i++) {
    		int pointIndex = piecesPoints[i];
    		p = getPointForIndex(pointIndex);
    		res += pieces[i] + "(" + p[0] + ", " + p[1] + ") ";
		}
    	return res;
    }

    
    public boolean isSameGame(ChessBoardLight b) {
    	return (b.xSize == xSize) && 
    			(b.ySize == ySize) &&
    			(b.nPieces == nPieces);
    }

    public boolean isDifferentGame(ChessBoardLight b) {
    	return (b.xSize != xSize) || 
    			(b.ySize != ySize) ||
    			(b.nPieces != nPieces);
    }
    public boolean isArrangeEqualsNative(ChessBoardLight b) {
    	for (int i = 0; i < nPoints; i++) {
    		if (boardView[i] != b.boardView[i])
    			return false;
    	}
    	return true;
    }

    public boolean isArrangeEquals(ChessBoardLight b) {
    	if (isDifferentGame(b))
    		return false;
    	return Arrays.equals(boardView, b.boardView);
    	/*
    	for (int p = 0; p < nPoints; p++)
   			if (b.boardView[p] != boardView[p])
   				return false;
    	return true;
    	*/
    }    
	@Override
	public boolean equals(Object obj) {
		if (updateEqualsCounter) equalsCounter++;
		if (!(obj instanceof ChessBoardLight))
			return false;
		ChessBoardLight b = (ChessBoardLight) obj;
		//if (!isArrangeEquals(b))
		if (!isArrangeEqualsNative(b))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		if (updateHashCounter) hashCounter++;
        final int prime = 31;
        int result = 1;
        result = prime * result + xSize;
        result = prime * result + ySize;
        result = prime * result + nPieces;
        //Arrays.
        for (int i = 0; i < nPoints; i++) {
        	result += boardView[i]*i;
        }
        return result;
    }
/*	
	public static void main (String arg[]) {
		Log.out("\n\n********************** Test testBoardLightRecursion4x4 **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.ROOK, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(4, 4, pcs1);
		board1.getArrangementVariants(false, false, true, false);

		int counter = 0;
        for (ChessBoardLight b : board1.getUniqueVariants()) {
        	System.out.println("Variant " + (++counter));
        	b.drawBoard();
        }			
	}
*/	
}
