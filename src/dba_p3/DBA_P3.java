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
    
    
     public static void connect(){
        AgentsConnection.connect(
            "isg2.ugr.es",              //hostr, "localhost"
            6000,
            "Practica2",
            "Kazi",			//user
            "moHhEBMN",			//password
            false			//SSL
	);
    }
     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
         connect();
        
        Dron D;
        try{
		D = new Dron(new AgentID("ert"));
	} catch (Exception ex){
		System.err.println("El agente ya existe en la plataforma");
		return;
	}

	D.start();
    }
    
}
