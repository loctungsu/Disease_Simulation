/**
 * Author: Loc Tung Su and Fermin Ramos
 * Email: locsu@unm.edu and ramosfer@unm.edu
 * Class: Cs 351L
 * Professor: Brooke Chenoweth
 * Project 4: Disease Simulation
 * ConfigReader analyze the configuration file
 * to collect different features information
 * and share it to Main.java
 */
package disease_simulation;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConfigReader {
    private static boolean hasGrid;
    private static boolean hasDimension;
    private static boolean hasExposureDistance;
    private static boolean hasIncubationPeriod;
    private static boolean hasSicknessTime;
    private static boolean hasRecoverProb;
    private static boolean hasInitialSick;
    private static boolean hasRandomGrid;
    private static boolean hasInitialImmune;
    private static boolean hasRandomTotalOfAgent;
    // disease_simulation.Dimension of the simulation board
    private static Dimension dimension = new Dimension(200,200);
    // How close agents need to be to spread the disease       (defaults to 20)
    private static int exposureDistance = 20;
    // How long after exposure does it take to become sick      (defaults to 5)
    private static int incubationPeriod = 5;
    // How long agents are sick before they recover (or die)   (defaults to 10)
    private static int sicknessTime = 10;
    // Probability that a sick agent will recover            (defaults to 0.95)
    private static double recoverProb = 0.95;
    // Probability that an immune agent will get sick
    private static double immunityFailProb = 0.10;
    // Number of agents in the random place
    private static int randomTotalOfAgent = 100;
    // Number of Agents sick at the beginning of the simulation  (defaults to 1)
    private static int initialSick = 1;
    // Number of Immune agent
    private static int initialImmune = 0;
    // disease_simulation.Grid type
    private static Grid grid = new Grid(10,10);
    // Random disease_simulation.Grid type
    private static Grid randomGrid = new Grid(10,10,100);

    private ArrayList<String> listOfConfig = new ArrayList<>();

    private final int defaultRow = 100;
    private final int defaultCol = 100;
    private final int defaultPopulation = 100;

    /**
     * Constructor takes in the string name
     * Make bufferedReader and read line by line
     * @param configFile String
     */
    public ConfigReader(String configFile) {
        String currLine;
        String[] lines;

        hasDimension = false;
        hasExposureDistance = false;
        hasIncubationPeriod = false;
        hasSicknessTime = false;
        hasRecoverProb = false;
        hasInitialSick = false;
        hasRandomTotalOfAgent = false;
        hasRandomGrid = false;
        hasInitialImmune = false;

        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFile);
            BufferedReader fileIn = new BufferedReader(new InputStreamReader(inputStream));
            while((currLine = fileIn.readLine()) != null) {
                lines = currLine.split(" ");
                switch (lines[0]) {
                    case "dimensions" -> {
                        hasDimension = true;
                        dimension = new Dimension(Integer.parseInt(lines[1]), Integer.parseInt(lines[2]));
                        listOfConfig.add("disease_simulation.Dimension");
                    }
                    case "exposuredistance" -> {
                        hasExposureDistance = true;
                        exposureDistance = Integer.parseInt(lines[1]);
                        this.listOfConfig.add("exposuredistance");

                    }
                    case "incubation" -> {
                        hasIncubationPeriod = true;
                        incubationPeriod = Integer.parseInt(lines[1]);
                        this.listOfConfig.add("incubation");
                    }
                    case "sickness" -> {
                        hasSicknessTime = true;
                        sicknessTime = Integer.parseInt(lines[1]);
                        this.listOfConfig.add("sickness");
                    }
                    case "recover" -> {
                        hasRecoverProb = true;
                        recoverProb = Double.parseDouble(lines[1]);
                        this.listOfConfig.add("recover");
                    }
                    case "random" -> {
                        hasRandomTotalOfAgent = true;
                        randomTotalOfAgent = Integer.parseInt(lines[1]);
                        this.listOfConfig.add("random");
                    }
                    case "initialsick" -> {
                        hasInitialSick = true;
                        initialSick = Integer.parseInt(lines[1]);
                        this.listOfConfig.add("initialsick");
                    }
                    case "grid" -> {
                        hasGrid = true;
                        grid = new Grid(Integer.parseInt(lines[1]), Integer.parseInt(lines[2]));
                        this.listOfConfig.add("grid");
                    }
                    case "randomgrid" -> {
                        hasRandomGrid = true;
                        randomGrid = new Grid(Integer.parseInt(lines[1]), Integer.parseInt(lines[2]), Integer.parseInt(lines[3]));
                        this.listOfConfig.add("randomgrid");
                    }
                    case "initialimmune" -> {
                        hasInitialImmune = true;
                        initialImmune = Integer.parseInt(lines[1]);
                        this.listOfConfig.add("initialimmune");
                    }
                    default -> {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isHasInitialImmune() {
        return hasInitialImmune;
    }

    public static void setHasInitialImmune(boolean hasInitialImmune) {
        ConfigReader.hasInitialImmune = hasInitialImmune;
    }

    public static int getInitialImmune() {
        return initialImmune;
    }

    public static void setInitialImmune(int initialImmune) {
        ConfigReader.initialImmune = initialImmune;
    }
    //Bunch of getters
    public ArrayList<String> getListOfConfig() {
        return this.listOfConfig;
    }
    public static Dimension getDimension() {
        return dimension;
    }

    public static Grid getGrid() {
        return grid;
    }

    public static Grid getRandomGrid() {
        return randomGrid;
    }

    public static int getIncubationPeriod() {
        return incubationPeriod;
    }

    public static int getSicknessTime() {
        return sicknessTime;
    }

    public static double getRecoverProb() {
        return recoverProb;
    }

    public static double getImmunityFailProb(){return immunityFailProb;}

    public static int getRandomTotalOfAgent() {
        return randomTotalOfAgent;
    }

    public static int getInitialSick() {
        return initialSick;
    }


    public static void main(String[] args) {
        ConfigReader reader = new ConfigReader("test2.txt");
    }

    public static boolean isHasDimension() {
        return hasDimension;
    }

    public static void setHasDimension(boolean hasDimensionVal) {
        hasDimension = hasDimensionVal;
    }

    public static boolean isHasExposureDistance() {
        return hasExposureDistance;
    }

    public static void setHasExposureDistance(boolean hasExposureDist) {
        hasExposureDistance = hasExposureDist;
    }

    public static boolean isHasIncubationPeriod() {
        return hasIncubationPeriod;
    }

    public static void setHasIncubationPeriod(boolean hasIncubationVal) {
        hasIncubationPeriod = hasIncubationVal;
    }

    public static boolean isHasSicknessTime() {
        return hasSicknessTime;
    }

    public static void setHasSicknessTime(boolean hasSicknessTimeVal) {
        hasSicknessTime = hasSicknessTimeVal;
    }

    public static boolean isHasRecoverProb() {
        return hasRecoverProb;
    }

    public static void setHasRecoverProb(boolean hasRecoverProbability) {
        hasRecoverProb = hasRecoverProbability;
    }

    public static boolean isHasInitialSick() {
        return hasInitialSick;
    }

    public static void setHasInitialSick(boolean hasInitialSickAgents) {
        hasInitialSick = hasInitialSickAgents;
    }

    public static boolean isHasRandomTotalOfAgent() {
        return hasRandomTotalOfAgent;
    }

    public static void setHasRandomTotalOfAgent(boolean hasRandomTotalNumOfAgent) {
        hasRandomTotalOfAgent = hasRandomTotalNumOfAgent;
    }

    public static boolean isHasRandomGrid() {
        return hasRandomGrid;
    }

    public static void setHasRandomGrid(boolean hasRandGrid) {
        hasRandomGrid = hasRandGrid;
    }

    public static boolean isHasGrid() {
        return hasGrid;
    }

    public static void setHasGrid(boolean hasGridType) {
        hasGrid = hasGridType;
    }

    public static int getExposureDistance() {
        return exposureDistance;
    }

    public static void setExposureDistance(int exposureDist) {
        exposureDistance = exposureDist;
    }
}
