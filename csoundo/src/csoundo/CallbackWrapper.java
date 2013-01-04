package csoundo;

import csnd.*;
import java.io.*;
import java.util.*; 


public class CallbackWrapper extends CsoundCallbackWrapper{
    public Csound csound;
    public MessageQueue messageQueue;
    
    public CallbackWrapper(Csound csnd){
        super(csnd);
        csound = csnd;
        messageQueue = new MessageQueue();
    }
   
    public int YieldCallback()
    {
    //update channels
    for(int i=0;i<messageQueue.getNumberOfMessagesInChannelQueue();i++)
    csound.SetChannel(messageQueue.getMessageFromChannelQueue(i).channelName, 
                      messageQueue.getMessageFromChannelQueue(i).channelData);

    //update table values
    for(int i=0;i<messageQueue.getNumberOfMessagesInTableQueue();i++)
    csound.TableSet(messageQueue.getMessageFromTableQueue(i).tableNumber, 
                    messageQueue.getMessageFromTableQueue(i).xVal,
                    messageQueue.getMessageFromTableQueue(i).yVal);

    
    //flush messages from queues
    messageQueue.flushMessagesFromTableQueue();
    messageQueue.flushMessagesFromChannelQueue();
    return 1;
    }

    
}
