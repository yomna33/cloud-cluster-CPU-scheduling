import java.util.*;

public class BatchRunner {
    public static void main(String[] args){
        String[] schedulers = {"SjfPolicy", "AcsPolicy"};
        int[] csValues = {0, 1, 2, 3, 4, 5};
        int[] quantumValues = {1, 2, 3, 4, 5, 6, 7, 8};

        System.out.println("TestCase,Scheduler,CS,Quantum,AvgTurnaround,AvgWaiting,CPUUtilization");
        for(TestCase t : TestCase.all()){
            for(String s : schedulers){
                for(int cs : csValues){
                    for(int q : quantumValues){
                        Policy policy = Schedulers.create(s, q);
                        Simulator simulator = new Simulator(t.processes, cs, policy, false);
                        List<Process> processes = simulator.runLoop();

                        int totalBurst = 0;
                        int lastFinish = 0;
                        for(Process p : processes){
                            totalBurst += p.burstTime;
                            if(p.finishTime > lastFinish){
                                lastFinish = p.finishTime;
                            }
                        }
                        double utilization = 100.0 * totalBurst / lastFinish;

                        String quantum = String.valueOf(q);
                        if(s.equals("SjfPolicy")){
                            quantum = "-";
                        }

                        System.out.printf("%s,%s,%d,%s,%.2f,%.2f,%.2f\n",
                                t.name, s, cs, quantum,
                                Metrics.avgTurnaroundTime(processes), Metrics.avgWaitingTime(processes), utilization);

                        if(s.equals("SjfPolicy")){
                            break;
                        }
                    }
                }
            }
        }
    }
}
