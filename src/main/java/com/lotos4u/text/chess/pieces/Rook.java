package main.java.com.lotos4u.text.chess.pieces;

import java.util.List;

import main.java.com.lotos4u.text.chess.boards.Point;

public class Rook extends Piece {

	@Override
	public List<Point> getPointsTakeble() {
		return getRookTakeble();
	}

	@Override
	public String getName() {
		return "Rook";
	}

	@Override
	public boolean isValidMove(Point point) {
		return true;
	}

    @Override
    public int getTakebility() {
        return 50;
    }

}
