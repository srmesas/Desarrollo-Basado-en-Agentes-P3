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

/**
 *
 * @author jopoku
 */
class Dron extends SuperAgent{

    private final String mapa;
    private final String user;
    private final String password;
    
    private ACLMessage outbox;
    private String respuesta;

    public Dron(AgentID aid) throws Exception {
        super(aid);
        this.mapa = "map8";
        this.user = "Kazi";
        this.password = "moHhEBMN";
    }

    @Override // opcional
    public void init(){ // lo que hace el agente

    }
    
    @Override //obligatorio
    public void execute(){ // lo que hace el agente
        enviarMensajeJSON("login");
        
        respuesta = recibirMensajeJSON();
    }
    
    @Override// opcional
    public void finalize(){ // lo que hace el agente

    }

    private void enviarMensajeJSON(String comando) {
        switch(comando){
            case "suscribe": 
                System.out.println("Suscribe \n");
                JsonObject objeto = new JsonObject();
                objeto.add("map",this.mapa);
                objeto.add("user", this.user);
                objeto.add("password", this.password);

                String resultado = objeto.toString();
                System.out.println("\nMensaje JSON enviado: \n <"+resultado+"> \n");
                ACLMessage outbox = new ACLMessage(); 
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID("Keid"));
                outbox.setContent(resultado);
                outbox.setPerformative(ACLMessage.SUBSCRIBE);
                this.send(outbox);   
            break; 
            
            case "login":

            break;
            
            case "move":

            break;
            
            case "logout":

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
            String respuesta = objetoRespuesta.get("in-reply-to").asString();
            String resultado = objetoRespuesta.get("result").asString();
        }catch (InterruptedException ex) {
            System.out.println(this.getName()+"*** ERROR en la recepción del mensaje\n");
            return "ERROR";
        }  
        return "ok";
         
    }
    
}
