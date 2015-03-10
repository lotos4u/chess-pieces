package com.lotos4u.text.chess.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lotos4u.text.chess.pieces.King;
import com.lotos4u.text.chess.pieces.Piece;
import com.lotos4u.text.chess.pieces.Point;
import com.lotos4u.text.chess.pieces.Rook;

public class Main {

	public static void main(String[] args) {
	    //testPoints();System.exit(0);
		Rook rook1 = new Rook();
		Rook rook2 = new Rook();
		Rook rook3 = new Rook();
		Rook rook4 = new Rook();
		
        King king1 = new King();
        King king2 = new King();
        King king3 = new King();
        King king4 = new King();
       
        //System.out.println("Compare " + king1.getName() + " to " + rook1.getName() + ": " + king1.compareTo(rook1));
        //System.out.println("Compare " + rook1.getName() + " to " + king1.getName() + ": " + rook1.compareTo(king1));
		ChessBoard board = new ChessBoard(3, 3);
		System.out.println("Board initial");
		System.out.println(board);
		
		//Point point = new Point(3, 3);
		//System.out.println("Neighbors of " + point + " are: " + point.getNeighbors(board));
		//ChessBoard board55 = new ChessBoard(5, 5);
		//System.out.println("Neighbors of " + point + " are: " + point.getNeighbors(board55));
		
		List<Piece> pieces = new ArrayList<Piece>();
		pieces.add(rook1);
		pieces.add(rook2);
		pieces.add(king1);
		pieces.add(rook3);
        
        //pieces.add(king2);
        //pieces.add(king3);
		
        //System.out.println("Unsorted: " + pieces);
		//Collections.sort(pieces);
		//System.out.println("Sorted: " + pieces);
		
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

	public static void testPoints(){
	    Point p1 = new Point(1, 1);
	    Point p2 = new Point(2, 2);
	    Point p3 = new Point(3, 3);
	    Point p4 = new Point(1, 2);
	    
	    ChessBoard board = new ChessBoard(3, 3);
	    p1.setMax(board);
	    p2.setMax(board);
	    p3.setMax(board);
	    p4.setMax(board);
	    
	    List<Point> points = new ArrayList<Point>();
	    points.add(p1);
	    points.add(p2);
	    points.add(p3);
	    points.add(p4);
	    
	    System.out.println("Neighbors of " + p1 + " are " + p1.getNeighbors());
	    System.out.println("Neighbors of " + p2 + " are " + p2.getNeighbors());
	    System.out.println("Neighbors of " + p3 + " are " + p3.getNeighbors());
	    System.out.println("Neighbors of " + p4 + " are " + p4.getNeighbors());
	    
	    System.out.println("Unsorted points: " + points);
	    Collections.sort(points);
	    System.out.println("Sorted points: " + points);
	    
	    
	}
}
