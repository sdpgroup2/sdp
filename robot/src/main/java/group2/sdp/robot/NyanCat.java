package group2.sdp.robot;

import lejos.nxt.Button;

public class NyanCat {

	private static String NYAN = "";
	
	public static void main(String[] args)
	{
		for (int j = 0; j < 20; j++)
		{
			for (int i = 0; i < 10; i++)
			{
				System.out.print(i);
			}
		}
		Button.waitForAnyPress();
	}
	
}
