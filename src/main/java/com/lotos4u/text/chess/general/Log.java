package com.lotos4u.text.chess.general;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Log {
	private static BufferedReader pauseReader = new BufferedReader(new InputStreamReader(System.in));
	private static OutputStream fileWriter;
	private static String filename = "e:/SVN/Programs/git/chess-pieces/log_output.txt";
	private static boolean fileLog = false;
	
    public static void out(Object o){
        System.out.println(o);
        if(!fileLog)return;
        if(fileWriter == null) {
        	try {
        		fileWriter = new FileOutputStream(filename);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        }
        try {
			fileWriter.write(((String)(o.toString() + "\n")).getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
        	
    }
    public static void pause() {
    	try {pauseReader.readLine();} catch (IOException e) {e.printStackTrace();}
    }
}
