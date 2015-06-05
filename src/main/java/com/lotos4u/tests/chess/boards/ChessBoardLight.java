package com.lotos4u.tests.chess.boards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

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
	
	public static boolean sortPoints = false;
	
	protected boolean logExtraResults;
	
	protected int rc;
	
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
		for (int pointIndex = 0; pointIndex < nPoints; pointIndex++) {
			sortedComparablePoints.add(new ChessBoardLight.ComparablePoint(pointIndex));
			for (int p = 0; p < nPieces; p++)
				Arrays.fill(boardsGray[p][pointIndex], false);
		}
		
		if (sortPoints)
			Collections.sort(sortedComparablePoints);
		
		for (int i = 0; i < nPoints; i++)
			sortedPoints[i] = sortedComparablePoints.get(i).index;
		
		//System.out.println(Arrays.toString(piecesPoints));
		//Arrays.sort(piecesPoints);
		//System.out.println(Arrays.toString(piecesPoints));
		//System.out.println(Arrays.binarySearch(piecesPoints, -1));
		//Log.pause();
		//ArrayUtils.
		//ArrayUtils.clone(boardPiecesNames)
		
	}
	
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
		rc = 0;
		
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
		updateBoardsGray();
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
		for (int pointIndex : piecesPoints) 
			if ((pointIndex > -1) && boardsGray[pieceIndex][newLocation][pointIndex])
				return false;
		return true;
	}
	
	private void updateBoardsGray() {
		for (int pieceIndex = 0; pieceIndex < nPieces; pieceIndex++) {
			char piece = pieces[pieceIndex];
			for (int location = 0; location < nPoints; location++)
				for (int takeble = 0; takeble < nPoints; takeble++) 
					boardsGray[pieceIndex][location][takeble] = (takeble != location) && canPieceTakePoint(piece, location, takeble);
		}
	}
	private int[] getFreePoints(int[] input) {
		int[] free = new int[freePointsCounter];
		int index = 0;
		for (int p : input)
			if (boardFreeAndSafe[p] < 0)
				free[index++] = p;
		return free;
	}

	private int[] getFreePoints() {
		return getFreePoints(sortedPoints);
	}

	public int getFistUnpositionedIndex() {
		return ArrayUtils.indexOf(piecesPoints, EMPTY);
		//for (int index = 0; index < nPieces; index++)
		//	if (piecesPoints[index] < 0)
		//		return index;
		//return EMPTY;
	}
	protected void updateCompactPieces() {
		int index = 0;
		for (byte i = 0; i < nPoints; i++)
			if (boardPiecesNames[i] != NONAME) {
				compactPieces[index] = boardPiecesNames[i];
				compactPieces[(index++)+nPieces] = (char)(i+65);
			}
		///System.out.println(Arrays.toString(compactPieces));drawBoard();Log.pause();
		
		/*
		for (byte i = 0; i < nPoints; i++) {
			if (boardPiecesNames[i] != NONAME) {
				compactPieces[index] = boardPiecesNames[i];
				compactPieces[index+nPieces] = (char)i;
				index++;
			}
		}*/
	}

	
	protected Object getCompactPiecesObject() {
		//updateCompactPieces();
		//return new MicroChessBoard(this);
		//return new String(compactPieces);
		//return new String(boardPiecesNames);
		return String.valueOf(boardPiecesNames);
		//return String.copyValueOf(boardPiecesNames);
		//MicroChessBoard b = new MicroChessBoard(this);
	}

	public boolean arrangeRecursively(){
		if (puttedPiecesCounter == nPieces) 
			return true;

		int pieceIndex = getFistUnpositionedIndex();
		
		if (freePointsCounter < 1)
			return false;
		
		int[] free = getFreePoints();
		
		//int pointIndex = EMPTY;
		//int oldPointIndex = EMPTY;
		for (int pointIndex : free) {
		//int[] free = ArrayUtils.clone(boardFreeAndSafe);
		//while (freePointsCounter > 0) {
			//pointIndex = ArrayUtils.indexOf(free, EMPTY);
			//if(pointIndex < 0)	{
				//rc--;
				//System.out.println("[" + rc + "] BACK point="+pointIndex + ", freePointsCounter=" + freePointsCounter);drawBoard();Log.pause();
				//return (puttedPiecesCounter == nPieces);
			//}
			//free[pointIndex]++;
			//System.out.println("[" + rc + "] point="+pointIndex + ", freePointsCounter=" + freePointsCounter);drawBoard(); Log.pause();
			
			if (isItSafeToPutPieceAtPoint(pieceIndex, pointIndex)) {
				setPiecePosition(pieceIndex, pointIndex);
				if ((puttedPiecesCounter == nPieces) || arrangeRecursively()) {
					//variantsCounter++;
					//System.out.println("[" + rc + "] !!!!!!!!");drawBoard(); Log.pause();
					if (uniqueVariants.add(getCompactPiecesObject())) {
						//uniqueCounter++;
						//if (logExtraResults && ((uniqueCounter % 500000) == 0)) System.out.println("N unique = " + uniqueCounter + " from " + variantsCounter + " variants, Hash=" + MicroChessBoard.hashCounter + ", Equals=" + MicroChessBoard.equalsCounter + ", " + (System.currentTimeMillis() - recursionStart) + " ms");
					}
				}
				clearBoardPoint(pointIndex);
			}
			//boardFreeAndSafe[pointIndex]++;
//			oldPointIndex = pointIndex;
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
			piecesPoints[oldPieceIndex] = EMPTY;
			boardPiecesNames[pointIndex] = NONAME;
			boardPiecesIndexes[pointIndex] = EMPTY;
			puttedPiecesCounter--;
			boardFreeAndSafe[pointIndex]--;
			freePointsCounter++;
			for (int index = 0; index < nPoints; index++)
				if (boardsGray[oldPieceIndex][pointIndex][index])
					if(--boardFreeAndSafe[index] < 0)
						freePointsCounter++;
		}
	}

	public void setPiecePosition(int pieceIndex, int newPointIndex) {
		boardPiecesNames[newPointIndex] = pieces[pieceIndex];
		boardPiecesIndexes[newPointIndex] = pieceIndex;
		piecesPoints[pieceIndex] = newPointIndex;
		puttedPiecesCounter++;
		boardFreeAndSafe[newPointIndex]++;
		freePointsCounter--;
		for (int index = 0; index < nPoints; index++)
			if (boardsGray[pieceIndex][newPointIndex][index])
				if (boardFreeAndSafe[index]++ == -1)
					freePointsCounter--;
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
    public void drawNeighbors() {
    	System.out.println(getArrayAsString(getBoardFromLinear(pointsNeighbors)));	
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
