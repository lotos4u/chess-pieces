package com.lotos4u.test.chess;


public class ChessBoard {
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
	public static final char EMPTY = ' ';
	
	private char[][] board;
	private char[] piecesInitial;
	private char[][][] piecesPutted;
	private int xSize;
	private int ySize;
	private int nPieces;
	
	private void clearPoint(int x, int y) {
		board[x][y] = EMPTY;
	}
	
	public ChessBoard(int x, int y, char[] pieces) {
		xSize = x;
		ySize = y;
		nPieces = pieces.length;
		board = new char[x][y];
		piecesInitial = new char[nPieces];
		piecesPutted = new char[nPieces][x][y];
		for (int i = 0; i < nPieces; i++) {
			piecesInitial[i] = pieces[i];
		}
	}
	
	public char[][][] getPiecesPutted() {
		return piecesPutted;
	}
	
	public void dropPieces() {
		for (int x = 0; x < xSize; x++) {
			for (int y = 0; y < ySize; y++) {
				clearPoint(x, y);
				board[x][y] = ' ';
				for (int index = 0; index < nPieces; index++) {
					piecesPutted[index][x][y] = ' ';
				}
			}
		}
	}
	
	private boolean isEmpty(int x, int y) {
		return board[x][y] == EMPTY;
	}
	public boolean isAnyPieceAtPoint(int x, int y) {
		return (board[x][y] == KING) || 
			   (board[x][y] == KNIGHT) || 
			   (board[x][y] == ROOK) ||
			   (board[x][y] == BISHOP) ||
			   (board[x][y] == QUEEN);
	}
	public boolean isAnyPieceTakePoint(int x, int y) {
		return (board[x][y] == KING_TAKES) || 
			   (board[x][y] == KNIGHT_TAKES) || 
			   (board[x][y] == ROOK_TAKES) ||
			   (board[x][y] == BISHOP_TAKES) ||
			   (board[x][y] == QUEEN_TAKES);
	}
	public int getPieceIndexAtPoint(int x, int y) {
		if (!isEmpty(x, y))
			return -1;
		for (int i = 0; i < piecesPutted.length; i++) {
			if (piecesPutted[i][x][y] == ) 
		}
	}
	public void setPiecePosition(int pieceIndex, int x, int y) {
		if (board[x][y] == EMPTY) {
			piecesPutted[pieceIndex][x][y] = piecesInitial[pieceIndex];
			board[x][y] = piecesInitial[pieceIndex];
			updateTakeblePointsForPiece(pieceIndex, x, y);
		} else {
			piecesPutted[pieceIndex][x][y] = piecesInitial[pieceIndex];
			board[x][y] = piecesInitial[pieceIndex];
			updateTakeblePointsForPiece(pieceIndex, x, y);
			
		}
		
	}
	
	private boolean isBishop(int pieceIndex) {
		return BISHOP == piecesInitial[pieceIndex];
	}

	private boolean isRook(int pieceIndex) {
		return ROOK == piecesInitial[pieceIndex];
	}

	private boolean isKing(int pieceIndex) {
		return KING == piecesInitial[pieceIndex];
	}

	private boolean isQueen(int pieceIndex) {
		return QUEEN == piecesInitial[pieceIndex];
	}
	private boolean isKnight(int pieceIndex) {
		return KNIGHT == piecesInitial[pieceIndex];
	}
	
	private void updateTakeblePointsForPiece(int pieceIndex, int x, int y) {
		if (isBishop(pieceIndex))
			updateTakeblePointsForBishop(pieceIndex, x, y);
		if (isRook(pieceIndex))
			updateTakeblePointsForRook(pieceIndex, x, y);	
		if (isKing(pieceIndex))
			updateTakeblePointsForKing(pieceIndex, x, y);
		if (isKnight(pieceIndex))
			updateTakeblePointsForKnight(pieceIndex, x, y);
		if (isQueen(pieceIndex))
			updateTakeblePointsForQueen(pieceIndex, x, y);		
	}
	
	
	private boolean canBishopTakePoint(int hisX, int hisY, int testX, int testY) {
		return (Math.abs(hisY - testY) == Math.abs(hisX - testX));
	}
	private boolean canRookTakePoint(int hisX, int hisY, int testX, int testY) {
		return (hisY == testY) || (hisX == testX);
	}
	private boolean canKingTakePoint(int hisX, int hisY, int testX, int testY) {
		return (Math.abs(hisY - testY) <= 1) && (Math.abs(hisX - testX) <= 1);
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
		return (Math.abs(hisY - testY) == Math.abs(hisX - testX)) ||
				(hisY == testY) || (hisX == testX);
	}
	
	private void updateTakeblePointsForBishop(int pieceIndex, int x, int y) {
		for (int testX = 0; testX < piecesPutted[pieceIndex].length; testX++) {
			for (int testY = 0; testY < piecesPutted[pieceIndex][testX].length; testY++) {
				if (canBishopTakePoint(x, y, testX, testY))
					piecesPutted[pieceIndex][testX][testY] = BISHOP_TAKES;
			}
		}
	}
	private void updateTakeblePointsForRook(int pieceIndex, int x, int y) {
		
	}
	private void updateTakeblePointsForKing(int pieceIndex, int x, int y) {
		
	}
	private void updateTakeblePointsForKnight(int pieceIndex, int x, int y) {
		
	}
	private void updateTakeblePointsForQueen(int pieceIndex, int x, int y) {
		
	}
	
	private boolean doesPieceCanTakePoint(int pieceIndex, int x, int y) {
		if(piecesPutted[pieceIndex][x][y] == EMPTY)
			return true;
		return false;
	}
    /**
     * Draw a board into System.out
     */
    public void draw() {
    	System.out.println(getArrayAsString(board));
    }
    public void drawPutted(int pieceIndex) {
    	System.out.println(getArrayAsString(piecesPutted[pieceIndex]));
    }
    
    private String getArrayAsString(char[][] input) {
    	int xMax = input.length;
    	int yMax = (xMax > 0) ? input[0].length : 0;
    	String res = "  ";
    	for (int y = 1; y <= yMax; y++) {
    		res += (" " + y + " ");
    	}
    	res += "\n";
    	for (int x = 1; x <= xMax; x++) {
    		String num = "" + x;
    		num = (x < 10) ? " " + num : num;
    		res += num;
    		for (int y = 1; y <= yMax; y++) {
    			res += ("[" + input[x-1][y-1] + "]");
    		}
    		res += "\n";
    	}
    	return res;
    }
    
    public boolean isArrangeEquals(ChessBoard b) {
    	if (!isSameGame(b))
    		return false;
    	for (int x = 0; x < board.length; x++)
    		for (int y = 0; y < board[x].length; y++) {
    			if (b.board[x][y] != board[x][y])
    				return false;
    		}	
    	return true;
    }
    
    public boolean isSameGame(ChessBoard b) {
    	return (b.xSize == xSize) && 
    			(b.ySize == ySize) &&
    			(b.nPieces == nPieces);
    }
    
	public static void main(String arg[]) {
		char[] pcs = new char[]{BISHOP, KING, ROOK};
		ChessBoard board1 = new ChessBoard(3, 3, pcs);
//		board1.draw();
		board1.setPiecePosition(0, 1, 1);
		//board1.setPiecePosition(1, 1, 1);
		board1.setPiecePosition(2, 2, 2);
		board1.draw();
		
		ChessBoard board2 = new ChessBoard(3, 3, pcs);
		board2.setPiecePosition(0, 0, 0);
		board2.setPiecePosition(1, 1, 1);
		board2.setPiecePosition(2, 2, 2);
		board2.draw();
		board2.dropPieces();
		board2.draw();
		
		System.out.println("Board arranged equals = " + board1.isArrangeEquals(board2));
		
		
		board1.drawPutted(0);
		
	}
}
