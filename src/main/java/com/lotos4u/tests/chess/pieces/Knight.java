package com.lotos4u.tests.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.lotos4u.tests.chess.boards.ChessBoard;
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
    public List<Point> getPointsTakeble(ChessBoard board) {
        List<Point> res = new ArrayList<Point>();
        if(position == null)
            return res;        
        Point[] takePoints = new Point[]{
                board.getPoint(position.getX() - 1, position.getY() - 2),
                board.getPoint(position.getX() + 1, position.getY() - 2),
                board.getPoint(position.getX() - 2, position.getY() - 1),
                board.getPoint(position.getX() - 2, position.getY() + 1),
                board.getPoint(position.getX() - 1, position.getY() + 2),
                board.getPoint(position.getX() + 1, position.getY() + 2),
                board.getPoint(position.getX() + 2, position.getY() - 1),
                board.getPoint(position.getX() + 2, position.getY() + 1)
        };
        
        for (int i = 0; i < takePoints.length; i++) {
            if(takePoints[i] != null){
                res.add(takePoints[i]);
            }
        }
        return res;
    }
}
