package com.lotos4u.text.chess.general;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Log {
	private static BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
    public static void out(Object o){
        System.out.println(o);
    }
    public static void pause() {
    	try {stdin.readLine();} catch (IOException e) {e.printStackTrace();}
    }
}
