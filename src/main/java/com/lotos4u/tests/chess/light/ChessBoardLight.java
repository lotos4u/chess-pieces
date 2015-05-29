package com.lotos4u.tests.chess.light;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.lotos4u.tests.chess.general.Log;


public class ChessBoardLight {
	public static final char KING = 'K';
	public static final char QUEEN = 'Q';
	public static final char KNIGHT = 'N';
	public static final char ROOK = 'R';
	public static final char BISHOP = 'B';
	
	public static final char KING_TAKES = 'k';
	public static final char QUEEN_TAKES = 'q';
	public static final char KNIGHT_TAKES = 'n';
	public static final char ROOK_TAKES = 'r';
	public static final char BISHOP_TAKES = 'b';
	
	public static final char NONAME = ' ';
	public static final int EMPTY = -1;

	private Set<ChessBoardLight> uniqueVariants = new HashSet<ChessBoardLight>();
	private List<ChessBoardLight> allVariants = new LinkedList<ChessBoardLight>();
	
	private char[][] boardView; //Letters (pieces names) at the points
	private int[][] board; //numbers (pieces indexes) at the points
	private int[][] boardTakeble; //incremental takebility data. If some piece take point value++
	private int[][][] boardsGray;
	private char[] pieces; //all pieces names
	private int[][] piecesPoints;
	private int xSize;
	private int ySize;
	private int nPieces;
	
	private long recursionStart;
	private int variantsCounter;
	private int uniqueCounter;
	private int callCounter;
	private int putCounter;
	private int rc;
	private boolean updateGray = false;
	private boolean updateTakeble = false;
	private boolean testBeforePut = true;
	

	public ChessBoardLight(ChessBoardLight input) {
		copyFromBoard(input);
	}
	
	private void copyFromBoard(ChessBoardLight input) {
		xSize = input.xSize;
		ySize = input.ySize;
		nPieces = input.nPieces;
		boardView = new char[xSize][ySize];
		board = new int[xSize][ySize];
		boardTakeble = new int[xSize][ySize];
		boardsGray = new int[nPieces][xSize][ySize];
		pieces = new char[nPieces];
		piecesPoints = new int[nPieces][2];
		for (int i = 0; i < nPieces; i++) {
			pieces[i] = input.pieces[i];
			piecesPoints[i][0] = input.piecesPoints[i][0];
			piecesPoints[i][1] = input.piecesPoints[i][1];
		}
		for (int i = 0; i < xSize; i++) {
			for (int j = 0; j < ySize; j++) {
				boardView[i][j] = input.boardView[i][j];
				board[i][j] = input.board[i][j];
				boardTakeble[i][j] = input.boardTakeble[i][j];
				for (int p = 0; p < nPieces; p++)
					boardsGray[p][i][j] = input.boardsGray[p][i][j];
			}
		}		
	}
	
	public ChessBoardLight(int x, int y, char[] inPieces) {
		xSize = x;
		ySize = y;
		nPieces = inPieces.length;
		boardView = new char[x][y];
		board = new int[x][y];
		boardTakeble = new int[x][y];
		boardsGray = new int[nPieces][x][y];
		pieces = new char[nPieces];
		piecesPoints = new int[nPieces][2];
		for (int i = 0; i < nPieces; i++) {
			pieces[i] = inPieces[i];
			piecesPoints[i][0] = EMPTY;
			piecesPoints[i][1] = EMPTY;
		}
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				boardView[i][j] = NONAME;
				board[i][j] = EMPTY;
				boardTakeble[i][j] = EMPTY;
				for (int p = 0; p < nPieces; p++)
					boardsGray[p][i][j] = EMPTY;
			}
		}
	}
	
	
	public Set<ChessBoardLight> getUniqueVariants() {
		return uniqueVariants;
	}
	
	public List<ChessBoardLight> getAllVariants() {
		return allVariants;
	}

	public int getArrangementVariants(boolean log, boolean logInner, boolean logExtra, boolean pauses) {
		recursionStart = System.currentTimeMillis();
		callCounter = 0;
		putCounter = 0;
		variantsCounter = 0;
		uniqueCounter = 0;
		uniqueVariants.clear();
		allVariants.clear();
		arrangeRecursively(log, logInner, logExtra, pauses);
		long end = System.currentTimeMillis();
		if (logExtra) System.out.println("Arragements takes " + (end-recursionStart) + " ms");
		if (logExtra) System.out.println("Number of unique variants = " + uniqueVariants.size());
		if (logExtra) System.out.println("Number of all variants = " + variantsCounter);
		if (logExtra) System.out.println("Number of recursive calls = " + callCounter);
		if (logExtra) System.out.println("Number of put tries = " + putCounter);
		if (logExtra) System.out.println();
		return uniqueVariants.size();		
	}

	public boolean tryToPut(int pieceIndex, int x, int y, boolean log) {
		boolean putted = false;
		putCounter++;
		if (testBeforePut) {
			char piece = pieces[pieceIndex];
			for (int testX = 0; testX < xSize; testX++)
				for (int testY = 0; testY < ySize; testY++) {
					if ((board[testX][testY] != EMPTY) && canPieceTakePoint(piece, x, y, testX, testY))
					{return false;}	
				}
			putted = true;
			setPiecePosition(pieceIndex, x, y);
		} else {
			setPiecePosition(pieceIndex, x, y);
			putted = isValidQuick();
		}
		if (putted) {
			if(log)System.out.println("[" + rc + "] Putted " + pieces[pieceIndex]);
		} else {
			if(log)System.out.println("[" + rc + "] Not Putted " + pieces[pieceIndex]);
			dropPiece(pieceIndex);
		}
		return putted;
	}
	
	public int[] getFirstFreePoint() {
		List<int[]> l = getFreePoints();
		if (l.size() > 0)
			return l.get(0);
		return new int[] {EMPTY, EMPTY};
	}
	
	public List<int[]> getFreePoints() {
		List<int[]> res = new ArrayList<int[]>();
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				if (isEmpty(x, y) && !isTakeble(x, y)) {
					res.add(new int[] {x, y});
				}
			}
		return res;
	}
	
	private boolean isTakeble(int x, int y) {
		if (updateTakeble)
			return boardTakeble[x][y] != EMPTY;
		else {
			for (int p = 0; p < nPieces; p++) {
				char piece = pieces[p];
				int[] point = piecesPoints[p];
				if (point[0] != EMPTY) {
					if (canPieceTakePoint(piece, point[0], point[1], x, y))
						return true;
				}
			}
			return false;
		}
	}

	public int getFistUnpositionedIndex() {
		for (int index = 0; index < nPieces; index++) {
			if (piecesPoints[index][0] == EMPTY) {
				return index;
			}
		}
		return EMPTY;
	}

	public boolean arrangeRecursively(boolean log, boolean logInner, boolean logExtra, boolean pauses){
		callCounter++;
		rc++;
		boolean res = false;
		boolean putted = false;
		
		if (logExtra) System.out.println("[" + rc  + "] N full = " + variantsCounter + ", N unique = " + uniqueCounter + ", CallCounter = " + callCounter );
		
		int pieceIndex = getFistUnpositionedIndex();
		
		if (pieceIndex == EMPTY) {
			if (log) System.out.println("[" + rc + "] No pieces! All arranged!");
			rc--;
			return true;
		}
		
		if (log) System.out.println("[" + rc + "] We have to put " + pieces[pieceIndex] + "(" + pieceIndex +  ")");
		
		List<int[]> free = getFreePoints();
		
		if (free.size() < 1) {
			if (log) System.out.println("[" + rc + "] No free points for " + pieces[pieceIndex] + "(" + pieceIndex +  ")!");
			rc--;
			return false;
		}
		
		if (log) System.out.println("[" + rc + "] We have " + free.size() + " points for " + pieces[pieceIndex]);
		//if (pauses) drawBoardAndBoardViewAndTakebleAndFree();
		//if (pauses) Log.pause();
		//if (log) drawBoardAndTakeble();
		//if (log) System.out.println("[" + rc + "] Pieces: " + getPiecesAsString());
		//if (log) System.out.println("[" + rc + "] Free: " + getPointsAsString(free));
		
		for (int[] point : free) {
		//int[] point = getFirstFreePoint();
		//while (point[0] != EMPTY) {
			int x = point[0];
			int y = point[1];
			//if ((boardTakeble[x][y] != EMPTY) || (board[x][y] != EMPTY))
			//	continue;
			res = false;
			if (log) System.out.println("[" + rc + "] Try " + pieces[pieceIndex] + " at (" + x + ", " + y + ")");
			putted = tryToPut(pieceIndex, x, y, logInner);
			if (putted) {
				//if (pauses) System.out.println("Putted " + pieces[pieceIndex] + "(" + pieceIndex + ")");
				if (pauses) drawBoardAndBoardViewAndTakebleAndFree();
				//if (pauses) drawBoard();
				if (pauses) Log.pause();
				res = arrangeRecursively(log, logInner, logExtra, pauses); 
				if (res) {
					if (log) System.out.println("[" + rc + "] No pieces!");
					ChessBoardLight b = new ChessBoardLight(this);
					boolean isUnique = uniqueVariants.add(b);
					variantsCounter++;
					//if (pauses) System.out.println("Success!!!");
					if (pauses) drawBoardAndBoardViewAndTakebleAndFree();
					//if (pauses) drawBoard();
					if (pauses) Log.pause();
					if (isUnique) {
						uniqueCounter++;
						//drawBoard();
						//Log.pause();
					}
				} 				
			}
			dropPiece(pieceIndex);
			//point = getFirstFreePoint();
		}
		res = isArranged();
		if (!res) {
			dropPiece(pieceIndex);
		}
		if (log) System.out.println("[" + rc + "] Tested " + free.size() + " free points for " + pieces[pieceIndex] + "(" + pieceIndex +  "). Result = " + res);
		rc--;
		
		return res;				
	}

	public List<int[]> getTakebleForPiece(char piece, int newX, int newY) {
		List<int[]> res = new ArrayList<int[]>();
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < xSize; y++)
				if (canPieceTakePoint(piece, newX, newY, x, y)) {
					res.add(new int[] {x, y});
				}
		return res;
	}

	public boolean isArrangedAndValid() {
		return isArranged() && isValid();
	}
	
	public boolean canPieceTakePoint(char piece, int hisX, int hisY, int testX, int testY) {
		if ((isBishop(piece) && canBishopTakePoint(hisX, hisY, testX, testY)) ||
				   (isKing(piece) && canKingTakePoint(hisX, hisY, testX, testY)) ||
				   (isKnight(piece) && canKnightTakePoint(hisX, hisY, testX, testY)) ||
				   (isRook(piece) && canRookTakePoint(hisX, hisY, testX, testY)) ||
				   (isQueen(piece) && canQueenTakePoint(hisX, hisY, testX, testY))) {
					return true;
					}	
		return false;
	}
	
	public boolean isValidQuick() {
		if (!updateTakeble)
			return isValid();
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) 
				if ((board[x][y] != EMPTY) && (boardTakeble[x][y] != EMPTY))
					return false;
		return true;
	}
	public boolean isValid() {
		for (int p1 = 0; p1 < nPieces; p1++) {
			if (piecesPoints[p1][0] != EMPTY)
				for (int p2 = 0; p2 < nPieces; p2++) {
					int x1 = piecesPoints[p1][0];
					int y1 = piecesPoints[p1][1];
					int x2 = piecesPoints[p2][0];
					int y2 = piecesPoints[p2][1];
					if (!(piecesPoints[p2][0] != EMPTY) && canPieceTakePoint(pieces[p1], x1, y1, x2, y2))
						return false;
				}
		}
		return true;
	}
	
	public boolean isArranged() {
		for (int i = 0; i < nPieces; i++)
			if (piecesPoints[i][0] == EMPTY)
				return false;
		return true;
	}
	private void updateGrayForPut(int pieceIndex, int newX, int newY) {
		if (!updateGray)return;
		char piece = pieces[pieceIndex];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++)
				if (canPieceTakePoint(piece, newX, newY, x, y))
					boardsGray[pieceIndex][x][y]++;
	}
	private void updateGrayForDrop(int pieceIndex, int oldX, int oldY) {
		if (!updateGray)return;
		char piece = pieces[pieceIndex];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++)
				if (canPieceTakePoint(piece, oldX, oldY, x, y))
					boardsGray[pieceIndex][x][y]--;
	}
	private void updateTakebleForPut(char piece, int newX, int newY) {
		if(!updateTakeble)return;
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++)
				if (canPieceTakePoint(piece, newX, newY, x, y))
					boardTakeble[x][y]++;
	}
	
	private void updateTakebleForDrop(char piece, int oldX, int oldY) {
		if(!updateTakeble)return;
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++)
				if (canPieceTakePoint(piece, oldX, oldY, x, y))
					boardTakeble[x][y]--;
	}
	
	private void clearBoardPoint(int x, int y) {
		int pieceIndex = board[x][y];
		if (pieceIndex != EMPTY) {
			updateTakebleForDrop(pieces[pieceIndex], x, y);
			updateGrayForDrop(pieceIndex, x, y);
			piecesPoints[pieceIndex][0] = EMPTY;
			piecesPoints[pieceIndex][1] = EMPTY;
		}
		boardView[x][y] = NONAME;
		board[x][y] = EMPTY;
	}
	
	public void dropPieces() {
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++)
				clearBoardPoint(x, y);
	}
	
	public void dropPiece(int pieceIndex) {
		int point[] = piecesPoints[pieceIndex];
		if (point[0] != EMPTY) {
			clearBoardPoint(point[0], point[1]);	
		}
	}

	public void setPiecePosition(int pieceIndex, int x, int y) {
		if (!isEmpty(x, y))
			clearBoardPoint(x, y);
		if (piecesPoints[pieceIndex][0] != EMPTY)
			clearBoardPoint(piecesPoints[pieceIndex][0], piecesPoints[pieceIndex][0]);
		boardView[x][y] = pieces[pieceIndex];
		board[x][y] = pieceIndex;
		piecesPoints[pieceIndex][0] = x;
		piecesPoints[pieceIndex][1] = y;
		updateTakebleForPut(pieces[pieceIndex], x, y);
		updateGrayForPut(pieceIndex, x, y);
	}
	
	private boolean isEmpty(int x, int y) {
		return board[x][y] == EMPTY;
	}
	
	private boolean isBishop(char pieceType) {
		return BISHOP == pieceType;
	}
	private boolean isRook(char pieceType) {
		return ROOK == pieceType;
	}
	private boolean isKing(char pieceType) {
		return KING == pieceType;
	}
	private boolean isQueen(char pieceType) {
		return QUEEN == pieceType;
	}
	private boolean isKnight(char pieceType) {
		return KNIGHT == pieceType;
	}
	
	
	private boolean canBishopTakePoint(int hisX, int hisY, int testX, int testY) {
		int diffX = Math.abs(hisX - testX);
		int diffY = Math.abs(hisY - testY);
		return (diffY == diffX) && (diffX > 0);
	}
	private boolean canRookTakePoint(int hisX, int hisY, int testX, int testY) {
		return (hisY == testY) ^ (hisX == testX);
	}
	private boolean canKingTakePoint(int hisX, int hisY, int testX, int testY) {
		int diffX = Math.abs(hisX - testX);
		int diffY = Math.abs(hisY - testY);
		return (diffY <= 1) && (diffX <= 1) && ((diffX + diffY) > 0);
	}
	private boolean canKnightTakePoint(int hisX, int hisY, int testX, int testY) {
		int diffX = Math.abs(hisX - testX);
		int diffY = Math.abs(hisY - testY);
		return ((diffX == 1) && (diffY == 2)) || ((diffX == 2) && (diffY == 1));
	}
	private boolean canQueenTakePoint(int hisX, int hisY, int testX, int testY) {
		return canRookTakePoint(hisX, hisY, testX, testY) ||
				canBishopTakePoint(hisX, hisY, testX, testY);
	}
	
	public int[][] getBoardFree() {
		int[][] b = new int[xSize][ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				boolean empty = board[x][y] == EMPTY;
				boolean notTakeble;
				if(updateTakeble) {
					notTakeble = boardTakeble[x][y] == EMPTY;	
				} else {
					notTakeble = true;
					for (int p = 0; p < nPieces; p++) {
						char piece = pieces[p];
						int hisX = piecesPoints[p][0];
						int hisY = piecesPoints[p][1];
						if (canPieceTakePoint(piece, hisX, hisY, x, y)) {
							notTakeble = false;
							break;
						}
					}
					
				}
				if (empty && notTakeble)
					b[x][y] = 0;
				else
					b[x][y] = EMPTY;
			}
		return b;
	}
    private int[][] getBoardTakeble() {
		int[][] b = new int[xSize][ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				for (int p = 0; p < nPieces; p++) {
					char piece = pieces[p];
					int hisX = piecesPoints[p][0];
					int hisY = piecesPoints[p][1];
					if (canPieceTakePoint(piece, hisX, hisY, x, y))
						b[x][y]++;
				}
			}
		return b;
	}
	public char[][] getBoardView() {
		char[][] b = new char[xSize][ySize];
		for (int x = 0; x < xSize; x++)
			for (int y = 0; y < ySize; y++) {
				int index = board[x][y];
				if (index != EMPTY)
					b[x][y] = pieces[index];
				else
					b[x][y] = NONAME;
			}
		return b;
	}
    /**
     * Draw a board into System.out
     */
    public void drawBoard() {
    	System.out.println(getArrayAsString(getBoardView()));
    }
    public void drawTakeble() {
    	if (updateTakeble)
    		System.out.println(getArrayAsString(boardTakeble));
    	else
    		System.out.println(getArrayAsString(getBoardTakeble()));
    }
	public void drawBoardViewAndTakeble() {
    	System.out.println(getBoardViewAndTakebleAsString());
    }
    public void drawBoardAndTakeble() {
    	System.out.println(getBoardAndTakebleAsString());
    }
    public void drawBoardAndBoardViewAndTakeble() {
    	System.out.println(getBoardAndBoardViewAndTakebleAsString());
    }
    public void drawBoardAndBoardViewAndTakebleAndFree() {
    	System.out.println(getBoardAndBoardViewAndTakebleAndFreeAsString());
    }
    private String getBoardAndBoardViewAndTakebleAndFreeAsString() {
    	String res = "";
    	List<String> boardViewStrings = getArrayAsStrings(getBoardView());
    	List<String> boardStrings = getArrayAsStrings(board);
    	List<String> takebleStrings = getArrayAsStrings(boardTakeble);
    	List<String> freeStrings = getArrayAsStrings(getBoardFree());
    	for (int i = 0; i < boardStrings.size(); i++) {
    		res += boardViewStrings.get(i) + "    " + boardStrings.get(i) + "    " + takebleStrings.get(i) + "    " + freeStrings.get(i) + "\n";
		}
    	res += "\n";
    	return res;
    }
    private String getBoardAndBoardViewAndTakebleAsString() {
    	String res = "";
    	List<String> boardViewStrings = getArrayAsStrings(getBoardView());
    	List<String> boardStrings = getArrayAsStrings(board);
    	List<String> takebleStrings = getArrayAsStrings(boardTakeble);
    	for (int i = 0; i < boardStrings.size(); i++) {
    		res += boardViewStrings.get(i) + "    " + boardStrings.get(i) + "    " + takebleStrings.get(i) + "\n";
		}
    	res += "\n";
    	return res;
    }

    private String getBoardViewAndTakebleAsString() {
    	String res = "";
    	List<String> boardStrings = getArrayAsStrings(getBoardView());
    	List<String> takebleStrings = getArrayAsStrings(boardTakeble);
    	for (int i = 0; i < boardStrings.size(); i++) {
    		res += boardStrings.get(i) + "    " + takebleStrings.get(i) + "\n";
		}
    	res += "\n";
    	return res;
    }
    private String getBoardAndTakebleAsString() {
    	String res = "";
    	List<String> boardStrings = getArrayAsStrings(board);
    	List<String> takebleStrings = getArrayAsStrings(boardTakeble);
    	for (int i = 0; i < boardStrings.size(); i++) {
    		res += boardStrings.get(i) + "    " + takebleStrings.get(i) + "\n";
		}
    	res += "\n";
    	return res;
    }
    private String getArrayAsString(int[][] input) {
    	String res = "";
    	List<String> pre = getArrayAsStrings(input);
    	for (Iterator<String> iterator = pre.iterator(); iterator.hasNext();) {
			 res += (iterator.next() + "\n");
		}
    	return res;
    }
    private String getArrayAsString(char[][] input) {
    	String res = "";
    	List<String> pre = getArrayAsStrings(input);
    	for (Iterator<String> iterator = pre.iterator(); iterator.hasNext();) {
			 res += (iterator.next() + "\n");
		}
    	return res;
    }
    private List<String> getArrayAsStrings(int[][] input) {
    	int xMax = input.length;
    	int yMax = (xMax > 0) ? input[0].length : 0;
    	List<String> resList = new ArrayList<String>();
    	String res1 = "  ";
    	for (int y = 0; y < yMax; y++) {
    		res1 += (" " + y + " ");
    	}
    	resList.add(res1);
    	String res2[] = new String[xMax];
    	for (int x = 0; x < xMax; x++) {
    		res2[x] = "";
    		String num = "" + x;
    		num = (x < 10) ? " " + num : num;
    		res2[x] += num;
    		for (int y = 0; y < yMax; y++) {
    			String v = (input[x][y] >= 0) ? ("" + input[x][y]) : " ";
    			res2[x] += ("[" + v + "]");
    		}
    		resList.add(res2[x]);
    	}
    	return resList;
    }    
    private List<String> getArrayAsStrings(char[][] input) {
    	int xMax = input.length;
    	int yMax = (xMax > 0) ? input[0].length : 0;
    	List<String> resList = new ArrayList<String>();
    	String res1 = "  ";
    	for (int y = 0; y < yMax; y++) {
    		res1 += (" " + y + " ");
    	}
    	resList.add(res1);
    	String res2[] = new String[xMax];
    	for (int x = 0; x < xMax; x++) {
    		res2[x] = "";
    		String num = "" + x;
    		num = (x < 10) ? " " + num : num;
    		res2[x] += num;
    		for (int y = 0; y < yMax; y++) {
    			res2[x] += ("[" + input[x][y] + "]");
    		}
    		resList.add(res2[x]);
    	}
    	return resList;
    }
    
    public String getPointsAsString(List<int[]> pts) {
    	String res = "";
    	for (Iterator<int[]> iterator = pts.iterator(); iterator.hasNext();) {
			int[] pt = iterator.next();
			res += "(" + pt[0] + ", " + pt[1] + ") ";
		}
    	return res;
    }
    public String getPiecesAsString() {
    	String res = "";
    	for (int i = 0; i < nPieces; i++) {
    		res += pieces[i] + "(" + piecesPoints[i][0] + ", " + piecesPoints[i][1] + ") ";
		}
    	return res;
    }

    
    public boolean isSameGame(ChessBoardLight b) {
    	return (b.xSize == xSize) && 
    			(b.ySize == ySize) &&
    			(b.nPieces == nPieces);
    }

    public boolean isDifferentGame(ChessBoardLight b) {
    	return (b.xSize != xSize) || 
    			(b.ySize != ySize) ||
    			(b.nPieces != nPieces);
    }
    public boolean isArrangeEqualsNative(ChessBoardLight b) {
    	int myX, myY;
    	char my;
    	for (int i = 0; i < nPieces; i++) {
    		my = pieces[i];
    		myX = piecesPoints[i][0];
    		myY = piecesPoints[i][1];
    		if (b.boardView[myX][myY] != my)
    			return false;
    	}
    	return true;
    }

    public boolean isArrangeEquals(ChessBoardLight b) {
    	if (isDifferentGame(b))
    		return false;
    	for (int x = 0; x < xSize; x++)
    		for (int y = 0; y < ySize; y++)
    			if (b.boardView[x][y] != boardView[x][y])
    				return false;
    	return true;
    }    
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ChessBoardLight))
			return false;
		ChessBoardLight b = (ChessBoardLight) obj;
		//if (!isArrangeEquals(b))
		if (!isArrangeEqualsNative(b))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + xSize;
        result = prime * result + ySize;
        result = prime * result + nPieces;
        return result;
    }
}
