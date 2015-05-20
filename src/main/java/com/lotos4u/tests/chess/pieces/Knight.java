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
        
        int X = position.getX();
        int Y = position.getY();
        int x, y;
        
    	x = X - 1; y = Y - 2;
    	if (board.isPointOnBoard(x, y))
    		res.add(new Point(x, y));
    	x = X + 1; y = Y - 2;
    	if (board.isPointOnBoard(x, y))
    		res.add(new Point(x, y));
    	x = X - 2; y = Y - 1;
    	if (board.isPointOnBoard(x, y))
    		res.add(new Point(x, y));
    	x = X - 2; y = Y + 1;
    	if (board.isPointOnBoard(x, y))
    		res.add(new Point(x, y));
    	x = X - 1; y = Y + 2;
    	if (board.isPointOnBoard(x, y))
    		res.add(new Point(x, y));
    	x = X + 1; y = Y + 2;
    	if (board.isPointOnBoard(x, y))
    		res.add(new Point(x, y));
    	x = X + 2; y = Y - 1;
    	if (board.isPointOnBoard(x, y))
    		res.add(new Point(x, y));
    	x = X + 2; y = Y + 1;
    	if (board.isPointOnBoard(x, y))
    		res.add(new Point(x, y));
    	
        return res;
    }
}
