package com.lotos4u.text.chess.pieces;

import java.util.List;

public class Rook extends Piece {

	@Override
	public List<Point> getTakePoints() {
		List<Point> res = super.getTakePoints();

		if(!board.isPointOnBoard(position))
			return res;

		for (int x = 1; x <= board.getxSize(); x++) {
			if(x != position.getX())
				res.add(new Point(x, position.getY()));
		}
		for (int y = 1; y <= board.getySize(); y++) {
			if(y != position.getY())
				res.add(new Point(position.getX(), y));
		}
			
		return res;
	}

	@Override
	public String getName() {
		return "Rook";
	}

	@Override
	public boolean isValidMove(Point point) {
		return true;
	}

}
