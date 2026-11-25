public class Schedulers {
    public static Policy create(String name, int quantum){
        switch(name.toUpperCase()){
            case "SJFPOLICY":
                return new SjfPolicy();
            case "ACSPOLICY":
                return new AcsPolicy(quantum);
            default: 
                throw new IllegalArgumentException("Unknown Policy: " + name);
            }
        
    }
}
