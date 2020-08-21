package i_paper_maths_puzzle.pojos;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 03/10/2017.
 */
public enum Operator implements Cell {
	MULTIPLY, DIVIDE, ADD, SUBTRACT;


	@Override
	public CellType getType() {
		return CellType.OPERATOR;
	}
}
