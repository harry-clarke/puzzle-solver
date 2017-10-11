package i_paper_maths_puzzle.pojos;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 04/10/2017.
 */
public class NullCell implements Cell {

	public static NullCell INSTANCE = new NullCell();

	private NullCell() {
		// Singleton instance.
	}

	@Override
	public CellType getType() {
		return CellType.NULL;
	}
}
