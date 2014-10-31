package com.small.library.util;

import java.util.*;

/***************************************************************************************
*
*	A Map class that maintains a List with the objects in order they were added
*	for iteratoring through.
*
*	@author David Small
*	@version 1.1.0.0
*	@date 4/18/2002
*
***************************************************************************************/

public class MapList implements Map, java.io.Serializable
{
	/******************************************************************************
	*
	*	Constructors
	*
	******************************************************************************/

	/** Constructor - constructs an empty object. */
	public MapList()
	{
		m_List = new ArrayList();
		m_Map = new HashMap();
	}

	/** Constructor - constructs an empty object with an initial capacity.
		@param nInitialCapacity Initial size of the Map List.
	*/
	public MapList(int nInitialCapacity)
	{
		m_List = new ArrayList(nInitialCapacity);
		m_Map = new HashMap(nInitialCapacity);
	}

	/** Constructor - constructs a populated object.
		@param pMap <I>Map</I> of objects to prepopulate the Map List with.
	*/
	public MapList(Map pMap)
	{
		this(pMap.size());
		putAll(pMap);
	}

	/******************************************************************************
	*
	*	Accessor methods
	*
	******************************************************************************/

	/** Accessor method - Returns true if this list contains the specified
	    element.
	*/
	public boolean contains(Object o) { return m_List.contains(o); }

	/** Accessor method - Returns true if this list contains
	    all of the elements of the specified collection.
	*/
	public boolean containsAll(Collection c) { return m_List.containsAll(c); }

	/** Accessor method - Returns true if this map contains a mapping for the
	    specified key.
	*/
	public boolean containsKey(Object key) { return m_Map.containsKey(key); }
           
	/** Accessor method - Returns true if this map maps one or more keys to the
	    specified value.
	*/
	public boolean containsValue(Object value) { return m_Map.containsValue(value); }

	/** Accessor method - Returns a set view of the mappings contained in this
	    map.
	*/
	public Set entrySet() { return m_Map.entrySet(); }

	/** Accessor method - Compares the specified object with this map for equality. */
	public boolean equals(Object o) { return m_Map.equals(o); }

	/** Accessor method - Returns the element at the specified position in this list.
	*/
	public Object get(int index) { return m_List.get(index); }

	/** Accessor method - Returns the value to which this map maps the specified key. */
	public Object get(Object key)
	{
		Integer intIndex = (Integer) m_Map.get(key);

		if (intIndex == null)
			return null;

		return m_List.get(intIndex.intValue());
	}

	/** Accessor method - Returns the hash code value for this map. */
	public int hashCode() { return m_Map.hashCode(); }

	/** Accessor method - Returns the index in this list of the first occurrence
	    of the specified element, or -1 if this list does not contain this element.
	*/
	public int indexOf(Object o) { return m_List.indexOf(o); }

	/** Accessor method - Returns an iterator over the elements in this list in
	    proper sequence.
	*/
	public Iterator iterator() { return m_List.iterator(); }

	/** Accessor method - Returns true if this map contains no key-value mappings. */
	public boolean isEmpty() { return m_Map.isEmpty(); }

	/** Accessor method - Returns a set view of the keys contained in this map. */
	public Set keySet() { return m_Map.keySet(); }

	/** Accessor method - Returns the index in this list of the last
	    occurrence of the specified element, or -1 if this list does not
	    contain this element.
	*/
	public int lastIndexOf(Object o) { return m_List.lastIndexOf(o); }

	/** Accessor method - Returns a list iterator of the elements in
	    this list (in proper sequence).
	*/
	public ListIterator listIterator() { return m_List.listIterator(); }

	/** Accessor method - Returns a list iterator of the elements in this
	    list (in proper sequence), starting at the specified position in this list.
	*/
	public ListIterator listIterator(int index) { return m_List.listIterator(index); }

	/** Accessor method - Returns the number of key-value mappings in this map. */
	public int size() { return m_List.size(); }

	/** Mutator method - Returns a view of the portion of this list
	    between the specified fromIndex, inclusive, and toIndex, exclusive.
	*/
	public  List subList(int fromIndex, int toIndex)
	{ return m_List.subList(fromIndex, toIndex); }

	/** Accessor method - Returns an array containing all of the elements in this
	    list in proper sequence.
	*/
	public Object[] toArray() { return m_List.toArray(); }

	/** Accessor method - Returns an array containing all of the elements in
	    this list in proper sequence; the runtime type of the returned array is that of the specified array.
	*/
	public Object[] toArray(Object[] a) { return m_List.toArray(a); }

	/** Accessor method - Returns a collection view of the values contained in
	    this map.
	*/
	public Collection values() { return m_Map.values(); }

	/******************************************************************************
	*
	*	Mutator methods
	*
	******************************************************************************/

	/** Mutator method - Removes all mappings from this map (optional operation). */
	public void clear()
	{
		m_List.clear();
		m_Map.clear();
	}

	/** Mutator method - Associates the specified value with the specified key in
	    this map (optional operation).
	*/
	public Object put(Object key, Object value)
	{
		Integer i = (Integer) m_Map.get(key);

		if (null == i)
		{
			m_List.add(value);
			return m_Map.put(key, new Integer(m_List.size() - 1));
		}

		return m_List.set(i.intValue(), value);
	}

	/** Mutator method - Copies all of the mappings from the specified map to this
	    map (optional operation).
	*/
	public void putAll(Map t)
	{
		Iterator itKeys = t.keySet().iterator();

		while (itKeys.hasNext())
		{
			Object pKey = itKeys.next();
			m_Map.put(pKey, t.get(pKey));
		}
	}

	/** Mutator method - Removes the mapping for this key from this map if it
	    is present (optional operation).
	*/
	public Object remove(Object key)
	{
		// Get the position
		Integer intIndex = (Integer) m_Map.get(key);

		// Has one been found?
		if (null == intIndex)
			return null;

		int nIndex = intIndex.intValue();
		Object pOldValue = m_List.remove(nIndex);
		m_Map.remove(key);

		// No need to compact.
		if (nIndex >= size())
			return pOldValue;

		// Must decrement the Integer values that map to the array list.
		// This is going to be slow. Must devise new storage method in future.
		Iterator itKeys = m_Map.keySet().iterator();

		while (itKeys.hasNext())
		{
			Object pKey = itKeys.next();
			intIndex = (Integer) m_Map.get(pKey);

			// Override previous value.
			if (nIndex < intIndex.intValue())
				m_Map.put(pKey, new Integer(intIndex.intValue() - 1));
		}

		// Return the value that was removed.
		return pOldValue;
	}

	/******************************************************************************
	*
	* 	Member variables
	*
	******************************************************************************/

	/** Member variable - contains a reference to the objects in the list. */
	private ArrayList m_List = null;

	/** Member variable - contains the map of the objects in the list. */
	private Map m_Map = null;
}
