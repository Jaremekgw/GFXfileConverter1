/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gfx.gfxfileconverter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jarek
 */
public class ShaftModel {
    /* it is a table for X-Y diagram of values to the shaft position 
     * It is a list of 112 lines with shaft position at the beginning 
     * of each line
     */
    List<DiagramShaftLineAxe> table;
    public final int MAX_SHAFT = 111;    // [1..111]; position 0 = description
    /* temporary values needed for update */
    List<ShaftTurn> turn;
    Integer persistPeriod;
    Integer persistDuty;
    Integer persistAcceleration;
    
    public ShaftModel() {
        table = new ArrayList<>();
        turn = new ArrayList<>();
        // It creates X-axis with values as a shaft position
        for (int i = 0; i <= this.MAX_SHAFT; i++) {
            table.add(new DiagramShaftLineAxe(i));
            turn.add(new ShaftTurn(i));
        }
        persistPeriod = null;
        persistDuty = null;
        persistAcceleration = null;
    }
   
    public void writePeriod(int shaft, String strPeriod) {        
        Integer p = Integer.valueOf(strPeriod);
        
        if (shaft < 0 || shaft > MAX_SHAFT)
            return;
        if (persistPeriod == null)
            persistPeriod = p;
        turn.get(shaft).setPeriod(p);
    }

    public void writeDuty(int shaft, String strDuty) {        
        Integer d = Integer.valueOf(strDuty);
        
        if (shaft < 0 || shaft > MAX_SHAFT)
            return;
        if (persistDuty == null)
            persistDuty = d;
        turn.get(shaft).setDuty(d);
    }

    public void writeAccelerate(int shaft, String strAcc) {        
        Integer a = Integer.valueOf(strAcc);
        
        if (shaft < 0 || shaft > MAX_SHAFT)
            return;
        if (persistAcceleration == null)
            persistAcceleration = a;
        turn.get(shaft).setAcceleration(a);
    }

    /**
     * It moves temporary 'turn' data into 'table'
     * and fill missed elements
     * 
     */
    void update() {
        ShaftTurn shaftTurn;
        Integer tempPeriod, tempDuty, tempAcc;
        
        if (persistPeriod == null)
            persistPeriod = 0;
        if (persistDuty == null)
            persistDuty = 0;
        if (persistAcceleration == null)
            persistAcceleration = 0;
        /* write all point into table to each line respectively */
        for (int i = 0; i <= this.MAX_SHAFT; i++) {
            shaftTurn = turn.get(i);
            
            tempPeriod = shaftTurn.getPeriod();
            if (tempPeriod == null)
                tempPeriod = persistPeriod;
            tempDuty = shaftTurn.getDuty();
            if (tempDuty == null)
                tempDuty = persistDuty;
            tempAcc = shaftTurn.getAcceleration();
            if (tempAcc == null)
                tempAcc = persistAcceleration;
           
            /* tutaj wpisuje do tabeli ale można zrobić analizę tego
            * obrotu i zapisac do tabeli odpowiedniej dla danych potrzeb
            * na przykład do wyznaczania tylko rozpoczecia ruchu
            */
            table.get(i).add(tempPeriod, tempDuty, tempAcc);
            persistPeriod = tempPeriod;
            persistDuty = tempDuty;
            persistAcceleration = tempAcc;
        }
        
        // po zapisaniu wykasowac temporary data
        for (int i = 0; i <= this.MAX_SHAFT; i++) {
            turn.get(i).clean();
        }
        persistPeriod = null;
        persistDuty = null;
        persistAcceleration = null;
    }
    
    public List<String> getList() {
        List<String> list = new ArrayList<>();
 
        table.forEach((line) -> {
            list.add(line.toString());
        });
        return list;
    }

    void clean() {
        for (int i = 0; i <= this.MAX_SHAFT; i++) {
            turn.get(i).clean();
        }
        persistPeriod = null;
        persistDuty = null;
        persistAcceleration = null;
    }

    void writeSeries(Integer shaft, Integer period, Integer duty, Integer acc) {

        if (shaft < 0 || shaft > MAX_SHAFT)
            return;
        
        if (period != null) {
            if (persistPeriod == null)
                persistPeriod = period;
            turn.get(shaft).setPeriod(period);
        }
        if (duty != null) {
            if (persistDuty == null)
                persistDuty = duty;
            turn.get(shaft).setDuty(duty);
        }
        if (acc != null) {
            if (persistAcceleration == null)
                persistAcceleration = acc;
            turn.get(shaft).setAcceleration(acc);
        }
        
    }
 
}
