package com.recluit.bureanservices;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.recluit.sockets.ClientSocketExample;

@Path("/socket")
public class BureanService {
		
	private ClientSocketExample clientSocket;
	
	@GET
	@Path("/{msg}")
	@Consumes(MediaType.TEXT_PLAIN)
	public int sendMsg(@PathParam("msg") String msg){
		String response;
		String[] message = msg.split(":");
		System.out.println("Msg to send: "+msg);
		clientSocket = new ClientSocketExample();
		clientSocket.setOpt(message[0]);
		clientSocket.setMessage('\n'+message[1]+'\0');
		System.out.println("Opt: "+message[0]);
		
		response = clientSocket.establishConnection();
		
		switch(message[0]){
			case "A":
				return 1;
			case "R":
				String[] rows = response.split("%");
				return rows.length;
			case "E":
				return 1;
			default: 
				return 0;
		}
		
	}
		
}
