package group2.sdp.pc;

public class Debug {

	public static boolean DEBUGGING = true;
	
	public static void log(String message) {
		if (DEBUGGING) {
			System.out.println(message);
		}
	}
	
}
