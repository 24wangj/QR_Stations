package client;

import java.util.HashMap;

/**
 * This class keeps track of QRcodes that have checked in here. 
 * It is useful in 'de-bouncing' the input. Only recognizing a QR once amid the noise.
 * @author spockm
 */
public class StationRoster 
{
    private final long RECENT = 10000; //msec (It will only re-process a QR after this much time.)
    //keep qr and last valid check-in time
    private HashMap hm;
    
    public StationRoster()
    {
        hm = new HashMap();
    }
    
    public void add(String qrText)
    {
        long time = System.currentTimeMillis();
        hm.put(qrText,time);
    }
    
    public boolean hasVisited(String qrText)
    {
        return hm.containsKey(qrText);
    }
    
    public boolean hasVisitedRecently(String qrText)
    {
        if(!hasVisited(qrText))
            return false;
        long now = System.currentTimeMillis();
        long cTime = (long)hm.get(qrText);

        return (now-cTime < RECENT);
    }
}
