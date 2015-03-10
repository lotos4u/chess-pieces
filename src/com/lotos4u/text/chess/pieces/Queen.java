package com.lotos4u.text.chess.pieces;

public class Queen extends Piece {

    @Override
    public boolean isValidMove(Point point) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getName() {
        return "Queen";
    }

    @Override
    public int getTakebility() {
        return 100;
    }

}
