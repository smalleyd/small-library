package com.small.library.taglib;

import java.rmi.RemoteException;
import java.util.*;

import com.small.library.ejb.util.*;

/********************************************************************************
*
*	Factory class that exposes methods that convert various collections
*	to a <I>Collection</I> of <I>ListItem</I> objects.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 1/1/2003
*
*********************************************************************************/

public class ListItemFactory
{
	/** Factory method - converts a <I>Collection</I> of <I>CodeEntity</I>
	    objects to a <I>Collection</I> of <I>ListItem</I> objects.
	    	@param codeEntities Collection of <I>CodeEntity</I> objects.
		@see com.small.library.ejb.util.CodeEntity
	*/
	public static Collection<ListItem> fromCodeEntities(Collection<? extends CodeEntity> records)
		throws RemoteException
	{
		Collection<ListItem> listItems = new ArrayList<ListItem>(records.size());

		for (CodeEntity record : records)
			listItems.add(new ListItemImpl(record.getCodeId(),
				record.getCodeValue()));

		return listItems;
	}

	/** Factory method - converts a <I>Collection</I> of <I>CodeLocal</I>
	    objects to a <I>Collection</I> of <I>ListItem</I> objects.
		@param records Collection of <I>CodeLocal</I> objects.
		@see com.small.library.ejb.util.CodeLocal
	*/
	public static Collection<ListItem> fromLocalCodeEntities(Collection<? extends CodeLocal> records)
	{
		Collection<ListItem> listItems = new ArrayList<ListItem>(records.size());

		for (CodeLocal record : records)
			listItems.add(new ListItemImpl(record.getCodeId(),
				record.getCodeValue()));

		return listItems;
	}

	/** Factory method - converts a <I>List</I> of <I>Code</I>
	    objects to a <I>Collection</I> of <I>ListItem</I> objects.
		@param records List of <I>Code</I> objects.
		@see com.small.library.ejb.util.Code
	*/
	public static Collection<ListItem> fromCodes(List<? extends Code> records)
	{
		// Mainly for the team access handler output.
		if (null == records)
			return null;

		Collection<ListItem> values = new ArrayList<ListItem>(records.size());

		for (Code record : records)
			values.add(new ListItemImpl(record.getCodeId(), record.getCodeValue()));

		return values;
	}

	/** Factory method - converts a <I>List</I> of
	    objects to a <I>Collection</I> of <I>ListItem</I> objects.
		@param records List of objects.
		@see com.small.library.ejb.util.Code
	*/
	public static Collection<ListItem> fromObjects(List<? extends Object> records)
	{
		// Mainly for the team access handler output. JUST a precaution.
		if (null == records)
			return null;

		Collection<ListItem> values = new ArrayList<ListItem>(records.size());

		for (Object record : records)
			values.add(new ListItemImpl(record.toString()));

		return values;
	}
}
