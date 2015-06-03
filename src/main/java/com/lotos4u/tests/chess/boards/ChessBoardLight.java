package com.lotos4u.tests.chess.boards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class ChessBoardLight extends AbstractChessBoard {
	public class ComparablePoint implements Comparable<ComparablePoint> {
		public int index;
		public int nNneighbors;
		public ComparablePoint(int pointIndex) {
			index = pointIndex;
			nNneighbors = ChessBoardLight.this.pointsNeighbors[index];
		}
		public int compareTo(ComparablePoint o) {
			return (o.nNneighbors - nNneighbors);
		}
		@Override
		public String toString() {
			return "" + index;
		}
	}	

	protected Set<MicroBoard> uniqueVariants = new HashSet<MicroBoard>();
	protected List<MicroBoard> allVariants = new LinkedList<MicroBoard>();
	
	protected int[] boardPiecesIndexes; //numbers (pieces indexes) at the points
	protected int[] boardFree; //numbers (pieces indexes) at the points
	protected int[] pointsNeighbors; //Number of Neighbors for each point
	protected int[] boardTakeble; //incremental takebility data. If some piece take point value++
	protected boolean[][][] boardsGray;
	protected char[] pieces; //all pieces names
	protected int[] piecesPoints;
	protected List<ChessBoardLight.ComparablePoint> sortedComparablePoints;;
	
	protected int puttedPiecesCounter;
	protected int freePointsCounter;
	
	protected long recursionStart;
	protected long recursionFinish;
	protected long sortStart;
	protected long sortFinish;
	protected int variantsCounter;
	protected int uniqueCounter;
	
	protected int recursiveCallCounter;
	protected int tryToPutCounter;
	protected int isSafeCallCounter;
	protected int clearPointCounter;
	protected int setPositionCounter;
	
	public static boolean sortPoints = false;
	
	private int[] sortedPoints;
	
	
	public ChessBoardLight(int x, int y, char[] inPieces) {
		super(x, y);
		nPieces = inPieces.length;
		nPoints = x*y;
		boardPiecesIndexes = new int[nPoints];
		pointsNeighbors = new int[nPoints];
		boardFree = new int[nPoints];
		boardTakeble = new int[nPoints];
		sortedPoints = new int[nPoints];
		boardsGray = new boolean[nPieces][nPoints][nPoints];
		pieces = new char[nPieces];
		piecesPoints = new int[nPieces];
		Arrays.fill(piecesPoints, EMPTY);
		pieces = Arrays.copyOf(inPieces, nPieces);
		sortPieces();
		Arrays.fill(boardPiecesIndexes, EMPTY);
		Arrays.fill(boardFree, EMPTY);
		Arrays.fill(boardTakeble, EMPTY);

		sortedComparablePoints = new ArrayList<ChessBoardLight.ComparablePoint>();
		updateNeighborsNumber();
		for (int point1 = 0; point1 < nPoints; point1++) {
			sortedComparablePoints.add(new ChessBoardLight.ComparablePoint(point1));
			for (int p = 0; p < nPieces; p++)
				Arrays.fill(boardsGray[p][point1], false);
		}
		
		if (sortPoints)
			Collections.sort(sortedComparablePoints);
		
		for (int i = 0; i < nPoints; i++)
			sortedPoints[i] = sortedComparablePoints.get(i).index;
	}
	
	public Set<MicroBoard> getUniqueVariants() {
		return uniqueVariants;
	}

	public List<MicroBoard> getAllVariants() {
		return allVariants;
	}

	public int getNeighborsNumber(int pointIndex) {
		int res = 0;
		int[] point = getPointForIndex(pointIndex);
		int x, x0 = point[0];
		int y, y0 = point[1];
		x = x0 + 1; y = y0;
		if (isPointInside(x, y))
			res++;
		x = x0 - 1; y = y0;
		if (isPointInside(x, y))
			res++;
		x = x0; y = y0 + 1;
		if (isPointInside(x, y))
			res++;
		x = x0; y = y0 - 1;
		if (isPointInside(x, y))
			res++;
		x = x0 + 1; y = y0 + 1;
		if (isPointInside(x, y))
			res++;
		x = x0 + 1; y = y0 - 1;
		if (isPointInside(x, y))
			res++;
		x = x0 - 1; y = y0 + 1;
		if (isPointInside(x, y))
			res++;
		x = x0 - 1; y = y0 - 1;
		if (isPointInside(x, y))
			res++;
		return res;
	}
	public void updateNeighborsNumber() {
		for (int index = 0; index < nPoints; index++)
			pointsNeighbors[index] = getNeighborsNumber(index);
	}

	private void sortPieces() {
		int[] power = new int[nPieces];
		for (int i = 0; i < nPieces; i++) 
			power[i] = getWeakForPiece(pieces[i]);
		Arrays.sort(power);
		for (int i = 0; i < nPieces; i++) 
			pieces[i] = getPieceForWeak(power[i]);
	}
	
	public int getArrangementVariants(boolean inLog, boolean inLogInner, boolean inLogExtra, boolean logSummary, boolean inPauses) {
		if (logSummary) System.out.println("Sorted Pieces: " + getPiecesAsString());
		if (logSummary) System.out.println("Sorted Points: " + getSortedPointsIndexesAsString());
		recursionStart = System.currentTimeMillis();
		puttedPiecesCounter = 0;
		freePointsCounter = nPoints;
		
		recursiveCallCounter = 0;
		tryToPutCounter = 0;
		variantsCounter = 0;
		uniqueCounter = 0;
		equalsCounter = 0;
		isSafeCallCounter = 0;
		clearPointCounter = 0;
		setPositionCounter = 0;
		uniqueVariants.clear();
		allVariants.clear();
		MicroBoard.equalsCounter = 0;
		MicroBoard.hashCounter = 0;	
		if (logSummary) System.out.println("Chess complexity is = " + nPoints*nPieces);
		updateUniversalTakeble();
		long updateTakebleTime = System.currentTimeMillis() - recursionStart;
		if (logSummary) System.out.println("Update takeble time " + updateTakebleTime + " ms");
		arrangeRecursively();
		recursionFinish = System.currentTimeMillis();
		long recTime = recursionFinish-recursionStart;
		if (logSummary) System.out.println("Number of all variants = " + variantsCounter);
		if (logSummary) System.out.println("Arrangements time " + recTime + " ms");
		if (logSummary) System.out.println("Number of unique variants = " + uniqueVariants.size());
		if (logSummary) System.out.println("Number of recursive calls = " + recursiveCallCounter);
		if (recTime > 0) 
			if (logSummary) System.out.println("Number of recursive calls per ms = " + (float)(recursiveCallCounter/recTime));
		if (logSummary) System.out.println("Number of isSafe = " + isSafeCallCounter);
		if (logSummary) System.out.println("Number of clearPoint = " + clearPointCounter);
		if (logSummary) System.out.println("Number of setPosition = " + setPositionCounter);
		if (logSummary) System.out.println("Number of Equals calls = " + equalsCounter);
		if (logSummary) System.out.println("Number of HashCode calls = " + hashCounter);

		if (logSummary) System.out.println();
		return uniqueVariants.size();		
	}

	private boolean isItSafeToPutPieceAtPoint(int pieceIndex, int newLocation) {
		//isSafeCallCounter++;
		for (int pointIndex : piecesPoints) 
			if ((pointIndex >= 0) && boardsGray[pieceIndex][newLocation][pointIndex])
				return false;
		return true;
	}
	
	public void updateUniversalTakeble() {
		for (int pieceIndex = 0; pieceIndex < nPieces; pieceIndex++) {
			char piece = pieces[pieceIndex];
			for (int location = 0; location < nPoints; location++)
				for (int takeble = 0; takeble < nPoints; takeble++) 
					if ((takeble != location) && canPieceTakePoint(piece, location, takeble))
						boardsGray[pieceIndex][location][takeble] = true;
			}
	}

	private int[] getFreePoints() {
		int[] res = new int[nPoints];
		Arrays.fill(res, EMPTY);
		int index = -1;
		for (int p : sortedPoints)
			if (boardFree[p] < 0)
				res[++index] = p;
		return (index == -1) ? null : res;
		//return (index == -1) ? null : Arrays.copyOf(res, (index+1));
	}

	public int getFistUnpositionedIndex() {
		for (int index = 0; index < nPieces; index++)
			if (piecesPoints[index] < 0)
				return index;
		return EMPTY;
	}

	public boolean arrangeRecursively(){
		//recursiveCallCounter++;
		if (isArranged())
			return true;

		int pieceIndex = getFistUnpositionedIndex();
		
		int[] free = getFreePoints();
		
		if (free == null)
			return false;
		
		for (int pointIndex : free) {
			if (pointIndex < 0)
				break;
			if ((boardFree[pointIndex] >= 0))
				continue;
			if (isItSafeToPutPieceAtPoint(pieceIndex, pointIndex)) {
				setPiecePosition(pieceIndex, pointIndex);
				if (isArranged() || arrangeRecursively()) {
					variantsCounter++;
					MicroBoard b = new MicroBoard(this);
					if (uniqueVariants.add(b)) {
						uniqueCounter++;
						//if ((uniqueCounter % 500000) == 0) System.out.println("N unique = " + uniqueCounter + " from " + variantsCounter + " variants, " + (System.currentTimeMillis() - recursionStart) + " ms");
					}
				}
			}
			clearBoardPoint(pointIndex);
		}
		return isArranged();				
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
	
	public boolean isArranged() {
		return puttedPiecesCounter == nPieces;
	}
	private void clearBoardPoint(int pointIndex, int oldPieceIndex) {
		puttedPiecesCounter--;
		piecesPoints[oldPieceIndex] = EMPTY;
		boardFree[pointIndex]--;
		for (int index = 0; index < nPoints; index++)
			if (boardsGray[oldPieceIndex][pointIndex][index]) {
				boardFree[index]--;
				boardTakeble[index]--;
			}
		freePointsCounter++;
	}
	private void clearBoardPoint(int pointIndex) {
		//clearPointCounter++;
		if (boardPiecesIndexes[pointIndex] >= 0) {
			clearBoardPoint(pointIndex, boardPiecesIndexes[pointIndex]);
		}
		boardPiecesNames[pointIndex] = NONAME;
		boardPiecesIndexes[pointIndex] = EMPTY;
		
	}
	
	public void setPiecePosition(int pieceIndex, int pointIndex) {
		//setPositionCounter++;
		clearBoardPoint(pointIndex);
		boardPiecesNames[pointIndex] = pieces[pieceIndex];
		boardPiecesIndexes[pointIndex] = pieceIndex;
		piecesPoints[pieceIndex] = pointIndex;
		puttedPiecesCounter++;
		boardFree[pointIndex]++;
		for (int index = 0; index < nPoints; index++)
			if (boardsGray[pieceIndex][pointIndex][index]) {
				boardFree[index]++;
				boardTakeble[index]++;
			}
		freePointsCounter--;
	}
	
	public int[][] getBoardFree() {
		int[][] b = new int[xSize][ySize];
		for (int pointIndex = 0; pointIndex < nPoints; pointIndex++) {
			boolean empty = (boardPiecesIndexes[pointIndex] < 0);
			boolean notTakeble;
			notTakeble = boardTakeble[pointIndex] == EMPTY;
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
	
	public char[][] getBoardView() {
		char[][] b = new char[xSize][ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				int pointIndex = getIndexForPoint(x, y);
				int index = boardPiecesIndexes[pointIndex];
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
				int index = boardPiecesIndexes[pointIndex];
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
	
	public void drawTakebleForPiece(int pieceIndex, int pointIndex) {
		boolean[] takeble = Arrays.copyOf(boardsGray[pieceIndex][pointIndex], nPoints);
		int[][] b = getBoardFromLinear(takeble);
		String res = getArrayAsString(b);
		System.out.println(res);
	}	
    /**
     * Draw a board into System.out
     */
    public void drawBoard() {
    	System.out.println(getArrayAsString(getBoardView()));
    }
    public void drawTakeble() {
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

    public String getSortedPointsAsString() {
    	String res = "";
    	for (int index : sortedPoints) {
    		int[] p = getPointForIndex(index);
    		res += "(" + p[0] + ", " + p[1] + ") ";
    	}
    	return res;
    }
    public String getSortedPointsIndexesAsString() {
    	String res = "";
    	for (int index : sortedPoints) {
    		res += "" + index + " ";
    	}
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

}
