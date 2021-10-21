/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaserver;

/**
 *
 * @author Hong
 */
public class Server {
    public static void main(String args[]) {  
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                 JavaServer js = new JavaServer();
                 js.start();
            }
        });
    }
}
