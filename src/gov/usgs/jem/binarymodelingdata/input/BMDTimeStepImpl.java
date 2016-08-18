package gov.usgs.jem.binarymodelingdata.input;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.primitives.Ints;

import gov.usgs.jem.binarymodelingdata.BMDTimeStep;

/**
 * Implements {@link BMDTimeStep} including {@link #hashCode()} and
 * {@link #equals(Object)}
 *
 * Package-private.
 *
 * @author mckelvym
 * @since Apr 28, 2014
 *
 */
final class BMDTimeStepImpl implements BMDTimeStep
{
	/**
	 * Index at which this appears in the file.
	 *
	 * @since May 16, 2014
	 */
	private final int		m_Index;

	/**
	 * The raw time value in the file.
	 *
	 * @since May 16, 2014
	 */
	private final double	m_RawValue;

	/**
	 * The time value in ms
	 *
	 * @since Apr 28, 2014
	 */
	private final Long		m_TimeMS;

	/**
	 * Create a new instance for the provided time value in ms.
	 *
	 * @param p_Index
	 *            Index at which this appears in the file.
	 * @param p_TimeMS
	 *            the milleseconds timestamp
	 * @param p_RawValue
	 *            the raw value read from the file
	 * @since Apr 28, 2014
	 */
	public BMDTimeStepImpl(final int p_Index, final Long p_TimeMS,
			final double p_RawValue)
	{
		m_Index = p_Index;
		m_TimeMS = p_TimeMS;
		m_RawValue = p_RawValue;
	}

	@Override
	public int compareTo(final BMDTimeStep p_Other)
	{
		return Ints.compare(getIndex(), p_Other.getIndex());
	}

	@Override
	public boolean equals(final Object p_Obj)
	{
		if (this == p_Obj)
		{
			return true;
		}
		if (!(p_Obj instanceof BMDTimeStep))
		{
			return false;
		}
		return getIndex() == BMDTimeStep.class.cast(p_Obj).getIndex();
	}

	@Override
	public int getIndex()
	{
		return m_Index;
	}

	@Override
	public long getTime()
	{
		return m_TimeMS;
	}

	@Override
	public double getValue()
	{
		return m_RawValue;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(getIndex());
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(BMDTimeStep.class)
				.add("time", getTime()).toString();
	}
}