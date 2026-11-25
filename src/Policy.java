/*
    we used interface as a contract since we have more than one
    scheduling algoritm going on, so they shall have same structure and build,
    however differ in thier implementation.
*/
public interface Policy {
    /*  
        called when a new process (once per process) arrives, 
        to put it where processes are put based on the scheduling algorithm
    */
    void onArrival(Process p , int time); 

    /*
        called when the CPU is free only and we need to pick the next process to run based on the algorithm
    */
    Process pickNext(int time); 

    /*
        called when a process is running and we need to check if it should be preempted,
        for round robin it checks time quantum, for SJF it checks if a new process arrived with shorter time
        ACS tier 2 is non preemptive.
    */
    boolean shouldPreempt(Process running , int sliceUsed , int time);

    // called when a process is preempted to decide what to do with the process that got out of cpu
    void onPreempt(Process p, int time); 

    /*
        it gets you what is in the ready queue right now depending on the algorithm
        but it doesnt print it, it just hands the string to the simulator which handles the printing.
    */
    String snapshot(int time);
}
