public class Job {
    final String name;
    final int defaultDuration;
    final int accelerationCost;
    final int availableAcceleration;

    final int nextPeakIndex;
    final int previousPeakIndex;

    public int getCurrentDuration() {
        return currentDuration;
    }

    private int currentDuration;

    public Job(String name, int defaultDuration, int accelerationCost, int availableAcceleration, int previousPeakIndex, int nextPeakIndex) {
        this.name = name;
        this.defaultDuration = defaultDuration;
        this.accelerationCost = accelerationCost;
        this.availableAcceleration = availableAcceleration;
        this.currentDuration = defaultDuration;
        this.nextPeakIndex = nextPeakIndex;
        this.previousPeakIndex = previousPeakIndex;
    }

    public boolean checkAcceleratability() {
        return (currentDuration+availableAcceleration)>defaultDuration;
    }

    public int accelerate() {
        if (!this.checkAcceleratability()) {
            throw new RuntimeException("Not available");
        } else {
            this.currentDuration--;
            return this.accelerationCost;
        }
    }

    public String getName() {
        return name;
    }

    public int getAccelerationCost() {
        return accelerationCost;
    }

    public int getAvailableAcceleration() {
        return availableAcceleration+currentDuration - defaultDuration;
    }

    public int getNextPeakIndex() {
        return nextPeakIndex;
    }

    public int getPreviousPeakIndex() {
        return previousPeakIndex;
    }

    public String getFullName() {
        StringBuilder result = new StringBuilder();
        result.append("Accelerate work: ").append(name).append(", duration: ").append(currentDuration)
                        .append(", rest: ").append(availableAcceleration - defaultDuration + currentDuration);
        return result.toString();
    }

    public String getInfo() {
        return "Route "+this.name+" from "+ previousPeakIndex +" to "+ nextPeakIndex +", duration: " + currentDuration;
    }


    public String getJobInfoForRoute(boolean includeNextPeak) {
        return "P"
                + getPreviousPeakIndex()
                + "--"
                + getName()
                + "[" + getPreviousPeakIndex() + "," + getNextPeakIndex() + "]"
                + "("
                + getCurrentDuration()
                + (checkAcceleratability() ? "/"
                + getAccelerationCost()
                + "/"
                + getAvailableAcceleration()
                : "")
                + ")-->"
                + (includeNextPeak ? "P" + getNextPeakIndex() : "");
    }

    public String getShortName() {
        return name.substring(0,1);
    }
}
