package com.lotos4u.text.chess.pieces;

import java.util.List;

import com.lotos4u.text.chess.boards.ChessBoard;
import com.lotos4u.text.chess.boards.Point;

public class Bishop extends Piece {
	
    public Bishop() {
		super("Bishop");
	}

    @Override
    public List<Point> getPointsTakeble(ChessBoard board) {
        return getBishopTakeble(board);
    }
    
    @Override
    public int getTakebility() {
        return 50;
    }

}
