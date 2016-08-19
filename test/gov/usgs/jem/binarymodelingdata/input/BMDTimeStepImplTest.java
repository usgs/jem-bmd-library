package gov.usgs.jem.binarymodelingdata.input;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import gov.usgs.jem.binarymodelingdata.AllTests;
import gov.usgs.jem.binarymodelingdata.BMDTimeStep;

/**
 * Tests {@link BMDTimeStepImpl}
 *
 * @author mckelvym
 * @since Aug 18, 2016
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BMDTimeStepImplTest
{

	/**
	 * @throws java.lang.Exception
	 * @since Aug 18, 2016
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		final Class<?> classToTest = BMDTimeStepImpl.class;
		final Class<?> testingClass = BMDTimeStepImplTest.class;
		AllTests.assertHasRequiredMethods(classToTest, testingClass);

	}

	private final int		m_Index;
	private final double	m_RawValue;
	private final long		m_TimeMS;
	private BMDTimeStep		m_Val;

	{
		m_Index = 0;
		m_TimeMS = 0L;
		m_RawValue = 0.0;
	}

	/**
	 * Creates one or more scenarios to compare the equality of two objects.
	 *
	 * @param p_TestEquals
	 *            should test that the two provided objects are equal, either
	 *            via the {@link Object#equals(Object)} method or by comparing
	 *            their {@link Object#hashCode()} values.
	 * @param p_TestNotEqual
	 *            should test that the two provided objects are <b>NOT</b>
	 *            equal, either via the {@link Object#equals(Object)} method or
	 *            by comparing their {@link Object#hashCode()} values.
	 * @throws Exception
	 * @since Aug 19, 2016
	 */
	private void equalityTests(
			final java.util.function.BiConsumer<Object, Object> p_TestEquals,
			final java.util.function.BiConsumer<Object, Object> p_TestNotEqual)
			throws Exception
	{
		p_TestEquals.accept(m_Val, m_Val);
		p_TestEquals.accept(m_Val,
				new BMDTimeStepImpl(m_Index, m_TimeMS, m_RawValue));
		p_TestNotEqual.accept(m_Val,
				new BMDTimeStepImpl(m_Index + 1, m_TimeMS, m_RawValue));
	}

	/**
	 * @throws java.lang.Exception
	 * @since Aug 18, 2016
	 */
	@Before
	public void setUp() throws Exception
	{
		m_Val = new BMDTimeStepImpl(m_Index, m_TimeMS, m_RawValue);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDTimeStepImpl#compareTo(gov.usgs.jem.binarymodelingdata.BMDTimeStep)}.
	 */
	@Test
	public final void testCompareTo()
	{
		Assert.assertTrue(m_Val.compareTo(
				new BMDTimeStepImpl(m_Index, m_TimeMS, m_RawValue)) == 0);
		Assert.assertTrue(m_Val.compareTo(
				new BMDTimeStepImpl(m_Index + 1, m_TimeMS, m_RawValue)) < 0);
		Assert.assertTrue(m_Val.compareTo(
				new BMDTimeStepImpl(m_Index - 1, m_TimeMS, m_RawValue)) > 0);
	}

	@Test
	public final void testEquals() throws Exception
	{
		final java.util.function.BiConsumer<Object, Object> testEquals = (same,
				alsosame) ->
		{
			org.junit.Assert.assertEquals(same, alsosame);
		};
		final java.util.function.BiConsumer<Object, Object> testNotEqual = (one,
				two) ->
		{
			org.junit.Assert.assertNotEquals(one, two);
		};
		equalityTests(testEquals, testNotEqual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDTimeStepImpl#getIndex()}.
	 */
	@Test
	public final void testGetIndex()
	{
		Assert.assertEquals(m_Index, m_Val.getIndex());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDTimeStepImpl#getTime()}.
	 */
	@Test
	public final void testGetTime()
	{
		Assert.assertEquals(m_TimeMS, m_Val.getTime());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDTimeStepImpl#getValue()}.
	 */
	@Test
	public final void testGetValue()
	{
		Assert.assertEquals(m_RawValue, m_Val.getValue(), Double.MIN_NORMAL);
	}

	@Test
	public final void testHashCode() throws Exception
	{
		final java.util.function.BiConsumer<Object, Object> testEquals = (same,
				alsosame) ->
		{
			org.junit.Assert.assertEquals(same.hashCode(), alsosame.hashCode());
		};
		final java.util.function.BiConsumer<Object, Object> testNotEqual = (one,
				two) ->
		{
			org.junit.Assert.assertNotEquals(one.hashCode(), two.hashCode());
		};
		equalityTests(testEquals, testNotEqual);
	}
}
