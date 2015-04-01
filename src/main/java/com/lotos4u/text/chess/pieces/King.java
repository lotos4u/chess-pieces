package com.lotos4u.text.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.lotos4u.text.chess.boards.ChessBoard;
import com.lotos4u.text.chess.boards.Point;


public class King extends Piece {

	@Override
	public List<Point> getPointsTakeble() {
        List<Point> res = new ArrayList<Point>();
        if(position == null)
            return res;        
        ChessBoard board = position.getBoard();
        Point[] takePoints = new Point[]{
                board.getPoint(position.getX(), position.getY() + 1),
                board.getPoint(position.getX(), position.getY() - 1),
                board.getPoint(position.getX() + 1, position.getY()),
                board.getPoint(position.getX() + 1, position.getY() + 1),
                board.getPoint(position.getX() + 1, position.getY() - 1),
                board.getPoint(position.getX() - 1, position.getY()),
                board.getPoint(position.getX() - 1, position.getY() + 1),
                board.getPoint(position.getX() - 1, position.getY() - 1)
        };
        
        for (int i = 0; i < takePoints.length; i++) {
            if(takePoints[i] != null){
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

    @Override
    public int getTakebility() {
        return 10;
    }


}
