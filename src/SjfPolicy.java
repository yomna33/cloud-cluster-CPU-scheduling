import java.util.*;

public class SjfPolicy implements Policy {
    private final List<Process> queue = new ArrayList<>();

    @Override 
    public void onArrival(Process p, int time) {
        queue.add(p); // on arrival just puts stuff in the ready queue like we agreed in contract
    }

    @Override 
    public Process pickNext(int time) {
        if (queue.isEmpty()) {
            return null; // no process to be picked by CPU so just return to leave the CPU idle this tick.
        }
        Process shortestJob = queue.get(0); // just give an initial value until we do a full scan over the queue for the shortest job
        for (Process p : queue) { // scan for shortest job
            // if p remaining time is shorter than the current shortest update it, if they are equal break the tie by arrival time precedency
            if (p.remainingTime < shortestJob.remainingTime || (p.remainingTime == shortestJob.remainingTime && p.arrivalTime < shortestJob.arrivalTime)) {
                shortestJob = p;
            }
        }
        queue.remove(shortestJob); // when you find it remove it from the queue to be processed so you dont compare it with itself
        return shortestJob;
    }

    @Override
    public boolean shouldPreempt(Process running, int sliceUsed, int time) {
        for (Process p : queue) {
            if (p.remainingTime < running.remainingTime) {
                return true; //preempt if there's a process with strictly shorter remaining time so you dont waste switch time
            }
        }
        return false;
    }

    @Override
    public void onPreempt(Process p, int time) {
        queue.add(p); //add the preempted process back to the queue then pickNext chooses who runs, no state changing in tier
    }

    @Override
    public String snapshot(int time) {
        if (queue.isEmpty()) { return "[empty]";}
         StringBuilder sb = new StringBuilder("["); // if not empty build the string with the contents of the queue at the moment
        for (Process p : queue) {
            if (sb.length() > 1) sb.append(", ");
            sb.append(p.format());
        }
        return sb.append("]").toString();
    }
}
