import java.util.*;

public class Simulator {
    private List<Process> notArrived;
    private List<Process> originalList;
    private int cs;
    private int csLeft = 0;
    private Policy policy;
    private List<Process> processedProcesses = new ArrayList<>();
    private int sec = 0;
    private Process currentProcess = null;
    private Process nextProcess = null;
    private Process lastProcess = null; // process that was just preempted (no CS if it is picked again)
    private int sliceUsed = 0;
    private boolean processed = false;
    private String queueState;
    private boolean verbose;
    public Simulator(List<Process> processes, int cs, Policy policy, boolean verbose) {
        this.policy = policy;
        this.originalList = new ArrayList<>(processes);
        this.cs = cs;
        this.verbose = verbose;
    }

    public List<Process> runLoop() {
        notArrived = new ArrayList<>();
        for(Process p : originalList){
            notArrived.add(p.copy());
        }
        int total = notArrived.size();
        while (processedProcesses.size() < total) {
            admitProcess();
            checkRunning();
            admitNext();
            printR();
            tick();
        }
        return processedProcesses;
    }

    private void admitProcess() {
        Iterator<Process> it = notArrived.iterator();
        while (it.hasNext()) {
            Process p = it.next();
            if (p.arrivalTime == sec) {
                policy.onArrival(p, sec);
                it.remove();
            }
        }
    }

    private void checkRunning() {
        if (currentProcess != null) {
            if (currentProcess.remainingTime == 0) {
                currentProcess.finishTime = sec;
                processedProcesses.add(currentProcess);
                currentProcess = null;
            } else if (policy.shouldPreempt(currentProcess, sliceUsed, sec)) {
                policy.onPreempt(currentProcess, sec);
                lastProcess = currentProcess;
                currentProcess = null;
            }
        }
    }

    private void admitNext() {
        queueState = policy.snapshot(sec);
        if (currentProcess != null || nextProcess != null)
            return;
        Process p = policy.pickNext(sec);
        if (p == null)
            return;
        boolean sameProcess = (p == lastProcess);
        lastProcess = null;
        if (!processed || cs == 0 || sameProcess) {
            currentProcess = p;
            sliceUsed = 0;
            if (p.firstStartTime < 0)
                p.firstStartTime = sec;
        } else {
            sliceUsed = 0;
            nextProcess = p;
            csLeft = cs;
        }
        processed = true;
    }

    private void printR() {
        if(!verbose) return;
        String pName;
        if (currentProcess != null) {
            pName = currentProcess.name;
        } else if (nextProcess != null) {
            pName = "CS";
        } else {
            pName = "idle";
        }
        System.out.println(sec + "\t" + pName + "\tline: " + queueState);
    }

    private void tick() {
        if (nextProcess != null) {
            csLeft--;
            if (csLeft == 0) {
                currentProcess = nextProcess;
                nextProcess = null;
                sliceUsed = 0;
                if (currentProcess.firstStartTime < 0)
                    currentProcess.firstStartTime = sec + 1;
            }
        } else if (currentProcess != null) {
            currentProcess.remainingTime--;
            sliceUsed++;
        }
        sec++;
    }
}
