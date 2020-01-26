/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gfx.gfxfileconverter;

import java.math.BigInteger;
import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;


/**
 *
 * @author jarek
 */
public class MeasureModel {
    private List<Measure> table;
    private String[] label = new String[Measure.MAX_SIZE];
    private Integer amount;
    
    public MeasureModel() {
        table = new ArrayList<>();
        amount = 0;
    }
    
    public void add(Measure measure) {
        table.add(measure);
        
        if (measure.size() > amount)
            amount = measure.size();
    }
    
    public List<String> getListHorizontal() {
        List<String> list = new ArrayList<>();
        boolean show;
        
        for (int i = 0; i < amount; i++) {
            String str;
            if (label[i] == null)
                str = "v" + i + Measure.SEPARATOR;
            else
                str = label[i] + Measure.SEPARATOR;
            
            show = false;
            for (Measure measure : table) {
                str += (show ? Measure.SEPARATOR : "") + measure.getElement(i);
                show = true;
            }
            list.add(str);            
        }
        
        return list;
    }

    public List<String> getListPerpendicular() {
        List<String> list = new ArrayList<>();
        //boolean show = false;
        String str = "";
        
        for (int i = 0; i < amount; i++)
            if (label[i] == null)
                str += "v" + i + Measure.SEPARATOR;
            else
                str += label[i] + Measure.SEPARATOR;

        list.add(str);            

        for (Measure measure : table)
            list.add(measure.toString());
        
        return list;
    }

    
    public Integer lenght() {
        return amount;
    }
    
    public Integer getSize() {
        return table.size();
    }
    
    @Override
    public String toString() {
        String str = "";
        int pos = 0;
        
        for (Measure measure : table)
            str += "\r\n\t [" + (pos++) + "] = "  + measure.toString();
        return str;
    }

    /**
     * Get string of only 'i' element from all measurements
     * 
     * @param i
     * @return 
     */
    String getString(int i) {
        boolean show = false;
        String str = "";
        for (Measure measure : table) {
            str += (show ? Measure.SEPARATOR : "") + measure.getElement(i);
            show = true;
        }
        return str;
    }
    
    /**
     * Calculate acceleration,
     *  decrease value for offset to get middle point on level near 0
     * 
     * @param get
     * @param put 
     */
    void calc_acc(int get, int put) {
        long sum = 0;
        int count = 0;
        String str;
        
        /* first calculate average acc */
        for (Measure measure : table) {
            str = measure.getElement(get);
            if (str != null && str.length() > 0) {
                sum += Long.decode(str);
                count++;
            }
        }
        if (count > 0)
            sum /= count;
        
        for (Measure measure : table)
            measure.calc_acc(get, put, (int)sum);

        if (put + 1 > amount)
            amount = put + 1;
        label[put] = "acceleration";
    }

    /**
     * Calculate shaft position,
     *  from range [1..110] -> range [max_250 .. min_-250]
     * 
     * @param get
     * @param put 
     */
    void calc_shaft(int get, int put) {
        
        for (Measure measure : table)
            measure.calc_shaft(get, put);

        if (put + 1 > amount)
            amount = put + 1;
        label[put] = "shaft";
    }

}
