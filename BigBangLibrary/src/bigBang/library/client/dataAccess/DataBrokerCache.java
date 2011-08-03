package bigBang.library.client.dataAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements a cache for a DataBroker
 * 
 * @author Francisco Cabrita @ Premium Minds Lda.
 */
public class DataBrokerCache {

	/**
	 * Avaliable caching mechanisms
	 */
	public static enum Mechanism {
		LRU,
		MRU,
		LFU,
		MFU
	}

	/**
	 * A class that implements a cache entry
	 */
	protected static class CacheEntry {
		protected int timesAccessed = 0;
		protected int accessTick = 0;
		protected Object value;

		/**
		 * The constructor
		 * @param o The value to be stored in the cache entry
		 */
		public CacheEntry(Object o) {
			value = o;
		}
	}

	protected final int DEFAULT_THRESHOLD = 50;
	protected final int CLEAN_THRESHOLD = 10;

	protected int threshold;
	protected int tick;
	protected Map<String, CacheEntry> store;
	protected Mechanism mechanism;

	/**
	 * The constructor
	 */
	public DataBrokerCache(){
		this(Mechanism.LFU);
	}

	/**
	 * The constructor
	 * @param mechanism The mechanism to be used for caching
	 */
	public DataBrokerCache(Mechanism mechanism) {
		this.mechanism = mechanism;
		this.store = new HashMap<String, CacheEntry>();
		this.threshold = DEFAULT_THRESHOLD;
		this.tick = 0;
	}

	/**
	 * Sets the threshold for the cache size
	 * @param threshold The new threshold. If zero, the cache expands without limitation
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	/**
	 * Returns whether or not an item with the given id is in cache
	 * @param id The item id
	 * @return true if the item in in cache, false otherwise
	 */
	public boolean contains(String id) {
		return store.containsKey(id);
	}

	/**
	 * Gets an item stored in cache
	 * @param id The item id
	 * @return The item
	 */
	public Object get(String id) {
		this.tick++;
		if(!contains(id))
			throw new RuntimeException("The item with id=\"" + id + "\" could not be found in cache.");
		CacheEntry e = store.get(id);
		e.timesAccessed++;
		e.accessTick = this.tick;
		return e.value;
	}

	/**
	 * Gets all the values in cache
	 * @return Collection of values in cache
	 */
	public Collection<Object> getEntries(){
		ArrayList<Object> result = new ArrayList<Object>();
		for(CacheEntry e : store.values()){
			result.add(e.value);
		}
		return result;
	}
	
	/**
	 * Adds a given item to the cache
	 * @param id The id used to access the item
	 * @param o The item
	 */
	public void add(String id, Object o){
		if(store.size() == threshold){
			clean();
		}
		CacheEntry entry = new CacheEntry(o);
		store.put(id, entry);
		entry.accessTick = this.tick;

	}

	/**
	 * Updates an item located in cache
	 * @param id The item id
	 * @param o The item
	 */
	public void update(String id, Object o){
		this.tick++;
		if(!contains(id))
			throw new RuntimeException("The item with id=\"" + id + "\" could not be found in cache.");
		CacheEntry e = store.get(id);
		e.value = o;
		e.timesAccessed++;
		e.accessTick = this.tick;
	}

	/**
	 * Removes an item from cache
	 * @param id The item id
	 */
	public void remove(String id) {
		store.remove(id);
	}
	
	/**
	 * Gets the number of occupied entries in the cache.
	 * @return The number of cache entries
	 */
	public int getNumberOfEntries(){
		return this.store.size();
	}

	/**
	 * Cleans the cache in order to make room for more entries
	 */
	public void clean() {
		switch(mechanism) {
		case LFU:
			cleanLfu();
			break;
		case LRU:
			cleanLru();
			break;
		case MFU:
			cleanMfu();
			break;
		case MRU:
			cleanMru();
			break;
		default:
			throw new RuntimeException("Could not clean the cache. The caching mechanism is not defined.");
		}
	}

	/**
	 * Cleans the cache according to the LFU (Least Frequently Used) mechanism
	 */
	protected void cleanLfu(){
		List<String> auxStore = new ArrayList<String>(this.store.keySet());
		Collections.sort(auxStore, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return store.get(o1).timesAccessed - store.get(o2).timesAccessed;
			}
		});
		int size = auxStore.size();
		for(int i = 0; i < size && i < this.CLEAN_THRESHOLD; i++) {
			this.store.remove(auxStore.get(i));
		}
	}

	/**
	 * Cleans the cache according to the LRU (Least Recently Used) mechanism
	 */
	protected void cleanLru(){
		List<String> auxStore = new ArrayList<String>(this.store.keySet());
		Collections.sort(auxStore, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return store.get(o1).accessTick - store.get(o2).accessTick;
			}
		});
		int size = auxStore.size();
		for(int i = 0; i < size && i < this.CLEAN_THRESHOLD; i++) {
			this.store.remove(auxStore.get(i));
		}
	}

	/**
	 * Cleans the cache according to the MFU (Most Frequently Used) mechanism
	 */
	protected void cleanMfu(){
		List<String> auxStore = new ArrayList<String>(this.store.keySet());
		Collections.sort(auxStore, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return store.get(o2).timesAccessed - store.get(o1).timesAccessed;
			}
		});
		int size = auxStore.size();
		for(int i = 0; i < size && i < this.CLEAN_THRESHOLD; i++) {
			this.store.remove(auxStore.get(i));
		}
	}

	/**
	 * Cleans the cache according to the MRU (Most Recently Used) mechanism
	 */
	protected void cleanMru(){
		List<String> auxStore = new ArrayList<String>(this.store.keySet());
		Collections.sort(auxStore, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return store.get(o2).accessTick - store.get(o1).accessTick;
			}
		});
		int size = auxStore.size();
		for(int i = 0; i < size && i < this.CLEAN_THRESHOLD; i++) {
			this.store.remove(auxStore.get(i));
		}
	}

	/**
	 * Clears the cache completely
	 */
	public void clear() {
		store.clear();
	}

}
