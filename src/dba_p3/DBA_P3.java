/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dba_p3;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 *
 * @author jopoku
 */
public class DBA_P3 {
    public static final String NOMBRE_HAWK = "h01";
    public static final String NOMBRE_FLY1 = "fl01";
    public static final String NOMBRE_FLY2 = "f01";
    public static final String NOMBRE_RESCUE = "r1";
    
     public static void connect(){
        AgentsConnection.connect(
            "isg2.ugr.es",              //hostr, "localhost"
            6000,
            "Practica3",
            "Kazi",			//user
            "moHhEBMN",			//password
            false			//SSL
	);
    }
     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        
         connect();
        
        Dron D;
        DronHawk H;
        DronFly F1,F2;
        DronRescate R;
        try{
            D = new Dron(new AgentID("juan"));
            H = new DronHawk(new AgentID(NOMBRE_HAWK));
            F1 = new DronFly(new AgentID(NOMBRE_FLY1), 30, 30);
            F2 = new DronFly(new AgentID(NOMBRE_FLY2), 60, 60);
            R = new DronRescate(new AgentID(NOMBRE_RESCUE));
	} catch (Exception ex){
		System.err.println("El agente ya existe en la plataforma");
		return;
	}

	D.start();
        //Thread.sleep(1000);
        H.start();
        F1.start();
        F2.start();
        R.start();
    }
    
}
