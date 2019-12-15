/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dba_p3;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author srmesas
 */
public class DronFly extends Dron{
    
    
    private float gonioDistance = 5;
    private float gonioAngle = 5;
    int [][] radar =  new int[11][11];
    private String commandmov;
    private int [][] elevation =  new int[11][11];
    
    public DronFly(AgentID aid, int inicioX, int inicioY) throws Exception {
        super(aid);
        setQuiensoy("fly");
        this.inicioX = inicioX;
        this.inicioY = inicioY;
        this.dimX = super.dimX;
        this.dimY = super.dimY;
    }
    
    public void execute(){ // lo que hace el agente
        
        
        System.out.println("\nDrone "+ quiensoy + " " + session+ " x " + inicioX + " y " + inicioY);

        while (session==null) {            
            try {
                recibirSession();
            } catch (InterruptedException ex) {
                Logger.getLogger(DronHawk.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        enviarMensajeJSON("checkin");
        try {
            respuesta = recibirMensajeJSON();
        } catch (InterruptedException ex) {
            Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
        }
//        do{
//            enviarMensajeJSON("query");
//            try {
//                respuesta = recibirMensajeJSON();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            siguienteMovimiento();
//            enviarMensajeJSON("moveRefuelStopRescue");
//            try {
//                respuesta = recibirMensajeJSON();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }while(this.torescue > 0);
//        enviarMensajeJSON("logout");
    }
        
   
    
}