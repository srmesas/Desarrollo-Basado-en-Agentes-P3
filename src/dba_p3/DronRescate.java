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
public class DronRescate extends Dron{
    
    
    private float gonioDistance = 5;
    private float gonioAngle = 5;
    int [][] radar =  new int[11][11];
    private String commandmov;
    private int [][] elevation =  new int[11][11];
    
    public DronRescate(AgentID aid) throws Exception {
        super(aid);
        setQuiensoy("rescue");
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
        while(movimiento==null){
            enviarMensajeJSON("query");//SERVIDOR
            try {
                respuesta = recibirMensajeJSON();
            } catch (InterruptedException ex) {
                Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("\n\tGPS: x:" + this.x + " y:" + this.y + " z:" + this.z);
            enviarMensajeJSONControlador("moveRefuelStopRescue");//HACIA DIRECTOR
            System.out.println("antes del while " +movimiento);
       
            try {
                movimiento = recibirMovimiento();
            } catch (InterruptedException ex) {
                Logger.getLogger(DronHawk.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(movimiento);
            enviarMensajeJSON("moveRefuelStopRescue");
            try {
                respuesta = recibirMensajeJSON();
            } catch (InterruptedException ex) {
                Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
            }
            movimiento=null;
            
            //if(contador == 20){
            //    System.out.println("me salgo");
            //    break;
            //}
            //contador++;
        }
    }
    
    
}