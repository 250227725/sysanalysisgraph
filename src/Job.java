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
        return this.name;
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
}
