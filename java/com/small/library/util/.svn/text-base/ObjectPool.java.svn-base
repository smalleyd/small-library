package com.small.library.util;

/*****************************************************************************************
*
*	Generic <I>Object</I> pool. The constructors either accept a <I>String</I> name
*	for a class or an actual <I>Class</I> object.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 8/26/2000
*
*****************************************************************************************/

public class ObjectPool extends AbstractPool
{
	/********************************************************************************
	*
	*	Constructors/Destructor
	*
	********************************************************************************/

	/** Constructor - accepts a <I>String</I> object.
		@param strClassName A String representation of the class.
	*/
	public ObjectPool(String strClassName)
		throws ClassNotFoundException, PoolException
	{ super(); setFactory(strClassName); init(); }

	/** Constructor - accepts a <I>Class</I> factory object.
		@param pFactory - The <I>Class</I> factory.
	*/
	public ObjectPool(Class pFactory)
		throws PoolException
	{ super(); setFactory(pFactory); init(); }

	/** Constructor - accepts a <I>String</I> object.
		@param nInitialCapacity Initial capacity of pool.
		@param strClassName A String representation of the class.
	*/
	public ObjectPool(int nInitialCapacity, String strClassName)
		throws ClassNotFoundException, PoolException
	{ super(nInitialCapacity); setFactory(strClassName); init(); }

	/** Constructor - accepts a <I>Class</I> factory object.
		@param nInitialCapacity Initial capacity of pool.
		@param pFactory - The <I>Class</I> factory.
	*/
	public ObjectPool(int nInitialCapacity, Class pFactory)
		throws PoolException
	{ super(nInitialCapacity); setFactory(pFactory); init(); }

	/********************************************************************************
	*
	*	Implementation of abstract methods
	*
	********************************************************************************/

	/** Creates an instance from the class factory. */
	public Object create() throws PoolException
	{
		try { return m_Factory.newInstance(); }
		catch (Exception pEx) { throw new PoolException(pEx); }
	}

	/********************************************************************************
	*
	*	Accessor methods
	*
	********************************************************************************/

	/** Accessor - gets a reference to the class factory. */
	public Class getFactory() { return m_Factory; }

	/********************************************************************************
	*
	*	Mutator methods
	*
	********************************************************************************/

	/** Mutator - sets the class factory with a <I>String</I> representation of
	    of the class.
		@param strClassName String representation of the class name.
	*/
	public void setFactory(String strClassName) throws ClassNotFoundException
	{ setFactory(Class.forName(strClassName)); }

	/** Mutator - sets the class factory.
		@param pNewValue The <I>Class</I> factory.
	*/
	public void setFactory(Class pNewValue) { m_Factory = pNewValue; }

	/********************************************************************************
	*
	*	Member variables
	*
	********************************************************************************/

	/** Member variable - specified class factory. */
	private Class m_Factory = null;
}
