
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

public class ThreadSequence1 extends Thread { 
    
    private Hardware hard;
    private final Mechanism mechanism;  
    private final int[] sequenceArray;
    public int partsReceived;
    public int completeSequences;
    private ThreadClicks clicks;
     
    public ThreadSequence1(Hardware hard, Mechanism mechanism, ThreadClicks clicks) 
    {
        this.hard = hard;
        this.mechanism = mechanism;    
        this.sequenceArray = new int[]{ 0,0,1,1,2,2 }; //Sequence
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
                ThreadSequence1.sleep(1);
                Integer partIncoming = ThreadIdentify.peekPart();
                
                if(clicks.station1)
                    nextDesiredPart = clicks.part1;
                
                if(partIncoming != null && partIncoming == nextDesiredPart) { //Seq1 wants the incoming part
                    if(ThreadIdentify.TryConsumeMessage() != null){
                        //if we get here, then seq1 can take the incoming part
                        mechanism.conveyorStop();
                        mechanism.cylinder_2_Goto(1);
                        mechanism.cylinder_2_Goto(0);
                        mechanism.conveyorMove();
                        if(!clicks.station1)
                            i++;
                        if(i == 6){
                            i = 0;
                            this.completeSequences++;
                            long startTime = System.currentTimeMillis();
                            while((System.currentTimeMillis() - startTime)<1000){
                                hard.enterCriticalArea();
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
                        clicks.station1 = false;
                        this.partsReceived++;
                        ThreadIdentify.startIdentification();    
                    }   
                }
                
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadSequence1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}