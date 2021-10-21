package javaserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hong
 */
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
 
public class MultiThreadServer {
    
    public MultiThreadServer(){
        
    }
    
   public static void main(String args[]) throws IOException {
 
       ServerSocket server = null;
       ObjectInputStream in = null;
       System.out.println("Server is waiting to accept user...");
       int clientNumber = 0;
       try {
           server = new ServerSocket(9999);
       } catch (IOException e) {
           System.out.println(e);
           System.exit(1);
       }
        
       try {
           while (true) {
               System.out.println("Waiting ....");
               Socket socketOfServer = server.accept();
               System.out.println("Accept a client!");
               new ServiceThread(socketOfServer, clientNumber++).start();
           }
       } finally {
           server.close();
       }
 
   }
 
   private static void log(String message) {
       System.out.println(message);
   }
 
   private static class ServiceThread extends Thread {
 
       private int clientNumber;
       private Socket socketOfServer;
       JFrame frame = new JFrame();
       JLabel label = new JLabel();
       JLabel label2 = new JLabel();
       ObjectInputStream in = null;
       ObjectDetector objectDetector = null;
       public ServiceThread(Socket socketOfServer, int clientNumber) {
           this.clientNumber = clientNumber;
           this.socketOfServer = socketOfServer;
           
           try {
            in = new ObjectInputStream(socketOfServer.getInputStream());
            }catch (IOException e) {
               System.out.println(e);
               e.printStackTrace();
            }
           
            frame.setLayout(new FlowLayout());
            frame.setSize(1000,500);
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            label.setSize(640,360);
            label.setVisible(true);
            label2.setSize(1000,500);
            label2.setVisible(true);
                        
            frame.add(label);
            frame.add(label2);
            frame.setVisible(true);
            
            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Initializing server .....");
            String _modelConfiguration = "MobileNetSSD_deploy.prototxt";
            String _modelBinary = "MobileNetSSD_deploy.caffemodel";
            objectDetector = new ObjectDetector(_modelConfiguration, _modelBinary);
       
           // Log
           log("New connection with client# " + this.clientNumber + " at " + socketOfServer);
       }
 
       @Override
       public void run() {
           while(true)
        {     
            ImageIcon icon = new ImageIcon();
            try{          
            icon = (ImageIcon)in.readObject();
            // Chuyen iconimage thanh bufferedImage
            Image im = icon.getImage();
            Image img = im.getScaledInstance(500, 400,  java.awt.Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
            }catch(Exception e){ System.out.println("Error reading current frame from client");}        
        }
       }
   }
   
   private static class Detector extends Thread{
       JFrame frame = new JFrame();
       JLabel label = new JLabel();
       JLabel label2 = new JLabel();
       ObjectInputStream in = null;
       ObjectDetector objectDetector = null;
       
       public Detector(Image img){
           
       }
       
       public void run() {
           
       }
   }
}
