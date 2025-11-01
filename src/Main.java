import java.util.*;

public class Main {
    public static void main(String[] args) {
        String[] jobName = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
        int[] jobDuration = {5, 8, 4, 8, 10, 7, 6, 9, 4, 9, 7, 4};
        int[] jobAccelerationCost = {10, 3, 12, 3, 2, 4, 8, 3, 12, 3, 6, 12};
        int[] jobMaximumAcceleration = {2, 4, 1, 3, 5, 2, 3, 4, 1, 3, 2, 1};
        int[][] jobPeaks = {{0, 1}, {0, 2}, {1, 2}, {1, 3}, {1, 5}, {2, 4}, {4, 5}, {3, 6}, {5, 6}, {5, 7}, {4, 7}, {6, 7}};

        Graph graph = new Graph(jobName, jobDuration, jobAccelerationCost, jobMaximumAcceleration, jobPeaks);

//        graph.printGraphInfo();

        int totalCost = 0;
        int[] tp = graph.calculateTp();
        int lkp = tp[tp.length - 1];
//        int[] tn = graph.calculateTn(lkp);

        System.out.println("Total Day: " + lkp + ", current cost: " + totalCost);

        boolean acceleratable = true;
        while (acceleratable) {
            ArrayList<ArrayList<Job>> routesLkp = graph.recursiveCreateLkpList(graph.getPeakQuantity() - 1, tp, new ArrayList<>(), new ArrayList<>());
            graph.printCriticalRoutes(routesLkp);
            Optional<Job> accelerateJob = graph.selectJobToAccelerate(routesLkp);
            if (accelerateJob.isEmpty()) {
                System.out.println("Nothing to improve");
                acceleratable = false;
            }
            else {
                totalCost += accelerateJob.get().accelerate();
                System.out.println(accelerateJob.get().getFullName());

                tp = graph.calculateTp();
                if (tp[tp.length - 1] < lkp) {
                    lkp = tp[tp.length - 1];
                    System.out.println("Total Day: " + lkp + ", current cost: " + totalCost);
                }
            }
        }
    }
}