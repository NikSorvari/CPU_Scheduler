import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Scheduling {
	
	static FileRead fileRead = new FileRead();
	static FileCreate fileCreate = new FileCreate();
	
	static ArrayList <Process> runningQ = new ArrayList<Process>();
	static ArrayList <Process> tempQ = new ArrayList<Process>();
	static ArrayList <Process> readyQ = new ArrayList<Process>();
	static ArrayList <String> outputArray = new ArrayList<String>();
	
	static int t=0;
	static int defTimeQuantum=0;
	static int timeLeft=0;
	static int doneProcesses=0;
	static boolean allProcessesDone=false;
	static boolean processCorrect=false;
	boolean processInReadyQ=false;
	
	static String avgTime;
	
	public static void main(String args[])
	{
		fileRead.openFile();
		fileRead.readFile();
		fileRead.closeFile();
		
		if(fileRead.schedulingType.equals("RR  4") )
			RoundRobin();
		
		if(fileRead.schedulingType.equals("SJF") ) 
			ShortestJobFirst();
			
		if(fileRead.schedulingType.equals("PR_noPREMP") )
			PriorityWithoutPreemption();
		
		if(fileRead.schedulingType.equals("PR_withPREMP") )
			PriorityWithPreemption();
		
		fileCreate.openFile();
		fileCreate.addTitle(fileRead.schedulingType);
		fileCreate.addProcessNum(fileRead.processNum);
		
		for(int i = 0; i<outputArray.size();i++) {
			fileCreate.addNumber(outputArray, i);
		}
		
		fileCreate.addAvgTime(avgTime);
		fileCreate.closeFile();
	}
	
	public static void CheckProcessFinished()
	{
		for(int i = 0; i<fileRead.processList.size(); i++) {
			if(fileRead.processList.get(i).isDone == false) {
				doneProcesses = 0;
			}
			else {
				doneProcesses ++;
			}
		//	System.out.println("the number of finished processes are: " + numOfFinishedProcesses);
			if(doneProcesses == fileRead.processNum) {
				allProcessesDone = true;
			}
		}
	}
	
	public static void CheckProcessArrived()
	{
		for(int i = 0; i<fileRead.processList.size(); i++) {
			if(fileRead.processList.get(i).arrivalTime == t) {
				fileRead.processList.get(i).arrived = true;
				if(!(readyQ.contains(fileRead.processList.get(i)))) 
					readyQ.add(fileRead.processList.get(i));
			}
		}
	}
	
	public void checkReadyQEmpty() {
		if(readyQ.isEmpty()) {
			this.processInReadyQ = false;
		}
		else {
			this.processInReadyQ = true;
		}
	}
	
	public static void checkRR() {
		System.out.println("Now checking roundrobin");
			if(runningQ.isEmpty()) {
				runningQ.add(readyQ.get(0));
				readyQ.remove(0);
				timeLeft = defTimeQuantum;
				outputArray.add(""+t + " "+ runningQ.get(0).processNum);
				
			}
			if(!(runningQ.isEmpty()) && timeLeft == 0 ) {
				readyQ.add(runningQ.get(0));
				runningQ.clear();
				if(runningQ.isEmpty()) {
					runningQ.add(readyQ.get(0));
					readyQ.remove(0);
					timeLeft = defTimeQuantum;
					outputArray.add(""+t + " "+ runningQ.get(0).processNum);
					
				}
			}
		
	}
	
	public static void checkSJF() {
		int shortestCPUTime = Integer.MAX_VALUE;
		//adds the process with the highest priority into the running queue from ready queue
		if(runningQ.isEmpty()) {					
			for(int i = 0; i<readyQ.size(); i++) {
				if(readyQ.get(i).remainingBurstTime < shortestCPUTime) {
					shortestCPUTime = readyQ.get(i).remainingBurstTime;
				}
			}
			for(int i = 0; i<readyQ.size(); i++) {
				if(readyQ.get(i).remainingBurstTime == shortestCPUTime) {
					runningQ.add(readyQ.get(i));
					outputArray.add(""+t + " "+ readyQ.get(i).processNum);
					
					readyQ.remove(i);
				}
			}
			
		}
	}
	
	public static void checkPWP() {
		int highestPriorityInReady = Integer.MAX_VALUE;
		int priorityInRun = Integer.MAX_VALUE;
		
		if(runningQ.isEmpty()) {					
			for(int i = 0; i<readyQ.size(); i++) {
				if(readyQ.get(i).priorityNum < highestPriorityInReady) {
					highestPriorityInReady = readyQ.get(i).priorityNum;
				}
			}
			for(int i = 0; i<readyQ.size(); i++) {
				if(readyQ.get(i).priorityNum == highestPriorityInReady) {
					runningQ.add(readyQ.get(i));
					outputArray.add(""+t + " "+ readyQ.get(i).processNum);
					
					readyQ.remove(i);
				}
			}
			
		}
		else {
			System.out.println("there is a process in the running queue");
			priorityInRun = runningQ.get(0).priorityNum;
			
			for(int i = 0; i<readyQ.size(); i++) {
				if(readyQ.get(i).priorityNum < highestPriorityInReady) {
					highestPriorityInReady = readyQ.get(i).priorityNum;
				}
			}
			if(highestPriorityInReady < priorityInRun) {
				tempQ.addAll(runningQ);
				runningQ.clear();
				for(int i = 0; i< readyQ.size(); i++) {
					if(readyQ.get(i).priorityNum == highestPriorityInReady) {
						readyQ.add(tempQ.get(0));
						runningQ.add(readyQ.get(i));
						outputArray.add(""+t + " "+ readyQ.get(i).processNum);
						readyQ.remove(i);
						tempQ.clear();
					}
				}
			}
		}
		
	}
	
	public static void checkPNP() {
		int highestPriorityInReady = Integer.MAX_VALUE;
		//adds the process with the highest priority into the running queue from ready queue
		if(runningQ.isEmpty()) {					
			for(int i = 0; i<readyQ.size(); i++) {
				if(readyQ.get(i).priorityNum < highestPriorityInReady) {
					highestPriorityInReady = readyQ.get(i).priorityNum;
				}
			}
			for(int i = 0; i<readyQ.size(); i++) {
				if(readyQ.get(i).priorityNum == highestPriorityInReady) {
					runningQ.add(readyQ.get(i));
			//		outputArray.add("Time of Preemption: run is empty " + Time + " of Process: " + readyProcessQueue.get(i).processNum);
					outputArray.add(""+t + " "+ readyQ.get(i).processNum);
					
					readyQ.remove(i);
				}
			}
			
		}
		
	}
	
	public static void subtractRemainingTime() {
		for(int i = 0; i<runningQ.size(); i++) {
			runningQ.get(i).remainingBurstTime --;
			System.out.println("Now subtracting cpu time from Process:" + runningQ.get(i).processNum);
		
			if(runningQ.get(i).remainingBurstTime <= 0) {
				runningQ.get(i).isDone = true;
				System.out.println("Process # " + runningQ.get(i).processNum + " has finished!!!");
				runningQ.clear();
				
			}
		}
	}
	
	public static void addWaitTimeReadyQ() {
		for(int i = 0; i<readyQ.size();i++) {
			readyQ.get(i).waitTime ++;
		}
	}
	
	public static  double calculateAvgWaitTime() {
		double avgTime = 0.0;
		for(int i = 0; i<fileRead.processList.size();i++) {
			avgTime = avgTime + fileRead.processList.get(i).waitTime;
		}
		avgTime = avgTime/fileRead.numberOfProcesses;
		return avgTime;
	}
	
	//prints out all of the processes info. the information is from an arraylist that is stored in the ReadFile class
	public static void printOutAllProcessInfo() {
		for(int i =0; i<r.processList.size();i++) {
			System.out.println(r.processList.get(i).toSring());
		}
	}
	
	//prints out what would be written to the output.txt. This is just used as a check to see if things that will be 
	//written to the output.txt will be written correctly.
	public static void printOutOutputArray() {
		for(int i = 0; i<outputArray.size(); i++) {
			System.out.println(outputArray.get(i).toString());
		}
	}
	
	
	//not used but if it did it will just check to see if the number of total processes match the number on line 2 of input.txt
	public static void inputNumOfProcessWarning() {
		int numOfProcess2ndLine = r.numberOfProcesses;
		int actualNumOfProcess = r.processList.size();
		if(numOfProcess2ndLine != actualNumOfProcess) {
			System.out.println("WARNING CHECK YOUR INPUT. THE NUMBER OF PROCESSES YOU PUT ON THE SECOND LINE DOES NOT EQUAL......");
			System.out.println("....THE AMOUNT OF TOTAL PROCESSES THAT ARE READ IN THE INPUT. YOU WILL GET AN INCORRECT OUTPUT!!!!");
			bolOfProcessCheckCorrect = false;
		}
		else {
		bolOfProcessCheckCorrect = true;
		}
	}

	private static void PriorityWithPreemption() {
		// TODO Auto-generated method stub
		
	}

	private static void PriorityWithoutPreemption() {
		// TODO Auto-generated method stub
		
	}

	private static void ShortestJobFirst() {
		// TODO Auto-generated method stub
		
	}

	private static void RoundRobin() {
		// TODO Auto-generated method stub
		
	}

}
