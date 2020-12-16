
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application implements Initializable {

	 Server serverConnection;
	Client newClientConnect;
	serverScene serverControl;

	ArrayList<Integer> clientArr;
	static Stage primaryStage2;

	public GuiServer() throws IOException {
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub

		Parent root = FXMLLoader.load(getClass().getResource("GuiServer.fxml"));
		primaryStage.setTitle("Welcome to Chat App!");

		Scene scene = new Scene(root, 550,550);
		primaryStage.setScene(scene);
		primaryStage2 = primaryStage;

//		message = new TextField();
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});
		primaryStage.show();
	}

	public void changeToServerScene(ActionEvent actionEvent) throws IOException {

		FXMLLoader newRoot = new FXMLLoader(getClass().getResource("Server.fxml"));

		Parent root = newRoot.load();
		 serverControl =newRoot.getController();

		serverConnection = new Server(data->{
			Platform.runLater(()->{
				serverControl.listItems.getItems().add(data.toString());

			});
		}, data2 -> {

			});
//
//		});
//		serverControl.setServerConnection(serverConnection);
//		clientScene.setServerControl(serverControl);

		primaryStage2.setTitle("Welcome to the Server!");
		Scene scene = new Scene(root);

		primaryStage2.setScene(scene);
		primaryStage2.show();
	}


	public void changeToClientScene(ActionEvent actionEvent) throws IOException {
		FXMLLoader newRoot = new FXMLLoader(getClass().getResource("Client.fxml"));

		Parent root = newRoot.load();
		clientScene clientControl =newRoot.getController();


		newClientConnect = new Client(data->{
			Platform.runLater(()->{
//
				String sanitized = data.toString();
				if(sanitized.contains("Online:")) {
					sanitized = sanitized.substring(0, sanitized.indexOf("Online:"));
				}

				clientControl.listItems2.getItems().add(sanitized);

				final String substring = data.toString().substring(0,1);
				final String substring2 =Character.toString(data.toString().charAt(1));
				System.out.println(substring2);

				clientControl.onlineClientListView.getItems().clear();

				// manipulate string accepted to get the online clients
				String online = data.toString();
	   			 online = online.substring(online.indexOf("[")+1);
	   			 online = online.substring(0,online.indexOf("]"));

				clientControl.onlineClientListView.getItems().add(online);

				clientControl.selectClients.getItems().clear();

				online = online.replaceAll(",","");

				for(int i = 0 ;i < online.length() ; ++i)
				{
					CheckMenuItem menuItem = new CheckMenuItem(Character.toString(online.charAt(i)));
					clientControl.selectClients.getItems().add(menuItem);
				}


				clientControl.selectClients.getItems().stream().forEach((MenuItem menuItem) -> menuItem.setOnAction(e -> {
					clientControl.sendToClients = clientControl.selectClients.getItems().stream()
							.filter(item -> CheckMenuItem.class.isInstance(item) && CheckMenuItem.class.cast(item).isSelected())
							.map(MenuItem::getText)
							.collect(Collectors.toList());
				}));

			});
		},data2->{
			Platform.runLater(()->{

			});
//
		});
		newClientConnect.start();

		clientControl.setClientConnection( newClientConnect);

		primaryStage2.setTitle("Welcome to the Client!");
		Scene newScene = new Scene(root);
		primaryStage2.setScene(newScene);
		primaryStage2.show();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		clientArr = new ArrayList<>();

	}
}
