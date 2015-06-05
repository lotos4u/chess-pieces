package com.lotos4u.tests.chess.general;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.lotos4u.tests.chess.boards.ChessBoard;
import com.lotos4u.tests.chess.boards.ChessBoardLight;
import com.lotos4u.tests.chess.boards.ChessBoardLight.ComparablePoint;
import com.lotos4u.tests.chess.boards.MicroChessBoard;
import com.lotos4u.tests.chess.boards.Point;
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
        
        int res = board.arrangeVariants(true);

        int counter = 0;
        for (MicroChessBoard b : board.getVariants()) {
        	//System.out.println("Variant " + (counter++));
        	//System.out.println(b.getBoardViewAsString());;
        }
        Assert.assertEquals(4, res);
    }

    
    /**
     * 2 Rooks and 4 Knights on 4x4 Board
     */
    @Test @Ignore
    public void testRRNNNNon4x4_1(){
    	Log.out("\n\n********************** Test testRRNNNNon4x4_1 **********************\n");
    	ChessBoard board = new ChessBoard(4, 4);
    	board.addPiece(new Rook());
    	board.addPiece(new Rook());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
        int res = board.arrangeVariants(true);

        int counter = 0;
        for (MicroChessBoard b : board.getVariants()) {
        	//System.out.println("Variant " + (counter++));
        	//System.out.println(b.getBoardViewAsString());;
        }
        
        Assert.assertEquals(8, res);
    }
    @Test @Ignore
    public void testRRNNNNon4x4_2(){
    	Log.out("\n\n********************** Test testRRNNNNon4x4_2 **********************\n");
    	ChessBoard board = new ChessBoard(4, 4);
    	board.addPiece(new Rook());
    	board.addPiece(new King());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
    	board.addPiece(new Knight());
        int res = board.arrangeVariants(true);

        int counter = 0;
        for (MicroChessBoard b : board.getVariants()) {
        	//System.out.println("Variant " + (counter++));
        	//System.out.println(b.getBoardViewAsString());;
        }
        
        Assert.assertEquals(16, res);
    }

	@Test @Ignore
	public void testKRQQBBNon6x6(){
        Log.out("\n\n********************** Test testKRQQBBNon6x6 **********************\n");
        ChessBoard board = new ChessBoard(6, 6);
        board.addPiece(new King());
        board.addPiece(new Rook());
        board.addPiece(new Queen());
        board.addPiece(new Queen());
        board.addPiece(new Bishop());
        board.addPiece(new Bishop());
        board.addPiece(new Knight());
        int boards = board.arrangeVariants(true);
        
        int counter = 1;
        for (MicroChessBoard b : board.getVariants()) {
        	//System.out.println("Variant " + (counter++));
        	//System.out.println(b.getBoardViewAsString());;
        }
        
        Assert.assertTrue(boards > 0);
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
        int boards = board.arrangeVariants(true);
        int counter = 1;
        for (MicroChessBoard b : board.getVariants()) {
        	//System.out.println("Variant " + (counter++));
        	//System.out.println(b.getBoardViewAsString());;
        }
        
        Assert.assertTrue(boards > 0);
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Test @Ignore
	public void testIdexes(){
		Log.out("\n\n********************** Test testIdexes **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(3, 3, pcs1);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Log.out("(" + i + ", " + j + ")=" + board1.getIndexForPoint(i, j));
			}
		}
		for (int i = 0; i < 9; i++) {
			Log.out(board1.getPointForIndex(i)[0] + ", " + board1.getPointForIndex(i)[1]);	
		}
		
	}
	
	@Test @Ignore
	public void testBoardLightEquals() {
		Log.out("\n\n********************** Test testBoardLightEquals **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(3, 3, pcs1);
		char[] pcs2 = new char[]{ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight board2 = new ChessBoardLight(3, 3, pcs2);

		board1.setPiecePosition(0, 0);
		board1.setPiecePosition(1, 1);
		board1.setPiecePosition(2, 2);
		board2.setPiecePosition(0, 0);
		board2.setPiecePosition(1, 1);
		board2.setPiecePosition(2, 1);
		
		System.out.println("Equals=" + board1.equals(board2));
		System.out.println("H1=" + board1.hashCode() + ", H2=" + board2.hashCode());
		
		ChessBoard chessBoard1 = new ChessBoard(3, 3);
		chessBoard1.addPiece(new King());
		chessBoard1.addPiece(new King());
		chessBoard1.addPiece(new King());
		chessBoard1.getPiece(0).setPosition(1, 1, chessBoard1);
		chessBoard1.getPiece(1).setPosition(2, 2, chessBoard1);
		chessBoard1.getPiece(2).setPosition(3, 3, chessBoard1);
		
		ChessBoard chessBoard2 = new ChessBoard(3, 3);
		chessBoard2.addPiece(new King());
		chessBoard2.addPiece(new King());
		chessBoard2.addPiece(new King());
		chessBoard2.getPiece(0).setPosition(1, 1, chessBoard1);
		chessBoard2.getPiece(1).setPosition(2, 2, chessBoard1);
		chessBoard2.getPiece(2).setPosition(3, 1, chessBoard1);

		MicroChessBoard b1 = new MicroChessBoard(chessBoard1);
		MicroChessBoard b2 = new MicroChessBoard(chessBoard2);
		System.out.println("Equals=" + b1.equals(b2));
		System.out.println("H1=" + b1.hashCode() + ", H2=" + b2.hashCode());

		/*

		board1.setPiecePosition(0, 0, 0);
		board1.setPiecePosition(1, 0, 2);
		board1.setPiecePosition(2, 2, 1);
		board1.drawBoardViewAndTakeble();

		board2.setPiecePosition(0, 0, 0);
		board2.setPiecePosition(1, 0, 2);
		board2.setPiecePosition(2, 2, 1);
		board2.drawBoardViewAndTakeble();
		
*/
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
		
		/*
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
		*/
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
	public void testBoard3x3Multi() {
		Log.out("\n\n********************** Test testBoard3x3Multi **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight boardLight = new ChessBoardLight(3, 3, pcs1);
		int counter = 0;
		long time = 0;
		while (counter++ < 100) {
			long start = System.currentTimeMillis();
			boardLight.getArrangementVariants(false, false, false, false, false);
			long end = System.currentTimeMillis();
			time += (end - start);
			System.out.println("Arrangement #" + counter + ": " + (end - start) + " ms");
		}
		System.out.println("One Arragements takes " + (time/counter) + " ms");
	}
	
	@Test @Ignore
	public void testRKKon3x3_Light() {
		Log.out("\n\n********************** Test testRKKon3x3_Light **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KING, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(3, 3, pcs1);
		board1.getArrangementVariants(false, false, false, true, false);

		int counter = 0;
        //for (MicroChessBoard b : board1.getUniqueVariants()) {
        	//System.out.println("Variant " + (++counter));
        	//b.drawBoard();
        //}		
	}
	@Test @Ignore
	public void testBoard4x4Multi() {
		Log.out("\n\n********************** Test testBoard4x4Multi **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.ROOK, ChessBoardLight.ROOK, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT};
		ChessBoardLight boardLight = new ChessBoardLight(4, 4, pcs1);
		ChessBoard board = new ChessBoard(4, 4);
		board.addPiece(new Knight());
		board.addPiece(new Knight());
		board.addPiece(new Knight());
		board.addPiece(new Knight());
		board.addPiece(new Rook());
		board.addPiece(new Rook());
		
		int counter = 0;
		long time = 0, timeLight = 0;
		while (counter++ < 100) {
			
			long startLight = System.currentTimeMillis();
			boardLight.getArrangementVariants(false, false, false, false, false);	
			long endLight = System.currentTimeMillis();
			timeLight += (endLight - startLight);
			
			long start = System.currentTimeMillis();
			board.arrangeVariants(false);	
			long end = System.currentTimeMillis();
			time += (end - start);

			System.out.println("Arrangement #" + counter + ": " + (end - start) + " ms, (Light: " + (endLight - startLight) + " ms)");
		}	
		System.out.println("One Arragements takes " + (time/counter) + " ms (Light " + (timeLight/counter) + " ms)");
	}	
	
	@Test @Ignore
	public void testBoard6x6Multi() {
		Log.out("\n\n********************** Test testBoard6x6Multi **********************\n");
		char[] pcs1 = new char[]{
				ChessBoardLight.QUEEN,
				ChessBoardLight.QUEEN,
				ChessBoardLight.ROOK, 
				ChessBoardLight.BISHOP,
				ChessBoardLight.BISHOP,
				ChessBoardLight.KING,
				ChessBoardLight.KNIGHT 
			};
		ChessBoardLight boardLight = new ChessBoardLight(6, 6, pcs1);		
		ChessBoard board = new ChessBoard(6, 6);
		board.addPiece(new Queen());
		board.addPiece(new Queen());
		board.addPiece(new Rook());
		board.addPiece(new Bishop());
		board.addPiece(new Bishop());
		board.addPiece(new King());
		board.addPiece(new Knight());
		
		int counter = 0;
		long time = 0, timeLight = 0;
		while (counter++ < 100) {
			
			ChessBoardLight.sortPoints = false;
			long start = System.currentTimeMillis();
			boardLight.getArrangementVariants(false, false, false, false, false);
			//board.arrangeVariants(false);	
			long end = System.currentTimeMillis();
			time += (end - start);
			
			ChessBoardLight.sortPoints = true;
			long startLight = System.currentTimeMillis();
			boardLight.getArrangementVariants(false, false, false, false, false);	
			long endLight = System.currentTimeMillis();
			timeLight += (endLight - startLight);

			System.out.println("Arrangement #" + counter + " FIRST: " + (end - start) + " ms, SECOND: " + (endLight - startLight) + " ms");
		}	
		System.out.println("One Arragements takes FIRST: " + (time/counter) + " ms SECOND: " + (timeLight/counter) + " ms");
	}		
	@Test @Ignore
	public void testBoard7x7Multi() {
		Log.out("\n\n********************** Test testBoard7x7Multi **********************\n");
		char[] pcs1 = new char[]{
				ChessBoardLight.QUEEN,
				ChessBoardLight.QUEEN,
				ChessBoardLight.BISHOP,
				ChessBoardLight.BISHOP,
				ChessBoardLight.KING, 
				ChessBoardLight.KING,
				ChessBoardLight.KNIGHT 
			};
		ChessBoardLight boardLight = new ChessBoardLight(7, 7, pcs1);		

		int counter = 0;
		long time = 0, timeLight = 0;
		while (counter++ < 100) {
			/*
			ChessBoardLight.sortPoints = false;
			long start = System.currentTimeMillis();
			boardLight.getArrangementVariants(false, false, false, false, false);
			//board.arrangeVariants(false);	
			long end = System.currentTimeMillis();
			time += (end - start);
			*/
			ChessBoardLight.sortPoints = true;
			long startLight = System.currentTimeMillis();
			int n = boardLight.getArrangementVariants(false, false, false, false, false);	
			long endLight = System.currentTimeMillis();
			timeLight += (endLight - startLight);

			System.out.println("Arrangement #" + counter + ", N = " + n + ", " + (endLight - startLight) + " ms");
			//System.out.println("Arrangement #" + counter + " FIRST: " + (end - start) + " ms, SECOND: " + (endLight - startLight) + " ms");
		}	
		System.out.println("One Arragements takes: " + (timeLight/counter) + " ms");
	}		
	@Test @Ignore
	public void testRRNNNNon4x4_1_Light() {
		Log.out("\n\n********************** Test testRRNNNNon4x4_1_Light **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.ROOK, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(4, 4, pcs1);
		board1.getArrangementVariants(false, false, false, true, false);

		int counter = 0;
        //for (MicroChessBoard b : board1.getUniqueVariants()) {
        	//System.out.println("Variant " + (++counter) + ", H=" + b.hashCode());
        	//b.draw();
        //}
	}
	@Test @Ignore
	public void testRRNNNNon4x4_2_Light() {
		Log.out("\n\n********************** Test testRRNNNNon4x4_2_Light **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KING, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(4, 4, pcs1);
		board1.getArrangementVariants(false, false, false, true, false);

		int counter = 0;
        //for (MicroChessBoard b : board1.getUniqueVariants()) {
        	//System.out.println("Variant " + (++counter));
        	//b.drawBoard();
        //}		
	}
	@Test @Ignore 
	public void testRRNNNNon4x4_3_Light() {
		Log.out("\n\n********************** Test testRRNNNNon4x4_3_Light **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.BISHOP, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(4, 4, pcs1);
		board1.getArrangementVariants(false, false, false, true, false);

		int counter = 0;
        //for (MicroChessBoard b : board1.getUniqueVariants()) {
        	//System.out.println("Variant " + (++counter));
        	//b.drawBoard();
        //}		
	}

	@Test @Ignore
	public void testBoardLightRecursion5x5() {
		Log.out("\n\n********************** Test testBoardLightRecursion5x5 **********************\n");
		char[] pcs1 = new char[]{ChessBoardLight.KNIGHT, ChessBoardLight.KNIGHT, ChessBoardLight.BISHOP, ChessBoardLight.ROOK, ChessBoardLight.ROOK, ChessBoardLight.ROOK};
		ChessBoardLight board1 = new ChessBoardLight(5, 5, pcs1);
		board1.getArrangementVariants(false, false, false, true, false);

		int counter = 0;
        //for (MicroChessBoard b : board1.getUniqueVariants()) {
        	//System.out.println("Variant " + (++counter));
        	//b.drawBoard();
        //}		
	}

	@Test @Ignore
	public void testKRQQBBNon6x6_Light() {
		Log.out("\n\n********************** Test testKRQQBBNon6x6_Light **********************\n");
		char[] pcs1 = new char[]{
				ChessBoardLight.QUEEN,
				ChessBoardLight.QUEEN,
				ChessBoardLight.ROOK, 
				ChessBoardLight.BISHOP,
				ChessBoardLight.BISHOP,
				ChessBoardLight.KING,
				ChessBoardLight.KNIGHT 
			};
		ChessBoardLight board1 = new ChessBoardLight(6, 6, pcs1);
		board1.getArrangementVariants(false, false, false, true, false);

		int counter = 0;
        //for (MicroChessBoard b : board1.getUniqueVariants()) {
        	//System.out.println("Variant " + (++counter));
        	//b.drawBoard();
        //}	
	}
	
	@Test @Ignore
	public void testKKQQBBNon7x7_Light() {
		Log.out("\n\n********************** Test testKKQQBBNon7x7_Light **********************\n");
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
		board1.getArrangementVariants(false, false, true, true, false);

		int counter = 0;
        //for (MicroChessBoard b : board1.getUniqueVariants()) {
        	//System.out.println("Variant " + (++counter));
        	//b.drawBoard();
        //}	
		
/*

MY HASH
N unique = 500000 from 2013791 variants, Hash=0, Equals=10779124, 2325 ms
N unique = 1000000 from 4189840 variants, Hash=0, Equals=29711120, 5341 ms
N unique = 1500000 from 6726828 variants, Hash=0, Equals=62748293, 10698 ms
N unique = 2000000 from 9588775 variants, Hash=0, Equals=113277668, 16304 ms
N unique = 2500000 from 13245216 variants, Hash=0, Equals=191879227, 24058 ms
N unique = 3000000 from 20482979 variants, Hash=0, Equals=434754634, 43051 ms
Number of all variants = 24510624
Arrangements time 55025 ms
Number of unique variants = 3063828
Number of Equals calls = 584938165

ARRAYS HASH
N unique = 500000 from 2013791 variants, Hash=0, Equals=1513812, 1758 ms
N unique = 1000000 from 4189840 variants, Hash=0, Equals=3189969, 3551 ms
N unique = 1500000 from 6726828 variants, Hash=0, Equals=5227182, 6849 ms
N unique = 2000000 from 9588775 variants, Hash=0, Equals=7589510, 9227 ms
N unique = 2500000 from 13245216 variants, Hash=0, Equals=10746628, 12274 ms
N unique = 3000000 from 20482979 variants, Hash=0, Equals=17486045, 18627 ms
Number of all variants = 24510624
Arrangements time 22234 ms
Number of unique variants = 3063828
Number of Equals calls = 21450905


 * */		
		
        /*
LOTOS
Chess complexity is = 343
Number of all variants = 24510624
Number of unique variants = 3063828
Arragements time 267504 ms
Sorting time = 1745755 ms
Full time = 2013259 ms
Number of recursive calls = 13343298
Number of recursive calls per ms = 49.0
Number of put tries = 76037489
Number of Equals calls = 1374903753
Number of HashCode calls = 24510642 

Number of all variants = 24510624
Arrangements time 449563 ms
Number of unique variants = 3063828
Full time = 449563 ms
Number of recursive calls = 8366386
Number of recursive calls per ms = 18.0
Number of put tries = 54731825
Number of Equals calls = 0
Number of HashCode calls = 0

Number of all variants = 24510624
Arrangements time 461497 ms
Number of unique variants = 3063828
Full time = 461497 ms
Number of recursive calls = 8366386
Number of recursive calls per ms = 18.0
Number of put tries = 54731825
Number of Equals calls = 0
Number of HashCode calls = 0

Number of all variants = 24510624
Arrangements time 458255 ms
Number of unique variants = 3063828
Full time = 458255 ms
Number of recursive calls = 8366386
Number of recursive calls per ms = 18.0
Number of put tries = 54731825
Number of Equals calls = 0
Number of HashCode calls = 0

Number of all variants = 24510624
Arrangements time 23230 ms
Number of unique variants = 3063828
Full time = 23230 ms
Number of recursive calls = 0
Number of recursive calls per ms = 0.0
Number of put tries = 0
Number of Equals calls = 21462687
Number of HashCode calls = 24510642

HTPC
Number of all variants = 24510624
Arragements time 889820 ms
Number of unique variants = 3063828
Full time = 889820 ms
Number of recursive calls = 8366386
Number of recursive calls per ms = 9.0
Number of put tries = 54731825
Number of Equals calls = 0
Number of HashCode calls = 0

Number of all variants = 24510624
Arragements time 856887 ms
Number of unique variants = 3063828
Full time = 856887 ms
Number of recursive calls = 8366386
Number of recursive calls per ms = 9.0
Number of put tries = 54731825
Number of Equals calls = 0
Number of HashCode calls = 0

E-LOTOS-i3
Number of all variants = 24510624
Arrangements time 490545 ms
Number of unique variants = 3063828
Full time = 490545 ms
Number of recursive calls = 8366386
Number of recursive calls per ms = 17.0
Number of put tries = 54731825
Number of Equals calls = 0
Number of HashCode calls = 0

Number of all variants = 24510624
Arrangements time 86709 ms
Number of unique variants = 3063828
Full time = 86709 ms
Number of recursive calls = 0
Number of recursive calls per ms = 0.0
Number of put tries = 0
Number of Equals calls = 21462687
Number of HashCode calls = 24510642

         * */
	}
	@Test @Ignore
	public void testUpdateUniversalTakeble_Light() {
		Log.out("\n\n********************** Test testUpdateUniversalTakeble_Light **********************\n");
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
		long start = System.currentTimeMillis();
		board1.updateUniversalTakeble();
		long finish = System.currentTimeMillis();
		Log.out("Updating time = " + (finish-start) + " ms");
		
		board1.drawTakebleForPiece(0, 0);
		board1.drawTakebleForPiece(0, 1);
		board1.drawTakebleForPiece(0, 2);
		board1.drawTakebleForPiece(0, 3);
		board1.drawTakebleForPiece(0, 4);
		board1.drawTakebleForPiece(0, 5);
		board1.drawTakebleForPiece(0, 6);
		board1.drawTakebleForPiece(0, 7);
	}
	
	@Test @Ignore
	public void testNeighs() {
		Log.out("\n\n********************** Test testNeighs **********************\n");
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
		for (int i = 0; i < board1.getNPoints(); i++) {
			int[] point = board1.getPointForIndex(i);
			System.out.println("(" + point[0] + "," + point[1] + ") - " + board1.getNeighborsNumber(i));
		}
		/*
		ChessBoardLight.sortPoints = true;
		List<ComparablePoint> free = board1.getFreePoints();
		board1.updateNeighborsNumber();
		for (ComparablePoint i : free) {
			System.out.println(i.index);
		}
		*/
	}

	public void testCompactObjects(Object testObject1, Object testObject2) {
		long number = 1000000000l;
		System.out.println("Results for " + testObject1.getClass());
		System.out.println("Number of cycles = " + number);
		long start = System.nanoTime();
		for (long i = 0; i < number; i++) {
			int hash = testObject1.hashCode();
		}
		long finish = System.nanoTime();
		long hashTime = finish - start;
		

		start = System.nanoTime();
		for (long i = 0; i < number; i++) {
			boolean equal = testObject1.equals(testObject2);
		}
		finish = System.nanoTime();
		long equalsTime = finish - start;

		
		System.out.println("Full Hash Time = " + ((double)hashTime)/1000000 + " ms");
		System.out.println("Single Hash Time = " + ((double)hashTime)/((double)number) + " ns");
		System.out.println("Full Equals Time = " + ((double)equalsTime)/1000000 + " ms");
		System.out.println("Single Equals Time = " + ((double)equalsTime)/((double)number) + " ns");
		System.out.println();
		
	}
	
	@Test @Ignore
	public void testCompactObjects() {
		char[] charArray = new char[] {'q','w','e','r','t','y','u','i','q','w','e','r','t','y','u','i'};
		String stringObject1 = new String(charArray);
		String stringObject2 = new String(charArray);
		
		testCompactObjects(stringObject1, stringObject2);
		
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
		
		MicroChessBoard mb1 = new MicroChessBoard(board1);
		MicroChessBoard mb2 = new MicroChessBoard(board1);
		testCompactObjects(mb1, mb2);
	}
	
	@Test
	public void testComplexComparable() {
		//testUpdateUniversalTakeble_Light();
		//testNeighs();
		
		//testRKKon3x3();
		//testRKKon3x3_Light();
		//testRRNNNNon4x4_1();
		//testRRNNNNon4x4_1_Light();
		//testRRNNNNon4x4_2();
		//testRRNNNNon4x4_2_Light();
		//testKRQQBBNon6x6();
		//testKRQQBBNon6x6_Light();
		
		
		
		//testBoard4x4Multi();
		//testBoard6x6Multi();
		testBoard7x7Multi();
		//testBoardLightRecursion4x4Multi();
		
		//testCompactObjects();
		//testKKQQBBNon7x7_Light();
		//testKKQQBBNon7x7();
		
	}

}
