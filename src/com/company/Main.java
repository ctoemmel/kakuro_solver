package com.company;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {

        boolean debug = true; //True = Konsolenoutput; False = Kein Konsolenoutput (->Laufzeit besser)
        if (!debug) {

            System.setOut(new PrintStream(new OutputStream() {
                public void close() {
                }

                public void flush() {
                }

                public void write(byte[] b) {
                }

                public void write(byte[] b, int off, int len) {
                }

                public void write(int b) {
                }
            }));
        }

        String file;
        String kak;
        if(args.length > 0)
            file = args[0];
        else
            file = "./kakuro22_14x14.kak";
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            kak = sb.toString();
        } finally {
            br.close();
        }

        kak = kak.substring(1);
        kak = kak.replaceFirst(".$","");
        //System.out.println(kak);
        Kakuro kakuro = new Kakuro(file);
        int size;
        char c=kak.charAt(0);
        char c2 = kak.charAt(1);
        String puffer = "";
        puffer += c;
        if(isNumber(c2))
            puffer += c2;

        size = Integer.parseInt(puffer);
        kakuro.setSize(size);

        System.out.println("Größe des Kakuro (x*x Felder): " + size);

        int indexOfOpenBracket = kak.indexOf("(");
        int indexOfLastBracket = kak.lastIndexOf(")");
        kak = kak.substring(indexOfOpenBracket+1, indexOfLastBracket); //Entferne die unnötige erste und letzte Klammer
        //System.out.println(kak);
        kak = kak.replaceAll(" ", "");
        //System.out.println(kak);

        ArrayList<Kakurofeld> kaklist = new ArrayList<Kakurofeld>();

        String[] array = kak.split(",");
        for(int q = 0; q<array.length; q++){
            if(isNumber(array[q])) { //Wenn es eine Zahl ist, ist es keine Diagonale (Diagonale werden mit Klammern beschrieben)
                int kaktype = Integer.parseInt(array[q]);
                Kakurofeld kakfeld;
                if(kaktype == 0){

                    kakfeld = new Kakurofeld(-2,0,0,null);
                } else {
                    kakfeld = new Kakurofeld(0,0,0,null);
                }
                kaklist.add(kakfeld);

            } else { //... dann muss es ne Diagonale sein

                if(hasOpeningBracket(array[q])){ //Wenn ne offene Klammer drin ist, dann wird es eine Diagonale. Ansonsten ignorieren!
                    String diagonal = array[q] + "," + array[q+1]; //Die Diagonale als String zusammenbauen
                    //System.out.println(diagonal);
                    diagonal = diagonal.substring(1, diagonal.length()-1); //Die Klammern darum entfernen
                    //System.out.println(diagonal);

                    int diagonalNumber1 = Integer.parseInt(diagonal.split(",")[0]); //Jeweils rechts..
                    int diagonalNumber2 = Integer.parseInt(diagonal.split(",")[1]); //.. und unten herausfiltern
                    //System.out.println("Rechts: " + diagonalNumber1 + "; Unten: " + diagonalNumber2);
                    Kakurofeld kakfeld = new Kakurofeld(-1, diagonalNumber1, diagonalNumber2, null);
                    kakfeld.setLoesung(-1);
                    kaklist.add(kakfeld);
                }

            }

        }

        System.out.println("Anzahl Kakfelder:" + kaklist.size()); //size² viele sollten es immer sein. Passt bei meinen Tests. :)
        System.out.println("Entspricht size²: " + (kaklist.size() == size*size));
        int counter = 0;
        for (Kakurofeld k : kaklist){
            kakuro.addField(k, counter/size, counter%size);
            //System.out.println(counter%size + " - " + counter/size);
            counter++;
        }
        System.out.println("Kakuro erfolgreich erzeugt!");

        kakuro.solve();

    }

    private static boolean hasOpeningBracket(String s){
        for(char c : s.toCharArray())
            if(c=='(')
                return true;
        return false;
    }

    private static boolean isNumber(char c){
        if(c == '0' || c == '1' ||c == '2' ||c == '3' ||c == '4' ||
                c == '5' ||c == '6' ||c == '7' || c == '8' ||c == '9'){
            return true;
        }
        return false;
    }

    private static boolean isNumber(String s){
       for(char c : s.toCharArray())
           if(!Character.isDigit(c))
            return false;
        return true;
    }




}
