package gov.usgs.jem.binarymodelingdata;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.SortedSet;

/**
 * Test utilities
 *
 * @author mckelvym
 * @since Jul 28, 2015
 *
 */
public final class Tests
{
	/**
	 * Test name prefix.
	 *
	 * @since Jul 28, 2015
	 */
	private static final String PREFIX = "test";

	/**
	 * Ensure that a testing class has defined all required testing methods.
	 *
	 * @param p_ClassToTest
	 *            the class that is being tested
	 * @param p_TestingClass
	 *            the class that is doing the testing
	 * @throws NoSuchMethodException
	 *             if a method is missing.
	 * @since Jul 28, 2015
	 */
	public static void assertHasRequiredMethods(final Class<?> p_ClassToTest,
			final Class<?> p_TestingClass) throws NoSuchMethodException
	{
		final Iterable<String> missingRequiredTestMethods = Tests
				.getMissingRequiredTestMethods(p_ClassToTest, p_TestingClass);
		if (!Iterables.isEmpty(missingRequiredTestMethods))
		{
			throw new NoSuchMethodException(
					String.format("Methods %s missing in %s.",
							Joiner.on(", ").join(missingRequiredTestMethods),
							p_TestingClass.getSimpleName()));
		}
	}

	/**
	 * Gets the publicly-visible methods defined by the provided testing class
	 * that start with the "test" prefix.
	 *
	 * @param p_TestingClass
	 *            the test doing the testing
	 * @return an iterable of public method names starting with the prefix
	 * @since Jul 28, 2015
	 */
	public static Iterable<String> getDefinedTestMethods(
			final Class<?> p_TestingClass)
	{
		final SortedSet<String> methods = Sets.newTreeSet();
		for (final Method method : p_TestingClass.getMethods())
		{
			if (!method.getName().startsWith(PREFIX))
			{
				continue;
			}

			final StringBuilder builder = new StringBuilder(method.getName());
			methods.add(builder.toString());
		}
		return methods;
	}

	/**
	 * Get the methods that are listed as being defined by not required.
	 *
	 * @param p_RequiredMethodNames
	 *            the required method names
	 * @param p_DefinedMethodNames
	 *            all defined method names
	 * @return the defined method names not present in the required method names
	 * @since Jul 28, 2015
	 */
	public static Iterable<String> getExtraMethods(
			final Iterable<String> p_RequiredMethodNames,
			final Iterable<String> p_DefinedMethodNames)
	{
		final SortedSet<String> methods = Sets.newTreeSet(p_DefinedMethodNames);
		methods.removeAll(Sets.newHashSet(p_RequiredMethodNames));
		return methods;
	}

	/**
	 * Get the methods that are listed as being required but are not defined.
	 *
	 * @param p_ClassToTest
	 *            the class that is being tested
	 * @param p_TestingClass
	 *            the class that is doing the testing
	 * @return the required method names not present in the defined method names
	 * @since Jul 28, 2015
	 */
	public static Iterable<String> getMissingRequiredTestMethods(
			final Class<?> p_ClassToTest, final Class<?> p_TestingClass)
	{
		final Iterable<String> requiredTestMethods = getRequiredTestMethods(
				p_ClassToTest);
		final Iterable<String> definedTestMethods = getDefinedTestMethods(
				p_TestingClass);
		return getMissingRequiredTestMethods(requiredTestMethods,
				definedTestMethods);
	}

	/**
	 * Get the methods that are listed as being required but are not defined.
	 *
	 * @param p_RequiredMethodNames
	 *            the required method names
	 * @param p_DefinedMethodNames
	 *            all defined method names
	 * @return the required method names not present in the defined method names
	 * @since Jul 28, 2015
	 */
	public static Iterable<String> getMissingRequiredTestMethods(
			final Iterable<String> p_RequiredMethodNames,
			final Iterable<String> p_DefinedMethodNames)
	{
		final SortedSet<String> methods = Sets
				.newTreeSet(p_RequiredMethodNames);
		methods.removeAll(Sets.newHashSet(p_DefinedMethodNames));
		return methods;
	}

	/**
	 * Get the non-private method names that are overloaded (having the same
	 * name
	 *
	 * @param p_Class
	 *            the class to check for declared methods
	 * @return the non-private method names that are duplicated
	 * @since Jul 28, 2015
	 */
	private static Iterable<String> getOverloadedMethods(final Class<?> p_Class)
	{
		final SortedSet<String> methods = Sets.newTreeSet();
		final SortedSet<String> overloadedMethods = Sets.newTreeSet();
		for (final Method method : p_Class.getDeclaredMethods())
		{
			if (Modifier.isPrivate(method.getModifiers()))
			{
				continue;
			}
			final String elementName = method.getName();
			if (methods.contains(elementName))
			{
				overloadedMethods.add(elementName);
			}
			else
			{
				methods.add(elementName);
			}
		}
		return overloadedMethods;
	}

	/**
	 * Get the names of methods that should be defined in the testing class
	 *
	 * @param p_ClassToTest
	 *            the class to be tested
	 * @return the names of testing methods that should be defined by a testing
	 *         class
	 * @since Jul 28, 2015
	 */
	public static Iterable<String> getRequiredTestMethods(
			final Class<?> p_ClassToTest)
	{
		final SortedSet<String> methods = Sets.newTreeSet();
		final SortedSet<String> overloadedMethods = Sets
				.newTreeSet(getOverloadedMethods(p_ClassToTest));
		for (final Method method : p_ClassToTest.getDeclaredMethods())
		{
			final int modifiers = method.getModifiers();
			if (Modifier.isPrivate(modifiers) || Modifier.isAbstract(modifiers))
			{
				continue;
			}

			final String elementName = method.getName();
			if (elementName.contains("$") || elementName.equals("toString"))
			{
				continue;
			}
			if (p_ClassToTest.isEnum())
			{
				/**
				 * Skip valueOf(class, String)
				 */
				final Class<?>[] parameterTypes = method.getParameterTypes();
				if (elementName.equals("valueOf") && parameterTypes.length == 1
						&& parameterTypes[0].getSimpleName().equals("String"))
				{
					continue;
				}
				/**
				 * Skip values() which returns an array of this same type
				 */
				if (elementName.equals("values")
						&& method.getReturnType().isArray()
						&& method.getReturnType().getComponentType()
								.equals(p_ClassToTest))
				{
					continue;
				}
			}

			final StringBuilder builder = new StringBuilder(PREFIX);
			builder.append(Character.toUpperCase(elementName.charAt(0)))
					.append(elementName.substring(1));

			if (overloadedMethods.contains(elementName))
			{
				final Class<?>[] parameterTypes = method.getParameterTypes();
				for (final Class<?> parameter : parameterTypes)
				{
					/**
					 * Do not require compareToObject and compareTo{ThisType}.
					 * Only require only compareTo test.
					 */
					if (elementName.equalsIgnoreCase("compareTo")
							&& parameterTypes.length == 1)
					{
						break;
					}

					String psimpleName = parameter.getSimpleName();
					psimpleName = psimpleName.replace("[]", "Array");
					final StringBuilder psimpleNameBuilder = new StringBuilder(
							psimpleName);
					psimpleNameBuilder.setCharAt(0, Character
							.toUpperCase(psimpleNameBuilder.charAt(0)));
					builder.append(psimpleNameBuilder);
				}

			}
			replaceIllegalCharacters(builder);
			methods.add(builder.toString());
		}
		return methods;
	}

	/**
	 * Invoke the provided method by supplying the provided object and no
	 * arguments.
	 *
	 * @param p_Method
	 *            the method to invoke
	 * @param p_Object
	 *            the object to invoke the method on
	 * @since Nov 13, 2015
	 */
	public static void invokeMethod(final Method p_Method,
			final Object p_Object)
	{
		if (p_Method != null)
		{
			try
			{
				p_Method.invoke(p_Object);
			}
			catch (final IllegalAccessException e)
			{
				e.printStackTrace();
				throw new RuntimeException(String.format("%s: %s",
						p_Method.getName(), e.getMessage()));
			}
			catch (final IllegalArgumentException e)
			{
				e.printStackTrace();
				throw new RuntimeException(String.format("%s: %s",
						p_Method.getName(), e.getMessage()));
			}
			catch (final InvocationTargetException e)
			{
				e.printStackTrace();
				throw new RuntimeException(String.format("%s: %s",
						p_Method.getName(), e.getMessage()));
			}
		}
	}

	/**
	 * From jdt.junit.wizards.NewTestCaseWizardPageOne.java
	 *
	 * @param p_Buffer
	 *            the string buffer
	 * @since Jul 28, 2015
	 */
	private static void replaceIllegalCharacters(final StringBuilder p_Buffer)
	{
		char character = 0;
		for (int index = p_Buffer.length() - 1; index >= 0; index--)
		{
			character = p_Buffer.charAt(index);
			if (Character.isWhitespace(character))
			{
				p_Buffer.deleteCharAt(index);
			}
			else if (character == '<')
			{
				p_Buffer.replace(index, index + 1, "Of");
			}
			else if (character == '?')
			{
				p_Buffer.replace(index, index + 1, "Q");
			}
		}
	}

	/**
	 * Run junit tests on the provided object. Checks for {@code Before},
	 * {@code After}, and {@code Test} annotations on public methods
	 *
	 * @param p_TestingObject
	 *            the object under test
	 * @since Nov 13, 2015
	 */
	public static void runTests(final Object p_TestingObject)
	{
		final SortedSet<Method> methods = Sets
				.newTreeSet((x, y) -> x.getName().compareTo(y.getName()));
		final Class<? extends Object> clazz = p_TestingObject.getClass();
		methods.addAll(Arrays.asList(clazz.getMethods()));

		try
		{
			@SuppressWarnings("unchecked")
			final Class<? extends Annotation> beforeClass = (Class<? extends Annotation>) Class
					.forName("org.junit.Before");
			@SuppressWarnings("unchecked")
			final Class<? extends Annotation> afterClass = (Class<? extends Annotation>) Class
					.forName("org.junit.After");
			@SuppressWarnings("unchecked")
			final Class<? extends Annotation> testClass = (Class<? extends Annotation>) Class
					.forName("org.junit.Test");
			final Method before = Iterables
					.tryFind(methods, method -> method
							.getAnnotationsByType(beforeClass).length > 0)
					.orNull();
			final Method after = Iterables
					.tryFind(methods, method -> method
							.getAnnotationsByType(afterClass).length > 0)
					.orNull();
			for (final Method method : methods)
			{
				if (method.getAnnotationsByType(testClass).length == 0)
				{
					continue;
				}

				invokeMethod(before, p_TestingObject);
				invokeMethod(method, p_TestingObject);
				invokeMethod(after, p_TestingObject);
			}
		}
		catch (final ClassNotFoundException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * @since Jul 28, 2015
	 */
	private Tests()
	{
		/** Nothing for now */
	}

}
