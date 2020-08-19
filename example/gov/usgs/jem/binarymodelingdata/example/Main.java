package gov.usgs.jem.binarymodelingdata.example;

import com.google.common.collect.Iterables;
import gov.usgs.jem.binarymodelingdata.AllTests;
import gov.usgs.jem.binarymodelingdata.BMDSegment;
import gov.usgs.jem.binarymodelingdata.BMDTimeStep;
import gov.usgs.jem.binarymodelingdata.BMDVariable;
import gov.usgs.jem.binarymodelingdata.Concentration;
import gov.usgs.jem.binarymodelingdata.input.BMDReader;
import gov.usgs.jem.binarymodelingdata.input.ConcentrationsQuery;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.BasicConfigurator;

/**
 * Example using {@link BMDReader}
 *
 * @author mckelvym
 * @since Aug 19, 2016
 *
 */
public final class Main
{
	/**
	 * Class logger
	 */
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(Main.class);

	/**
	 * @param args
	 * @throws IOException
	 * @author mckelvym
	 */
	public static void main(final String[] args) throws IOException
	{
		BasicConfigurator.configure();

		try (BMDReader reader = BMDReader
				.openDebug(AllTests.getTestFile().getAbsolutePath());)
		{
			final List<BMDVariable> variables = reader.getVariables();
			log.info(String.format("Variables (%s): %s", variables.size(),
					variables.stream().map(BMDVariable::getName)
							.collect(Collectors.joining(", "))));
			final List<BMDSegment> segments = reader.getSegments();
			log.info(String.format("Segments (%s): %s", segments.size(),
					segments.stream().map(BMDSegment::getName)
							.collect(Collectors.joining(", "))));
			final List<BMDTimeStep> timeSteps = reader.getTimeSteps();
			log.info(String.format("Timesteps (%s): %s to %s", timeSteps.size(),
					new java.util.Date(timeSteps.get(0).getTime()),
					new java.util.Date(
							Iterables.getLast(timeSteps).getTime())));

			final ConcentrationsQuery query = reader.newConcentrationsQuery()
					.withAllTimeSteps()
					.withSegments(Arrays.asList(segments.get(0)))
					.withVariables(variables.stream()
							.filter(x -> x.getName().equals("Hydraulic Depth"))
							.collect(Collectors.toList()));

			for (final Concentration conc : query.execute())
			{
				log.info(String.format("%s, %s, %s: %s",
						conc.getVariable().getName(),
						conc.getSegment().getName(),
						new java.util.Date(conc.getTimeStep().getTime()),
						conc.getValue()));
			}
		}
		catch (final Throwable t)
		{
			t.printStackTrace();
		}
	}
}
