package SOCKET;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import java.util.ArrayList;

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

		addWindowListener(new EnvioOnline()); 
		}	
	
}

//-------------------CLASE DE ENVIO DE SEÑAL ONLINE-----------------------------
class EnvioOnline extends WindowAdapter{

	public void windowOpened(WindowEvent e){

		try {
			Socket socket=new Socket("192.168.0.194", 9999);
			PaqueteEnvio datos=new PaqueteEnvio();
			datos.setMensaje(" online");
			ObjectOutputStream paquete_datos=new ObjectOutputStream(socket.getOutputStream());
			paquete_datos.writeObject(datos);
			socket.close();
		} catch (Exception e2) {
			
		}
	}
}


class LaminaMarcoCliente extends JPanel implements Runnable {
	
	public LaminaMarcoCliente(){

		String nick_usuario=JOptionPane.showInputDialog("Nick: ");

		JLabel n_nick=new JLabel("Nick: ");
		add(n_nick);

		nick=new JLabel();
		nick.setText(nick_usuario);
		add(nick);
	
		JLabel texto=new JLabel("Chat");
		add(texto);

		ip=new JComboBox();
		ip.addItem("User 1");
		ip.addItem("User 2");
		ip.addItem("User 3");
		add(ip);

		areachat=new JTextArea(12,20);
		add(areachat);
	
		campo1=new JTextField(20);
		add(campo1);		
	
		miboton=new JButton("Enviar");
		EnviaTexto mi_evento=new EnviaTexto();
		miboton.addActionListener(mi_evento);
		add(miboton);	

		//PARA QUE EL HILO YA ESTÉ EN EJECUCION EN SEGUNDO PLANO: 
		Thread hilo=new Thread(this);
		hilo.start();
	}

	private class EnviaTexto implements ActionListener{

		
		public void actionPerformed(ActionEvent e) {
		
			areachat.append("\n" + campo1.getText()); //ESTO ES PARA VER QUE MENSAJES ENVIO 
			//System.out.println(campo1.getText()); CON ESTO ME ESCRIBE EN CONSOLA LO QUE ESCRIBI EN EL CAMPO DE TEXTO
			//CREAMOS EL SOCKET ASI: 
			try {
				Socket misocket=new Socket("192.168.1.10", 9999 );   //COMO PRIMER PARAMETRO ES LA IP Y SEGUNDO ES EL PUERTO
				PaqueteEnvio datos=new PaqueteEnvio();
				datos.setElnick(nick.getText());
				datos.setNum_ip(ip.getSelectedItem().toString());
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
		
		
	private JTextField campo1;
	private JComboBox ip;

	private JLabel nick;

	private JTextArea areachat;
	
	private JButton miboton;


	public void run() {
		try {
			ServerSocket servidorcliente=new ServerSocket(9090);
			Socket cliente;
			PaqueteEnvio parqueteRecibido;

			while(true){
				cliente=servidorcliente.accept();
				ObjectInputStream flujoentrada=new ObjectInputStream(cliente.getInputStream());
				parqueteRecibido=(PaqueteEnvio) flujoentrada.readObject(); //EN ESTA VARIABLE NOS LLEGA DE PARTE DEL SERVIDOR

				areachat.append("\n" + parqueteRecibido.getElnick() + " : " + parqueteRecibido.getMensaje());

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}

class PaqueteEnvio implements Serializable{ //LA SERIALIZAMOS PARA QUE SE CONVIERTA EN BYTES Y ENVIARLA X LA RED

	private String elnick, num_ip, mensaje;
	private ArrayList<String> Ips;
	

	public ArrayList<String> getIps() {
		return Ips;
	}

	public void setIps(ArrayList<String> ips) {
		Ips = ips;
	}

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