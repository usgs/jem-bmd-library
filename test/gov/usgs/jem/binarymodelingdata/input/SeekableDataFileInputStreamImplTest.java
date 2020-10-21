package gov.usgs.jem.binarymodelingdata.input;

import gov.usgs.jem.binarymodelingdata.AllTests;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Tests {@link SeekableDataFileInputStreamImpl}
 *
 * @author mckelvym
 * @since Aug 19, 2016
 *
 */
@SuppressWarnings("javadoc")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SeekableDataFileInputStreamImplTest
{

	/**
	 * Create a new {@link ByteBuffer} instance from the provided array
	 *
	 * @param p_Array
	 *            a byte array to use
	 * @param p_Size
	 *            the number of bytes from the beginning of the array to use
	 * @return the new {@link ByteBuffer} instance
	 * @since Aug 19, 2016
	 */
	private static ByteBuffer buf(final byte[] p_Array, final int p_Size)
	{
		return ByteBuffer.wrap(Arrays.copyOf(p_Array, p_Size))
				.order(ByteOrder.LITTLE_ENDIAN);
	}

	/**
	 *
	 * @throws java.lang.Exception
	 *             if unexpected condition causing test failure
	 * @since Aug 19, 2016
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		final Class<?> classToTest = SeekableDataFileInputStreamImpl.class;
		final Class<?> testingClass = SeekableDataFileInputStreamImplTest.class;
		AllTests.assertHasRequiredMethods(classToTest, testingClass);

	}

	private ByteOrder					m_ByteOrder;

	private byte[]						m_Bytes;

	private boolean						m_ExpectedBoolean;

	private byte						m_ExpectedByte;

	private byte[]						m_ExpectedBytes;

	private char						m_ExpectedChar;

	private char						m_ExpectedCharAscii;

	private double						m_ExpectedDouble;

	private float						m_ExpectedFloat;

	private int							m_ExpectedInt;

	private long						m_ExpectedLong;

	private short						m_ExpectedShort;

	private byte						m_ExpectedUByte;

	private int							m_ExpectedUInt32;

	private short						m_ExpectedUShort;

	private String						m_FilePath;

	private SeekableDataFileInputStream	m_Input;

	/**
	 * Creates one or more scenarios to compare the equality of two objects.
	 *
	 * @param p_TestEquals
	 *            should test that the two provided objects are equal, either
	 *            via the {@link Object#equals(Object)} method or by comparing
	 *            their {@link Object#hashCode()} values.
	 * @param p_TestNotEqual
	 *            should test that the two provided objects are <b>NOT</b>
	 *            equal, either via the {@link Object#equals(Object)} method or
	 *            by comparing their {@link Object#hashCode()} values.
	 * @throws Exception
	 * @since Aug 19, 2016
	 */
	private void equalityTests(
			final java.util.function.BiConsumer<Object, Object> p_TestEquals,
			final java.util.function.BiConsumer<Object, Object> p_TestNotEqual)
			throws Exception
	{
		p_TestEquals.accept(m_Input, m_Input);
		try (
			SeekableDataFileInputStreamImpl stream = new SeekableDataFileInputStreamImpl(
					m_FilePath, m_ByteOrder);)
		{
			p_TestEquals.accept(m_Input, stream);
		}
		try (
			SeekableDataFileInputStreamImpl stream = new SeekableDataFileInputStreamImpl(
					m_FilePath, ByteOrder.BIG_ENDIAN);)
		{
			p_TestNotEqual.accept(m_Input, stream);
		}
	}

	/**
	 * @throws java.lang.Exception
	 *             if unexpected condition causing test failure
	 * @since Aug 19, 2016
	 */
	@Before
	public void setUp() throws Exception
	{
		m_FilePath = AllTests.getTestFile().getAbsolutePath();
		m_ByteOrder = ByteOrder.LITTLE_ENDIAN;
		m_Input = new SeekableDataFileInputStreamImpl(m_FilePath, m_ByteOrder);

		m_Bytes = new byte[] { 0x42, 0x4d, 0x44, 0x0f, 0x09, 0x00, 0x00, 0x00 };
		m_ExpectedBoolean = true;
		m_ExpectedByte = m_Bytes[0];
		m_ExpectedChar = buf(m_Bytes, 2).getChar();
		m_ExpectedCharAscii = (char) m_Bytes[0];
		m_ExpectedDouble = buf(m_Bytes, Double.BYTES).getDouble();
		m_ExpectedFloat = buf(m_Bytes, Float.BYTES).getFloat();
		m_ExpectedBytes = Arrays.copyOf(m_Bytes, 2);
		m_ExpectedInt = buf(m_Bytes, Integer.BYTES).getInt();
		m_ExpectedLong = buf(m_Bytes, Long.BYTES).getLong();
		m_ExpectedShort = buf(m_Bytes, Short.BYTES).getShort();
		m_ExpectedUInt32 = m_ExpectedInt;
		m_ExpectedUByte = m_ExpectedByte;
		m_ExpectedUShort = m_ExpectedShort;
	}

	/**
	 * @throws java.lang.Exception
	 *             if unexpected condition causing test failure
	 * @since Aug 19, 2016
	 */
	@After
	public void tearDown() throws Exception
	{
		m_Input.close();
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#close()}.
	 */
	@Test
	public final void testClose()
	{
		try
		{
			m_Input.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public final void testEquals() throws Exception
	{
		final java.util.function.BiConsumer<Object, Object> testEquals = (same,
				alsosame) ->
		{
			org.junit.Assert.assertEquals(same, alsosame);
		};
		final java.util.function.BiConsumer<Object, Object> testNotEqual = (one,
				two) ->
		{
			org.junit.Assert.assertNotEquals(one, two);
		};
		equalityTests(testEquals, testNotEqual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#getByteOrder()}.
	 */
	@Test
	public final void testGetByteOrder()
	{
		Assert.assertEquals(m_ByteOrder, m_Input.getByteOrder());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#getFilePath()}.
	 */
	@Test
	public final void testGetFilePath()
	{
		Assert.assertEquals(m_FilePath, m_Input.getFilePath());
	}

	@Test
	public final void testHashCode() throws Exception
	{
		final java.util.function.BiConsumer<Object, Object> testEquals = (same,
				alsosame) ->
		{
			org.junit.Assert.assertEquals(same.hashCode(), alsosame.hashCode());
		};
		final java.util.function.BiConsumer<Object, Object> testNotEqual = (one,
				two) ->
		{
			org.junit.Assert.assertNotEquals(one.hashCode(), two.hashCode());
		};
		equalityTests(testEquals, testNotEqual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readBoolean()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadBoolean() throws IOException
	{
		final boolean actual = m_Input.readBoolean();
		Assert.assertEquals(m_ExpectedBoolean, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readByte()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadByte() throws IOException
	{
		final byte actual = m_Input.readByte();
		Assert.assertEquals(m_ExpectedByte, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readChar()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadChar() throws IOException
	{
		final char actual = m_Input.readChar();
		Assert.assertEquals(m_ExpectedChar, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readCharsAsAscii(int)}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadCharsAsAscii() throws IOException
	{
		final char[] actual = m_Input.readCharsAsAscii(1);
		Assert.assertEquals(m_ExpectedCharAscii, actual[0]);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readDouble()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadDouble() throws IOException
	{
		final double actual = m_Input.readDouble();
		Assert.assertEquals(m_ExpectedDouble, actual, Double.MIN_NORMAL);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readFloat()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadFloat() throws IOException
	{
		final float actual = m_Input.readFloat();
		Assert.assertEquals(m_ExpectedFloat, actual, Float.MIN_NORMAL);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readFully(byte[])}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadFullyByteArray() throws IOException
	{
		final byte[] actual = new byte[2];
		m_Input.readFully(actual);
		Assert.assertArrayEquals(m_ExpectedBytes, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readFully(byte[], int, int)}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadFullyByteArrayIntInt() throws IOException
	{
		final byte[] actual = new byte[2];
		m_Input.readFully(actual, 0, 2);
		Assert.assertArrayEquals(m_ExpectedBytes, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readInt()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadInt() throws IOException
	{
		final int actual = m_Input.readInt();
		Assert.assertEquals(m_ExpectedInt, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readLine()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadLine() throws IOException
	{
		try
		{
			m_Input.readLine();
			Assert.fail(
					"readLine should not be supported for this reader type.");
		}
		catch (@SuppressWarnings("unused") final UnsupportedOperationException e)
		{
			/**
			 * Expected
			 */
		}
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readLong()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadLong() throws IOException
	{
		final long actual = m_Input.readLong();
		Assert.assertEquals(m_ExpectedLong, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readShort()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadShort() throws IOException
	{
		final short actual = m_Input.readShort();
		Assert.assertEquals(m_ExpectedShort, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readUInt32()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadUInt32() throws IOException
	{
		final int actual = m_Input.readUInt32();
		Assert.assertEquals(m_ExpectedUInt32, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readUnsignedByte()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadUnsignedByte() throws IOException
	{
		final int actual = m_Input.readUnsignedByte();
		Assert.assertEquals(m_ExpectedUByte, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readUnsignedShort()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadUnsignedShort() throws IOException
	{
		final int actual = m_Input.readUnsignedShort();
		Assert.assertEquals(m_ExpectedUShort, actual);
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#readUTF()}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testReadUTF() throws IOException
	{
		/**
		 * Not used.
		 */
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#seek(int)}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testSeek() throws IOException
	{
		final int position = 4;
		final int seek = m_Input.seek(position);
		Assert.assertEquals(position, seek);
		Assert.assertEquals(m_Bytes[position], m_Input.readByte());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#skipBytes(int)}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testSkipBytes() throws IOException
	{
		final int position = 2;
		int seek = m_Input.skipBytes(position);
		Assert.assertEquals(position, seek);
		Assert.assertEquals(m_Bytes[position], m_Input.readByte());

		seek = m_Input.skipBytes(position);
		Assert.assertEquals(position, seek);
		Assert.assertEquals(m_Bytes[position + position + 1],
				m_Input.readByte());
	}

	/**
	 * Test method for
	 * {@link gov.usgs.jem.binarymodelingdata.input.SeekableDataFileInputStreamImpl#skipBytesAggressive(int)}.
	 *
	 * @throws IOException
	 */
	@Test
	public final void testSkipBytesAggressive() throws IOException
	{
		final int position = 2;
		int seek = m_Input.skipBytesAggressive(position);
		Assert.assertEquals(position, seek);
		Assert.assertEquals(m_Bytes[position], m_Input.readByte());

		seek = m_Input.skipBytesAggressive(position);
		Assert.assertEquals(position, seek);
		Assert.assertEquals(m_Bytes[position + position + 1],
				m_Input.readByte());
	}

}
