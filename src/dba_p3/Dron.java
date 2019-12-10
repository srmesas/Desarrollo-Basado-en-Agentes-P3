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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jopoku
 */
class Dron extends SuperAgent{

    private String mapa;
    private String user;
    private String password;
    private String comandoEnvi;
    
    private String rol;
    private int gasto;
    private int alturaMax;
    private int visibilidad;
    private int rango;
    
    private ACLMessage outbox;
    private String respuesta;
    public String id;
    public String session;
    private int x;
    private int dimx;
    private int dimy;

    public Dron(AgentID aid) throws Exception {
        super(aid);
        this.mapa = "map1";
        this.user = "Kazi";
        this.password = "moHhEBMN";
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

    private void enviarMensajeJSON(String comando) {
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

    private String recibirMensajeJSON() throws InterruptedException {
        
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
            }else if (inbox.getPerformativeInt() == ACLMessage.INFORM){
                id = inbox.getConversationId();
                percibirJSON(inbox);
                System.out.println(" id : "+ id + "  session " + session);
                
            }
            
        }catch (InterruptedException ex) {
            System.out.println(this.getName()+"*** ERROR en la recepción del mensaje\n");
            return "ERROR";
        }  
        return "ok";
         
    }
    
    protected void percibirJSON(ACLMessage inbox){

            System.out.println("\nPercepciones: ");
            String fuente = inbox.getContent();
            JsonObject objetoPercepcion = Json.parse(fuente).asObject();
            String percepcion = objetoPercepcion.toString();
            System.out.println("\nMensaje JSON recibido: \n <"+percepcion+"> \n"); 
            if(comandoEnvi == "suscribe"){
                session = objetoPercepcion.get("session").asString();
                this.dimx =objetoPercepcion.get("dimx").asInt();
                this.dimy =objetoPercepcion.get("dimy").asInt();
                System.out.println("x " + dimx +"  y:"+ dimy + " ");
            }else if(comandoEnvi == "request"){
                System.out.println(objetoPercepcion.get("result").asInt());
                gasto = objetoPercepcion.get("fuelrate").asInt();
//                rango = objetoPercepcion.get("range").asInt();
//                alturaMax = objetoPercepcion.get("maxlevel").asInt();
//                visibilidad = objetoPercepcion.get("visibility").asInt();
            }
            
    }
    
}
