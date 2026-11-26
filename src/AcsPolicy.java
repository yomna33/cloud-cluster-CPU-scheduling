import java.util.*;

public class AcsPolicy implements Policy {

    private static final int AGING = 4; //lower priority by 1 per 4 ticks
    private final int quantum; //round robin quantum
    private final Deque<Process> tier1 = new ArrayDeque<>(); //FIFO for round robin
    private final List<Process> tier2 = new ArrayList<>(); //demoted processes

    public AcsPolicy(int quantum){
        this.quantum = quantum;
    }

    @Override 
    public void onArrival(Process p, int time) {
        p.tier = 1; // start initially with tier one since we dont know if the process ends fast or not
        tier1.addLast(p); //add to the end of the queue
    }

    @Override
    public Process pickNext(int time) {
        if (!tier1.isEmpty()) {
            return tier1.pollFirst(); //pick the first process in the queue fifo order
        }
        if(tier2.isEmpty()) {
            return null; // if tier 1 is empty and tier 2 is also empty there is no processes arrived or in the queues to process yet
        }
        applyAging(time); //apply aging to tier 2 processes

        Process highestPrio = tier2.get(0);
        for (Process p : tier2) { // scan for highest priority (least value) and break tie with arrival time
            if (p.currentPriority < highestPrio.currentPriority || (p.currentPriority == highestPrio.currentPriority && p.arrivalTime < highestPrio.arrivalTime)) {
                highestPrio = p;
            }
        }

        tier2.remove(highestPrio); // it's going on the CPU, tier 2 is non-preemptive, so it will run to completion and never be re-queued
        return highestPrio;

    }

    @Override
    public boolean shouldPreempt(Process running, int sliceUsed, int time) {
        if(running.tier == 1 && sliceUsed >= quantum){
            return true; //preempt if the process has used up its quantum (only tier one is preemptive)
        }
        return false;
    }

    @Override
    public void onPreempt(Process p, int time) {
        p.tier = 2; //demote to tier 2
        p.waitingSince = time; //for aging calculation
        tier2.add(p); //add to the end of the tier 2 list
    }

    @Override
    public String snapshot(int time) {
        applyAging(time); // update current priority at that moment
        return "Tier 1: " + format(tier1) + " | Tier 2: " + format(tier2);// simply concat the string of both tiers

    }

    private void applyAging(int time) {
        for (Process p : tier2) {
            p.currentPriority = p.priority - (time - p.waitingSince) / AGING; //dec priority based on waiting time
        }
    }

    private String format(Collection<Process> c) {
        if (c.isEmpty()) return "[empty]";
        StringBuilder sb = new StringBuilder("[");
        for (Process p : c) {
            if (sb.length() > 1) sb.append(", ");
            sb.append(p.format());
        }
        return sb.append("]").toString();
    }

    
}
