package com.lotos4u.tests.chess.boards;

public interface IChessBoard {
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
	
	public int getXSize();
	public int getYSize();
	public int getNPoints();
	public int getNPieces();
	
	public char[] getBoardViewAsArray();
}
