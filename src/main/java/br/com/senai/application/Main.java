package br.com.senai.application;

import entities.Reader;
import entities.Sensor;
import java.util.Locale;

/* 
    Project: Data Collector of Automation
    Objective:
        -   Collect;
        -   Treatment;
        -   Industrial data validation.
*/

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        System.out.println("====================================");
        System.out.println(" INDUSTRIAL MONITORING SYSTEM - V01");
        System.out.println("====================================");

        for (int i = 1; i <= 5; i++) {
            System.out.printf("Reading the data process - Cycle %d %n", i);

            double temperatureValue = Reader.sensorRead();

            Sensor sensor = new Sensor("Legal", temperatureValue);
            sensor.dataSecurityValidation();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
               System.out.println("ERROR IN THE TIMER");
            }
        }
        System.out.println("====================================");
    }
}
