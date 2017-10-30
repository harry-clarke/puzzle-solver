package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ClassUtils.safeCast;

/**
 * @author Harry Clarke (hc306@kent.ac.uk).
 * @since 30/10/2017.
 */
class ClassUtilsTest {

	@Test
	void testSafeCastCorrect() {
		final Optional<Boolean> output = safeCast(true, Boolean.class);
		assertTrue(output.isPresent());
		assertTrue(output.get());
	}

	@Test
	void testSafeCastIncorrect() {
		final Optional<Boolean> output = safeCast(5, Boolean.class);
		assertFalse(output.isPresent());
	}

	@Test
	void testUtilConstructor() {
		testUtilConstructor(ClassUtils.class);
	}

	public static void testUtilConstructor(final Class<?> clazz) {
		final Executable executable = () -> {
			final Constructor<?> constructor = clazz.getDeclaredConstructor();
			assertFalse(constructor.canAccess(null));
			constructor.setAccessible(true);
			assertTrue(constructor.canAccess(null));
			try {
				constructor.newInstance();
			} catch (final InvocationTargetException e) {
				throw e.getTargetException();
			}
		};
		assertThrows(IllegalStateException.class, executable);
	}
}