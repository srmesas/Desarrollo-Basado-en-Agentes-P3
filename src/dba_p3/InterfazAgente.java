/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dba_p3;

import DBA.SuperAgent;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author srmesas
 */
public class InterfazAgente extends SuperAgent{

    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_RED_BACKGROUND_2 = "\u001b[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    
    protected String mapa;
    protected String user;
    protected String password;
    protected String comandoEnvi;
    public String id;
    public String session;
    
    
    protected ACLMessage outbox;
    protected String respuesta;
    protected String key;
    
    public InterfazAgente(AgentID aid) throws Exception {
        super(aid);
    }
    
    @Override // opcional
    public void init(){ // lo que hace el agente

    }
    
    @Override //obligatorio
    public void execute(){ // lo que hace el agente
        enviarMensajeJSON("suscribe");
       
        try {
            respuesta = recibirMensajeJSON();
        } catch (InterruptedException ex) {
            Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
        }
        enviarMensajeJSON("request");
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

    protected void enviarMensajeJSON(String comando) {
        comandoEnvi = comando;
        JsonObject objeto;
        String resultado = null;
        ACLMessage outbox = new ACLMessage();
        switch(comando){
            case "suscribe": 
                System.out.println("Suscribe \n");
                objeto = new JsonObject();
                objeto.add("map",this.mapa);
                objeto.add("user", this.user);
                objeto.add("password", this.password);

                resultado = objeto.toString();
                System.out.println("\nMensaje JSON enviado: \n <"+resultado+"> \n");
                outbox.setSender(this.getAid());
                outbox.setConversationId(id);
                outbox.setReceiver(new AgentID("Keid"));
                outbox.setContent(resultado);
                outbox.setPerformative(ACLMessage.SUBSCRIBE);
                this.send(outbox);   
            break; 
            
            case "request":
                System.out.println("request \n");
                objeto = new JsonObject();
                objeto.add("command","checkin");
                objeto.add("session", session);
                objeto.add("rol", "rescue");
                objeto.add("x", 0);
                objeto.add("y", 0);
                
                resultado = objeto.toString();
                System.out.println("\nMensaje JSON enviado: \n <"+resultado+"> \n"); 
                outbox.setSender(this.getAid());
                outbox.setConversationId(id);
                outbox.setReceiver(new AgentID("Keid"));
                outbox.setContent(resultado);
                outbox.setPerformative(ACLMessage.REQUEST);
                this.send(outbox);   
                
            break;
            
            case "move":

            break;
            
            case "logout":
                outbox = new ACLMessage(); 
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID("Keid"));
                outbox.setPerformative(ACLMessage.CANCEL);
                this.send(outbox);
            break;
        }
    }

    protected String recibirMensajeJSON() throws InterruptedException {
        ACLMessage inbox;
        try {
            inbox = this.receiveACLMessage();
            System.out.println("\nRespuesta del controlador: ");
            
      
            String fuente = inbox.getContent();
            JsonObject objetoRespuesta = Json.parse(fuente).asObject();
            String resultado = objetoRespuesta.get("result").asString();
            System.out.println(inbox.getPerformativeInt());
            if (inbox.getPerformativeInt() == ACLMessage.REFUSE)  {
              String fallo = objetoRespuesta.get("details").asString();
                System.out.println(fallo);
            }else if (inbox.getPerformativeInt() == ACLMessage.NOT_UNDERSTOOD){
                String fallo = objetoRespuesta.get("details").asString();
                System.out.println(fallo);
            }else if (inbox.getPerformativeInt() == ACLMessage.FAILURE){
                String fallo = objetoRespuesta.get("details").asString();
                System.out.println(fallo);
            }//else if (inbox.getPerformativeInt() == ACLMessage.INFORM){
                //id = inbox.getConversationId();
                //percibirJSON(inbox);
                //System.out.println(" id : "+ id + "  session " + session);
                
            //}
            
        }catch (InterruptedException ex) {
            System.out.println(this.getName()+"*** ERROR en la recepción del mensaje\n");
            return "ERROR";
        }  
        return "ok";        
    }
}
