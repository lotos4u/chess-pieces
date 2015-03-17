package com.lotos4u.text.chess.pieces;

import java.util.List;

import com.lotos4u.text.chess.boards.Point;


public class King extends Piece {

	@Override
	public List<Point> getPointsTakeble() {
	    return Piece.getKingTakeble(position);
	}

	@Override
	public String getName() {
		return "King";
	}

	@Override
	public boolean isValidMove(Point point) {
		return true;
	}

    @Override
    public int getTakebility() {
        return 10;
    }


}
