import java.util.*;

public class AcsPolicy implements Policy {

    private static final int AGING = 4; //improve prio by 1 per 4 times
    private final int quantum; //round robin quantum
    private final Deque<Process> tier1 = new ArrayDeque<>(); //FIFO for round robin
    private final List<Process> tier2 = new ArrayList<>(); //demoted processes

    public AcsPolicy(int quantum){
        this.quantum = quantum;
    }

    @Override 
    public void onArrival(Process p, int time) {
        p.tier = 1;
        tier1.addLast(p); //add to the end of the queue
    }

    @Override
    public Process pickNext(int time) {
        if (!tier1.isEmpty()) {
            return tier1.pollFirst(); //pick the first process in the queue
        }
        if(tier2.isEmpty()) {
            return null;
        }
        applyAging(time); //apply aging to tier 2 processes

        Process highestPrio = tier2.get(0);
        for (Process p : tier2) {
            if (p.currentPriority < highestPrio.currentPriority || (p.currentPriority == highestPrio.currentPriority && p.arrivalTime < highestPrio.arrivalTime)) {
                highestPrio = p;
            }
        }

        tier2.remove(highestPrio);
        return highestPrio;

    }

    @Override
    public boolean shouldPreempt(Process running, int sliceUsed, int time) {
        if(running.tier == 1 && sliceUsed >= quantum){
            return true; //preempt if the process has used up its quantum
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
    public String snapshot() {
        return "Tier 1: " + format(tier1) + " | Tier 2: " + format(tier2);  

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
