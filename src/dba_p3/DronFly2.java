/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dba_p3;

import static dba_p3.Dron.ANSI_YELLOW_BACKGROUND;
import es.upv.dsic.gti_ia.core.AgentID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jopoku
 */
public class DronFly2 extends DronFly{
    
    int contador=0;
    private String commandmov;
    
    public DronFly2(AgentID aid) throws Exception {
        super(aid);
        setQuiensoy("fly");
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
            if(alturaMax > z){
                movimiento = "moveUP";
                enviarMensajeJSON("moveRefuelStopRescue");
                System.out.println("subo subo subiendo");
                 try {
                    respuesta = recibirMensajeJSON();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
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
            } 
            movimiento=null;
            
            if(contador == 48){
                System.out.println("me salgo");
                break;
            }
            contador++;
            System.out.println(ANSI_YELLOW_BACKGROUND+" el contador es : "+contador+ANSI_RESET);
        }
    }
        
   
    
    
}
