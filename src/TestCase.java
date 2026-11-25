import java.util.*;
public class TestCase {
    public String name;
    public List<Process> processes;
    public int quantum;
    public int cs;
    public TestCase(String name, List<Process> processes, int quantum, int cs){
        this.name = name;
        this.processes = processes;
        this.quantum = quantum;
        this.cs = cs;
    }
    public static List<TestCase> all(){
        List<TestCase> cases = new ArrayList<>();
        cases.add(new TestCase("TC1", List.of(new Process("P1", 0, 4, 2), new Process("P2", 1, 7, 3), new Process("P3", 3, 2, 1)), 3, 0));
        cases.add(new TestCase("TC2", List.of(new Process("P1", 0, 5, 4), new Process("P2", 1, 6, 2), new Process("P3", 4, 4, 3)), 3, 1));

        return cases;
    }
}
