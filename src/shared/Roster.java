package shared;

import java.util.HashMap;


/**
 * Keeps track of current Players in the game
 * @author spockm
 */
public class Roster 
{
    private final long RECENT = 10000; //msec
    private HashMap hm;
    
    public Roster()
    {
        hm = new HashMap(); 
    }
    
    
    public void addEntry(String qrText, Player p)
    {
        hm.put(qrText,p);
        System.out.println("Adding roster entry: "+p.getName()+" , "+qrText);
    }
    
    public boolean contains(String qrText)
    {
        return hm.containsKey(qrText);
    }
    
    public Player getPlayerFromCode(String qrText)
    {
        return (Player)hm.get(qrText);
    }
    
    public boolean hasVisited(String qrText)
    {
        return hm.containsKey(qrText);
    }
    
    public boolean hasVisitedRecently(String qrText, String stationName)
    {
        long now = System.currentTimeMillis();
        Player p = (Player)hm.get(qrText);
        long cTime = p.getLastCheckinTime();
        return (now-cTime < RECENT && p.getLastCheckInLocation().equals(stationName));
    }

    public void checkin(String qrText, String stationName)
    {
        long time = System.currentTimeMillis();
        if(hm.containsKey(qrText)) //This qr is registered in the roster.
        {
            Player p = getPlayerFromCode(qrText);
            p.updateLocation(stationName);
        }
        else //A qr that is not in the roster has checked in at a station.
        {
            Player p = new Player(qrText, "??");
            hm.put(qrText,p);
        }
    }
    
}
