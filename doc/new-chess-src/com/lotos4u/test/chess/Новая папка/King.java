package com.lotos4u.tests.chess.pieces;

import com.lotos4u.tests.chess.boards.Point;


public class King extends Piece {

	public King() {
		super("King");
	}

	public boolean canTakePoint(Point point) {
		if ((point != null) && (position != null) && !position.isSamePoint(point)) {
			return (Math.abs(position.getY() - point.getY()) <= 1) && (Math.abs(position.getX() - point.getX()) <= 1);
		}
		return false;
	}
	
    @Override
    public int getTakebility() {
        return 10;
    }

}
