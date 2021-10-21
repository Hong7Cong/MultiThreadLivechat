/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package javaserver;


import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import static org.opencv.core.CvType.CV_8UC;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import static org.opencv.highgui.HighGui.imshow;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author Hong
 */

public class JavaServer extends Thread{
    static private int port = 9999;
    
    
    
    public void run() {
        // TODO code application logic here
        ServerSocket server = null;
        ObjectInputStream in = null;
        try {
            server = new ServerSocket(9999);
            System.out.println("Waiting ....");
            Socket socket = server.accept();
            System.out.println("Accept a client!");
            in = new ObjectInputStream(socket.getInputStream());
        }catch (IOException e) {
           System.out.println(e);
           e.printStackTrace();
        }
        
        JFrame frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(1000,500);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        JLabel label = new JLabel();
        label.setSize(640,360);
        label.setVisible(true);
        JLabel label2 = new JLabel();
        label2.setSize(1000,500);
        label2.setVisible(true);
        
        frame.add(label);
        frame.add(label2);
        frame.setVisible(true);

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            System.out.println("Initializing server .....");
            String _modelConfiguration = "MobileNetSSD_deploy.prototxt";
            String _modelBinary = "MobileNetSSD_deploy.caffemodel";
            ObjectDetector objectDetector = new ObjectDetector(_modelConfiguration, _modelBinary);
            
        while(true)
        {     
            ImageIcon icon = new ImageIcon();
            try{          
            icon = (ImageIcon)in.readObject();
            // Chuyen iconimage thanh bufferedImage
            Image im = icon.getImage();
            Image img = im.getScaledInstance(500, 400,  java.awt.Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
            /*
            //BufferedImage bi = (BufferedImage)icon.getImage();
            BufferedImage bi = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_3BYTE_BGR);
            //BufferedImage bi = new BufferedImage(1000,800,BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g2 = bi.createGraphics();
            g2.drawImage(img, 0, 0,null);
            g2.dispose();
            //Ghi BUffered image ra file jpg
            ImageIO.write(bi, "jpg",new File("C:\\Users\\asus\\Documents\\NetBeansProjects\\JavaServer\\out.jpg"));
            
            
            // Doc image tu file jpg
            Mat image =new Mat();
            image=Imgcodecs.imread("out.jpg");
            objectDetector.predict(image, true);

            //Chuyen doi mat sang buffedimage
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if ( image.channels() > 1 ) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }
            int bufferSize = image.channels()*image.cols()*image.rows();
            byte [] b = new byte[bufferSize];
            image.get(0,0,b); // get all the pixels
            BufferedImage image2 = new BufferedImage(image.cols(),image.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) image2.getRaster().getDataBuffer()).getData();
            System.arraycopy(b, 0, targetPixels, 0, b.length);  
            //Hienthi
            label.setIcon(new ImageIcon(image2));
            */
            }catch(Exception e){ System.out.println("Error reading current frame from client");}
               
        }
    }
    
    public static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
      }
    
    public static BufferedImage ImageIcon2BufferedImage(ImageIcon icon){
        BufferedImage bi = new BufferedImage(
            icon.getIconWidth(),
            icon.getIconHeight(),
            BufferedImage.TYPE_INT_RGB);
            Graphics g = bi.createGraphics();
            // paint the Icon to the BufferedImage.
            icon.paintIcon(null, g, 0,0);
            g.dispose();
            return bi;
    }
    
    public static BufferedImage ImageIcon2Image(ImageIcon icon)
    {
       return null; 
    }
}
