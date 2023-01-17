package server;

import java.net.*;
import java.io.*;
import javax.swing.JFrame;

/**
 * This class is a mash-up of the steps to start a network server
 * as well as starting an arcadeDemo frame (to display)
 * @author spockm
 */
public class ServerRunner //The Server-side main (MULTI_SERVER!)
{
    
    /**
     * The runnable MAIN method... Yea!
     * Basically it just constructs one of itself to do stuff.
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException 
    {      
        ServerRunner main = new ServerRunner();
    }
    
    int FPS = 30;   //Frames per second (animation speed)
    ServerEngine theEngine;
    JFrame myFrame;
//    ArrayList<SS_Thread> threads = new ArrayList<SS_Thread>();

    /**
     * The constructor for this class.  
     * Along with some debug statements to let you know things are working, 
     * It first: initializes the GameEngine display (like ArcadeRunner)
     * Second: initializes a ServerSocket to accept players.  
     * @throws IOException 
     */
    public ServerRunner() throws IOException
    {
        System.out.println("Beginning of SS_Main");
        initGameEngine();
        System.out.println("SS_GameEngine initialized");
        acceptClients();
        System.out.println("End of SS_Main");
    }
    
    public void initGameEngine()
    {
        theEngine = new ServerEngine();
//        myFrame = new JFrame();
//        myFrame.addWindowListener(new ServerRunner.Closer());
//        addFrameComponents();
//        startAnimation();
//        myFrame.setSize(theEngine.getPreferredSize());
//        myFrame.setVisible(true);
    }
    
    /**
     * This sets up the Server to forever listen to see if anyone
     * asks to join the game.  
     * 
     * @throws IOException 
     */
    public void acceptClients() throws IOException
    {
        ServerSocket sendSocket = null;
        boolean listening = true;

        //Declare and establish the Server Socket...
        //-----------------------------------------
        try  
        {
            sendSocket = new ServerSocket(4444);
        } 
        catch (IOException e) 
        {
            System.err.println("Could not listen on port: 4444. or 4445.?");
            System.exit(-1);
        }

        //Create a new thread whenever someone tries to connect...
        //-----------------------------------------
        while (listening)
        {
          System.out.println("Listening for new clients.");
          new SS_Thread(sendSocket.accept(), theEngine).start();
         
        }

        //Clean things up by closing socket.
        //-----------------------------------------------
        sendSocket.close();
    }
    
    //==============================================================
    // These next three methods are part of setting up the game engine display.
    // You will find them in ArcadeRunner.
    //==============================================================
    
//    public void addFrameComponents() 
//    {
//        myFrame.setTitle(theEngine.getMyName() + " - " + theEngine.getPreferredSize().width+"x"+theEngine.getPreferredSize().height);
//        myFrame.add(theEngine);
//    }
    
//    public void startAnimation() 
//    {
//        javax.swing.Timer t = new javax.swing.Timer(1000/FPS, new ActionListener() 
//        {   //This is something you may not have seen before...
//            //We are coding a method within the ActionListener object during it's construction!
//            public void actionPerformed(ActionEvent e) 
//            {
//                myFrame.getComponent(0).repaint();
//                myFrame.setSize(myFrame.getComponent(0).getPreferredSize());
//            }
//        }); //--end of construction of Timer--
//        t.start();
//    }    
    
    private static class Closer extends java.awt.event.WindowAdapter 
    {   
        public void windowClosing (java.awt.event.WindowEvent e) 
        {   System.exit (0);
        }   //======================
    }      

}

