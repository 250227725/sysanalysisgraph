import java.util.ArrayList;

public class Peak {
    final String name;
    public final ArrayList<Job> previousJobList;
    public final ArrayList<Job> nextJobList;


    public Peak(String name, ArrayList<Job> previousJobList, ArrayList<Job> nextJobList) {
        this.name = name;
        this.previousJobList = previousJobList;
        this.nextJobList = nextJobList;
    }

    public String getInfo() {
        String result = "Peak name: " + name + "\n";
        if (!previousJobList.isEmpty()) {
            String inJobs = "";
            for (Job job: previousJobList) {
                inJobs += job.getName() + ", ";
            }
            result += "Incoming jobs: " + inJobs;
        }
        else {
            result += "Incoming jobs: None";
        }
        result += "\n";
        if (!nextJobList.isEmpty()) {
            String outJobs = "";
            for (Job job: nextJobList) {
                outJobs += job.getName() + ", ";
            }
            result += "Outgoing jobs: " + outJobs;
        }
        else {
            result += "Outgoing jobs: None";
        }
        result += "\n";
        return result;
    }
}
