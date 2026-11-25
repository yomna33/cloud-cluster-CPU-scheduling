import java.util.*;

public class Metrics {

    public static double avgTurnaroundTime(List<Process> processes) {
        double totalTurnaroundTime = 0;
        for (Process p : processes) {
            totalTurnaroundTime += p.getTurnaroundTime();
        }
        return totalTurnaroundTime / processes.size();
    }

    public static double avgWaitingTime(List<Process> processes) {
        double totalWaitingTime = 0;
        for (Process p : processes) {
            totalWaitingTime += p.getWaitingTime();
        }
        return totalWaitingTime / processes.size();
    }

    public static void printMetrics(List<Process> processes) {
        List<Process> sorted = new ArrayList<>(processes);
        sorted.sort(Comparator.comparing(p -> p.name));

        System.out.println("Process\tArrival\tFinish\tTurnaround\tWaiting\tResponse");
        for (Process p : sorted) {
            System.out.printf("%s\t%d\t%d\t%d\t%d\t%d\n",
                    p.name, p.arrivalTime, p.finishTime, p.getTurnaroundTime(), p.getWaitingTime(), p.getResponseTime());
        }
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime(processes));
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime(processes));
    }
}
