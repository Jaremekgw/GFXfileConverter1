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
    static ShaftModel shaftTable;   // table values for shaft for chart X-Y

    static File file;
    static Controller controller;
    static String[] fName = {
            "pol15_01",
            "pol15_02",
//        "pol14_02",
//        "pol14_01",
        //"gfx_acc_03",
//        "gfx_acc_02",
//        "pol13_03",
            //"pol12_01",
            //"pol12_02",
        //    "pol11_03",
        //    "pol11_04",

//          "pol10_01_t1",
//          "pol10_02_t6",
//          "pol10_03_t5",
//          "pol10_04_t4",
//          "pol10_05_t3",
//          "pol10_06_t2",
//          "pol10_07_m2_film",
            
//          "pol9_01a",
//          "pol9_01b",
//          "pol9_01c",
//          "pol9_02a",
//          "pol9_02b",
//          "pol9_03",
//          "pol9_04a",
//          "pol9_04b",
//          "pol9_04c",
//          "pol9_04d",
//          "pol9_04e",

//        "pol8_01",
//        "pol8_02",
//        "pol8_03",
//        "pol8_04",
        
//        "pol7_01",
//        "pol7_03",
//        "pol7_05",  //
//        "pol7_0x3",
        
        //"pol6_01_auto",
        //"pol6_02_auto",
        
//        "pol5_01_400",
//        "pol5_02_250",
//        "pol5_03_200",
//        "pol5_04_150",
//        "pol5_05_100",
//        "pol5_06_200",
//        "pol5_07_150",
        
    //    "pol4_01_250",
    //    "pol4_02_220",
    //    "pol4_03_210",
    //    "pol4_04_200",
    //    "pol4_05_200",
    //    "pol4_06_300",
    //    "pol4_07_250",
    //    "pol3_02_200",
    //    "pol3_03_150",
//        //"pol3_04_300",
//        //"pol3_05_600",
    //    "pol3_06_110"
////        "pol2_01_400",
////        "pol2_02_500",
////        "pol2_03_600",
////        "pol2_04_400",
//  //      "pol2_05_500",
////        "pol2_06_600",
////        "pol2_07_700",
////        "pol2_08_800",
////        "pol2_09_900",
////        "pol2_10_1000"
            };
    
    
    
    public static void main(String[] args) {
        
        for (String name : fName) {
            file = new File(name + ".txt");
        
            if(!file.exists()) {
                System.out.println("Brak pliku z danymi:" + name + ".txt");
                continue;
            }
            data = new MeasureModel();
            shaftTable = new ShaftModel();
            controller = new Controller(data, shaftTable);
            
            /* read file and put data into table (data object) */
            controller.read(file);
            
            /* calculate additional values for better analyse */
            controller.calculate();
            
            /* create new table for additional diagrams for analyze */
            controller.createShaftTable();
            
            controller.saveShaft(name + "_Shaft.csv");
            
            /* write data in csv format for futher analysing */
            controller.save(name + ".csv");
        }
    }
}
