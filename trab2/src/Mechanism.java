/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author diogo
 */

public class Mechanism {
    
    private Cylinder cyl1, cyl2, cyl3; 
    private int pos1, pos2, pos3;
    
    public Mechanism(Hardware hard, Cylinder c1, Cylinder c2, Cylinder c3) 
    { 
        this.hard = hard; 
        this.cyl1 = c1; 
        this.cyl2 = c2; 
        this.cyl3 = c3; 
    } 
    
    Hardware hard; 
    public Mechanism(Hardware h)
    { 
        hard = h; 
    }
        
    public void conveyorMove()
    { 
        Hardware.enterCriticalArea();
        int x = hard.SafeReadPort(2); 
        x = hard.setBitValue(x, 2 , true); 
        hard.SafeWritePort(2, x);
        Hardware.leaveCriticalArea();
    }
    
    public void conveyorStop()
    {
        Hardware.enterCriticalArea();
        int x = hard.SafeReadPort(2); 
        x = hard.setBitValue(x, 2, false);
        hard.SafeWritePort(2, x);
        Hardware.leaveCriticalArea();
    }
    
    public void getPart()
    {
        this.cyl1.Goto(1);
        this.cyl1.Goto(0);
    }
    
    public void cylinder_1_moveBackward() 
    {
        this.cyl1.moveBackward();
    } 
    
    public void cylinder_1_moveForward() 
    {
         this.cyl1.moveForward();
    } 
    
    public void cylinder_1_stop() 
    {
        this.cyl1.stop();
    }
    
    public int cylinder_1_getPosition() 
    {
        pos1 = this.cyl1.getPosition();
        return pos1;
    } 
    
    public void cylinder_1_Goto(int pos1) 
    { 
        this.cyl1.Goto(pos1);
    } 
     
    public boolean cylinder_1_isAtPosition(int pos1) 
    {
        return pos1 == cylinder_1_getPosition();
    }
   
    public void cylinder_2_moveBackward() 
    {
        this.cyl2.moveBackward();
    } 
    
    public void cylinder_2_moveForward() 
    {
         this.cyl2.moveForward();
    } 
    
    public void cylinder_2_stop() 
    {
        this.cyl2.stop();
    }
    
    public int cylinder_2_getPosition() 
    {
        pos2 = this.cyl2.getPosition();
        return pos2;
    } 
    
    public void cylinder_2_Goto(int pos2) 
    { 
        this.cyl2.Goto(pos2);
    } 
     
    public boolean cylinder_2_isAtPosition(int pos2) 
    {
        return pos2 == cylinder_2_getPosition();
    }
    public void cylinder_3_moveBackward() 
    {
        this.cyl3.moveBackward();
    } 
    
    public void cylinder_3_moveForward() 
    {
         this.cyl3.moveForward();
    } 
    
    public void cylinder_3_stop() 
    {
        this.cyl3.stop();
    }
    
    public int cylinder_3_getPosition() 
    {
        pos3 = this.cyl3.getPosition();
        return pos3;
    } 
    
    public void cylinder_3_Goto(int pos3) 
    { 
        this.cyl3.Goto(pos3);
    } 
     
    public boolean cylinder_3_isAtPosition(int pos3) 
    {
        return pos3 == cylinder_3_getPosition();
    }
    
    public int identify()
    {
        int P0 = hard.SafeReadPort(0);
        int P1 = hard.SafeReadPort(1);
        int part = 0;
        boolean s5 = false;
        boolean s6 = false;
        
         while(hard.getBitValue(P0,0))               
        {    
            P0 = hard.SafeReadPort(0);
            P1 = hard.SafeReadPort(1);
            Thread.yield();
        }
         
        while(!hard.getBitValue(P0,0))               
        {    
            if(hard.getBitValue(P1,5))
                s5 = true;
            
            if(hard.getBitValue(P1,6))
                s6 = true;
                        
            if(s5 || s6)
                part = 1;
             
            if(s6 && s5)
                part = 2;

            if(!s5 && !s6)
                part = 0;

            P0 = hard.SafeReadPort(0);
            P1 = hard.SafeReadPort(1);
            Thread.yield();
        }
         
        return part;
    }
}