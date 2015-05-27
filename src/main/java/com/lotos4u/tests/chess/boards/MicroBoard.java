package com.lotos4u.tests.chess.boards;

public class MicroBoard {
	private char[][] board;

	public MicroBoard(int x, int y) {
		super();
		board = new char[x][y];
	}

	public MicroBoard(char[][] input) {
		super();
		board = new char[input.length][input[0].length];
		updateBoard(input);
	}
	
	public MicroBoard(ChessBoard chessBoard) {
		this(chessBoard.getArrayView());
	}

	private boolean isArraysEquals(char[][] b) {
    	for (int i = 0; i < board.length; i++)
    		for (int j = 0; j < board[i].length; j++)
    			if (board[i][j] != b[i][j])
    				return false;
    	return true;
	}
	
	public void updateBoard(char[][] input) {
    	for (int i = 0; i < board.length; i++)
    		for (int j = 0; j < board[i].length; j++)
    			board[i][j] = input[i][j];
	}
    /**
     * Draw a board into System.out
     */
    public String drawToString() {
    	String res = "  ";
    	if (board.length > 0)
    		for (int y = 1; y <= board[0].length; y++) {
    			res += (" " + y + " ");
    		}
    	res += ("\n");
    	for (int x = 1; x <= board.length; x++) {
    		String num = "" + x;
    		num = (x < 10) ? " " + num : num;
    		res += num;
    		for (int y = 1; y <= board[0].length; y++) {
    			String name = String.valueOf(board[x-1][y-1]);
    			res += "[" + name + "]";
    		}
    		res += ("\n");
    	}
    	return res;
    }	
	
    @Override
	public String toString() {
		return drawToString();
	}

	@Override
	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!isArraysEquals(((MicroBoard)obj).board))
        	return false;
        return true;		
	}

	public boolean equalsFull(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MicroBoard b = (MicroBoard) obj;
        if (board != null && b.board == null)
        	return false;
        if (b.board != null && board == null)
        	return false;
        if (board.length != b.board.length)
            return false;
        if (board.length > 0)
        	if (board[0].length != b.board[0].length)
        		return false;
        if (!isArraysEquals(b.board))
        	return false;
        return true;		
	}
	
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (board != null) {
        	result = prime * result + board.length;
        	if (board.length > 0) result = prime * result + board[0].length;
        }
        return result;
    }
	

}
