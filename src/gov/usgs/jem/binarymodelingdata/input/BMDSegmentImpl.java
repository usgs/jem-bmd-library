package gov.usgs.jem.binarymodelingdata.input;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.primitives.Ints;
import gov.usgs.jem.binarymodelingdata.BMDSegment;

/**
 * Implements {@link BMDSegment} including {@link #hashCode()} and
 * {@link #equals(Object)}
 *
 * Package-private.
 *
 * @author mckelvym
 * @since Apr 28, 2014
 *
 */
final class BMDSegmentImpl implements BMDSegment
{
	/**
	 * Index at which this appears in the file.
	 *
	 * @since May 16, 2014
	 */
	private final int		m_Index;

	/**
	 * The segment name
	 *
	 * @since Apr 28, 2014
	 */
	private final String	m_SegmentName;

	/**
	 * Create a new instance for the provided segment name
	 *
	 * @param p_Index
	 *            Index at which this appears in the file.
	 * @param p_SegmentName
	 *            the segment name
	 * @since Apr 28, 2014
	 */
	public BMDSegmentImpl(final int p_Index, final String p_SegmentName)
	{
		m_Index = p_Index;
		m_SegmentName = p_SegmentName;
	}

	@Override
	public int compareTo(final BMDSegment p_Other)
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
		if (!(p_Obj instanceof BMDSegment))
		{
			return false;
		}
		return getIndex() == BMDSegment.class.cast(p_Obj).getIndex();
	}

	@Override
	public int getIndex()
	{
		return m_Index;
	}

	@Override
	public String getName()
	{
		return m_SegmentName;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(getIndex());
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(BMDSegment.class)
				.add("name", getName()).toString();
	}
}