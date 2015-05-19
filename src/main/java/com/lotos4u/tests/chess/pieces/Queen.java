package com.lotos4u.tests.chess.pieces;

import java.util.ArrayList;
import java.util.List;

import com.lotos4u.tests.chess.boards.ChessBoard;
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
    public List<Point> getPointsTakeble(ChessBoard board) {
        List<Point> res = new ArrayList<Point>();
        res.addAll(getRookTakeble(board));
        res.addAll(getBishopTakeble(board));
        return res;
    }

}
