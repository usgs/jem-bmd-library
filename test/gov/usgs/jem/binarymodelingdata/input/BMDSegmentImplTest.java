package gov.usgs.jem.binarymodelingdata.input;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.usgs.jem.binarymodelingdata.AllTests;
import gov.usgs.jem.binarymodelingdata.BMDSegment;

/**
 * Tests {@link BMDSegmentImpl}
 *
 * @author mckelvym
 * @since Aug 18, 2016
 *
 */
public class BMDSegmentImplTest
{

	/**
	 * @throws java.lang.Exception
	 * @since Aug 18, 2016
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		final Class<?> classToTest = BMDSegmentImpl.class;
		final Class<?> testingClass = BMDSegmentImplTest.class;
		AllTests.assertHasRequiredMethods(classToTest, testingClass);
	}

	private final int		m_Index;
	private final String	m_Name;
	private BMDSegment		m_Val;

	{
		m_Name = "Name";
		m_Index = 0;
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
	 * @since Aug 18, 2016
	 */
	private void equalityTests(
			final java.util.function.BiConsumer<Object, Object> p_TestEquals,
			final java.util.function.BiConsumer<Object, Object> p_TestNotEqual)
			throws Exception
	{
		p_TestEquals.accept(m_Val, m_Val);
		p_TestEquals.accept(m_Val, new BMDSegmentImpl(m_Index, m_Name));
		p_TestNotEqual.accept(m_Val, new BMDSegmentImpl(m_Index + 1, m_Name));
	}

	/**
	 * @throws java.lang.Exception
	 * @since Aug 18, 2016
	 */
	@Before
	public void setUp() throws Exception
	{
		m_Val = new BMDSegmentImpl(m_Index, m_Name);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDSegmentImpl#compareTo(gov.usgs.jem.binarymodelingdata.BMDSegment)}.
	 */
	@Test
	public final void testCompareTo()
	{
		Assert.assertTrue(
				m_Val.compareTo(new BMDSegmentImpl(m_Index, m_Name)) == 0);
		Assert.assertTrue(
				m_Val.compareTo(new BMDSegmentImpl(m_Index + 1, m_Name)) < 0);
		Assert.assertTrue(
				m_Val.compareTo(new BMDSegmentImpl(m_Index - 1, m_Name)) > 0);
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
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDSegmentImpl#getIndex()}.
	 */
	@Test
	public final void testGetIndex()
	{
		Assert.assertEquals(m_Index, m_Val.getIndex());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDSegmentImpl#getName()}.
	 */
	@Test
	public final void testGetName()
	{
		Assert.assertEquals(m_Name, m_Val.getName());
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
