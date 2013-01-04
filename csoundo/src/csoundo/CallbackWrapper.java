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
    for(int i=0;i<messageQueue.getNumberOfMessagesInQueue();i++)
    //System.out.println("yielded");
    csound.SetChannel(messageQueue.getMessageFromQueue(i).channelName, messageQueue.getMessageFromQueue(i).channelData);
    //messageQueue.flushMessagesFromQueue();
    return 1;
    }

    
}
