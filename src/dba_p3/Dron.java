/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dba_p3;

import DBA.SuperAgent;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
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
    protected String quiensoy="hawk";
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
    protected float fuel;
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
    private float distanceRescueDron;
    private float rescueAngle;
    DBAMap map;
    private final String controlador;
    ArrayList<Aleman> arrayDeAlemanes = new ArrayList<Aleman>();
    private boolean esta;
    private boolean primerAleman;
    JsonArray radarJSON;
    public int[][] infraredR;
    JsonArray img;
    public DBAMap mapR;

    public Dron(AgentID aid) throws Exception {
        super(aid);
        this.mapa = "playground";
        this.user = "Kazi";
        this.password = "moHhEBMN";
        
        this.controlador = DBA_P3.Controlador;
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
        // aleman en el 30 30 y el 72 72
        System.out.print("\naaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\n");
        enviarSession(this.NOMBRE_HAWK, 20, 20);
        enviarSession(this.NOMBRE_FLY1, 30, 30);
       // enviarSession(this.NOMBRE_FLY2, 31, 31);
        enviarSession(this.NOMBRE_RESCUE, 30, 30);
        System.out.print("\nbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb\n");
        System.out.println("hola este es el mapa en la posicion 30 30 " +map.getLevel(30, 30));
        do{
//            enviarMensajeJSON("query");
//            try {
//                respuesta = recibirMensajeJSON();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
//            }
            System.out.print("\ncccccccccccccccccccccccccccccccccccccccccccccccc\n");
            try {
                emisor = recibirPosiciones();
                enviarMovimiento(emisor);
            } catch (InterruptedException ex) {
                Logger.getLogger(Dron.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }while(this.torescue < 1);
        System.out.print("//////////////////////////////////////////////////////////////////\n");
        System.out.print(this.torescue);
        System.out.print("\n//////////////////////////////////////////////////////////////////\n");
        //enviarMensajeJSON("logout");
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
                System.out.println(this.getAid());
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
                    
                    img = Json.parse(fuente).asObject().get("map").asArray();
                    //System.out.println(img);
                    map = new DBAMap();
                    map.fromJson(img);
                    
                    System.out.println("IMAGE DATA:");
                    /// 3) Cuyas dimensiones se pueden consultar
                    System.out.println(map.getWidth()+" pixs width & "+map.getHeight()+" pixs height");
                    //this.dimX = map.getWidth();
                    //this.dimY = map.getHeight();
                    crearMemoria();
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
                this.x = objetoPercepcion.get("result").asObject().get("gps").asObject().get("x").asInt();
                this.y =objetoPercepcion.get("result").asObject().get("gps").asObject().get("y").asInt();
                this.z =objetoPercepcion.get("result").asObject().get("gps").asObject().get("z").asInt();
                
                System.out.println("\n\tGPS: x:" + this.x + " y:" + this.y + " z:" + this.z);
                
                distance = objetoPercepcion.get("result").asObject().get("gonio").asObject().get("distance").asFloat();
                angle = objetoPercepcion.get("result").asObject().get("gonio").asObject().get("angle").asFloat();
                
                radarJSON = objetoPercepcion.get("result").asObject().get("infrared").asArray();
                
                fuel = objetoPercepcion.get("result").asObject().get("fuel").asFloat();
                
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
                //siguienteMovimiento();
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

    public String getSession() {
        return session;
    }

    public void setQuiensoy(String quiensoy) {
        this.quiensoy = quiensoy;
    }
    
    public void setAgente(String agente) {
        this.agente = agente;
    }
    
    public void enviarSession(String agente, int x, int y){
        JsonObject objeto = new JsonObject();
        objeto.add("session",session);
        objeto.add("inicioX", x);
        objeto.add("inicioY", y);
        objeto.add("mapa", img);
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
    
    public void enviarMovimiento(AgentID agente){
        if(agente.name != this.NOMBRE_RESCUE){
            siguienteMovimiento();
        }else{
            siguienteMovimientoRescue(agente.name);
        }
        
        JsonObject objeto = new JsonObject();
        objeto.add("movimiento",commandmov);
        String resultado = objeto.toString();
                
        outbox = new ACLMessage();
        outbox.setContent(resultado);
        outbox.setSender(this.getAid());
        outbox.setReceiver(agente);
        outbox.setConversationId(id);
        outbox.setInReplyTo(reply);
        outbox.setPerformative(ACLMessage.INFORM);
        this.send(outbox);
    }
    
    protected void siguienteMovimiento(){
        veoAleman();
        Random r = new Random();
        int ran = r.nextInt(7);
        
//        
////        
//        if(esAceptable("moveN") && esBueno("moveN")){
//            commandmov = "moveN";
//        }else if(esAceptable("moveNW") && esBueno("moveNW")){
//            commandmov = "moveNW";
//        }else if(esAceptable("moveW") && esBueno("moveW")){
//            commandmov = "moveW";
//        }else if(esAceptable("moveSW") && esBueno("moveSW")){
//            commandmov = "moveSW";
//        }else if(esAceptable("moveS") && esBueno("moveS")){
//            commandmov = "moveS";
//        }else if(esAceptable("moveSE") && esBueno("moveSE")){
//            commandmov = "moveSE";
//        }else if(esAceptable("moveE") && esBueno("moveE")){
//            commandmov = "moveE";
//        }else if(esAceptable("moveNE") && esBueno("moveNE")){
//            commandmov = "moveNE";
//        }

        switch(ran){

            case 0:
                    commandmov = "moveN";
                  break; 
            case 1:
                    commandmov = "moveS";
                   break; 
            case 2:
                    commandmov = "moveE";

                    break; 
            case 3:
                    commandmov = "moveW";
                break; 
            case 4:
                    commandmov = "moveNE";
                break; 
            case 5:
                    commandmov = "moveSE";
                break; 
            case 6:
                    commandmov = "moveSW";
                break; 
            case 7:
                    commandmov = "moveNW";
                    break; 

        }
        System.out.println("el movimiento elegido " + commandmov);
        if(!esBueno(commandmov)&& !esAceptable(commandmov)){
            siguienteMovimiento();
        }else{
            guardarPosicionMemoria();
        }    
        //checkFuel();
        
        System.out.println(ANSI_YELLOW_BACKGROUND+arrayDeAlemanes+ ANSI_RESET + "  ");
        
    }
    
    private float calcularDistancia2Puntos(int x1, int y1, int x2, int y2){
        float distancia = (float) Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
        return distancia;
    }
    
    private void calcularRescate(){
        int xAleman=0;// = arrayDeAlemanes.
        int yAleman=0;//DATOS FALSEADOS
        distanceRescueDron = calcularDistancia2Puntos(x_rec,xAleman,y_rec,yAleman);//c
        int xCentro = map.getWidth()/2;
        int yCentro = map.getHeight()/2;
        float a = calcularDistancia2Puntos(x_rec,xCentro,y_rec,yCentro);
        float b = calcularDistancia2Puntos(xAleman,xCentro,yAleman,yCentro);
        //Cálculo del angulo del triángulo que se forma con el centro
        //rescueAngle = (float) Math.acos((Math.pow(a, 2)+Math.pow(b, 2)-Math.pow(distanceRescueDron, 2)/(2*a*b)));
        rescueAngle = (float) (Math.toDegrees(Math.atan2(yAleman - y_rec,xAleman- x_rec))+90);
    }
    
    public void siguienteMovimientoRescue(String NombreAgente){
        calcularRescate();
        if(arrayDeAlemanes.size() == 0){
            if (map.getLevel(x_rec, y_rec)>z_rec){
                commandmov = "moveDW";
            }else{
                commandmov = "refuel";
            }
        }else{
            if(distanceRescueDron <= 1){
                System.out.println("encontre una alemannn eeeeehhhh");
                if(distanceRescueDron==0){
                    if (map.getLevel(x_rec, y_rec)<z_rec){
                        System.out.println("VOY BAJANDO; BAJANDO; BAJANDO");
                        commandmov = "moveDW";
                    }else{
                        System.out.println("RESCATO A UN ALEMAAAAAAAAANSSSSSS");
                        commandmov = "rescue";
                        // arrayDeAlemanes.add(a); QUITAR DEL ARRAY
                    }  
                }
            }else{
                if (this.rescueAngle<0 && this.rescueAngle<=22.5 && this.rescueAngle>337.5 && this.rescueAngle <=360){
                    if (map.getLevel(x_rec-1, y_rec)>z_rec){
                        commandmov = "moveUP";
                    }else{
                        commandmov = "moveN";
                    }
                }

                if (this.rescueAngle>22.5 && this.rescueAngle<=67.5){
                    if (map.getLevel(x_rec-1, y_rec+1)>z_rec){
                        commandmov = "moveUP";
                    }else{
                        commandmov = "moveNE";
                    }
                }

                if (this.rescueAngle>67.5 && this.rescueAngle<=112.5){
                    if (map.getLevel(x_rec, y_rec+1)>z_rec){
                        commandmov = "moveUP";
                    }else{
                        commandmov = "moveE";
                    }           
                }

                if (this.rescueAngle>112.5 && this.rescueAngle<=157.5){
                    if (map.getLevel(x_rec+1, y_rec+1)>z_rec){
                        commandmov = "moveUP";
                    }else{
                        commandmov = "moveSE";
                    }
                }

                if (this.rescueAngle>157.5 && this.rescueAngle<=202.5){
                    if (map.getLevel(x_rec+1, y_rec)>z_rec){
                        commandmov = "moveUP";
                    }else{
                        commandmov = "moveS";
                    }
                }
                if (this.rescueAngle>205.5 && this.rescueAngle<=247.5){
                    if (map.getLevel(x_rec+1, y_rec-1)>z_rec){
                        commandmov = "moveUP";
                    }else{
                        commandmov = "moveSW";
                    }
                }
                if (this.rescueAngle>247.5 && this.rescueAngle<=292.5){
                    if (map.getLevel(x_rec, y_rec-1)>z_rec){
                        commandmov = "moveUP";
                    }else{
                        commandmov = "moveW";
                    }
                }

                if (this.rescueAngle>292.5 && this.rescueAngle<=337.5){
                    if (map.getLevel(x_rec-1, y_rec-1)>z_rec){
                        commandmov = "moveUP";
                    }else{
                        commandmov = "moveNW";
                    }
                }
            }
            
            //checkFuel();
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
                objeto.add("fuel",fuel);
                objeto.add("rango",rango);
               
                objeto.add("infrared",radarJSON);
                
                resultado = objeto.toString();
                
                outbox = new ACLMessage();
                outbox.setContent(resultado);
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID(controlador));
                outbox.setConversationId(id);
                outbox.setInReplyTo(reply);
                outbox.setPerformative(ACLMessage.INFORM);
                this.send(outbox);
            break;
            
            case "query":
               // 
                outbox = new ACLMessage();
                
                outbox.setReceiver(new AgentID(controlador));
                outbox.setSender(this.getAid());
                
                outbox.setConversationId(id);
                outbox.setInReplyTo(reply);
                outbox.setPerformative(ACLMessage.QUERY_REF);
                this.send(outbox);
            break;
            
            case "logout":
                outbox = new ACLMessage();
                outbox.setSender(this.getAid());
                outbox.setReceiver(new AgentID(controlador));
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
            if (inbox.getPerformativeInt() == ACLMessage.INFORM){
                System.out.println("entrooooooooooo");
                reply = inbox.getReplyWith();
                System.out.println(reply);
                JsonObject objetoPercepcion = Json.parse(fuente).asObject();
                session = objetoPercepcion.get("session").asString();
                this.inicioX = objetoPercepcion.get("inicioX").asInt();
                this.inicioY = objetoPercepcion.get("inicioY").asInt();
                JsonArray radarJSONRec = objetoPercepcion.get("mapa").asArray();
                mapR = new DBAMap();
                try {
                    mapR.fromJson(radarJSONRec);
                } catch (IOException ex) {
                    System.out.println("fallo al recibir el mapa en el agente");
                }
            }

            return session;   
        }
        
        protected AgentID recibirPosiciones() throws InterruptedException {
            ACLMessage inbox;
            inbox = this.receiveACLMessage();
            emisor = inbox.getSender();
            System.out.println("\nRecibir posiciones de : "+ emisor );
            String fuente = inbox.getContent();
            JsonObject objetoRespuesta = Json.parse(fuente).asObject();
            if (inbox.getPerformativeInt() == ACLMessage.INFORM){
                x_rec = objetoRespuesta.get("x").asInt();
                y_rec = objetoRespuesta.get("y").asInt();
                z_rec = objetoRespuesta.get("z").asInt();
                gonioAngle = objetoRespuesta.get("angulo").asFloat();
                distance_rec = objetoRespuesta.get("distancia").asFloat();
                fuel = objetoRespuesta.get("fuel").asFloat();
                float rangoR = objetoRespuesta.get("rango").asFloat();
                JsonArray radarJSONRec = objetoRespuesta.get("infrared").asArray();
                int rangoconverR = (int) rangoR;
              
                for (int i = 0; i < radarJSONRec.size(); i++) {
                    prueba[i] = radarJSONRec.get(i).asInt();
                }
                int indice=0;
                infraredR = new int[rangoconverR][rangoconverR];
                
                for (int i = 0; i < infraredR.length; i++) {
                   for (int j = 0; j < infraredR[i].length; j++) {
                    infraredR[i][j] =(int) prueba[indice];
                    indice++;
                    }
                }
            }
            System.out.println("\n\tGPS: x:" + x_rec + " y:" + y_rec + " z:" + z_rec + " distance " + distance_rec + " angulo " + gonioAngle);
            int level = map.getLevel(x_rec+25, y_rec+20);
            System.out.println(level);
            return emisor;
        }
        
        protected boolean esBueno(String movimiento){
            System.out.println(" infrared recibido " + infraredR.length/2);
            int centro = infraredR.length/2;
            switch (movimiento) {
                case "moveN":
                    if(infraredR[centro-1][centro]!=-1){
                        return true;
                    }
                    break;
                case "moveNE":
                    if(infraredR[centro-1][centro+1]!=-1){
                        return true;
                    }
                    break;
                case "moveE":
                    if(infraredR[centro][centro+1]!=-1){
                        return true;
                    }
                    break;
                case "moveSE":
                    if(infraredR[centro+1][centro+1]!= -1 ){
                        return true;
                    }
                    break;
                case "moveS":
                    if(infraredR[centro+1][centro]!=-1){
                        return true;
                    }
                    break;
                case "moveSW":
                    if(infraredR[centro-1][centro-1]!=-1){
                        return true;
                    }
                    break;
                case "moveW":
                    if(infraredR[centro][centro-1]!=-1){
                        return true;
                    }
                    break;
                case "moveNW":
                    if(infraredR[centro-1][centro-1]!=-1){
                        return true;
                    }
                    break;
                case "moveUP":
                    if(z_rec < this.alturaMax){
                        return true;
                    }
                    break;
                case "moveDW":
                    if(z_rec > map.getLevel(x_rec, y_rec)){
                        return true;
                    }
                    break;
        }
            return false;
        }
        
        protected String recibirMovimiento() throws InterruptedException {
            ACLMessage inbox;
            inbox = this.receiveACLMessage();
            System.out.println("\nRespuesta del controlador: ");
            String fuente = inbox.getContent();
            JsonObject objetoRespuesta = Json.parse(fuente).asObject();
            if (inbox.getPerformativeInt() == ACLMessage.INFORM){
                System.out.println("entrooooooooooo");
                movimiento = objetoRespuesta.get("movimiento").asString();
            }

            return movimiento;   
        }
       
        protected void checkFuel(){
            
            if((z_rec - mapR.getLevel(x_rec, y_rec))/5 > (this.fuel-10)/gasto){
                System.out.println("\nNECESITA REPOSTAR "+fuel + " " + z_rec);
                commandmov = "moveDW";
                if(z_rec == mapR.getLevel(x_rec, y_rec)){
                    commandmov = "refuel";
                }
            }
        }
        
    private boolean estaContenido(int x, int y){
        Aleman a = new Aleman(x, y);
        //boolean existe = arrayDeAlemanes.contains(a);
        if(arrayDeAlemanes.size()==0){
            return false;
        }else{
            for(int i=0; i < arrayDeAlemanes.size(); i++){
                if(a.getX() == arrayDeAlemanes.get(i).getX() && a.getY() == arrayDeAlemanes.get(i).getY()){
                    return true;
                }
            }
            return false;
        }
                   
    } 
    /**
    *
    * @author Manuel
    */
    protected void crearMemoria(){
        this.mapaMemoria = new int[dimY][dimX];
        for(int i=0; i<dimY; i++)
            for(int j=0; j<dimX; j++)
                this.mapaMemoria[i][j]=0; 
    }
    
    /**
    *
    * @author Manuel
    */
    protected void guardarPosicionMemoria(){
        this.mapaMemoria[y_rec][x_rec]++;
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
    protected Boolean esAceptable(String movimiento){
        
        
        if(movimiento == "moveN"){
            if(this.y==0){
                return false;
            }else if(y_rec == this.dimY-1 && x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec][x_rec-1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == this.dimY && x_rec == 0){
                if(this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(x_rec==0){
                if((this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec+1])){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == this.dimY-1){
                if(this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else{
                if(this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec-1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }
        }
        
        if(movimiento == "moveNE"){///////////////////////////////////////////////////////////////
            if(y_rec == 0){
                return false;
            }else if(x_rec == this.dimX-1){
                return false;
            }else if(y_rec == this.dimY-1 && x_rec == 0){
                if(this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec]
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }    
            }else if(x_rec == 0){
                if(this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == this.dimY-1){
                if(this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else{
                if(this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec-1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    //mostrarMemoria();
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }
        }
        
        if(movimiento == "moveE"){
            if(x_rec == this.dimX-1){
                return false;
            }else if(y_rec == 0 && x_rec == 0){
                if(this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec][x_rec] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == this.dimY-1 && x_rec == 0){
                if(this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec+1]
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec][x_rec] ){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == 0){
                if(this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec][x_rec] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec-1]
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(x_rec == 0){
                if(this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec+1]
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec][x_rec]
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == this.dimY-1){
                if(this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec+1]
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec][x_rec] ){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else{
                if(this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec+1]
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec][x_rec] 
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec-1]
                && this.mapaMemoria[y_rec][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }
        }
        
        if(movimiento == "moveSE"){
            if(y_rec == this.dimY-1){
                return false;
            }else if(x_rec == this.dimX-1){
                return false;
            }else if(y_rec == 0 && x_rec == 0){
                if(this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec+1]  
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == 0){
                if(this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(x_rec == 0){
                if(this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec+1]
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec+1]
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else{
                if(this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec+1][x_rec+1]<=this.mapaMemoria[y_rec-1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }
        }
        
        if(movimiento == "moveS"){
            if(y_rec == this.dimY-1){
                return false;
            }else if(y_rec == 0 && x_rec == 0){
                if(this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec][x_rec+1]
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == 0 && x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == 0){
                if(this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == 0){
                if(this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec+1]
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec][x_rec+1]
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec]
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else{
                if(this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec-1][x_rec]
                && this.mapaMemoria[y_rec+1][x_rec]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }
        }
        
        if(movimiento == "moveSW"){
            if(y_rec == this.dimY-1){
                return false;
            }else if(x_rec == 0){
                return false;
            }else if(y_rec == 0 && x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == 0){
                if(this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec-1] 
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else{
                if(this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec+1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }
        }
        
        if(movimiento == "moveW"){
            if(x_rec == 0){
                return false;
            }else if(y_rec == 0 && x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == this.dimY-1 && x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec-1]
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == 0){
                if(this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == this.dimY-1){
                if(this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec]
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec-1]
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec]
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else{
                if(this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec]
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }

        }
        
        if(movimiento == "moveNW"){
            if(y_rec == 0){
                return false;
            }else if(x_rec == 0){
                return false;
            }else if(y_rec == this.dimY-1 && x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec]
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec-1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(y_rec == this.dimY-1){
                if(this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else if(x_rec == this.dimX-1){
                if(this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec]
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }else{
                if(this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec] 
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec-1][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec-1]
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec][x_rec+1] 
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec-1] 
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec]
                && this.mapaMemoria[y_rec-1][x_rec-1]<=this.mapaMemoria[y_rec+1][x_rec+1]){
                    System.out.print("\nNo es un movimiento repe");
                    return true;
                }else{
                    System.out.print("\nES UN MOVIMIENTO REPE");
                    return false;
                }
            }

        }
        
        return false;
        
    }
    
    public void veoAleman(){
        int rel_i, rel_j;
        String cuadrante=null;
        int centro = infraredR.length/2;
        for(int i=0; i<infraredR.length; i++){
            for(int j=0; j<infraredR[i].length; j++){
                rel_i=-1;
                rel_j=-1;
                if(infraredR[i][j]==1){
                   
                    if(i==centro && j==centro){ //si esta en el centro
                        rel_i = y_rec;
                        rel_j = x_rec;
                    }
                    
                    if(i<centro && j==centro){ //N
                        rel_i = y_rec-centro+i;
                        rel_j = x_rec;
                    }
                    
                    if(i>centro && j==centro){ //S
                        rel_i = y_rec+centro+(infraredR.length-i);
                        rel_j = x_rec;
                    }
                    
                    if(i==centro && j<centro){ //W
                        rel_i = y_rec;
                        rel_j = x_rec-centro+j;
                    }
                    
                    if(i==centro && j>centro){ //E
                        rel_i = y_rec;
                        rel_j = x_rec+centro+(infraredR.length-j);
                    }
                    
                    if(i<centro && j<centro){ //NW
                        rel_i = y_rec-i;
                        rel_j = x_rec-j;
                    }
                    
                    if(i>centro && j<centro){ //SW
                        //rel_i = y_rec+centro+(infraredR.length-i);
                        rel_i = y_rec-i;
                        rel_j = x_rec-j+centro;
                    }
                    
                    if(i<centro && j>centro){ //NE
                        rel_i = y_rec+centro+i;
                        rel_j = x_rec+(centro-j);
                    }
                    
                    if(i>centro && j>centro){ //SE
                        rel_i = y_rec+(infraredR.length-i)+centro;
                        rel_j = x_rec+(infraredR.length-j)+centro;
                    }
                
                    //System.out.println("he encontrado un aleman" + rel_i + " " + rel_j);
                    if(rel_i!=-1 && rel_j!=-1){
                        
                        esta = estaContenido(rel_i, rel_j);
                        if(!esta)
                            System.out.println(ANSI_RED_BACKGROUND+"añado aleman"+ANSI_RESET);
                            arrayDeAlemanes.add(new Aleman(rel_j, rel_i));
                        } 
                    }
                    
                }
            
        }
    }
    
    public boolean estoyEncimaAleman(){
        int centro = infrared.length/2;
        if(infrared[centro][centro]==1){
            return true;
        }
        return false;
    }
    
    public int obtener(int x , int y){
       return this.map.getLevel(x, y);
    }
}

