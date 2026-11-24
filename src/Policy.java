public interface Policy {
    void onArrival(Process p , int time);
    Process pickNext(int time);
    boolean shouldPreempt(Process running , int sliceUsed , int time);
    void onPreempt(Process p, int time);
    String snapshot();
}
