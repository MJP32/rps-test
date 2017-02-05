package com.tw.casino.actor;

import com.tw.casino.ResponseListener;

/**
 * Base class for Context classes. This class extends Thread for lifecycle 
 * management along with calling various features provided by the 
 * PlayerContext. Additionally, it allows the Context to be managed 
 * alongside other resources.
 * 
 * @author siddharths1787
 *
 */
public abstract class ActorContext implements ResponseListener
{
    public abstract void run();
    
    public abstract void displayMessage(String message);
}
