package com.small.library.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.*;

import com.small.library.biz.FieldValidationException;
import com.small.library.biz.ValidationException;

/***************************************************************************************
*
*	Handles Multipart Form data. Accepts a <I>javax.servlet.http.HttpServletRequest</I>
*	object that contains the Form data. The HTML form must contain the
*	<CODE>ENCTYPE</CODE> tag with a value of "multipart/form-data".
*
*	@author i-Deal\David Small
*	@version 1.0.0.0
*	@date 8/3/2000
*
***************************************************************************************/

public class FileUp
{
	/******************************************************************************
	*
	*	Constants
	*
	*****************************************************************************/

	/** Constant - Multipart/form-data content header. */
	private static final String CONTENT_HEADER = "Content-Disposition: form-data; name=\"";

	/** Constant - Multipart/form-data content type. */
	private static final String CONTENT_TYPE = "Content-Type: ";

	/** Constant - Multipart/form-data file information. */
	private static final String FILE_INFO = "; filename=\"";

	/** Constant - Multipart/form-data line separator. */
	private static final String LINE_SEPARATOR = "\r\n";

	/** Constant - Multipart/form-data data separator. */
	private static final String HEADER_DATA_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;

	/** Constant - Multipart/form-data content header length. */
	private static final int LEN_CONTENT_HEADER = CONTENT_HEADER.length();

	/** Constant - Multipart/form-data content type length. */
	private static final int LEN_CONTENT_TYPE = CONTENT_TYPE.length();

	/** Constant - Multipart/form-data file information length. */
	private static final int LEN_FILE_INFO = FILE_INFO.length();

	/** Constant - Multipart/form-data line separator length. */
	private static final int LEN_LINE_SEPARATOR = LINE_SEPARATOR.length();

	/** Constant - Multipart/form-data data separator length. */
	private static final int LEN_HEADER_DATA_SEPARATOR = HEADER_DATA_SEPARATOR.length();

	/** Logger. */
	private static final Log log = LogFactory.getLog(FileUp.class);

	/******************************************************************************
	*
	*	Constructors
	*
	*****************************************************************************/

	/** Constructor - accepts a <I>javax.servlet.http.HttpServletRequest</I> object. The constructor
	    reads the data, parses the data, and exposes the data.
		@pRequest The Http Request object.
	*/
	public FileUp(HttpServletRequest pRequest) throws IOException
	{
		properties = new HashMap<String, Object>();

		getData(pRequest);
	}

	/******************************************************************************
	*
	*	Helper methods
	*
	*****************************************************************************/

	/** Helper method - Pulls the data in from the HTML post. */
	private void getData(HttpServletRequest pRequest) throws IOException
	{
		int length = pRequest.getContentLength();

		// Do nothing - no data.
		if (0 == length)
			return;

		// Throw an exception - indicate that the length is not known.
		if (-1 == length)
			throw new IOException("The content length of the input stream is not known.");

		byte[] bytes = new byte[length];
		InputStream in = pRequest.getInputStream();

		// Get the data.
		int read = 0;

		while (read < length)
			read+= (in.read(bytes, read, length - read));

		// Create a string with the data.
		String bytes_ = new String(bytes, "ISO-8859-1");

		// Parse the data.
		parseData(bytes_, bytes);
	}

	/** Parses the contents of the bytes read from the post. */
	@SuppressWarnings("unchecked")
	private void parseData(String data_, byte data[])
	{
		int start = 0;
		int length = data_.length();
		Object value = null;

		// The very first line is the content terminator.
		int end = data_.indexOf(LINE_SEPARATOR, start);
		if (0 > end)
		{
			log.warn("Missing FileUp header.");
			return;
		}

		// Need to dynamically determine the info separator because Safari and Chrome use
		// a special WebKit separator different than the standard IE and FF.
		// DLS - 5/4/2010
		String infoSeparator = data_.substring(start, end);
		String contentTerminator = LINE_SEPARATOR + infoSeparator;
		int contentTerminatorLen = contentTerminator.length();
		log.info("Info Separator: '" + infoSeparator + "'");
		start = end + LEN_LINE_SEPARATOR;

		while (0 <= start)
		{
			end = data_.indexOf(CONTENT_HEADER, start);

			if (0 > end)
				break;

			start = end + LEN_CONTENT_HEADER;

			end = data_.indexOf("\"", start);

			if (0 > end)
				break;

			String key = data_.substring(start, end);

			end++;

			// Determine if it is a file?
			if ((length > end + LEN_FILE_INFO) &&
				data_.substring(end, end + LEN_FILE_INFO).equals(FILE_INFO))
			{
				// Set the next search point.
				start = end + LEN_FILE_INFO;

				if ('"' == data_.charAt(start))
				{
					value = new Content();	// Create a blank content.
					start++;
				}
				else
				{
					end = data_.indexOf("\"", start);
					Content content = new Content(data_.substring(start, end));

					// Has a valid content type been defined?
					end+= LEN_LINE_SEPARATOR + 1;
					if ((length > end + LEN_CONTENT_TYPE) &&
						data_.substring(end, end + LEN_CONTENT_TYPE).equals(CONTENT_TYPE))
					{
						start = end + LEN_CONTENT_TYPE;
						end = data_.indexOf(LINE_SEPARATOR, start);
						content.setType(data_.substring(start, end));

						start = end + LEN_HEADER_DATA_SEPARATOR;
						end = data_.indexOf(contentTerminator, start);

						// End of stream, but no terminator
						if (0 > end)
							content.setData(data, start, length - 1);

						else
							content.setData(data, start, end - 1);
					}

					value = content;
				}
			}

			else
			{
				start = end + LEN_HEADER_DATA_SEPARATOR;
				end = data_.indexOf(contentTerminator, start);

				// End of stream, but no terminator
				if (0 > end)
					break;

				// Nothing to add to the properties.
				if (start == end)
					continue;

				String v = data_.substring(start, end);
				if ((v = v.trim()).isEmpty())
					continue;	// Nothing to add to the properties.

				value = v;
			}

			// If the key is duplicated it means more than one form element
			// used the same name. Put all values in array list.
			// Do not remove any values - it will throw off the other keys.
			Object oldValue = getObject(key);

			if (null != oldValue)
			{
				if (oldValue instanceof ArrayList)
					((ArrayList<Object>) oldValue).add(value);
				else
				{
					List<Object> list = new ArrayList<Object>();
					list.add(oldValue);
					list.add(value);
					value = list;
				}
			}

			properties.put(key, value);

			// End of stream if the last search result is -1.
			if (0 > end)
				break;

			start = end + contentTerminatorLen;
		}
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	*****************************************************************************/

	/** Accessor method - Retrieve the number of items in the collection. */
	public int size() { return properties.size(); }

	/** Accessor method - gets an <I>Iterator</I> to the keys on the items
	    in the collection.
	*/
	public Iterator<String> getKeys() { return properties.keySet().iterator(); }

	/** Accessor method - get boolean data. */
	public boolean getBoolean(String strKey) { return 0 == getInt(strKey); }

	/** Accessor method - get  data. */
	public double getDate(String strKey) { return getDouble(strKey); }

	/** Accessor method - get  data. */
	public double getDouble(String strKey)
	{
		String value = getString(strKey);

		if (null == value)
			return 0D;

		return Double.parseDouble(value);
	}

	/** Accessor method - get  data. */
	public float getFloat(String strKey) { return (float) getDouble(strKey); }

	/** Accessor method - get  data. */
	public int getInt(String strKey)
	{
		String value = getString(strKey);

		if (null == value)
			return 0;

		return Integer.parseInt(value);
	}

	/** Accessor method - get Integer data. */
	public Integer getInteger(String key)
		throws ValidationException
	{
		String value = getString(key);

		if (null == value)
			return null;

		try { return new Integer(value); }
		catch (NumberFormatException ex)
		{
			throw new FieldValidationException(key, key, value, "it's an invalid number");
		} 
	}

	/** Accessor method - get  data. */
	public Object getObject(String strKey)
	{
		return properties.get(strKey);
	}

	/** Accessor method - get  data. */
	public short getShort(String strKey) { return (short) getInt(strKey); }

	/** Accessor method - get  data. */
	public String getString(String strKey)
	{
		Object value = getObject(strKey);

		if (null == value)
			return null;

		return value.toString();
	}

	/** Accessor method - get  data. */
	@SuppressWarnings("unchecked")
	public List<String> getStrings(String key)
	{
		Object values = getObject(key);

		if (null == values)
			return null;

		List<String> out = new ArrayList<String>();
		if (values instanceof List)
		{
			for (Object v : (List) values)
				out.add(v.toString());
			return out;
		}

		String value = values.toString();
		if (0 == value.length())
			return null;

		StringTokenizer tokens = new StringTokenizer(value, ",");
		while (tokens.hasMoreTokens())
			out.add(tokens.nextToken());

		return out;
	}

	/** Accessor method - get  data. */
	public Content getContent(String strKey)
	{
		Object value = getObject(strKey);

		if (null == value)
			return null;

		if (value instanceof Content)
			return (Content) value;

		return null;
	}

	/******************************************************************************
	*
	*	Member variables
	*
	*****************************************************************************/

	/** Member variable - reference to map of data. */
	private Map<String, Object> properties = null;

/***************************************************************************************
*
*	Class that encapsulates file information retrieved from the HTTP post.
*
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 8/3/2001
*
***************************************************************************************/

	public static class Content
	{
		/** Constructor - default - only accessible by the parent class. */
		private Content() {}

		/** Constructor - accepts a string value for the file name. */
		private Content(String strName) { setName(strName); }

		/******************************************************************************
		*
		*	Accessors
		*
		*****************************************************************************/

		/** Accessor - retrieves the name of the file. */
		public String toString() { return getName(); }

		/** Accessor - retrieves the Name property. */
		public String getName() { return m_strName; }

		/** Accessor - retrieves the (Content) Type property. */
		public String getType() { return m_strType; }

		/** Accessor - retrieves the file title of the fully qualified file name. */
		public String getTitle() { return m_strTitle; }

		/** Accessor - retrieves the file extension of the fully qualified file name. */
		public String getExtension() { return m_strExtension; }

		/** Accessor - retrieves the actual Data file itself. */
		public byte[] getData() { return m_Values; }

		/** Accessor - indicates whether the Content object is valid? */
		public boolean isValid() { return (null == getName()) ? false : true; }

		/** Accessor - indicates whether the Content is without data. */
		public boolean isEmpty() { return (null == getData()) ? true : false; }

		/******************************************************************************
		*
		*	Mutators - only accessible by the parent class
		*
		*****************************************************************************/

		/** Mutator - updates the Name property. */
		private void setName(String strNewValue)
		{ m_strName = strNewValue; setTitle(strNewValue); setExtension(strNewValue); }

		/** Mutator - updates the (Content) Type property. */
		private void setType(String strNewValue) { m_strType = strNewValue; }

		/** Mutator - updates the Data property based on an array of bytes,
		    the starting point, and the ending point. */
		private void setData(byte pNewValue[], int nStart, int nEnd)
		{
			if (nEnd < nStart)
			{
				m_Values = null;
				return;
			}

			m_Values = new byte[nEnd - nStart + 1];

			for (int i = nStart; i <= nEnd; i++)
				m_Values[i - nStart] = pNewValue[i];
		}

		/** Mutator - supplies a fully qualified file name. */
		private void setTitle(String strFileName)
		{
			int nLastSlash = strFileName.lastIndexOf('\\');
			int nPeriod = strFileName.lastIndexOf(".");

			if (-1 == nLastSlash)
				nLastSlash = strFileName.lastIndexOf('/');

			if (-1 == nPeriod)
				nPeriod = strFileName.length();

			m_strTitle = strFileName.substring(nLastSlash + 1, nPeriod);
		}

		/** Mutator - supplies a fully qualified file name. */
		private void setExtension(String strFileName)
		{
			int nPeriod = strFileName.lastIndexOf(".");

			if ((-1 == nPeriod) || (nPeriod == (strFileName.length() - 1)))
				m_strExtension = null;
			else
				m_strExtension = strFileName.substring(nPeriod + 1);
		}

		/******************************************************************************
		*
		*	Member variables
		*
		*****************************************************************************/

		private String m_strName = null;
		private String m_strType = null;
		private String m_strTitle = null;
		private String m_strExtension = null;
		private byte m_Values[] = null;
	}
}
