package com.small.library.metadata;

import java.sql.*;
import java.util.*;

import com.small.library.data.DataRecord;

/***************************************************************************************
*
*	Generic class for defining a collection of index key columns.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

public class Keys
{
	public static final short SORT_UNKNOWN = 0;
	public static final short SORT_ASC = 1;
	public static final short SORT_DESC = 2;

	public void add(String strName) { add(strName, SORT_UNKNOWN); }
	public void add(String strName, short nSort) { add(new Record(strName, nSort)); }
	public void add(Record pKey) { m_Data.add(pKey); }
	public void add(String strName, String strSort)
	{
		if (null == strSort)
			add(strName);
		else if (strSort.equals("A"))
			add(strName, SORT_ASC);
		else if (strSort.equals("D"))
			add(strName, SORT_DESC);
		else
			add(strName);
	}	

	public Record item(int nIndex) { return (Record) m_Data.get(nIndex); }
	public int size() { return m_Data.size(); }

	/** Action method - lists the keys in a comma separated <I>String</I>. */
	public String listItems()
	{
		int nSize = size();
		StringBuffer strReturn = new StringBuffer();

		for (int i = 0; i < nSize; i++)
		{
			if (i > 0)
				strReturn.append(", ");

			strReturn.append(item(i).getName());
		}

		return strReturn.toString();
	}

	private ArrayList m_Data = new ArrayList();

/***************************************************************************************
*
*	Generic class for defining a record object of index key columns.
*	@author Xpedior\David Small
*	@version 1.0.0.0
*	@date 2/10/2000
*
***************************************************************************************/

	public static class Record
	{
		public Record(String strName) { this(strName, SORT_UNKNOWN); }
		public Record(String strName, short nSort)
		{
			setName(strName);
			setSort(nSort);
		}

		public String toString() { return m_strName; }
		public int hashCode() { return m_strName.hashCode(); }
		public String getName() { return m_strName; }
		public short getSort() { return m_nSort; }

		private void setName(String strValue) { m_strName = strValue; }
		private void setSort(short nValue) { m_nSort = nValue; }

		public boolean equals(Object pValue)
		{
			if ((null == pValue) || (!(pValue instanceof Record)))
				return false;

			return m_strName.equals(((Record) pValue).m_strName);
		}

		private String m_strName = null;
		private short m_nSort = SORT_UNKNOWN;
	}
}
