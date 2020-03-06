import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author diogo
 */
public class ThreadIdentify extends Thread
{
    private Mechanism mechanism;    
    private static final Semaphore semaphore = new Semaphore(0);
    static Integer lock = new Integer(0);
    private static final ArrayBlockingQueue messageQueue = new ArrayBlockingQueue(10);

    public ThreadIdentify(Mechanism mechanism) 
    {
        this.mechanism = mechanism;       
    }
    
    public static void startIdentification() 
    {
        semaphore.release();
    }
        
    public static Integer peekPart() 
    {
        return (Integer)messageQueue.peek();
    }
        
   public static Integer consumeMessage() 
   {
        try {
            return (Integer)messageQueue.take();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() 
    {
        while(true){            
            try {
                ThreadIdentify.semaphore.acquire();
                int part = mechanism.identify();
                messageQueue.add(part);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }  
    
    public static Integer TryConsumeMessage() {
        synchronized (lock) {
            if (messageQueue.peek() != null) {
                try {
                    return (Integer) messageQueue.take();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        } 
    }    
}
