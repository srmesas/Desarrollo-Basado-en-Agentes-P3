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
        inicioX=25;
        inicioY=35;
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
        do{
            enviarMensajeJSON("query");
            try {
                respuesta = recibirMensajeJSON();
            } catch (InterruptedException ex) {
                Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
            }
            siguienteMovimiento();
            enviarMensajeJSON("moveRefuelStopRescue");
            try {
                respuesta = recibirMensajeJSON();
            } catch (InterruptedException ex) {
                Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
            }
        }while(this.torescue > 0);
        enviarMensajeJSON("logout");
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
                this.dimX = super.dimX;
                this.dimY = super.dimY;
            }
            return session;
           
    }
    
    
    public void siguienteMovimiento(){
        
        //Ha encontrado un alemán
        if (this.gonioDistance > 1){ 
            
            if (((this.gonioAngle>=0 && this.gonioAngle<=22.5) || (this.gonioAngle>337.5 && this.gonioAngle <=360))){
                if (radar[4][5]>z && esBueno("moveUP")){
                    commandmov = "moveUP";
                }else if(radar[4][5]>0 && esAceptable("moveN") && esBueno("moveN")){
                    commandmov = "moveN";
                }else{
                    if(esAceptable("moveNW") && esBueno("moveNW")){
                        commandmov = "moveNW";
                    }else if(esAceptable("moveW") && esBueno("moveW")){
                        commandmov = "moveW";
                    }else if(esAceptable("moveSW") && esBueno("moveSW")){
                        commandmov = "moveSW";
                    }else if(esAceptable("moveS") && esBueno("moveS")){
                        commandmov = "moveS";
                    }else if(esAceptable("moveSE") && esBueno("moveSE")){
                        commandmov = "moveSE";
                    }else if(esAceptable("moveE") && esBueno("moveE")){
                        commandmov = "moveE";
                    }else if(esAceptable("moveNE") && esBueno("moveNE")){
                        commandmov = "moveNE";
                    }
                }
            }
            if (this.gonioAngle>22.5 && this.gonioAngle<=67.5 ){
                if (radar[4][6]>z && esBueno("moveUP")){
                    commandmov = "moveUP";
                }else if (radar[4][6]>0 && esAceptable("moveNE") && esBueno("moveNE")){
                    commandmov = "moveNE";
                }else{
                    if(esAceptable("moveN") && esBueno("moveN")){
                        commandmov = "moveN";
                    }else if(esAceptable("moveNW") && esBueno("moveNW")){
                        commandmov = "moveNW";
                    }else if(esAceptable("moveW") && esBueno("moveW")){
                        commandmov = "moveW";
                    }else if(esAceptable("moveSW") && esBueno("moveSW")){
                        commandmov = "moveSW";
                    }else if(esAceptable("moveS") && esBueno("moveS")){
                        commandmov = "moveS";
                    }else if(esAceptable("moveSE") && esBueno("moveSE")){
                        commandmov = "moveSE";
                    }else if(esAceptable("moveE") && esBueno("moveE")){
                        commandmov = "moveE";
                    } 
                }
            }
            if ((this.gonioAngle>67.5 && this.gonioAngle<=112.5)){
                if (radar[5][6]>z && esBueno("moveUP")){
                    commandmov = "moveUP";
                }else if(radar[5][6]>0 && esAceptable("moveE") && esBueno("moveE")){
                    commandmov = "moveE";
                }else{
                    if(esAceptable("moveNE") && esBueno("moveNE")){
                        commandmov = "moveNE";
                    }else if(esAceptable("moveN") && esBueno("moveN")){
                        commandmov = "moveN";
                    }else if(esAceptable("moveNW") && esBueno("moveNW")){
                        commandmov = "moveNW";
                    }else if(esAceptable("moveW") && esBueno("moveW")){
                        commandmov = "moveW";
                    }else if(esAceptable("moveSW") && esBueno("moveSW")){
                        commandmov = "moveSW";
                    }else if(esAceptable("moveS") && esBueno("moveS")){
                        commandmov = "moveS";
                    }else if(esAceptable("moveSE") && esBueno("moveSE")){
                        commandmov = "moveSE";
                    }
                }         
            }
            
            if ((this.gonioAngle>112.5 && this.gonioAngle<=157.5)){
                if (radar[6][6]>z && esBueno("moveUP")){
                    commandmov = "moveUP";
                }else if(radar[6][6]>0 && esAceptable("moveSE") && esBueno("moveSE")){
                    commandmov = "moveSE";
                }else{
                    if(esAceptable("moveE") && esBueno("moveE")){
                        commandmov = "moveE";
                    }else if(esAceptable("moveNE") && esBueno("moveNE")){
                        commandmov = "moveNE";
                    }else if(esAceptable("moveN") && esBueno("moveN")){
                        commandmov = "moveN";
                    }else if(esAceptable("moveNW") && esBueno("moveNW")){
                        commandmov = "moveNW";
                    }else if(esAceptable("moveW") && esBueno("moveW")){
                        commandmov = "moveW";
                    }else if(esAceptable("moveSW") && esBueno("moveSW")){
                        commandmov = "moveSW";
                    }else if(esAceptable("moveS") && esBueno("moveS")){
                        commandmov = "moveS";
                    }
                }  
            }

            if ((this.gonioAngle>157.5 && this.gonioAngle<=205.5)){
                if (radar[6][5]>z && esBueno("moveUP")){
                    commandmov = "moveUP";
                }else if(radar[6][5]>0 && esAceptable("moveS") && esBueno("moveS")){
                    commandmov = "moveS";
                }else{
                    if(esAceptable("moveSE") && esBueno("moveSE")){
                        commandmov = "moveSE";
                    }else if(esAceptable("moveE") && esBueno("moveE")){
                        commandmov = "moveE";
                    }else if(esAceptable("moveNE") && esBueno("moveNE")){
                        commandmov = "moveNE";
                    }else if(esAceptable("moveN") && esBueno("moveN")){
                        commandmov = "moveN";
                    }else if(esAceptable("moveNW") && esBueno("moveNW")){
                        commandmov = "moveNW";
                    }else if(esAceptable("moveW") && esBueno("moveW")){
                        commandmov = "moveW";
                    }else if(esAceptable("moveSW") && esBueno("moveSW")){
                        commandmov = "moveSW";
                    }
                } 
            }
            
            if ((this.gonioAngle>205.5 && this.gonioAngle<=247.5)){
                if (radar[6][4]>z && esBueno("moveUP")){
                    commandmov = "moveUP";
                }else if(radar[6][4]>0 && esAceptable("moveSW") && esBueno("moveSW")){
                    commandmov = "moveSW";
                }else{
                    if(esAceptable("moveS") && esBueno("moveS")){
                        commandmov = "moveS";
                    }else if(esAceptable("moveSE") && esBueno("moveSE")){
                        commandmov = "moveSE";
                    }else if(esAceptable("moveE") && esBueno("moveE")){
                        commandmov = "moveE";
                    }else if(esAceptable("moveNE") && esBueno("moveNE")){
                        commandmov = "moveNE";
                    }else if(esAceptable("moveN") && esBueno("moveN")){
                        commandmov = "moveN";
                    }else if(esAceptable("moveNW") && esBueno("moveNW")){
                        commandmov = "moveNW";
                    }else if(esAceptable("moveW") && esBueno("moveW")){
                        commandmov = "moveW";
                    }
                } 
            }
            
            if ((this.gonioAngle>247.5 && this.gonioAngle<=292.5)){
                if (radar[5][4]>z && esBueno("moveUP")){
                    commandmov = "moveUP";
                }else if(radar[5][4]>0 && esAceptable("moveW") && esBueno("moveW")){
                    commandmov = "moveW";
                }else{
                    if(esAceptable("moveSW") && esBueno("moveSW")){
                        commandmov = "moveSW";
                    }else if(esAceptable("moveS") && esBueno("moveS")){
                        commandmov = "moveS";
                    }else if(esAceptable("moveSE") && esBueno("moveSE")){
                        commandmov = "moveE";
                    }else if(esAceptable("moveE") && esBueno("moveE")){
                        commandmov = "moveE";
                    }else if(esAceptable("moveNE") && esBueno("moveNE")){
                        commandmov = "moveNE";
                    }else if(esAceptable("moveN") && esBueno("moveN")){
                        commandmov = "moveN";
                    }else if(esAceptable("moveNW") && esBueno("moveNW")){
                        commandmov = "moveNW";
                    } 
                } 
            }
            
            if (this.gonioAngle>292.5 && this.gonioAngle<=337.5){
                if (radar[4][4]>z && esBueno("moveUP")){
                    commandmov = "moveUP";
                }else if (radar[4][4]>0 && esAceptable("moveNW") && esBueno("moveNW")){
                    commandmov = "moveNW";
                }else{
                    if(esAceptable("moveW") && esBueno("moveW")){
                        commandmov = "moveW";
                    }else if(esAceptable("moveSW") && esBueno("moveSW")){
                        commandmov = "moveSW";
                    }else if(esAceptable("moveS") && esBueno("moveS")){
                        commandmov = "moveS";
                    }else if(esAceptable("moveSE") && esBueno("moveSE")){
                        commandmov = "moveSE";
                    }else if(esAceptable("moveE") && esBueno("moveE")){
                        commandmov = "moveE";
                    }else if(esAceptable("moveNE") && esBueno("moveNE")){
                        commandmov = "moveNE";
                    }else if(esAceptable("moveN") && esBueno("moveN")){
                        commandmov = "moveN";
                    }
                } 
            }

        }else{
            if(z > elevation[5][5]){
                    commandmov = "moveDW";
                    if(elevation[5][5]==0){
                       //Rescatar
                    }
                    
            }
            
        }
        checkFuel();
        if(!esBueno(commandmov)){
            System.out.println("\nQUE SE CAE");
        }
        if(this.commandmov != "moveUP" && this.commandmov != "moveDW"){
            guardarPosicionMemoria();
        }
        
        System.out.println(commandmov);
        
        
        
    }
    
}