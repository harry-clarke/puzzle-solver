package finite_groupings;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MockAbstractCellFactory<E> implements Supplier<MockAbstractCell<E>> {

	private final Set<E> possibilities;

	public MockAbstractCellFactory(final Set<E> possibilities) {
		this.possibilities = possibilities;
	}

	public MockAbstractCell<E> create() {
		return new MockAbstractCell<>(possibilities);
	}

	@Nonnull
	public List<MockAbstractCell<E>> createAll() {
		return possibilities.parallelStream()
				.map(p -> new MockAbstractCell<>(possibilities))
				.collect(Collectors.toList());
	}

	@Nonnull
	public static <E> List<MockAbstractCell<E>> createAll(final @Nonnull Set<E> possibilities) {
		final MockAbstractCellFactory<E> factory = new MockAbstractCellFactory<>(possibilities);
		return factory.createAll();
	}

	/**
	 * Gets a result.
	 *
	 * @return a result
	 */
	@Override
	public MockAbstractCell<E> get() {
		return create();
	}
}
