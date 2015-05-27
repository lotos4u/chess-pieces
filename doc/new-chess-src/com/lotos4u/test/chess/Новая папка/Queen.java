package com.lotos4u.tests.chess.pieces;

import com.lotos4u.tests.chess.boards.Point;

public class Queen extends Piece {

	
    public Queen() {
		super("Queen");
	}

    @Override
    public int getTakebility() {
        return 100;
    }

	@Override
	public boolean canTakePoint(Point point) {
		if ((point != null) && (position != null) && !position.isSamePoint(point)) {
			return ((position.getY() == point.getY()) || (position.getX() == point.getX())) ||
					(Math.abs(position.getY() - point.getY()) == Math.abs(position.getX() - point.getX()));
		}
		return false;
	}

}
