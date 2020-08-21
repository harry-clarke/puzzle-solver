package i_paper_maths_puzzle.pojos;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 03/10/2017.
 */
public enum Number implements Cell {
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	EIGHT(8),
	NINE(9);

	public final Integer value;

	Number(final int v) {
		value = v;
	}


	@Override
	public CellType getType() {
		return CellType.NUMBER;
	}
}
