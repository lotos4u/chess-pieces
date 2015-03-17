package com.lotos4u.text.chess.pieces;

import java.util.List;

import com.lotos4u.text.chess.boards.Point;

public class Queen extends Piece {

    @Override
    public boolean isValidMove(Point point) {
        return true;
    }

    @Override
    public String getName() {
        return "Queen";
    }

    @Override
    public int getTakebility() {
        return 100;
    }

    @Override
    public List<Point> getPointsTakeble() {
        return Piece.getQueenTakeble(position);
    }

}
