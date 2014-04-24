package gov.usgs.jem.binarymodelingdata;

/**
 * Represents a segment from a BMD file. Comparison is based on segment index in
 * the file, not its name.
 * 
 * @author mckelvym
 * @since Apr 24, 2014
 * 
 */
public interface BMDSegment extends Comparable<BMDSegment>
{
	/**
	 * Get the index where this segment appears in the file.
	 * 
	 * @return the index where this segment appears in the file.
	 * @since Apr 24, 2014
	 */
	int getIndex();

	/**
	 * Get the segment name, as it was retrieved from the file. . The file
	 * limits the name to 15 characters.
	 * 
	 * @return The segment name, as it was retrieved from the file.
	 * @since Apr 24, 2014
	 */
	String getName();
}
