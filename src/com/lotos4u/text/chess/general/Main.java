package com.lotos4u.text.chess.general;

import java.util.ArrayList;
import java.util.List;

import com.lotos4u.text.chess.pieces.King;
import com.lotos4u.text.chess.pieces.Piece;
import com.lotos4u.text.chess.pieces.Rook;

public class Main {

	public static void main(String[] args) {
		Rook rook1 = new Rook();
		Rook rook2 = new Rook();
		Rook rook3 = new Rook();
        King king1 = new King();
        King king2 = new King();
        King king3 = new King();

		ChessBoard board = new ChessBoard(3, 3);
		System.out.println("Board initial");
		System.out.println(board);
		
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(rook1);
		pieces.add(rook2);
		pieces.add(rook3);
        //pieces.add(king1);
        //pieces.add(king2);
        //pieces.add(king3);
		board.putPieces(pieces);

		
		/*
		board.arrangePieces();
		System.out.println("Board after normal arrangement");
		System.out.println(board);
		System.out.println("Free points on the board");
		System.out.println(board.getPointsFree());
		*/
		
		board.arrangePiecesWisely();
		System.out.println("Board after wise arrangement");
		System.out.println(board);
        System.out.println("Free points on the board");
        System.out.println(board.getPointsFree());

	}

}
