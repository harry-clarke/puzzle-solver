package utils;

import java.util.Optional;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 04/10/2017.
 */
public final class ClassUtils {

	private ClassUtils() {
		// Util class.
	}

	@SuppressWarnings("unchecked")
	public static <F, E extends F> Optional<E> safeCast(final F object, final Class<E> clazz) {
		if (clazz.isInstance(object))
			return Optional.of((E) object);
		return Optional.empty();
	}
}
