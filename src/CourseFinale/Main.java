package CourseFinale;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.vecmath.Color3f;

import CourseFinale.Server.HandleAClient;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Line;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Main extends Application {

	private int request;

	private Socket socket;
	private DataOutputStream toServer = null;
	private DataInputStream fromServer = null;
	private Socket socket2;
	private DataOutputStream toServer2 = null;

	private Stage primaryStage;
	private Scene scene;
	private BorderPane pane;
	private TextField tfUsername;
	private ToggleGroup tgGameMode;
	private RadioButton rbTrainig;
	private RadioButton rbGame;
	private ToggleGroup tgGameLevel;
	private ToggleButton btLevelGame1;
	private ToggleButton btLevelGame2;
	private ToggleButton btLevelGame3;
	private Button btStartGame;

	private String mUserName;
	private String mMode;
	private String mLevel;

	@Override
	public void start(Stage primaryStage) throws Exception {

		this.primaryStage = primaryStage;

		// Create components
		Label lblTitle = new Label("Final exercise - Gun3D");
		Label lblStudents = new Label("Barr Inbar & Omer Elgrably");

		Label lblUserName = new Label("Enter your name:");
		tfUsername = new TextField();

		tgGameMode = new ToggleGroup();
		rbTrainig = new RadioButton("Training");
		rbTrainig.setToggleGroup(tgGameMode);
		rbGame = new RadioButton("Game");
		rbGame.setToggleGroup(tgGameMode);
		Label lblLevel = new Label("Choose Level:");
		tgGameLevel = new ToggleGroup();
		btLevelGame1 = new ToggleButton(Common.BEGINNER);
		btLevelGame1.setToggleGroup(tgGameLevel);
		btLevelGame2 = new ToggleButton(Common.NORMAL);
		btLevelGame2.setToggleGroup(tgGameLevel);
		btLevelGame3 = new ToggleButton(Common.HARD);
		btLevelGame3.setToggleGroup(tgGameLevel);

		btStartGame = new Button("Start");
		Button btExit = new Button("Exit");

		// Components properties
		lblTitle.setStyle("-fx-font: 30 arial;");
		btStartGame.setStyle("-fx-base: green;");
		btExit.setStyle("-fx-base: red;");
		tfUsername.setMaxWidth(200);
		btLevelGame1.setPrefWidth(100);
		btLevelGame2.setPrefWidth(100);
		btLevelGame3.setPrefWidth(100);
		rbTrainig.setSelected(true);

		// create panes and order components in it
		BorderPane borderPaneTitle = new BorderPane();
		borderPaneTitle.setTop(lblTitle);
		borderPaneTitle.setBottom(lblStudents);
		BorderPane.setAlignment(lblTitle, Pos.CENTER);
		BorderPane.setAlignment(lblStudents, Pos.CENTER);
		borderPaneTitle.setPadding(new Insets(20));

		HBox hbMode = new HBox(10);
		hbMode.getChildren().addAll(rbTrainig, rbGame);
		hbMode.setAlignment(Pos.CENTER);

		VBox vbGame = new VBox(10);
		vbGame.getChildren().addAll(lblUserName, tfUsername, hbMode, lblLevel, btLevelGame1, btLevelGame2,
				btLevelGame3);
		vbGame.setAlignment(Pos.CENTER);
		vbGame.setStyle("-fx-font: 18 arial;");

		HBox hBoxButtons = new HBox(10);
		hBoxButtons.getChildren().addAll(btExit, btStartGame);
		hBoxButtons.setAlignment(Pos.CENTER);
		hBoxButtons.setPadding(new Insets(20));
		hBoxButtons.setStyle("-fx-font: 24 arial;");

		// Create main pane
		this.pane = new BorderPane();
		this.pane.setTop(borderPaneTitle);
		this.pane.setCenter(vbGame);
		this.pane.setBottom(hBoxButtons);

		// create Main Scene
		mainScene();
		connectToServer();

		// actions
		btExit.setOnAction(e -> {
			try {
				toServer2.flush();
				toServer2.writeInt(Common.EXIT);
				socket.close();
				socket2.close();
				Platform.exit();
				System.exit(0);
			} catch (IOException err) {
			}
		});

		btStartGame.setOnAction(e -> {
			if (checkValidation()) {
				if (checkNameWithServer()) {
					tfUsername.setEditable(false);
					// start dialog thread with server
					new Thread(new HandleServer()).start();
				} else
					alertDialog("Information Dialog", "Name is not available, please try other name");
			} else
				alertDialog("Information Dialog", "Please fill all the properties");
		});

	}

	public void mainScene() {
		this.scene = new Scene(pane, 500, 600);
		primaryStage.setTitle("Final exercise - Gun3D"); // Set the stage title
		primaryStage.setScene(this.scene); // Place the scene in the stage
		// primaryStage.setAlwaysOnTop(true);
		primaryStage.show(); // Display the stage
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				try {
					toServer2.flush();
					toServer2.writeInt(Common.EXIT);
					socket.close();
					socket2.close();
					Platform.exit();
					System.exit(0);
				} catch (IOException e) {
				}
			}
		});
	}

	private boolean checkValidation() {
		mUserName = tfUsername.getText().trim();
		if (mUserName.isEmpty())
			return false;
		ToggleButton tbGame = (ToggleButton) tgGameLevel.getSelectedToggle();
		if (tbGame == null)
			return false;
		else
			getCurrentLevel(tbGame);
		return true;
	}

	private void connectToServer() {
		try {
			socket = new Socket("localhost", 8000);
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
			socket2 = new Socket("localhost", 8001);
			toServer2 = new DataOutputStream(socket2.getOutputStream());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// if username is occupied return false, else sign in and return true
	private boolean checkNameWithServer() {
		try {
			toServer.flush();
			toServer.writeUTF(mUserName);
			return fromServer.readBoolean();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

	private void alertDialog(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	class HandleServer implements Runnable {

		Game game;

		public HandleServer() {
		};

		@Override
		public void run() {

			// Update start button
			btStartGame.setOnAction(e -> {
				if (checkValidation()) {
					try {
						toServer2.flush();
						toServer2.writeInt(Common.START_GAME);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else
					alertDialog("Information Dialog", "Please fill all the properties");
			});

			while (true) {
				try {
					request = fromServer.readInt();
					switch (request) {
					case Common.EXIT:
						break;
					case Common.START_GAME:
						Platform.runLater(() -> {
							game = new Game();
						});
						break;
					case Common.CHANGE_NAME:
						break;
					case Common.CLOSE_USER:
						// TODO finish game
						toServer2.writeInt(Common.EXIT);
						socket2.close();
						socket.close();
						Platform.exit();
						System.exit(0);
						break;
					case Common.GET_LEVEL_AND_MODE:
						toServer2.flush();
						toServer2.writeInt(Common.GET_LEVEL_AND_MODE);
						getCurrentLevel((ToggleButton) tgGameLevel.getSelectedToggle());
						getCurrentMode();
						toServer2.flush();
						toServer2.writeUTF(mLevel);
						toServer2.flush();
						toServer2.writeUTF(mMode);
						break;
					case Common.QUIT_FROM_GAME:
						Platform.runLater(() -> {
							game.quitGame();
						});
						break;
					case Common.TIMER:
						String time = fromServer.readUTF();
						break;
					default:
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getCurrentMode() {
		if (rbTrainig.isSelected()) {
			mMode = Common.TRAINING;
			return mMode;
		} else
			mMode = Common.GAME;
		return mMode;
	}

	private String getCurrentLevel(ToggleButton tbGame) {
		if (tbGame.equals(btLevelGame1)) {
			mLevel = Common.BEGINNER; // level 1
			return mLevel;
		} else if (tbGame.equals(btLevelGame2)) {
			mLevel = Common.NORMAL; // level 2
			return mLevel;
		} else {
			mLevel = Common.HARD; // level 3
		}
		return mLevel;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void updateScore(int score) {
		try {
			toServer2.flush();
			toServer2.writeInt(Common.GET_SCORE);
			toServer2.writeInt(score);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public class Game {

		final static int BALL_RADIUS = 5;
		final static int GUN_LENGTH = 30;
		final static int PANEL_WIDTH = 200;
		final static int PANEL_HEIGHT = 100;
		final static int CHANGE_ANGLE = 3;
		final Color[] arrayOfColors = { Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.INDIGO,
				Color.PURPLE, Color.CHOCOLATE, Color.GRAY };
		
		private int balloonRadious = 50;
		int angle = 90;
		final static float DELAY = (float) 0.5;
		Pane pane = new Pane();
		Cylinder gun;
		int score = 0;
		Stage stage;

		class SmallBall extends Sphere {
			private int length;
			private int angle;

			SmallBall(int length, int angle) {
				super(BALL_RADIUS);
				this.length = length;
				this.angle = angle;
				refresh();
			}

			public void addLength(int value) {
				this.length += value;
			}

			public void refresh() {
				this.setLayoutX(
						(int) (this.length * 0.1 * Math.cos(Math.toRadians(this.angle)) + (pane.getWidth() / 2)));
				this.setLayoutY((int) (pane.getHeight() - this.length * 0.1 * Math.sin(Math.toRadians(this.angle))));
			}

			public boolean isOutOfPane() {
				return (this.getLayoutX() > pane.getWidth() || this.getLayoutY() < 0 || this.getLayoutX() < 0);
			}
		}

		public Game() {
			primaryStage.hide();
			stage = new Stage();
			stage.setTitle("Shooting Game V1.0");
			Scene scene = new Scene(pane, 700, 500);
			Label txTimer = new Label();
			txTimer.setLayoutX(20);
			txTimer.setLayoutY(20);
			txTimer.setStyle("-fx-font: 18 arial;-fx-text-fill: white;");
			Label text = new Label();
			text.setLayoutX(5);
			text.setLayoutY(350);
			text.setStyle("-fx-font: 18 arial;-fx-text-fill: white;");
			Button btQuitFromGame = new Button("Quit");
			btQuitFromGame.setStyle("-fx-font: 18 arial;-fx-base: red;");
			btQuitFromGame.setLayoutX(5);
			btQuitFromGame.setLayoutY(450);
			Timeline timer;
			timer = new Timeline(new KeyFrame(new Duration(1000*120), ae -> quitGame()));
			timer.play();
			txTimer.setText(timer.toString());
			
			ImageView imv = new ImageView();
	        Image image = new Image(Main.class.getResourceAsStream("stars.jpg"));
	        imv.setImage(image);
	        imv.setFitWidth(700);
	        imv.setFitHeight(500);
	        
	        pane.getChildren().add(0, imv);
	        pane.getChildren().add(1, getNewBalloon());
			pane.getChildren().add(2, text);
			pane.getChildren().add(3, btQuitFromGame);
			pane.getChildren().add(4, txTimer);
	        
			
			gun = new Cylinder(20, 100);
			gun.layoutXProperty().bind(pane.widthProperty().divide(2));
			gun.layoutYProperty().bind(pane.heightProperty());
			PhongMaterial material = new PhongMaterial();
		    material.setDiffuseColor(Color.GOLD);
		    material.setSpecularColor(Color.GRAY);
			gun.setMaterial(material);
			
			game();

			btQuitFromGame.setOnAction(e -> {
				stage.close();
				primaryStage.show();
				try {
					toServer2.flush();
					toServer2.writeInt(Common.QUIT_FROM_GAME);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			pane.getChildren().addAll(gun);
			setANDrequestFocus();
			pane.setOnKeyPressed(e -> {
				if (e.getCode() == KeyCode.LEFT) {
					if (angle < 180)
						angle += CHANGE_ANGLE;
					game();
					setANDrequestFocus();
				} else if (e.getCode() == KeyCode.RIGHT) {
					if (angle > 0)
						angle -= CHANGE_ANGLE;
					game();
					setANDrequestFocus();
				} else if (e.getCode() == KeyCode.UP) {
					pane.getChildren().add(new SmallBall(GUN_LENGTH, angle));
				}
			});
			EventHandler<ActionEvent> eventHandler = e -> {
				game();
				setANDrequestFocus();
				txTimer.setText( "Time left: "+Integer.toString(120 - (int)timer.getCurrentTime().toSeconds()));
			};
			Timeline animation = new Timeline(new KeyFrame(Duration.millis(DELAY), eventHandler));
			animation.setCycleCount(Timeline.INDEFINITE);
			stage.setScene(scene);
			stage.setResizable(true);
			stage.setTitle("GUN3D");
			stage.show();
			animation.play();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent event) {
					// TODO
				}
			});

		}

		public void quitGame() {
			stage.close();
			primaryStage.show();
			try {
				toServer2.flush();
				toServer2.writeInt(Common.QUIT_FROM_GAME);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		private void game() {
			
			((Label) (pane.getChildren().get(2)))
					.setText("User name: " + mUserName + "\nMode: " + mMode + "\nLevel: " + mLevel + "\n");
			((Sphere) (pane.getChildren().get(1))).setRadius(balloonRadious);
			gun.setRotate(90-angle);
			for (int i = 0; i < pane.getChildren().size(); i++) {
				if (pane.getChildren().get(i) instanceof SmallBall) {
					if (!((SmallBall) (pane.getChildren().get(i))).isOutOfPane()) {
						((SmallBall) (pane.getChildren().get(i))).addLength(1);
						((SmallBall) (pane.getChildren().get(i))).refresh();
					} else {
						pane.getChildren().remove(i);
					}
					try {
						if (overlaps((Sphere) pane.getChildren().get(i), (Sphere) pane.getChildren().get(1))) {
							hit(i);
						}
					} catch (IndexOutOfBoundsException e) {
					}
				}
			}
		}

		public int getRand(int a, int b) {
			return (int) (Math.random() * ((b - a) + 1) + a);
		}

		private void setANDrequestFocus() {
			pane.setFocusTraversable(true);
			pane.requestFocus();
		}

		public Sphere getNewBalloon() {
			
			int colorIndex;
			PhongMaterial material;
			Sphere mySphere;
			
			switch (mLevel){
			case Common.BEGINNER:
				balloonRadious = 50;
				break;
			case Common.NORMAL:
				balloonRadious = 30;
				break;
			case Common.HARD:
				balloonRadious = 10;
				break;
				
			}
			mySphere = new Sphere(balloonRadious);
			mySphere.setLayoutX(getRand(balloonRadious, (int) (pane.getWidth() - balloonRadious)));
			mySphere.setLayoutY(getRand(balloonRadious, (int) (pane.getHeight() * 0.3)));
			
			colorIndex = (int) ((arrayOfColors.length) * Math.random());
			material = new PhongMaterial();
		    material.setDiffuseColor(arrayOfColors[colorIndex]);
		    material.setSpecularColor(arrayOfColors[colorIndex]);
		    
			mySphere.setMaterial(material);
			return mySphere;
		}

		public boolean overlaps(Sphere s1, Sphere s2) {
			return Math.sqrt(Math.pow((s1.getLayoutX() - s2.getLayoutX()), 2)
					+ Math.pow((s1.getLayoutY()- s2.getLayoutY()), 2)) <= (s1.getRadius() + s2.getRadius());
		}

		public void hit(int i) {
			pane.getChildren().remove(i);
			pane.getChildren().remove(1);
			pane.getChildren().add(1, getNewBalloon());
			setANDrequestFocus();
			this.score++;
			updateScore(score);
		}

	}

}
