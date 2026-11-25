
public class Process {

    /* 
        this is the process object class it basically holds the name of the process,
        arrival time, burst time, remaining time, first start and finish time and turnaround time will
        be computed using this info 
    */
    public final String name;
    public final int arrivalTime, burstTime, priority;
    public int remainingTime;
    public int firstStartTime = -1, finishTime = -1;
    public int tier = 1; // the value of which tier or queue we are in right now either 1 or 2 (tier 2 is where aging happen)
    public int currentPriority; // the current priority of the process (based on aging) 
    public int waitingSince; // set in tier two to be able to calculate current priority

    public Process(String name, int arrivalTime, int burstTime, int priority) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.currentPriority = priority;
    }

    //copy so the simulator runs on copies instead of modifying the original processes
    public Process copy() {
        return new Process(name, arrivalTime, burstTime, priority);
    }

    /*metrics, calculate all kinds of time diagnostics that is associated with a process*/
    // can be calculated once the process is over
    public int getTurnaroundTime() {
        return finishTime - arrivalTime;
    }

    /*
    total waiting time can be calculated by subtracting all the time spent from arrival to leaving
    minus the time it spent executing
    */
    public int getWaitingTime() {
        return getTurnaroundTime() - burstTime;
    }

    // gets how long until first ever response
    public int getResponseTime() {
        return firstStartTime - arrivalTime;
    }

    //queue state 
    public String format() {
        return name + " (remaining: " + remainingTime + ", Priority: " + currentPriority + ")";
    }

    @Override
    public String toString() {
        return name;
    }

}
