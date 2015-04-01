package com.lotos4u.text.chess.pieces;

import java.util.List;

import com.lotos4u.text.chess.boards.Point;

public class Bishop extends Piece {

    @Override
    public boolean isValidMove(Point point) {
        return true;
    }

    @Override
    public List<Point> getPointsTakeble() {
        return getBishopTakeble();
    }
    
    @Override
    public String getName() {
        return "Elephant";
    }

    @Override
    public int getTakebility() {
        return 50;
    }

}
