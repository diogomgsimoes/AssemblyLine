
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
public class ThreadClicks extends Thread {

    Hardware hard;
    int nClicks1 = 0; 
    int nClicks2 = 0;
    public int part1 = 0;
    public int part2 = 0;
    boolean station1 = false;
    boolean station2 = false;
        
    public ThreadClicks(Hardware hard) 
    { 
        this.hard = hard;  
    } 
    
    @Override
    public void run()    
    {
        int portActivate = hard.SafeReadPort(1);
        long startTime = System.currentTimeMillis();
        
        while(true){
            try{
                if(hard.getBitValue(portActivate, 4) || hard.getBitValue(portActivate, 3)){
                    station1 = false;
                    station2 = false;
                    startTime = System.currentTimeMillis();
                    while((System.currentTimeMillis() - startTime) < 10000)
                    {
                        if (hard.getBitValue(portActivate, 4)){
                            nClicks1++;
                            station1 = true;
                        }

                        if (hard.getBitValue(portActivate, 3)){
                            nClicks2++;
                            station2 = true;
                        }

                        Thread.sleep(1500);
                        portActivate = hard.SafeReadPort(1);
                    }

                    if(station1)
                        part1 = nClicks1 - 1;
                    
                    if(station2)
                        part2 = nClicks2 - 1;
                   
                    nClicks1 = 0;
                    nClicks2 = 0;
                }
                
                Thread.sleep(500);
                System.out.printf("Station 1 is now waiting for part: " + part1 + "\n");
                System.out.printf("Station 2 is now waiting for part: " + part2 + "\n");
                System.out.println("-----------------------------------------");
                Thread.sleep(500);
                
                portActivate = hard.SafeReadPort(1);
            }
            
            catch (InterruptedException ex) {
                Logger.getLogger(ThreadClicks.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }  
    }
}
    
   
    
    
    