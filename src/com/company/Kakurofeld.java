package com.company;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

/**
 * Created by Christian on 04.06.2017.
 */
public class Kakurofeld implements Serializable{

    private int type; //-2: blockiert; -1: diagonale; 0: frei
    private int rechts;
    private int unten;
    private boolean[] moeglichkeiten;
    private int loesung;

    public Kakurofeld(int t, int r, int u, boolean[] m){ //Initialisieren mit m=null lässt alle Möglichkeiten offen (alles true)

        loesung = 0;
        if (t == -2) {
            type = -2;
            this.rechts = 0;
            this.unten = 0;
            this.moeglichkeiten = null;
        } else if (t == -1){
            type = -1;
            this. rechts = r;
            this. unten = u;
            this.moeglichkeiten = null;
        } else if (t == 0){
            moeglichkeiten = new boolean[9]; //Indexe von 0 - 8 für die Zahlen in Kakuro
            for(int i = 0; i<9; i++){
                moeglichkeiten[i] = true;
            }
            this.type = 0;
            this.rechts = 0;
            this.unten = 0;
            if(m != null)
                vereinigeMoeglichkeiten(m);
        } else {
            throw new ExceptionInInitializerError();
        }
    }

    public int getType(){
        return this.type;
    }

    public int getRechts(){
        return this.rechts;
    }

    public int getUnten(){
        return this.unten;
    }



    public boolean[] getMoeglichkeiten(){
        return this.moeglichkeiten;
    }

    public void vereinigeMoeglichkeiten(boolean[] m){ //Bildet die Schnittmenge aus der Eingabe und den bisherigen Möglichkeiten
        for(int i = 0; i < 9; i++){
            if(moeglichkeiten[i] && m[i]) {
                moeglichkeiten[i] = true;
            } else {
                moeglichkeiten[i] = false;
            }
        }
    }

    public void entferneEineMoeglichkeit(int j){
        moeglichkeiten[j-1] = false;
    }

    public void setMoeglichkeiten(boolean[] m){
        this.moeglichkeiten = m;
    }

    public boolean nurEineMoeglichkeit(boolean instantSet){ //Ein einzelnes Feld zählt als gelöst, wenn es nur noch eine Möglichkeit gibt
        if(type != 0)
            return true;
        int counter = 0;
        int lsgid = 0;
        for(int i = 0; i<9; i++){
            if(moeglichkeiten[i]) {
                counter++;
                lsgid = i+1;
            }
        }

        if(counter==1 && instantSet){
            loesung=lsgid;
        }

        return (counter==1) && (loesung!=0);
    }


    public void setLoesung(int i){
        this.loesung = i;
    }

    public int getLoesung(){
        return this.loesung;
    }

    public void speichereEineLoesung(int j){
        if(j == -1) {
            this.loesung = -1;
            return;
        }
        if(loesung != 0)
            return;
        boolean[] b = new boolean[9];
        for(int i = 0; i<9; i++){
            b[i] = false;
        }

        System.out.println("Eine Lösung gefunden.");

        b[j-1] = true;
        this.loesung = j;
        vereinigeMoeglichkeiten(b);
    }

    public int getAnzMoeglichkeiten(){
        int counter = 0;
        for(int i = 0; i < 9; i++){
            if(moeglichkeiten[i])
                counter++;
        }
        return counter;
    }

    public void bestLookingSolution(Kakuro kak){ //Hier wird der Backtrack schritt initiert. Das Kakuro wird geklont und dann wird ein guess gemacht

        Kakuro btkak;
        for(int i = 0; i < 9; i++){
            if(moeglichkeiten[i]){

                int counter = 0;
                for(int j = 0; j < 9; j++){
                    if(moeglichkeiten[j])
                        counter++;
                }
                moeglichkeiten[i] = false;
                loesung = i+1;

                if(counter>1)
                    kak.addGuess();
                btkak = kak.cloneThis();
                if(btkak.checkKakurosValidness() && btkak.claimNewSecures()) {
                    System.out.println("Setze Feld auf " + (i + 1));
                    btkak.backtracking();
                }
            }
        }
    }

}
