/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dba_p3;

import es.upv.dsic.gti_ia.core.AgentID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author srmesas
 */
public class DronHawk extends Dron{

    
    
    
    
    public DronHawk(AgentID aid) throws Exception {
        super(aid);
        setAgente("keid");
        setQuiensoy("hawk");
        setX(50);
        setY(50);
        
        
    }
    
    public void execute(){ // lo que hace el agente
        
        
        System.out.println("segundo drone "+ quiensoy + " " + session);
        try {
            respuesta = recibirMensajeJSON();
        } catch (InterruptedException ex) {
            Logger.getLogger(DronHawk.class.getName()).log(Level.SEVERE, null, ex);
        }
//        enviarMensajeJSON("suscribe");
//       
//        try {
//            respuesta = recibirMensajeJSON();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
//        }
        enviarMensajeJSON("checkin");
        try {
            respuesta = recibirMensajeJSON();
        } catch (InterruptedException ex) {
            Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
        }
        enviarMensajeJSON("query");
        try {
            respuesta = recibirMensajeJSON();
        } catch (InterruptedException ex) {
            Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
        }
        movimiento="moveNW";
        enviarMensajeJSON("moveRefuelStopRescue");
        try {
            respuesta = recibirMensajeJSON();
        } catch (InterruptedException ex) {
            Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
        }
        enviarMensajeJSON("logout");
    }


    
    @Override// opcional
    public void finalize(){ // lo que hace el agente

    }
    
}
