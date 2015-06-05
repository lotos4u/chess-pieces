package com.lotos4u.tests.chess.boards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.lotos4u.tests.chess.general.Log;


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

	//protected Set<MicroChessBoard> uniqueVariants = new HashSet<MicroChessBoard>();
	protected Set<Object> uniqueVariants = new HashSet<Object>();
	protected List<Object> allVariants = new LinkedList<Object>();
	
	protected int[] boardPiecesIndexes; //numbers (pieces indexes) at the points
	protected int[] boardFreeAndSafe; //numbers (pieces indexes) at the points
	protected int[] pointsNeighbors; //Number of Neighbors for each point
	protected boolean[][][] boardsGray;
	protected char[] pieces; //all pieces names
	protected int[] piecesPoints;
	protected int[] sortedPoints;
	//protected byte[] compactPieces;
	protected char[] compactPieces;
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
	
	public static boolean sortPoints = true;
	
	
	protected boolean logExtraResults;
	
	public ChessBoardLight(int x, int y, char[] inPieces) {
		super(x, y);
		nPieces = inPieces.length;
		boardPiecesIndexes = new int[nPoints];
		pointsNeighbors = new int[nPoints];
		boardFreeAndSafe = new int[nPoints];
		sortedPoints = new int[nPoints];
		boardsGray = new boolean[nPieces][nPoints][nPoints];
		pieces = new char[nPieces];
		//compactPieces = new byte[nPieces*2];
		compactPieces = new char[nPieces*2]; //names[0] and points[1]
		piecesPoints = new int[nPieces];
		Arrays.fill(piecesPoints, EMPTY);
		pieces = Arrays.copyOf(inPieces, nPieces);
		sortPieces();
		Arrays.fill(boardPiecesIndexes, EMPTY);
		Arrays.fill(boardFreeAndSafe, EMPTY);

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
	
	//public Set<MicroChessBoard> getUniqueVariants() {
	public Set<Object> getUniqueVariants() {
		return uniqueVariants;
	}

	public List<Object> getAllVariants() {
		return allVariants;
	}

	public int getNeighborsNumber(int pointIndex) {
		int res = 0;
		int[] point = getPointForIndex(pointIndex);
		int x0 = point[0], x1 = x0 - 2, x2 = x0 + 2;
		int y0 = point[1], y1 = y0 - 2, y2 = y0 + 2;
		for (int x = x1; x <= x2; x++)
			for (int y = y1; y <= y2; y++) {
				int diffX = Math.abs(x - x0);
				int diffY = Math.abs(y - y0);
				if (isPointInside(x, y))
					if (Math.max(diffX, diffY) == 1)
						res += 2;
					else
						res += 1;
			}
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
		logExtraResults = inLogExtra;
		if (logSummary) System.out.println("Sorted Pieces: " + getPiecesAsString());
		if (logSummary) System.out.println("Sorted Points: " + getSortedPointsIndexesAsString());
		recursionStart = Log.getMilliSeconds();
		puttedPiecesCounter = 0;
		freePointsCounter = nPoints;
		
		recursiveCallCounter = 0;
		tryToPutCounter = 0;
		variantsCounter = 0;
		uniqueCounter = 0;
		isSafeCallCounter = 0;
		clearPointCounter = 0;
		setPositionCounter = 0;
		
		
		//MicroChessBoard.equalsCounter = 0;
		//MicroChessBoard.hashCounter = 0;	
		uniqueVariants.clear();
		allVariants.clear();
		updateUniversalTakeble();
		if (logSummary) System.out.println("Update takeble time " + (Log.getMilliSeconds() - recursionStart) + " ms");
		arrangeRecursively();
		recursionFinish = Log.getMilliSeconds();
		long recTime = recursionFinish-recursionStart;
		if (logSummary) System.out.println("Number of all variants = " + variantsCounter);
		if (logSummary) System.out.println("Arrangements time " + recTime + " ms");
		if (logSummary) System.out.println("Number of unique variants = " + uniqueCounter);
		if (recursiveCallCounter > 0)
			if (logSummary) System.out.println("Number of recursive calls = " + recursiveCallCounter);
		if (MicroChessBoard.equalsCounter > 0)
			if (logSummary) System.out.println("Number of Equals calls = " + MicroChessBoard.equalsCounter);
		/*
		if (logSummary) System.out.println("Number of isSafe = " + isSafeCallCounter);
		if (logSummary) System.out.println("Number of clearPoint = " + clearPointCounter);
		if (logSummary) System.out.println("Number of setPosition = " + setPositionCounter);
		
		if (logSummary) System.out.println("Number of HashCode calls = " + hashCounter);
		if (recTime > 0) {
			if (logSummary) System.out.println("Number of recursive calls per ms = " + (float)(recursiveCallCounter/recTime));
			if (logSummary) System.out.println("Number of isSafe per ms = " + (float)(isSafeCallCounter/recTime));
			if (logSummary) System.out.println("Number of clearPoint per ms = " + (float)(clearPointCounter/recTime));
			if (logSummary) System.out.println("Number of setPosition per ms = " + (float)(setPositionCounter/recTime));
			if (logSummary) System.out.println("Number of Equals calls per ms = " + (float)(equalsCounter/recTime));
			if (logSummary) System.out.println("Number of HashCode calls per ms = " + (float)(hashCounter/recTime));
		}
*/
		if (logSummary) System.out.println();
		return uniqueVariants.size();		
	}

	private boolean isItSafeToPutPieceAtPoint(int pieceIndex, int newLocation) {
		//isSafeCallCounter++;
		for (int pointIndex : piecesPoints) 
			if ((pointIndex > -1) && boardsGray[pieceIndex][newLocation][pointIndex])
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
		int[] free = new int[freePointsCounter];
		int index = 0;
		for (int p : sortedPoints)
			if (boardFreeAndSafe[p] < 0)
				free[index++] = p;
		return free;
	}

	public int getFistUnpositionedIndex() {
		for (int index = 0; index < nPieces; index++)
			if (piecesPoints[index] < 0)
				return index;
		return EMPTY;
	}
	protected void updateCompactPieces() {
		int index = 0;
		for (byte i = 0; i < nPoints; i++) {
			if (boardPiecesNames[i] != NONAME) {
				compactPieces[index] = boardPiecesNames[i];
				compactPieces[index+nPieces] = (char)i;
				index++;
			}
		}
	}
	
	protected Object getCompactPiecesObject() {
		//updateCompactPieces();
		//return new MicroChessBoard(this);
		//return new String(compactPieces);
		//return new String(boardPiecesNames);
		return String.copyValueOf(boardPiecesNames);
		//MicroChessBoard b = new MicroChessBoard(this);
		//System.out.println("hash=" + b.hashCode());drawBoard();Log.pause();
		
		/*
		String names = "";
		String points = "";
		for (byte i = 0; i < nPoints; i++) {
			if (boardPiecesNames[i] != NONAME) {
				names += boardPiecesNames[i];
				points += i;
				//res = i;zzz
				//index++;
			}
		}
		return names + points;
		*/
	}

	public boolean arrangeRecursively(){
		//recursiveCallCounter++;
		if (puttedPiecesCounter == nPieces)
			return true;

		int pieceIndex = getFistUnpositionedIndex();
		
		if (freePointsCounter < 1)
			return false;
		
		int[] free = getFreePoints();
		
		for (int pointIndex : free) {
			if (isItSafeToPutPieceAtPoint(pieceIndex, pointIndex)) {
				setPiecePosition(pieceIndex, pointIndex);
				if ((puttedPiecesCounter == nPieces) || arrangeRecursively()) {
					variantsCounter++;
					if (uniqueVariants.add(getCompactPiecesObject())) {
						uniqueCounter++;
						//if (logExtraResults && ((uniqueCounter % 500000) == 0)) System.out.println("N unique = " + uniqueCounter + " from " + variantsCounter + " variants, Hash=" + MicroChessBoard.hashCounter + ", Equals=" + MicroChessBoard.equalsCounter + ", " + (System.currentTimeMillis() - recursionStart) + " ms");
					}
				}
				clearBoardPoint(pointIndex);
			}
		}
		return false;
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
	
	private void clearBoardPoint(int pointIndex) {
		int oldPieceIndex = boardPiecesIndexes[pointIndex];
		if (oldPieceIndex > -1) {
			//compactPieces[0][puttedPiecesCounter-1] = NONAME;
			//compactPieces[1][puttedPiecesCounter-1] = EMPTY;
			puttedPiecesCounter--;
			piecesPoints[oldPieceIndex] = EMPTY;
			boardFreeAndSafe[pointIndex]--;
			freePointsCounter++;
			for (int index = 0; index < nPoints; index++)
				if (boardsGray[oldPieceIndex][pointIndex][index]) {
					if(boardFreeAndSafe[index] == 0)
						freePointsCounter++;
					boardFreeAndSafe[index]--;
				}
		}
		boardPiecesNames[pointIndex] = NONAME;
		boardPiecesIndexes[pointIndex] = EMPTY;
	}

	public void setPiecePosition(int pieceIndex, int newPointIndex) {
		//clearBoardPoint(newPointIndex);
		boardPiecesNames[newPointIndex] = pieces[pieceIndex];
		boardPiecesIndexes[newPointIndex] = pieceIndex;
		piecesPoints[pieceIndex] = newPointIndex;
		puttedPiecesCounter++;
		//compactPieces[0][puttedPiecesCounter-1] = pieces[pieceIndex];
		//compactPieces[1][puttedPiecesCounter-1] = newPointIndex;
		boardFreeAndSafe[newPointIndex]++;
		freePointsCounter--;
		//updatePointsForPut(pieceIndex, newPointIndex);

		for (int index = 0; index < nPoints; index++)
			if (boardsGray[pieceIndex][newPointIndex][index]) {
				if (boardFreeAndSafe[index] == -1)
					freePointsCounter--;
				boardFreeAndSafe[index]++;
			}
	
		/*
		int index = 0;
		for (boolean isGray : boardsGray[pieceIndex][newPointIndex]) {
			if (isGray) {
				if (boardFreeAndSafe[index] == -1)
					freePointsCounter--;
				boardFreeAndSafe[index]++;
			}
			index++;
		}
			*/	
	}
	/*
	private int[][] getBoardFree() {
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
	*/
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
	/*
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
	*/
	
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
    public void drawNeighbors() {
    	System.out.println(getArrayAsString(getBoardFromLinear(pointsNeighbors)));	
    }
    /*
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
    */

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
    		res += "" + index + "(" + pointsNeighbors[index] + ") ";
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

	@Override
	protected void updatePiecesNames() {
		// TODO Auto-generated method stub
		
	}

}
