public class Job {
    final String name;
    final int defaultDuration;
    final int accelerationCost;
    final int availableAcceleration;

    final int nextPickIndex;
    final int previousPickIndex;

    public int getCurrentDuration() {
        return currentDuration;
    }

    private int currentDuration;

    public Job(String name, int defaultDuration, int accelerationCost, int availableAcceleration, int previousPickIndex, int nextPickIndex) {
        this.name = name;
        this.defaultDuration = defaultDuration;
        this.accelerationCost = accelerationCost;
        this.availableAcceleration = availableAcceleration;
        this.currentDuration = defaultDuration;
        this.nextPickIndex = nextPickIndex;
        this.previousPickIndex = previousPickIndex;
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
        return "Route "+this.name+" from "+previousPickIndex+" to "+nextPickIndex+", duration: " + currentDuration;
    }
}
