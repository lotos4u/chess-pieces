package com.lotos4u.tests.chess.pieces;

import com.lotos4u.tests.chess.boards.Point;

public class Rook extends Piece {

	public Rook() {
		super("Rook");
	}

    @Override
    public int getTakebility() {
        return 50;
    }

	@Override
	public boolean canTakePoint(Point point) {
		if ((point != null) && (position != null) && !position.isSamePoint(point)) {
			return (position.getY() == point.getY()) || (position.getX() == point.getX());
		}
		return false;
	}

}
