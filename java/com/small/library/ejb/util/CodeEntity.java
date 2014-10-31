package com.small.library.ejb.util;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**********************************************************************************
*
*	Interface for an EJB remote interface that represents a code table entity.
*	Useful for converting to a <I>Collection</I> of <I>ListItem</I> object for
*	building GUI select lists.
*
*	@author David Small
*	@version 1.0.0.0
*	@date 1/1/2003
*	@see com.small.library.taglib.ListItem
*	@see com.small.library.taglib.ListItemFactory#fromCodeEntity
*
***********************************************************************************/

public interface CodeEntity extends EJBObject
{
	/** Accessor method - gets the entity's code ID. */
	public String getCodeId() throws RemoteException;

	/** Accessor method - gets the entity's code value. */
	public String getCodeValue() throws RemoteException;
}

