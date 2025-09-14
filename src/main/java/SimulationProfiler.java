import java.util.HashMap;
import java.util.Map;

public class SimulationProfiler {
    private Map<String,Long> timings = new HashMap<>();
    private Map<String, Integer> counts = new HashMap<>();

    public void startTimer(String operation){
        timings.put(operation+"_start",System.nanoTime());
    }
    public void reset() {
        timings.clear();
        counts.clear();
    }
    public void endTimer(String operation){
        long start = timings.get(operation+"_start");
        long duration = System.nanoTime()-start;
        timings.merge(operation,duration,Long::sum);
        counts.merge(operation, 1, Integer::sum);

    }
    public void printReport(){
        System.out.println("======= Performance Analysis ========");
        timings.entrySet().stream().filter(e -> !e.getKey().endsWith("_start")).sorted(Map.Entry.<String, Long>comparingByKey().reversed()).forEach(e -> {
            String op = e.getKey();
            long totalNs = e.getValue();
            double totalMs = totalNs / 1_000_000.0;
            int count = counts.get(op);
            double avgMs = totalMs / count;

            System.out.printf("%-20s: %8.2f ms total, %8.2f ms avg (%d calls)\n",
                    op, totalMs, avgMs, count);
        });
    }

}
