/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dba_p3;

/**
 *
 * @author jopoku
 */
public class Aleman {

    private final int x;
    private final int y;

    public Aleman(int x,int y) {
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Aleman{" + "x=" + x + ", y=" + y + '}';
    }
    
    
    
    
    
    
}
