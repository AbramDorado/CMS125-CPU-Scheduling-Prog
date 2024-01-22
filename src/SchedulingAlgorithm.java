import java.util.*;
import java.util.Queue;

public abstract class SchedulingAlgorithm implements Runnable{

    protected ArrayList<Process> processesList_for_GanttChart;
    protected ArrayList<Process> processesList_for_Table; //for table printing
    protected ArrayList<Process> readyStateArray = new ArrayList<>();
    private ArrayList<Process> waitingStateArray;
    private final boolean isPreemptive;
    private int timeQuantum = -1;

    // Constructor for non-preemptive algorithms
    public SchedulingAlgorithm(ArrayList<Process> processesList, boolean isPreemptive) {
        this.processesList_for_GanttChart = new ArrayList<>(processesList);
        this.processesList_for_Table = new ArrayList<>(processesList);
        this.isPreemptive = isPreemptive;
    }

    // Constructor for preemptive Round Robin algorithm
    public SchedulingAlgorithm(ArrayList<Process> processesList, int timeQuantum) {
        this.processesList_for_GanttChart = new ArrayList<>(processesList);
        this.processesList_for_Table = new ArrayList<>(processesList);
        this.isPreemptive = false;
        this.timeQuantum = timeQuantum;
    }

    // Arrange processes based on arrival time, burst time, priority, and process ID
    public void arrangeProcesses() {
        processesList_for_GanttChart.sort((p1, p2) -> {
            if (p1.getArrivalTime() > p2.getArrivalTime()) {
                return 1;
            } else if (p1.getArrivalTime() == p2.getArrivalTime()) {
                // If arrival times are equal, compare burst time
                if (p1.getBurstTime() > p2.getBurstTime()) {
                    return 1;
                } else if (p1.getBurstTime() == p2.getBurstTime()) {
                    // If burst times are also equal, compare priority
                    if (p1.getPriorityNumber() > p2.getPriorityNumber()) {
                        return 1;
                    } else if (p1.getPriorityNumber() == p2.getPriorityNumber()) {
                        // If priorities are also equal, use process ID as the tiebreaker
                        return Integer.compare(p1.getProcessID(), p2.getProcessID());
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        });
    }

    // Sort the readyStateArray based on specific criteria (to be implemented by subclasses)
    public void sortReadyStateArray(){

    }

    // Update the readyStateArray based on the current time
    public void updateReadyStateArray(int currentTime){

        for(Process process : waitingStateArray){
            if(process.getArrivalTime() <= currentTime){
                readyStateArray.add(process);
            }
        }
        for(Process processInReadyState : readyStateArray){
            waitingStateArray.remove(processInReadyState);
        }
        sortReadyStateArray();
    }
    
    // Execute the scheduling algorithm
    public final void execute(){
        arrangeProcesses();
        waitingStateArray = new ArrayList<>(processesList_for_GanttChart);
        processesList_for_GanttChart.clear();
        int currentTime = 0;

        Queue<Process> readyStateQueue = new LinkedList<>();
        
        // Main scheduling loop
        while(!waitingStateArray.isEmpty() || !readyStateArray.isEmpty() || !readyStateQueue.isEmpty()){
        	// Non-preemptive scheduling logic
            if(!isPreemptive){
            	// Non-preemptive without time quantum
                if(timeQuantum == -1){
                    updateReadyStateArray(currentTime);
                    if(!readyStateArray.isEmpty()){
                        Process process = readyStateArray.get(0);
                        readyStateArray.remove(0);

                        process.setTimeWhenFirstAllocated(currentTime);

                        currentTime += process.getBurstTime();

                        process.setCompletionTime(currentTime);
                        process.setExecutionTime(process.getBurstTime());
                        process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
                        process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
                        process.setResponseTime(process.getTimeWhenFirstAllocated() - process.getArrivalTime());

                        //ganttChartArray.add(process);
                        processesList_for_GanttChart.add(process);
                    }
                    else{
                        currentTime = waitingStateArray.get(0).getArrivalTime();
                    }
//                    readyStateArray.clear();

                }
                else{
                	// Non-preemptive with time quantum (Round Robin)
                    for(Process process : waitingStateArray){
                        if(process.getArrivalTime() <= currentTime){
                            readyStateQueue.add(process);
                        }
                        else{
                            break;
                        }
                    }

                    if(!waitingStateArray.isEmpty()){
                        for(Process processInReadyState : readyStateQueue){
                            waitingStateArray.remove(processInReadyState);
                        }
                    }

                    if(!readyStateQueue.isEmpty()){
                        Process process = readyStateQueue.remove();

                        if(process.getTimeWhenFirstAllocated() == -1){
                            process.setTimeWhenFirstAllocated(currentTime);
                            process.setAllocationTime(currentTime);
                        }

                        if(process.getRemainingBurstTime() <= timeQuantum){
                            currentTime+= process.getRemainingBurstTime();
                            process.setExecutionTime(process.getRemainingBurstTime());
                            process.setCompletionTime(currentTime);
                            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
                            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
                            process.setResponseTime(process.getTimeWhenFirstAllocated() - process.getArrivalTime());

                            processesList_for_Table.set(process.getProcessID()-1, process);
                            processesList_for_GanttChart.add(process);
                        }
                        else{
                            Process newProcess = new Process();
                            newProcess.setAllocationTime(currentTime);

                            currentTime += timeQuantum;

                            process.setRemainingBurstTime(process.getRemainingBurstTime() - timeQuantum);
                            process.setExecutionTime(process.getExecutionTime()+ timeQuantum);

                            for(Process waitingProcess : waitingStateArray){
                                if(waitingProcess.getArrivalTime() <= currentTime){
                                    readyStateQueue.add(waitingProcess);
                                }
                                else{
                                    break;
                                }
                            }

                            if(!waitingStateArray.isEmpty()){
                                for(Process processInReadyState : readyStateQueue){
                                    waitingStateArray.remove(processInReadyState);
                                }
                            }
                            readyStateQueue.add(process);


                            newProcess.setExecutionTime(timeQuantum);
                            newProcess.setProcessID(process.getProcessID());
                            newProcess.setCompletionTime(currentTime);

                            processesList_for_GanttChart.add(newProcess);

                        }

                    }

                    else{
                        currentTime = waitingStateArray.get(0).getArrivalTime();
                    }
                }
//                readyStateArray.clear();
                

            }
        	// Preemptive scheduling logic
            else{
                updateReadyStateArray(currentTime);

                if(!readyStateArray.isEmpty()){

                    Process process = readyStateArray.get(0);

                    if(process.getTimeWhenFirstAllocated() == -1){
                        process.setTimeWhenFirstAllocated(currentTime);
                        process.setAllocationTime(currentTime);
                    }


                    if(!waitingStateArray.isEmpty()){
                        int timeBeforeNextProcessArrives = waitingStateArray.get(0).getArrivalTime() - currentTime;

                        if(process.getRemainingBurstTime() <= timeBeforeNextProcessArrives){
                            currentTime+= process.getRemainingBurstTime();
                            process.setExecutionTime(process.getExecutionTime() + process.getRemainingBurstTime());
                            process.setCompletionTime(currentTime);
                            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
                            process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
                            process.setResponseTime(process.getTimeWhenFirstAllocated() - process.getArrivalTime());
                            readyStateArray.remove(0);

                            //ganttChartArray.add(process);
                            processesList_for_Table.set(process.getProcessID()-1, process);
                            processesList_for_GanttChart.add(process);
                        }

                        else{
                            int referenceTime = currentTime;
                            currentTime = waitingStateArray.get(0).getArrivalTime();
                            int executionTime = currentTime - referenceTime;
                            process.setRemainingBurstTime(process.getRemainingBurstTime() - executionTime);
                            process.setExecutionTime(process.getExecutionTime()+ executionTime);
                            updateReadyStateArray(currentTime);

                            if(readyStateArray.get(0).getProcessID() != process.getProcessID()){

                                Process newProcess = new Process();
                                newProcess.setExecutionTime(executionTime);
                                newProcess.setProcessID(process.getProcessID());
                                newProcess.setCompletionTime(currentTime);

                                //ganttChartArray.add(newProcess);
                                processesList_for_GanttChart.add(newProcess);
                            }
                        }
                    }

                    else{
                        currentTime+= process.getRemainingBurstTime();
                        process.setExecutionTime(process.getExecutionTime() + process.getRemainingBurstTime());
                        process.setCompletionTime(currentTime);
                        process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
                        process.setWaitingTime(process.getTurnaroundTime() - process.getBurstTime());
                        process.setResponseTime(process.getTimeWhenFirstAllocated() - process.getArrivalTime());
                        readyStateArray.remove(0);

                        //ganttChartArray.add(process);
                        processesList_for_GanttChart.add(process);
                    }
                }
                else{
                    currentTime = waitingStateArray.get(0).getArrivalTime();
                }
            }
//            readyStateArray.clear();

        }
        displayTable();
//        displayGanttChart();
    }

    // Display the result table
    public final void displayTable() {
        System.out.println("PID     AT      BT      CT      WT      TT");
        // Table header
        double totalTAT = 0;
        double totalWT = 0;
//        double totalRT = 0;
        int count = 0;

        // Display each process's data
        for (Process process : processesList_for_Table) {
            System.out.printf(" %-5d   %-5d   %-5d   %-5d   %-5d   %-5d%n",
                    process.getProcessID(), process.getArrivalTime(), process.getBurstTime(),
                    process.getCompletionTime(), process.getWaitingTime(),
                    process.getTurnaroundTime());

            totalTAT += process.getTurnaroundTime();
            totalWT += process.getWaitingTime();
//            totalRT += process.getResponseTime();
            count += 1;
        }
     // Display table footer
        System.out.println("___________________________________________________");

//        double averageRT = totalRT / count;
        double averageWT = totalWT / count;
        double averageTAT = totalTAT / count;

        System.out.printf("Average:                        %-6.2f %-6.2f%n%n", averageWT, averageTAT);
    }
    //End display table

//can input GanttChart 
    
}//End of scheduling algo



//Concrete subclass for First-Come, First-Served algorithm
class FCFS extends SchedulingAlgorithm {
    public FCFS(ArrayList<Process> processesList) {
        super(processesList, false);
    }

    @Override
    public void run() {
        super.execute();
//        readyStateArray.clear();
        
    }
}

//Concrete subclass for Shortest Job First algorithm
class SJF extends SchedulingAlgorithm {

    protected SJF(ArrayList<Process> processesList, boolean isPreemptive) {
        super(processesList, isPreemptive);
    }

    public void sortReadyStateArray(){
        readyStateArray.sort((p1, p2) -> {
            if (p1.getBurstTime() > p2.getBurstTime()) {
                return 1;
            } else if (p1.getBurstTime() == p2.getBurstTime()) {
                if (p1.getArrivalTime() > p2.getArrivalTime()) {
                    return 1;
                } else if (p1.getArrivalTime() == p2.getArrivalTime()) {
                    if (p1.getProcessID() > p2.getProcessID()) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        });
    }

    @Override
    public void run() {
        super.execute();
//        readyStateArray.clear();

    }
}

//Concrete subclass for Priority Scheduling algorithm
class Priority extends SchedulingAlgorithm{

    public Priority(ArrayList<Process> processesList, boolean isPreemptive) {
        super(processesList, isPreemptive);
    }
 // Sort readyStateArray based on burst time, arrival time, and process ID
    public void sortReadyStateArray(){
        readyStateArray.sort((p1, p2) -> {
            if(p1.getPriorityNumber() > p2.getPriorityNumber()){
                return 1;
            }
            else if(p1.getPriorityNumber() == p2.getPriorityNumber()){
                if(p1.getArrivalTime() > p2.getArrivalTime()){
                    return 1;
                }
                else if(p1.getArrivalTime() == p2.getArrivalTime()){
                    if(p1.getProcessID() > p2.getProcessID()){
                        return 1;
                    }else{
                        return -1;
                    }
                }else{
                    return -1;
                }
            }else{
                return -1;
            }
        });
    }

    @Override
    public void run() {
        super.execute();
//        readyStateArray.clear();

    }
}

//Concrete subclass for Round Robin algorithm
class RoundRobin extends SchedulingAlgorithm{

    protected RoundRobin(ArrayList<Process> processesList, int timeQuantum) {
        super(processesList, timeQuantum);
    }

    @Override
    public void run() {
        super.execute();
//        readyStateArray.clear();

    }
}

