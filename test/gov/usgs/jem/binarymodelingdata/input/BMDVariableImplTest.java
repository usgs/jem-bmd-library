package gov.usgs.jem.binarymodelingdata.input;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import gov.usgs.jem.binarymodelingdata.AllTests;
import gov.usgs.jem.binarymodelingdata.BMDVariable;

/**
 * Tests {@link BMDVariableImpl}
 *
 * @author mckelvym
 * @since Aug 18, 2016
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BMDVariableImplTest
{
	/**
	 * @throws java.lang.Exception
	 * @since Aug 18, 2016
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		final Class<?> classToTest = BMDVariableImpl.class;
		final Class<?> testingClass = BMDVariableImplTest.class;
		AllTests.assertHasRequiredMethods(classToTest, testingClass);

	}

	private final int		m_Index;
	private final String	m_Name;
	private final String	m_PCode;
	private final String	m_Units;
	private BMDVariable		m_Val;

	{
		m_Name = "Name";
		m_Units = "Units";
		m_PCode = "PCode";
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
		p_TestEquals.accept(m_Val,
				new BMDVariableImpl(m_Index, m_Name, m_Units, m_PCode));
		p_TestNotEqual.accept(m_Val,
				new BMDVariableImpl(m_Index + 1, m_Name, m_Units, m_PCode));
	}

	/**
	 *
	 * @throws java.lang.Exception
	 * @since Aug 18, 2016
	 */
	@Before
	public void setUp() throws Exception
	{
		m_Val = new BMDVariableImpl(m_Index, m_Name, m_Units, m_PCode);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDVariableImpl#compareTo(gov.usgs.jem.binarymodelingdata.BMDVariable)}.
	 */
	@Test
	public final void testCompareTo()
	{
		Assert.assertTrue(m_Val.compareTo(
				new BMDVariableImpl(m_Index, m_Name, m_Units, m_PCode)) == 0);
		Assert.assertTrue(m_Val.compareTo(new BMDVariableImpl(m_Index + 1,
				m_Name, m_Units, m_PCode)) < 0);
		Assert.assertTrue(m_Val.compareTo(new BMDVariableImpl(m_Index - 1,
				m_Name, m_Units, m_PCode)) > 0);
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
	 *
	 * /** Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDVariableImpl#getIndex()}.
	 */
	@Test
	public final void testGetIndex()
	{
		Assert.assertEquals(m_Index, m_Val.getIndex());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDVariableImpl#getName()}.
	 */
	@Test
	public final void testGetName()
	{
		Assert.assertEquals(m_Name, m_Val.getName());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDVariableImpl#getPCode()}.
	 */
	@Test
	public final void testGetPCode()
	{
		Assert.assertEquals(m_PCode, m_Val.getPCode());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDVariableImpl#getUnits()}.
	 */
	@Test
	public final void testGetUnits()
	{
		Assert.assertEquals(m_Units, m_Val.getUnits());
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
