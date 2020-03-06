import java.io.IOException;

public class Hello{

    private static BlockPlay malApp;
 
    public static void main(String args[]) throws IOException, InterruptedException    
    {        
        System.out.println("Welcome!");         
        Hardware h = new Hardware();        
        h.create_di(0); 
        h.create_di(1);                
        h.create_do(2);   
         
        Cylinder cyl1 = new Cylinder1(h); 
        Cylinder cyl2 = new Cylinder2(h); 
        Cylinder cyl3 = new Cylinder3(h); 

        Mechanism mechanism = new Mechanism(h, cyl1, cyl2, cyl3);           // Creation of a mechanism
        ThreadIdentify threadIdentify = new ThreadIdentify(mechanism);      // Creation of the threadIdentify
        ThreadClicks threadClicks = new ThreadClicks(h);                    // Creation of the threadClicks
        
        
        ThreadSequence1 seq1 = new ThreadSequence1(h, mechanism, threadClicks);
        ThreadSequence2 seq2 = new ThreadSequence2(h, mechanism, threadClicks);
      
        malApp  = new BlockPlay(h, mechanism, seq1, seq2);             // Creation of the app
        malApp.setVisible(true);                                            // Shows the app
        
        // Calibration of cylinder A
     
        int pos1 = mechanism.cylinder_1_getPosition();
        if (pos1 != 0 && pos1 != 1){
            while (pos1 != 0 && pos1 != 1){
                mechanism.cylinder_1_moveForward();
                pos1 = mechanism.cylinder_1_getPosition();
            }
        }
         
        mechanism.cylinder_1_stop();
        
        // Calibration of cylinder B
         
        int pos2 = mechanism.cylinder_2_getPosition();
        if (pos2 != 0 && pos2 != 1){
            while (pos2 != 0 && pos2 != 1){
                mechanism.cylinder_2_moveForward();
                pos2 = mechanism.cylinder_2_getPosition();
            }
        }
        
        mechanism.cylinder_2_stop();   
    
        // Calibration of cylinder C
        
        int pos3 = mechanism.cylinder_3_getPosition();
        if (pos3 != 0 && pos3 != 1){
            while (pos3 != 0 && pos3 != 1){
                mechanism.cylinder_3_moveForward();
                pos3 = mechanism.cylinder_3_getPosition();
            }
        }
         
        mechanism.cylinder_3_stop();
        
        ///////////////////////////////////////////////////////////////////////
 
        int aux = 0;
        while(aux != 'e')
        {
            ThreadIdentify.startIdentification();
            threadClicks.start();
            threadIdentify.start();  
            seq1.start();
            seq2.start();
            
            
            
            
            //Integer partType = ThreadIdentify.consumeMessage();
            //System.out.printf("\n part is %d\n", partType);
         
            try{   
                aux = System.in.read(); 
            } catch(IOException e){ } 
        }
    }  

}  