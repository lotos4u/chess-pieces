package com.lotos4u.tests.chess.boards;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

//public class MicroChessBoard extends AbstractChessBoard {
public class MicroChessBoard {
	protected int xSize;
	protected int ySize;
	protected int nPoints;
	protected int nPieces;
	protected int nCompact;
	protected char[] boardPiecesNames;
	protected char[] compactPieces;
	public static long equalsCounter;
	public static long hashCounter;
	
	//protected String asString;
	//public static boolean updateEqualsCounter = false;
	//public static boolean updateHashCounter = false;
	
	//public static int equalsCounter = 0;
	//public static int hashCounter = 0;
	
	public MicroChessBoard(ChessBoardLight input) {
		xSize = input.xSize;
		ySize = input.ySize;
		nPoints = xSize*ySize;
		nPieces = input.nPieces;
		nCompact = nPieces*2;
		
		//compactPieces = Arrays.copyOf(input.compactPieces, nCompact);
		//compactPieces = ArrayUtils.clone(input.compactPieces);
		
		boardPiecesNames = ArrayUtils.clone(input.boardPiecesNames);
		
		//asString = new String(compactPieces);
		//System.arraycopy(input.compactPieces[1], 0, compactPieces, nPieces, nPieces);
		//asString = new String(compactPieces);
		//System.out.println(Arrays.toString(input.boardPiecesNames));
		//System.out.println(Arrays.toString(input.compactPieces[0]));
		//System.out.println(Arrays.toString(input.compactPieces[1]));
		
		//System.out.println(Arrays.toString(compactPieces));
		//input.drawBoard();
		
		//boardAsString = new String(input.boardPiecesNames);
		/*
		*/
		
		//System.out.println(Arrays.toString(compactPieces));
		//Log.pause();
	}
	protected void updateCompactPieces(char[] names) {
		int index = 0;
		for (byte i = 0; i < nPoints; i++) {
			if (names[i] != AbstractChessBoard.NONAME) {
				compactPieces[index] = names[i];
				compactPieces[index+nPieces] = (char)i;
				index++;
			}
		}
	}
	public MicroChessBoard(ChessBoard input) {
		xSize = input.xSize;
		ySize = input.ySize;
		nPoints = xSize*ySize;
		nPieces = input.nPieces;
		boardPiecesNames = Arrays.copyOf(input.boardPiecesNames, nPoints);
		/*
		compactPieces = new int[input.nPieces*2];
		for (int i = 0; i < compactPieces.length; i += 2) {
			compactPieces[i] = boardPiecesNames[i];
			compactPieces[i+1] = boardPiecesNames[i];
		}
		*/
	}

	@Override
	public boolean equals(Object obj) {
		//if (updateEqualsCounter) 
		//equalsCounter++;
		//if (!(obj instanceof AbstractChessBoard))
		//	return false;
		//if (!getClass().equals(obj.getClass()))
		//	return false;
		MicroChessBoard b = (MicroChessBoard) obj;
		//return boardAsString.equals(b.boardAsString);
		//if (useCompact) {
		//return ArrayUtils.isEquals(compactPieces, b.compactPieces);
		//return Arrays.equals(compactPieces, b.compactPieces);
		return Arrays.equals(boardPiecesNames, b.boardPiecesNames);
	}

	@Override
	public int hashCode() {
		//if (updateHashCounter) 
		//hashCounter++;
		//if (useCompact) {
		//return Arrays.hashCode(compactPieces);
		return Arrays.hashCode(boardPiecesNames);
		//return asString.hashCode();
			//return asString.hashCode();
	//System.out.println("Local right hash=" + Arrays.hashCode(compactPieces));
		/*
		int res = 1;
		for (int i = 0; i < nCompact; i++)	
			res *= compactPieces[i]+1;//*i*compactPieces[i]*i + i;
		return res;
      */
	//return Arrays.hashCode(compactPieces);
		//return boardAsString.hashCode();
    }
}
