package CourseFinale;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Server extends Application {

	private static final int EXIT = -1;
	private static final int START_GAME = 0;
	private static final int GET_SCORE = 1;
	private static final int CHANGE_NAME = 2;
	private static final int CLOSE_USER = 3;
	private static final int ACK = 4;
	private static final int GET_LEVEL_AND_MODE = 5;
	private static final int QUIT_FROM_GAME = 6;
	private static final int TIMER = 7;

	private int request;

	private ServerSocket primaryServerSocket;
	private Socket priamrySocket;
	private ServerSocket listenerServerSocket;
	private Socket listenerSocket;

	private Stage primaryStage;
	private Scene scene;
	private BorderPane pane;
	private Button btCommit;
	private ComboBox<String> cbUserList;
	private ComboBox<String> cbQueryList;
	private Button btExit;
	private TextArea taQuery;
	private TextArea taLog;
	private int clientNo = 0; // Number a client

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Create components
		Label lblTitle = new Label("Server side - Gun3D");
		Label lblUser = new Label("Choose user from the list:");
		Label lblQuery = new Label("Choose query from the list:");
		Label lblServerLog = new Label("Server log:");
		Label lblAdminRequest = new Label("Admin request:");
		btCommit = new Button("Commit");
		btExit = new Button("Exit");
		cbUserList = new ComboBox<>();
		cbQueryList = new ComboBox<>();
		taLog = new TextArea();
		taQuery = new TextArea();

		// Components properties
		taLog.setEditable(false);
		taLog.setMaxWidth(600);
		taQuery.setEditable(false);
		taQuery.setMaxWidth(380);
		lblTitle.setStyle("-fx-font: 30 arial;");
		lblServerLog.setStyle("-fx-font: 18 arial;");
		lblAdminRequest.setStyle("-fx-font: 18 arial;");
		btExit.setStyle("-fx-font: 18 arial;-fx-base: red;");

		// create panes and order components in it
		VBox vbControl = new VBox(10);
		vbControl.getChildren().addAll(lblUser, cbUserList, lblQuery, cbQueryList, btCommit);
		vbControl.setAlignment(Pos.CENTER);
		vbControl.setStyle("-fx-font: 18 arial;");

		HBox hbcontrol = new HBox(10);
		hbcontrol.getChildren().addAll(vbControl, taQuery);
		hbcontrol.setAlignment(Pos.CENTER);

		VBox vbMainControl = new VBox(10);
		vbMainControl.getChildren().addAll(lblAdminRequest, hbcontrol, lblServerLog, taLog, btExit);
		vbMainControl.setAlignment(Pos.CENTER);

		// Create main pane
		this.pane = new BorderPane();
		this.pane.setTop(lblTitle);
		this.pane.setCenter(vbMainControl);
		BorderPane.setAlignment(lblTitle, Pos.CENTER);
		lblTitle.setPadding(new Insets(20));

		// Create main scene
		this.scene = new Scene(pane, 700, 600);
		primaryStage.setTitle("Final exercise - server"); // Set the stage title
		primaryStage.setScene(this.scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});

		// actions
		btExit.setOnAction(e -> {
			Platform.exit();
			System.exit(0);
		});
		new Thread(() -> {
			try { // Create a server socket
				primaryServerSocket = new ServerSocket(8000);
				listenerServerSocket = new ServerSocket(8001);
				Platform.runLater(() -> {
					taLog.appendText("MultiThreadServer started at " + new Date() + '\n');
				});
				while (true) { // Listen for a new connection request
					priamrySocket = primaryServerSocket.accept();
					listenerSocket = listenerServerSocket.accept();
					// Increment clientNo
					clientNo++;
					Platform.runLater(() -> { // Display the client number
						taLog.appendText("Starting thread for client(" + clientNo + ") at " + new Date() + '\n');
						// Find the client's host name, and IP address
						InetAddress inetAddress = priamrySocket.getInetAddress();
						taLog.appendText("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
						taLog.appendText(
								"Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");
					});
					// Create and start a new thread for the connection
					new Thread(new HandleAClient(priamrySocket, clientNo)).start();
				}
			} catch (IOException ex) {
			}
		}).start();
	}

	// Define the thread class for handling new connection
	class HandleAClient implements Runnable {
		private Socket socket; // A connected socket
		private int clientNo;
		private int currentScore;
		private String currentLevel;
		private String currentMode;
		private Date currentime;
		private String userName;
		private DataInputStream inputFromClient;
		private DataOutputStream outputToClient;
		private Duration timer;

		// Construct a thread clientNo
		public HandleAClient(Socket socket, int clientNo) {
			this.socket = socket;
			this.clientNo = clientNo;
			new Thread(new HandleAClientListener(listenerSocket, clientNo)).start();
		}

		// Run a thread
		@SuppressWarnings("unused")
		public void run() {
			try { // Create data input and output streams
				inputFromClient = new DataInputStream(socket.getInputStream());
				outputToClient = new DataOutputStream(socket.getOutputStream());
				Platform.runLater(() -> {
					taLog.appendText(
							"Waiting from client(" + this.clientNo + ") to send a new user name to aprove...\n");
				});
				while (true) {
					// get from the client potential user name
					userName = inputFromClient.readUTF();
					if (isNameInDataBase(userName)) {
						// Send client boolean answer
						outputToClient.writeBoolean(true);
						Platform.runLater(() -> {
							taLog.appendText("Client(" + this.clientNo + "): the name "+userName+" is available ...signing in\n");
						});
						break;
					} else {
						Platform.runLater(() -> {
							taLog.appendText(
									"Client(" + this.clientNo + "):  the name  is occupied. need to change the name\n");
						});
						// Send client number to the client
						outputToClient.writeBoolean(false);
					}
				}

				// TODO sign in name in data base
				StartGame();

			} catch (SocketException ex) {
				try {
					primaryServerSocket.close();
				} catch (IOException e) {
				}
			} catch (IOException ex) {
			}
		}

		// Define the thread class for handling new connection
		class HandleAClientListener implements Runnable {
			private Socket socket2; // A connected socket for client to server
			private int clientNo;

			// Construct a thread clientNo
			public HandleAClientListener(Socket socket2, int clientNo) {
				this.socket2 = socket2;
				this.clientNo = clientNo;
			}

			@Override
			public void run() {

				try {
					DataInputStream inputFromClient = new DataInputStream(socket2.getInputStream());
					Platform.runLater(() -> {
						taLog.appendText("SocketListener for client(" + clientNo + ") is ready\n");
					});

					while (true) {
						int request = inputFromClient.readInt();

						switch (request) {
						case EXIT:
							// TODO method clean the user from the system
							Platform.runLater(() -> {
								taLog.appendText("client(" + clientNo + ") was exit!!!!");
							});
							break;
						case GET_LEVEL_AND_MODE:
							currentLevel = inputFromClient.readUTF();
							currentMode = inputFromClient.readUTF();
							Platform.runLater(() -> {
								taLog.appendText("client(" + clientNo + ") level is " + currentLevel + " in "
										+ currentMode + " mode\n");
							});
							break;
						case START_GAME:
							StartGame();
							break;
						case QUIT_FROM_GAME:
							// TODO stop time
							// save final result
							Platform.runLater(() -> {
								taLog.appendText("client(" + clientNo + ") finished the game "
										+ "final score is:" + currentScore + "\n");
							});
							break;
						case GET_SCORE:
							currentScore = inputFromClient.readInt();
							Platform.runLater(() -> {
								taLog.appendText("client(" + clientNo + ") new score is: " + currentScore + "\n");
							});
							break;
						default:
							break;

						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private void StartGame() {
			currentScore = 0;
			try {
				// Start game
				outputToClient.flush();
				outputToClient.writeInt(GET_LEVEL_AND_MODE);
				outputToClient.flush();
				outputToClient.writeInt(START_GAME);
				currentime = new Date();
				Platform.runLater(() -> {
					taLog.appendText("client(" + clientNo + ") started new game at "
							+ currentime.toString()+"\n");
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	private boolean isNameInDataBase(String name) {
		// TODO
		return true;
	}

}
