/**
 * Author: Loc Tung Su and Fermin Ramos
 * Project 4: Disease Simulation
 * The Agent class that will act
 * as a "human", it can interact, it can
 * spread disease, get sicked, died, or recovered
 */
package disease_simulation;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * disease_simulation.Agent Object.
 */
public class Agent extends Circle implements  Runnable{
    private Status myStatus;
    private Color agentColor;
    public Thread thread;
    private BlockingQueue<Status> incomingMail;
    private List<Agent> neighbors;

    /**
     * disease_simulation.Agent's constructor with its status & thread
     * @param status status (sick, immune, etc.)
     */
    public Agent(Status status) {
        // Logic Stuff
        this.myStatus = status;
        this.agentColor = getColor(status);
        this.incomingMail = new LinkedBlockingQueue<>();
        this.neighbors = new ArrayList<>();

        // Each Agent gets it's own thread
        thread = new Thread(this);

        //GUI stuff
        this.setRadius(5);
        this.setFill(agentColor);
        this.setCenterX(5);
        this.setCenterY(5);
        myStatus = status;
    }

    /**
     * get agentStatus
     * @return String agentStatus
     */
    public Status getStatus() {
        return this.myStatus;
    }

    /**
     * Get agentColor base on it status
     * @param agentStatus
     * @return Color agentColor
     */
    public Color getColor(Status agentStatus) {
        Color agentColor;
        if (Status.Sick.equals(agentStatus)) {
            agentColor = Color.DARKGREY;
        } else if (Status.Exposed.equals(agentStatus)) {
            agentColor = Color.BLUE;
        } else if (Status.Dead.equals(agentStatus)) {
            agentColor = Color.BLACK;
        } else if (Status.Immune.equals(agentStatus)) {
            agentColor = Color.GREEN;
        } else {
            agentColor = Color.rgb(235, 201, 52);
        }
        return agentColor;
    }
    /**
     * Starts the disease_simulation.Agent's Thread
     */
    public void startThread(){
        thread.start();
    }

    /**
     * Change Color (only for testing)
     * Loc Su
     */
    public void changeColor(Status agentStatus) {
        this.setFill(getColor(agentStatus));
    }

    public void addNeighbours(List<Agent> newNeighbourAgent) {
        this.neighbors.addAll(newNeighbourAgent);
    }

    /**
     * Allows a thread to send a message to Console. Useful for Debugging.
     * @param message message to console
     */
    public static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + ": " + message);
    }

    /**
     * Checks if our mailbox has mail (if our neighbor is sick)
     * @return true if at least 1 neighbor is sick
     * @throws InterruptedException interruption exception
     */
    public synchronized boolean hasSickNeighbor() throws InterruptedException {
        int size = incomingMail.size();
        boolean ourSickStatus = false;

        if(size > 0){
            for(int i = 0; i < size; i++){
                Status neighbor = incomingMail.take();
                if(neighbor == Status.Sick){
                    ourSickStatus = true;
                }
            }
        }
        // Clear old mail
        incomingMail.clear();

        return ourSickStatus;
    }


    /**
     * Tell ALL our neighbors that we're sick (only sends mail if we're sick)
     */
    public void sendMail() throws InterruptedException {
        if(myStatus == Status.Sick){
            for(Agent neighbor: neighbors){
                neighbor.mailBox(myStatus);
            }
        }
    }


    /**
     * Method is used by neighbors to send us mail (to tell us their status)
     */
    public synchronized void mailBox(Status status) throws InterruptedException {
        incomingMail.put(status);
    }

    /**
     * Sets an disease_simulation.Agent's disease_simulation.Status & Informs GUI's History Pane
     * @param newStatus new status
     */
    public void setAgentStatus(Status newStatus) {
        // Change disease_simulation.Status
        this.myStatus = newStatus;

        // Inform GUI of status change
        String event = thread.getName() + " ";
        switch (newStatus){
            case Sick:
                event = event + "is sick";
                break;
            case Dead:
                event = event + "died";
                break;
            case Exposed:
                event = event + "was exposed to disease";
                break;
            default:
                event = event + "has recovered";
        }

        Main.updateGUIHistory(event);
    }

    /**
     * Method should be used after an agent has finished their sickness
     *  period. Calculates probability if agent should recover.
     * @return true if agent should recover
     */
    private boolean isRecovered(){
        int randomNum = new Random().nextInt(0,100);
        if(randomNum <= (ConfigReader.getRecoverProb()*100) && randomNum != 0){
            return true;
        }
        return false;
    }

    /**
     * Calculates probability that an immune agent gets sick.
     * @return true if agent should get sick
     */
    private boolean immunityFailed(){
        int randomNum = new Random().nextInt(0,100);
        if(randomNum <= (ConfigReader.getImmunityFailProb()*100) && randomNum != 0){
            return true;
        }
        return false;
    }

    /**
     * Daily Tasks
     */
    @Override
    public void run() {
        while(myStatus != Status.Dead){
            try{
                // Tasks can be organized in 2 categories:
                // 1) "Sick" Tasks
                // 2) "Checking if Sick" Tasks  -- Immune & Vulnerable
                switch(myStatus){

                    //Do nothing an entire day if Recovered (performance's sake)
                    case Recovered:
                        Thread.sleep(Time.daySecDuration() * 1000);
                        break;

                    // *May* get infected (small probability)
                    case Immune:
                        if(hasSickNeighbor() && immunityFailed()){
                            setAgentStatus(Status.Exposed);
                        }
                        Thread.sleep(50);  // Sleep for performance's sake
                        break;

                    // "Checking if Sick" Tasks
                    case Vulnerable:
                        if(hasSickNeighbor()){
                            setAgentStatus(Status.Exposed);
                        }
                        Thread.sleep(50);  // Sleep for performance's sake
                        break;

                    // "Sick" Tasks
                    default:
                        // Stay Exposed for "N" Seconds
                        if(myStatus == Status.Exposed){
                            Thread.sleep((1000L * Time.daySecDuration()) * ConfigReader.getIncubationPeriod());

                            setAgentStatus(Status.Sick);
                        }

                        // Stay Sick for "N" Seconds
                        if(myStatus == Status.Sick){
                            Thread.sleep((1000L * Time.daySecDuration()) * ConfigReader.getSicknessTime());

                            sendMail();  // Only Sends Mail if Sick

                            // Either: Recover or Die
                            if(isRecovered()){
                                setAgentStatus(Status.Recovered);
                            }else{
                                setAgentStatus(Status.Dead);
                                Main.updateDeathCounter();
                            }
                        }

                }

            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}

