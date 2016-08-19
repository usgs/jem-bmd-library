package gov.usgs.jem.binarymodelingdata;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Tests {@link BMDHeader}
 *
 * @author mckelvym
 * @since Aug 18, 2016
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BMDHeaderTest
{

	private static final Random r;

	static
	{
		r = new Random(System.currentTimeMillis());
	}

	private static void expectFailure(final BMDHeader.Builder p_Builder)
	{
		try
		{
			p_Builder.build();
			Assert.fail("Not trapping bad input.");
		}
		catch (final Throwable t)
		{
			/**
			 * Expected
			 */
		}
	}

	/**
	 * @return the next random unsigned int value
	 * @since Aug 19, 2016
	 */
	private static int nextUInt()
	{
		int v = r.nextInt();
		while (v < 0)
		{
			v = r.nextInt();
		}
		return v;
	}

	/**
	 * @return the next random unsigned long value
	 * @since Aug 19, 2016
	 */
	private static long nextULong()
	{
		long v = r.nextLong();
		while (v < 0)
		{
			v = r.nextLong();
		}
		return v;
	}

	/**
	 * @throws java.lang.Exception
	 *             if unexpected condition causing test failure
	 * @since Aug 18, 2016
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		final Class<?> classToTest = BMDHeader.class;
		final Class<?> testingClass = BMDHeaderTest.class;
		AllTests.assertHasRequiredMethods(classToTest, testingClass);
	}

	private BMDHeader.Builder m_Builder;

	/**
	 * @throws java.lang.Exception
	 *             if unexpected condition causing test failure
	 * @since Aug 18, 2016
	 */
	@Before
	public void setUp() throws Exception
	{
		m_Builder = BMDHeader.builder().withSignature("ABC").withSourceType("D")
				.withProducer("E").withOldSeedTime(0L).withSeedSecond(0)
				.withSeedJDay(0)
				.withSpaces(new String(new char[BMDHeader.SPACE_SIZE]))
				.withNumSegments(0).withNumTimes(0).withNumVars(0)
				.withStartTime(0.0).withEndTime(0.0).withVersion(0.0f);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#builder()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testBuilder() throws Exception
	{
		Assert.assertNotNull(BMDHeader.builder());
		Assert.assertNotNull(m_Builder.build());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getEndTime()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetEndTime() throws Exception
	{
		final double v = r.nextDouble();
		Assert.assertEquals(v, m_Builder.withEndTime(v).build().getEndTime(),
				Double.MIN_NORMAL);

		expectFailure(m_Builder.withEndTime(-1));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getOldSeedTime()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetOldSeedTime() throws Exception
	{
		final long v = nextULong();
		Assert.assertEquals(v,
				m_Builder.withOldSeedTime(v).build().getOldSeedTime());

		expectFailure(m_Builder.withOldSeedTime(-1));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getProducer()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetProducer() throws Exception
	{
		final String v = "P";
		Assert.assertEquals(v, m_Builder.withProducer(v).build().getProducer());

		expectFailure(m_Builder.withProducer(null));
		expectFailure(m_Builder.withProducer(""));
		expectFailure(m_Builder.withProducer("PP"));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getSeedJDay()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetSeedJDay() throws Exception
	{
		final int v = nextUInt();
		Assert.assertEquals(v, m_Builder.withSeedJDay(v).build().getSeedJDay());

		expectFailure(m_Builder.withSeedJDay(-1));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getSeedSecond()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetSeedSecond() throws Exception
	{
		final int v = nextUInt();
		Assert.assertEquals(v,
				m_Builder.withSeedSecond(v).build().getSeedSecond());

		expectFailure(m_Builder.withSeedSecond(-1));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getSegmentsSize()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetSegmentsSize() throws Exception
	{
		final int v = nextUInt();
		Assert.assertEquals(v,
				m_Builder.withNumSegments(v).build().getSegmentsSize());

		expectFailure(m_Builder.withNumSegments(-1));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getSignature()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetSignature() throws Exception
	{
		final String v = "Sig";
		Assert.assertEquals(v,
				m_Builder.withSignature(v).build().getSignature());

		expectFailure(m_Builder.withSignature(null));
		expectFailure(m_Builder.withSignature(""));
		expectFailure(m_Builder.withSignature("P"));
		expectFailure(m_Builder.withSignature("PPPP"));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getSourceType()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetSourceType() throws Exception
	{
		final String v = "S";
		Assert.assertEquals(v,
				m_Builder.withSourceType(v).build().getSourceType());

		expectFailure(m_Builder.withSourceType(null));
		expectFailure(m_Builder.withSourceType(""));
		expectFailure(m_Builder.withSourceType("PPPP"));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getSpaces()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetSpaces() throws Exception
	{
		final String v = new String(new char[BMDHeader.SPACE_SIZE]);
		Assert.assertEquals(v, m_Builder.withSpaces(v).build().getSpaces());

		expectFailure(m_Builder.withSpaces(null));
		expectFailure(m_Builder.withSpaces(""));
		expectFailure(m_Builder.withSpaces("PPPP"));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getStartTime()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetStartTime() throws Exception
	{
		final double v = r.nextDouble();
		Assert.assertEquals(v, m_Builder.withStartTime(v).withEndTime(v + 1.0)
				.build().getStartTime(), Double.MIN_NORMAL);

		expectFailure(m_Builder.withStartTime(-1));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getTimesSize()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetTimesSize() throws Exception
	{
		final int v = nextUInt();
		Assert.assertEquals(v,
				m_Builder.withNumTimes(v).build().getTimesSize());

		expectFailure(m_Builder.withNumTimes(-1));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getVariablesSize()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetVariablesSize() throws Exception
	{
		final int v = nextUInt();
		Assert.assertEquals(v,
				m_Builder.withNumVars(v).build().getVariablesSize());

		expectFailure(m_Builder.withNumVars(-1));
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.BMDHeader#getVersion()}.
	 *
	 * @throws Exception
	 */
	@Test
	public final void testGetVersion() throws Exception
	{
		final float v = r.nextFloat();
		Assert.assertEquals(v, m_Builder.withVersion(v).build().getVersion(),
				Float.MIN_NORMAL);
	}

}
