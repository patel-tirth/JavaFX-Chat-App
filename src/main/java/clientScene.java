import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class clientScene implements Initializable {


    public TextField message;
    public  MenuButton selectClients;
    public ListView listItems2;
    public CheckBox sendAll;
    public Label displayClientNum;
    public ListView onlineClientListView;
    ObservableList<Integer> onlineClients = FXCollections.observableArrayList();;


    Client clientConnection;
    List<String> sendToClients  ;
    public serverScene serverScene;
    public  ArrayList<Integer> clientArrs;
   public ObservableList<Integer> items = FXCollections.observableArrayList();
    public clientScene()
    {
        clientArrs = new ArrayList<>();
//        onlineClients = new ArrayList<>();
    }
    public void sendMessage(ActionEvent actionEvent) {

        ChatData c = new ChatData();
		c.message = message.getText();
//		System.out.print(message.getText());
		c.clientlist = sendToClients; // store the client numbers in client list to whom message is supposed to be sent
		// if specific client/clients are selected for sending message

		if(c.clientlist != null ) {
            // add client number to send message to
            for (int i = 0; i < c.clientlist.size(); ++i) {
                c.clientNum.add(Integer.parseInt(c.clientlist.get(i)));
            }
        }
		if(sendAll.isSelected())
            c.sendAll = true;

//		System.out.println(clientArrs.size());

		clientConnection.send(c);
		message.clear();

    }

    public void setClientConnection( Client connect){
        clientConnection=connect;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
