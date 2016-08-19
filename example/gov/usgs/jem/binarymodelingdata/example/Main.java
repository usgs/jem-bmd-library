package gov.usgs.jem.binarymodelingdata.example;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;

import gov.usgs.jem.binarymodelingdata.AllTests;
import gov.usgs.jem.binarymodelingdata.input.BMDReader;

/**
 * Example using {@link BMDReader}
 *
 * @author mckelvym
 * @since Aug 19, 2016
 *
 */
public final class Main
{
	public static void main(final String[] args) throws IOException
	{
		BasicConfigurator.configure();

		try (BMDReader reader = BMDReader
				.openDebug(AllTests.getTestFile().getAbsolutePath());)
		{
		}
		catch (final Throwable t)
		{
			t.printStackTrace();
		}
	}
}
