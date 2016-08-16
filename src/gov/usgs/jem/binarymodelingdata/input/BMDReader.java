/**
 *
 */
package gov.usgs.jem.binarymodelingdata.input;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TimeZone;

import org.apache.log4j.Level;
import org.eclipse.core.runtime.IProgressMonitor;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;
import com.google.common.collect.TreeBasedTable;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.io.Files;

import gov.usgs.jem.binarymodelingdata.BMDHeader;
import gov.usgs.jem.binarymodelingdata.BMDSegment;
import gov.usgs.jem.binarymodelingdata.BMDTimeStep;
import gov.usgs.jem.binarymodelingdata.BMDVariable;
import gov.usgs.jem.binarymodelingdata.Concentration;
import gov.usgs.jem.binarymodelingdata.Concentrations;

/**
 * Reads WASP BMD output files. Based on clsBMD.vb @
 * http://wrdb.codeplex.com/SourceControl/latest.
 *
 * BMD file structure is configured as follows:
 * <ul>
 * <li>Header (78 bytes)</li>
 *
 * <li>Variables (18 single-octet chars for name followed by 12 single-octet
 * chars for units)</li>
 *
 * <li>Concs ("concentrations": floats arranged by Segment, Time, then Variable)
 * </li>
 *
 * <li>Times (doubles: number of days since seed year)</li>
 *
 * <li>MIN/MAX VAR (float pairs of min/max values for each variable)</li>
 *
 * <li>MIN/MAX SEG-VAR (float pairs of min/max values for each segment and
 * variable)</li>
 *
 * <li>SEGNAMES (15 single-octet char names for each segment)</li>
 * </ul>
 *
 * @author mckelvym
 * @since Apr 17, 2014
 *
 */
public class BMDReader
{
	/**
	 * Query class for getting concentrations data.
	 *
	 * @author mckelvym
	 * @since Apr 22, 2014
	 *
	 */
	private final class ConcentrationsQueryImpl implements ConcentrationsQuery
	{
		/**
		 * Progress reporting monitor
		 *
		 * @since Feb 9, 2015
		 */
		private final List<IProgressMonitor>	m_Monitor;
		/**
		 * The indices into {@link BMDReader#m_Segments} to query
		 *
		 * @since Apr 22, 2014
		 */
		private final SortedSet<Integer>		m_qSegments;

		/**
		 * The indices into {@link BMDReader#m_TimeSteps} to query
		 *
		 * @since Apr 22, 2014
		 */
		private final SortedSet<Integer>		m_qTimeSteps;

		/**
		 * The indices into {@link BMDReader#m_Variables} to query
		 *
		 * @since Apr 22, 2014
		 */
		private final SortedSet<Integer>		m_qVariables;

		/**
		 * Create a new, empty query
		 *
		 * @since Apr 22, 2014
		 */
		private ConcentrationsQueryImpl()
		{
			m_qVariables = Sets.newTreeSet();
			m_qSegments = Sets.newTreeSet();
			m_qTimeSteps = Sets.newTreeSet();
			m_Monitor = Lists.newArrayList();
		}

		/**
		 * Executes the query and returns the results as a
		 * {@link Concentrations} collection
		 *
		 * @return the results of the query.
		 * @throws IOException
		 *             if the concentrations provided in the query could not be
		 *             read
		 * @since Apr 22, 2014
		 */
		@Override
		public Concentrations execute() throws IOException
		{
			return readConcentrations(this);
		}

		/**
		 * Determines if the given variable index, segment index, and time index
		 * are part of the query.
		 *
		 * @param p_VariableIndex
		 *            the variable index (see {@link BMDReader#m_Variables})
		 * @param p_SegmentIndex
		 *            the segment index (see {@link BMDReader#m_Segments})
		 * @param p_TimeIndex
		 *            the time index (see {@link BMDReader#m_TimeSteps})
		 * @return true if this given indices are part of the query
		 * @since Apr 23, 2014
		 */
		private boolean isInQuery(final int p_VariableIndex,
				final int p_SegmentIndex, final int p_TimeIndex)
		{
			return m_qVariables.contains(p_VariableIndex)
					&& m_qSegments.contains(p_SegmentIndex)
					&& m_qTimeSteps.contains(p_TimeIndex);
		}

		@Override
		public String toString()
		{
			return MoreObjects.toStringHelper(ConcentrationsQuery.class)
					.add("numVars", m_qVariables.size())
					.add("numSegs", m_qSegments.size())
					.add("numTSteps", m_qTimeSteps.size()).toString();
		}

		/**
		 * Checks that the query is ready for {@link #execute()}
		 *
		 * @throws IllegalStateException
		 *             if the query is malformed
		 * @since Apr 22, 2014
		 */
		@Override
		public void validate() throws IllegalStateException
		{
			checkState(!m_qVariables.isEmpty(), "No variables specified.");
			checkElementIndex(m_qVariables.first(), m_Variables.size(),
					"Invalid start variable index detected.");
			checkElementIndex(m_qVariables.last(), m_Variables.size(),
					"Invalid end variable index detected.");

			checkState(!m_qSegments.isEmpty(), "No segments specified.");
			checkElementIndex(m_qSegments.first(), m_Segments.size(),
					"Invalid start segment index detected.");
			checkElementIndex(m_qSegments.last(), m_Segments.size(),
					"Invalid end segment index detected.");

			checkState(!m_qTimeSteps.isEmpty(), "No timesteps specified.");
			checkElementIndex(m_qTimeSteps.first(), m_TimeSteps.size(),
					"Invalid start time index detected.");
			checkElementIndex(m_qTimeSteps.last(), m_TimeSteps.size(),
					"Invalid end time index detected.");
		}

		/**
		 * Use all segments in the query.
		 *
		 * @return this
		 * @since Apr 22, 2014
		 */
		@Override
		public ConcentrationsQuery withAllSegments()
		{
			m_qSegments.clear();
			m_qSegments.addAll(
					ContiguousSet.create(Range.closedOpen(0, m_Segments.size()),
							DiscreteDomain.integers()).asList());
			return this;
		}

		/**
		 * Use all time steps in the query.
		 *
		 * @return this
		 * @since Apr 22, 2014
		 */
		@Override
		public ConcentrationsQuery withAllTimeSteps()
		{
			m_qTimeSteps.clear();
			m_qTimeSteps.addAll(getTimeStepIndices());
			return this;
		}

		/**
		 * Use all variables in the query.
		 *
		 * @return this
		 * @since Apr 22, 2014
		 */
		@Override
		public ConcentrationsQuery withAllVariables()
		{
			m_qVariables.clear();
			m_qVariables.addAll(ContiguousSet
					.create(Range.closedOpen(0, m_Variables.size()),
							DiscreteDomain.integers())
					.asList());
			return this;
		}

		@Override
		public ConcentrationsQuery withProgress(
				final IProgressMonitor p_Monitor)
		{
			m_Monitor.clear();
			m_Monitor.add(p_Monitor);
			return this;
		}

		@Override
		public ConcentrationsQuery withSegments(
				final List<BMDSegment> p_Segments)
		{
			checkNotNull(p_Segments, "Invalid argument.");
			for (final BMDSegment segment : p_Segments)
			{
				m_qSegments.add(segment.getIndex());
			}
			return this;
		}

		@Override
		public ConcentrationsQuery withTimeSteps(
				final List<BMDTimeStep> p_TimeSteps)
		{
			checkNotNull(p_TimeSteps, "Invalid argument.");
			for (final BMDTimeStep timeStep : p_TimeSteps)
			{
				m_qTimeSteps.add(timeStep.getIndex());
			}
			return this;
		}

		@Override
		public ConcentrationsQuery withVariables(
				final List<BMDVariable> p_Variables)
		{
			checkNotNull(p_Variables, "Invalid argument.");
			for (final BMDVariable variable : p_Variables)
			{
				m_qVariables.add(variable.getIndex());
			}
			return this;
		}
	}

	/**
	 * Number of bytes representing concentrations values (float)
	 *
	 * @since Apr 23, 2014
	 */
	private static final int				CONCENTRATIONS_SIZE	= 4;

	/**
	 * Class logger
	 */
	private static org.apache.log4j.Logger	log					= org.apache.log4j.Logger
			.getLogger(BMDReader.class);

	/**
	 * Number of bytes representing segment names (15 single-octet chars)
	 *
	 * @since Apr 23, 2014
	 */
	private static final int				SEGMENT_NAME_SIZE	= 15;

	/**
	 * Number of bytes representing time stamps (double
	 *
	 * @since Apr 23, 2014
	 */
	private static final int				TIMESTAMP_SIZE		= 8;

	/**
	 * Number of bytes representing variable names (18 single-octet chars)
	 *
	 * @since Apr 23, 2014
	 */
	private static final int				VARIABLE_NAME_SIZE	= 18;

	/**
	 * Number of bytes representing variable units (12 single-octet chars)
	 *
	 * @since Apr 23, 2014
	 */
	private static final int				VARIABLE_UNIT_SIZE	= 12;

	/**
	 * Used mainly for "toString" implementations, this takes an array
	 * transforms it into a new array that retains the first two elements, adds
	 * ellipses as the third element, and retains the last element as the fourth
	 * output element. For each element, calls its toString() method.
	 *
	 * @param p_Array
	 *            An input array
	 * @return a new array representing an "abbreviated" version of the input
	 *         array
	 * @since Dec 9, 2013
	 */
	public static String[] abbreviate(final Object[] p_Array)
	{
		if (p_Array == null)
		{
			return new String[0];
		}
		if (p_Array.length <= 4)
		{
			final String[] output = new String[p_Array.length];
			for (int i = 0; i < p_Array.length; i++)
			{
				output[i] = String.valueOf(p_Array[i]);
			}
			return output;
		}

		final String[] output = new String[4];
		output[0] = String.valueOf(p_Array[0]);
		output[1] = String.valueOf(p_Array[1]);
		output[2] = "...";
		output[3] = String.valueOf(p_Array[p_Array.length - 1]);
		return output;
	}

	/**
	 * Open the BMD file at the provided path and read its header
	 *
	 * @param p_FilePath
	 *            the path to the BMD file
	 * @return the {@link BMDReader}
	 * @throws IOException
	 *             if the file could not be opened for any reason
	 * @since Apr 21, 2014
	 */
	public static BMDReader open(final String p_FilePath) throws IOException
	{
		log.setLevel(Level.INFO);
		return openInternal(p_FilePath);
	}

	/**
	 * Open the BMD file at the provided path and read its header. Debugging
	 * messages will be logged.
	 *
	 * @param p_FilePath
	 *            the path to the BMD file
	 * @return the {@link BMDReader}
	 * @throws IOException
	 *             if the file could not be opened for any reason
	 * @since Apr 21, 2014
	 */
	public static BMDReader openDebug(final String p_FilePath)
			throws IOException
	{
		return openInternal(p_FilePath);
	}

	/**
	 * Open the BMD file at the provided path and read its header. Debugging
	 * messages will be logged.
	 *
	 * @param p_FilePath
	 *            the path to the BMD file
	 * @return the {@link BMDReader}
	 * @throws IOException
	 *             if the file could not be opened for any reason
	 * @since Apr 21, 2014
	 */
	private static BMDReader openInternal(final String p_FilePath)
			throws IOException
	{
		checkNotNull(p_FilePath, "File path required.");
		checkArgument(
				Objects.equal("bmd",
						Files.getFileExtension(p_FilePath).toLowerCase()),
				"BMD file required, but got %s instead", p_FilePath);
		final BMDReader bmdReader = new BMDReader(p_FilePath);
		bmdReader.readHeader();
		return bmdReader;
	}

	/**
	 * The {@link ByteOrder} to read from the file.
	 *
	 * @since Apr 22, 2014
	 */
	private final ByteOrder						m_ByteOrder;

	/**
	 * Computed after the size of the dimensions are known. This is the number
	 * of bytes into the file where concentrations values can be found.
	 *
	 * @since Apr 18, 2014
	 */
	private long								m_ConcentrationsLocation;

	/**
	 * The data input stream used to read from the file.
	 *
	 * @since Apr 22, 2014
	 */
	private SeekableDataFileInputStream			m_DIS;

	/**
	 * The path to the BMD file
	 *
	 * @see #getFilePath()
	 * @since Apr 22, 2014
	 */
	private final String						m_FilePath;

	/**
	 * The header for the BMD file
	 *
	 * @see #getHeader()
	 * @since Apr 18, 2014
	 */
	private BMDHeader							m_Header;

	/**
	 * Mapping of variable name to maximum value.
	 *
	 * @see #getVariableMax(String)
	 * @since Apr 18, 2014
	 */
	private final Map<String, Float>			m_MaxOverVars;

	/**
	 * Variable name, segment name mapped to maximum value
	 *
	 * @see #getVariableSegmentMax(String, String)
	 * @since Apr 18, 2014
	 */
	private final Table<String, String, Float>	m_MaxOverVarSegs;

	/**
	 * Computed after the size of the dimensions are known. This is the number
	 * of bytes into the file where the min/max values for segments can be
	 * found.
	 *
	 * @since Apr 18, 2014
	 */
	private long								m_MinMaxOverVarSegsLocation;

	/**
	 * Computed after the size of the dimensions are known. This is the number
	 * of bytes into the file where the min/max values for variables can be
	 * found.
	 *
	 * @since Apr 18, 2014
	 */
	private long								m_MinMaxOverVarsLocation;

	/**
	 * Mapping of variable name to minimum value.
	 *
	 * @see #getVariableMin(String)
	 * @since Apr 18, 2014
	 */
	private final Map<String, Float>			m_MinOverVars;

	/**
	 * Variable name, segment name mapped to minimum value
	 *
	 * @see #getVariableSegmentMin(String, String)
	 * @since Apr 18, 2014
	 */
	private final Table<String, String, Float>	m_MinOverVarSegs;

	/**
	 * @see #getSeedDate()
	 * @since Apr 18, 2014
	 */
	private Date								m_SeedDate;

	/**
	 * Computed after the size of the dimensions are known. This is the number
	 * of bytes into the file where the segment names can be found.
	 *
	 * @since Apr 18, 2014
	 */
	private long								m_SegmentNamesLocation;

	/**
	 * @see #getSegments()
	 * @since Apr 18, 2014
	 */
	private final List<BMDSegment>				m_Segments;

	/**
	 * Computed after the size of the dimensions are known. This is the number
	 * of bytes into the file where the time values can be found.
	 *
	 * @since Apr 18, 2014
	 */
	private long								m_TimesLocation;

	/**
	 * @see #getTimeSteps()
	 * @since Apr 18, 2014
	 */
	private final List<BMDTimeStep>				m_TimeSteps;

	/**
	 * @see #getVariables()
	 * @since Apr 18, 2014
	 */
	private final List<BMDVariable>				m_Variables;

	/**
	 * Create a new reader for the BMD file at the provided path
	 *
	 * @param p_FilePath
	 *            path to the BMD file
	 * @since Apr 23, 2014
	 */
	private BMDReader(final String p_FilePath)
	{
		m_FilePath = checkNotNull(p_FilePath);
		m_Variables = Lists.newArrayList();
		m_Segments = Lists.newArrayList();
		m_TimeSteps = Lists.newArrayList();
		m_MinOverVars = Maps.newHashMap();
		m_MaxOverVars = Maps.newHashMap();
		m_MinOverVarSegs = HashBasedTable.create();
		m_MaxOverVarSegs = HashBasedTable.create();
		m_ByteOrder = ByteOrder.LITTLE_ENDIAN;
	}

	/**
	 * Close the reader.
	 *
	 * @throws IOException
	 *             if closing the internal {@link SeekableDataFileInputStream}
	 *             failed
	 * @since Apr 25, 2014
	 */
	public void close() throws IOException
	{
		try
		{
			m_DIS.close();
		}
		catch (final IOException e)
		{
			throw e;
		}
		finally
		{
			m_DIS = null;
		}
	}

	/**
	 * Get the opened file path
	 *
	 * @return the opened file path
	 * @since Apr 23, 2014
	 */
	public String getFilePath()
	{
		validate();
		return m_FilePath;
	}

	/**
	 * Get the file header
	 *
	 * @return the file header
	 * @since Apr 23, 2014
	 */
	public BMDHeader getHeader()
	{
		validate();
		return m_Header;
	}

	/**
	 * Get the seed date
	 *
	 * @return the seed date
	 * @since Apr 23, 2014
	 */
	public Date getSeedDate()
	{
		validate();
		return new Date(m_SeedDate.getTime());
	}

	/**
	 * Get the list of segments. <i>Note: Constructs a new list of new objects
	 * for each call.</i>
	 *
	 * @return the list of segments
	 * @since Apr 23, 2014
	 */
	public ImmutableList<BMDSegment> getSegments()
	{
		validate();
		return ImmutableList.copyOf(m_Segments);
	}

	/**
	 * Creates a new contiguous set of integers starting at 0 and proceeding to
	 * 1 - the size of the times list.
	 *
	 * @return the 0-based timestep indices
	 * @since Apr 22, 2014
	 */
	private ImmutableList<Integer> getTimeStepIndices()
	{
		validate();
		return ContiguousSet.create(Range.closedOpen(0, m_TimeSteps.size()),
				DiscreteDomain.integers()).asList();
	}

	/**
	 * Get the list of time steps. <i>Note: Constructs a new list of new objects
	 * for each call.</i>
	 *
	 * @return the list of time steps
	 * @since Apr 23, 2014
	 */
	public ImmutableList<BMDTimeStep> getTimeSteps()
	{
		validate();
		return ImmutableList.copyOf(m_TimeSteps);
	}

	/**
	 * Get the maximum value, retrieved from the header, for the specified
	 * variable.
	 *
	 * @param p_VariableName
	 *            the variable name
	 * @return the maximum value
	 * @throws IllegalArgumentException
	 *             if the variable name does not exist
	 * @since Apr 23, 2014
	 */
	public Float getVariableMax(final String p_VariableName)
	{
		validate();
		try
		{
			return checkNotNull(
					m_MaxOverVars.get(checkNotNull(p_VariableName,
							"Variable name required.")),
					"No value exists for '%s'", p_VariableName);
		}
		catch (final NullPointerException e)
		{
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	/**
	 * Get the minimum value, retrieved from the header, for the specified
	 * variable.
	 *
	 * @param p_VariableName
	 *            the variable name
	 * @return the minimum value
	 * @throws IllegalArgumentException
	 *             if the variable name does not exist
	 * @since Apr 23, 2014
	 */
	public Float getVariableMin(final String p_VariableName)
	{
		validate();
		try
		{
			return checkNotNull(
					m_MinOverVars.get(checkNotNull(p_VariableName,
							"Variable name required.")),
					"No value exists for '%s'", p_VariableName);
		}
		catch (final NullPointerException e)
		{
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	/**
	 * Get the list of variables. <i>Note: Constructs a new list of new objects
	 * for each call.</i>
	 *
	 * @return the list of variables
	 * @since Apr 23, 2014
	 */
	public ImmutableList<BMDVariable> getVariables()
	{
		validate();
		return ImmutableList.copyOf(m_Variables);
	}

	/**
	 * Get the maximum value, retrieved from the header, for the specified
	 * variable segment
	 *
	 * @param p_VariableName
	 *            the variable name
	 * @param p_SegmentName
	 *            the segment name
	 * @return the maximum value
	 * @throws IllegalArgumentException
	 *             if the variable name or segment name does not exist
	 * @since Apr 23, 2014
	 */
	public Float getVariableSegmentMax(final String p_VariableName,
			final String p_SegmentName)
	{
		validate();
		try
		{
			return checkNotNull(m_MaxOverVarSegs.get(
					checkNotNull(p_VariableName, "Variable name required."),
					checkNotNull(p_SegmentName, "Segment name required.")),
					"No value exists for '%s', '%s'", p_VariableName,
					p_SegmentName);
		}
		catch (final NullPointerException e)
		{
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	/**
	 * Get the minimum value, retrieved from the header, for the specified
	 * variable segment
	 *
	 * @param p_VariableName
	 *            the variable name
	 * @param p_SegmentName
	 *            the segment name
	 * @return the minimum value
	 * @throws IllegalArgumentException
	 *             if the variable name or segment name does not exist
	 * @since Apr 23, 2014
	 */
	public Float getVariableSegmentMin(final String p_VariableName,
			final String p_SegmentName)
	{
		validate();
		try
		{
			return checkNotNull(m_MinOverVarSegs.get(
					checkNotNull(p_VariableName, "Variable name required."),
					checkNotNull(p_SegmentName, "Segment name required.")),
					"No value exists for '%s', '%s'", p_VariableName,
					p_SegmentName);
		}
		catch (final NullPointerException e)
		{
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	/**
	 * Construct a new query for concentrations.
	 *
	 * @return a new {@link ConcentrationsQuery} instance
	 * @since Apr 22, 2014
	 */
	public ConcentrationsQuery newConcentrationsQuery()
	{
		validate();
		return new ConcentrationsQueryImpl();
	}

	/**
	 * Reads the concentrations corresponding to the
	 * {@link ConcentrationsQueryImpl} into a table.
	 *
	 * @param p_Query
	 *            the valid {@link ConcentrationsQueryImpl}
	 * @return the {@link Concentrations} for the provided
	 *         {@link ConcentrationsQueryImpl}
	 * @throws IOException
	 *             if concentations could not be read from the file
	 * @since Apr 22, 2014
	 */
	private Concentrations readConcentrations(
			final ConcentrationsQueryImpl p_Query) throws IOException
	{
		checkNotNull(p_Query, "Query cannot be null.");
		p_Query.validate();
		validate();
		m_DIS.seek((int) m_ConcentrationsLocation);

		/**
		 * Number of bytes in a float (4)
		 */
		final int FBYTES = Float.SIZE / 8;
		/**
		 * Table mapping (variable name, segment name) -> sorted map of (time
		 * step -> value)
		 */
		final Table<BMDVariable, BMDSegment, SortedMap<BMDTimeStep, Float>> results = TreeBasedTable
				.create();

		IProgressMonitor monitor = null;
		if (!p_Query.m_Monitor.isEmpty())
		{
			monitor = p_Query.m_Monitor.get(0);
		}

		if (monitor != null)
		{
			monitor.beginTask("Querying...", m_Header.getTimesSize());
		}

		/**
		 * We can either calculate the skip from the current seek, or just keep
		 * a tally of bytes to skip.
		 *
		 * If calculating skip, use: skip = t * m_SegmentNames.size() *
		 * m_VariableNames.size() FBYTES + s * m_VariableNames.size() * FBYTES +
		 * v * FBYTES
		 */
		int skipBytes = 0;
		for (int timeNum = 0; timeNum < m_Header.getTimesSize(); timeNum++)
		{
			for (int segmentNum = 0; segmentNum < m_Header
					.getSegmentsSize(); segmentNum++)
			{
				for (int variableNum = 0; variableNum < m_Header
						.getVariablesSize(); variableNum++)
				{

					if (p_Query.isInQuery(variableNum, segmentNum, timeNum))
					{
						final int skippedBytes = m_DIS
								.skipBytesAggressive(skipBytes);
						checkState(skippedBytes == skipBytes,
								"Unable to continue reading from the file (tried to skip %s bytes, but could only skip %s)",
								skipBytes, skippedBytes);

						/**
						 * Reset skip count.
						 */
						skipBytes = 0;

						final BMDVariable variable = m_Variables
								.get(variableNum);
						final BMDSegment segment = m_Segments.get(segmentNum);
						final BMDTimeStep timeStep = m_TimeSteps.get(timeNum);
						final float value = m_DIS.readFloat();
						if (!results.contains(variable, segment))
						{
							final SortedMap<BMDTimeStep, Float> timeValues = Maps
									.newTreeMap();
							results.put(variable, segment, timeValues);
						}

						results.get(variable, segment).put(timeStep, value);
					}
					else
					{
						/**
						 * Equivalent of reading one float.
						 */
						skipBytes += FBYTES;
					}
				}
			}

			if (monitor != null)
			{
				if (monitor.isCanceled())
				{
					break;
				}
				monitor.worked(1);
			}
		}

		final ImmutableList<BMDVariable> variables = ImmutableList.copyOf(
				Iterables.filter(m_Variables, new Predicate<BMDVariable>()
				{

					@Override
					public boolean apply(final BMDVariable p_Input)
					{
						return results.containsRow(p_Input);
					}
				}));
		final ImmutableList<BMDSegment> segments = ImmutableList
				.copyOf(Iterables.filter(m_Segments, new Predicate<BMDSegment>()
				{

					@Override
					public boolean apply(final BMDSegment p_Input)
					{
						return results.containsColumn(p_Input);
					}
				}));
		final ImmutableList<BMDTimeStep> timeSteps = ImmutableList.copyOf(
				Iterables.filter(m_TimeSteps, new Predicate<BMDTimeStep>()
				{

					@Override
					public boolean apply(final BMDTimeStep p_Input)
					{
						return p_Query.m_qTimeSteps
								.contains(p_Input.getIndex());
					}
				}));

		return new Concentrations()
		{
			@Override
			public Concentration get(final BMDVariable p_Variable,
					final BMDSegment p_Segment, final BMDTimeStep p_TimeStep)
			{
				checkArgument(results.contains(p_Variable, p_Segment),
						"Invalid variable (%s) or segment (%s) name.",
						p_Variable, p_Segment);
				final SortedMap<BMDTimeStep, Float> timeMap = results
						.get(p_Variable, p_Segment);
				checkArgument(timeMap.containsKey(p_TimeStep),
						"Invalid time step: %s", p_TimeStep);
				final Float value = timeMap.get(p_TimeStep);
				return new ConcentrationImpl(p_Variable, p_Segment, p_TimeStep,
						value);
			}

			@Override
			public ImmutableList<BMDSegment> getSegments()
			{
				return segments;
			}

			@Override
			public ImmutableList<BMDTimeStep> getTimeSteps()
			{
				return timeSteps;
			}

			@Override
			public ImmutableList<BMDVariable> getVariables()
			{
				return variables;
			}

			@Override
			public Iterator<Concentration> iterator()
			{
				return new UnmodifiableIterator<Concentration>()
				{
					/**
					 * The time steps and values for a particular variable and
					 * segment
					 *
					 * @since Apr 23, 2014
					 */
					Cell<BMDVariable, BMDSegment, SortedMap<BMDTimeStep, Float>>					m_Cell			= null;

					/**
					 * Iterates the variables and segments
					 *
					 * @since Apr 23, 2014
					 */
					final Iterator<Cell<BMDVariable, BMDSegment, SortedMap<BMDTimeStep, Float>>>	m_CellIterator	= results
							.cellSet().iterator();

					/**
					 * Time steps and values iterator
					 *
					 * @since Apr 23, 2014
					 */
					Iterator<Entry<BMDTimeStep, Float>>												m_TimeIterator	= null;

					@Override
					public boolean hasNext()
					{
						return m_TimeIterator != null
								&& m_TimeIterator.hasNext()
								|| m_CellIterator.hasNext();
					}

					@Override
					public Concentration next()
					{
						if (!hasNext())
						{
							throw new NoSuchElementException();
						}
						if (m_TimeIterator == null || !m_TimeIterator.hasNext())
						{
							m_Cell = m_CellIterator.next();
							m_TimeIterator = m_Cell.getValue().entrySet()
									.iterator();
						}

						final Entry<BMDTimeStep, Float> nextEntry = m_TimeIterator
								.next();
						final BMDTimeStep p_TimeStep = nextEntry.getKey();
						final Float value = nextEntry.getValue();
						return new ConcentrationImpl(m_Cell.getRowKey(),
								m_Cell.getColumnKey(), p_TimeStep, value);
					}
				};
			}
		};
	}

	/**
	 * Reads the header from the file, initializing the {@link #m_DIS},
	 * {@link #m_Header} fields and retrieving the variable names, variable
	 * units, segment names, min/max over variables, and min/max over variable
	 * segments.
	 *
	 * Times are not retrieved (aside from header information).
	 *
	 * @throws IOException
	 *             if the header does not match expectations
	 * @since Apr 21, 2014
	 */
	private void readHeader() throws IOException
	{
		final BMDHeader.Builder headerBuilder = BMDHeader.builder();
		log.debug(String.format("Open %s", m_FilePath));
		m_DIS = new SeekableDataFileInputStreamImpl(m_FilePath, m_ByteOrder);

		try
		{
			/**
			 * Read header. Do not modify the order that these local variables
			 * are declared as the data read process is order-dependent.
			 */
			final String signature = new String(m_DIS.readCharsAsAscii(3));
			final String sourceType = new String(m_DIS.readCharsAsAscii(1));
			final String producer = new String(m_DIS.readCharsAsAscii(1));
			final float version = m_DIS.readFloat();
			final int oldSeedTime = m_DIS.readUInt32();
			final int seedSecond = m_DIS.readInt();
			final int seedJDay = m_DIS.readInt();
			final String spaces = new String(
					m_DIS.readCharsAsAscii(BMDHeader.SPACE_SIZE));
			final int numSegments = m_DIS.readInt();
			final int numTimes = m_DIS.readInt();
			final int numVars = m_DIS.readInt();
			final double startTime = m_DIS.readDouble();
			final double endTime = m_DIS.readDouble();
			try
			{
				m_Header = headerBuilder.withSignature(signature)
						.withSourceType(sourceType).withProducer(producer)
						.withVersion(version).withOldSeedTime(oldSeedTime)
						.withSeedSecond(seedSecond).withSeedJDay(seedJDay)
						.withSpaces(spaces).withNumSegments(numSegments)
						.withNumTimes(numTimes).withNumVars(numVars)
						.withStartTime(startTime).withEndTime(endTime).build();
				log.debug(m_Header);
			}
			catch (final Exception e)
			{
				final String message = "Unable to read header from file: "
						+ m_FilePath;
				throw new IOException(message, e);
			}

			/**
			 * Initialize the seed date.
			 */
			final TimeZone timeZone = TimeZone.getTimeZone("UTC");
			final Calendar cal = Calendar.getInstance();
			cal.setTimeZone(timeZone);

			if (seedJDay != 0 && oldSeedTime == 0)
			{
				/**
				 * WRDB: Constant was determined by trial and error to force
				 * match with MOVEM
				 */
				final int DATESHIFT = 1721439;
				/**
				 * Extract fudge-factor determined in the translation from VB
				 * source to Java. When comparing dates to those within WRDB,
				 * the day of month was two days less than it should have been.
				 */
				final int DATESHIFT2 = -2;

				/**
				 * JulianToGregorian
				 */
				final int julian = seedJDay;
				final int totalDays = julian - (DATESHIFT + DATESHIFT2);

				cal.set(Calendar.YEAR, 1);
				cal.set(Calendar.MONTH, Calendar.JANUARY);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);

				cal.add(Calendar.DATE, totalDays);
				cal.add(Calendar.SECOND, seedSecond);
			}
			else if (oldSeedTime != 0)
			{
				cal.set(Calendar.YEAR, 1901);
				cal.set(Calendar.MONTH, Calendar.JANUARY);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);

				cal.add(Calendar.SECOND, oldSeedTime);
			}
			else
			{
				cal.set(Calendar.YEAR, 1970);
				cal.set(Calendar.MONTH, Calendar.JANUARY);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
			}
			m_SeedDate = cal.getTime();
			final SimpleDateFormat dateFormatUTC = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
			log.debug(String.format("Seed Date: %s",
					dateFormatUTC.format(m_SeedDate)));

			/**
			 * Read variable names and units
			 */
			for (int variableNum = 0; variableNum < m_Header
					.getVariablesSize(); variableNum++)
			{
				final String variableName = new String(
						m_DIS.readCharsAsAscii(VARIABLE_NAME_SIZE)).trim();
				final String variableUnits = new String(
						m_DIS.readCharsAsAscii(VARIABLE_UNIT_SIZE)).trim();
				String pCode = variableName.replaceFirst("\\(.*", "").trim()
						.toUpperCase();
				pCode = pCode.substring(0, Math.min(10, pCode.length())).trim();

				final BMDVariableImpl variable = new BMDVariableImpl(
						variableNum, variableName, variableUnits, pCode);
				m_Variables.add(variable);
				log.debug(String.format(
						"Variable #%s: '%s';\tPCode: '%s'\tUnits: '%s'",
						variableNum + 1, variableName, pCode, variableUnits));
			}

			/**
			 * Once all the dimensions are known, compute the location of
			 * various blocks of data
			 */
			m_ConcentrationsLocation = BMDHeader.LOCATION_VARIABLES
					+ (long) m_Header.getVariablesSize()
							* (VARIABLE_NAME_SIZE + VARIABLE_UNIT_SIZE);
			m_TimesLocation = m_ConcentrationsLocation
					+ (long) m_Header.getVariablesSize()
							* m_Header.getSegmentsSize()
							* m_Header.getTimesSize() * CONCENTRATIONS_SIZE;
			m_MinMaxOverVarsLocation = m_TimesLocation
					+ (long) m_Header.getTimesSize() * TIMESTAMP_SIZE;
			m_MinMaxOverVarSegsLocation = m_MinMaxOverVarsLocation
					+ (long) m_Header.getVariablesSize() * CONCENTRATIONS_SIZE
							* 2;
			m_SegmentNamesLocation = m_MinMaxOverVarSegsLocation
					+ (long) m_Header.getVariablesSize()
							* m_Header.getSegmentsSize() * CONCENTRATIONS_SIZE
							* 2;
			log.debug(MoreObjects.toStringHelper("Locations: ")
					.add("concs", m_ConcentrationsLocation)
					.add("times", m_TimesLocation)
					.add("minMaxVars", m_MinMaxOverVarsLocation)
					.add("minMaxVarSegs", m_MinMaxOverVarSegsLocation)
					.add("segNames", m_SegmentNamesLocation).toString());

			/**
			 * skip past times and concs and read stuff at bottom
			 */
			m_DIS.seek((int) m_MinMaxOverVarsLocation);

			/**
			 * Read min/max
			 */
			for (int variableNum = 0; variableNum < m_Header
					.getVariablesSize(); variableNum++)
			{
				final String variableName = m_Variables.get(variableNum)
						.getName();
				final float min = m_DIS.readFloat();
				final float max = m_DIS.readFloat();
				m_MinOverVars.put(variableName, min);
				m_MaxOverVars.put(variableName, max);
			}

			/**
			 * Read min/max per segment, but segment names have not been
			 * determined yet.
			 */
			final Map<String, List<Float>> minOverVarSegs = Maps.newHashMap();
			final Map<String, List<Float>> maxOverVarSegs = Maps.newHashMap();
			for (int variableNum = 0; variableNum < m_Header
					.getVariablesSize(); variableNum++)
			{
				final String variableName = m_Variables.get(variableNum)
						.getName();
				final List<Float> minOverSegs = Lists.newArrayList();
				final List<Float> maxOverSegs = Lists.newArrayList();
				for (int segmentNum = 0; segmentNum < m_Header
						.getSegmentsSize(); segmentNum++)
				{
					final float min = m_DIS.readFloat();
					final float max = m_DIS.readFloat();
					minOverSegs.add(min);
					maxOverSegs.add(max);
				}

				minOverVarSegs.put(variableName, minOverSegs);
				maxOverVarSegs.put(variableName, maxOverSegs);
			}

			/**
			 * Read segment names, BUT they might not be present.
			 */
			try
			{
				for (int segmentNum = 0; segmentNum < m_Header
						.getSegmentsSize(); segmentNum++)
				{
					final String segmentName = new String(
							m_DIS.readCharsAsAscii(SEGMENT_NAME_SIZE)).trim();

					final BMDSegmentImpl segment = new BMDSegmentImpl(
							segmentNum, segmentName);
					m_Segments.add(segment);
					log.debug(String.format("Segment #%s: '%s'", segmentNum + 1,
							segmentName));
				}
			}
			catch (final Exception e)
			{
				log.warn("Unable to read segment names, but that might be ok.",
						e);
				for (int segmentNum = 0; segmentNum < m_Header
						.getSegmentsSize(); segmentNum++)
				{
					final String segmentName = String.format("Segment %s",
							segmentNum);

					final BMDSegmentImpl segment = new BMDSegmentImpl(
							segmentNum, segmentName);
					m_Segments.add(segment);
					log.debug(String.format("Segment #%s: '%s'", segmentNum + 1,
							segmentName));
				}
			}

			/**
			 * From original source: WASP 7.x BMD files omit the last two
			 * characters so you can get duplicate segment names and don't know
			 * the K values. WASP always writes out the top layers first
			 * (highest K values) then works down to the bottom (K=1) Look for
			 * segment names formatted like "I=iii J=jjj K" and add the correct
			 * K value automatically. Then, reformat it so it looks like the
			 * WASP 8 format (I=xxxJ=xxxK=xxx)
			 */
			int kMax = 0;
			int lastJ = Integer.MAX_VALUE;
			if (!m_Segments.isEmpty())
			{
				final Splitter splitter = Splitter.on("=");
				List<String> splitToList = splitter
						.splitToList(m_Segments.get(0).getName());
				boolean doFormat = splitToList.size() >= 2
						&& splitToList.size() <= 3;
				if (doFormat)
				{
					for (final BMDSegment segment : m_Segments)
					{
						/**
						 * This is carried over from original source.
						 */
						splitToList = splitter.splitToList(segment.getName());
						doFormat = splitToList.size() >= 2
								&& splitToList.size() <= 3;
						if (!doFormat)
						{
							continue;
						}
						// int i = Integer.valueOf(segmentName.substring(2,
						// 2+3));
						final Integer j = Integer
								.valueOf(segment.getName().substring(8, 8 + 3));
						if (j < lastJ)
						{
							kMax++;
							lastJ = j;
						}
					}

					lastJ = Integer.MAX_VALUE;
					int k = kMax + 1;
					for (int segmentNum = 0; segmentNum < m_Header
							.getSegmentsSize(); segmentNum++)
					{
						final String segmentName = m_Segments.get(segmentNum)
								.getName();
						/**
						 * This is carried over from original source.
						 */
						splitToList = splitter.splitToList(segmentName);
						doFormat = splitToList.size() >= 2
								&& splitToList.size() <= 3;
						if (!doFormat)
						{
							continue;
						}
						final Integer i = Integer
								.valueOf(segmentName.substring(2, 2 + 3));
						final Integer j = Integer
								.valueOf(segmentName.substring(8, 8 + 3));
						if (j < lastJ)
						{
							k--;
							lastJ = j;
							m_Segments.set(segmentNum, new BMDSegmentImpl(
									segmentNum,
									String.format("I=%03dJ=%03dK=%03d", i, j, k)
											.trim()));
						}
					}
				}
			}

			/**
			 * Update the min/max per segment now that we have segment names.
			 */
			for (int variableNum = 0; variableNum < m_Header
					.getVariablesSize(); variableNum++)
			{
				final String variableName = m_Variables.get(variableNum)
						.getName();
				for (int segmentNum = 0; segmentNum < m_Header
						.getSegmentsSize(); segmentNum++)
				{
					final String segmentName = m_Segments.get(segmentNum)
							.getName();
					final Float minVarSeg = minOverVarSegs.get(variableName)
							.get(segmentNum);
					final Float maxVarSeg = maxOverVarSegs.get(variableName)
							.get(segmentNum);
					m_MinOverVarSegs.put(variableName, segmentName, minVarSeg);
					m_MaxOverVarSegs.put(variableName, segmentName, maxVarSeg);
				}
			}

			/**
			 * Read times
			 */
			m_DIS.seek((int) m_TimesLocation);
			final List<Double> rawTimes = Lists.newArrayList();
			for (int timeNum = 0; timeNum < m_Header.getTimesSize(); timeNum++)
			{
				final double t = m_DIS.readDouble();
				rawTimes.add(t);

			}
			log.debug(String.format("Times: %s",
					Arrays.toString(abbreviate(rawTimes.toArray()))));

			/**
			 * Calculate and store the "dates"
			 */
			final int secsPerDay = 60 * 60 * 24;
			for (int timeNum = 0; timeNum < rawTimes.size(); timeNum++)
			{
				final double timeValue = rawTimes.get(timeNum);
				cal.setTime(m_SeedDate);
				final int addSecs = (int) Math.round(secsPerDay * timeValue);
				cal.add(Calendar.SECOND, addSecs);
				final long time = cal.getTime().getTime();

				final BMDTimeStepImpl timeStep = new BMDTimeStepImpl(timeNum,
						time, timeValue);
				m_TimeSteps.add(timeStep);
			}
			log.debug(String.format("Dates (first, last): (%s, %s)",
					dateFormatUTC
							.format(new Date(m_TimeSteps.get(0).getTime())),
					dateFormatUTC.format(new Date(
							Iterables.getLast(m_TimeSteps).getTime()))));
		}
		catch (final Throwable t)
		{
			log.error("Error reading file.", t);
			close();
		}
	}

	/**
	 * Validate the reader.
	 *
	 * @throws IllegalStateException
	 *             if the {@link SeekableDataFileInputStream} instance was not
	 *             initialized
	 * @since Apr 22, 2014
	 */
	private void validate() throws IllegalStateException
	{
		checkState(m_DIS != null, "File is not open.");
	}
}
