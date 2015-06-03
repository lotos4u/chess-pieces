package com.lotos4u.tests.chess.boards;

import java.util.Arrays;

public class MicroChessBoard extends AbstractChessBoard {
	public MicroChessBoard(AbstractChessBoard input) {
		xSize = input.xSize;
		ySize = input.ySize;
		nPoints = xSize*ySize;
		nPieces = input.nPieces;
		boardPiecesNames = Arrays.copyOf(input.boardPiecesNames, nPoints);
	}

	@Override
	public boolean equals(Object obj) {
		//if (updateEqualsCounter) 
		//equalsCounter++;
		//if (!(obj instanceof AbstractChessBoard))
		//	return false;
		//if (!getClass().equals(obj.getClass()))
		//	return false;
		MicroChessBoard b = (MicroChessBoard) obj;
		return Arrays.equals(boardPiecesNames, b.boardPiecesNames);
		//return Arrays.hashCode(board) == Arrays.hashCode(b.board);
	}

	@Override
	public int hashCode() {
		//if (updateHashCounter) 
		//hashCounter++;
        return Arrays.hashCode(boardPiecesNames);
    }	

}
