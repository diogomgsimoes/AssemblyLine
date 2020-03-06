
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author diogo
 */

public class ThreadSequence2 extends Thread { 
    
    private final Mechanism mechanism;  
    Hardware hard; 
    private final int[] sequenceArray;
    public int partsReceived;
    public int completeSequences;
    public int partsRecycle;
    private ThreadClicks clicks;
     
    public ThreadSequence2(Hardware hard, Mechanism mechanism, ThreadClicks clicks) 
    {
        this.hard = hard;
        this.mechanism = mechanism;    
        this.sequenceArray = new int[]{ 0,1,2,0,1,2 }; //Sequence
        this.partsReceived = 0;
        this.completeSequences = 0;
        this.clicks = clicks;  
    }
    
    @Override
    public void run(){
        
        int i = 0;
        int nextDesiredPart = sequenceArray[i];
        
        while(true){
            try {
                ThreadSequence2.sleep(1);
                int v = this.hard.SafeReadPort(1);
                Integer partIncoming = ThreadIdentify.peekPart();
                
                if(clicks.station2)
                    nextDesiredPart = clicks.part2;
               
                if(partIncoming != null && partIncoming == nextDesiredPart && this.hard.getBitValue(v, 7)) { //Seq2 wants the incoming part
                    if(ThreadIdentify.TryConsumeMessage() != null){
                        //if we get here, then seq1 can take the incoming part
                        mechanism.conveyorStop();
                        mechanism.cylinder_3_Goto(1);
                        mechanism.cylinder_3_Goto(0);
                        mechanism.conveyorMove();
                        if(!clicks.station2)
                            i++;
                        if(i == 6){
                            i = 0;
                            this.completeSequences++;
                            long startTime = System.currentTimeMillis();
                            while((System.currentTimeMillis() - startTime) < 2000){
                                Hardware.enterCriticalArea();
                                int a = hard.SafeReadPort(2);
                                a = hard.setBitValue(a, 7, true); 
                                hard.SafeWritePort(2, a); 
                                Thread.sleep(80);
                                a = hard.setBitValue(a, 7, false);
                                hard.SafeWritePort(2, a); 
                                Thread.sleep(80);
                                Hardware.leaveCriticalArea();
                                Thread.yield();
                            }
                        }
                        
                        nextDesiredPart = sequenceArray[i];
                        clicks.station2 = false;
                        this.partsReceived++;
                        ThreadIdentify.startIdentification();  
                    }              
                }
                
                if(partIncoming != null && partIncoming != nextDesiredPart && this.hard.getBitValue(v, 7)) { //Seq2 wants the incoming part
                     if(ThreadIdentify.TryConsumeMessage() != null){
                            partsRecycle++;
                            long startTime = System.currentTimeMillis();
                            while((System.currentTimeMillis() - startTime) < 2000){
                                Hardware.enterCriticalArea();
                                int b = hard.SafeReadPort(2);
                                b = hard.setBitValue(b, 7, true); 
                                hard.SafeWritePort(2, b); 
                                Thread.sleep(30);
                                b = hard.setBitValue(b, 7, false);
                                hard.SafeWritePort(2, b); 
                                Thread.sleep(30);
                                Hardware.leaveCriticalArea();
                                Thread.yield();
                            }
                            ThreadIdentify.startIdentification();
                    }
                        
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadSequence1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}