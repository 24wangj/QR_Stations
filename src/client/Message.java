package client;

/**
 *
 * @author spockm
 */
public class Message 
{
    private final long MESSAGE_DURATION = 6000; //mSec
    private String message;
    private long removalTime;
    
    public Message(String msg)
    {
        message = msg;
        removalTime = System.currentTimeMillis()+MESSAGE_DURATION;
    }
    
    public String getMessage() { return message; }
    public boolean shouldRemove() 
    {
        return System.currentTimeMillis() > removalTime;
    }
    
    
}
