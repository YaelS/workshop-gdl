package com.recluit.lab.restclient;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestClient {

//	public String getMessage(){
//		Client client = Client.create();
//		WebResource webResource = client.resource("http://localhost:8080/RESTServer/rest/socket");
//		ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);
//		
//		if(response.getStatus() != 200){
//			throw new RuntimeException("Failed: "+ response.getStatus());
//		}
//		
//		return response.getEntity(String.class);
//	}
	
	public int sendMsg(String row){
		
		System.out.println("Entro al insertLoan de RestClient...");
		Client client = Client.create();
		String url = "http://localhost:8080/RESTService/rest/socket/"+row.replace(" ", "%20");
		System.out.println("URL: "+url);
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.accept("text/plain").get(ClientResponse.class);
		System.out.println("Status: "+response.getStatus());
		if(response.getStatus() == 200){
			System.out.println("Se envio sin error.");			
		}
		return response.getEntity(Integer.class);
		
	}
	
}
