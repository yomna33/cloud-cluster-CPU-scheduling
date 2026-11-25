public interface Policy {
    void onArrival(Process p , int time); // called when a new process arrives
    Process pickNext(int time); // called when the CPU is free and we need to pick the next process to run
    boolean shouldPreempt(Process running , int sliceUsed , int time); // called when a process is running and we need to check if it should be preempted
    void onPreempt(Process p, int time); // called when a process is preempted
    String snapshot(int time); //save queue state
}
