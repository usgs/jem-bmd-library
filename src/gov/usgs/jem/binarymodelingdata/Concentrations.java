package gov.usgs.jem.binarymodelingdata;

import java.util.List;

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
	 * @param p_VariableName
	 *            the variable name
	 * @param p_SegmentName
	 *            the segment name
	 * @param p_TimeStep
	 *            the time step
	 * @return the {@link Concentration}
	 * @throw {@link IllegalArgumentException} if there is not an entry for the
	 *        given variable, segment, or time step.
	 * @since Apr 23, 2014
	 */
	Concentration get(String p_VariableName, String p_SegmentName,
			int p_TimeStep);

	/**
	 * Get the segment names in this container
	 * 
	 * @return the segment names in this container
	 * @since Apr 23, 2014
	 */
	List<String> getSegments();

	/**
	 * Get the time steps in this container
	 * 
	 * @return the time steps in this container
	 * @since Apr 23, 2014
	 */
	List<Integer> getTimeSteps();

	/**
	 * Get the variable names in this container
	 * 
	 * @return the variable names in this container
	 * @since Apr 23, 2014
	 */
	List<String> getVariables();
}
