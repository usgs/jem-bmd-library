package gov.usgs.jem.binarymodelingdata.input;

import gov.usgs.jem.binarymodelingdata.Concentrations;

import java.io.IOException;

/**
 * Query class for getting concentrations data. See
 * {@link BMDReader#newConcentrationsQuery()}
 * 
 * @author mckelvym
 * @since Apr 25, 2014
 * 
 */
public interface ConcentrationsQuery
{

	/**
	 * Executes the query and returns the results as a {@link Concentrations}
	 * collection
	 * 
	 * @return the results of the query.
	 * @throws IOException
	 * @since Apr 22, 2014
	 */
	Concentrations execute() throws IOException;

	/**
	 * Checks that the query is ready for {@link #execute()}
	 * 
	 * @throws IllegalStateException
	 * @since Apr 22, 2014
	 */
	void validate() throws IllegalStateException;

	/**
	 * Use all segments in the query.
	 * 
	 * @return this
	 * @since Apr 22, 2014
	 */
	ConcentrationsQuery withAllSegments();

	/**
	 * Use all time steps in the query.
	 * 
	 * @return this
	 * @since Apr 22, 2014
	 */
	ConcentrationsQuery withAllTimeSteps();

	/**
	 * Use all variables in the query.
	 * 
	 * @return this
	 * @since Apr 22, 2014
	 */
	ConcentrationsQuery withAllVariables();

	/**
	 * Add the pcodes (which are an alias for variable names) to the query.
	 * 
	 * @param p_PCodes
	 *            the pcodes to add to the query
	 * @return this
	 * @since Apr 22, 2014
	 */
	ConcentrationsQuery withPCodes(String... p_PCodes);

	/**
	 * Add the segment names to the query.
	 * 
	 * @param p_SegmentNames
	 *            the name of segments to add to the query
	 * @return this
	 * @since Apr 22, 2014
	 */
	ConcentrationsQuery withSegments(String... p_SegmentNames);

	/**
	 * Add the time steps to the query.
	 * 
	 * @param p_TimeSteps
	 *            the time steps to add to the query
	 * @return this
	 * @since Apr 22, 2014
	 */
	ConcentrationsQuery withTimeSteps(Integer... p_TimeSteps);

	/**
	 * Add the variable names to the query.
	 * 
	 * @param p_VariableNames
	 *            the name of variables to add to the query
	 * @return this
	 * @since Apr 22, 2014
	 */
	ConcentrationsQuery withVariables(String... p_VariableNames);

}