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
    private int sliceUsed = 0;
    private boolean processed = false;
    private String queueState;

    public Simulator(List<Process> processes, int cs, Policy policy) {
        this.policy = policy;
        this.originalList = new ArrayList<>(processes);
        this.cs = cs;
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
                currentProcess = null;
            }
        }
    }

    private void admitNext() {
        queueState = policy.snapshot();
        if (currentProcess != null || nextProcess != null)
            return;
        Process p = policy.pickNext(sec);
        if (p == null)
            return;
        if (!processed || cs == 0) {
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
