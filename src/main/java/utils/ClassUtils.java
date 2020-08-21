package utils;

import java.util.Optional;

/**
 * @author Harry Clarke (harry-clarke@outlook.com).
 * @since 04/10/2017.
 */
public final class ClassUtils {

	private ClassUtils() {
		// Util class.
		throw createUtilConstructorAssertionError(getClass());
	}

	/**
	 * Wraps a standard {@link Class#isInstance(Object)} call as a
	 * Guava {@link Optional}, preventing cast exceptions.
	 * @param object The object to be cast.
	 * @param clazz The class to cast the object to.
	 * @param <F> Type of the object, superclass of E.
	 * @param <E> Type to cast to, extends F.
	 * @return An Optional either containing the cast object, or nothing if the cast failed.
	 */
	@SuppressWarnings("unchecked")
	public static <F, E extends F> Optional<E> safeCast(final F object, final Class<E> clazz) {
		if (clazz.isInstance(object))
			return Optional.of((E) object);
		return Optional.empty();
	}

	/**
	 * Creates an assertion error to be thrown when a util class' constructor is
	 * mysteriously called.
	 * Mainly used for verifying that the class' constructor isn't used for anything.
	 * @param clazz The util class that's generating this message.
	 * @return An assertion error explaining why calling a util class' constructor
	 * 	is gonna be a bad time.
	 */
	public static IllegalStateException createUtilConstructorAssertionError(final Class<?> clazz) {
		return new IllegalStateException("Util class' constructor was called: %s" + clazz.getName());
	}
}
