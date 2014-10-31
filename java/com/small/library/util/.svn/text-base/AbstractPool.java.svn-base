package com.small.library.util;

import java.util.ArrayList;

/*****************************************************************************************
*
*	Base class for varying Object pools. Derived classes only need to implement
*	the <I>create</I> method so that the pool can be initialized.
*
*	@author Xpedior\David Small
*	@version 1.1.0.0
*	@date 8/26/2000
*
*****************************************************************************************/

public abstract class AbstractPool
{
	/*********************************************************************************
	*
	*	Constants
	*
	*********************************************************************************/

	/** Constant - initial default capacity of the pool. */
	public static final int INITIAL_CAPACITY = 10;

	/*********************************************************************************
	*
	*	Constructors/Destructor
	*
	*********************************************************************************/

	/** Constructor - default. */
	public AbstractPool() throws PoolException
	{ this(INITIAL_CAPACITY); }

	/** Constructor - specifies initial capacity.
		@param nInitialCapacity integer value.
	*/
	public AbstractPool(int nInitialCapacity)
	{
		if (0 >= nInitialCapacity)
			nInitialCapacity = INITIAL_CAPACITY;

		m_nInitialCapacity = nInitialCapacity;
	}

	/*********************************************************************************
	*
	*	Main functionality
	*
	*********************************************************************************/

	/** Creates the objects initially cached in the pool. Do not assume that the pool
	    can be initialized during construction because the derived class may not be
	    prepared to "create" individual objects.
	*/
	protected void init() throws PoolException
	{
		// Set the capacity.
		m_nCapacity = m_nInitialCapacity;
		m_Objects = new ArrayList(m_nInitialCapacity);

		synchronized (m_Objects)
		{
			// Populate pool.
			for (int i = 0; i < m_nCapacity; i++)
				m_Objects.add(create());
		}

		m_IdleResourceManager = new IdleResourceManager(this);
		m_IdleResourceManager.setPriority(Thread.MIN_PRIORITY);
		m_IdleResourceManager.setDaemon(true);
		m_IdleResourceManager.start();

		m_bInitialized = true;
	}

	/** Retrieves the next object in the pool. If the pool is empty, the pool size
	    is doubled.
	*/
	public Object get() throws PoolException
	{
		synchronized (m_Objects)
		{
			if (0 == m_Objects.size())
				doubleCapacity();

			return m_Objects.remove(m_Objects.size() - 1);
		}
	}

	/** Releases an object. Placing the object back on the pool. */
	public void release(Object pValue) throws PoolException
	{ synchronized (m_Objects) { m_Objects.add(pValue); } }

	/** Helper method - doubles the capacity of the pool. Usually, called when
	    the pool is empty.
	*/
	private void doubleCapacity() throws PoolException
	{
		int i = m_nCapacity;
		int nCapacity = i * 2;

		synchronized (m_Objects)
		{
			m_Objects.ensureCapacity(nCapacity);

			for (; i < nCapacity; i++)
			{
				m_Objects.add(create());

				// Make sure the object is created before increasing capacity.
				m_nCapacity++;
			}
		}
	}

	/** Helper method - halves the capacity of the pool. Usually, called by IdleResourceManager
	    to remove excess resources.
	*/
	private void halfCapacity() throws PoolException
	{
		synchronized (m_Objects)
		{
			int nSize = m_Objects.size() - 1;
			int nNewSize = (m_nCapacity / 2) - 1;

			// Remove from the back of the object - performance.
			for (int i = nSize; i > nNewSize; i--)
				m_Objects.remove(i);

			m_nCapacity/= 2;
		}
	}

	/*********************************************************************************
	*
	*	Abstract methods
	*
	*********************************************************************************/

	/** Abstract method - placeholder for the object factory. */
	protected abstract Object create() throws PoolException;

	/*********************************************************************************
	*
	*	Accessor methods
	*
	*********************************************************************************/

	/** Accessor - indicates whether the pool has been initialized. */
	public boolean isInitialized() { return m_bInitialized; }

	/** Accessor - gets the current capacity of the pool. */
	public int getCapacity() { return m_nCapacity; }

	/** Accessor - gets the current number of objects available. */
	public int getSize() { return m_Objects.size(); }

	/*********************************************************************************
	*
	*	Member variables
	*
	*********************************************************************************/

	/** Member variable - collection of objects in the pool. */
	private ArrayList m_Objects = null;

	/** Member variable - initial capacity. */
	private int m_nInitialCapacity = 0;

	/** Member variable - capacity. */
	private int m_nCapacity = 0;

	/** Member variable - has the pool been initialized. */
	private boolean m_bInitialized = false;

	/** Member variable - holds the idle resource manager. */
	private IdleResourceManager m_IdleResourceManager = null;

	/*********************************************************************************
	*
	*	Inner Class: Background thread that maintains the size of the pool with
	*	regards to idle resources. Class is an extension to <I>Thread</I> so
	*	that the parent class can reference the Thread object and this object
	*	with the same variable. Simplifies coding. For an example see the
	*	<CODE>m_IdleResourceManager</CODE> variable above.
	*
	*********************************************************************************/

	private class IdleResourceManager extends Thread
	{
		/** Constructor - accepts a reference to the Pool to be managed.
			@param pPool Pool object to manager.
		*/
		public IdleResourceManager(AbstractPool pPool) { m_Pool = pPool; }

		/** Action method - implements the thread functionality. */
		public void run()
		{
			// Always start over when the thread is started.
			m_nReportedUnderUsage = 0;

			// Poll the pool once a minute.
			while (true)
			{
				// Sleep for a minute.
				try { Thread.sleep(60000); }
				catch (Exception pEx) { break; }

				int nObjectsInUse = (m_Pool.m_nCapacity - m_Pool.getSize());
				if (0 == nObjectsInUse) nObjectsInUse = 1;

System.out.println(nObjectsInUse + ", " + m_Pool.m_nCapacity + ", " + m_Pool.m_nInitialCapacity + ", " + m_nReportedUnderUsage);

				// Has the pool exceeded it necessary capacity limit?
				if ((m_Pool.m_nCapacity > m_Pool.m_nInitialCapacity) &&
					(2 < (m_Pool.m_nCapacity / nObjectsInUse)))
					m_nReportedUnderUsage++;

				// Otherwise, resume from scratch.
				else
					m_nReportedUnderUsage = 0;

				// Has the excess resources existed for at least two loops.
				if (2 <= m_nReportedUnderUsage)
				{
					try { m_Pool.halfCapacity(); m_nReportedUnderUsage = 0; }

					// DO NOTHING. Nothing can be done. This is in a separate thread.
					catch (PoolException pEx) {}
				}

			}
		}

		/** Member variable - Pool to be managed. */
		private AbstractPool m_Pool = null;

		/** Member variable - tracks the number of times that the usage reported is
		    below the necessary limit to maintain the current capacity. If the number
		    of times exceeds 1, the capacity will be decreased.
		*/
		private int m_nReportedUnderUsage = 0;
	}
}
