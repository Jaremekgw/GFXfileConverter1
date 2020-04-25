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
public class Measure {
    public static final String SEPARATOR = ";";
    /* amount of elements in measurement */
    public static final int MAX_SIZE = 20;
    private String[] elements;
    Integer size;
    
    static public Measure create(String[] array) {
        Measure measure = new Measure(array);
        return measure;
    }

    private Measure(String[] array) {
        elements = array.clone();
        int s = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && i > s)
                s = i;
        }
        size = ++s;
    }
    
    /**
     * How many elements is in this measurement
     * @return 
     */
    public int size() {
        return size;
    }
    
    @Override
    public String toString() {
        String str = "";
        boolean next = false;
        
        for (int i = 0; i < size; i++) {
            if (elements[i] == null)
                str += (next ? SEPARATOR : "");
            else
                str += (next ? SEPARATOR : "") + elements[i];
            next = true;
        }
        return str;
    }

    public String getElement(int id) {
        if(id < size)
            return elements[id];
        return "";
    }
    
    final int acc_offset = 4400;
    
    /**
     * Calculate acceleration,
     *  decrease value for offset to get middle point on level near 0
     * 
     * @param get
     * @param put 
     */
    public void calc_acc(int get, int put, int offset) {
        int val;
        if (offset == 0)
            offset = acc_offset;
        val = offset;
        if ((get >= MAX_SIZE) || (put >= MAX_SIZE))
            return;
        if (elements[get] != null && elements[get].length() > 0)
            val = Integer.decode(elements[get]);
        val -= offset;
        String str = "" + val;
        elements[put] = str;
        put++;
        if (size < put)
            size = put;
    }

    final Double MAX_SHAFT_POSITION = 110.0;
    final Double MAX_SHAFT_RANGE = 500.0;

    /**
     * Calculate shaft position,
     *  from range [1..110] -> range [max_250 .. min_-250]
     * 
     * @param get
     * @param put 
     */
    void calc_shaft(int get, int put) {
        Integer val = 0;
        Double rad, level;         // Math.sin(radians)
        if ((get >= MAX_SIZE) || (put >= MAX_SIZE))
            return;
        if (elements[get] != null && elements[get].length() > 0)
            val = Integer.decode(elements[get]);
        else
            return;
        
        rad = Double.parseDouble(val.toString());
        if (rad > MAX_SHAFT_POSITION)
            rad = MAX_SHAFT_POSITION;

        rad /= MAX_SHAFT_POSITION;
        level = Math.cos(rad * 2 * Math.PI) * MAX_SHAFT_RANGE;
        
        //      x/110
        //    =J$295 *-1* (0,5 - SIN(PI()*D431/111))
        
        val = level.intValue();
        String str = "" + val;
        elements[put] = str;
        put++;
        if (size < put)
            size = put;        
    }

    List<Integer> getIntegerList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (elements[i] != null)
                list.add(Integer.valueOf(elements[i]));
            else
                list.add(null);
        }
        return list;
    }
}
