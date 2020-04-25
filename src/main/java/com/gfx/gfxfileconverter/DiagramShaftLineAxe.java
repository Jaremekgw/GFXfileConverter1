/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gfx.gfxfileconverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Concept is to have different values for appropriate shaft position
 * collected from different point of table from main data.
 * It has array of only one type od value, the order is important.
 * So I used ArrayList.
 * 
 * @author jarek
 */
public class DiagramShaftLineAxe {
    private List<Integer> period;       // Inverse of speed
    private List<Integer> duty;
    private List<Integer> acceleration;
    private int shaft;

    public DiagramShaftLineAxe(int shaft) {
        period = new ArrayList<>();
        duty = new ArrayList<>();
        acceleration = new ArrayList<>();
        this.shaft = shaft;
    }
    
    public void writePeriod(Integer p) {
        period.add(p);
    }

    public void writeDuty(Integer p) {
        duty.add(p);
    }

    void add(Integer tempPeriod, Integer tempDuty, Integer tempAcc) {
        period.add(tempPeriod);
        duty.add(tempDuty);
        acceleration.add(tempAcc);
    }
    
    @Override
    public String toString() {
        String str = "" + shaft;
        
        for (int i = 0; i < period.size(); i++) {
//            str += (Measure.SEPARATOR + period.get(i).toString()
//                + Measure.SEPARATOR + duty.get(i).toString()
//                + Measure.SEPARATOR + acceleration.get(i).toString());            
            str += (Measure.SEPARATOR + period.get(i).toString());
//                + Measure.SEPARATOR + duty.get(i).toString()
  //              + Measure.SEPARATOR + acceleration.get(i).toString());            
        }
        str += Measure.SEPARATOR;
        return str;
    }
}
