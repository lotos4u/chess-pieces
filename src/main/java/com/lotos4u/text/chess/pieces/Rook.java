package com.lotos4u.text.chess.pieces;

import java.util.List;

import com.lotos4u.text.chess.boards.ChessBoard;
import com.lotos4u.text.chess.boards.Point;

public class Rook extends Piece {

	public Rook() {
		super("Rook");
	}

	@Override
	public List<Point> getPointsTakeble(ChessBoard board) {
		return getRookTakeble(board);
	}

    @Override
    public int getTakebility() {
        return 50;
    }

}
