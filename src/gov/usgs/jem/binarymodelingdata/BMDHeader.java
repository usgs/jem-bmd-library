package gov.usgs.jem.binarymodelingdata;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;

/**
 * The BMD header structure. Use {@link #builder()} to construct a new header
 * structure.
 *
 * @author mckelvym
 * @since Apr 17, 2014
 *
 */
public final class BMDHeader
{
	/**
	 * Builds new instances of {@link BMDHeader}
	 *
	 * @author mckelvym
	 * @since Apr 18, 2014
	 *
	 */
	public static class Builder
	{
		private double	m_bEndTime;
		private int		m_bNumSegments;
		private int		m_bNumTimes;
		private int		m_bNumVars;
		private long	m_bOldSeedTime;
		private String	m_bProducer;
		private int		m_bSeedJDay;
		private int		m_bSeedSecond;
		private String	m_bSignature;
		private String	m_bSourceType;
		private String	m_bSpaces;
		private double	m_bStartTime;
		private float	m_bVersion;

		/**
		 * @since Apr 18, 2014
		 */
		private Builder()
		{

		}

		public BMDHeader build() throws Exception
		{
			return new BMDHeader(this);
		}

		/**
		 * Set the end time field
		 *
		 * @param p_EndTime
		 *            the end time field
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withEndTime(final double p_EndTime)
		{
			m_bEndTime = p_EndTime;
			return this;
		}

		/**
		 * Set the number of segments
		 *
		 * @param p_NumSegments
		 *            the number of segments
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withNumSegments(final int p_NumSegments)
		{
			m_bNumSegments = p_NumSegments;
			return this;
		}

		/**
		 * Set the number of time steps
		 *
		 * @param p_NumTimes
		 *            the number of time steps
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withNumTimes(final int p_NumTimes)
		{
			m_bNumTimes = p_NumTimes;
			return this;
		}

		/**
		 * Set the number of variables
		 *
		 * @param p_NumVars
		 *            the number of variables.
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withNumVars(final int p_NumVars)
		{
			m_bNumVars = p_NumVars;
			return this;
		}

		/**
		 * Set the old seed time
		 *
		 * @param p_OldSeedTime
		 *            the old seed time
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withOldSeedTime(final long p_OldSeedTime)
		{
			m_bOldSeedTime = p_OldSeedTime;
			return this;
		}

		/**
		 * Set the producer, a 1-character string
		 *
		 * @param p_Producer
		 *            the producer, a 1-character string
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withProducer(final String p_Producer)
		{
			m_bProducer = p_Producer;
			return this;
		}

		/**
		 * Set the seed julian day
		 *
		 * @param p_SeedJDay
		 *            the seed julian day
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withSeedJDay(final int p_SeedJDay)
		{
			m_bSeedJDay = p_SeedJDay;
			return this;
		}

		/**
		 * Set the seed second
		 *
		 * @param p_SeedSecond
		 *            the seed second
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withSeedSecond(final int p_SeedSecond)
		{
			m_bSeedSecond = p_SeedSecond;
			return this;
		}

		/**
		 * Set the signature
		 *
		 * @param p_Signature
		 *            the signature, a string of 3 characters
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withSignature(final String p_Signature)
		{
			m_bSignature = p_Signature;
			return this;
		}

		/**
		 * Set the source type
		 *
		 * @param p_SourceType
		 *            the source type, a string of 1-character
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withSourceType(final String p_SourceType)
		{
			m_bSourceType = p_SourceType;
			return this;
		}

		/**
		 * Set the spaces field
		 *
		 * @param p_Spaces
		 *            the spaces field
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withSpaces(final String p_Spaces)
		{
			m_bSpaces = p_Spaces;
			return this;
		}

		/**
		 * Set the start time field
		 *
		 * @param p_StartTime
		 *            the start time field
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withStartTime(final double p_StartTime)
		{
			m_bStartTime = p_StartTime;
			return this;
		}

		/**
		 * Set the version field
		 *
		 * @param p_Version
		 *            the version field
		 * @return this builder
		 * @since Apr 18, 2014
		 */
		public Builder withVersion(final float p_Version)
		{
			m_bVersion = p_Version;
			return this;
		}
	}

	/**
	 * The number of bytes location for the end time field
	 *
	 * @since Apr 17, 2014
	 */
	public static final long	LOCATION_END_TIME		= 70;

	/**
	 * The number of bytes location for the number of segments field
	 *
	 * @since Apr 17, 2014
	 */
	public static final long	LOCATION_NUM_SEGMENTS	= 50;

	/**
	 * The number of bytes location for the number of times field
	 *
	 * @since Apr 17, 2014
	 */
	public static final long	LOCATION_NUM_TIMES		= 54;

	/**
	 * The number of bytes location for the number of variables field
	 *
	 * @since Apr 17, 2014
	 */
	public static final long	LOCATION_NUM_VARS		= 58;

	/**
	 * The number of bytes location for the seed time field
	 *
	 * @since Apr 17, 2014
	 */
	public static final long	LOCATION_SEED_TIME		= 9;

	/**
	 * The number of bytes location for the start time field
	 *
	 * @since Apr 17, 2014
	 */
	public static final long	LOCATION_START_TIME		= 62;

	/**
	 * The number of bytes location for the variables
	 *
	 * @since Apr 17, 2014
	 */
	public static final long	LOCATION_VARIABLES		= 78;

	/**
	 * Number of bytes for the space field
	 *
	 * @since Apr 17, 2014
	 */
	public static final int		SPACE_SIZE				= 29;

	/**
	 * Creates a new, empty builder
	 *
	 * @return a new builder instance.
	 * @since Apr 18, 2014
	 */
	public static Builder builder()
	{
		return new Builder();
	}

	/**
	 * The end time stamp
	 *
	 * @since Apr 17, 2014
	 */
	private final double	m_EndTime;

	/**
	 * The number of segments in the file.
	 *
	 * @since Apr 17, 2014
	 */
	private final int		m_NumSegments;

	/**
	 * The number of time stamps in the files
	 *
	 * @since Apr 17, 2014
	 */
	private final int		m_NumTimes;

	/**
	 * The number of variables in the file.
	 *
	 * @since Apr 17, 2014
	 */
	private final int		m_NumVars;

	/**
	 * The old seed time field.
	 *
	 * "this is for older BMD files that used a single unsigned long value relative to 1/1/1970 but may have problems with DST"
	 *
	 * @since Apr 17, 2014
	 */
	private final long		m_OldSeedTime;

	/**
	 * The producer field: VBFixedString(1)
	 *
	 * @since Apr 17, 2014
	 */
	private final String	m_Producer;

	/**
	 * The julian seed day field.
	 *
	 * "the following are a new representation of the seed time that is based on the julian day and second"
	 *
	 * @since Apr 17, 2014
	 */
	private final int		m_SeedJDay;

	/**
	 * The seed second field
	 *
	 * "the following are a new representation of the seed time that is based on the julian day and second"
	 *
	 * @since Apr 17, 2014
	 */
	private final int		m_SeedSecond;

	/**
	 * The signature field: VBFixedString(3)
	 *
	 * @since Apr 17, 2014
	 */
	private final String	m_Signature;

	/**
	 * The source type field: VBFixedString(1)
	 *
	 * @since Apr 17, 2014
	 */
	private final String	m_SourceType;

	/**
	 * The spaces field: VBFixedString({@link #SPACE_SIZE})
	 *
	 * @since Apr 17, 2014
	 */
	private final String	m_Spaces;

	/**
	 * The start time stamp
	 *
	 * @since Apr 17, 2014
	 */
	private final double	m_StartTime;

	/**
	 * The version field
	 *
	 * @since Apr 17, 2014
	 */
	private final float		m_Version;

	/**
	 * Create a new header instance from the provided builder.
	 *
	 * @since Apr 17, 2014
	 */
	private BMDHeader(final Builder p_Builder)
	{
		m_Signature = p_Builder.m_bSignature;
		m_SourceType = p_Builder.m_bSourceType;
		m_Producer = p_Builder.m_bProducer;
		m_Version = p_Builder.m_bVersion;
		m_OldSeedTime = p_Builder.m_bOldSeedTime;
		m_SeedSecond = p_Builder.m_bSeedSecond;
		m_SeedJDay = p_Builder.m_bSeedJDay;
		m_Spaces = p_Builder.m_bSpaces;
		m_NumSegments = p_Builder.m_bNumSegments;
		m_NumTimes = p_Builder.m_bNumTimes;
		m_NumVars = p_Builder.m_bNumVars;
		m_StartTime = p_Builder.m_bStartTime;
		m_EndTime = p_Builder.m_bEndTime;

		boolean condition;

		checkNotNull(m_Signature, "Signature field cannot be null.");
		condition = m_Signature.length() == 3;
		checkArgument(condition, "Signature field must be 3 characters");

		checkNotNull(m_SourceType, "Source type field cannot be null.");
		condition = m_SourceType.length() == 1;
		checkArgument(condition, "Source type field must be 1-character");

		checkNotNull(m_Producer, "Producer field cannot be null.");
		condition = m_Producer.length() == 1;
		checkArgument(condition, "Producer field must be 1-character");

		condition = m_OldSeedTime >= 0;
		checkArgument(condition, "Old seed time must be at least 0.");

		condition = m_SeedSecond >= 0;
		checkArgument(condition, "Seed second field must be at least 0.");

		condition = m_SeedJDay >= 0;
		checkArgument(condition, "Seed jday field must be at least 0.");

		checkNotNull(m_Spaces, "Spaces field cannot be null.");
		condition = m_Spaces.length() == SPACE_SIZE;
		checkArgument(condition, "Spaces field length must be %s characters",
				SPACE_SIZE);

		condition = m_NumSegments >= 0;
		checkArgument(condition, "Number of segments field must be at least 0.");

		condition = m_NumTimes >= 0;
		checkArgument(condition, "Number of times field must be at least 0.");

		condition = m_NumVars >= 0;
		checkArgument(condition,
				"Number of variables field must be at least 0.");

		condition = m_StartTime >= 0;
		checkArgument(condition, "Start time field must be at least 0.");

		condition = m_EndTime >= 0 && m_EndTime >= m_StartTime;
		checkArgument(condition,
				"End time field must be at least 0 and greater than or equal to start time");
	}

	/**
	 * Get the start time field
	 *
	 * @return the start time field
	 * @since Apr 18, 2014
	 */
	public double getEndTime()
	{
		return m_EndTime;
	}

	/**
	 * Get the old seed time
	 *
	 * @return the old seed time
	 * @since Apr 18, 2014
	 */
	public long getOldSeedTime()
	{
		return m_OldSeedTime;
	}

	/**
	 * Get the producer field
	 *
	 * @return the producer field
	 * @since Apr 18, 2014
	 */
	public String getProducer()
	{
		return m_Producer;
	}

	/**
	 * Get the julian seed day
	 *
	 * @return the julian seed day
	 * @since Apr 18, 2014
	 */
	public int getSeedJDay()
	{
		return m_SeedJDay;
	}

	/**
	 * Get the seed second
	 *
	 * @return the seed second
	 * @since Apr 18, 2014
	 */
	public int getSeedSecond()
	{
		return m_SeedSecond;
	}

	/**
	 * Get the number of segments
	 *
	 * @return the number of segments
	 * @since Apr 18, 2014
	 */
	public int getSegmentsSize()
	{
		return m_NumSegments;
	}

	/**
	 * Get the signature field
	 *
	 * @return the signature field
	 * @since Apr 18, 2014
	 */
	public String getSignature()
	{
		return m_Signature;
	}

	/**
	 * Get the source type field
	 *
	 * @return the source type field
	 * @since Apr 18, 2014
	 */
	public String getSourceType()
	{
		return m_SourceType;
	}

	/**
	 * Get the spaces field
	 *
	 * @return the spaces field
	 * @since Apr 18, 2014
	 */
	public String getSpaces()
	{
		return m_Spaces;
	}

	/**
	 * Get the start time field
	 *
	 * @return the start time field
	 * @since Apr 18, 2014
	 */
	public double getStartTime()
	{
		return m_StartTime;
	}

	/**
	 * Get the number of time steps
	 *
	 * @return the number of time steps
	 * @since Apr 18, 2014
	 */
	public int getTimesSize()
	{
		return m_NumTimes;
	}

	/**
	 * Get the number of variables
	 *
	 * @return the number of variables
	 * @since Apr 18, 2014
	 */
	public int getVariablesSize()
	{
		return m_NumVars;
	}

	/**
	 * Get the version field
	 *
	 * @return the version field
	 * @since Apr 18, 2014
	 */
	public float getVersion()
	{
		return m_Version;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName()
				+ "\n"
				+ Joiner.on("\n").join(
						Splitter.on(",").splitToList(
								MoreObjects.toStringHelper("")
										.add("signature", m_Signature)
										.add("sourceType", m_SourceType)
										.add("producer", m_Producer)
										.add("version", m_Version)
										.add("oldSeedTime", m_OldSeedTime)
										.add("seedSecond", m_SeedSecond)
										.add("seedJDay", m_SeedJDay)
										.add("spaces", m_Spaces)
										.add("numSegments", m_NumSegments)
										.add("numTimes", m_NumTimes)
										.add("numVariables", m_NumVars)
										.add("startTime", m_StartTime)
										.add("endTime", m_EndTime).toString()));
	}

}
