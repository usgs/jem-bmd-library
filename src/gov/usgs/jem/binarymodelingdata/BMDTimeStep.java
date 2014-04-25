package gov.usgs.jem.binarymodelingdata;

/**
 * Represents a time step from a BMD file. Comparison is based on time index in
 * the file.
 * 
 * <i>Note:</i> {@code hashCode()} and {@code equals(Object)} are NOT
 * overridden.
 * 
 * @author mckelvym
 * @since Apr 24, 2014
 * 
 */
public interface BMDTimeStep extends Comparable<BMDTimeStep>
{
	/**
	 * Get the index where this time step appears in the file.
	 * 
	 * @return the index where this time step appears in the file.
	 * @since Apr 24, 2014
	 */
	int getIndex();

	/**
	 * Get the number of milliseconds since January 1, 1970, 00:00:00 GMT, which
	 * can be used to create a Date instance.
	 * 
	 * @return the number of milliseconds since January 1, 1970, 00:00:00 GMT
	 * @since Apr 24, 2014
	 */
	long getTime();

	/**
	 * Get the raw time value retrieved from the file, assumed to be a floating
	 * point number of days since the seed date.
	 * 
	 * @return the raw time value retrieved from the file, assumed to be a
	 *         floating point number of days since the seed date.
	 * @since Apr 24, 2014
	 */
	double getValue();
}
