package imgrecognition;

import java.awt.image.BufferedImage;

/**
 * Methods used by both server and client to read a QR using a Camera.
 * @author spockm (using Jiaxuan's code) 
 */
public class QRHelper 
{
    public Camera camera; 
    
    public QRHelper()
    {
        camera = new Camera();
        System.out.println("Constructing a Camera object in QRHelper");
    }
    
//    public void initializeHelper()
//    {
//        if(camera == null)
//        {
//            camera = new Camera();
//            System.out.println("Constructing a Camera object in QRHelper");
//        }
//        else
//            System.out.println("Camera object is already constructed in QRHelper");
//            
//    }
    
    public BufferedImage getQRImage()
    {
        BufferedImage qrImage = null;
        try {
                qrImage = camera.scanQR();
                
            } catch (QRNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        return qrImage;
    }
    
    public BufferedImage getCameraImage()
    {
        BufferedImage cameraImage = null;
        cameraImage = camera.getCurrentFrame();
        return cameraImage;
    }
    
    public String convertImageToText(BufferedImage qrImage)
    {
        String text = "QR Not found";
        if(qrImage != null)
        {
            boolean[][] grid = QRUtil.qrToBooleanGrid(qrImage);
            BufferedImage qrDisplayImage = QRUtil.booleanGridToQR(grid);
            try {
                text = QRUtil.decode(grid);
            } catch (InvalidQRException e) {
                text = null;
                System.out.println(e.getMessage());
            }
        }
        
        return text;    
    }
  
}
