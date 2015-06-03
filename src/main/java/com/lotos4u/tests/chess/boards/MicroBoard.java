package com.lotos4u.tests.chess.boards;

import java.util.Arrays;

public class MicroBoard extends AbstractChessBoard {
	protected int nPieces;
	
	public MicroBoard(IChessBoard input) {
		super(input.getXSize(), input.getYSize());
		nPieces = input.getNPieces();
		board = Arrays.copyOf(input.getBoardViewAsArray(), input.getNPoints());
	}
	
	public int getNPieces() {
		return nPieces;
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
	public boolean equals(Object obj) {
		//if (updateEqualsCounter) equalsCounter++;
		if (!(obj instanceof AbstractChessBoard))
			return false;
		AbstractChessBoard b = (AbstractChessBoard) obj;
		return Arrays.equals(board, b.board);
	}

	@Override
	public int hashCode() {
		//if (updateHashCounter) hashCounter++;
        final int prime = 31;
        int result = 1;
        //result = prime * result + xSize;
        //result = prime * result + ySize;
        //result = prime * result + getNPieces();
        //Arrays.
        for (int i = 0; i < nPoints; i++) {
        	result += board[i]*i;
        }
        return result;
    }	

}
