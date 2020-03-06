/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author diogo
 */
public interface Cylinder 
{
        public void moveForward();
        public void moveBackward();
        public void stop();
        public void Goto(int position); // 0, 1
        public int getPosition();
}

