/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.threadinglab;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author soblab
 */
public class Main {

    static int grandTotal = 0;
    static Queue<String> que = new LinkedList<>();
    static Object grandLock = new Object();
    static Object QueLock = new Object();
    static CountDownLatch cdl = new CountDownLatch(5);
    
    public static void sumFile(){
        
        String fileName = "";
        synchronized(QueLock){
            fileName = que.poll();
        }
        try {
           
            
            Scanner scan = new Scanner(new FileReader(fileName));
            int fileTotal = 0;
            
            while(scan.hasNextInt()){
                
                fileTotal+=scan.nextInt();
                
            }
            
            synchronized(grandLock){
                grandTotal+=fileTotal;                 
            }
            
            System.out.println(fileName + " total: " + fileTotal);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void main(String[] args) {

        
        
        
        ExecutorService exe = Executors.newSingleThreadExecutor();

        for (int i = 0; i < 10; i++) {
            final int loopNum = i;
            exe.submit(() -> {

                String str = Thread.currentThread().getName();
                System.out.println(str + " Loop Num: " + loopNum);

            });

        }
        System.out.println("===============================================");
          ExecutorService exe2 = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 10; i++) {
            final int loopNum = i;
            exe2.submit(() -> {

                String str = Thread.currentThread().getName();
                System.out.println(str + " Loop Num: " + loopNum);

            });

        }
        exe.shutdown();
        
        que.add("file1.txt");
        que.add("file2.txt");
        que.add("file3.txt");
        que.add("file4.txt");
        que.add("file5.txt");

        ExecutorService exe3 = Executors.newFixedThreadPool(2);
        
        for(int i = 0; i < 5; i++){
            final int loopNum = i;
            exe3.submit(() -> {

                Main.sumFile();

            });
        }
        
        
        System.out.println(grandTotal + " gt");
        
        
    }

}
