package com.lotos4u.tests.chess.light;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

	private Set<ChessBoardLight> variants = new HashSet<ChessBoardLight>();
	
	private char[][] boardView; //Letters (pieces names) at the points
	private int[][] boardWork; //numbers (pieces indexes) at the points
	private char[] pieces; //all pieces names
	private int[] piecesPoints;
	private int xSize;
	private int ySize;
	private int nPieces;
	
	private long recursionStart;
	private int totalCounter;
	private int rc;
	private int callCounter;

	public ChessBoardLight(ChessBoardLight input) {
		copyFromBoard(input);
	}
	
	private void copyFromBoard(ChessBoardLight input) {
		xSize = input.xSize;
		ySize = input.ySize;
		nPieces = input.nPieces;
		boardView = new char[xSize][ySize];
		boardWork = new int[xSize][ySize];
		pieces = new char[nPieces];
		piecesPoints = new int[nPieces];
		for (int i = 0; i < nPieces; i++) {
			pieces[i] = input.pieces[i];
			piecesPoints[i] = piecesPoints[i];
		}
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				boardView[i][j] = input.boardView[i][j];
				boardWork[i][j] = input.boardWork[i][j];
			}
		}		
	}
	
	public ChessBoardLight(int x, int y, char[] inPieces) {
		xSize = x;
		ySize = y;
		nPieces = inPieces.length;
		boardView = new char[x][y];
		boardWork = new int[x][y];
		pieces = new char[nPieces];
		piecesPoints = new int[nPieces];
		for (int i = 0; i < nPieces; i++) {
			pieces[i] = inPieces[i];
			piecesPoints[i] = EMPTY;
		}
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				boardView[i][j] = NONAME;
				boardWork[i][j] = EMPTY;
			}
		}
	}
	
	
	public Set<ChessBoardLight> getVariants() {
		return variants;
	}

	public int getArrangementVariants() {
		recursionStart = System.currentTimeMillis();
		callCounter = 0;
		arrangeRecursively();
		long end = System.currentTimeMillis();
		System.out.println("It takes " + (end-recursionStart) + " ms");
		System.out.println("Number of variants = " + variants.size());
		System.out.println("Total counter = " + totalCounter);
		return variants.size();		
	}
	
	public boolean tryToPut(int pieceIndex, int x, int y, boolean log) {
		boolean res = false;
		setPiecePosition(pieceIndex, x, y);
		res = isValid();
		if (!res) {
			if(log)System.out.println("[" + rc + "] Not Putted #" + pieceIndex);
			dropPiece(pieceIndex);
		} else {
			if(log)System.out.println("[" + rc + "] Putted #" + pieceIndex);
		}
		return res;
	}
	
	public List<Integer> getFreePointsIndexes() {
		List<Integer> res = new ArrayList<Integer>();
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				if (isEmpty(x, y))
					res.add(getIndexForPoint(x, y));
			}
		return res;
	}
	
	public int getFistUnpositionedIndex() {
		for (int index = 0; index < nPieces; index++) {
			if (piecesPoints[index] == EMPTY) {
				return index;
			}
		}
		return EMPTY;
	}
	
	public boolean arrangeRecursively(){
		callCounter++;
		rc++;
		boolean res = false;
		boolean putted = false;
		boolean log = false;
		boolean logInner = false;
		boolean logExtra = true;
		if (logExtra) System.out.println("N = " + variants.size() + ", counter = " + callCounter );
		if (isArranged()) {
			rc--;
			return true;
		}
		int pieceIndex = getFistUnpositionedIndex();
		char piece = pieces[pieceIndex];
		
		List<Integer> free = getFreePointsIndexes();
		if (free.size() < 1) {
			if (log) System.out.println("[" + rc + "] No free points!");
			rc--;
			return false;
		}
		if (log) System.out.println("[" + rc + "] We have " + free.size() + " points for " + piece);
		if (log) System.out.println("[" + rc + "] Pieces:" + getPiecesAsString());
		if (log) System.out.println("[" + rc + "] Free:" + free);
		
		for (Integer index : free) {
			int[] arr = getPointForIndex(index);
			int x = arr[0];
			int y = arr[1];
			res = false;
			if (log)System.out.println("[" + rc + "] Try " + piece + " at (" + x + ", " + y + ")");
			putted = tryToPut(pieceIndex, x, y, logInner);
			if (putted) {
				res = isArranged() || arrangeRecursively();
				if (res) { //Arrangement successful
					variants.add(new ChessBoardLight(this));
					totalCounter++;
				} else { //Arrangement unsuccessful
					
				}
			}
			dropPiece(pieceIndex);
		}
		
		rc--;
		res = isArranged(); 
		return res;				
	}
	public int getIndexForPoint(int x, int y) {
		return x*ySize + y;
	}
	
	public int[] getPointForIndex(int index) {
		int y = index % ySize;
		int x = (index - y)/ySize;
		return new int[] {x, y};
	}
	
	private void clearBoardPoint(int x, int y) {
		int pieceIndex = boardWork[x][y];
		if (pieceIndex != EMPTY)
			piecesPoints[pieceIndex] = EMPTY;
		boardView[x][y] = NONAME;
		boardWork[x][y] = EMPTY;
	}
	
	public void dropPieces() {
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				clearBoardPoint(x, y);
			}
		}
	}

	public boolean isArrangedAndValid() {
		return isArranged() && isValid();
	}
	
	public boolean canPieceTakePoint(char piece, int hisX, int hisY, int testX, int testY) {
		if ((isBishop(piece) && canBishopTakePoint(hisX, hisY, testX, testY)) ||
				   (isKing(piece) && canKingTakePoint(hisX, hisY, testX, testY)) ||
				   (isKnight(piece) && canKnightTakePoint(hisX, hisY, testX, testY)) ||
				   (isRook(piece) && canRookTakePoint(hisX, hisY, testX, testY)) ||
				   (isQueen(piece) && canQueenTakePoint(hisX, hisY, testX, testY))) {
					return true;
					}	
		return false;
	}
	
	private boolean pieceCanTakePiece(int p1, int p2) {
		int pointIndex1 = piecesPoints[p1];
		if (pointIndex1 == EMPTY)
			return false;
		int pointIndex2 = piecesPoints[p2];
		if (pointIndex2 == EMPTY)
			return false;
		char piece1 = pieces[p1];
		int[] point1 = getPointForIndex(pointIndex1);
		int x1 = point1[0];
		int y1 = point1[1];
		int[] point2 = getPointForIndex(pointIndex2);
		int x2 = point2[0];
		int y2 = point2[1];
		return canPieceTakePoint(piece1, x1, y1, x2, y2);
	}
	
	public boolean isPutted(int pieceIndex) {
		return piecesPoints[pieceIndex] != EMPTY;
	}
	public boolean isValid() {
		for (int p1 = 0; p1 < nPieces; p1++) {
			if (isPutted(p1))
				for (int p2 = 0; p2 < nPieces; p2++) {
					if (isPutted(p2) && pieceCanTakePiece(p1, p2))
						return false;
				}
		}
		return true;
	}
	
	public boolean isArranged() {
		for (int i = 0; i < nPieces; i++)
			if (piecesPoints[i] == EMPTY)
				return false;
		return true;
	}

	public void dropPiece(int pieceIndex) {
		int pointIndex = piecesPoints[pieceIndex];
		if (pointIndex != EMPTY) {
			int[] point = getPointForIndex(pointIndex);
			clearBoardPoint(point[0], point[1]);	
		}
	}
	
	public void dropPiece(int pieceIndex, int oldX, int oldY) {
		clearBoardPoint(oldX, oldY);
	}
	
	private boolean isEmpty(int x, int y) {
		return boardView[x][y] == NONAME;
	}
	public boolean isAnyPiece(char c) {
		return (c == KING) || 
			   (c == KNIGHT) || 
			   (c == ROOK) ||
			   (c == BISHOP) ||
			   (c == QUEEN);
	}
	public boolean isAnyTakeble(char c) {
		return (c == KING_TAKES) || 
			   (c == KNIGHT_TAKES) || 
			   (c == ROOK_TAKES) ||
			   (c == BISHOP_TAKES) ||
			   (c == QUEEN_TAKES);
	}
	public boolean isAnyPieceAtPoint(int x, int y) {
		return boardWork[x][y] != EMPTY;
	}

	public void setPiecePosition(int pieceIndex, int x, int y) {
		clearBoardPoint(x, y);
		boardView[x][y] = pieces[pieceIndex];
		boardWork[x][y] = pieceIndex;
		piecesPoints[pieceIndex] = getIndexForPoint(x, y);
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
		return ((testX == hisX - 1) && (testY == hisY - 2)) ||
			   ((testX == hisX + 1) && (testY == hisY - 2)) ||
			   ((testX == hisX - 2) && (testY == hisY - 1)) ||
			   ((testX == hisX - 2) && (testY == hisY + 1)) ||
			   ((testX == hisX - 1) && (testY == hisY + 2)) ||
			   ((testX == hisX + 1) && (testY == hisY + 2)) ||
			   ((testX == hisX + 2) && (testY == hisY - 1)) ||
			   ((testX == hisX + 2) && (testY == hisY + 1));		
	}
	private boolean canQueenTakePoint(int hisX, int hisY, int testX, int testY) {
		return canRookTakePoint(hisX, hisY, testX, testY) ||
				canBishopTakePoint(hisX, hisY, testX, testY);
	}
	
    /**
     * Draw a board into System.out
     */
    public void draw() {
    	System.out.println(getArrayAsString(boardView));
    }
    
    private String getArrayAsString(char[][] input) {
    	int xMax = input.length;
    	int yMax = (xMax > 0) ? input[0].length : 0;
    	String res = "  ";
    	for (int y = 0; y < yMax; y++) {
    		res += (" " + y + " ");
    	}
    	res += "\n";
    	for (int x = 0; x < xMax; x++) {
    		String num = "" + x;
    		num = (x < 10) ? " " + num : num;
    		res += num;
    		for (int y = 0; y < yMax; y++) {
    			res += ("[" + input[x][y] + "]");
    		}
    		res += "\n";
    	}
    	return res;
    }
    public String getPiecesAsString() {
    	String res = "";
    	for (int i = 0; i < nPieces; i++) {
    		res += pieces[i] + "(" + piecesPoints[i] + ") ";
		}
    	return res;
    }
    
    public boolean isArrangeEquals(ChessBoardLight b) {
    	if (isDifferentGame(b))
    		return false;
    	for (int x = 0; x < xSize; x++)
    		for (int y = 0; y < ySize; y++) {
    			if (b.boardView[x][y] != boardView[x][y])
    				return false;
    		}	
    	return true;
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
    
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChessBoardLight))
			return false;
		ChessBoardLight b = (ChessBoardLight) obj;
		if (!isArrangeEquals(b))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + xSize;
        result = prime * result + ySize;
        result = prime * result + nPieces;
        return result;
    }
}
