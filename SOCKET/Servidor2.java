package SOCKET;

import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.io.*;
public class Servidor2  {

	public static void main(String[] args) {
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidor extends JFrame implements Runnable {
	
	public MarcoServidor(){
		
		setBounds(1000,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);

		Thread mihilo=new Thread(this); //CREAMOS EL HILO 1
		mihilo.start();
		
		}
	
	private	JTextArea areatexto;

	public void run() {
		try {

			// ASI SE CREA EL SERVIDOR: CON LA CLASE SERVERSOCKET
			ServerSocket servidor=new ServerSocket(9999); // Y LO PUSIMOS A LA ESCUCHA Y QUE ABRA EL PUERTO 9999
			Socket elsocket=servidor.accept(); //QUE ACEPTE LAS CONECCIONES
			
			//ESTO ES PARA CREAR UN FLUJO DE DATOS DE ENTRADA
			//PARA QUE SEA CAPAZ DE CAPTURAR LO QUE VIAJA DEL OTRO FLUJO DE DATOS (LA LINEA DE ABAJO)
			DataInputStream flujo_entrada=new DataInputStream(elsocket.getInputStream());

			//PARA PODER LEER LO QUE VIENE PONEMOS:
			String mensaje_texto=flujo_entrada.readUTF();

			//¿COMO HACEMOS PARA DECIRLE QUE LO ESCRIBA/AGREGUE EN EL JTEXTAREA? A LO QUE RECIBIMOS EN mensaje_texto
			areatexto.append("\n" + mensaje_texto); //LE AGREGUE UN SALTO DE LÍNEA
			
			elsocket.close(); // cerramos la coneccion


		} catch (IOException e) {
	
			e.printStackTrace();
		}
	}
}
