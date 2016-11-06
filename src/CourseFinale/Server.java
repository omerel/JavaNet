package CourseFinale;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
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

	private ServerSocket primaryServerSocket;
	private Socket primarySocket;
	private ServerSocket listenerServerSocket;
	private Socket listenerSocket;

	private Stage primaryStage;
	private Scene scene;
	private BorderPane pane;
	private Button btCommit;
	private ComboBox<KeyValPair> cbUserList;
	private ComboBox<String> cbQueryList;
	private Button btExit;
	private TextArea taQuery;
	private TextArea taLog;
	private TableView tvQuery;
	private int clientNo = 0; // Number a client
	
	// DB vars and consts 
	private final static int SCORES_ASC = 0;
	private final static int SCORES_DESC = 1;
	private final static int ALL_GAMES = 2;
	private final static int RANKS = 3;
	private final static int ALL_PLAYERS = -1;
	
	private Connection	connection;
	private Statement	statement;

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
		tvQuery = new TableView();

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
				try {
					if (connection != null && !connection.isClosed())
						connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Platform.exit();
				System.exit(0);
			}
		});

		// actions
		btExit.setOnAction(e -> {
			try {
				if (connection != null && !connection.isClosed())
					connection.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			Platform.exit();
			System.exit(0);
		});
		
		btCommit.setOnAction(e -> {
			showContents(cbQueryList.getSelectionModel().getSelectedIndex());
		});
		new Thread(() -> {
			try { // Create a server socket
				primaryServerSocket = new ServerSocket(8000);
				listenerServerSocket = new ServerSocket(8001);
				Platform.runLater(() -> {
					taLog.appendText("MultiThreadServer started at " + new Date() + '\n');
				});
				
				initializeDB();
				
				while (true) { // Listen for a new connection request
					primarySocket = primaryServerSocket.accept();
					listenerSocket = listenerServerSocket.accept();
					// Increment clientNo
					clientNo++;
					Platform.runLater(() -> { // Display the client number
						taLog.appendText("Starting thread for client(" + clientNo + ") at " + new Date() + '\n');
						// Find the client's host name, and IP address
						InetAddress inetAddress = primarySocket.getInetAddress();
						taLog.appendText("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + "\n");
						taLog.appendText(
								"Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + "\n");
					});
					// Create and start a new thread for the connection
					new Thread(new HandleAClient(primarySocket, clientNo)).start();
				}
			} catch (IOException ex) {
			}
		}).start();
	}

	// Define the thread class for handling new connection
	class HandleAClient implements Runnable {
		private Socket socket; // A connected socket
		private int clientNo;
		private int currentGame;
		private int currentScore;
		private String currentLevel;
		private String currentMode;
		private String userName;
		private int userId;
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
					if (!isNameInDataBase(userName)) {
						userId = addPlayer(userName);
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
							updateScore(currentGame, currentScore);
							addEvent(currentGame, "PLAYER LEFT GAME");
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
								// Update score in DB
								updateScore(currentGame, currentScore);
								addEvent(currentGame, "GAME FINISHED");
								taLog.appendText("client(" + clientNo + ") finished the game "
										+ "final score is:" + currentScore + "\n");
							});
							break;
						case GET_SCORE:
							currentScore = inputFromClient.readInt();
							Platform.runLater(() -> {
								taLog.appendText("client(" + clientNo + ") new score is: " + currentScore + "\n");
								addEvent(currentGame, "NEW HIT");
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
				Platform.runLater(() -> {
					taLog.appendText("client(" + clientNo + ") started new game at "
							+ new Date().toString()+"\n");
					currentGame = addGame(currentScore, currentLevel, currentMode, userId);
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class KeyValPair {

		private final int key;
		private final String value;
		
		public KeyValPair(int key, String value) {
			this.key = key;
			this.value = value;
			}
		
		public int getKey()   {    return key;    }
		
		public String toString() {    return value;  }
	}
	
	private void initializeDB()
	  { try
	    { // Load the JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");
	      System.out.println("Driver loaded");
	      // Establish a connection
	      connection = DriverManager.getConnection
	        ("jdbc:mysql://localhost/gun", "scott", "tiger");
	      System.out.println("Database connected");
	      
	      populatePlayersList(cbUserList);
	      populateQueriesList();
	    }
	    catch (Exception ex)
	    { ex.printStackTrace();
	    }
	  }
	
	private boolean isNameInDataBase(String name) {
		ResultSet rs = null;
		try {
			statement = connection.createStatement();
			rs = statement.executeQuery(
						"SELECT name"
					+ " FROM Players"
					+ " WHERE name = '" + name + "'");

			return (rs != null && rs.first());
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void populatePlayersList(ComboBox<KeyValPair> cbUserList) {
		// Add empty cell for all players
		cbUserList.getItems().add(new KeyValPair(ALL_PLAYERS, "ALL"));
	      try {
	    	  statement = connection.createStatement();
		
			  ResultSet rsPlayers = statement.executeQuery("SELECT id, name FROM Players");
			  while (rsPlayers.next())
			  { 
				  KeyValPair player = new KeyValPair(rsPlayers.getInt("id"), rsPlayers.getString("name"));
				  cbUserList.getItems().add(player);
			  }
			  //cbUserList.getSelectionModel().selectFirst();
	      } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void populateQueriesList() {
	      cbQueryList.getItems().add(SCORES_ASC, "User games");
	      cbQueryList.getItems().add(SCORES_DESC, "User games scores desc");
	      cbQueryList.getItems().add(ALL_GAMES, "All games scores desc");
	      cbQueryList.getItems().add(RANKS, "Players ranks");
	      }
	
	  private void showContents(int query) {
		  try {
			  String queryString = "";
			  switch (query) {
				  case SCORES_ASC: {
					  queryString = "SELECT g.id Game, g.Level Level, g.startTime 'Start Time', et.eventType Event, e.eventTime 'Event Time', g.score Score"
					  		+ " FROM Games AS g, Events AS e, EventTypes AS et"
					  		+ " WHERE e.game = g.id AND e.eventType = et.id"
					  		+ " ORDER BY g.player, g.startTime, g.score, e.eventTime";
					  //statement.setInt(1, cbUserList.getSelectionModel().getSelectedItem().getKey());
					  break;
				  }
				  case SCORES_DESC: {
					  queryString = "SELECT g.id Game, g.Level Level, g.startTime 'Start Time', et.eventType Event, e.eventTime 'Event Time', g.score Score"
						  		+ " FROM Games AS g, Events AS e, EventTypes AS et"
						  		+ " WHERE e.game = g.id AND e.eventType = et.id"
						  		+ " ORDER BY g.player, g.score DESC, g.startTime, e.eventTime";
					  break;
				  }
				  case ALL_GAMES: {
					  queryString = "SELECT p.name Player, g.id Game, g.level Level, g.startTime 'Start Time', g.score Score"
						  		+ " FROM Player As p, Games AS g, Events AS e, EventTypes AS et"
						  		+ " WHERE p.id = g.player"
						  		+ " ORDER BY g.score DESC"
						  		+ " GROUP BY p.name";
					  break;
				  }
				  case RANKS: {
					  queryString =	  "SELECT p.name Player,"
							  		+ " AVG(SELECT score"
							  		+ "		FROM Games"
							  		+ "		WHERE p.id = player"
							  		+ "		ORDER BY score DESC"
							  		+ "		LIMIT 3) Rank"
								  	+ " FROM Player As p, Games AS g"
								  	+ " WHERE p.id = g.player"
								  	+ " AND COUNT(SELECT id FROM Games WHERE id = p.id) > 3";
						  		//+ " ORDER BY g.score DESC"
						  		//+ " GROUP BY p.name";
					  break;
				  }
				  default:
					  break;
			  }
		  
			  statement = connection.createStatement();
			  ResultSet resultSet = statement.executeQuery(queryString);
		      populateTableView(resultSet, tvQuery);
		    } 
		    catch (SQLException ex)
		    { ex.printStackTrace();
		    }
	  }
	  @SuppressWarnings({ "unchecked", "rawtypes" })
	  private void populateTableView(ResultSet rs, TableView tableView)
	  { 
		  tableView.getItems().clear();
		  tableView.getColumns().clear();
		  tableView.getColumns().clear();
		ObservableList<ObservableList> data = 
	      FXCollections.observableArrayList();
	    try
	    {
	    	// Add column names
	        for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
	            //We are using non property style for making dynamic table
	            final int j = i; 
	            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
	            col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
	                public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
	                	if (param.getValue().get(j) != null)
	                		return new SimpleStringProperty(param.getValue().get(j).toString());
	                	else
	                		return new SimpleStringProperty("");
	                }
	            });
	            tableView.getColumns().addAll(col);
	        }
	        
	        // Add data to ObservableList 
	        while(rs.next()){
	            //Iterate Row
	            ObservableList<String> row = FXCollections.observableArrayList();
	            for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
	                //Iterate Column
	                row.add(rs.getString(i));
	            }
	            data.add(row);
	        }
	        // Add to TableView
	        tableView.setItems(data);
	    } 
	    catch (Exception e)
	    { e.printStackTrace();
	      System.out.println("Error on Building Data");
	    }
	  }
	  
	  private void addEvent(int game, String type) {
		  try {
			  statement = connection.createStatement();
			  
			  statement.execute("INSERT INTO Events"
			  		+ "			VALUES("+ game + ", '" + type + "',  NOW())");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
	  
	  private int addGame(int score, String level, String mode, int player){
		  int id = 0;
		  try {
			  statement = connection.createStatement();
			  ResultSet rsId = statement.executeQuery("SELECT MAX(id) AS max_id FROM Games)");
			  if (rsId.first())
				  id = rsId.getInt("max_id") + 1;
			  
			  statement.execute("INSERT INTO Games"
			  		+ "			VALUES(" + id + ", " + player + ", '" + level + "', '"+ mode + "', NOW(), "+ score);
			  
			  addEvent(id, "START GAME");
			  return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	  }
	  
	  private int addPlayer(String name) {
		  int id = 0;
		  try {
			  statement = connection.createStatement();
			  ResultSet rsId = statement.executeQuery("SELECT MAX(id) AS max_id FROM Players)");
			  if (rsId.first())
				  id = rsId.getInt("max_id") + 1;
			  
			  statement.execute("INSERT INTO Players"
			  		+ "			VALUES(" + id + ", '" + name + "'");
			  
			  cbUserList.getItems().add(new KeyValPair(id,  name));
			  return id;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		  }
	  
	  private void updatePlayer(int id, String name) {
		  try {
			  statement = connection.createStatement();
			  statement.executeUpdate("UPDATE Players"
			  		+ "					SET name = '" + name + "'"
			  		+ "					WHERE id = " + id);
		} catch (SQLException e) {
			e.printStackTrace();
		}		  
	  }
	  
	  private void updateScore(int gameId, int score) {
		  try {
			  statement = connection.createStatement();
			  statement.executeUpdate("UPDATE Games"
			  		+ "					SET score = " + score
			  		+ "					WHERE id = " + gameId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }
	
	public static void main(String[] args) {
		launch(args);
	}
}
