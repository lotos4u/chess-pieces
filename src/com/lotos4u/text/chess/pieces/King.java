package com.lotos4u.text.chess.pieces;

import java.util.List;


public class King extends Piece {

	@Override
	public List<Point> getPointsTakeble() {
		List<Point> res = super.getPointsTakeble();
		
		Point[] takePoints = new Point[]{
				new Point(position.getX(), position.getY() + 1),
				new Point(position.getX(), position.getY() - 1),
				new Point(position.getX() + 1, position.getY()),
				new Point(position.getX() + 1, position.getY() + 1),
				new Point(position.getX() + 1, position.getY() - 1),
				new Point(position.getX() - 1, position.getY()),
				new Point(position.getX() - 1, position.getY() + 1),		
				new Point(position.getX() - 1, position.getY() - 1)
		};
		
		if(!board.isPointOnBoard(position))
			return res;
		
		for (int i = 0; i < takePoints.length; i++) {
			if(board.isPointOnBoard(takePoints[i])){
				res.add(takePoints[i]);
			}
		}
		return res;
	}

	@Override
	public String getName() {
		return "King";
	}

	@Override
	public boolean isValidMove(Point point) {
		return true;
	}

}
