package gov.usgs.jem.binarymodelingdata.input;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import gov.usgs.jem.binarymodelingdata.BMDSegment;
import gov.usgs.jem.binarymodelingdata.BMDTimeStep;
import gov.usgs.jem.binarymodelingdata.BMDVariable;
import gov.usgs.jem.binarymodelingdata.Concentrations;

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
	 *             if the query could not be successfully executed
	 * @since Apr 22, 2014
	 */
	Concentrations execute() throws IOException;

	/**
	 * Checks that the query is ready for {@link #execute()}
	 *
	 * @throws IllegalStateException
	 *             if the query is not properly formed
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
	 * Include progress reporting with the query
	 *
	 * @param p_Monitor
	 *            the progress monitor
	 * @return this
	 * @since Feb 9, 2015
	 */
	ConcentrationsQuery withProgress(IProgressMonitor p_Monitor);

	/**
	 * Add the segments to the query.
	 *
	 * @param p_Segments
	 *            the {@link BMDSegment}(s) to add to the query
	 * @return this
	 * @since May 16, 2014
	 */
	ConcentrationsQuery withSegments(List<BMDSegment> p_Segments);

	/**
	 * Add the time steps to the query.
	 *
	 * @param p_TimeSteps
	 *            the {@link BMDTimeStep}(s) to add to the query
	 * @return this
	 * @since May 16, 2014
	 */
	ConcentrationsQuery withTimeSteps(List<BMDTimeStep> p_TimeSteps);

	/**
	 * /** Add the variables to the query.
	 *
	 * @param p_Variables
	 *            the {@link BMDVariable}(s) to add to the query
	 * @return this
	 * @since May 16, 2014
	 */
	ConcentrationsQuery withVariables(List<BMDVariable> p_Variables);

}