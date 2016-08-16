package gov.usgs.jem.binarymodelingdata.input;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.primitives.Ints;

import gov.usgs.jem.binarymodelingdata.BMDVariable;

/**
 * Implements {@link BMDVariable} including {@link #hashCode()} and
 * {@link #equals(Object)}
 *
 * Package-private.
 *
 * @author mckelvym
 * @since Apr 28, 2014
 *
 */
class BMDVariableImpl implements BMDVariable
{
	/**
	 * Index at which this appears in the file.
	 *
	 * @since May 16, 2014
	 */
	private final int		m_Index;

	/**
	 * @see #getPCode()
	 * @since May 16, 2014
	 */
	private final String	m_PCode;

	/**
	 * @see #getUnits()
	 * @since May 16, 2014
	 */
	private final String	m_Units;

	/**
	 * The variable name
	 *
	 * @since Apr 28, 2014
	 */
	private final String	m_VariableName;

	/**
	 * Create a new instance for the provided variable name.
	 *
	 * @param p_Index
	 *            Index at which this appears in the file.
	 * @param p_VariableName
	 *            the variable name
	 * @param p_Units
	 *            variable units
	 * @param p_PCode
	 *            pcode for variable
	 * @since Apr 28, 2014
	 */
	public BMDVariableImpl(final int p_Index, final String p_VariableName,
			final String p_Units, final String p_PCode)
	{
		m_Index = p_Index;
		m_VariableName = p_VariableName;
		m_Units = p_Units;
		m_PCode = p_PCode;
	}

	@Override
	public int compareTo(final BMDVariable p_Other)
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
		if (!(p_Obj instanceof BMDVariable))
		{
			return false;
		}
		return getIndex() == BMDVariable.class.cast(p_Obj).getIndex();
	}

	@Override
	public int getIndex()
	{
		return m_Index;
	}

	@Override
	public String getName()
	{
		return m_VariableName;
	}

	@Override
	public String getPCode()
	{
		return m_PCode;
	}

	@Override
	public String getUnits()
	{
		return m_Units;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(getIndex());
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(BMDVariable.class)
				.add("name", getName()).toString();
	}
}