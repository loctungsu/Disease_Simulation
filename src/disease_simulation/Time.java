/**
 * Author: Loc Tung Su and Fermin Ramos
 * Email: locsu@unm.edu and ramosfer@unm.edu
 * Class: Cs 351L
 * Professor: Brooke Chenoweth
 * Project 4: Disease Simulation
 * The Time file responsible for helping the AnimationTimer
 * to show the correct status color change after a certain time period has
 * passed.
 */
package disease_simulation;

import javafx.animation.AnimationTimer;

public class Time implements Runnable{
    // Keeps track of what day it is, since program start.   (Start = 0th day)
    private static int currentDay = 0;
    // The official time when the program started (in seconds)
    private static long startTime;
    // Used to convert nanoseconds -> seconds
    private static final int unitConv = 1000000000;
    // How long 1 day is (seconds)
    private static int daySecDuration = 2;
    public Time(int daySecDuration) {
        this.daySecDuration = daySecDuration;
    }

    /**
     * How many RAW seconds have passed since start of program
     * @return seconds
     */
    public static long currentTime(){
        return (System.nanoTime()/unitConv) - Time.startTime;
    }

    /**
     * Gets the current day
     * @return current day
     */
    public static int getCurrentDay(){
        return currentDay;
    }

    public static int daySecDuration(){
        return daySecDuration;
    }

    /**
     * Updates Day
     */
    private static void updateDay(){
        currentDay = (int) currentTime()/daySecDuration;
    }

    @Override
    public void run() {
        // Initialize disease_simulation.Time
        startTime = System.nanoTime() / unitConv;

        // Run infinitely
        while(true){
            // Did a new day even pass?
            if((currentTime() / daySecDuration) != currentDay){
                AnimationTimer timer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        for (int i = 0; i < Main.agents.size();i++) {
                            Main.agents.get(i).changeColor(Main.agents.get(i).getStatus());
                        }

                    }
                };
                timer.start();
                Main.testAgentDayStatus.clear();

                // Update Day!
                updateDay();
            }
        }
    }
}
