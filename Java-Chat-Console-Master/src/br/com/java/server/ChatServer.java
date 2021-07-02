package br.com.java.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;

public class ChatServer {
	
	Vector<String> users = new Vector<String>();
	Vector<HandleClient> clients = new Vector<HandleClient>();
	
	public void process() throws Exception {
		ServerSocket server = new ServerSocket(9999,10);
		System.out.println("Servidor iniciado...");
		
		
		while (true) {
			
			Socket client = server.accept();
			
			HandleClient c = new HandleClient(client);
			clients.add(c);
		}
	}

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		new ChatServer().process();
	}
	
	public void broadcast(String user, String message) {
		for(HandleClient c : clients)
			
			if (! c.getUserName().equals(user)) {
				c.sendMessage(user, message);
			}
	}
	
	class HandleClient extends Thread {
		String name = "";
		BufferedReader input;
		PrintWriter output;
		
		public HandleClient(Socket client ) throws Exception {
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			output = new PrintWriter (client.getOutputStream(), true);
			
			name = input.readLine();
			
			users.add(name);
			
			start();
		}
		
		public void sendMessage(String uname, String msg) {
			output.println(uname + ":" + msg);
		}
		
		public String getUserName() {
			return name;
		}
		
		public void run() {
			String line;
			
			try {
				while (true) {
					line = input.readLine();
					
					if (line.equals("end")) {
						clients.remove(this);
						users.remove(name);
						
						break;
					}
					
					broadcast(name, line);
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
			}
		}
		
	}

}
