package com.recluit.sockets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientSocketExample {
	
	private String opt;
	private String message;

	public String establishConnection(){
		
		System.out.println("Message: "+message);
		String str = null;
		
		Socket s;
		try {
			s = new Socket("127.0.0.1", 3550);
			InputStreamReader stream = new InputStreamReader(s.getInputStream());
			BufferedReader reader = new BufferedReader(stream);
			
			PrintWriter writer = new PrintWriter(s.getOutputStream());
			writer.flush();
			writer.print(opt);
			writer.flush();	
			
			//writer.print(hola);
			//writer.flush();
			
			//writer.print("\n118|34567ZXCVB|PATRIA|INDIA|35000|30/6/2013|VERY BAD|Y\0");
			writer.print(message);
			writer.flush();		
			
			while((str = reader.readLine()) != null){
				System.out.println("Text received from client: "+str);
			}		
			
			reader.close();
			writer.close();	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return str;
		
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
