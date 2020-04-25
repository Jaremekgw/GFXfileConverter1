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
    ShaftModel shaftTable;

    Controller(MeasureModel data, ShaftModel shaftTable) {
        this.data = data;
        this.shaftTable = shaftTable;
    }

    /**
     * It takes string of data from file and put it in table
     * Value (as a String) in variable 'vStr' is written dependently
     * of value 'newPos'.
     * Every new measure is created in 'array' and add into table
     * by command: data.add(Measure.create(array));
     * With time stamp at the beginning, at position '0'.
     * 
     * @param file 
     */
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
                    // for pol14 only:  begin ----------
                    case 'S' -> 1;      // acceleration, filtered value (filtered_accelerate)
                    case 'J' -> 2;      // duty, only when changed  (actualDutySpeed)
                    case 'Q' -> 3;      // shaft position       (motData->countShaftPosition)
                    case 'T' -> 4;      // speed input fuzzy when changed (timeImpMotSpeed)
                    case 'W' -> 5;      // round time period
                    case 'L' -> 6;      // sync correct +/(-) (accSyncUpTime)
                    case 'P' -> 7;      // (generalFuzzyPeriodUp)
                    case 'H' -> 8;      // regulacja fade, [1..4]-stop;
                    //case 'I' -> 9;      // integralDuty
                    case 'D' -> 9;      // alarm: motorData.alarm.accelerationHigh (acceleration)
                    case 'K' -> 10;      // min/max impulse for synch. checkDown (0 - accImpDnTime); gdy checkUp (accImpUpTime) 
                    //case 'G' -> 11;      // oznacza id tablicy speedTable[][] do fuzzy logic
                    case 'E' -> 11;      // alarm: motorData.alarm.swingHigh (acceleration)
                    case 'M' -> 12;      // sync time set speed (roundTime)
                    case 'R' -> 13;      // swing acc in axes X & Y (acc->sValue)
                    // for pol14 only:  end   ----------


//                    // for pol9 only:  begin ----------
//                    case 'S' -> 1;      // acceleration, filtered value
//                    case 'J' -> 2;      // bylo "X"  duty % before table setDuty[]
//                    case 'Q' -> 3;      // shaft position
//                    case 'T' -> 4;      // speed (period) input fuzzy logic
//                    case 'W' -> 5;      // round time period
//                    case 'L' -> 6;      // sync correct +/(-) time było "Y"
//                    case 'P' -> 7;      // generalFuzzyPeriodUp
//                    case 'H' -> 8;      // generalFuzzyDeltaUp
//                    case 'I' -> 9;      // integralDuty
//                    case 'K' -> 10;      // min/max impulse for synchronization. było "D"
//                    case 'G' -> 11;      // oznacza id tablicy speedTable[][] do fuzzy logic
//                    // for pol9 only:  end   ----------
//                    // for pol10 only:  begin ----------
//                    case 'M' -> 12;      // roundTime = sync time set speed
//                    // case 'Y' -> 6;      // sync correct (+)/- time było "Y"
                    // for pol10 only:  end   ----------
                    // for pol12 only:  begin ----------
                //    case 'M' -> 12;      // roundTime = sync time set speed
                //    case 'X' -> 13;      // zla wartosc dla: waveTime
                //    case 'Y' -> 14;      // zla wartosc dla: currentTime
                    // for pol12 only:  end   ----------

//                    // for pol8 only:  begin ----------
//                    case 'S' -> 1;      // acceleration, filtered value
//                    case 'X' -> 2;      // duty % set to motor - 2020-03-06 zmieniłem na wartość wyliczaną z fuzzy bez korekty z tabeli
//                    case 'Q' -> 3;      // shaft position
//                    case 'T' -> 4;      // speed (period) with driver correction
//                    case 'W' -> 5;      // round time period
//                    case 'Z' -> 6;      // speed - input for fuzzy logic
//                    case 'P' -> 7;      // pol8 = fuzzyInLevel
//                    case 'G' -> 8;      // pol7 = oznacza id tablicy danych do fuzzy logic
//                    // for pol8 only:  end   ----------
                    
                    
//                    case 'R' -> 1;      // acceleration, direct value
//                    case 'S' -> 1;      // acceleration, filtered value
//                    case 'X' -> 2;      // duty % set to motor - 2020-03-06 zmieniłem na wartość wyliczaną z fuzzy bez korekty z tabeli
//                    case 'Q' -> 3;      // shaft position
//                    case 'T' -> 4;      // speed (period) with driver correction
//                    //case 'U' -> 4;      // raw speed value
//                    // wylaczylem dla pol7      case 'D' -> 5;      // pol3 = differences for acc to calculate min/max; pol4 = period between diff min/max acc
//                    case 'M' -> 5;      // pol3 = differences for acc to calculate min/max; pol4 = period between diff min/max acc
//                    case 'W' -> 6;      // round time period
//                    case 'Y' -> 7;      // synch time between max acc and 'syncShaft = 80'
//                    case 'Z' -> 8;      // average for fuzzyLogic duty set and motorSpeedState: 
//                                        // -4: Mspeed_Start
//                                        // -3: Mspeed_Idle
//                                        // -2: Mspeed_Stop
//                                        // -1: not described
//                    case 'P' -> 9;      // pol8 = fuzzyInLevel
//                    case 'A' -> 10;      // pol7 = oznacza id tablicy danych do fuzzy logic
//                    case 'G' -> 10;      // pol7 = oznacza id tablicy danych do fuzzy logic
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

    public void saveShaft(String fileName) {
        
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Path.of(fileName))) {
            
            // list = data.getListPerpendicular();
            list = shaftTable.getList();

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
        data.calc_acc(1, 15);       // 'R' 'S'  ->  15      przelicza odchylenie od średniej
        data.calc_shaft(3, 16);     // 'Q'  ->  16          przelicza pozycje 'n' na pozycję obrotową [+250...-250]

    }

    void createShaftTable() {
        Integer lastShaft, shaft, period, duty, acc;
        List<Integer> varList;
        
        lastShaft = 1;
        for (int i = 0; i < data.getSize(); i++) {
            varList = data.getIntegerList(i);
            
            shaft = varList.get(3);     // "Q"
            
            if (shaft != null) {
                if (shaft < lastShaft) {
                    shaftTable.update();
                    shaftTable.clean();
                }
                period = varList.get(4);// "T"
                duty = varList.get(2);  // "X"
                acc = varList.get(10);  // przeliczone w odniesieniu do '0'
                
                shaftTable.writeSeries(shaft, period, duty, acc );
                
                lastShaft = shaft;
            }
        }
     
//        for (int i = 0; i < 50; i++) {
//            shaftTable.writePeriod(i, "" + (20 + i));
//        }
//
//        for (int i = 10; i < 20; i++) {
//            shaftTable.writeDuty(i, "" + (201 + i));
//        }
        
        
        // after shaft turn created it must be updated
        shaftTable.update();
        
        shaftTable.clean();
    }
    
}
