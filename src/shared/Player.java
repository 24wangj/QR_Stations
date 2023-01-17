package shared;

import java.util.ArrayList;

/**
 * @author spockm
 */
public class Player 
{
    String QRtext;
    String name;
    long lastCheckInTime;
    String lastCheckInLocation;
    ArrayList<String> stationsVisited; 
//    String currentDestination;
    
    public Player(String qr, String n)
    {
        QRtext = qr;
        name = n;
        lastCheckInTime = System.currentTimeMillis();
        stationsVisited = new ArrayList<String>();
        lastCheckInLocation = "";
//        currentDestination = "";
    }
    
    public Player(String packed)
    {
        unpack(packed);
    }
    
    public String getName() { return name; }
    public String getQRtext() { return QRtext; }
//    public String getDestination() { return currentDestination; }
    public int getNumStationsVisited() { return stationsVisited.size(); }
    public long getLastCheckinTime() { return lastCheckInTime; }
    public String getLastCheckInLocation() { return lastCheckInLocation; }
    
    public boolean isThisFirstTimeAtLocation(String loc)
    {
        return !stationsVisited.contains(loc);
    }
    
    public void updateLocation(String loc)
    {
        lastCheckInLocation = loc; 
        lastCheckInTime = System.currentTimeMillis();
        if(isThisFirstTimeAtLocation(loc))
        {
            stationsVisited.add(loc);
        }       
        System.out.println("Player "+name+" checked in at "+loc);    
    }
    
    
    //For networking...
    //========================================================================
    String split = ",";

    public String pack()
    {
        String result = "PLAYER";
        result+=split+name+split+QRtext+split+lastCheckInTime+split+lastCheckInLocation;
        for(String s : stationsVisited)
            result +=split+s;
        return result;
    }
    
    public void unpack(String in)
    {
        String[] parts = in.split(split);
        if(parts.length > 4 && parts[0].equals("PLAYER"))
        {
            name = parts[1];
            QRtext = parts[2];
            lastCheckInTime = Long.parseLong(parts[3]);
            lastCheckInLocation = parts[4];
            
            stationsVisited = new ArrayList<String>();
            for(int q=5; q<parts.length; q++)
            {
                stationsVisited.add(parts[q]);
            }
        }
    }
}
