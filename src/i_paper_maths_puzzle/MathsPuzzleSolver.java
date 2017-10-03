package i_paper_maths_puzzle;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 03/10/2017.
 */
public class MathsPuzzleSolver {

	private static final String[] EXAMPLE_1 = new String[] {
			"1-6+5",
			"* * *",
			"7-3*4",
			"* - *",
			"8+9-2"
	};

	public static void main(final String[] args) {
		new MathsPuzzle(EXAMPLE_1);
	}

}
