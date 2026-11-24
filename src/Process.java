public class Process {
    public final String name;
    public final int arrivalTime , burstTime , priority;
    public int remainingTime;
    public int firstStartTime = -1 , finishTime = -1;

    public Process(String name , int arrivalTime , int burstTime , int priority){
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    public int getTurnaroundTime(){
        return finishTime - arrivalTime;
    }
    public int getWaitingTime(){
        return getTurnaroundTime() - burstTime;
    }
    public int getResponseTime(){
        return firstStartTime - arrivalTime;
    }

   
}
