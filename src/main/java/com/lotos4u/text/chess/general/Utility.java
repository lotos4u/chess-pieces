package com.lotos4u.text.chess.general;

public class Utility {
    
    public static int putValueInRange(int input, int min, int max){
        int ret = input;
        ret = (ret < min) ? min : ret;
        ret = (ret > max) ? max : ret;
        return ret;
    }
    
    public static int cycledInc(int input, int min, int max){
        int ret = input;
        if(ret < max){
            ret++;
        }
        else{
            ret = min;
        }
        return ret;
    }
}
