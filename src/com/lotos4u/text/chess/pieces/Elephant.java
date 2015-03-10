package com.lotos4u.text.chess.pieces;

public class Elephant extends Piece {

    @Override
    public boolean isValidMove(Point point) {
        return true;
    }

    @Override
    public String getName() {
        return "Elephant";
    }

    @Override
    public int getTakebility() {
        // TODO Auto-generated method stub
        return 50;
    }

}
