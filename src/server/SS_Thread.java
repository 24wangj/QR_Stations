package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import shared.GenericComm;
import java.net.*;
import javax.swing.Timer;

//=========================================================================
//THIS IS THE SERVER-SIDE 'LISTENER' THREAD...
// A seperate instance of this class is created for each user's connection.
// Static methods in this class can run and organize the game.  
//=========================================================================

public class SS_Thread extends Thread implements ActionListener 
{
    //--static class variables--
    private static int numConnections = 0;  //Counts the total number of connections
    private static final int MINUTE_IN_MS = 1000 * 60;
    private static final int DELAY = 20 * 1000;

    //--private instance variables--
    private ServerEngine serverEngine;
    private int myNumber = 0; //Which connection number am I?
    private GenericComm comm = null; //Generic communication object
    Socket mySocket;
    Timer timer;

    /**
     * The constructor for a Thread (a new Station).  
     * @param socket - the necessary server info...
     * @param theEngine - gives this Thread visibility to the game data.
     */
    public SS_Thread(Socket socket, ServerEngine theEngine) 
    {        
        super("SS_Thread");
        mySocket = socket;
        serverEngine = theEngine;
        //***There is a new connection, and I'm it!
        myNumber = numConnections;
        numConnections++;

        //***Initialize the GenericComm object.  
        comm = new GenericComm(socket);
        comm.setDebugValues("SS_Thread", myNumber);
    }

    //This method is automatically called once the Thread is running.  
    //---------------------------------------------------------------
    public void run() 
    {
//        Player myself = new Player(mine.getPort());
        serverEngine.addNewStation(myNumber,comm.getUserName());
        comm.sendMessage("WELCOME," + myNumber +","+comm.getUserName() ); //Send a welcome. 
        debugMsg("SS: Welcome #" + myNumber +comm.getUserName());
//        initTimer();

        String inputLine;
        //This loop constantly waits for input from Client and responds...
        //----------------------------------------------------------------
        while ((inputLine = comm.getMessage()) != null){ 
                
            if(inputLine.equals("UPDATE"))
            {
                String gameData = "Hello from the server.";
                comm.sendMessage(gameData);
            }
            else
            {
                String response = serverEngine.processInput(inputLine, myNumber);
//                String gameData = gameEngine.getStatusUpdate(myNumber);
                comm.sendMessage(response);
            }
        }
        System.out.println("END OF WHILE!!! - SS_Thread");
        serverEngine.processInput("terminated", mySocket.getPort());
        //Clean things up by closing streams and sockets.
        //-----------------------------------------------
        comm.closeNicely();
    } //--end of run() method--
    
    private void initTimer() {   //Set up a timer that calls this object's action handler.
        timer = new Timer(DELAY, this);
        timer.setInitialDelay(DELAY);
        timer.setCoalesce(true);
        timer.start();
    }
    
    public void actionPerformed(ActionEvent e) {
        //kick player from arena if no response after certain delay
//        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nShould kick this player: " + myNumber);
//        gameEngine.processInput("terminated", mySocket.getPort());
//       
//        comm.closeNicely();
//        timer.stop();
        //teach client to know when socket closed, and close itself.
       
    }
    
    public static int getNumStations() { return numConnections; }

    
    public void debugMsg(String msg)
    {
        if(ServerEngine.debug)
            System.out.println(msg);
    }

} //--end of SS_Thread class--

