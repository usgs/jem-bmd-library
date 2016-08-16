package gov.usgs.jem.binarymodelingdata;

import com.google.common.collect.ImmutableList;

/**
 * Container for a collection of {@link Concentration}.
 *
 * @author mckelvym
 * @since Apr 23, 2014
 *
 */
public interface Concentrations extends Iterable<Concentration>
{
	/**
	 * Retrieve a particular entry.
	 *
	 * @param p_Variable
	 *            the variable
	 * @param p_Segment
	 *            the segment
	 * @param p_TimeStep
	 *            the time step
	 * @return the {@link Concentration}
	 * @throw {@link IllegalArgumentException} if there is not an entry for the
	 *        given variable, segment, or time step.
	 * @since Apr 23, 2014
	 */
	Concentration get(BMDVariable p_Variable, BMDSegment p_Segment,
			BMDTimeStep p_TimeStep);

	/**
	 * Get the segments in this container
	 *
	 * @return the segments in this container
	 * @since Apr 23, 2014
	 */
	ImmutableList<BMDSegment> getSegments();

	/**
	 * Get the time steps in this container
	 *
	 * @return the time steps in this container
	 * @since Apr 23, 2014
	 */
	ImmutableList<BMDTimeStep> getTimeSteps();

	/**
	 * Get the variables in this container
	 *
	 * @return the variables in this container
	 * @since Apr 23, 2014
	 */
	ImmutableList<BMDVariable> getVariables();
}
