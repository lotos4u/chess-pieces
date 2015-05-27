package com.lotos4u.tests.chess.pieces;

import com.lotos4u.tests.chess.boards.Point;

public class Knight extends Piece {

	
    public Knight() {
		super("Knight");
	}

    @Override
    public int getTakebility() {
        return 20;
    }

	@Override
	public boolean canTakePoint(Point point) {
		if ((point != null) && (position != null) && !position.isSamePoint(point)) {
			int pointX = point.getX();
			int pointY = point.getY();
			int x = position.getX();
			int y = position.getY();
			return ((pointX == x - 1) && (pointY == y - 2)) ||
				   ((pointX == x + 1) && (pointY == y - 2)) ||
				   ((pointX == x - 2) && (pointY == y - 1)) ||
				   ((pointX == x - 2) && (pointY == y + 1)) ||
				   ((pointX == x - 1) && (pointY == y + 2)) ||
				   ((pointX == x + 1) && (pointY == y + 2)) ||
				   ((pointX == x + 2) && (pointY == y - 1)) ||
				   ((pointX == x + 2) && (pointY == y + 1));					
		}
		return false;
	}
}
