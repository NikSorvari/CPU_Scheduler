import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

public class FileRead {

	String schedulingType;
	String processNumString;
	
	public int processNum;
	int timeQuantum;
	
	ArrayList<Process> processList = new ArrayList<Process>();
	ArrayList<Integer> numList = new ArrayList<Integer>();
	
	private Scanner scan;

	public void openFile() {
		try {
			scan = new Scanner(new File("input.txt"));
		}
		catch(Exception e) {
			System.out.println("could not find file");
		}
	}

	public void readFile() {
		
		schedulingType = scan.nextLine();
		if(schedulingType.contains("RR")) {
			String temp = "";
			temp = schedulingType.substring(schedulingType.lastIndexOf(" ")+1);
			timeQuantum = Integer.parseInt(temp);
			System.out.println("The time Quantum is: "+ timeQuantum);
		}
		processNumString = scan.nextLine();
		processNum = Integer.parseInt(processNumString);
		

		
		while(scan.hasNext()) {
			
			String processNum = scan.next();
			int num = Integer.parseInt(processNum);
			numList.add(num);
			String arrivalTime = scan.next();
			int num1 = Integer.parseInt(arrivalTime);
			numList.add(num1);
			String cpuBurstTime = scan.next();
			int num2 = Integer.parseInt(cpuBurstTime);
			numList.add(num2);
			String priority = scan.next();
			int num3 = Integer.parseInt(priority);
			numList.add(num3);
			Process process = new Process (num, num1,num2,num3);
			processList.add(process);
			
		}
		
		
		
		
		
	}

	public void closeFile() {
		scan.close();
		
	}

}
