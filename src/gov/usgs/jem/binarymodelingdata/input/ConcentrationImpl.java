package gov.usgs.jem.binarymodelingdata.input;

import gov.usgs.jem.binarymodelingdata.BMDSegment;
import gov.usgs.jem.binarymodelingdata.BMDTimeStep;
import gov.usgs.jem.binarymodelingdata.BMDVariable;
import gov.usgs.jem.binarymodelingdata.Concentration;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

/**
 * Package-private implementation of {@link Concentration}
 * 
 * @author mckelvym
 * @since Apr 23, 2014
 * 
 */
final class ConcentrationImpl implements Concentration
{
	/**
	 * @see #getSegment()
	 * @since Apr 23, 2014
	 */
	private final BMDSegment	m_Segment;

	/**
	 * @see #getTimeStep()
	 * @since Apr 23, 2014
	 */
	private final BMDTimeStep	m_TimeStep;

	/**
	 * @see #getValue()
	 * @since Apr 23, 2014
	 */
	private final float			m_Value;

	/**
	 * @see #getVariable()
	 * @since Apr 23, 2014
	 */
	private final BMDVariable	m_Variable;

	/**
	 * Create a new instance for the provided parameters
	 * 
	 * @param p_Variable
	 *            variable
	 * @param p_Segment
	 *            segment
	 * @param p_TimeStep
	 *            time step
	 * @param p_Value
	 *            concentration value
	 * @since Apr 23, 2014
	 */
	public ConcentrationImpl(final BMDVariable p_Variable,
			final BMDSegment p_Segment, final BMDTimeStep p_TimeStep,
			final float p_Value)
	{
		super();
		m_Variable = p_Variable;
		m_Segment = p_Segment;
		m_TimeStep = p_TimeStep;
		m_Value = p_Value;
	}

	@Override
	public int compareTo(final Concentration p_Other)
	{
		return ComparisonChain.start()
				.compare(getVariable(), p_Other.getVariable())
				.compare(getSegment(), p_Other.getSegment())
				.compare(getTimeStep(), p_Other.getTimeStep()).result();
	}

	@Override
	public boolean equals(final Object p_Object)
	{
		if (this == p_Object)
		{
			return true;
		}
		if (!(p_Object instanceof Concentration))
		{
			return false;
		}
		final Concentration o = Concentration.class.cast(p_Object);
		return Objects.equal(getVariable(), o.getVariable())
				&& Objects.equal(getSegment(), o.getSegment())
				&& Objects.equal(getTimeStep(), o.getTimeStep());
	}

	@Override
	public BMDSegment getSegment()
	{
		return m_Segment;
	}

	@Override
	public BMDTimeStep getTimeStep()
	{
		return m_TimeStep;
	}

	@Override
	public float getValue()
	{
		return m_Value;
	}

	@Override
	public BMDVariable getVariable()
	{
		return m_Variable;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(getVariable(), getSegment(), getTimeStep());
	}

	@Override
	public String toString()
	{
		return String.valueOf(getValue());
	}

}
