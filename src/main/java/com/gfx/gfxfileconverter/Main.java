/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gfx.gfxfileconverter;

import java.io.File;

/**
 *
 * @author jarek
 */
public class Main {
    
    static MeasureModel data;
    static File file;
    static Controller controller;
    static String[] fName = {
        "pol2_01_400",
        "pol2_02_500",
        "pol2_03_600",
        "pol2_04_400",
        "pol2_05_500",
        "pol2_06_600",
        "pol2_07_700",
        "pol2_08_800",
        "pol2_09_900",
        "pol2_10_1000"};
    
    
    
    public static void main(String[] args) {
        
        for (String name : fName) {
            file = new File(name + ".txt");
        
            if(!file.exists()) {
                System.out.println("Brak pliku z danymi:" + name + ".txt");
                continue;
            }
            data = new MeasureModel();
            controller = new Controller(data);
            
            /* read file and put data into table (data object) */
            controller.read(file);
            
            /* calculate additional values for better analyse */
            controller.calculate();
            
            
            
            /* write data in csv format for futher analysing */
            controller.save(name + ".csv");
        }
    }
}
