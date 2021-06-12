package finite_groupings;

import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public final class GroupingImplFactory<E> {

	@Nonnull
	private final List<MockAbstractCell<E>> cells;
	@Nonnull
	private final GroupImpl<E> grouping;

	private GroupingImplFactory(final @Nonnull List<MockAbstractCell<E>> cells,
								final @Nonnull GroupImpl<E> grouping) {
		this.cells = cells;
		this.grouping = grouping;
	}

	@Nonnull
	public List<MockAbstractCell<E>> getCells() {
		return cells;
	}

	@Nonnull
	public GroupImpl<E> getGrouping() {
		return grouping;
	}

	public static <E> GroupingImplFactory<E> create(final @Nonnull Set<E> values) {
		final MockAbstractCellFactory<E> cellFactory = new MockAbstractCellFactory<>(values);
		final List<MockAbstractCell<E>> cells = cellFactory.createAll();
		final GroupImpl<E> grouping = new GroupImpl<>(Sets.newHashSet(cells), values);
		return new GroupingImplFactory<>(cells, grouping);
	}
}
