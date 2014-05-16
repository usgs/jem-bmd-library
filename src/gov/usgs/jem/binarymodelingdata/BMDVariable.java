package gov.usgs.jem.binarymodelingdata;

/**
 * Represents a variable from a BMD file. Comparison is based on variable index
 * in the file, not its name.
 * 
 * @author mckelvym
 * @since Apr 24, 2014
 * 
 */
public interface BMDVariable extends Comparable<BMDVariable>
{
	/**
	 * Get the index where this variable appears in the file.
	 * 
	 * @return the index where this variable appears in the file.
	 * @since Apr 24, 2014
	 */
	int getIndex();

	/**
	 * Get the variable name, as it was retrieved from the file. The file limits
	 * the name to 18 characters.
	 * 
	 * @return The variable name, as it was retrieved from the file.
	 * @since Apr 24, 2014
	 */
	String getName();

	/**
	 * Get the "program code" for the variable, which is an uppercase, slightly
	 * modified variant of the name.
	 * 
	 * @return the "program code" for the variable
	 * @since Apr 24, 2014
	 */
	String getPCode();

	/**
	 * Get the units for the variable, as it was retrieved from the file. The
	 * file limits the units to 12 characters.
	 * 
	 * @return the units for the variable, as it was retrieved from the file.
	 * @since Apr 24, 2014
	 */
	String getUnits();
}
