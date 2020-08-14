package gov.usgs.jem.binarymodelingdata.input;

import com.google.common.io.LittleEndianDataInputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

/**
 * Wraps a {@link DataInputStream} (big-endian) or a
 * {@link LittleEndianDataInputStream} (little-endian) as a {@link DataInput}
 * that allows {@link #seek(int)} to any part of the file (similar to
 * {@link RandomAccessFile}).
 *
 * @see #open(String, ByteOrder)
 * @see #seek(int)
 * @see #close()
 *
 * @author mckelvym
 * @since Apr 21, 2014
 *
 */
public interface SeekableDataFileInputStream extends DataInput, Closeable
{
	/**
	 * Close the input
	 *
	 * @throws IOException
	 *             if the stream could not be closed
	 *
	 * @since Apr 21, 2014
	 */
	@Override
	void close() throws IOException;

	/**
	 * Get the endianness
	 *
	 * @return the {@link ByteOrder}
	 * @since Apr 21, 2014
	 */
	ByteOrder getByteOrder();

	/**
	 * Get the file path being read from
	 *
	 * @return the file path being read from.
	 * @since Apr 21, 2014
	 */
	String getFilePath();

	/**
	 * Create a new {@link SeekableDataFileInputStream} using the provided file
	 * path and endianness.
	 *
	 * @param p_FilePath
	 *            the file path to read from
	 * @param p_ByteOrder
	 *            the {@link ByteOrder} endianness to use
	 * @throws IOException
	 *             if the file could not be opened for some reason
	 * @return a new {@link SeekableDataFileInputStream} instance
	 * @since Apr 21, 2014
	 */
	default SeekableDataFileInputStream open(final String p_FilePath,
			final ByteOrder p_ByteOrder) throws IOException
	{
		return new SeekableDataFileInputStreamImpl(p_FilePath, p_ByteOrder);
	}

	/**
	 * Reads <b>one</b> input byte per char and returns p_Count char values.
	 * This method is suitable for ascii-encoded single octet values from a
	 * file.
	 *
	 * @param p_Count
	 *            the number of ascii characters to read
	 * @return the char values read
	 * @throws IOException
	 *             an error occurred while attempting to read from the file
	 * @since Apr 21, 2014
	 */
	char[] readCharsAsAscii(final int p_Count) throws IOException;

	/**
	 * Reads an unsigned integer (4-bytes) into a java signed integer (4-bytes).
	 *
	 * @return the unsigned integer recast as a signed integer (beware overflow)
	 * @throws IOException
	 *             an error occurred while attempting to read from the file
	 * @since Apr 21, 2014
	 */
	int readUInt32() throws IOException;

	/**
	 * Sets the file-pointer offset, measured from the beginning of this file,
	 * at which the next read or write occurs. Closes the streams and reopens
	 * them first.
	 *
	 * @param p_Position
	 *            the offset position, measured in bytes from the beginning of
	 *            the file, at which to set the file pointer.
	 * @return the actual number of bytes skipped.
	 * @throws IOException
	 *             if the contained input stream does not support seek or
	 *             another I/O error occurs.
	 * @since Apr 21, 2014
	 */
	int seek(int p_Position) throws IOException;

	/**
	 * Makes an attempt to skip over <code>p_Count</code> bytes of data from the
	 * input stream, discarding the skipped bytes. This method behaves very
	 * similar to {@link #skipBytes(int)}, but may make several attempts to read
	 * from the stream until the specified number of bytes have been skipped
	 * (with 0 bytes being the escape condition). This method never throws an
	 * <code>EOFException</code>. The actual number of bytes skipped is
	 * returned.
	 *
	 * @param p_Count
	 *            the number of bytes to be skipped.
	 * @return the number of bytes actually skipped.
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	int skipBytesAggressive(int p_Count) throws IOException;
}