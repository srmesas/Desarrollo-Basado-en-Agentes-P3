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
 * @author Manuel Jopoku 
 */
public class DronRescate extends Dron{
    
    
    private float gonioDistance = 5;
    private float gonioAngle = 5;
    int [][] radar =  new int[11][11];
    private String commandmov;
    private int [][] elevation =  new int[11][11];
    private int contador=1;
    private Dron Padre;
    public DronRescate(AgentID aid, Dron D) throws Exception {
        super(aid);
        setQuiensoy("rescue");
        Padre = D;
    }
    
    public void execute(){ // lo que hace el agente
        
        
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
//            if(alturaMax > z){
//                movimiento = "moveUP";
//                enviarMensajeJSON("moveRefuelStopRescue");
//                System.out.println("subo subo subiendo");
//                 try {
//                    respuesta = recibirMensajeJSON();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }else{
                 System.out.println("estoy encima de un aleman " + estoyEncimaAleman());
                if(estoyEncimaAleman()){
                System.out.println("valor de la altura " + z + " " + mapR.getLevel(x, y));
                
                if(mapR.getLevel(x, y) < z){
                    movimiento = "moveDW";
                    enviarMensajeJSON("moveRefuelStopRescue");
                    System.out.println("bajo bajo bajando rescue");
                    try {
                        respuesta = recibirMensajeJSON();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    movimiento = "rescue";
                    enviarMensajeJSON("moveRefuelStopRescue");
                    System.out.println("rescato rescato rescato");
                    //contador=60;
                     try {
                        respuesta = recibirMensajeJSON();
                        if(respuesta.equals("ok")){
                            Padre.EliminarAleman();
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                }else if(x == Padre.xAleman && y == Padre.yAleman){
                    System.out.println("estoy encima de un aleman que no existe");
                    Padre.EliminarAleman();
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
                
            //} 
            movimiento=null;
            
//            if(contador == 500){
//                System.out.println("me salgo");
//                break;
//            }
//            contador++;
        }
    }
    
    
}