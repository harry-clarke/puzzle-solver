package utils.updatablepriorityqueue;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 29/10/2017.
 */
public class UpdatablePriorityQueue<E> implements Collection<E> {

	/**
	 * A mapping of values to their priorities.
	 * @see Priority
	 */
	private final Map<E, Priority<E>> priorities;
	/**
	 * The underlying priority queue.
	 * Uses {@link Priority} instead of the raw value to allow for comparison to be based on a counter instead of on the
	 * value itself.
	 * Counter values are updated by first removing, then adding the priority back into the queue
	 * ({@link UpdatablePriorityQueue#updatePriority(Priority)}).
	 */
	private final PriorityQueue<Priority<E>> queue;

	public UpdatablePriorityQueue() {
		super();
		priorities = Maps.newHashMap();
		queue = new PriorityQueue<>();
	}

	public Priority<E> getPriority(final @Nonnull E value) {
		return priorities.get(value);
	}

	protected void updatePriority(final @Nonnull Priority<E> priority) {
		queue.remove(priority);
		queue.add(priority);
	}

	protected Map<E, Priority<E>> getPriorities() {
		return priorities;
	}

	protected PriorityQueue<Priority<E>> getQueue() {
		return queue;
	}

	/*
		Queue Implementation.
		Note:
			Doesn't actually implement Queue because of type erasure issues
			between Queue<Priority<E>> and Collection<E>.

			Also would implement methods that don't make sense in the context of our priority queue
			(Add<Priority<E>> for example).

		Todo: See if there's a better way to do this.
	 */

	/**
	 * Retrieves, but does not remove, the head of this queue,
	 * or returns {@code null} if this queue is empty.
	 *
	 * @return the head of this queue, or {@code null} if this queue is empty
	 */
	@Nullable
	public Priority<E> peek() {
		return queue.peek();
	}

	/**
	 * Retrieves and removes the head of this queue,
	 * or returns {@code null} if this queue is empty.
	 *
	 * @return the head of this queue, or {@code null} if this queue is empty
	 */
	@Nullable
	public Priority<E> poll() {
		return Optional.ofNullable(queue.poll())
				.map(Priority::getValue)
				.map(priorities::remove)
				.orElse(null);
	}

	/**
	 * Returns the comparator used to order the elements in this
	 * queue, or {@code null} if this queue is sorted according to
	 * the {@linkplain Comparable natural ordering} of its elements.
	 *
	 * @return the comparator used to order this queue, or
	 *         {@code null} if this queue is sorted according to the
	 *         natural ordering of its elements
	 */
	public Comparator<? super Priority<E>> comparator() {
		return queue.comparator();
	}

	/**
	 * Retrieves and removes the head of this queue.  This method differs
	 * from {@link #poll poll} only in that it throws an exception if this
	 * queue is empty.
	 *
	 * <p>This implementation returns the result of {@code poll}
	 * unless the queue is empty.
	 *
	 * @return the head of this queue
	 * @throws NoSuchElementException if this queue is empty
	 */
	public Priority<E> remove() {
		final Priority<E> priority = queue.remove();
		priorities.remove(priority.getValue());
		return priority;
	}

	/**
	 * Retrieves, but does not remove, the head of this queue.  This method
	 * differs from {@link #peek peek} only in that it throws an exception if
	 * this queue is empty.
	 *
	 * <p>This implementation returns the result of {@code peek}
	 * unless the queue is empty.
	 *
	 * @return the head of this queue
	 * @throws NoSuchElementException if this queue is empty
	 */
	public Priority<E> element() {
		return queue.element();
	}

	/*
	Collection Implementation.
	 */

	/**
	 * Returns the number of elements in this collection.  If this collection
	 * contains more than {@code Integer.MAX_VALUE} elements, returns
	 * {@code Integer.MAX_VALUE}.
	 *
	 * @return the number of elements in this collection
	 */
	@Override
	public int size() {
		return queue.size();
	}

	/**
	 * Returns {@code true} if this collection contains no elements.
	 *
	 * @return {@code true} if this collection contains no elements
	 */
	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(final @Nonnull Object o) {
		//noinspection SuspiciousMethodCalls
		return queue.contains(o) || priorities.containsKey(o);
	}

	/**
	 * Returns an iterator over the elements in this collection.  There are no
	 * guarantees concerning the order in which the elements are returned
	 * (unless this collection is an instance of some class that provides a
	 * guarantee).
	 *
	 * @return an {@code Iterator} over the elements in this collection
	 */
	@Override
	public @Nonnull Iterator<E> iterator() {
		return queue.stream().map(Priority::getValue).iterator();
	}

	/**
	 * Returns an array containing all of the elements in this collection.
	 * If this collection makes any guarantees as to what order its elements
	 * are returned by its iterator, this method must return the elements in
	 * the same order.
	 * <p>
	 * <p>The returned array will be "safe" in that no references to it are
	 * maintained by this collection.  (In other words, this method must
	 * allocate a new array even if this collection is backed by an array).
	 * The caller is thus free to modify the returned array.
	 * <p>
	 * <p>This method acts as bridge between array-based and collection-based
	 * APIs.
	 *
	 * @return an array containing all of the elements in this collection
	 */
	@Override
	@Nonnull
	public Object[] toArray() {
		return priorities.keySet().toArray();
	}

	/**
	 * Returns an array containing all of the elements in this collection;
	 * the runtime type of the returned array is that of the specified array.
	 * If the collection fits in the specified array, it is returned therein.
	 * Otherwise, a new array is allocated with the runtime type of the
	 * specified array and the size of this collection.
	 * <p>
	 * <p>If this collection fits in the specified array with room to spare
	 * (i.e., the array has more elements than this collection), the element
	 * in the array immediately following the end of the collection is set to
	 * {@code null}.  (This is useful in determining the length of this
	 * collection <i>only</i> if the caller knows that this collection does
	 * not contain any {@code null} elements.)
	 * <p>
	 * <p>If this collection makes any guarantees as to what order its elements
	 * are returned by its iterator, this method must return the elements in
	 * the same order.
	 * <p>
	 * <p>Like the {@link #toArray()} method, this method acts as bridge between
	 * array-based and collection-based APIs.  Further, this method allows
	 * precise control over the runtime type of the output array, and may,
	 * under certain circumstances, be used to save allocation costs.
	 * <p>
	 * <p>Suppose {@code x} is a collection known to contain only strings.
	 * The following code can be used to dump the collection into a newly
	 * allocated array of {@code String}:
	 * <p>
	 * <pre>
	 *     String[] y = x.toArray(new String[0]);</pre>
	 * <p>
	 * Note that {@code toArray(new Object[0])} is identical in function to
	 * {@code toArray()}.
	 *
	 * @param a the array into which the elements of this collection are to be
	 *          stored, if it is big enough; otherwise, a new array of the same
	 *          runtime type is allocated for this purpose.
	 * @return an array containing all of the elements in this collection
	 * @throws ArrayStoreException  if the runtime type of the specified array
	 *                              is not a supertype of the runtime type of every element in
	 *                              this collection
	 * @throws NullPointerException if the specified array is null
	 */
	@Override
	@Nonnull
	public <T> T[] toArray(final @Nonnull T[] a) {
		return priorities.keySet().toArray(a);
	}

	/**
	 * Ensures that this collection contains the specified element (optional
	 * operation).  Returns {@code true} if this collection changed as a
	 * result of the call.  (Returns {@code false} if this collection does
	 * not permit duplicates and already contains the specified element.)<p>
	 * <p>
	 * Collections that support this operation may place limitations on what
	 * elements may be added to this collection.  In particular, some
	 * collections will refuse to add {@code null} elements, and others will
	 * impose restrictions on the type of elements that may be added.
	 * Collection classes should clearly specify in their documentation any
	 * restrictions on what elements may be added.<p>
	 * <p>
	 * If a collection refuses to add a particular element for any reason
	 * other than that it already contains the element, it <i>must</i> throw
	 * an exception (rather than returning {@code false}).  This preserves
	 * the invariant that a collection always contains the specified element
	 * after this call returns.
	 *
	 * @param e element whose presence in this collection is to be ensured
	 * @return {@code true} if this collection changed as a result of the
	 * call
	 * @throws UnsupportedOperationException if the {@code add} operation
	 *                                       is not supported by this collection
	 * @throws ClassCastException            if the class of the specified element
	 *                                       prevents it from being added to this collection
	 * @throws NullPointerException          if the specified element is null and this
	 *                                       collection does not permit null elements
	 * @throws IllegalArgumentException      if some property of the element
	 *                                       prevents it from being added to this collection
	 * @throws IllegalStateException         if the element cannot be added at this
	 *                                       time due to insertion restrictions
	 */
	@Override
	public boolean add(final @Nonnull E e) {
		final Priority<E> priority = priorities.computeIfAbsent(e, k -> new Priority<>(this, k));
		return queue.add(priority);
	}

	/**
	 * Removes a single instance of the specified element from this
	 * collection, if it is present (optional operation).  More formally,
	 * removes an element {@code e} such that
	 * {@code Objects.equals(o, e)}, if
	 * this collection contains one or more such elements.  Returns
	 * {@code true} if this collection contained the specified element (or
	 * equivalently, if this collection changed as a result of the call).
	 *
	 * @param o element to be removed from this collection, if present
	 * @return {@code true} if an element was removed as a result of this call
	 * @throws ClassCastException            if the type of the specified element
	 *                                       is incompatible with this collection
	 *                                       (<a href="#optional-restrictions">optional</a>)
	 * @throws NullPointerException          if the specified element is null and this
	 *                                       collection does not permit null elements
	 *                                       (<a href="#optional-restrictions">optional</a>)
	 * @throws UnsupportedOperationException if the {@code remove} operation
	 *                                       is not supported by this collection
	 */
	@Override
	public synchronized boolean remove(final @Nonnull Object o) {
		final Priority<E> priority = priorities.remove(o);
		return priority != null && queue.remove(priority);
	}

	/**
	 * Returns {@code true} if this collection contains all of the elements
	 * in the specified collection.
	 *
	 * @param c collection to be checked for containment in this collection
	 * @return {@code true} if this collection contains all of the elements
	 * in the specified collection
	 * @throws ClassCastException   if the types of one or more elements
	 *                              in the specified collection are incompatible with this
	 *                              collection
	 *                              (<a href="#optional-restrictions">optional</a>)
	 * @throws NullPointerException if the specified collection contains one
	 *                              or more null elements and this collection does not permit null
	 *                              elements
	 *                              (<a href="#optional-restrictions">optional</a>),
	 *                              or if the specified collection is null.
	 * @see #contains(Object)
	 */
	@Override
	public boolean containsAll(final @Nonnull Collection<?> c) {
		return priorities.keySet().containsAll(c)
				&& queue.containsAll(c);
	}

	/**
	 * Adds all of the elements in the specified collection to this collection
	 * (optional operation).  The behavior of this operation is undefined if
	 * the specified collection is modified while the operation is in progress.
	 * (This implies that the behavior of this call is undefined if the
	 * specified collection is this collection, and this collection is
	 * nonempty.)
	 *
	 * @param c collection containing elements to be added to this collection
	 * @return {@code true} if this collection changed as a result of the call
	 * @throws UnsupportedOperationException if the {@code addAll} operation
	 *                                       is not supported by this collection
	 * @throws ClassCastException            if the class of an element of the specified
	 *                                       collection prevents it from being added to this collection
	 * @throws NullPointerException          if the specified collection contains a
	 *                                       null element and this collection does not permit null elements,
	 *                                       or if the specified collection is null
	 * @throws IllegalArgumentException      if some property of an element of the
	 *                                       specified collection prevents it from being added to this
	 *                                       collection
	 * @throws IllegalStateException         if not all the elements can be added at
	 *                                       this time due to insertion restrictions
	 * @see #add(Object)
	 */
	@Override
	public boolean addAll(final @Nonnull Collection<? extends E> c) {
		return c.stream().map(this::add)
				.reduce(Boolean::logicalOr)
				.orElseThrow(NullPointerException::new);
	}

	/**
	 * Removes all of this collection's elements that are also contained in the
	 * specified collection (optional operation).  After this call returns,
	 * this collection will contain no elements in common with the specified
	 * collection.
	 *
	 * @param c collection containing elements to be removed from this collection
	 * @return {@code true} if this collection changed as a result of the
	 * call
	 * @throws UnsupportedOperationException if the {@code removeAll} method
	 *                                       is not supported by this collection
	 * @throws ClassCastException            if the types of one or more elements
	 *                                       in this collection are incompatible with the specified
	 *                                       collection
	 *                                       (<a href="#optional-restrictions">optional</a>)
	 * @throws NullPointerException          if this collection contains one or more
	 *                                       null elements and the specified collection does not support
	 *                                       null elements
	 *                                       (<a href="#optional-restrictions">optional</a>),
	 *                                       or if the specified collection is null
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	@Override
	public boolean removeAll(final @Nonnull Collection<?> c) {
		return c.stream().map(this::remove)
				.reduce(Boolean::logicalOr)
				.orElseThrow(NullPointerException::new);
	}

	/**
	 * Retains only the elements in this collection that are contained in the
	 * specified collection (optional operation).  In other words, removes from
	 * this collection all of its elements that are not contained in the
	 * specified collection.
	 *
	 * @param c collection containing elements to be retained in this collection
	 * @return {@code true} if this collection changed as a result of the call
	 * @throws UnsupportedOperationException if the {@code retainAll} operation
	 *                                       is not supported by this collection
	 * @throws ClassCastException            if the types of one or more elements
	 *                                       in this collection are incompatible with the specified
	 *                                       collection
	 *                                       (<a href="#optional-restrictions">optional</a>)
	 * @throws NullPointerException          if this collection contains one or more
	 *                                       null elements and the specified collection does not permit null
	 *                                       elements
	 *                                       (<a href="#optional-restrictions">optional</a>),
	 *                                       or if the specified collection is null
	 * @see #remove(Object)
	 * @see #contains(Object)
	 */
	@Override
	public boolean retainAll(final @Nonnull Collection<?> c) {
		return Sets.newHashSet(priorities.keySet()).stream()
				.filter(k -> !c.contains(k))
				.peek(this::remove)
				.findFirst().isPresent();
	}

	/**
	 * Removes all of the elements from this collection (optional operation).
	 * The collection will be empty after this method returns.
	 *
	 * @throws UnsupportedOperationException if the {@code clear} operation
	 *                                       is not supported by this collection
	 */
	@Override
	public void clear() {
		priorities.clear();
		queue.clear();
	}
}
