package i_paper_maths_puzzle.cell;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 04/10/2017.
 */
public class NullCell implements Cell {
	@Override
	public CellType getType() {
		return CellType.NULL;
	}
}
