package com.company;

/**
 * Created by Raphael on 27.06.2017.
 */
public class Guesses {

    private static int guesses = 0;

    public static int getGuesses() {
        return guesses;
    }

    public static void incrementGuesses(){
        guesses ++;
    }
}
