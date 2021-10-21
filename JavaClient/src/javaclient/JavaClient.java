/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaclient;

import com.github.sarxos.webcam.Webcam;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Hong
 */
public class JavaClient {
    static public Socket socket;
    static String ip = "localhost";
    static int port = 9999;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        ObjectOutputStream dout = null;
        BufferedImage bm = null;
        ImageIcon im = null;
        try {
        socket = new Socket(ip,port);
        bm = webcam.getImage();
        im = new ImageIcon(bm);
        dout = new ObjectOutputStream(socket.getOutputStream());
        }catch (IOException e) {
           System.out.println(e);
           e.printStackTrace();
        }
        
        JFrame frame = new JFrame("PC 1");
        frame.setLayout(new FlowLayout());
        frame.setSize(360,280);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        JLabel label = new JLabel();
        //label.setSize(640,360);
        label.setVisible(true);
        
        
        frame.add(label);
        frame.setVisible(true);
        while(true)
        {
            try{
            bm = webcam.getImage();
            im = new ImageIcon(bm);
            label.setIcon(im);
            dout.writeObject(im);
            
            dout.flush();
            }catch(Exception e){ System.out.println("Error reading current frame from server");}
        }
    }
    
}
