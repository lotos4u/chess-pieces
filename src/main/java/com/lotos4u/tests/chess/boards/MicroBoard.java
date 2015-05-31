package com.lotos4u.tests.chess.boards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MicroBoard {
	private int xSize;
	private int ySize;
	private char[] board;
	public static int equalsCounter = 0;
	public static int hashCounter = 0;
	private boolean updateEqualsCounter = true;
	private boolean updateHashCounter = true;

	public MicroBoard(int x, int y) {
		super();
		xSize = x;
		ySize = y;
		board = new char[x*y];
	}

	public MicroBoard(char[] input, int x,  int y) {
		this(x, y);
		updateBoard(input);
	}
	
	public MicroBoard(ChessBoard board) {
		this(board.getArrayView(),  board.xSize, board.ySize);
	}
	public int[] getPointForIndex(int index) {
		int x = index / ySize;
		int y = index - x*ySize;
		return new int[]{x, y};
	}
	public int getIndexForPoint(int x, int y) {
		return y + ySize*x;//=index
	}
	public void updateBoard(char[] input) {
    	for (int i = 0; i < board.length; i++)
   			board[i] = input[i];
	}
    private String getArrayAsString(char[][] input) {
    	String res = "";
    	List<String> pre = getArrayAsStrings(input);
    	for (Iterator<String> iterator = pre.iterator(); iterator.hasNext();) {
			 res += (iterator.next() + "\n");
		}
    	return res;
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
	
    public String getBoardViewAsString() {
    	return getArrayAsString(getBoardView());
    }
    
	public char[][] getBoardView() {
		char[][] b = new char[xSize][ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				int pointIndex = getIndexForPoint(x, y);
				b[x][y] = board[pointIndex];
			}
		return b;
	}
    
	@Override
	public boolean equals(Object obj) {
		if (updateEqualsCounter) equalsCounter++;
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!Arrays.equals(((MicroBoard)obj).board, board))
        	return false;
        return true;		
	}

	public boolean equalsFull(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof MicroBoard))
            return false;
        MicroBoard b = (MicroBoard) obj;
        if (board != null && b.board == null)
        	return false;
        if (b.board != null && board == null)
        	return false;
        if (board.length != b.board.length)
            return false;
        if (!Arrays.equals(b.board, board))
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
        for (int i = 0; i < board.length; i++)
        	result += board[i]*i;
        return result;
    }
	

}
