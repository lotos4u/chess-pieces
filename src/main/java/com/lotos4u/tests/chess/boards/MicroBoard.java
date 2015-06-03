package com.lotos4u.tests.chess.boards;

import java.util.Arrays;

public class MicroBoard extends AbstractChessBoard {
	
	public MicroBoard(AbstractChessBoard input) {
		super(input.xSize, input.ySize);
		nPieces = input.nPieces;
		System.arraycopy(input.getBoardViewAsArray(), 0, boardPiecesNames, 0, input.nPoints);
	}

	public boolean equalsFull(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof MicroBoard))
            return false;
        MicroBoard b = (MicroBoard) obj;
        if (boardPiecesNames != null && b.boardPiecesNames == null)
        	return false;
        if (b.boardPiecesNames != null && boardPiecesNames == null)
        	return false;
        if (boardPiecesNames.length != b.boardPiecesNames.length)
            return false;
        if (!Arrays.equals(b.boardPiecesNames, boardPiecesNames))
        	return false;
        return true;		
	}
	@Override
	public boolean equals(Object obj) {
		//if (updateEqualsCounter) 
		equalsCounter++;
		//if (!(obj instanceof AbstractChessBoard))
		//	return false;
		
		AbstractChessBoard b = (AbstractChessBoard) obj;
		return Arrays.equals(boardPiecesNames, b.boardPiecesNames);
	}

	@Override
	public int hashCode() {
		//if (updateHashCounter) 
		hashCounter++;
        final int prime = 31;
        int result = 1;
        //result = prime * result + xSize;
        //result = prime * result + ySize;
        //result = prime * result + getNPieces();
        //Arrays.
        //int[] p;
        result += Arrays.hashCode(boardPiecesNames);
        return result;
    }	

}
