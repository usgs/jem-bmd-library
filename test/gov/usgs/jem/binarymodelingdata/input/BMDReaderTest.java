package gov.usgs.jem.binarymodelingdata.input;

import java.io.IOException;
import java.util.Calendar;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.common.collect.Sets;

import gov.usgs.jem.binarymodelingdata.AllTests;
import gov.usgs.jem.binarymodelingdata.BMDHeader;
import gov.usgs.jem.binarymodelingdata.BMDSegment;
import gov.usgs.jem.binarymodelingdata.BMDTimeStep;
import gov.usgs.jem.binarymodelingdata.BMDVariable;
import gov.usgs.jem.binarymodelingdata.Concentrations;

/**
 * Tests {@link BMDReader}
 *
 * @author mckelvym
 * @since Aug 18, 2016
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BMDReaderTest
{

	/**
	 * @throws java.lang.Exception
	 * @since Aug 18, 2016
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		final Class<?> classToTest = BMDReader.class;
		final Class<?> testingClass = BMDReaderTest.class;
		AllTests.assertHasRequiredMethods(classToTest, testingClass);
	}

	private double		m_MaxTime;
	private double		m_MinTime;
	private BMDReader	m_Reader;

	/**
	 *
	 * @throws java.lang.Exception
	 * @since Aug 18, 2016
	 */
	@Before
	public void setUp() throws Exception
	{
		m_Reader = BMDReader.open(AllTests.getTestFile().getAbsolutePath());
		m_MinTime = 169.00;
		m_MaxTime = 171.71;
	}

	/**
	 *
	 * @throws java.lang.Exception
	 * @since Aug 18, 2016
	 */
	@After
	public void tearDown() throws Exception
	{
		m_Reader.close();
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#close()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testClose() throws IOException
	{
		m_Reader.close();
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#getFilePath()}.
	 */
	@Test
	public final void testGetFilePath()
	{
		Assert.assertEquals(AllTests.getTestFile().getAbsolutePath(),
				m_Reader.getFilePath());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#getHeader()}.
	 */
	@Test
	public final void testGetHeader()
	{
		final BMDHeader header = m_Reader.getHeader();
		Assert.assertNotNull(header);
		final double endTime = header.getEndTime();
		final long oldSeedTime = header.getOldSeedTime();
		final String producer = header.getProducer();
		final int seedJDay = header.getSeedJDay();
		final int seedSecond = header.getSeedSecond();
		final int segmentsSize = header.getSegmentsSize();
		final String signature = header.getSignature();
		final String sourceType = header.getSourceType();
		final double startTime = header.getStartTime();
		final int timesSize = header.getTimesSize();
		final int variablesSize = header.getVariablesSize();
		final float version = header.getVersion();

		Assert.assertEquals(m_MaxTime, endTime, Double.MIN_NORMAL);
		Assert.assertEquals(3060997200L, oldSeedTime);
		Assert.assertEquals("\t", producer);
		Assert.assertEquals(0, seedJDay);
		Assert.assertEquals(0, seedSecond);
		Assert.assertEquals(40, segmentsSize);
		Assert.assertEquals("BMD", signature);
		Assert.assertEquals(new String(new byte[] { 0x0F }), sourceType);
		Assert.assertEquals(m_MinTime, startTime, Double.MIN_NORMAL);
		Assert.assertEquals(66, timesSize);
		Assert.assertEquals(17, variablesSize);
		Assert.assertEquals(2.0, version, Float.MIN_NORMAL);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#getSeedDate()}.
	 */
	@Test
	public final void testGetSeedDate()
	{
		final Calendar instance = Calendar.getInstance();
		instance.set(1997, 11, 31, 0, 0);
		instance.set(Calendar.SECOND, 0);
		instance.set(Calendar.MILLISECOND, 0);
		Assert.assertEquals(instance.getTime(), m_Reader.getSeedDate());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#getSegments()}.
	 */
	@Test
	public final void testGetSegments()
	{
		final List<BMDSegment> segments = m_Reader.getSegments();
		final int numSegments = 40;
		Assert.assertEquals(numSegments, segments.size());

		final Set<String> expecteds = Sets.newHashSet();
		IntStream.range(1, numSegments + 1)
				.mapToObj(x -> String.format("Seg %s", x))
				.forEach(expecteds::add);
		final Set<String> actuals = segments.stream().map(BMDSegment::getName)
				.collect(Collectors.toSet());
		expecteds.forEach(x -> Assert.assertTrue(actuals.contains(x)));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#getTimeSteps()}.
	 */
	@Test
	public final void testGetTimeSteps()
	{
		final List<BMDTimeStep> timesteps = m_Reader.getTimeSteps();
		final int numTimesteps = 66;
		Assert.assertEquals(numTimesteps, timesteps.size());

		final DoubleSummaryStatistics summaryStatistics = timesteps.stream()
				.mapToDouble(BMDTimeStep::getValue).summaryStatistics();
		Assert.assertEquals(m_MinTime, summaryStatistics.getMin(),
				Double.MIN_NORMAL);
		Assert.assertEquals(m_MaxTime, summaryStatistics.getMax(),
				Double.MIN_NORMAL);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#getVariableMax(java.lang.String)}.
	 */
	@Test
	public final void testGetVariableMax()
	{
		Assert.assertEquals(27.79, m_Reader.getVariableMax("Distance (mi)"),
				0.01);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#getVariableMin(java.lang.String)}.
	 */
	@Test
	public final void testGetVariableMin()
	{
		Assert.assertEquals(0.0, m_Reader.getVariableMin("ALGAE"),
				Float.MIN_NORMAL);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#getVariables()}.
	 */
	@Test
	public final void testGetVariables()
	{
		final List<BMDVariable> variables = m_Reader.getVariables();
		final int numVariables = 17;
		Assert.assertEquals(numVariables, variables.size());

		final Set<String> expecteds = Sets.newHashSet("ALGAE", "COLIFORM BACT",
				"DO", "FE", "MN", "PO4", "ORGANIC-P", "NO3-N", "NH3-N",
				"Organic N", "CBODNS", "Temperature", "Hydraulic Depth",
				"Flow (cfs)", "Velocity (ft/se", "Stage (ft)", "Distance (mi)");
		final Set<String> actuals = variables.stream().map(BMDVariable::getName)
				.collect(Collectors.toSet());
		expecteds.forEach(x -> Assert.assertTrue(actuals.contains(x)));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#getVariableSegmentMax(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetVariableSegmentMax()
	{
		Assert.assertEquals(0.0,
				m_Reader.getVariableSegmentMin("ALGAE", "Seg 16"), 0.001);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#getVariableSegmentMin(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetVariableSegmentMin()
	{
		Assert.assertEquals(15.30,
				m_Reader.getVariableSegmentMin("Distance (mi)", "Seg 16"),
				0.00001);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#newConcentrationsQuery()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testNewConcentrationsQuery() throws IOException
	{
		final ConcentrationsQuery query = m_Reader.newConcentrationsQuery();
		Assert.assertNotNull(query);
		final List<BMDSegment> segments = m_Reader.getSegments().stream()
				.filter(x -> x.getIndex() == 0).collect(Collectors.toList());
		final List<BMDTimeStep> timesteps = m_Reader.getTimeSteps().stream()
				.filter(x -> x.getIndex() == 0).collect(Collectors.toList());
		final List<BMDVariable> variables = m_Reader.getVariables().stream()
				.filter(x -> x.getIndex() == 0).collect(Collectors.toList());
		final Concentrations concentrations = query.withSegments(segments)
				.withTimeSteps(timesteps).withVariables(variables).execute();
		concentrations.forEach(x -> Assert.assertEquals(27.80f, x.getValue(),
				Float.MIN_NORMAL));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#open(java.lang.String)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testOpen()
	{
		try (BMDReader open = BMDReader
				.open(AllTests.getTestFile().getAbsolutePath());)
		{
			Assert.assertNotNull(open);
		}
		catch (final Throwable t)
		{
			t.printStackTrace();
			Assert.fail("Unable to open file.");
		}
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.BMDReader#openDebug(java.lang.String)}.
	 */
	@SuppressWarnings("static-method")
	@Test
	public final void testOpenDebug()
	{
		try (BMDReader open = BMDReader
				.openDebug(AllTests.getTestFile().getAbsolutePath());)
		{
			Assert.assertNotNull(open);
		}
		catch (final Throwable t)
		{
			t.printStackTrace();
			Assert.fail("Unable to open file.");
		}
	}

}
