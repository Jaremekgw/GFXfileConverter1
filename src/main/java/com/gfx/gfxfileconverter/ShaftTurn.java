/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gfx.gfxfileconverter;

/**
 *
 * @author jarek
 */
public class ShaftTurn {
        int shaft;
        Integer period;
        Integer duty;
        Integer acceleration;
        
        ShaftTurn(int s) {
            shaft = s;
            clean();
        }

        void setPeriod(Integer p) {
            this.period = p;
        }

        void setDuty(Integer d) {
            this.duty = d;
        }

        void setAcceleration(Integer a) {
            this.acceleration = a;
        }

        public Integer getPeriod() {
            return period;
        }

        public Integer getDuty() {
            return duty;
        }

        public Integer getAcceleration() {
            return acceleration;
        }

        void clean() {
            this.period = null;
            this.duty = null;
            this.acceleration = null;
        }
}
