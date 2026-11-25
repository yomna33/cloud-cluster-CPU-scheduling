import java.util.*;

public class Main {
    public static void main(String[] args){
        String[] schedulers = {"SjfPolicy", "AcsPolicy"};
        for(TestCase t : TestCase.all()){
            for(String s : schedulers){
                System.out.println("=== " + t.name + " | " + s + " ===");
                System.out.println("Process\tArrival\tBurst\tPriority");
                for(Process p : t.processes){
                    System.out.println(p.name + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" + p.priority);
                }
                System.out.println("Quantum = " + t.quantum + ", CS = " + t.cs);
                Policy policy = Schedulers.create(s, t.quantum);
                Simulator simulator = new Simulator(t.processes, t.cs, policy, true);
                List<Process> processes = simulator.runLoop();
                Metrics.printMetrics(processes);
                System.out.println();
            }
        }
    }
    
}
