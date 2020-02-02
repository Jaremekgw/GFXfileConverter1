/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gfx.gfxfileconverter;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jarek
 */
public class Controller {
    MeasureModel data;
    List<String> list = new ArrayList<>();
    String[] array;

    Controller(MeasureModel data) {
        this.data = data;
    }

    public void read(File file) {
        //  final int timeStep = 50;    // wczesniej było 50ms
        final int timeStep = 20;        // chciałem to zrobić automatycznie z pliku "F20"
        
        int time;       // time stamp [ms] for each measure
        int rb;         // read byte
        int currPos;    // current position in measurement
        int newPos;     // new position of element in measurement
        String vStr;

        try (BufferedInputStream in = new BufferedInputStream( Files.newInputStream(Path.of(file.getAbsolutePath())) )) {

            time = 0;
            currPos = 0;
            // initialize array for gathering elements in measurement
            array = new String[Measure.MAX_SIZE];
            // initialize string to read digits followed by main letter
            vStr = "";
            while ((rb = in.read()) >= 0) {
                // convert main letter to position in measurement
                newPos = switch (rb) {
                    case 'R' -> 1;      // acceleration, direct value
                    case 'S' -> 1;      // acceleration, filtered value
                    case 'X' -> 2;      // duty % set to motor
                    case 'Q' -> 3;      // shaft position
                    case 'T' -> 4;      // speed with driver correction
                    //case 'U' -> 4;      // raw speed value
                    case 'D' -> 5;      // differences for acc to calculate min/max
                    case 'W' -> 6;      // round time period
                    case 'A' -> 7;      // duty value, change
                    case 'F' -> 9;      // okres czasu pomiaru przyspieszenia acc
                    case '0','1','2','3','4','5','6','7','8','9'
                            -> 0;
                    case '-' -> 100;
                    default -> -1;
                };

                if (newPos == 100) {
                    if ( (currPos > 0) && vStr.length() == 0 )
                        newPos = 0;
                    else
                        newPos = -1;
                }

                if (currPos == 0) {
                    if (newPos > 0)
                        currPos = newPos;

                } else if (newPos == 0) {
                    // wpisywanie wartości, do czasu gdy są cyfry
                    vStr += (char)rb;
                    if (vStr.length() > 4 && currPos != 6)
                        System.out.println("Problem z wartoscia:" + vStr + "  dla currPos=" + currPos + " w pliku:"+ file.getAbsolutePath() );

                } else {
                    // gdy rozpoznano nowy element 'R' a we wczewniejszym
                    // nie było danych to zacząć odczyt od początku dla nowej pozycji
                    if (vStr.length() == 0) {
                        vStr = "";
                        currPos = (newPos < 0) ? 0 : newPos;
                        continue;
                    }

                    // jeżeli to pierwszy wpis to zapisz czas tego pomiaru
                    if(array[0] == null) {
                        // jako pierwszy dozwolony jest tylko 'R'
                        if (currPos > 1) {
                            vStr = "";
                            currPos = (newPos < 0) ? 0 : newPos;
                            continue;
                        }
                        String tStr = "" + time;
                        time += timeStep;
                        array[0] = tStr;
                        array[currPos] = vStr;
                        vStr = "";
			currPos = (newPos < 0) ? 0 : newPos;
                    } else {
                        if ((currPos == 1) && array[currPos] != null) {
                            // new measure, so write old one then start the new one
                            data.add(Measure.create(array));
                            
                            array = new String[Measure.MAX_SIZE];
                            String tStr = "" + time;
                            time += timeStep;
                            array[0] = tStr;
                            array[currPos] = vStr;
                            vStr = "";
			    currPos = (newPos < 0) ? 0 : newPos;
                        } else {
			    if (array[currPos] == null)
                                array[currPos] = vStr;

                            currPos = (newPos < 0) ? 0 : newPos;
                            vStr = "";
                        }
                    }
                }
            }

            if (array[0] != null) {
                data.add(Measure.create(array));
                System.out.println("--- LAST,  Add array: " + array[0] + ";" + array[1] + ";" + array[2] + ";" + array[3] );
            }
            
            System.out.println("Reading finished, elements=" + data.lenght() + "  size=" + data.getSize());
            
        } catch (IOException ex) {
            System.out.println("Error: problem przy zapisywaniu danych do pliku.");
        }
    
    }
    
    /**
     * Save data into new file on disk
     * 
     * @param fileName 
     */
    public void save(String fileName) {
        
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(fileName))) {
            
            list = data.getListPerpendicular();

            //System.out.println("Save data size: " + list.size() + "  data:" + list.get(0));
            
            for (String str : list) {
                bufferedWriter.write(str);
                bufferedWriter.newLine();
            }            
        } catch (IOException ex) {
            System.out.println("Error: problem przy zapisywaniu danych do pliku.");
        }
    }

    void calculate() {
        /* calculate additional values for better analyse */
        data.calc_acc(1, 10);       // 'R' 'S'  ->  10      przelicza odchylenie od średniej
        data.calc_shaft(3, 11);     // 'Q'  ->  11          przelicza pozycje 'n' na pozycję obrotową [+250...-250]

    }
    
}
