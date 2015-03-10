package com.lotos4u.text.chess.pieces;

import java.util.List;

public class Rook extends Piece {

	@Override
	public List<Point> getPointsTakeble() {
		return Piece.getRookTakeble(position);
	}

	@Override
	public String getName() {
		return "Rook";
	}

	@Override
	public boolean isValidMove(Point point) {
		return true;
	}

    @Override
    public int getTakebility() {
        return 50;
    }

}
