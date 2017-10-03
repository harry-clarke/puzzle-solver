package sudoku;

import com.google.common.collect.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 31/07/2017.
 */
public class Grouping {

	private final HashSet<Cell> cells;

	public Grouping() {
		this.cells = new HashSet<>();
	}

	public void addCell(final Cell cell) {
		cells.add(cell);
	}

	/**
	 * It's possible for cells to form a sub group,
	 * in which case those values aren't a possibility for any of the other cells in the grouping.
	 * Examples:
	 * <ul>
	 *     <li>(1,2),(1,2),(<strike>1,2</strike>3,4)...</li>
	 *     <li>(1,2,3),(1,2,3),(1,2,3),(<strike>1,2,3</strike>,4)...</li>
	 *     <li>(1,2),(2,3),(1,3),(<strike>1,2,3</strike>,4)...</li>
	 * </ul>
	 * @return A set of values that are decidedly already taken.
	 */
	private void updateSubGroups(final Set<Cell> cells) {
		final HashMultiset<Set<Value>> candidateSet = cells.parallelStream()
				.map(Cell::getCandidates)
				.collect(Collectors.toCollection(HashMultiset::create)); // Match up cells with the same candidates.

//		final HashMultimap<Integer, Set<Value>>
		// Filter by completed candidate sets.
		candidateSet.entrySet().parallelStream()
				.filter(e -> e.getCount() == e.getElement().size())
				.map(Multiset.Entry::getElement)
				.forEach(s -> s.parallelStream().forEach(c -> {return;}));
	}

	public Set<Value> getFoundValues() {
		return cells.parallelStream()
				.map(Cell::getValue)
				.collect(Collectors.toSet());
	}

	public Set<Cell> getCells() {
		return cells;
	}
}
