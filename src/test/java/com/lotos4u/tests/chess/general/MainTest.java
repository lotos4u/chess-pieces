package com.lotos4u.tests.chess.general;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.lotos4u.tests.chess.boards.ChessBoard;
import com.lotos4u.tests.chess.boards.MicroBoard;
import com.lotos4u.tests.chess.boards.Point;
import com.lotos4u.tests.chess.general.Log;
import com.lotos4u.tests.chess.light.ChessBoardLight;
import com.lotos4u.tests.chess.pieces.Bishop;
import com.lotos4u.tests.chess.pieces.King;
import com.lotos4u.tests.chess.pieces.Knight;
import com.lotos4u.tests.chess.pieces.Piece;
import com.lotos4u.tests.chess.pieces.Queen;
import com.lotos4u.tests.chess.pieces.Rook;

public class MainTest {

    @Test @Ignore
    public void testSetArray(){
    	Set<char[]> s = new HashSet<char[]>();
    	char[] c1 = new char[] {'1','2','3','4'};
    	char[] c2 = new char[] {'1','2','3','4'};
    	s.add(c1);
    	s.add(c2);
    	Log.out(s);
    }	
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
	
    @Test @Ignore
    public void testCanTakePoint(){
    	Log.out("\n\n********************** Test testCanTakePoint **********************\n");
    	ChessBoard board = new ChessBoard(7, 7);
    	board.addPiece(new Queen());
    	board.getPiece(0).setPosition(new Point(4, 4), board);
    	for (int i = 1; i <= board.getxSize(); i++)
    		for (int j = 1; j <= board.getySize(); j++) {
    			Point test = new Point(i,j);
    			if (board.getPiece(0).canTakePoint(test)) {
    				Piece p = new Queen();
    				p.setPosition(test, board);
    				board.addPiece(p);
    			}
    			//Log.out("Point " + test + " takeble = " + board.getPiece(0).canTakePoint(test));
    		}
    	board.draw();
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
        
    	long start = System.currentTimeMillis();
        int res = board.arrangeRecursivelyVariants();
        long end = System.currentTimeMillis();
        Log.out("Arrangement performed in " + (end - start) + " ms");

        int counter = 0;
        for (MicroBoard b : board.getVariants()) {
        	System.out.println("Variant " + (counter++));
        	System.out.println(b.drawToString());;
        }

        
        Assert.assertEquals(4, res);
    }

    
    /**
     * 2 Rooks and 4 Knights on 4x4 Board
     */
    @Test @Ignore
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
        for (MicroBoard b : board.getVariants()) {
        	System.out.println("Variant " + (counter++));
        	System.out.println(b.drawToString());;
        }
        
        Assert.assertEquals(8, res);
    }

	/**
     * 2 Kings, 2 Queens, 2 Bishops and 1 Knight on 7×7 board
     */
	@Test @Ignore
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
        for (MicroBoard b : board.getVariants()) {
        	System.out.println("Variant " + (counter++));
        	System.out.println(b.drawToString());;
        }
        
        Assert.assertTrue(boards > 0);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Test @Ignore
	public void testBoardLightEquals() {
		Log.out("\n\n********************** Test testBoardLightEquals **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(3, 3, pcs1);
		board1.setPiecePosition(0, 0, 0);
		board1.setPiecePosition(1, 0, 2);
		board1.setPiecePosition(2, 2, 1);
		board1.drawBoardViewAndTakeble();

		char[] pcs2 = new char[]{ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight board2 = new ChessBoardLight(3, 3, pcs2);
		board2.setPiecePosition(0, 0, 0);
		board2.setPiecePosition(1, 0, 2);
		board2.setPiecePosition(2, 2, 1);
		board2.drawBoardViewAndTakeble();
		
		System.out.println("Equals=" + board1.equals(board2));

	}
	@Test @Ignore
	public void testBoardLightGeneral() {
		Log.out("\n\n********************** Test testBoardLightGeneral **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.BISHOP, ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK, ChessBoardLight.KNIGHT, ChessBoardLight.QUEEN};
		ChessBoardLight board1 = new ChessBoardLight(3, 3, pcs1);
//		board1.draw();
		//board1.setPiecePosition(0, 0, 0);
		//board1.setPiecePosition(1, 1, 1);
		//board1.setPiecePosition(2, 2, 2);
		//board1.setPiecePosition(3, 3, 3);
		//board1.setPiecePosition(4, 4, 4);
		//board1.draw();
		
		board1.dropPieces();
		
		board1.setPiecePosition(1, 0, 0);
		board1.drawBoardViewAndTakeble();
		board1.setPiecePosition(2, 0, 2);
		board1.drawBoardViewAndTakeble();
		//board1.dropPiece(1);
		board1.setPiecePosition(3, 2, 0);
		board1.drawBoardViewAndTakeble();
		board1.dropPiece(3);
		board1.setPiecePosition(3, 2, 1);
		board1.drawBoardViewAndTakeble();
		//board1.setPiecePosition(3, 1, 0);
		//board1.drawBoardAndTakeble();
		
		/*
		char[] pcs2 = new char[]{ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight board2 = new ChessBoardLight(3, 3, pcs2);
		board2.setPiecePosition(0, 0, 0);
		board2.setPiecePosition(1, 0, 2);
		board2.setPiecePosition(2, 2, 1);
		board2.draw();
		System.out.println(board2.getPiecesAsString());
		System.out.println(board2.isArranged());
		System.out.println(board2.isValid());
		*/
		/*
		board2.dropPieces();
		board2.draw();
		System.out.println(board2.getPiecesAsString());
		
		System.out.println("Board arranged equals = " + board1.isArrangeEquals(board2));
		System.out.println(board2.isArraged());
		*/
	}	

	@Test @Ignore
	public void testBoardLightRecursion3x3Multi() {
		Log.out("\n\n********************** Test testBoardLightRecursion3x3Multi **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(3, 3, pcs1);
		int counter = 0;
		long time = 0;
		while (counter++ < 10) {
			long start = System.currentTimeMillis();
			board1.getArrangementVariants(false, false, false, false);
			long end = System.currentTimeMillis();
			time += (end - start);
			System.out.println("Arrangement #" + counter + ": " + (end - start) + " ms");
		}
		System.out.println("One Arragements takes " + (time/counter) + " ms");
	}
	
	@Test @Ignore
	public void testBoardLightRecursion3x3() {
		Log.out("\n\n********************** Test testBoardLightRecursion3x3 **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(3, 3, pcs1);
		board1.getArrangementVariants(false, false, true, false);

		int counter = 0;
        for (ChessBoardLight b : board1.getUniqueVariants()) {
        	System.out.println("Variant " + (++counter));
        	b.drawBoard();
        }		
	}
	@Test @Ignore
	public void testBoardLightRecursion4x4Multi() {
		Log.out("\n\n********************** Test testBoardLightRecursion4x4Multi **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.ROOK, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(4, 4, pcs1);
		int counter = 0;
		long time = 0;
		while (counter++ < 10) {
			long start = System.currentTimeMillis();
			board1.getArrangementVariants(false, false, false, false);	
			long end = System.currentTimeMillis();
			time += (end - start);
			System.out.println("Arrangement #" + counter + ": " + (end - start) + " ms");
		}	
		System.out.println("One Arragements takes " + (time/counter) + " ms");
	}	
	@Test 
	public void testBoardLightRecursion4x4() {
		Log.out("\n\n********************** Test testBoardLightRecursion4x4 **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.ROOK, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(4, 4, pcs1);
		board1.getArrangementVariants(false, false, true, false);

		int counter = 0;
        for (ChessBoardLight b : board1.getUniqueVariants()) {
        	System.out.println("Variant " + (++counter));
        	b.drawBoard();
        }		
	}

	@Test
	public void testBoardLightRecursion7x7() {
		Log.out("\n\n********************** Test testBoardLightRecursion7x7 **********************\n");
		char[] pcs1 = new char[]{
				ChessBoardLight.QUEEN,
				ChessBoardLight.QUEEN,
				ChessBoardLight.BISHOP,
				ChessBoardLight.BISHOP,
				ChessBoardLight.KING, 
				ChessBoardLight.KING,
				ChessBoardLight.KNIGHT 
			};
		ChessBoardLight board1 = new ChessBoardLight(7, 7, pcs1);
		board1.getArrangementVariants(false, false, true, false);

		int counter = 0;
        for (ChessBoardLight b : board1.getUniqueVariants()) {
        	System.out.println("Variant " + (++counter));
        	b.drawBoard();
        }		
	}}
