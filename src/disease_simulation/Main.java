/**
 * Author: Loc Tung Su and Fermin Ramos
 * Project 4: Disease Simulation
 * The main file that responsible for creating the GUI
 * with the game logic
 */
package disease_simulation;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.*;

/**
 * Main Class. Initializes program & holds GUI logic.
 */
public class Main extends Application {
    // disease_simulation.Dimension of the simulation board
    private static Dimension dimension= new Dimension(200,200);
    // disease_simulation.Grid type
    private static Grid grid = new Grid(10,10);
    // Random disease_simulation.Grid type
    private static Grid randomGrid = new Grid(10,10,100);
    // Number of agents in the random place
    private static int randomTotalOfAgent = 100;
    // How close agents need to be to spread the disease     (defaults to 20)
    private static int exposureDist = 20;
    // How long after exposure does it take to become sick   (defaults to 5)
    private static int incubationPeriod = 5;
    // How long agents are sick before they recover (or die) (defaults to 10)
    private static int sicknessTime = 10;
    // Probability that a sick agent will recover            (defaults to 0.95)
    private static double recoverProb = 0.95;
    // Agents sick at the beginning of the simulation        (defaults to 1)
    private static int initialSick = 1;
    private static int initialImmune = 0;
    public static ArrayList<Agent> agents;
    private static ArrayList<Integer> randomList = new ArrayList<>();
    public static BlockingQueue<String> testAgentDayStatus = new LinkedBlockingQueue<>();

    private ScheduledExecutorService scheduledExecutorService;
    private static ArrayList<String> listOfConfig;
    final int WINDOW_SIZE = 10;
    static Object fin = 9;
    private static VBox historyPane;
    // Total Deaths over the course of the program
    private static int totalDead = 0;

    /**
     * Launches GUI
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(fin);

        // Launch GUI
        launch(args);
        System.exit(1);  // Exit when finished w/ GUI
    }

    /**
     * Updates History Pane in GUI
     * @param newEvent string event sent to GUI History Pane
     */
    public synchronized static void updateGUIHistory(String newEvent){
        // Allows Multiple Threads to access JavaFX Application Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                historyPane.getChildren().add(new Label(newEvent));
            }
        });
    }

    /**
     * Updates the death counter +1
     */
    public synchronized static void updateDeathCounter(){totalDead++;}

    /**
     * Gets the total dead counter
     * @return (int) total dead agents
     */
    private synchronized static Integer getDeathCounter(){
        return totalDead;
    }

    /**
     * Generate the GUI that fit the disease_simulation.Dimension type config
     * @return a Pane for the disease_simulation.Dimension type in the config file
     */
    public Pane dimensionType() {
        Random rand = new Random();
        Pane boardPane = new Pane();
        System.out.println(dimension.getWidth() + " : " + dimension.getHeight());
        boardPane.setPrefSize(dimension.getWidth(), dimension.getHeight());
        boardPane.setBorder(Border.stroke(Color.GREEN));
        boardPane.setTranslateX(300);
        boardPane.setTranslateY(200);
        agents = new ArrayList<>();
        for(int i = 0; i < randomTotalOfAgent;i++) {
            if (initialSick != 0) {
                if (rand.nextInt(2) == 1) {
                    agents.add(i, new Agent(Status.Sick));
                    initialSick--;
                } else {
                    agents.add(i, new Agent(Status.Vulnerable));
                }
            } else if (initialImmune != 0) {
                if (rand.nextInt(2) == 1) {
                    agents.add(i, new Agent(Status.Immune));
                    initialImmune--;
                } else {
                    agents.add(i, new Agent(Status.Vulnerable));
                }
            } else {
                agents.add(i, new Agent(Status.Vulnerable));
            }
            boardPane.getChildren().add(agents.get(i));
            agents.get(i).setCenterX(rand.nextInt(10, dimension.getWidth() - 20));
            agents.get(i).setCenterY(rand.nextInt(10, dimension.getHeight() - 20));
        }

        return boardPane;
    }

    /**
     * Generate the GUI that fit the grid type from the config
     * @return a Pane that fit the grid type
     */
    public Pane gridType() {
        Pane boardPane = new Pane();
        Random rand = new Random();
        boardPane.setBorder(Border.stroke(Color.GREEN));
        boardPane.setTranslateX(300);
        boardPane.setTranslateY(200);
        int padding = 15;
        int i = 0;

        agents = new ArrayList<>();
        for (int row = 0; row < grid.getRow();row++) {
            for (int col = 0; col < grid.getCol();col++) {
                if (initialSick != 0) {
                    if (rand.nextInt(2) == 1) {
                        agents.add(i, new Agent(Status.Sick));
                        initialSick--;
                    } else {
                        agents.add(i, new Agent(Status.Vulnerable));
                    }
                } else if (initialImmune != 0) {
                    if (rand.nextInt(2) == 1) {
                        agents.add(i, new Agent(Status.Immune));
                        initialImmune--;
                    } else {
                        agents.add(i, new Agent(Status.Vulnerable));
                    }
                } else {
                    agents.add(i, new Agent(Status.Vulnerable));
                }

                boardPane.getChildren().add(agents.get(i));
                agents.get(i).setCenterX(5 + padding*row);
                agents.get(i).setCenterY(5 + padding*col);
                i++;
            }
        }
        return boardPane;
    }
    /**
     * Generate the GUI that fit the RANDOM GRID type from the config
     * @return a Pane that fit the RANDOM GRID type
     */
    public Pane randomGridType() {
        Pane boardPane = new Pane();
        Random rand = new Random();
        boardPane.setBorder(Border.stroke(Color.GREEN));
        boardPane.setTranslateX(300);
        boardPane.setTranslateY(200);
        int padding = 15;
        int i = 0;
        int randNum = 0;
        agents = new ArrayList<>();
        for(int row = 0; row < randomGrid.getRow();row++) {
            for (int col = 0; col < randomGrid.getCol();col++) {
                randNum = rand.nextInt(0, 6);
                randomList.add(randNum);
                if (initialSick != 0) {
                    if (rand.nextInt(2) == 1) {
                        agents.add(i, new Agent(Status.Sick));
                        initialSick--;
                    } else {
                        agents.add(i, new Agent(Status.Vulnerable));
                    }
                } else if (initialImmune != 0) {
                    if (rand.nextInt(2) == 1) {
                        agents.add(i, new Agent(Status.Immune));
                        initialImmune--;
                    } else {
                        agents.add(i, new Agent(Status.Vulnerable));
                    }
                } else {
                    agents.add(i, new Agent(Status.Vulnerable));
                }
                boardPane.getChildren().add(agents.get(i));
                agents.get(i).setCenterX(5 + padding*row);
                agents.get(i).setCenterY(5 + padding*col);
                i++;

            }
        }
        return boardPane;
    }

    /**
     * Updates the mouse position from GUI
     * @param e MouseEvent
     * @param txtPosition Text
     */
    public void updateMousePosition(MouseEvent e, Text txtPosition) {
        txtPosition.setX(e.getX() + 2);
        txtPosition.setY(e.getY() - 2);
        txtPosition.setText("(" + e.getX() + ", " + e.getY() + ")");
        txtPosition.setVisible(true);
    }


    /**
     * Start Method
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     */
    @Override
    public void start(final Stage primaryStage) {
        /*
           Live line chart from 11/04
         */
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis(); // plot against time
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (day)");
        xAxis.setAnimated(false); // axis animations are removed
        yAxis.setLabel("Dead");
        yAxis.setAnimated(false); // axis animations are removed

        //creating the line chart with two axis created above
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Total Death Counter");
        lineChart.setAnimated(false); // disable animations

        //defining a series to display data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Data Series");

        // add series to chart
        lineChart.getData().add(series);


        // setup a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        // put data onto graph per second
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // Update the chart
            Platform.runLater(() -> {
                // Put day number + total dead
                series.getData().add(new XYChart.Data<>("day " + Time.getCurrentDay(), getDeathCounter()));

                // Makes sure data fits on screen
                if (series.getData().size() > WINDOW_SIZE)
                    series.getData().remove(0);
            });

            // 1 graphical update per day
        }, 0, Time.daySecDuration(), TimeUnit.SECONDS);
        /***********************************************/
        //Testing mouse position to test
        Text txtPosition = new Text();
        int chosenDaySecDuration = 0;
        String chosenTestFile = "";
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your chosen configuration test file: ");
        chosenTestFile = sc.next();
        System.out.print("Enter your chosen the amount of second represent a day (1 or 0) for fast simulation: ");
        chosenDaySecDuration = sc.nextInt();
        Time timer = new Time(chosenDaySecDuration);
        HBox pane = new HBox();
        ConfigReader configReader = new ConfigReader(chosenTestFile);
        listOfConfig = configReader.getListOfConfig();
        storingConfig(configReader);
        Pane boardPane = new Pane();
        boardPane.setPadding(new Insets(5, 5, 5, 5));
        if (listOfConfig.contains("disease_simulation.Dimension")) {
            boardPane = dimensionType();
        } else if (listOfConfig.contains("grid")){
            boardPane = gridType();
        } else if (listOfConfig.contains("randomgrid")) {
            boardPane = randomGridType();
        }
        storeNeighbourAgent();

        Pane simulationBoard = new Pane();
        simulationBoard.setPrefSize(800,200);
        simulationBoard.setBorder(Border.stroke(Color.BLACK));
        simulationBoard.getChildren().add(boardPane);
        simulationBoard.getChildren().add(txtPosition);

        simulationBoard.setOnMousePressed(e -> {
            updateMousePosition(e,txtPosition);
        });
        simulationBoard.setOnMouseDragged(e -> {
            updateMousePosition(e,txtPosition);
        });
        simulationBoard.setOnMouseReleased(e -> txtPosition.setVisible(false));
        // Right Pane
        VBox rightPane = new VBox();
        rightPane.setPadding(new Insets(10,10,10,10));
        rightPane.setSpacing(20);

        // Plot XY
        Pane plot = new Pane();
        plot.setPadding(new Insets(5,5,5,5));
        plot.setMinSize(300,300);
        plot.setBorder(Border.stroke(Color.BLUE));
        lineChart.setPrefSize(350,300);
        plot.getChildren().add(lineChart);

        // "Play Simulation" Button
        Button playButton = new Button("Run");
        playButton.setPadding(new Insets(5,50,5,50));
        playButton.setTranslateX(100);
        playButton.setFont(new Font(15));
        playButton.setOnAction(event -> {
            /// Start Time Manager (GUI Updates based on Time Manager)
            Thread timeThread = new Thread(timer);
            timeThread.start();
            playButton.setDisable(true);
            // Start Agent Threads
            for(Agent agent: agents){agent.startThread();}
        });

        // Simulation History
        historyPane = new VBox();
        historyPane.setSpacing(5);
        historyPane.setPrefSize(368,400);
        historyPane.setBorder(Border.stroke(Color.BLACK));
        historyPane.setPadding(new Insets(5,5,5,5));
        ScrollPane scrollPane = new ScrollPane(historyPane);
        scrollPane.setPrefSize(400,400);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        rightPane.getChildren().add(plot);
        rightPane.getChildren().add(scrollPane);
        rightPane.getChildren().add(playButton);
//        rightPane.getChildren().add(restartButt);


        HBox.setMargin(simulationBoard,new Insets(5,5,5,5));
        HBox.setMargin(rightPane,new Insets(5,5,5,5));
        pane.getChildren().add(simulationBoard);
        pane.getChildren().add(rightPane);


        Scene scene = new Scene(pane, 1200, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Disease Simulation");
        primaryStage.show();

    }

    /**
     * Storing config in the correct variable
     * @param configReader configReader
     */
    public void storingConfig(ConfigReader configReader) {
        if (configReader.isHasDimension()) {
            dimension = configReader.getDimension();
        }
        if (configReader.isHasExposureDistance()) {
            exposureDist = configReader.getExposureDistance();
        }
        if (configReader.isHasIncubationPeriod()) {
            incubationPeriod = configReader.getIncubationPeriod();
        }
        if (configReader.isHasInitialSick()) {
            initialSick = configReader.getInitialSick();
        }
        if (configReader.isHasRandomGrid()) {
            randomGrid = configReader.getRandomGrid();
        }
        if (configReader.isHasRecoverProb()) {
            recoverProb = configReader.getRecoverProb();
        }
        if (configReader.isHasRandomTotalOfAgent()) {
            randomTotalOfAgent = configReader.getRandomTotalOfAgent();
        }
        if (configReader.isHasSicknessTime()) {
            sicknessTime = configReader.getSicknessTime();
        }
        if (configReader.isHasGrid()) {
            grid = configReader.getGrid();
        }

        if (configReader.isHasInitialImmune()) {
           initialImmune = configReader.getInitialImmune();
        }
    }

    /**
     * For each agent in the list of agents
     * Generate each agent a list of neighbourAgents within
     * a given distance
     */
    public static void storeNeighbourAgent() {
        for (int i = 0; i < agents.size();i++) {
            agents.get(i).addNeighbours(getNeighbourList(agents.get(i)));
        }
    }

    /**
     * Check if the surrounding of an agent is withn a given
     * distance, if it is, store it in an arraylist
     * @param currAgent disease_simulation.Agent type
     * @return a list of neighbor agents of the currAgent
     */
    public static ArrayList<Agent> getNeighbourList(Agent currAgent) {
        ArrayList<Agent> neighAgent = new ArrayList<>();
        Dimension curr = new Dimension((int) currAgent.getCenterX(), (int) currAgent.getCenterY());
        Dimension neighbour;

        for (int i = 0;i < agents.size();i++) {
            if (agents.get(i) == currAgent) {
                continue;
            }
            neighbour = new Dimension((int) agents.get(i).getCenterX(), (int) agents.get(i).getCenterY());
            if (dist(curr, neighbour) < 20) {
                neighAgent.add(agents.get(i));
            }

        }

        return neighAgent;
    }

    /**
     * Calculate the distance of 2 agents using its dimension
     * @param oneCir the disease_simulation.Dimension of the first agent
     * @param twoCir the disease_simulation.Dimension of the second agent
     * @return a Double type with the value of the distance between two agents.
     */
    public static Double dist(Dimension oneCir, Dimension twoCir) {
        double x1 = oneCir.getWidth();
        double y1 = oneCir.getHeight();
        double x2 = twoCir.getWidth();
        double y2 = twoCir.getHeight();

        double ac = Math.abs(y2 - y1);
        double cb = Math.abs(x2 - x1);

        return Math.hypot(ac, cb);
    }
}