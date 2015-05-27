package com.lotos4u.tests.chess.pieces;

import com.lotos4u.tests.chess.boards.Point;

public class Bishop extends Piece {
	
    public Bishop() {
		super("Bishop");
	}

    @Override
    public int getTakebility() {
        return 50;
    }

	@Override
	public boolean canTakePoint(Point point) {
		if ((point != null) && (position != null) && !position.isSamePoint(point)) {
			return (Math.abs(position.getY() - point.getY()) == Math.abs(position.getX() - point.getX()));
		}
		return false;
	}

}
