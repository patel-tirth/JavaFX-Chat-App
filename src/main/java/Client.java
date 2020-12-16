import javax.management.remote.JMXServerErrorException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;



public class Client extends Thread{
	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	Server serverConnection;
	clientScene scene;
	
	private Consumer<Serializable> callback;
	private Consumer<String> callback2;

	ArrayList<Server.ClientThread> clientArrs = new ArrayList<>();;
	
	Client(Consumer<Serializable> call, Consumer<String> call2){
//		clientArrs.addAll(Server.clients);

		callback = call;
		callback2 = call2;

	}
	public void run() {
		
		try {
		socketClient= new Socket("127.0.0.1",5555);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}

		while(true) {
			try {
			String message =  in.readObject().toString();

//			ArrayList<Server.ClientThread> receive = (ArrayList<Server.ClientThread>) in.readObject();
				callback.accept(message);
//				callback2.accept( message.substring(message.length() - 1));
//				Strmessage.charAt(message.length() - 1);
//			scene.updateClients(receive);
//				System.out.println(receive.size());
			}
			catch(Exception e) {}
		}
	
    }
	
	public void send(ChatData data) {
		
		try {
			out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
