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
	 * Get the segment name
	 * 
	 * @return the segment name
	 * @since Apr 23, 2014
	 */
	String getSegmentName();

	/**
	 * Get the time value
	 * 
	 * @return the time value
	 * @since Apr 23, 2014
	 */
	double getTime();

	/**
	 * Get the time index
	 * 
	 * @return the time index
	 * @since Apr 23, 2014
	 */
	int getTimeStep();

	/**
	 * Get the concentration value
	 * 
	 * @return the concentration value
	 * @since Apr 23, 2014
	 */
	float getValue();

	/**
	 * Get the variable name
	 * 
	 * @return the variable name
	 * @since Apr 23, 2014
	 */
	String getVariableName();

}