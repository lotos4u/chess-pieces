package com.lotos4u.text.chess.general;

import java.util.ArrayList;
import java.util.List;

import com.lotos4u.text.chess.pieces.Piece;
import com.lotos4u.text.chess.pieces.Rook;

public class Main {

	public static void main(String[] args) {
		Rook rook1 = new Rook();
		Rook rook2 = new Rook();

		ChessBoard board = new ChessBoard(3, 3);
		System.out.println("Board initial");
		System.out.println(board);
		
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(rook1);
		pieces.add(rook2);
		board.addPieces(pieces);
		
		board.arrange();
		
		System.out.println("Board final");
		System.out.println(board);

	}

}
