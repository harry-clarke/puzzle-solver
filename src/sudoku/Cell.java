package sudoku;

import com.google.common.collect.*;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.*;

import static com.google.common.collect.Multimaps.index;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 31/07/2017.
 */
public class Cell {

	private final Grouping row, col, grid;
	private final Set<Grouping> groupings;
	private final Set<ImmutableCollection<Cell>> subGroups;
	private Value value = null;
	private Set<Value> candidates = EnumSet.allOf(Value.class);

	public Cell(final Grouping row, final Grouping col, final Grouping grid) {
		this.row = row;
		this.col = col;
		this.grid = grid;
		this.groupings = new HashSet<>();
		this.subGroups = new HashSet<>();
		Arrays.stream(new Grouping[] {row, col, grid})
				.forEach(g -> {
					groupings.add(g);
					g.addCell(this);
				});
	}

	/**
	 * (1) If already decided, return singleton.
	 * (2) Reduce possibilities by the remaining numbers available:
	 * (3) Reduce possibilities by completed sets.
	 * (4) Is there only one option?
	 * (4a) Yes: Set and break
	 * (4b) No:
	 *
	 * @return A set of values that this cell could possibly be.
	 */
	public synchronized @NotNull Set<Value> update() {
		// (1)
		final Value value = getValue();
		if (value != null)
			EnumSet.of(value);

		// (2)
		Set<Value> remaining = Sets.difference(candidates,
				groupings.parallelStream()
						.map(Grouping::getFoundValues)
						.reduce(Sets::union).get()
		);

		// (3)
		remaining = Sets.difference(remaining,
				groupings.parallelStream()
						.map(Grouping::getCells)
						.map(this::findSubGroup)
						.reduce(Sets::union).get()
		);

		// (4)
		if (remaining.size() == 0)
			throw new RuntimeException("Sudoku unsolvable.");
		if (remaining.size() == 1) {
			this.value = remaining.iterator().next();
			return remaining;
		}

		// (5)

	}

	public void addSubGroup(final ImmutableCollection<Cell> subGroup) {
		this.subGroups.add(subGroup);
	}

	public Set<Value> getCandidates() {
		return candidates;
	}

	public @Nullable Value getValue() {
		return value;
	}
}
