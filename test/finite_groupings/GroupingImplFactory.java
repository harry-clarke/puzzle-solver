package finite_groupings;

import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 12/11/2017.
 */
public final class GroupingImplFactory<E> {

	@Nonnull
	private final List<MockAbstractCell<E>> cells;
	@Nonnull
	private final GroupingImpl<E> grouping;

	private GroupingImplFactory(final @Nonnull List<MockAbstractCell<E>> cells,
								final @Nonnull GroupingImpl<E> grouping) {
		this.cells = cells;
		this.grouping = grouping;
	}

	@Nonnull
	public List<MockAbstractCell<E>> getCells() {
		return cells;
	}

	@Nonnull
	public GroupingImpl<E> getGrouping() {
		return grouping;
	}

	public static <E> GroupingImplFactory<E> create(final @Nonnull Set<E> values) {
		final MockAbstractCellFactory<E> cellFactory = new MockAbstractCellFactory<>(values);
		final List<MockAbstractCell<E>> cells = cellFactory.createAll();
		final GroupingImpl<E> grouping = new GroupingImpl<>(Sets.newHashSet(cells), values);
		return new GroupingImplFactory<>(cells, grouping);
	}
}
