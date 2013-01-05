package csoundo;

import processing.core.*;

import csnd.*;
import java.io.*;
import java.util.*; 


public class CallbackWrapper extends CsoundCallbackWrapper{
    public Csound csound;
    public MessageQueue messageQueue;

    
    public CallbackWrapper(Csound csnd){
        super(csnd);
        //System.out.print(this.getClass().getSuperclass().getName()+"\n");
        csound = csnd;
        messageQueue = new MessageQueue();
    }
   
    
    public int YieldCallback()
    {
    //update Csound channels
    for(int i=0;i<messageQueue.getNumberOfMessagesInChannelQueue();i++)
    csound.SetChannel(messageQueue.getMessageFromChannelQueue(i).channelName, 
                      messageQueue.getMessageFromChannelQueue(i).channelData);

    //update Csound table values
    for(int i=0;i<messageQueue.getNumberOfMessagesInTableQueue();i++)
    csound.TableSet(messageQueue.getMessageFromTableQueue(i).tableNumber, 
                    messageQueue.getMessageFromTableQueue(i).xVal,
                    messageQueue.getMessageFromTableQueue(i).yVal);

    csound.TableGet(1, 1);
    
    //flush messages from queues
    messageQueue.flushMessagesFromTableQueue();
    messageQueue.flushMessagesFromChannelQueue();
    return 1;
    }

    
    
    
}
