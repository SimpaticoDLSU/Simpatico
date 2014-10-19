package shortcuts;

import java.util.Scanner;

public class Scan {
	
	public static Scanner sc = new Scanner(System.in);	
	
	public int GetInt()
	{
		int result;
		result = sc.nextInt();
		return result;		
	}
	
	public String GetLine()
	{
		String result;
		result = sc.nextLine();
		return result;
	}
	
	public String GetNext()
	{
		String result;
		result = sc.next();
		return result;
	}
	
	public double GetDouble()
	{
		double result;
		result = sc.nextDouble();
		return result;
	}
	
}
