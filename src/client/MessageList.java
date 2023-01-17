package client;

import java.util.ArrayList;

/**
 * Keeps track of multiple messages to be displayed, 
 * also removes messages when they time out.
 * @author spockm
 */
public class MessageList 
{
    private ArrayList<Message> messageList;
    
    public MessageList()
    {
        messageList = new ArrayList<>();
    }
    
    public void addMessage(String text)
    {
        messageList.add(new Message(text));        
    }
    
    public ArrayList<Message> getMessages() 
    {
        return messageList;
    }
    
    public void removeOldMessages()
    {
        for(int z=0; z<messageList.size(); z++)
        {
            if(messageList.get(z).shouldRemove())
                messageList.remove(z);
        }
    }
}
