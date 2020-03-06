/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author diogo
 */
public class Cylinder1 implements Cylinder
{ 
    Hardware hardware;
        public Cylinder1(Hardware __hardware)
        { 
            this.hardware = __hardware;
        } 

    @Override
    public void moveForward() 
    { 
        Hardware.enterCriticalArea();
        int v = hardware.SafeReadPort(2);
        v = hardware.setBitValue(v, 1, true); 
        v = hardware.setBitValue(v, 0, false); 
        hardware.SafeWritePort(2, v); 
        Hardware.leaveCriticalArea();
    }

    @Override 
    public void moveBackward() 
    { 
        Hardware.enterCriticalArea();   
        int v = hardware.SafeReadPort(2);
        v = hardware.setBitValue(v, 0, true); 
        v = hardware.setBitValue(v, 1, false); 
        hardware.SafeWritePort(2, v);
        Hardware.leaveCriticalArea();

    }

    @Override 
    public void stop() 
    { 
        Hardware.enterCriticalArea();   
        int v = hardware.SafeReadPort(2);
        v = hardware.setBitValue(v, 1, false); 
        v = hardware.setBitValue(v, 0, false); 
        hardware.SafeWritePort(2, v);
        Hardware.leaveCriticalArea();

    }
    
    @Override 
    public int getPosition() 
    { 
        Hardware.enterCriticalArea(); 
        int v = hardware.SafeReadPort(0); 
        Hardware.leaveCriticalArea();
        
        if(hardware.getBitValue(v, 6)) {
            return 0; 
        }
        else if(hardware.getBitValue(v, 5)) {
            return 1; 
        }
        return -1; 
    }   
    
    @Override 
    public void Goto(int position)//0, 1 
    {
        if (getPosition() < position) {
            moveForward();
        } else if (getPosition() > position) {
            moveBackward();
        }
        while (getPosition() != position) {
            Thread.yield();
        }
        stop();
    }
}