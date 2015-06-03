package com.lotos4u.tests.chess.boards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractChessBoard {
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

	public static final int KING_POWER = 20;
	public static final int QUEEN_POWER = 100;
	public static final int KNIGHT_POWER = 40;
	public static final int ROOK_POWER = 80;
	public static final int BISHOP_POWER = 60;
	
	public static final char NONAME = ' ';
	public static final int EMPTY = -1;	
	protected int xSize;
	protected int ySize;
	protected int nPoints;
	protected int nPieces;
	protected char[] boardPiecesNames;
	
	public static boolean updateEqualsCounter = false;
	public static boolean updateHashCounter = false;
	
	public static int equalsCounter = 0;
	public static int hashCounter = 0;
	
	/*
	
	public int getXSize();
	public int getYSize();
	public int getNPoints();
	public int getNPieces();
	
	public char[] getBoardViewAsArray();
	 * */
	public AbstractChessBoard(int x, int y) {
		xSize = x;
		ySize = y;
		nPoints = x*y;
		boardPiecesNames = new char[nPoints];
		Arrays.fill(boardPiecesNames, NONAME);
	}
	
	public int getNPieces() {
		return nPieces;
	}	
	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}
	
	public int getNPoints() {
		return nPoints;
	}

	public int[] getPointForIndex(int index) {
		int x = index / ySize;
		int y = index - x*ySize;
		return new int[]{x, y};
	}
	
	public int getIndexForPoint(int x, int y) {
		return y + ySize*x;//=index
	}
	protected char getPieceForWeak(int weak) {
		return getPieceForPower(QUEEN_POWER - weak);
	}
	protected boolean isPointInside(int x, int y) {
		return ((x >= 0) && (x < xSize) && (y >= 0) && (y < ySize));
	}	
	protected char getPieceForPower(int power) {
		if (power == QUEEN_POWER) 
			return QUEEN;
		else
			if (power == ROOK_POWER)
				return ROOK;
			else
				if (power == BISHOP_POWER)
					return BISHOP;
				else
					if (power == KNIGHT_POWER)
						return KNIGHT;
					else
						return KING;		
	}
	protected int getWeakForPower(int power) {
		return QUEEN_POWER - power;
	}
	protected int getPowerForWeak(int weak) {
		return QUEEN_POWER - weak;
	}
	protected int getWeakForPiece(char piece) {
		return getWeakForPower(getPowerForPiece(piece));
	}
	protected int getPowerForPiece(char piece) {
		if (isQueen(piece)) 
			return QUEEN_POWER;
		else
			if (isRook(piece))
				return ROOK_POWER;
			else
				if (isBishop(piece))
					return BISHOP_POWER;
				else
					if (isKnight(piece))
						return KNIGHT_POWER;
					else
						return KING_POWER;
	}
	protected boolean isBishop(char pieceType) {
		return BISHOP == pieceType;
	}
	protected boolean isRook(char pieceType) {
		return ROOK == pieceType;
	}
	protected boolean isKing(char pieceType) {
		return KING == pieceType;
	}
	protected boolean isQueen(char pieceType) {
		return QUEEN == pieceType;
	}
	protected boolean isKnight(char pieceType) {
		return KNIGHT == pieceType;
	}
	
	protected boolean canBishopTakePoint(int hisX, int hisY, int testX, int testY) {
		int diffX = Math.abs(hisX - testX);
		int diffY = Math.abs(hisY - testY);
		return (diffY == diffX) && (diffX > 0);
	}
	protected boolean canRookTakePoint(int hisX, int hisY, int testX, int testY) {
		return (hisY == testY) ^ (hisX == testX);
	}
	protected boolean canKingTakePoint(int hisX, int hisY, int testX, int testY) {
		int diffX = Math.abs(hisX - testX);
		int diffY = Math.abs(hisY - testY);
		return (diffY <= 1) && (diffX <= 1) && ((diffX + diffY) > 0);
	}
	protected boolean canKnightTakePoint(int hisX, int hisY, int testX, int testY) {
		int diffX = Math.abs(hisX - testX);
		int diffY = Math.abs(hisY - testY);
		return ((diffX == 1) && (diffY == 2)) || ((diffX == 2) && (diffY == 1));
	}
	protected boolean canQueenTakePoint(int hisX, int hisY, int testX, int testY) {
		return canRookTakePoint(hisX, hisY, testX, testY) ||
				canBishopTakePoint(hisX, hisY, testX, testY);
	}	
	public int[][] getBoardFromLinear(boolean[] input) {
		int[][] b = new int[xSize][ySize];
		for (int i = 0; i < xSize; i++)
			Arrays.fill(b[i], EMPTY);
		for (int index = 0; index < nPoints; index++)
			if (input[index]) {
				int[] point = getPointForIndex(index);
				int x = point[0];
				int y = point[1];
				b[x][y] = 1;
			}
		return b;
	}

	public int[][] getBoardFromLinear(int[] input) {
		int[][] b = new int[xSize][ySize];
		for (int i = 0; i < xSize; i++)
			Arrays.fill(b[i], EMPTY);
		for (int index = 0; index < nPoints; index++)
			if (input[index] != EMPTY) {
				int[] point = getPointForIndex(index);
				int x = point[0];
				int y = point[1];
				b[x][y] = input[index];
			}
		return b;
	}
	
	public char[][] getBoardView() {
		updateBoardView();
		char[][] b = new char[xSize][ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				int pointIndex = getIndexForPoint(x, y);
				b[x][y] = boardPiecesNames[pointIndex];
			}
		return b;
	}
	
    protected String getArrayAsString(int[][] input) {
    	String res = "";
    	List<String> pre = getArrayAsStrings(input);
    	for (Iterator<String> iterator = pre.iterator(); iterator.hasNext();) {
			 res += (iterator.next() + "\n");
		}
    	return res;
    }
    
    protected String getArrayAsString(char[][] input) {
    	String res = "";
    	List<String> pre = getArrayAsStrings(input);
    	for (Iterator<String> iterator = pre.iterator(); iterator.hasNext();) {
			 res += (iterator.next() + "\n");
		}
    	return res;
    }
    
    protected List<String> getArrayAsStrings(int[][] input) {
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
    
    protected List<String> getArrayAsStrings(char[][] input) {
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
    
    public String getBoardViewAsString() {
    	return getArrayAsString(getBoardView());
    }
    
    public void draw() {
    	System.out.println(getBoardViewAsString());
    }

    public boolean isSameGame(AbstractChessBoard b) {
    	return (b.getXSize() == xSize) && 
    			(b.getYSize() == ySize) &&
    			(b.nPieces == nPieces);
    }

    public boolean isDifferentGame(AbstractChessBoard b) {
    	return (b.getXSize() != xSize) || 
    			(b.getYSize() != ySize) ||
    			(b.nPieces != nPieces);
    }

    public boolean isArrangeEquals(AbstractChessBoard b) {
    	if (isDifferentGame(b))
    		return false;
    	return Arrays.equals(boardPiecesNames, b.boardPiecesNames);
    }   
    
	public void updateBoardView() {}

	public char[] getBoardViewAsArray() {
		return boardPiecesNames;
	}
}
