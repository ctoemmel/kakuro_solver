package com.company;

import java.io.*;
import java.util.*;

/**
 * Created by Christian on 04.06.2017.
 */
public class Kakuro implements Serializable{

    Kakurofeld[][] fields = null;
    int guesses = 0;
    int backtracks = 0;
    int size = 0;
    String file;

    public Kakuro(String f){
        this.file = f;
    }

    public void setSize(int n){
        size = n;
        fields = new Kakurofeld[n][n];
    }

    public Kakurofeld getField(int x, int y){
        return fields[x][y];
    }

    public void addField(Kakurofeld k, int x, int y){
        fields[x][y] = k;

    }

    public void solve() {
        //LOL wenn es nur so einfach wäre
        System.out.println("Beginne Lösevorgang des Kakuro...");

            aktualisiereMoeglichkeitenUnmoegliche();

        //Löse alle offensichtlichen einstelligen
        int runcounter = 0;
        boolean changed = true;
        while(changed) {
            runcounter++;
            changed = false;
            for (int j = 0; j < size; j++) { //nach unten

                for (int i = 0; i < size; i++) { //nach rechts
                    Kakurofeld field = fields[j][i];
                    if (field.getType() == -1) {

                        //Finde relevante Felder
                        int fieldcounter = 0;

                        int gesuchterWert = field.getRechts();
                        for (int k = i + 1; k < size; k++) { //Bis zum maximal rechten Ende durchlaufen und herausfinden, wie viele Felder wichtig sind
                            if (fields[j][k].getType() == 0 && field.getLoesung()!=-1)
                                fieldcounter++;
                            else
                                break;

                        }
                        if (fieldcounter == 1) {
                            fields[j][i + 1].speichereEineLoesung(field.getRechts());
                            field.speichereEineLoesung(-1); //Abgehakt!
                            changed = true;
                            //return;
                        }
                        //fieldcounter == anzahl an zu füllenden Feldern
                        System.out.println("Anzahl gesuchter felder:" + fieldcounter);
                        System.out.println("Zu füllen mit insgesamt:" + gesuchterWert);
                        System.out.println("ID: fields[" + i + "][" + j + "]");
                        System.out.println("------------------------------------------");

                        fieldcounter = 0;

                        gesuchterWert = field.getUnten();
                        for (int k = j + 1; k < size; k++) { //Bis zum maximal rechten Ende durchlaufen und herausfinden, wie viele Felder wichtig sind
                            if (fields[k][i].getType() == 0 && field.getLoesung()!=-1)
                                fieldcounter++;
                            else
                                break;

                        }
                        if (fieldcounter == 1) {
                            fields[j + 1][i].speichereEineLoesung(field.getUnten());
                            field.speichereEineLoesung(-1); //Abgehakt!
                            changed = true;
                            //return;
                        }
                        //fieldcounter == anzahl an zu füllenden Feldern
                        System.out.println("Anzahl gesuchter felder:" + fieldcounter);
                        System.out.println("Zu füllen mit insgesamt:" + gesuchterWert);
                        System.out.println("ID: fields[" + i + "][" + j + "]");
                        System.out.println("------------------------------------------");


                    }


                }
            }


            for (int j = 0; j < size; j++) { //nach unten

                for (int i = 0; i < size; i++) { //nach rechts
                    Kakurofeld field = fields[j][i];
                    if (field.getType() == -1) {

                        //Finde relevante Felder
                        int fieldcounter = 0;
                        int freeFieldcounter = 0;
                        int sum = 0;
                        int gesuchterWert = field.getRechts();
                        int right = 0;
                        int counter = 0;
                        for (int k = i + 1; k < size; k++) { //Bis zum maximal rechten Ende durchlaufen und herausfinden, wie viele Felder wichtig sind
                            if (fields[j][k].getLoesung() == 0 && fields[j][k].getType() == 0) {
                                freeFieldcounter++;
                                right = counter;
                            } else if (fields[j][k].getLoesung() >= 0 && fields[j][k].getType() == 0) {
                                counter++;
                                sum += fields[j][k].getLoesung();
                            } else break;

                        }
                        if (freeFieldcounter == 1) {
                            if(field.getRechts()-sum<1)
                                return;
                            fields[j][i + 1 + right].speichereEineLoesung(field.getRechts() - sum);
                            field.speichereEineLoesung(-1); //Abgehakt!
                            changed = true;
                            System.out.println("Feld " + j + "/" + i + " sucht nach rechts:" + field.getRechts() + " -> " + (field.getRechts() - sum));
                            //return;
                        }

                        //Finde relevante Felder
                        fieldcounter = 0;
                        freeFieldcounter = 0;
                        sum = 0;
                        gesuchterWert = field.getUnten();
                        right = 0;
                        counter = 0;
                        for (int k = j + 1; k < size; k++) { //Bis zum maximal rechten Ende durchlaufen und herausfinden, wie viele Felder wichtig sind
                            if (fields[k][i].getLoesung() == 0 && fields[k][i].getType() == 0) {
                                freeFieldcounter++;
                                right = counter;
                            } else if (fields[k][i].getLoesung() >= 0 && fields[k][i].getType() == 0) {
                                counter++;
                                sum += fields[k][i].getLoesung();
                            } else break;

                        }
                        if (freeFieldcounter == 1) {
                            if(field.getUnten()-sum<1)
                                return;
                            fields[j + 1 + right][i].speichereEineLoesung(field.getUnten() - sum);
                            field.speichereEineLoesung(-1); //Abgehakt!
                            changed = true;
                            System.out.println("Feld " + j + "/" + i + " sucht nach unten:" + field.getUnten() + " -> " + (field.getUnten() - sum));
                            //return;
                        }
                    }


                }

            }

        }
        //SOLVER MUSS HIER HIN
        //Mit Sicherheit lässt sich nichts mehr sagen, also wird es Zeit für den Backtracking-Ansatz



        //Jetzt sollte ein gelöstes Kakuro vorlieren. Hier kommen nun die Endstatistiken sowie die Ausgabe
        kakuroErfolgreichGeloest();
        System.out.print("Kakuro erfolgreich gelöst: ");
        System.out.println((korrekteLoesung()?"Ja":"Nein"));
        System.out.println("In Durchläufen: " + runcounter);
        if(korrekteLoesung()) {
            writeToFile();
            System.exit(0);
            return;
        }
        else {
            System.out.println("Notwendigkeit von Backtracking besteht. Beginne...");
            backtracking();
        }
    }

    public void backtracking(){
        backtracks++;
        improvedGraphics();
        Kakuro btKak = cloneThis();
        if(btKak.korrekteLoesung()){
            System.out.println("gelöst. GG");
            btKak.writeToFile();
            System.exit(0);
        }
        //btKak.updateMoeglichkiten();
        btKak.oneBTstep();
        //btKak.solve();
    }

    public void aktualisiereMoeglichkeitenDoubles(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Kakurofeld thisfield = fields[i][j];

                boolean[] doubles = new boolean[9];
                for(int z = 0; z < 9; z++)
                    doubles[z] = true;

                if(thisfield.getType()==-1 && thisfield.getRechts() != 0) {
                    for (int k = j + 1; k < size; k++) {
                        if (fields[i][k].getType() == 0) {
                            if (fields[i][k].getLoesung() != 0) {

                                doubles[fields[i][k].getLoesung() - 1] = false;
                            }
                        } else break;
                    }
                }
                for (int k = j + 1; k < size; k++) {
                    if (fields[i][k].getType() == 0) {
                        if (fields[i][k].getLoesung() == 0) {
                            fields[i][k].vereinigeMoeglichkeiten(doubles);
                        }
                    } else break;
                }

                for(int z = 0; z < 9; z++)
                    doubles[z] = true;

                if(thisfield.getType()==-1 && thisfield.getUnten() != 0) {
                    for (int k = i + 1; k < size; k++) {
                        if (fields[k][j].getType() == 0) {
                            if (fields[k][j].getLoesung() != 0) {

                                doubles[fields[k][j].getLoesung() - 1] = false;
                            }
                        } else break;
                    }
                }
                for (int k = i + 1; k < size; k++) {
                    if (fields[k][j].getType() == 0) {
                        if (fields[k][j].getLoesung() == 0) {

                            fields[k][j].vereinigeMoeglichkeiten(doubles);
                        }
                    } else break;
                }

            }
        }
    }

    public void aktualisiereMoeglichkeitenUnmoegliche(){
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Kakurofeld thisfield = fields[i][j];

                int sum = 0;
                int freeFieldcounter = 0;
                Kakurofeld kak1 = null;
                Kakurofeld kak2 = null;
                Kakurofeld kak3 = null;
                Kakurofeld kak4 = null;
                Kakurofeld kak5 = null;
                Kakurofeld kak6 = null;
                Kakurofeld kak7 = null;
                Kakurofeld kak8 = null;
                if (thisfield.getType() == -1 && thisfield.getUnten() != 0) {
                    for (int k = i + 1; k < size; k++) {
                        if (fields[k][j].getType() == 0 && fields[k][j].getLoesung() != 0) {
                            sum = sum + fields[k][j].getLoesung();
                        } else if (fields[k][j].getType() == 0 && fields[k][j].getLoesung() == 0) {
                            if (freeFieldcounter == 0)
                                kak1 = fields[k][j];
                            else if (freeFieldcounter==1)
                                kak2 = fields[k][j];
                            else if (freeFieldcounter==2)
                                kak3 = fields[k][j];
                            else if (freeFieldcounter==3)
                                kak4 = fields[k][j];
                            else if (freeFieldcounter==4)
                                kak5 = fields[k][j];
                            else if (freeFieldcounter==5)
                                kak6 = fields[k][j];
                            else if (freeFieldcounter==6)
                                kak7 = fields[k][j];
                            else
                                kak8 = fields[k][j];
                            freeFieldcounter++;
                        } else
                            break;
                    }
                    int freesum = thisfield.getUnten() - sum;
                    if (freeFieldcounter == 2) {

                        boolean[] b = new boolean[9];

                        if (freesum == 3) {
                            b[0] = true;
                            b[1] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 4) {
                            b[0] = true;
                            b[2] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 5){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 6){
                            b[0] = true;
                            b[1] = true;
                            b[3] = true;
                            b[4] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 7){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 8){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 9){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 10){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 11){
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 12){
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 13){
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 14){
                            b[4] = true;
                            b[5] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 15){
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 16) {
                            b[6] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        } else if (freesum == 17) {
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }


                    } else if (freeFieldcounter == 3){

                        boolean[] b = new boolean[9];

                        if (freesum == 6){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 7){
                            b[0] = true;
                            b[1] = true;
                            b[3] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 8){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }

                        else if (freesum == 9){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 10){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 11){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 12 && freesum <= 18){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 19){
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 20){
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 21){
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 22){
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 23){
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 24){
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }

                    }
                    else if (freeFieldcounter == 4){
                        boolean[] b = new boolean[9];

                        if (freesum == 10) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 11) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 12) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 13) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 14) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 15 && freesum<= 25) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 26) {
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 27) {
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 28) {
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 29) {
                            b[4] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 30) {
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                    }

                    else if (freeFieldcounter == 5) {

                        boolean[] b = new boolean[9];

                        if (freesum == 15) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        } else if (freesum == 16) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[5] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 17) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 18) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 19 && freesum <=31) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 32) {
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 33) {
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 34) {
                            b[3] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 35) {
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                    }
                    else if (freeFieldcounter == 6) {

                        boolean[] b = new boolean[9];

                        if (freesum == 21) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 22) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 23) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 24 && freesum <= 36) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 37) {
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 38) {
                            b[2] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 39) {
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                    }
                    else if (freeFieldcounter == 7) {

                        boolean[] b = new boolean[9];

                        if (freesum == 28) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                        } else if (freesum == 29) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 30 && freesum<= 40) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 41) {
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 42) {
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                        }
                    }
                    else if (freeFieldcounter == 8) {

                        boolean[] b = new boolean[9];

                        if (freesum == 36) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                            kak8.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 37 && freesum <= 43) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                            kak8.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 44) {
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                            kak8.vereinigeMoeglichkeiten(b);
                        }
                    }


                }
                sum = 0;
                freeFieldcounter = 0;
                kak1 = null;
                kak2 = null;
                kak3 = null;
                kak4 = null;
                kak5 = null;
                kak6 = null;
                kak7 = null;
                kak8 = null;
                if (thisfield.getType() == -1 && thisfield.getRechts() != 0) {
                    for (int k = j + 1; k < size; k++) {
                        if (fields[i][k].getType() == 0 && fields[i][k].getLoesung() != 0) {
                            sum = sum + fields[i][k].getLoesung();
                        } else if (fields[i][k].getType() == 0 && fields[i][k].getLoesung() == 0) {
                            if (freeFieldcounter == 0)
                                kak1 = fields[i][k];
                            else if (freeFieldcounter==1)
                                kak2 = fields[i][k];
                            else if (freeFieldcounter==2)
                                kak3 = fields[i][k];
                            else if (freeFieldcounter==3)
                                kak4 = fields[i][k];
                            else if (freeFieldcounter==4)
                                kak5 = fields[i][k];
                            else if (freeFieldcounter==5)
                                kak6 = fields[i][k];
                            else if (freeFieldcounter==6)
                                kak7 = fields[i][k];
                            else
                                kak8 = fields[i][k];
                            freeFieldcounter++;
                        } else
                            break;
                    }
                    int freesum = thisfield.getRechts() - sum;

                    if (freeFieldcounter == 2) {

                        boolean[] b = new boolean[9];

                        if (freesum == 3) {
                            b[0] = true;
                            b[1] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 4) {
                            b[0] = true;
                            b[2] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 5){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 6){
                            b[0] = true;
                            b[1] = true;
                            b[3] = true;
                            b[4] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 7){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 8){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 9){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 10){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 11){
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 12){
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 13){
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 14){
                            b[4] = true;
                            b[5] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 15){
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 16) {
                            b[6] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        } else if (freesum == 17) {
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                        }


                    }  else if (freeFieldcounter == 3){

                        boolean[] b = new boolean[9];

                        if (freesum == 6){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 7){
                            b[0] = true;
                            b[1] = true;
                            b[3] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 8){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }

                        else if (freesum == 9){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 10){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 11){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 12 && freesum <= 18){
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 19){
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 20){
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 21){
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 22){
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 23){
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 24){
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                        }

                    }
                    else if (freeFieldcounter == 4){
                        boolean[] b = new boolean[9];

                        if (freesum == 10) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 11) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 12) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 13) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 14) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 15 && freesum<= 25) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 26) {
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 27) {
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 28) {
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 29) {
                            b[4] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 30) {
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                        }
                    }

                    else if (freeFieldcounter == 5) {

                        boolean[] b = new boolean[9];

                        if (freesum == 15) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        } else if (freesum == 16) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[5] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 17) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 18) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 19 && freesum <=31) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 32) {
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 33) {
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 34) {
                            b[3] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 35) {
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                        }
                    }
                    else if (freeFieldcounter == 6) {

                        boolean[] b = new boolean[9];

                        if (freesum == 21) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 22) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 23) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 24 && freesum <= 36) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 37) {
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 38) {
                            b[2] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 39) {
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                        }
                    }
                    else if (freeFieldcounter == 7) {

                        boolean[] b = new boolean[9];

                        if (freesum == 28) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                        } else if (freesum == 29) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 30 && freesum<= 40) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 41) {
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 42) {
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                        }
                    }
                    else if (freeFieldcounter == 8) {

                        boolean[] b = new boolean[9];

                        if (freesum == 36) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                            kak8.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum >= 37 && freesum <= 43) {
                            b[0] = true;
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                            kak8.vereinigeMoeglichkeiten(b);
                        }
                        else if (freesum == 44) {
                            b[1] = true;
                            b[2] = true;
                            b[3] = true;
                            b[4] = true;
                            b[5] = true;
                            b[6] = true;
                            b[7] = true;
                            b[8] = true;
                            kak1.vereinigeMoeglichkeiten(b);
                            kak2.vereinigeMoeglichkeiten(b);
                            kak3.vereinigeMoeglichkeiten(b);
                            kak4.vereinigeMoeglichkeiten(b);
                            kak5.vereinigeMoeglichkeiten(b);
                            kak6.vereinigeMoeglichkeiten(b);
                            kak7.vereinigeMoeglichkeiten(b);
                            kak8.vereinigeMoeglichkeiten(b);
                        }
                    }
                }

            }
        }
    }

    public void setzeEinzigartigeEin(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Kakurofeld thisfield = fields[i][j];
                if(thisfield.getType()==0 && thisfield.getLoesung()==0){
                    if(thisfield.nurEineMoeglichkeit(true)){
                        System.out.println("Einzigartige Lösung gefunden! :)");
                    }
                }
            }
        }
    }

    public void oneBTstep(){

        for(int i = 0; i < 11; i++) {
            if (!claimNewSecures()) {
                return;
            }
            aktualisiereMoeglichkeitenDoubles();
            aktualisiereMoeglichkeitenUnmoegliche();


            setzeEinzigartigeEin();


            if (!checkKakurosValidness()) {
                System.out.println("Neue 'sichere' Zahlen haben das Puzzle kaputtgemacht. Gehe zurück...");
                return;
            }

        }

        if(this.korrekteLoesung()){
            System.out.println("gelöst. GG");
            this.writeToFile();
            System.exit(0);
        }

        findFirstFreeField();
        System.out.println("NEIIIN");
        improvedGraphics();

        if(this.korrekteLoesung()){
            System.out.println("gelöst. GG");
            this.writeToFile();
            System.exit(0);
        }

    }

    public boolean checkKakurosValidness(){ //Hier soll alles rein, was einen BackTrack schritt abbrechen kann!
        boolean result = true;
        result = isStillValid() && noDoubles() && noImpossibles() && noZeros();
        ;

        return result;
    }

    public boolean claimNewSecures() {
        boolean changed = true;
        while (changed) {

            changed = false;

            for (int j = 0; j < size; j++) { //nach unten

                for (int i = 0; i < size; i++) { //nach rechts
                    Kakurofeld field = fields[j][i];

                    if (field.getType() == -1) {

                        //Finde relevante Felder
                        int fieldcounter = 0;
                        int freeFieldcounter = 0;
                        int sum = 0;
                        int gesuchterWert = field.getRechts();
                        int right = 0;
                        int counter = 0;
                        for (int k = i + 1; k < size; k++) { //Bis zum maximal rechten Ende durchlaufen und herausfinden, wie viele Felder wichtig sind
                            if (fields[j][k].getLoesung() == 0 && fields[j][k].getType() == 0) {
                                freeFieldcounter++;
                                right = counter;
                            } else if (fields[j][k].getLoesung() >= 0 && fields[j][k].getType() == 0) {
                                counter++;
                                sum += fields[j][k].getLoesung();
                            } else break;

                        }
                        if (freeFieldcounter == 1) {
                            if (field.getRechts() - sum < 1 || field.getRechts() - sum > 9) {
                                return false;
                            } else
                            {
                                fields[j][i + 1 + right].speichereEineLoesung(field.getRechts() - sum);
                                field.speichereEineLoesung(-1); //Abgehakt!
                                changed = true;
                                System.out.println("Feld " + j + "/" + i + " sucht nach rechts:" + field.getRechts() + " -> " + (field.getRechts() - sum));
                                //return;
                            }
                        }

                        //Finde relevante Felder
                        fieldcounter = 0;
                        freeFieldcounter = 0;
                        sum = 0;
                        gesuchterWert = field.getUnten();
                        right = 0;
                        counter = 0;
                        for (int k = j + 1; k < size; k++) { //Bis zum maximal rechten Ende durchlaufen und herausfinden, wie viele Felder wichtig sind
                            if (fields[k][i].getLoesung() == 0 && fields[k][i].getType() == 0) {
                                freeFieldcounter++;
                                right = counter;
                            } else if (fields[k][i].getLoesung() >= 0 && fields[k][i].getType() == 0) {
                                counter++;
                                sum += fields[k][i].getLoesung();
                            } else break;

                        }
                        if (freeFieldcounter == 1) {
                            if (field.getUnten() - sum < 1 || field.getUnten() - sum > 9) {
                                return false;
                            } else {
                                fields[j + 1 + right][i].speichereEineLoesung(field.getUnten() - sum);
                                field.speichereEineLoesung(-1); //Abgehakt!
                                changed = true;
                                System.out.println("Feld " + j + "/" + i + " sucht nach unten:" + field.getUnten() + " -> " + (field.getUnten() - sum));
                                //return;
                            }
                        }
                    }

                }
            }

        }
        return true;
    } //NAch einem Backtrackschritt die dadurch implizierten neuen Felder eintragen (wie im normalen solve() )

    public void findFirstFreeField(){
/*
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Kakurofeld bet = fields[i][j];
                if(bet.getType()==-1){
                    for(int k = j+1; k < size; k++){
                        if(fields[i][k].getLoesung()==0 && fields[i][k].getType()==0 && bet.getRechts()!=0){
                            fields[i][k].bestLookingSolution(this);
                            System.out.println("Feld gefunden. Backtracke" +i+"/"+k);
                            return;
                        }
                    }

                    for(int k = i+1; k < size; k++){
                        if(fields[k][j].getLoesung()==0 && fields[k][j].getType()==0 && bet.getUnten()!=0){
                            fields[k][j].bestLookingSolution(this);
                            System.out.println("Feld gefunden.. Backtracke" +k+"/"+j);
                            return;
                        }
                    }
                }
            }
        }
*/

        Map.Entry<Integer, Kakurofeld> min = null;
        HashMap<Integer, Kakurofeld> ar = new HashMap<>();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                Kakurofeld bet = fields[i][j];

                if (bet.getLoesung() == 0 && bet.getType() == 0) {
                    int importance = bet.getAnzMoeglichkeiten();
                    ar.put(importance, bet);

                }

            }
        }
        int x = 0;
        while(x<size*size) {
            x++;
            min = null;
            for (Map.Entry<Integer, Kakurofeld> entry : ar.entrySet()) {
                if (min == null || min.getKey() > entry.getKey()) {
                    min = entry;
                }
            }
            ar.remove(min.getValue());
            min.getValue().bestLookingSolution(this);
        }

    } //für den Backtrackschritt. Nimmt das erste freie Feld. TODO Jedes andere Feld könnte schlauer sein!

    public boolean noDoubles(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Kakurofeld thisfield = fields[i][j];

                boolean[] doubles = new boolean[9];

                if(thisfield.getType()==-1){

                    for(int k = j+1; k < size; k++){
                        if(fields[i][k].getType()==0){
                            if(fields[i][k].getLoesung()!=0){
                                if(doubles[fields[i][k].getLoesung()-1]){
                                    System.out.println("Duplikat gefunden. Breche ab..");
                                    return false;
                                }
                                doubles[fields[i][k].getLoesung()-1] = true;
                            }
                        }
                        else break;
                    }


                    for(int l = 0; l < 9; l++){
                        doubles[l] = false;
                    }

                    for(int k = i+1; k < size; k++){
                        if(fields[k][j].getType()==0){
                            if(fields[k][j].getLoesung()!=0){
                                if(doubles[fields[k][j].getLoesung()-1]){
                                    System.out.println("Duplikat gefunden. Breche ab..");
                                    return false;
                                }
                                doubles[fields[k][j].getLoesung()-1] = true;
                            }
                        }
                        else break;
                    }
                }

            }
        }
        return true;
    } //Prüft, ob nirgendwo im Kakuro Duplikate in einer Zeile/Spalte auftauchen

    public Kakuro cloneThis(){
        Kakuro testkak = this;
        DeepObjectCopy doc = new DeepObjectCopy();
        Kakuro kakuroClone = (Kakuro) doc.clone(this);
        System.out.println("Neues BT-Kakuro erzeugt. Schritt " + backtracks);
        System.out.println((testkak==this)?"Originale Kakuros sind gleich.":"Dieser Fehler kann unmöglich auftreten.");
        System.out.println((testkak==kakuroClone)?"Fehler beim Klonen!":"Klonen war erfolgreich.");
        return kakuroClone;
    } //Die Klon-Funktion (vgl. DeepCopy)

    public void writeToFile(){
        String futureFile = "("+size+", (";
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Kakurofeld field = fields[i][j];

                if(field.getType()==-2){
                    futureFile+="0, ";
                }
                else if(field.getType()==-1){
                    futureFile+="("+field.getRechts()+", "+field.getUnten()+"), ";
                }
                else if(field.getType()==0){
                    futureFile+=field.getLoesung()+", ";
                }
            }
        }
        futureFile=futureFile.substring(0,futureFile.length()-2);
        futureFile += "), "+Guesses.getGuesses() + ", "+backtracks+")";
        System.out.println(futureFile);
        File result = new File(this.file+"s");
        try{
            result.createNewFile();

            FileWriter fileWriter = new FileWriter(result, false);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(futureFile);
            bufferedWriter.close();

            System.out.println("Datei erfolgreich geschrieben.");
        } catch(IOException e) {
            System.out.println("Fehler beim Schreiben.");
        }

    } //Ausgabefunktion

    public void kakuroErfolgreichGeloest(){

        for(int i = 0; i < size*size; i++){
            Kakurofeld thisfield = fields[i/size][i%size];
            if(i%size==0)
                System.out.println("");

            String spaces = " ";
            int item;
            if(thisfield.getType() != 0)
                item = thisfield.getType();
            else
                item = thisfield.getLoesung();
            if(item == -1 || item == -2)
                spaces = "";
            System.out.print(spaces + item + "..");

        }
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("-2 = BLOCKED");
        System.out.println("-1 = DIAGONALE");
        System.out.println("0  = ZU LÖSEN");

        improvedGraphics();
    }

    public void improvedGraphics(){
        String decke = "-";
        String leereReihe = "|";
        String zweiteReihe = "|";
        String dritteReihe = "|";
        for(int i = 0; i < size; i++){
            decke +=      "--------";
            //leereReihe += "       |";
        }
        for(int i = 0; i < size*size; i++){
            if(i%size==0 && i > 0) {
                System.out.println(decke);
                System.out.println(leereReihe);
                System.out.println(zweiteReihe);
                System.out.println(dritteReihe);
                leereReihe = "|";
                zweiteReihe = "|";
                dritteReihe = "|";
            }
            Kakurofeld thisfield = fields[i/size][i%size];
            String spaces  = "     ";
            String spaces2 = "  ";
            String item = "";
            String item2 = "";
            if(thisfield.getType() == -2) {
                spaces = "xxx";
                spaces2 = "xxxxxxx";
                item = "xxxx";
                leereReihe+="xxxxxxx|";
            }
            else if(thisfield.getType() == -1) {
                if(thisfield.getRechts()<10) {
                    spaces += " ";
                }
                if(thisfield.getUnten()<10) {
                    spaces2 += " ";
                    if(thisfield.getUnten()==0)
                        spaces2+=" ";
                }
                item = Integer.toString(thisfield.getRechts());
                item2 = Integer.toString(thisfield.getUnten());
                if (thisfield.getRechts()==0)
                    item = " ";
                if(thisfield.getUnten()==0)
                    item2 = "";
                item2 += "   ";
                leereReihe+="       |";
            } else {
                spaces="   ";
                spaces2+= "  ";
                if(thisfield.getLoesung()==0)
                    item = "    ";
                else
                    item = Integer.toString(thisfield.getLoesung())+"   ";
                item2 = "   ";
                leereReihe+="       |";
            }
            zweiteReihe+=spaces+item+"|";
            dritteReihe+=spaces2+item2+"|";
        }
        System.out.println(decke);
        System.out.println(leereReihe);
        System.out.println(zweiteReihe);
        System.out.println(dritteReihe);
        System.out.println(decke);
        return;
    }

    public boolean korrekteLoesung() {
        boolean fertig = true;
        for (int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Kakurofeld field = fields[i][j];
                boolean[] b = new boolean[9];
                int sum = 0;
                if(field.getType()==-1){
                    //Alle rechts abklappern
                    for(int k = j+1; k < size; k++){
                        if(fields[i][k].getType()==0) {
                            sum += fields[i][k].getLoesung();
                            if(fields[i][k].getLoesung()!=0) {
                                if (b[fields[i][k].getLoesung() - 1])
                                    return false;
                                else
                                    b[fields[i][k].getLoesung() - 1] = true;
                            }
                        }
                        else
                            break;
                    }

                    fertig &= (sum==field.getRechts());

                    sum = 0;
                    b = new boolean[9];
                    //Alle unten abklappern
                    for(int k = i+1; k < size; k++){
                        if(fields[k][j].getType()==0) {
                            sum += fields[k][j].getLoesung();
                            if(fields[k][j].getLoesung()!=0) {
                                if (b[fields[k][j].getLoesung() - 1])
                                    return false;
                                else
                                    b[fields[k][j].getLoesung() - 1] = true;
                            }
                        }
                        else
                            break;
                    }
                    fertig &= (sum==field.getUnten());

                }
            }
        }
        return fertig;
    }

    public boolean isStillValid(){

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                Kakurofeld thisf = fields[i][j];
                if(thisf.getType()==-1) {
                    boolean geloesteReihe = true;
                    int sum = 0;
                    for (int k = j + 1; k < size; k++) {
                        if(fields[i][k].getType()==0){
                            if(fields[i][k].getLoesung()==0){
                                geloesteReihe = false;
                            }
                            sum += fields[i][k].getLoesung();
                        }
                        else break;
                    }
                    if(geloesteReihe && sum!=thisf.getRechts()) {
                        return false;
                    }


                    geloesteReihe = true;
                    sum = 0;
                    for (int k = i + 1; k < size; k++) {
                        if(fields[k][j].getType()==0){
                            if(fields[k][j].getLoesung()==0){
                                geloesteReihe = false;
                            }
                            sum += fields[k][j].getLoesung();
                        }
                        else break;
                    }
                    if(geloesteReihe && sum!=thisf.getUnten()) {
                        return false;
                    }

                }
            }
        }


        return true;
    }

    public boolean noImpossibles() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Kakurofeld thisfield = fields[i][j];

                int sum = 0;
                int sumIsMadeOutOf = 0;
                int freeFielcounter = 0;
                if (thisfield.getType() == -1) {

                    for (int k = j + 1; k < size; k++) {
                        if (fields[i][k].getType() == 0) {
                            if (fields[i][k].getLoesung() != 0) {
                                sum += fields[i][k].getLoesung();
                                sumIsMadeOutOf++;
                            } else {
                                freeFielcounter++;
                            }
                        } else break;
                    }
                    int mostPossible = 0;
                    for (int k = 9; k > 9 - freeFielcounter; k--) {
                        mostPossible += k;
                    }
                    int leastPossible = 0;
                    for (int k = 1;k<=freeFielcounter; k++){
                        leastPossible=leastPossible+k;
                    }
                    if ((mostPossible < thisfield.getRechts() - sum) || (sum > thisfield.getRechts()) || (freeFielcounter>0 && sum==thisfield.getRechts())||(leastPossible > thisfield.getRechts() - sum) ) {
                        System.out.println("Summe nicht mehr erfüllbar mit Ziffern 1-9. Breche ab...");

                        return false;
                    }
                }


                sum = 0;
                freeFielcounter = 0;
                if (thisfield.getType() == -1) {

                    for (int k = i + 1; k < size; k++) {
                        if (fields[k][j].getType() == 0) {
                            if (fields[k][j].getLoesung() != 0) {
                                sum += fields[k][j].getLoesung();
                            } else {
                                freeFielcounter++;
                            }
                        } else break;
                    }
                    int mostPossible = 0;
                    for (int k = 9; k > 9 - freeFielcounter; k--) {
                        mostPossible += k;
                    }

                    int leastPossible = 0;
                    for (int k = 1;k<=freeFielcounter; k++){
                        leastPossible=leastPossible+k;
                    }

                    if ((mostPossible < thisfield.getUnten() - sum) || (sum > thisfield.getUnten()) || (freeFielcounter>0 && sum==thisfield.getUnten()) || (leastPossible > thisfield.getUnten() - sum) ) {
                        System.out.println("Summe nicht mehr erfüllbar mit Ziffern 1-9. Breche ab...");

                        return false;
                    }
                }
            }
        }

        return true;
    } //Filtert, ob nicht einzelne Felder existieren, die schon das Kakuro kaputt machen (zu hohe Summe)

    public boolean noZeros(){
        //Überprüft, ob Summen schon vollständig sind bis auf n Felder und eines dieser 0 sein müsste
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Kakurofeld thisfield = fields[i][j];
                int sum = 0;
                //rechts
                boolean[] used = new boolean[9];
                int freeFieldCounter = 0;
                if (thisfield.getType() == -1) {

                    for (int k = j + 1; k < size; k++) {
                        if (fields[i][k].getType() == 0) {
                            if (fields[i][k].getLoesung() != 0) {
                                used[fields[i][k].getLoesung() - 1] = true;   //prueft welche zahlen schon alles dran kamen
                                sum += fields[i][k].getLoesung();               //berechnet die bereits erreichte summe
                            }
                            else {
                                freeFieldCounter++;                             //anzahl freier felder
                            }
                        } else break;
                    }
                    int counter = 0;
                    int tempfree = freeFieldCounter;
                    int possibleleftunder = 0;
                    int possibleleftover = 0;
                    int freeDigits = 0;
                    int sumAvailable=0;
                    for(int a=0;a<9;a++){
                        if(used[a]==false){
                            freeDigits++;
                            sumAvailable= sumAvailable+a+1;
                        }
                    }
                    if (freeFieldCounter>0) {
                        if ((freeDigits == freeFieldCounter) &&( sumAvailable != thisfield.getRechts() - sum)) { //prueft ob die noch restlichen zahlen mit den restlichen feldern die summe ergeben
                            System.out.println("Summe nicht mehr erfüllbar mit den restlichen Ziffern. Breche ab..."+freeDigits + freeFieldCounter);
                            System.out.println(sumAvailable+" - " + (thisfield.getRechts()-sum));
                            return false;
                        } else {
                            boolean check = true;
                            while (check) {
                                if (used[counter] == false) {
                                    tempfree--;
                                    possibleleftunder =possibleleftunder +counter + 1; //summiert alle moeglichen zahlen von unten solange wie freie felder existieren
                                }
                                if (tempfree == 0) {
                                    check = false;
                                }
                                counter++;
                            }

                            tempfree = freeFieldCounter;
                            counter = 8;
                            check = true;
                            while (check) {
                                if (used[counter] == false) {
                                    tempfree--;
                                    possibleleftover = possibleleftover+counter + 1; //summiert alle moeglichen zahlen von oben solange wie freie felder existieren
                                }
                                if (tempfree == 0) {
                                    check = false;
                                }
                                counter--;
                            }


                            if (possibleleftunder > thisfield.getRechts() - sum) {

                                System.out.println("Summe nicht mehr erfüllbar mit Ziffern 1-9 von UNTEN abgegrenzt. Breche ab...");
                                return false;
                            }
                            if (possibleleftover < thisfield.getRechts() - sum) {
                                System.out.println("Summe nicht mehr erfüllbar mit Ziffern 1-9 von OBEN abgegrenzt. Breche ab...");
                                return false;
                            }


                        }
                    }



                }
            }
        }
        return true;
    }

    public void addGuess(){
        Guesses.incrementGuesses();
    }
}
