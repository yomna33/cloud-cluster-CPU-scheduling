import java.util.*;

public class AcsPolicy implements Policy {
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
        return tier1.pollFirst(); //pick the first process in the queue
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
