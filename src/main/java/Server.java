import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;


public class Server{

	int count = 1;
	public  ArrayList<ClientThread> clients = new ArrayList<>();
	public  ArrayList<Integer> onlineClients = new ArrayList<>();
	TheServer server;
	private Consumer<Serializable> callback;

	private Consumer<ArrayList<ClientThread>> callback2;


	Server(Consumer<Serializable> call,Consumer<ArrayList<ClientThread>> call2) {
//		clientList = clients;
		synchronized (this) {
			clients = new ArrayList<>();
			callback = call;
			callback2 = call2;
			server = new TheServer();
			server.start();
		}
	}

	public class clientMessage{

		String message;
		ArrayList<ClientThread> client;

	}
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				clients.add(c);
				onlineClients.clear();
				for(int i = 0 ; i < clients.size() ; ++i)
				{
					onlineClients.add(clients.get(i).count);
				}
				callback.accept(count + " has connected to server: " + "online: " + onlineClients.toString() );


				c.start();
//
				count++;

			    }
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}

		 class  ClientThread extends Thread{

			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			clientMessage wo=new clientMessage();

			ArrayList<clientMessage> woi=new ArrayList<>();
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}
			
			public void updateClients(String message,ArrayList<ClientThread> clients, int left) {

				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
						if(left ==1 )
						{
							t.out.writeObject(count + " left! Online: "+ onlineClients.toString());
						}
						else if(left == 2)
						{
							t.out.writeObject(message + " Online: " + onlineClients.toString());
						}
						else {
							t.out.writeObject(count + " said: " + message + " Online: " + onlineClients.toString());
						}

					}
					catch(Exception e) {}
				}
//				send(clients);
			}
			// for updating specific client/clients
			public void updateClients2(String message,ChatData receive, int sender) throws IOException {

				int leftIdx = 0;
				int rightIdx = 0 ;
				int i =0;
				// print the message to current client's chat window
				while(true)
				{
					if(clients.get(i).count == sender) {
						clients.get(i).out.writeObject(sender + " said: " + message + " Online: " + onlineClients.toString());
						break;
					}
					i++;
				}
				while(leftIdx < receive.clientNum.size() )
				{
					if (clients.get(rightIdx).count != receive.clientNum.get(leftIdx))
					{
						rightIdx++;
					}
					if (clients.get(rightIdx).count == receive.clientNum.get(leftIdx))
					{
						ClientThread t = clients.get(rightIdx);
						t.out.writeObject ( sender + " said: " + message +" Online: "+ onlineClients.toString() );
						leftIdx++;
					}
				}

			}


			public void run() {
				synchronized (this) {
					try {
						in = new ObjectInputStream(connection.getInputStream());
						out = new ObjectOutputStream(connection.getOutputStream());
						connection.setTcpNoDelay(true);
					} catch (Exception e) {
						System.out.println("Streams not open");
					}
					System.out.println("new client on server: client #" + count);
					updateClients(count + " on server",clients,2);

//					callback2.accept(clients);

					while (true) {
						try {
							ChatData receive = (ChatData) in.readObject();
								callback.accept("client: " + count + " sent: " + receive.message);
								if(!receive.sendAll) {
									updateClients2( receive.message, receive,count);
								}
								if(receive.sendAll)
								{
									updateClients( receive.message,clients,0);
								}
//

						} catch (Exception e) {
							callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");

							clients.remove(this);
							for(int i = 0 ; i < onlineClients.size() ; ++i)
							{
								if(onlineClients.get(i) == count){
									onlineClients.remove(i);
								}

							}
							updateClients( count + "left the server!" ,clients,1);

//							callback2.accept(count);
							break;
						}
					}
				}//end of run
			}
			public void send(ArrayList<ClientThread> data){
				try{ out.writeObject(data);}
				catch(IOException e) {e.printStackTrace();}
			}//end of ru
		}//end of client thread


}


	
	

	
