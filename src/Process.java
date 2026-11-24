public class Process {
    public final String name;
    public final int arrivalTime , burstTime , priority;
    public int remainingTime;
    public int firstStartTime = -1 , finishTime = -1;

    public int tier =1; 
    public int currentPriority ;
    public int waitingTime;

    public Process(String name , int arrivalTime , int burstTime , int priority){
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
    
    //metrics
    public int getTurnaroundTime(){
        return finishTime - arrivalTime;
    }
    public int getWaitingTime(){
        return getTurnaroundTime() - burstTime;
    }
    public int getResponseTime(){
        return firstStartTime - arrivalTime;
    }

    //queue state 
    public String format(){
        return name + " (remaining: " + remainingTime + ", Priority: " + currentPriority + ")";
    }

    @Override public String toString() { return name;}

   
}
