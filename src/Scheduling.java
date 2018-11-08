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
			if(fileRead.processList.get(i).hasFinished == false) {
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
