import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;



class global{
	static String server_ip;
	static String server_to_connect;
	static int port;
	static int my_port;
	static String last_mes;
	static HashMap<String, String> index = new HashMap<String, String>();
}

class client_error implements Runnable {
	String error;
	public client_error(String error) {
		this.error=error;
	}

	@Override
	public void run() {
		try {
		String[] tokens=error.split(" ");
		if(global.index.containsKey(tokens[3])) {
			Socket socket = new Socket(tokens[1], Integer.parseInt(tokens[2]));
        	//System.out.println("ahhh");
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
					
	                outToServer.writeBytes(global.index.get(tokens[3]));
	                socket.close();
	                
	               String requestMessageLine="PUT"+" "+tokens[3]+" "+global.index.get(tokens[3]);
	            	//System.out.println("my life sucks!");
	            	Socket socket2 = new Socket(global.server_to_connect, global.port);
	            	//System.out.println("ahhh");
	    			DataOutputStream outToServer2 = new DataOutputStream(socket2.getOutputStream()); 
	                outToServer2.writeBytes(requestMessageLine);
	                socket2.close();
			
			}
		
        if(tokens[4].equals(global.server_ip)&&tokens[5].equals(String.valueOf(global.my_port))) {
        	Socket socket = new Socket(tokens[1], Integer.parseInt(tokens[2]));
        	//System.out.println("ahhh");
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
            outToServer.writeBytes("FILE NOT FOUND");
            socket.close();
        }else {
        	Socket socket = new Socket(global.server_to_connect, global.port);
        	//System.out.println("ahhh");
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
            outToServer.writeBytes(error);
            socket.close();
        }
        
        }catch(Exception e) {
        	
        }
        }
		
	}




class c implements Runnable{
	String requestMessageLine;
	String no_get_alarm="";
	public c(String requestMessageLine) {
		
		this.requestMessageLine=requestMessageLine;
	}
	@Override
	public void run() {
		 String[] tokens=requestMessageLine.split(" ");
		if(tokens[0].equals("GET")) {	
			try {
						if(global.index.containsKey(tokens[1])) {
							Socket socket = new Socket(tokens[2], Integer.parseInt(tokens[3]));
				        	//System.out.println("ahhh");
							DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
									
					                outToServer.writeBytes(global.index.get(tokens[1]));
					                socket.close();
							
							}else {
								
									no_get_alarm="searching_for_get"+" "+tokens[2]+" "+tokens[3]+" "+tokens[1]+" "+global.server_ip +" "+global.my_port;
					            	//System.out.println("my life sucks!");
					            	Socket socket = new Socket(global.server_to_connect, global.port);
					            	//System.out.println("ahhh");
					    			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
					                outToServer.writeBytes(no_get_alarm);
					                socket.close();
									
										
								
							}
						
				}catch(Exception e) {
				
				}
			}else {
				String new_mes="";
				int i;
				int flag=0;
				for(i=0;i<requestMessageLine.length();i++) {
					if(requestMessageLine.charAt(i)==' ') {
						if(flag==0)flag=1;
						else break;
					}
				}
				new_mes=requestMessageLine.substring(i+1);
				if(!(new_mes.equals(global.index.get(tokens[1])))) {
					try {
						global.index.put(tokens[1], new_mes);
						Socket socket = new Socket(global.server_to_connect, global.port);
		            	//System.out.println("ahhh");
		    			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
		                outToServer.writeBytes(requestMessageLine);
		                socket.close();
					}catch(Exception e) {
						
					}
				}
			}
	}
}







class r implements Runnable{
	
	
public r(String server_to_connect,String server_ip,int port,int my_port) {
	global.server_ip=server_ip;
	global.server_to_connect=server_to_connect;
	global.port=port;
	global.my_port=my_port;
	
}
	@Override
	public void run() {
		String requestMessageLine;
		while(true) {
			try {
				ServerSocket listen_socket = new ServerSocket(global.my_port);
	            Socket connectionSocket = listen_socket.accept();

	            BufferedReader inFromClient =new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
	        	requestMessageLine = inFromClient.readLine();
	        	listen_socket.close();
	        	//System.out.println("from the other side:"+requestMessageLine);
	            String[] tokens=requestMessageLine.split(" ");
	            //System.out.println("HEYY");
	            //System.out.println("IP:"+tokens[0]+"port:"+tokens[1]);
	           // System.out.println("IP off:"+server_ip+"port off:"+my_port);
	         client_searching:{
	            if(tokens[0].equals(global.server_ip)&&tokens[1].equals(String.valueOf(global.my_port))) {
	            	//System.out.println("i m here");
	            	System.out.println("server status");
	            	System.out.println(tokens.length);
	            	for(int i=0;i<tokens.length;i++)
	            		System.out.println(tokens[i]);
	            	System.out.println();
	            	global.last_mes=requestMessageLine;
	                break client_searching;
	            }
	            if((tokens[0].equals("searching_for_get")))  {
	            	Thread client_error=new Thread(new client_error(requestMessageLine));
	            	client_error.start();
		            	
		            	break client_searching;
		            }
	            if((tokens[0].equals("GET")||tokens[0].equals("PUT")))  {
	            	
	            	Thread client=new Thread(new c(requestMessageLine));
	            	client.start();
	            	break client_searching;
	            }
	           /* for(int i=0;i<tokens.length;i++) 
	            	System.out.println(tokens[i]);*/
	            for(int i=0;i<tokens.length;i+=4) {
	            	//System.out.println(tokens.length+" "+i);
	            	//System.out.println("XD");
	            	//System.out.println(tokens[i+3]);
	            	//System.out.println(global.port);
	            if(tokens[i+3].equals(Integer.toString(global.port))) {
	            	//System.out.println("i am here");
	            	global.server_to_connect=tokens[i];
	            	global.port=Integer.parseInt(tokens[i+1]);
	            	requestMessageLine=requestMessageLine+" "+global.server_ip+" "+global.my_port+" "+global.server_to_connect+" "+global.port;
	            	//System.out.println("my life sucks!");
	            	Socket socket = new Socket(global.server_to_connect, global.port);
	            	//System.out.println("ahhh");
	    			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
	                outToServer.writeBytes(requestMessageLine);
	                socket.close();
	                
	                break client_searching;
	            }
	            }
	            requestMessageLine=requestMessageLine+" "+global.server_ip+" "+global.my_port+" "+global.server_to_connect+" "+global.port;
            	//System.out.println("my life sucks!");
            	Socket socket = new Socket(global.server_to_connect, global.port);
            	//System.out.println("ahhh");
    			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
                outToServer.writeBytes(requestMessageLine);
                socket.close();
			}
	            
	            
	}catch(Exception e) {
		//System.out.println("Error2");
			}
		}
	}
}


class s implements Runnable{
	
	
public s(String server_to_connect,String server_ip,int port,int my_port) {
	global.server_ip=server_ip;
	global.server_to_connect=server_to_connect;
	global.port=port;
	global.my_port=my_port;
	
}
	@Override
	public void run() {	
		while(true) {
		try {
			
			Socket socket = new Socket(global.server_to_connect, global.port);
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
			String starting_mes=global.server_ip+" "+global.my_port+" "+global.server_to_connect+" "+global.port;
			global.last_mes=starting_mes;
            outToServer.writeBytes(starting_mes);
            socket.close();
           // System.out.println("my port:"+global.my_port+"portto:"+global.port);
				
	}catch(Exception e) {
			if(global.last_mes.isEmpty()) {
				System.out.println("Searching.....");
			}else {
				try {
				String[] tokens=global.last_mes.split(" ");
				
				for(int i=0;i<tokens.length;i+=4) {
	            	//System.out.println(tokens.length+" "+i);
	            	//System.out.println("XD");
	            	//System.out.println(tokens[i+3]);
	            	//System.out.println(global.port);
	            if(tokens[i+3].equals(Integer.toString(global.port))) {
	            	//System.out.println("i am here");
	            	global.server_to_connect=tokens[i+6];
	            	global.port=Integer.parseInt(tokens[i+7]);
	            	global.last_mes=global.server_ip+" "+global.my_port+" "+global.server_to_connect+" "+global.port;
	            	//System.out.println("my life sucks!");
	            	Socket socket = new Socket(global.server_to_connect, global.port);
	            	//System.out.println("ahhh");
	    			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream()); 
	                outToServer.writeBytes(global.last_mes);
	                socket.close();
	                
	            }
	            }
				
				}catch(Exception e1) {
				}
			}
		}
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	}
}



public class server4 {
public static void main(String argv[]) throws Exception  {
    	
		String server_ip,server_to_connect;
		int port;
		int my_port;
		global.last_mes="";
		InetAddress ip=InetAddress.getLocalHost();
		server_ip=ip.getHostAddress().trim();
		Scanner scanner = new Scanner(System. in);
		server_to_connect = scanner. nextLine();
        port=scanner. nextInt();
        my_port=scanner. nextInt();
	
	Thread send=new Thread(new s(server_to_connect,server_ip,port,my_port));
	Thread receive=new Thread(new r(server_to_connect,server_ip,port,my_port));
	send.start();
	receive.start();
  

  }



}
