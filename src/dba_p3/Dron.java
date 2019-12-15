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
import java.util.Random;
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
    
    public String NOMBRE_HAWK;
    public String NOMBRE_FLY1;
    public String NOMBRE_FLY2;
    public String NOMBRE_RESCUE;
    
    protected String agente="Keid";
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
    protected int inicioX=50;
    protected int inicioY=50;
    private float gonioDistance;
    private float gonioAngle;
    int [][] radar =  new int[11][11];
    private String commandmov;
    private int [][] elevation =  new int[11][11];
    private AgentID emisor;
    private int x_rec;
    private int y_rec;
    private int z_rec;
    private float angule_rec;
    private float distance_rec;


    public Dron(AgentID aid) throws Exception {
        super(aid);
        this.mapa = "playground";
        this.user = "Kazi";
        this.password = "moHhEBMN";
        
        this.NOMBRE_HAWK = DBA_P3.NOMBRE_HAWK;
        this.NOMBRE_FLY1 = DBA_P3.NOMBRE_FLY1;
        this.NOMBRE_FLY2 = DBA_P3.NOMBRE_FLY2;
        this.NOMBRE_RESCUE = DBA_P3.NOMBRE_RESCUE;
    }
    
    @Override // opcional
    public void init(){ // lo que hace el agente

    }
    
    @Override //obligatorio
    public void execute(){ // lo que hace el agente
        System.out.println("primer drone "+quiensoy);
        enviarMensajeJSON("suscribe");
       
        try {
            respuesta = recibirMensajeJSON();
        } catch (InterruptedException ex) {
            Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        enviarMensajeJSON("checkin");
//        try {
//            respuesta = recibirMensajeJSON();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
//        }
        System.out.print("\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
        enviarSession(this.NOMBRE_HAWK);
        enviarSession(this.NOMBRE_FLY1);
        enviarSession(this.NOMBRE_FLY2);
        enviarSession(this.NOMBRE_RESCUE);
        System.out.print("\nbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n");
        do{
//            enviarMensajeJSON("query");
//            try {
//                respuesta = recibirMensajeJSON();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
//            }
            System.out.print("\ncccccccccccccccccccccccccccccccccccccccccccccccc\n");
            try {
                recibirPosiciones();
            } catch (InterruptedException ex) {
                Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
            }
        }while(this.torescue > 0);
        System.out.print("//////////////////////////////////////////////////////////////////\n");
        System.out.print(this.torescue);
        System.out.print("\n//////////////////////////////////////////////////////////////////\n");
        enviarMensajeJSON("logout");
    }
    
    @Override// opcional
    public void finalize(){ // lo que hace el agente

    }
    
    protected void enviarMensajeJSON(String comando) {
        comandoEnvi = comando;
        JsonObject objeto;
        String resultado = null;
        ACLMessage outbox = null;
        switch(comando){
            case "suscribe": 
                System.out.println("Suscribe \n");
                objeto = new JsonObject();
                objeto.add("map",this.mapa);
                objeto.add("user", this.user);
                objeto.add("password", this.password);

                resultado = objeto.toString();
                System.out.println("\nMensaje JSON enviado: \n <"+resultado+"> \n");
                outbox = new ACLMessage();
                outbox.setSender(this.getAid());
                System.out.println(this.getAid());
                outbox.setReceiver(new AgentID(agente));
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
                objeto.add("x", inicioX);
                objeto.add("y", inicioY);

                resultado = objeto.toString();
                System.out.println("\nMensaje JSON enviado: \n <"+resultado+"> \n"); 
                outbox = new ACLMessage();
                System.out.println(this.getAid());
                outbox.setSender(this.getAid());
                outbox.setConversationId(id);
                outbox.setReceiver(new AgentID(agente));
                outbox.setContent(resultado);
                outbox.setPerformative(ACLMessage.REQUEST);
                this.send(outbox);   
                
            break;
            
            case "moveRefuelStopRescue":
                objeto = new JsonObject();
                objeto.add("command",movimiento);
                resultado = objeto.toString();
                
                outbox = new ACLMessage();
                outbox.setContent(resultado);
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID(agente));
                outbox.setConversationId(id);
                outbox.setInReplyTo(reply);
                outbox.setPerformative(ACLMessage.REQUEST);
                this.send(outbox);
            break;
            
            case "query":
               // 
                outbox = new ACLMessage();
                
                outbox.setReceiver(new AgentID(agente));
                outbox.setSender(this.getAid());
                
                outbox.setConversationId(id);
                outbox.setInReplyTo(reply);
                outbox.setPerformative(ACLMessage.QUERY_REF);
                this.send(outbox);
            break;
            
            case "logout":
                outbox = new ACLMessage();
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID(agente));
                outbox.setConversationId(id);
                outbox.setPerformative(ACLMessage.CANCEL);
                this.send(outbox);
            break;
        }
    }
    
    protected void percibirJSON(ACLMessage inbox){

            System.out.println("\nPercepciones: "+ quiensoy);
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
            }else if(comandoEnvi == "moveRefuelStopRescue"){
                System.out.println("Estoy en moveRefuelStopRescue");
                System.out.println(objetoPercepcion.get("result").asString());
                siguienteMovimiento();
            }
            
            
    }
    
    protected String recibirMensajeJSON() throws InterruptedException {
         System.out.println("\nRespuesta del controlador: " + quiensoy +" "+ comandoEnvi);
        ACLMessage inbox;
        try {
            inbox = this.receiveACLMessage();
           
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
                //System.out.println(" id : "+ id + "  on " + session);
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
                    System.out.print(ANSI_GREEN_BACKGROUND +this.mapaMemoria[i][j]+ ANSI_RESET + "  ");
                }else{
                    System.out.print(this.mapaMemoria[i][j]+" ");
                }
            }
            System.out.print("\n");
        }
    }

    public String getSession() {
        return session;
    }

    public void setQuiensoy(String quiensoy) {
        this.quiensoy = quiensoy;
    }
    
    public void setAgente(String agente) {
        this.agente = agente;
    }
    
    public void enviarSession(String agente){
        JsonObject objeto = new JsonObject();
        objeto.add("session",session);
        String resultado = objeto.toString();
                
        outbox = new ACLMessage();
        outbox.setContent(resultado);
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID(agente));
        outbox.setConversationId(id);
        outbox.setInReplyTo(reply);
        outbox.setPerformative(ACLMessage.INFORM);
        this.send(outbox);
    }
    
    protected void siguienteMovimiento(){
       

            if (this.gonioAngle<0 && this.gonioAngle<=22.5 && this.gonioAngle>337.5 && this.gonioAngle <=360){
                if (>y_rec){
                    commandmov = "moveUP";
                }else{
                    commandmov = "moveN";
                }
            }
            
            if (this.gonioAngle>22.5 && this.gonioAngle<=67.5){
                if (radar[4][6]>y_rec){
                    commandmov = "moveUP";
                }else{
                    commandmov = "moveNE";
                }
            }
            
            if (this.gonioAngle>67.5 && this.gonioAngle<=112.5){
                if (radar[5][6]>y_rec){
                    commandmov = "moveUP";
                }else{
                    commandmov = "moveE";
                }           
            }
            
            if (this.gonioAngle>112.5 && this.gonioAngle<=157.5){
                if (radar[6][6]>y_rec){
                    commandmov = "moveUP";
                }else{
                    commandmov = "moveSE";
                }
            }

            if (this.gonioAngle>157.5 && this.gonioAngle<=202.5){
                if (radar[6][5]>y_rec){
                    commandmov = "moveUP";
                }else{
                    commandmov = "moveS";
                }
            }
            if (this.gonioAngle>205.5 && this.gonioAngle<=247.5){
                if (radar[6][4]>y_rec){
                    commandmov = "moveUP";
                }else{
                    commandmov = "moveSW";
                }
            }
            if (this.gonioAngle>247.5 && this.gonioAngle<=292.5){
                if (radar[5][4]>y_rec){
                    commandmov = "moveUP";
                }else{
                    commandmov = "moveW";
                }
            }
             
            if (this.gonioAngle>292.5 && this.gonioAngle<=337.5){
                if (radar[4][4]>y_rec){
                    commandmov = "moveUP";
                }else{
                    commandmov = "moveNW";
                }
            }
            
        
    }
    
 
     protected void enviarMensajeJSONControlador(String comando) {
        comandoEnvi = comando;
        JsonObject objeto;
        String resultado = null;
        ACLMessage outbox = null;
        switch(comando){
            case "moveRefuelStopRescue":
                objeto = new JsonObject();
                objeto.add("x",x);
                objeto.add("y",y);
                objeto.add("z",z);
                objeto.add("distancia",distance);
                objeto.add("angulo",angle);
                resultado = objeto.toString();
                
                outbox = new ACLMessage();
                outbox.setContent(resultado);
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID("juan"));
                outbox.setConversationId(id);
                outbox.setInReplyTo(reply);
                outbox.setPerformative(ACLMessage.INFORM);
                this.send(outbox);
            break;
            
            case "query":
               // 
                outbox = new ACLMessage();
                
                outbox.setReceiver(new AgentID("juan"));
                outbox.setSender(this.getAid());
                
                outbox.setConversationId(id);
                outbox.setInReplyTo(reply);
                outbox.setPerformative(ACLMessage.QUERY_REF);
                this.send(outbox);
            break;
            
            case "logout":
                outbox = new ACLMessage();
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID("juan"));
                outbox.setConversationId(id);
                outbox.setPerformative(ACLMessage.CANCEL);
                this.send(outbox);
            break;
        }
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
        
        protected void recibirPosiciones() throws InterruptedException {
            ACLMessage inbox;
            inbox = this.receiveACLMessage();
            emisor = inbox.getReceiver();
            System.out.println("\nRecibir posiciones de : "+ emisor );
            String fuente = inbox.getContent();
            JsonObject objetoRespuesta = Json.parse(fuente).asObject();
            if (inbox.getPerformativeInt() == ACLMessage.INFORM){
                x_rec = objetoRespuesta.get("x").asInt();
                y_rec = objetoRespuesta.get("x").asInt();
                z_rec = objetoRespuesta.get("x").asInt();
                gonioAngle = objetoRespuesta.get("angulo").asFloat();
                distance_rec = objetoRespuesta.get("distancia").asFloat();
            }
             System.out.println("\n\tGPS: x:" + x_rec + " y:" + y_rec + " z:" + z_rec + " distance " + distance_rec + " angulo " + angule_rec);
  
        }
        
        private float calcularGonio(){
            
           float resultado = 50;
            return resultado;
        }
        
//    protected boolean esBueno(String movimiento){
//        if(movimiento.equals("moveN")){
//            if(radar[4][5]!=0 && radar[4][5] <= this.z){
//                return true;
//            }
//        }else if(movimiento.equals("moveNE")){
//            
//            if(radar[4][6]!=0 && radar[4][6] <= this.z){
//                return true;
//            }
//        }else if(movimiento.equals("moveE")){
//            if(radar[5][6]!=0 && radar[5][6] <= this.z){
//                return true;
//            }
//        }else if(movimiento.equals("moveSE")){
//            if(radar[6][6]!=0 && radar[6][6] <= this.z){
//                return true;
//            }
//        }else if(movimiento.equals("moveS")){
//            if(radar[6][5]!=0 && radar[6][5] <= this.z){
//                return true;
//            }
//        }else if(movimiento.equals("moveSW")){
//            
//           if(radar[6][4]!=0 && radar[6][4] <= this.z){
//                return true;
//            }
//        }else if(movimiento.equals("moveW")){
//            
//            if(radar[5][4]!=0 && radar[5][4] <= this.z){
//                return true;
//            }
//        }else if(movimiento.equals("moveNW")){
//             if(radar[4][4]!=0 && radar[4][4] <= this.z){
//                return true;
//            }
//        }else if(movimiento.equals("moveUP")){
//            if(this.z < this.alturaMax){
//                return true;
//            }
//        }else if(movimiento.equals("moveDW")){
//            if(this.z > this.alturaMin || this.z > radar[5][5]){
//                return true;
//            }
//        }
//        return false;
//    }
        
}

