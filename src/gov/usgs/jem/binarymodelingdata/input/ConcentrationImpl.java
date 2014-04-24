package gov.usgs.jem.binarymodelingdata.input;

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
	 * @see #getSegmentName()
	 * @since Apr 23, 2014
	 */
	private final String	m_SegmentName;

	/**
	 * @see #getTime()
	 * @since Apr 23, 2014
	 */
	private final double	m_Time;

	/**
	 * @see #getTimeStep()
	 * @since Apr 23, 2014
	 */
	private final int		m_TimeStep;

	/**
	 * @see #getValue()
	 * @since Apr 23, 2014
	 */
	private final float		m_Value;

	/**
	 * @see #getVariableName()
	 * @since Apr 23, 2014
	 */
	private final String	m_VariableName;

	/**
	 * Create a new instance for the provided parameters
	 * 
	 * @param p_VariableName
	 *            variable name
	 * @param p_SegmentName
	 *            segment name
	 * @param p_Time
	 *            time value
	 * @param p_TimeStep
	 *            time index
	 * @param p_Value
	 *            concentration value
	 * @since Apr 23, 2014
	 */
	public ConcentrationImpl(final String p_VariableName,
			final String p_SegmentName, final double p_Time,
			final int p_TimeStep, final float p_Value)
	{
		super();
		m_VariableName = p_VariableName;
		m_SegmentName = p_SegmentName;
		m_Time = p_Time;
		m_TimeStep = p_TimeStep;
		m_Value = p_Value;
	}

	@Override
	public int compareTo(final Concentration p_Other)
	{
		return ComparisonChain.start()
				.compare(getVariableName(), p_Other.getVariableName())
				.compare(getSegmentName(), p_Other.getSegmentName())
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
		return Objects.equal(getVariableName(), o.getVariableName())
				&& Objects.equal(getSegmentName(), o.getSegmentName())
				&& getTimeStep() == o.getTimeStep();
	}

	@Override
	public String getSegmentName()
	{
		return m_SegmentName;
	}

	@Override
	public double getTime()
	{
		return m_Time;
	}

	@Override
	public int getTimeStep()
	{
		return m_TimeStep;
	}

	@Override
	public float getValue()
	{
		return m_Value;
	}

	@Override
	public String getVariableName()
	{
		return m_VariableName;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(getVariableName(), getSegmentName(),
				getTimeStep());
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(Concentration.class)
				.add("variable", getVariableName())
				.add("segment", getSegmentName())
				.add("timeStep", getTimeStep()).add("time", getTime())
				.add("value", getValue()).toString();
	}

}
