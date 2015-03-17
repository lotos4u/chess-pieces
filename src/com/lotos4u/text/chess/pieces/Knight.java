package com.lotos4u.text.chess.pieces;

import java.util.List;

import com.lotos4u.text.chess.boards.Point;

public class Knight extends Piece {

    @Override
    public boolean isValidMove(Point point) {
        return true;
    }

    @Override
    public String getName() {
        return "Knight";
    }

    @Override
    public int getTakebility() {
        return 20;
    }

    @Override
    public List<Point> getPointsTakeble() {
        return Piece.getKnightTakeble(position);
    }
}
