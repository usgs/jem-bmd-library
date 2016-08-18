package gov.usgs.jem.binarymodelingdata;

/**
 * A single concentration entry read from the file. This is associated with a
 * specific variable, segment, and time index.
 *
 * @author mckelvym
 * @since Apr 23, 2014
 *
 */
public interface Concentration extends Comparable<Concentration>
{
	/**
	 * Get the segment
	 *
	 * @return the {@link BMDSegment}
	 * @since Apr 23, 2014
	 */
	BMDSegment getSegment();

	/**
	 * Get the time step
	 *
	 * @return the {@link BMDTimeStep}
	 * @since Apr 23, 2014
	 */
	BMDTimeStep getTimeStep();

	/**
	 * Get the concentration value
	 *
	 * @return the concentration value
	 * @since Apr 23, 2014
	 */
	float getValue();

	/**
	 * Get the variable
	 *
	 * @return the {@link BMDVariable}
	 * @since Apr 23, 2014
	 */
	BMDVariable getVariable();

}