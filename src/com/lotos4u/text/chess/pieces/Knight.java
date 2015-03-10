package com.lotos4u.text.chess.pieces;

public class Knight extends Piece {

    @Override
    public boolean isValidMove(Point point) {
        // TODO Auto-generated method stub
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

}
