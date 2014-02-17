package sdp.group2.display;


public class NyanCat {

	private static String[][] NYAN = new String[][] {
		new String[] {
		"  o  +    o +  o",
		"-_-_,------,   o",
		"_-_-|   /\\_/\\   ",
		"-_-~|__( ^ .^) +",
		"_-_-\"\"+ \"\" +  o ",
		"+  o    + o  +  "
		},
		new String[] {
		"o  +    o +  o  ",
		"_-_-_,-------, o",
		"-_-_-|    /\\_/\\+",
		"_-_-~|___( ^ .^)+",
		"_-+- \" \" \" \"o   ",
		"o    + o  +  o o"
		}
	};
	// 8 x 16
	public static void main(String[] args)
	{
		while (true) {
		for (String[] n : NYAN)
		{
			for (String l : n)
			{
				System.out.println(l);
			}
			for (int i = 0; i < 100000; i++);
			for (int i = 0; i < 8; i++)
			{System.out.println("");}
		}
		}
	}
	
}
