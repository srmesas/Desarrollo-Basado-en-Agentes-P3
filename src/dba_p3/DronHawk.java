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
        inicioX=5;
        inicioY=5;
    }
    
    public void execute(){ // lo que hace el agente
        
        
        System.out.println("segundo drone "+ quiensoy + " " + session+ " x " + inicioX + " y " + inicioY);

//        while (session==null) {            
//            try {
//                recibirSession();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(DronHawk.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        enviarMensajeJSON("suscribe");
       
        try {
            respuesta = recibirMensajeJSON();
        } catch (InterruptedException ex) {
            Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        enviarMensajeJSON("checkin");
        try {
            respuesta = recibirMensajeJSON();
        } catch (InterruptedException ex) {
            Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
        }
        enviarSession("pedro");
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
    
    protected String recibirSession() throws InterruptedException {
        ACLMessage inbox;
            inbox = this.receiveACLMessage();
            System.out.println("\nRespuesta del controlador: ");
            String fuente = inbox.getContent();
            JsonObject objetoRespuesta = Json.parse(fuente).asObject();
            if (inbox.getPerformativeInt() == ACLMessage.INFORM){
                System.out.println("entrooooooooooo");
                reply = inbox.getReplyWith();
                System.out.println(reply);
                JsonObject objetoPercepcion = Json.parse(fuente).asObject();
                session = objetoPercepcion.get("session").asString();
            }
            return session;
           
    }
    
}
