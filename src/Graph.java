import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    public final ArrayList<Job> jobs = new ArrayList<>();
    public final ArrayList<Peak> peaks = new ArrayList<>();

    private final int peakQuantity;

    public Graph (String[] n, int[] j, int[] a, int[] dj, int[][] p) {
        int maxPeakIndex = 0;
        for (int[] job: p) {
            maxPeakIndex = Math.max(job[0], Math.max(job[1], maxPeakIndex));
        }

        this.peakQuantity = maxPeakIndex + 1;

        for (int i = 0; i < n.length; i++) {
            jobs.add(new Job(n[i], j[i], a[i], dj[i], p[i][0], p[i][1]));
        }

        for (int i = 0; i < peakQuantity; i++) {
            ArrayList<Job> in = new ArrayList<>();
            ArrayList<Job> out = new ArrayList<>();
            for (Job job: jobs) {
                if (job.nextPeakIndex == i) {
                    in.add(job);
                }
                if (job.previousPeakIndex == i) {
                    out.add(job);
                }
            }
            peaks.add(new Peak("P"+i, in, out));
        }
    }

    public int getPeakQuantity() {
        return this.peakQuantity;
    }

    public int[] calculateTp() {
        int[] tp = new int[this.getPeakQuantity()];
        tp[0] = 0;
        for (int i = 1; i < tp.length; i++) {
            int route = 0;
            for (Job job: peaks.get(i).previousJobList) {
                route = Math.max(route, (tp[job.previousPeakIndex] + job.getCurrentDuration()));
            }
            tp[i] = route;
        }
        return tp;
    }

    public int[] calculateTn(int lkp) {
        int[] tn = new int[this.getPeakQuantity()];
        tn[peaks.size()-1] = lkp;

        for (int i = tn.length-2; i >= 0; i--) {
            int route = lkp+1; //Guaranteed more than minimum
            for (Job job: peaks.get(i).nextJobList) {
                route = Math.min(route, (tn[job.nextPeakIndex] - job.getCurrentDuration()));
            }
            tn[i] = route;
        }

        return tn;
    }


    public ArrayList<ArrayList<Job>> recursiveCreateLkpList (int index, int[] tp, ArrayList<ArrayList<Job>> routes, ArrayList<Job> route) {
        //Success exit condition
        if (index==0) {
            routes.add(route);
            return routes;
        }

        for (Job job: peaks.get(index).previousJobList) {

            int previousIndex = job.previousPeakIndex;

            if (job.getCurrentDuration() == tp[index]-tp[previousIndex]) {
                ArrayList<Job> newRoute = new ArrayList<>(route);
                newRoute.add(job);
                recursiveCreateLkpList(previousIndex, tp, routes, newRoute);
            }
        }

        return routes;
    }




    public void printGraphInfo() {
        for (Peak peak : this.peaks) {
            System.out.println(peak.getInfo());
        }

        jobs.forEach(job -> System.out.println(job.getInfo()));
    }

    public Optional<Job> selectJobToAccelerate(ArrayList<ArrayList<Job>> routesLkp) {
        if (routesLkp.size()==1) {
            return routesLkp.get(0).stream()
                    .filter(Job::checkAcceleratability)
                    .min(Comparator.comparingInt(job -> job.accelerationCost));
        }
        else {
            Set<Job> jobs = routesLkp.stream()
                    .flatMap(ArrayList::stream) // Flatten all inner lists
                    .collect(Collectors.toSet());

            HashMap<Job, Integer> directFlow = new HashMap<>();
            HashMap<Job, Integer> reverseFlow = new HashMap<>();

            for (Job job: jobs) {
                directFlow.put(job, job.checkAcceleratability() ? job.accelerationCost : 1000);
                reverseFlow.put(job, 0);
            }

            boolean isAllRotesBreak = false;

            int emergencyFlag = 0;

            while (!isAllRotesBreak) {
                emergencyFlag++;
                // Iteration
                Optional<Job> flowJob = directFlow.entrySet().stream()
                        .filter(k -> k.getValue() > 0 && k.getValue() < 1000)
                        .min(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey);

                if (flowJob.isEmpty()) return Optional.empty();

                Set<Job> updateJobs = routesLkp.stream()
                        .filter(route -> route.contains(flowJob.get()))  // Filter routes that contain seekingJob
                        .flatMap(ArrayList::stream)                   // Flatten all jobs from matching routes
                        .collect(Collectors.toCollection(HashSet::new)); // Collect to ArrayList

                int decreaseFlow = directFlow.get(flowJob.get());

                for (Job job : updateJobs) {
                    int direct = directFlow.get(job);
                    int newDirect = direct < 1000 ? direct - decreaseFlow : direct;

                    int reverse = reverseFlow.get(job);
                    int newReverse = direct < 1000 ? reverse + decreaseFlow : reverse;

                    directFlow.put(job, newDirect);
                    reverseFlow.put(job, newReverse);
                }

                isAllRotesBreak = true;
                for (ArrayList<Job> route : routesLkp) {
                    boolean isRouteBreak = false;
                    for (Job job : route) {
                        if (directFlow.get(job) == 0) {
                            isRouteBreak = true;
                            break;
                        }
                    }
                    if (!isRouteBreak) {
                        isAllRotesBreak = false;
                        break;
                    }
                }

                if (isAllRotesBreak) {
                    return flowJob;
                }

                if (emergencyFlag>30) { //Emergency exit for infinity loop
                    isAllRotesBreak = true;
                }
            }
        }
        return Optional.empty();
    }

}
