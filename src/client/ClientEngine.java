package client;

/**
 * Class ClientSideGameEngine
 * This is based off of the ArcadeDemo.  
 */

import imgrecognition.QRHelper;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import shared.AnimationPanel;
import shared.GenericComm;
import shared.Player;


public class ClientEngine extends AnimationPanel implements ActionListener
{
    //Constants
    //-------------------------------------------------------
    private boolean COMMUNICATION_ON = true;
    private boolean CHEATS_ON = false;
    private boolean CAMERA_DISPLAY_ON = true;
    
    public enum StationColor { BLUE, RED, GREEN, BLACK, WHITE, ORANGE, PURPLE, YELLOW }
    public Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.BLACK, Color.WHITE, Color.ORANGE, Color.MAGENTA, Color.YELLOW};
    
    //Instance Variables
    //-------------------------------------------------------
    
    //Network variables
    //-------------------------------------------------------
    private int myID; //for Networking
    private Timer timer;
    private final int DELAY = 500; //delay in mSec
    private GenericComm comm;
    String displayMessage = "No message.";
    String response = "";
    
    private StationColor stationColor;
    private String stationName;
    private Color myColor;
    
    private StationRoster stationRoster;
    private ArrayList<String> QRIHaveWelcomed;
    private MessageList messageList;
    private QRHelper qrHelper;
    //Constructor
    //-------------------------------------------------------
    public ClientEngine()
    {   //Enter the name and width and height.  
        super("Client Window", 600, 500);
        
        qrHelper = new QRHelper();
        stationRoster = new StationRoster();        
        messageList = new MessageList();
        QRIHaveWelcomed = new ArrayList<>();
        
        //Which station is this?!! 
        Object selectedValue = JOptionPane.showInputDialog(null, 
            "Choose one", "Input",
            JOptionPane.INFORMATION_MESSAGE, null,
            StationColor.values(), StationColor.values()[0]);
        stationColor = (StationColor)selectedValue;
        stationName = ""+stationColor;
        
        if(COMMUNICATION_ON)
        {
            comm = new GenericComm();
//            comm.setDebugValues("C_COMM",0); 
            myID = -1;  //Until I get a WELCOME message...
            initTimer();
        }
    }

    /**
     * This updates the animation view every frame.  
     * @param g
     */       
    @Override
    protected void renderFrame(Graphics g) 
    {
        g.setColor(colors[stationColor.ordinal()]);
        g.fillRect(0,0,this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(20,20,this.getWidth()-40, this.getHeight()-40);
        g.setColor(Color.BLACK);
        
        String qrText = checkForQRAndRespond();     
        
        if(CAMERA_DISPLAY_ON)
        {
            g.drawImage(qrHelper.getCameraImage(), 600, 50, this);
//            g.drawImage(qrHelper.getQRImage(), 800, 600, this);
        }
        
        messageList.removeOldMessages();
        g.setFont(new Font("Helvetica", Font.BOLD, 42));
        for(int z=0; z<messageList.getMessages().size(); z++)
        {
            String s = messageList.getMessages().get(z).getMessage();
            g.drawString(s, 30, 250+50*z);
        }


        g.setFont(new Font("Helvetica", Font.BOLD, 18));
        g.drawString("This is the "+stationColor+" station.", 30, 35);
//        g.drawString("QR-TEXT" + qrText,90,150);
//        g.drawString("msg= "+displayMessage, 25,100);
    }//--end of renderFrame method--

    
    public String checkForQRAndRespond()
    {
        String qrText = qrHelper.convertImageToText(qrHelper.getQRImage());

        if(qrText != null && !qrText.equals("QR Not found"))
        { 
            if(!stationRoster.hasVisitedRecently(qrText)) //Ignore if a repeat...
            {
//                stationRoster.add(qrText);
                sendCheckinToServer(qrText);
            }
            stationRoster.add(qrText);
        }
        return qrText;  
    }

    /**
     * This sends the CHECKIN message to the server. It should 
     * @param qrText 
     */
    public void sendCheckinToServer(String qrText)
    {   //Update Server
        String message = "CHECKIN,"+stationName+","+qrText;
        comm.sendMessage(message);
    }
    //-------------------------------------------------------
    //Respond to Mouse Events
    //-------------------------------------------------------
    public void mouseClicked(MouseEvent e)  
    {
    }
    
    //-------------------------------------------------------
    //Respond to Keyboard Events
    //-------------------------------------------------------
    public void keyTyped(KeyEvent e) 
    {
        char c = e.getKeyChar(); 
//        if(c >= '0' && c <='9') //number 
//        {
//            String message = "CHECKIN"+c;
//            comm.sendMessage(message);
//            
////            System.out.println(c+" pressed   + "+response);
//        }
    }
    
    
    //-------------------------------------------------------
    //Initialize Graphics
    //-------------------------------------------------------       
//    private Image startScreenImage;
    
    public void initGraphics() 
    {      
        Toolkit toolkit = Toolkit.getDefaultToolkit();
    } //--end of initGraphics()--
    


    //*********************************************************
    // NETWORK STUFF
    //*********************************************************
    private void initTimer()
    {   //Set up a timer that calls this object's action handler.
        timer = new Timer(DELAY, this);
        timer.setInitialDelay(DELAY);
        timer.setCoalesce(true);
        timer.start();
    }
    public void sendMessage(String choice)
    {
        comm.sendMessage(choice); 
        do {
            response = comm.getMessage();
        }  while (comm.messageWaiting());
    }
    
    /**
     * This method is called whenever the Timer goes off, 
     * It also handles WELCOME messages.  
     * @param e 
     */
    public void actionPerformed(ActionEvent e) 
    {   
        //Receive a reply from the server.  
        if(myID == -1) //Need to listen to all until I get WELCOME
            response = comm.getMessage();
        else //typically just take the most recent message.  
            response = comm.getMostRecentMessage();

        //Choose how to deal with the message... 
        if(response == null)
        { 
//            response = "No response."; 
        }
        else if(response.startsWith("PLAYER"))
        {
            System.out.print("QRIHaveWelcomed:");
            for(String s : QRIHaveWelcomed) 
                System.out.print(s+",");
            System.out.println("=====");
            Player p = new Player(response);
            
            String msg = p.getName();
            if(p.getNumStationsVisited()==4)
                msg += ". You've made it to every station!";
            else if(p.getNumStationsVisited()>1)
                msg += ". You have been to "+p.getNumStationsVisited()+" stations.";
            if(!QRIHaveWelcomed.contains(p.getQRtext()))
            {
                messageList.addMessage("Welcome, "+msg);
                QRIHaveWelcomed.add(p.getQRtext());
            }
            else
            {
                messageList.addMessage("Welcome back, "+msg);
            }
            displayMessage = response;
            //update the roster and add a message
            System.out.println("CSGE_KD:"+response);
        }
        else if(response.startsWith("WELCOME"))
        {
            displayMessage = response;
            String[] parts = response.split(",");
            myID = Integer.parseInt(parts[1]); 
            //Tell the server my name... 
            comm.sendMessage("NAME,"+myID+","+comm.getUserName());
        }
        else
        {
            displayMessage = response;
            response = "???: "+response;
            debugMsg(response);
        }
    }   //end of actionPerformed()    
    
    
    private void debugMsg(String m)
    {
        if(GenericComm.debugMode)
            System.out.println(m);
    }

}//--end of ArcadeDemo class--

