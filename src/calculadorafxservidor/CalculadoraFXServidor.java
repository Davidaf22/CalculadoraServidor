package calculadorafxservidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalculadoraFXServidor extends Thread {
    
    String nombre;
    Socket socket;
    
    public CalculadoraFXServidor (String nombre, Socket socket) {
        
        super(nombre);
        this.socket = socket ;
        
    }
    
    public static int calcular(String lineaOperacion){
        int resultado=0;
        String OperacionString="";
        String numeroActualString="";
        int contadorOperaciones=0;
        int numeroPresente=0;
            
          
          String[] operacionSeparada = lineaOperacion.split("");
          
          
          for(int i=0; i<operacionSeparada.length;i++){   
              
            if(operacionSeparada[i].equals("1")||operacionSeparada[i].equals("2")||operacionSeparada[i].equals("3")||operacionSeparada[i].equals("4")||operacionSeparada[i].equals("5")||operacionSeparada[i].equals("6")||operacionSeparada[i].equals("7")||operacionSeparada[i].equals("8")||operacionSeparada[i].equals("9")||operacionSeparada[i].equals("0")){
                numeroActualString=numeroActualString+operacionSeparada[i];
            }           
            else{
                numeroPresente = Integer.parseInt(numeroActualString);
                System.out.println(numeroPresente);
                if(contadorOperaciones==0){
                    System.out.println("entro");
                    resultado=numeroPresente;
                    System.out.println(resultado);
                }
                else{
                    System.out.println(resultado+""+OperacionString+""+numeroPresente);
                    switch(OperacionString) {
                        case "+":
                            resultado = resultado+numeroPresente;
                            break;
                        case "-":
                            resultado = resultado-numeroPresente;
                            break;
                        case "x":
                            resultado = resultado*numeroPresente;
                            break;
                        case "/":
                            resultado = resultado/numeroPresente;
                            break;
                    } 
                }              
            }  
            if(operacionSeparada[i].equals("+")||operacionSeparada[i].equals("-")||operacionSeparada[i].equals("/")||operacionSeparada[i].equals("x")){
                    OperacionString=operacionSeparada[i];
                    numeroActualString="";
                    contadorOperaciones=1;
                }
        }   
        return resultado;
    }
    
    public void run(){
        
        InputStream is=null;
        try {
            
            is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            
            byte[] operacion=new byte[50];
            is.read(operacion);
            
            
            String operacionString2 = new String(operacion);
            String[] operacionString3 = operacionString2.split(",");
            String operacionString = operacionString3[0];
            operacionString = operacionString+"y";
            
            System.out.println(operacionString);
            
            int resultado = CalculadoraFXServidor.calcular(operacionString);
            String str1 = Integer.toString(resultado);
            
            os.write(str1.getBytes());
            System.out.println("Resultado enviado");
            
            socket.close();
            
        } catch (IOException ex) {
            Logger.getLogger(CalculadoraFXServidor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(CalculadoraFXServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    
    public static void main(String[] args) throws IOException{
        
	System.out.println("Creando socket servidor");
	
        ServerSocket serverSocket=new ServerSocket();

        System.out.println("Realizando el bind");

        InetSocketAddress addr=new InetSocketAddress("192.168.0.27",5555);
        serverSocket.bind(addr);
        
        System.out.println("Aceptando conexiones");
        
        
        while(serverSocket != null){   
            
            Socket newSocket= serverSocket.accept();
            System.out.println("Conexion recibida");
            
            CalculadoraFXServidor secundarioThread = new CalculadoraFXServidor ("secundarioThread",newSocket);
            secundarioThread.start();
            

            System.out.println("Terminado");
            
        } 
        
        //serverSocket.close();
        
    }
}