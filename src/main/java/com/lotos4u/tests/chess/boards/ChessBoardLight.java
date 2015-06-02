package com.lotos4u.tests.chess.boards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class ChessBoardLight extends AbstractChessBoard {
	private Set<MicroBoard> uniqueVariants = new HashSet<MicroBoard>();
	private List<MicroBoard> allVariants = new LinkedList<MicroBoard>();
	
	private char[] boardView; //Letters (pieces names) at the points
	private int[] board; //numbers (pieces indexes) at the points
	private int[] boardTakeble; //incremental takebility data. If some piece take point value++
	private boolean[][][] boardsGray;
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
	private int rc;
	
	private boolean updateUniversalTakeble = true;
	private boolean updateTakeble = true;
	private boolean testBeforePut = true;
	private boolean sortAfter = true;
	
		
	public ChessBoardLight(int x, int y, char[] inPieces) {
		super(x, y);
		nPieces = inPieces.length;
		nPoints = x*y;
		boardView = new char[nPoints];
		board = new int[nPoints];
		boardTakeble = new int[nPoints];
		boardsGray = new boolean[nPieces][nPoints][nPoints];
		pieces = new char[nPieces];
		piecesPoints = new int[nPieces];
		Arrays.fill(piecesPoints, EMPTY);
		pieces = Arrays.copyOf(inPieces, nPieces);
		
		Arrays.fill(boardView, NONAME);
		Arrays.fill(board, EMPTY);
		Arrays.fill(boardTakeble, EMPTY);

		for (int point1 = 0; point1 < nPoints; point1++) {
			for (int p = 0; p < nPieces; p++)
				Arrays.fill(boardsGray[p][point1], false);
		}
	}
	
	public Set<MicroBoard> getUniqueVariants() {
		return uniqueVariants;
	}
	
	public List<MicroBoard> getAllVariants() {
		return allVariants;
	}

	public int getNPieces() {
		return nPieces;
	}


	private void sortPieces() {
		int[] power = new int[nPieces];
		for (int i = 0; i < nPieces; i++) 
			power[i] = getWeakForPiece(pieces[i]);
		Arrays.sort(power);
		for (int i = 0; i < nPieces; i++) 
			pieces[i] = getPieceForWeak(power[i]);
	}
	
	public int getArrangementVariants(boolean log, boolean logInner, boolean logExtra, boolean logSummary, boolean pauses) {
		if (logSummary) System.out.println("Unsorted Pieces: " + getPiecesAsString());
		sortPieces();
		if (logSummary) System.out.println("Sorted Pieces: " + getPiecesAsString());
		recursionStart = System.currentTimeMillis();
		recursiveCallCounter = 0;
		tryToPutCounter = 0;
		variantsCounter = 0;
		uniqueCounter = 0;
		equalsCounter = 0;
		puttedPiecesCounter = 0;
		uniqueVariants.clear();
		allVariants.clear();
		if (logSummary) System.out.println("Chess complexity is = " + nPoints*nPieces);
		updateUniversalTakeble();
		long updateTakebleTime = System.currentTimeMillis() - recursionStart;
		if (logSummary) System.out.println("Update takeble time " + updateTakebleTime + " ms");
		arrangeRecursively(log, logInner, logExtra, pauses);
		recursionFinish = System.currentTimeMillis();
		long recTime = recursionFinish-recursionStart;
		if (logSummary) System.out.println("Number of all variants = " + variantsCounter);
		if (logSummary) System.out.println("Arragements time " + recTime + " ms");
		long sortTime = 0;		
		if (sortAfter) {
			sortStart = System.currentTimeMillis();
			uniqueVariants.addAll(allVariants);
			sortFinish = System.currentTimeMillis();
			sortTime = sortFinish - sortStart;
		}
		if (logSummary) System.out.println("Number of unique variants = " + uniqueVariants.size());
		if (sortAfter) {
			if (logSummary) System.out.println("Sorting time = " + sortTime + " ms");
		} 
		if (logSummary) System.out.println("Full time = " + (recTime + sortTime) + " ms");
		if (logSummary) System.out.println("Number of recursive calls = " + recursiveCallCounter);
		if (recTime > 0) 
			if (logSummary) System.out.println("Number of recursive calls per ms = " + (float)(recursiveCallCounter/recTime));
		if (logSummary) System.out.println("Number of put tries = " + tryToPutCounter);		
		if (logSummary) System.out.println("Number of Equals calls = " + equalsCounter);
		if (logSummary) System.out.println("Number of HashCode calls = " + hashCounter);

		if (logSummary) System.out.println();
		return uniqueVariants.size();		
	}

	private boolean willItTake(int pieceIndex, int newLocation) {
		boolean takeble[] = Arrays.copyOf(boardsGray[pieceIndex][newLocation], nPoints);
		for (int testPieceIndex = 0; testPieceIndex < nPieces; testPieceIndex++)
			if ((piecesPoints[testPieceIndex] != EMPTY) && takeble[piecesPoints[testPieceIndex]])
				return true;
		return false;
	}
	public boolean tryToPut(int pieceIndex, int pointIndex, boolean log) {
		boolean putted = false;
		tryToPutCounter++;
		if (testBeforePut) {
			if (updateUniversalTakeble) {
				if (willItTake(pieceIndex, pointIndex)) 
					return false;
			} else {
				char piece = pieces[pieceIndex];
				for (int point = 0; point < nPoints; point++) {
					if ((board[point] != EMPTY) && canPieceTakePoint(piece, pointIndex, point))
						return false;
				}
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
	
	public void updateUniversalTakeble() {
		for (int pieceIndex = 0; pieceIndex < nPieces; pieceIndex++) {
			char piece = pieces[pieceIndex];
			for (int location = 0; location < nPoints; location++)
				for (int takeble = 0; takeble < nPoints; takeble++) 
					if ((takeble != location) && canPieceTakePoint(piece, location, takeble)) {
						boardsGray[pieceIndex][location][takeble] = true;
					}
			}
	}
		
	public void drawTakebleForPiece(int pieceIndex, int pointIndex) {
		boolean[] takeble = Arrays.copyOf(boardsGray[pieceIndex][pointIndex], nPoints);
		int[][] b = getBoardFromLinear(takeble);
		String res = getArrayAsString(b);
		System.out.println(res);
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
		if (logExtra) System.out.println("[" + rc  + "] N full = " + variantsCounter);

		int pieceIndex = getFistUnpositionedIndex();
		
		if (isArranged() || (pieceIndex == EMPTY)) {
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
			if ((boardTakeble[pointIndex] != EMPTY) || (board[pointIndex] != EMPTY))
				continue;
			res = false;
			if (log) System.out.println("[" + rc + "] Try " + pieces[pieceIndex] + " at (" + point[0] + ", " + point[1] + ")");
			putted = tryToPut(pieceIndex, pointIndex, logInner);
			if (putted) {
				res = isArranged() || arrangeRecursively(log, logInner, logExtra, pauses); 
				if (res) {
					variantsCounter++;
					if (log) System.out.println("[" + rc + "] No pieces!");
					MicroBoard b = new MicroBoard(this);
					//boolean isUnique = false;
					if (sortAfter) {
						allVariants.add(b);	
					} else {
						boolean isUnique = uniqueVariants.add(b);
						if (isUnique) {
							uniqueCounter++;
						}
					}
				} 				
			}
			dropPiece(pieceIndex);
		}
		res = isArranged();
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
	}
	
	private boolean isEmpty(int pointIndex) {
		return board[pointIndex] == EMPTY;
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
    		res += pieces[i] + " ";
    		int pointIndex = piecesPoints[i];
    		if (pointIndex != EMPTY) {
        		p = getPointForIndex(pointIndex);
        		res += "(" + p[0] + ", " + p[1] + ") ";
    		}
		}
    	return res;
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
	public char[] getBoardViewAsArray() {
		return boardView;
	}
}
