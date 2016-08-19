package gov.usgs.jem.binarymodelingdata;

import java.io.File;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import gov.usgs.jem.binarymodelingdata.input.BMDReaderTest;
import gov.usgs.jem.binarymodelingdata.input.BMDSegmentImplTest;
import gov.usgs.jem.binarymodelingdata.input.BMDTimeStepImplTest;
import gov.usgs.jem.binarymodelingdata.input.BMDVariableImplTest;
import gov.usgs.jem.binarymodelingdata.input.ConcentrationImplTest;
import gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImplTest;

/**
 * Tests all cases
 *
 * @author mckelvym
 * @since Aug 18, 2016
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ BMDHeaderTest.class, BMDReaderTest.class,
		BMDSegmentImplTest.class, BMDTimeStepImplTest.class,
		BMDVariableImplTest.class, ConcentrationImplTest.class,
		SeekableDataFileInputStreamImplTest.class, })
public class AllTests
{
	/**
	 * Ensure that a testing class has defined all required testing methods.
	 *
	 * @param p_ClassToTest
	 *            the class that is being tested
	 * @param p_TestingClass
	 *            the class that is doing the testing
	 * @since Jul 28, 2015
	 * @since Mar 16, 2016
	 */
	public static void assertHasRequiredMethods(final Class<?> p_ClassToTest,
			final Class<?> p_TestingClass)
	{
		try
		{
			setUpBeforeClass();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			Assert.fail("Unable to set up tests: " + e.getMessage());
		}
		try
		{
			Tests.assertHasRequiredMethods(p_ClassToTest, p_TestingClass);
		}
		catch (final NoSuchMethodException e)
		{
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * @return the test file to use
	 * @since Aug 19, 2016
	 */
	public static final File getTestFile()
	{
		return new File("test/data/Sampleq.BMD");
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		BasicConfigurator.configure();
	}
}
