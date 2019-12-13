/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dba_p3;

import DBAMap.DBAMap;
import DBA.SuperAgent;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jopoku
 */
class Dron extends SuperAgent {
    
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
    
    static String _filename="map500x500";
    
    protected ACLMessage outbox;
    protected String respuesta;
    protected String key;
    
//    protected int alturaMaxima;

    protected int dimX;
    protected int dimY;
//    protected int alturaMin;
    protected int alturaMax;
    protected float visibilidad;
    protected float rango;
    protected String quiensoy="rescue";
    protected int x;
    protected int y;
    protected int z;
//    
    protected float gasto;
//    
    protected int mapaMemoria[][];
//    List<String> coordenadas = new ArrayList<>();
    protected String reply;
    protected float distance;
    protected float angle;
    protected int fuel;
    protected boolean goal;
    protected String status;
    protected int torescue;
    protected int energy;
    protected boolean cancel;
    protected int prueba[] = new int [9999];
    protected int[][] infrared;
    protected String movimiento;


    public Dron(AgentID aid) throws Exception {
        super(aid);
        this.mapa = "map5";
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
        movimiento="moveX";
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
            
            case "checkin":
                System.out.println("request \n");
                objeto = new JsonObject();
                objeto.add("command","checkin");
                objeto.add("session", session);
                objeto.add("rol", quiensoy);
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
            
            case "moveRefuelStopRescue":
                objeto = new JsonObject();
                objeto.add("command",movimiento);
                resultado = objeto.toString();
                outbox.setContent(resultado);
                outbox = new ACLMessage(); 
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID("Keid"));
                outbox.setConversationId(id);
                outbox.setInReplyTo(reply);
                outbox.setPerformative(ACLMessage.REQUEST);
                this.send(outbox);
            break;
            
            case "refuel":

            break;
            
            case "query":
                outbox = new ACLMessage(); 
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID("Keid"));
                outbox.setConversationId(id);
                outbox.setInReplyTo(reply);
                outbox.setPerformative(ACLMessage.QUERY_REF);
                this.send(outbox);
            break;
            
            case "logout":
                outbox = new ACLMessage(); 
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID("Keid"));
                outbox.setConversationId(id);
                outbox.setPerformative(ACLMessage.CANCEL);
                this.send(outbox);
            break;
        }
    }
    
    protected void percibirJSON(ACLMessage inbox){

            System.out.println("\nPercepciones: ");
            String fuente = inbox.getContent();
            reply = inbox.getReplyWith();
            System.out.println(reply);
            JsonObject objetoPercepcion = Json.parse(fuente).asObject();
           
            if(comandoEnvi == "suscribe"){
                try {
                    session = objetoPercepcion.get("session").asString();
                    this.dimX =objetoPercepcion.get("dimx").asInt();
                    this.dimY =objetoPercepcion.get("dimy").asInt();
                    System.out.println("x " + dimX +"  y:"+ dimY + " ");
                    
                    JsonArray img = Json.parse(fuente).asObject().get("map").asArray();
                    //System.out.println(img);
                    DBAMap map = new DBAMap();
                    map.fromJson(img);
                    System.out.println("IMAGE DATA:");
                    /// 3) Cuyas dimensiones se pueden consultar
                    System.out.println(map.getWidth()+" pixs width & "+map.getHeight()+" pixs height");
                    /// 4) Y cuyos valores se pueden consultar en getLevel(X,Y)
                    System.out.print("First row starts with: ");
                    for (int i=0; i<10; i++)
                        System.out.print(map.getLevel(i, 0)+"-");
                    System.out.print("\nLast row ends with: ");
                    for (int i=0; i<10; i++)
                        System.out.print(map.getLevel(map.getWidth()-1-i, map.getHeight()-1)+"-");
                    System.out.println();
                    System.out.println("Saving file ./maps/"+_filename+".png");
                    map.save("./maps/"+_filename+".png");
                    
                } catch (IOException ex) {
                     System.err.println("***ERROR "+ex.toString());
                }
                
            }else if(comandoEnvi == "checkin"){
                System.out.println(objetoPercepcion.get("result").asString());
                gasto = objetoPercepcion.get("fuelrate").asFloat();
                rango = objetoPercepcion.get("range").asFloat();
                alturaMax = objetoPercepcion.get("maxlevel").asInt();
                visibilidad = objetoPercepcion.get("visibility").asInt();
                System.out.println("gasto "+  gasto + " rango "+ rango +  " altura max " + alturaMax + " visibilidad "+ visibilidad);
            }else if(comandoEnvi == "query"){
                System.out.println("query");
                String percepcion = objetoPercepcion.toString();
                System.out.println("\nMensaje JSON recibido: \n <"+percepcion+"> \n"); 
                this.x = objetoPercepcion.get("result").asObject().get("gps").asObject().get("y").asInt();
                this.y =objetoPercepcion.get("result").asObject().get("gps").asObject().get("y").asInt();
                this.z =objetoPercepcion.get("result").asObject().get("gps").asObject().get("z").asInt();
                
                System.out.println("\n\tGPS: x:" + this.x + " y:" + this.y + " z:" + this.z);
                
                distance = objetoPercepcion.get("result").asObject().get("gonio").asObject().get("distance").asFloat();
                angle = objetoPercepcion.get("result").asObject().get("gonio").asObject().get("angle").asFloat();
                
                JsonArray radarJSON = objetoPercepcion.get("result").asObject().get("infrared").asArray();
                
                fuel = objetoPercepcion.get("result").asObject().get("fuel").asInt();
                
                goal = objetoPercepcion.get("result").asObject().get("goal").asBoolean();
                
                status = objetoPercepcion.get("result").asObject().get("status").asString();
                torescue = objetoPercepcion.get("result").asObject().get("torescue").asInt();
                energy = objetoPercepcion.get("result").asObject().get("torescue").asInt();
                cancel = objetoPercepcion.get("result").asObject().get("cancel").asBoolean();
                
                int rangoconver = (int) rango;
              
                for (int i = 0; i < radarJSON.size(); i++) {
                    prueba[i] = radarJSON.get(i).asInt();
                }
                int indice=0;
                infrared = new int[rangoconver][rangoconver];
                
                for (int i = 0; i < infrared.length; i++) {
                   for (int j = 0; j < infrared[i].length; j++) {
                    infrared[i][j] = prueba[indice];
                    indice++;
                    }
                }
                
                for (int i = 0; i < infrared.length; i++) {
                System.out.print("\t\t\t\t");
                for (int j = 0; j < infrared[i].length; j++) {
                    if(i== rangoconver/2 && j== rangoconver/2){
                        System.out.print(ANSI_GREEN_BACKGROUND +infrared[i][j] + ANSI_RESET + "  ");
                    }else{
                        if(infrared[i][j]==1){
                            System.out.print(ANSI_YELLOW_BACKGROUND +infrared[i][j] + ANSI_RESET + "  ");
                        }else{
                            System.out.print(infrared[i][j] + "  ");
                        }
                    }
                }
                    System.out.println(" ");
                }
                System.out.println("distancia " + distance + " angulo "+angle);
            }
            
            
    }
    
    protected String recibirMensajeJSON() throws InterruptedException {
        ACLMessage inbox;
        try {
            inbox = this.receiveACLMessage();
            System.out.println("\nRespuesta del controlador: ");
            String fuente = inbox.getContent();
            JsonObject objetoRespuesta = Json.parse(fuente).asObject();
           // System.out.println(inbox.getPerformativeInt());
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
                //System.out.println(" id : "+ id + "  session " + session);
            }
            
        }catch (InterruptedException ex) {
            System.out.println(this.getName()+"*** ERROR en la recepción del mensaje\n");
            return "ERROR";
        }  
        return "ok";        
    }

    /**
    *
    * @author Manuel
    */
    protected void crearMemoria(){
        this.mapaMemoria = new int[this.dimY][this.dimX];
        for(int i=0; i<this.dimY; i++)
            for(int j=0; j<this.dimX; j++)
                this.mapaMemoria[i][j]=0; 
    }
    
    /**
    *
    * @author Manuel
    */
    protected void guardarPosicionMemoria(){
        this.mapaMemoria[this.y][this.x]++;
    }
    
    /**
    *
    * @author Manuel
    */
    protected void mostrarMemoria(){
        
        for(int i=0; i<this.dimY; i++){
            for(int j=0; j<this.dimX; j++){
                if(this.mapaMemoria[i][j]!=0){
                    //System.out.print(ANSI_GREEN_BACKGROUND +this.mapaMemoria[i][j]+ ANSI_RESET + "  ");
                }else{
                    System.out.print(this.mapaMemoria[i][j]+" ");
                }
            }
            System.out.print("\n");
        }
    }
    
}