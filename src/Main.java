import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<Process> processes = new ArrayList<>();

    public static void main(String[] args) {
    	int processID = 1;  // Initialize process ID

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the CSV file name: ");

        try {
            String file = scanner.nextLine();
            FileInputStream fs = new FileInputStream(file);
            
         // Check if the file extension is CSV
            if (getFileExtension(file).equals("csv")) {
                Scanner myReader = new Scanner(fs);

                // Indices for parsing CSV data
                	int priorityIndex = 0;
                    int arrivalTimeIndex = 1;
                    int burstTimeIndex = 2;

                 // Read data from the CSV file and create Process objects
                    while (myReader.hasNextLine()) {
                        String[] data = myReader.nextLine().split(",");
                        
                        // Assuming the CSV file format is Priority, Arrival Time, Burst Time
                        Process process = new Process();
                        process.setProcessID(processID++);
                        process.setPriorityNumber(Integer.parseInt(data[priorityIndex].trim()));
                        process.setArrivalTime(Integer.parseInt(data[arrivalTimeIndex].trim()));
                        process.setBurstTime(Integer.parseInt(data[burstTimeIndex].trim()));
                        process.setRemainingBurstTime(process.getBurstTime());
                        processes.add(process);
                    }
                
                myReader.close();
            } else {
                System.err.println("File is not csv!!");
                scanner.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found!!!");
            scanner.close();
            return;
        }
        
//        boolean exit = false;
//
//        while (!exit) {
        	System.out.println("");
        	System.out.println("Please select your scheduling algorithm:");
            System.out.println("1 - FCFS");
            System.out.println("2 - SJF");
            System.out.println("3 - SRTF");
            System.out.println("4 - Round Robin");
            System.out.println("5 - Priority (preemptive)");
            System.out.println("6 - Priority (nonpreemptive)");
            System.out.println("7 - Quit");

            System.out.print("Select Algorithm: ");
            int choice = scanner.nextInt();

            switch (choice) {
            case 1:
                FCFS fcfs = new FCFS(processes);
                System.out.println("");
                System.out.println("First Come First Serve Algorithm");
                fcfs.execute();
                break;
            case 2:
                SJF sjfNP = new SJF(processes, false);
                System.out.println("");
                System.out.println("Shortest Job First Algorithm");
                sjfNP.execute();
                break;
            case 3:
                SJF sjfP = new SJF(processes, true);
                System.out.println("");
                System.out.println("Shortest Remaining Time First Algorithm");
                sjfP.execute();
                break;
            case 4:
                int timeQuantumRR;
                do {
                    System.out.print("Enter the time quantum for Round Robin (positive integer): ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("Invalid input. Please enter a positive integer: ");
                        scanner.next(); // consume non-integer input
                    }
                    timeQuantumRR = scanner.nextInt();
                } while (timeQuantumRR <= 0);

                RoundRobin roundRobin = new RoundRobin(processes, timeQuantumRR);
                System.out.println("");
                System.out.println("Round Robin Algorithm");
                roundRobin.execute();
                break;
            case 5:
                Priority priorityP = new Priority(processes, true);
                System.out.println("");
                System.out.println("Preemptive Priority Algorithm");
                priorityP.execute();
                
                break;
            case 6:
                Priority priorityNP = new Priority(processes, false);
                System.out.println("");
                System.out.println("Non-preemptive Priority Algorithm");
                priorityNP.execute();
                break;
            case 7:
            	System.out.println("");
            	System.out.println("Program Exited"); 
//                exit = true;
                break;
            default:
            	System.out.println("");
                System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                break;
        }
            

         // Add a separator line for better readability
        System.out.println("================================================================================================================");
//    }
    scanner.close();
    }
    
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";  // No extension found
        }
        return fileName.substring(lastDotIndex + 1);
    }
  
}