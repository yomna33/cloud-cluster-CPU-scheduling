import java.util.*;

public class SjfPolicy implements Policy {
    private final List<Process> queue = new ArrayList<>();

    @Override 
    public void onArrival(Process p, int time) {
        queue.add(p);
    }

    @Override 
    public Process pickNext(int time) {
        if (queue.isEmpty()) {
            return null;
        }
        Process shortestJob = queue.get(0);
        for (Process p : queue) {
            if (p.remainingTime < shortestJob.remainingTime || (p.remainingTime == shortestJob.remainingTime && p.arrivalTime < shortestJob.arrivalTime)) {
                shortestJob = p;
            }
        }
        queue.remove(shortestJob);
        return shortestJob;
    }

    @Override
    public boolean shouldPreempt(Process running, int sliceUsed, int time) {
        for (Process p : queue) {
            if (p.remainingTime < running.remainingTime) {
                return true; //preempt if there's a process with shorter remaining time
            }
        }
        return false;
    }

    @Override
    public void onPreempt(Process p, int time) {
        queue.add(p); //add the preempted process back to the queue
    }

    @Override
    public String snapshot() {
        if (queue.isEmpty()) { return "[empty]";}
         StringBuilder sb = new StringBuilder("[");
        for (Process p : queue) {
            if (sb.length() > 1) sb.append(", ");
            sb.append(p.format());
        }
        return sb.append("]").toString();
    }
    

    
}
