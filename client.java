
/**
 * Code is taken from Computer Networking: A Top-Down Approach Featuring 
 * the Internet, second edition, copyright 1996-2002 J.F Kurose and K.W. Ross, 
 * All Rights Reserved.
 **/

import java.io.*;
import java.net.*;
import java.util.*;

class client{
    
    public static void main(String argv[]) throws Exception  {
	
    	 String sentence; 
		  String modifiedSentence,requestMessageLine; 
		  String server_ip,server_to_connect;
			int port;
			int my_port;
			InetAddress ip=InetAddress.getLocalHost();
			server_ip=ip.getHostAddress().trim();
			Scanner scanner = new Scanner(System. in);
			server_to_connect = scanner. nextLine();
	        port=scanner. nextInt();
	        my_port=scanner. nextInt();

		  BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 

		  Socket clientSocket = new Socket(server_to_connect, port); 

		  DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
		   
		  sentence = inFromUser.readLine(); 
		  String[] tokens=sentence.split(" ");
		  if(tokens[0].equals("GET"))
			  sentence=sentence+" "+server_ip+" "+my_port;
		  outToServer.writeBytes(sentence); 
		  
		  clientSocket.close();
		 if(tokens[0].equals("GET") ){
			 ServerSocket listen_socket = new ServerSocket(my_port);
	            Socket connectionSocket = listen_socket.accept();

	            BufferedReader inFromClient =new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	        	requestMessageLine = inFromClient.readLine();
	        	System.out.println(requestMessageLine);
	        	listen_socket.close();
		 }
		
		
		  
			 
		
	
	
	
    }
}


