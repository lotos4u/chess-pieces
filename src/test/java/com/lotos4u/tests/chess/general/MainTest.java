package com.lotos4u.tests.chess.general;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.lotos4u.tests.chess.boards.ChessBoard;
import com.lotos4u.tests.chess.boards.Point;
import com.lotos4u.tests.chess.general.Log;
import com.lotos4u.tests.chess.pieces.Bishop;
import com.lotos4u.tests.chess.pieces.King;
import com.lotos4u.tests.chess.pieces.Knight;
import com.lotos4u.tests.chess.pieces.Queen;
import com.lotos4u.tests.chess.pieces.Rook;

public class MainTest {
	
	@Test @Ignore
	public void testPointEquals() {
        Log.out("\n\n********************** Test testPointEquals **********************\n");
        Point point1 = new Point(1, 1);
        Point point2 = new Point(1, 1);
        Point point3 = new Point(1, 2);
        Point point4 = new Point(1, 2);
        Set<Point> set = new HashSet<Point>();
        set.add(point1);
        set.add(point2);
        set.add(point3);
        set.add(point4);
        Log.out("(" + point1 + " == " + point2 + ") = " + point1.equals(point2));
        Log.out("(" + point1 + " == " + point3 + ") = " + point1.equals(point3));
        Log.out("H(" + point1 + ") = " + point1.hashCode());
        Log.out("H(" + point2 + ") = " + point2.hashCode());
        Log.out("H(" + point3 + ") = " + point3.hashCode());
        Log.out("Set=" + set);
	}
	@Test @Ignore
	public void testPieceSameKind() {
        Log.out("\n\n********************** Test testPieceSameKind **********************\n");
        King king1 = new King();
        King king2 = new King();
        Queen queen1 = new Queen();
        Queen queen2 = new Queen();
        Bishop bishop1 = new Bishop();
        Bishop bishop2 = new Bishop();
        
        Log.out(king2.isSameKind(king1));
        Log.out(king2.isSameKind(queen1));
		
	}

	@Test @Ignore
	public void testKing() {
        Log.out("\n\n********************** Test testKing **********************\n");
        ChessBoard board = new ChessBoard(5,5);
        board.addPiece(new King());
        board.getPiece(0).setPosition(3, 3, board);
        Log.out(board.getPiece(0) + " " + board.getPiece(0).getPointsTakeble());
        board.getPiece(0).setPosition(1, 1, board);
        Log.out(board.getPiece(0) + " " + board.getPiece(0).getPointsTakeble());
        board.getPiece(0).setPosition(3, 1, board);
        Log.out(board.getPiece(0) + " " + board.getPiece(0).getPointsTakeble());
	}
	
	@Test @Ignore
	public void testKnight() {
        Log.out("\n\n********************** Test testKnight **********************\n");
        ChessBoard board = new ChessBoard(5,5);
        board.addPiece(new Knight());
        board.getPiece(0).setPosition(3, 3, board);
        Log.out(board.getPiece(0) + " " + board.getPiece(0).getPointsTakeble());
	}

	@Test @Ignore
	public void testBishop() {
        Log.out("\n\n********************** Test testBishop **********************\n");
        ChessBoard board = new ChessBoard(5,5);
        board.addPiece(new Bishop());
        board.getPiece(0).setPosition(2, 1, board);
        Log.out(board.getPiece(0) + " " + board.getPiece(0).getPointsTakeble());
	}
	
	@Test @Ignore
	public void testRook() {
        Log.out("\n\n********************** Test testRook **********************\n");
        ChessBoard board = new ChessBoard(5,5);
        board.addPiece(new Rook());
        board.getPiece(0).setPosition(2, 2, board);
        Log.out(board.getPiece(0) + " " + board.getPiece(0).getPointsTakeble());
	}

	@Test @Ignore
	public void testQueen() {
        Log.out("\n\n********************** Test testQueen **********************\n");
        ChessBoard board = new ChessBoard(5,5);
        board.addPiece(new Queen());
        board.getPiece(0).setPosition(1, 1, board);
        Log.out(board.getPiece(0) + " " + board.getPiece(0).getPointsTakeble());
	}
    /**
     * ?? Queens on 7×7 board
     */
	@Test @Ignore
	public void testQQQon7x7(){
        Log.out("\n\n********************** Test testQQQon7x7 **********************\n");
        ChessBoard board = new ChessBoard(2,2);
        board.addPiece(new Queen());
        board.addPiece(new Queen());
        //board.addPiece(new Queen());
        //board.addPiece(new Queen());
        //board.addPiece(new Queen());
        
        int boards = board.arrangeRecursivelyVariants();
        
        Assert.assertTrue(boards > 0);
    }
	
	@Test @Ignore
	public void testBoardDraw(){
        Log.out("\n\n********************** Test testBoardDraw **********************\n");
        ChessBoard board = new ChessBoard(7, 7);
        board.draw();
	}
	
	/**
     * 2 Kings, 2 Queens, 2 Bishops and 1 Knight on 7×7 board
     */
	@Test @Ignore
	public void testKKQQBBNon7x7Single(){
        Log.out("\n\n********************** Test testKKQQBBNon7x7Single **********************\n");
        ChessBoard board = new ChessBoard(7, 7);
        board.addPiece(new King());
        board.addPiece(new King());
        board.addPiece(new Queen());
        board.addPiece(new Queen());
        board.addPiece(new Bishop());
        board.addPiece(new Bishop());
        board.addPiece(new Knight());
        
        board.arrangeRecursively();
        
        //Log.out(board);
        board.draw();
        
        //Assert.assertTrue(boards > 0);
    }
	
	@Test @Ignore
	public void testSameArrange() {
		Log.out("\n\n********************** Test testSameArrange **********************\n");
		ChessBoard board1 = new ChessBoard(3, 3);
		board1.addPiece(new Rook());
		board1.addPiece(new Rook());
		board1.getPiece(1).setPosition(new Point(1, 1), board1);
		board1.getPiece(0).setPosition(new Point(1, 2), board1);
		Log.out(board1);
		
		ChessBoard board2 = new ChessBoard(3, 3);
		board2.addPiece(new Rook());
		board2.addPiece(new Rook());
		board2.getPiece(0).setPosition(new Point(1, 1), board2);
		board2.getPiece(1).setPosition(new Point(1, 2), board2);
		Log.out(board2);
		
		Log.out(board1.equals(board2));
		Log.out("H1=" + board1.hashCode() + ", H2=" + board2.hashCode());
		
		Set<ChessBoard> boards = new HashSet<ChessBoard>();
		boards.add(board1);
		boards.add(board2);
		Log.out(boards.size());
	}
    /**
     * 1 Rook and 2 Kings on 3x3 Board
     */
    @Test @Ignore
    public void testRKKon3x3(){
    	Log.out("\n\n********************** Test testRKKon3x3 **********************\n");
    	ChessBoard board = new ChessBoard(3, 3);
    	board.addPiece(new Rook());
    	board.addPiece(new King());
    	board.addPiece(new King());
        
        int res = board.arrangeRecursivelyVariants();
        
        Assert.assertEquals(4, res);
    }

    
    /**
     * 2 Rooks and 4 Knights on 4x4 Board
     */
    @Test
    public void testRRNNNNon4x4(){
    	Log.out("\n\n********************** Test testRRNNNNon4x4 **********************\n");
    	ChessBoard board = new ChessBoard(4, 4);
    	board.addPiece(new Rook());
    	board.addPiece(new Rook());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	long start = System.currentTimeMillis();
        int res = board.arrangeRecursivelyVariants();
        long end = System.currentTimeMillis();
        Log.out("Arrangement performed in " + (end - start) + " ms");

        int counter = 0;
        for (ChessBoard b : board.getVariants()) {
        	System.out.println("Variant " + (counter++));
        	b.draw();
        }
        
        Assert.assertEquals(8, res);
    }

	/**
     * 2 Kings, 2 Queens, 2 Bishops and 1 Knight on 7×7 board
     */
	@Test
	public void testKKQQBBNon7x7(){
        Log.out("\n\n********************** Test testKKQQBBNon7x7 **********************\n");
        ChessBoard board = new ChessBoard(7, 7);
        board.addPiece(new King());
        board.addPiece(new King());
        board.addPiece(new Queen());
        board.addPiece(new Queen());
        board.addPiece(new Bishop());
        board.addPiece(new Bishop());
        board.addPiece(new Knight());
        long start = System.currentTimeMillis();
        int boards = board.arrangeRecursivelyVariants();
        long end = System.currentTimeMillis();
        Log.out("Arrangement performed in " + (end - start) + " ms");
        
        int counter = 1;
        for (ChessBoard b : board.getVariants()) {
        	System.out.println("Variant " + (counter++));
        	b.draw();
        }
        
        Assert.assertTrue(boards > 0);
    }

    
}
