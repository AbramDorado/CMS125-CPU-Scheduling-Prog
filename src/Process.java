// Class representing a process with various attributes
public class Process implements Comparable{
	// Process attributes
    private int processID;
    private int arrivalTime;
    private int burstTime;
    private int remainingBurstTime;
    private int executionTime;
    private int priorityNumber;
    private int completionTime;
    private int responseTime;
    private int waitingTime;
    private int turnaroundTime;
    private int TimeWhenFirstAllocated = -1;
    private int allocationTime;

    
    // Getter and setter methods for process attributes

    // Get the process ID
    public int getProcessID() {
        return processID;
    }
    // Set the process ID
    public void setProcessID(int processID) {
        this.processID = processID;
    }
    // Get the arrival time of the process
    public int getArrivalTime() {
        return arrivalTime;
    }
    // Set the arrival time of the process
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    // Get the burst time of the process
    public int getBurstTime() {
        return burstTime;
    }
    // Set the burst time of the process
    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }
    // Get the priority number of the process
    public int getPriorityNumber() {
        return priorityNumber;
    }
    // Set the priority number of the process
    public void setPriorityNumber(int priorityNumber) {
        this.priorityNumber = priorityNumber;
    }
    // Get the completion time of the process
    public int getCompletionTime() {
        return completionTime;
    }
    // Set the completion time of the process
    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }
    // Get the response time of the process
    public int getResponseTime() {
        return responseTime;
    }
    // Set the response time of the process
    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }
    // Get the waiting time of the process
    public int getWaitingTime() {
        return waitingTime;
    }
    // Set the waiting time of the process
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }
    // Get the turnaround time of the process
    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    // Set the turnaround time of the process
    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }
    // Get the time when the process was first allocated
    public int getTimeWhenFirstAllocated() {
        return TimeWhenFirstAllocated;
    }
    // Set the time when the process was first allocated
    public void setTimeWhenFirstAllocated(int TimeWhenFirstAllocated) {
        this.TimeWhenFirstAllocated = TimeWhenFirstAllocated;
    }
    // Get the allocation time of the process
    public int getAllocationTime() {
        return allocationTime;
    }
    // Set the allocation time of the process
    public void setAllocationTime(int allocationTime) {
        this.allocationTime = allocationTime;
    }
    // Get the remaining burst time of the process
    public int getRemainingBurstTime() {
        return remainingBurstTime;
    }
    // Set the remaining burst time of the process
    public void setRemainingBurstTime(int remainingBurstTime) {
        this.remainingBurstTime = remainingBurstTime;
    }
    // Get the execution time of the process
    public int getExecutionTime() {
        return executionTime;
    }
    // Set the execution time of the process
    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }
    // Implementation of the Comparable interface for sorting processes
    @Override
    public int compareTo(Object o) {
        Process otherProcess = (Process) o;
        if(this.getArrivalTime() > otherProcess.getArrivalTime()){
            return 1;
        }
        else if(this.getArrivalTime() == otherProcess.getArrivalTime()){
            if(this.getProcessID() > otherProcess.getProcessID()){
                return 1;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }
}
