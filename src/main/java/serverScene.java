import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class serverScene implements Initializable {

    //    public static ChoiceBox<Object> listItems;
    public ListView listItems;
    public Server serverConnection;
    ArrayList<Integer> clientArr ;

    public serverScene() {
        clientArr = new ArrayList<>();
    }


//    public void setServerConnection( Server connect){
//        serverConnection=connect;
//    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
