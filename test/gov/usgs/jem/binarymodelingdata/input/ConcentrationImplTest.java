package gov.usgs.jem.binarymodelingdata.input;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import gov.usgs.jem.binarymodelingdata.AllTests;
import gov.usgs.jem.binarymodelingdata.BMDSegment;
import gov.usgs.jem.binarymodelingdata.BMDTimeStep;
import gov.usgs.jem.binarymodelingdata.BMDVariable;
import gov.usgs.jem.binarymodelingdata.Concentration;

/**
 * Tests {@link ConcentrationImpl}
 *
 * @author mckelvym
 * @since Aug 19, 2016
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConcentrationImplTest
{

	/**
	 *
	 * @throws java.lang.Exception
	 *             if unexpected condition causing test failure
	 * @since Aug 19, 2016
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		final Class<?> classToTest = ConcentrationImpl.class;
		final Class<?> testingClass = ConcentrationImplTest.class;
		AllTests.assertHasRequiredMethods(classToTest, testingClass);

	}

	private Concentration	m_Concentration;
	private BMDSegment		m_Segment;
	private BMDTimeStep		m_TimeStep;
	private float			m_Value;
	private BMDVariable		m_Variable;

	/**
	 * Create a new instance of {@link Concentration} that differs from the one
	 * in {@link #setUp()} by changing the segment
	 *
	 * @param p_Inc
	 *            the segment index change
	 * @since Aug 19, 2016
	 */
	private Concentration concentrationWithSegChange(final int p_Inc)
	{
		final Concentration concentrationWithSegChange = new ConcentrationImpl(
				m_Variable,
				new BMDSegmentImpl(m_Segment.getIndex() + p_Inc, "Seg"),
				m_TimeStep, m_Value);
		return concentrationWithSegChange;
	}

	/**
	 * Create a new instance of {@link Concentration} that differs from the one
	 * in {@link #setUp()} by changing the timestep
	 *
	 * @param p_Inc
	 *            the timestep index change
	 * @return the new instance
	 * @since Aug 19, 2016
	 */
	private Concentration concentrationWithTimeChange(final int p_Inc)
	{
		final Concentration concentrationWithTimeChange = new ConcentrationImpl(
				m_Variable, m_Segment,
				new BMDTimeStepImpl(m_TimeStep.getIndex() + p_Inc, 0L, 0.0),
				m_Value);
		return concentrationWithTimeChange;
	}

	/**
	 * Create a new instance of {@link Concentration} that differs from the one
	 * in {@link #setUp()} by changing the variable
	 *
	 * @param p_Inc
	 *            the variable index change
	 * @since Aug 19, 2016
	 */
	private Concentration concentrationWithVarChange(final int p_Inc)
	{
		final Concentration concentrationWithVarChange = new ConcentrationImpl(
				new BMDVariableImpl(m_Variable.getIndex() + p_Inc, "V", "U",
						"P"),
				m_Segment, m_TimeStep, m_Value);
		return concentrationWithVarChange;
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
		p_TestEquals.accept(m_Concentration, m_Concentration);
		p_TestEquals.accept(m_Concentration, new ConcentrationImpl(m_Variable,
				m_Segment, m_TimeStep, m_Value));
		p_TestNotEqual.accept(m_Concentration, concentrationWithSegChange(1));
		p_TestNotEqual.accept(m_Concentration, concentrationWithVarChange(1));
		p_TestNotEqual.accept(m_Concentration, concentrationWithTimeChange(1));
	}

	/**
	 * @throws java.lang.Exception
	 *             if unexpected condition causing test failure
	 * @since Aug 19, 2016
	 */
	@Before
	public void setUp() throws Exception
	{
		m_Variable = new BMDVariableImpl(0, "Var", "Units", "PCode");
		m_Segment = new BMDSegmentImpl(0, "Seg");
		m_TimeStep = new BMDTimeStepImpl(0, 0L, 0.0);
		m_Value = 0.0f;
		m_Concentration = new ConcentrationImpl(m_Variable, m_Segment,
				m_TimeStep, m_Value);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.ConcentrationImpl#compareTo(gov.usgs.jem.binarymodelingdata.Concentration)}.
	 */
	@Test
	public final void testCompareTo()
	{
		Assert.assertTrue(
				m_Concentration.compareTo(new ConcentrationImpl(m_Variable,
						m_Segment, m_TimeStep, m_Value)) == 0);
		for (final int direction : new int[] { -1, 1 })
		{
			final int inc = -direction;

			final Concentration concentrationWithVarChange = concentrationWithVarChange(
					inc);
			Assert.assertTrue(m_Concentration
					.compareTo(concentrationWithVarChange) == direction);

			final Concentration concentrationWithSegChange = concentrationWithSegChange(
					inc);
			Assert.assertTrue(m_Concentration
					.compareTo(concentrationWithSegChange) == direction);

			final Concentration concentrationWithTimeChange = concentrationWithTimeChange(
					inc);
			Assert.assertTrue(m_Concentration
					.compareTo(concentrationWithTimeChange) == direction);
		}
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
	 * {@link gov.usgs.jem.binarymodelingdata.input.ConcentrationImpl#getSegment()}.
	 */
	@Test
	public final void testGetSegment()
	{
		Assert.assertEquals(m_Segment, m_Concentration.getSegment());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.ConcentrationImpl#getTimeStep()}.
	 */
	@Test
	public final void testGetTimeStep()
	{
		Assert.assertEquals(m_TimeStep, m_Concentration.getTimeStep());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.ConcentrationImpl#getValue()}.
	 */
	@Test
	public final void testGetValue()
	{
		Assert.assertEquals(m_Value, m_Concentration.getValue(),
				Float.MIN_NORMAL);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.ConcentrationImpl#getVariable()}.
	 */
	@Test
	public final void testGetVariable()
	{
		Assert.assertEquals(m_Variable, m_Concentration.getVariable());
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
