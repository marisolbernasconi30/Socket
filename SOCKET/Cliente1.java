package SOCKET;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import javax.swing.*;


public class Cliente1 {

	public static void main(String[] args) {
		
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		}	
	
}

class LaminaMarcoCliente extends JPanel{
	
	public LaminaMarcoCliente(){

		nick=new JTextField(8);
		add(nick);
	
		JLabel texto=new JLabel("Chat");
		add(texto);

		ip=new JTextField(12);
		add(ip);

		areachat=new JTextArea(12,20);
		add(areachat);
	
		campo1=new JTextField(20);
		add(campo1);		
	
		miboton=new JButton("Enviar");
		EnviaTexto mi_evento=new EnviaTexto();
		miboton.addActionListener(mi_evento);
		add(miboton);	
		
	}

	private class EnviaTexto implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
		
			//System.out.println(campo1.getText()); CON ESTO ME ESCRIBE EN CONSOLA LO QUE ESCRIBI EN EL CAMPO DE TEXTO
			//CREAMOS EL SOCKET ASI: 
			try {
				Socket misocket=new Socket("192.168.1.10", 9999 );   //COMO PRIMER PARAMETRO ES LA IP Y SEGUNDO ES EL PUERTO
				PaqueteEnvio datos=new PaqueteEnvio();
				datos.setElnick(nick.getText());
				datos.setNum_ip(ip.getText());
				datos.setMensaje(campo1.getText());
				
				ObjectOutputStream datos_salida=new ObjectOutputStream(misocket.getOutputStream());

				datos_salida.writeObject(datos);
				misocket.close();
				
				/* 
				DataOutputStream flujo_salida=new DataOutputStream(misocket.getOutputStream()); //LE DECIMOS POR DONDE TIENE QUE CIRCULAR EL FLUJO DE SALIDA
				flujo_salida.writeUTF(campo1.getText()); //escribe en el flujo lo que hay en el campo1
				flujo_salida.close(); //cerramos el flujo de datos
				*/
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
		}

	}
		
		
	private JTextField campo1, nick, ip;

	private JTextArea areachat;
	
	private JButton miboton;
	
}

class PaqueteEnvio implements Serializable{ //LA SERIALIZAMOS PARA QUE SE CONVIERTA EN BYTES Y ENVIARLA X LA RED

	private String elnick, num_ip, mensaje;

	public String getElnick() {
		return elnick;
	}

	public void setElnick(String elnick) {
		this.elnick = elnick;
	}

	public String getNum_ip() {
		return num_ip;
	}

	public void setNum_ip(String num_ip) {
		this.num_ip = num_ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}