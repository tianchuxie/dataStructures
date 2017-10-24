package cmsc420.sortedmap;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

import cmsc420.canonicalsolution.City;

public class AvlGTree <K, V> implements SortedMap<K, V> {
	private Node<K,V> root;
	private Comparator<? super K> comparator;
	private int g;
	private transient int modCount;
	private V previousValue;
	private Collection<V> values;
	private Set<java.util.Map.Entry<K, V>> entries;
	private Set<K> keySet;
	
	// Default constructor
	public AvlGTree(){
		this.root = null;
		this.comparator = null;
		this.previousValue = null;
	}
	
	// If this generic wildcard confuses you, reread the end of the
	// part 1 spec for the introduction to generics.
	public AvlGTree(final Comparator<? super K> comparator, final int g){
		this.root = null; 
		this.comparator = comparator;
		this.g = g;
		this.previousValue = null;
	}
	
	@Override
	public void clear() {
		root = null;
	}

	@Override
	public boolean containsKey(Object key) {
		return getEntry(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		if(value == null)
			throw new NullPointerException();
		for(Node <K,V> e = getFirstEntry(); e != null; e = successor(e)){
			if (valEquals(value, e.getValue()))
				return true;
		}
		return false;
	}
	
	private Node<K, V> getFirstEntry() {
		Node<K, V> p = root;
		if (p != null)
			while(p.left != null)
				p = p.left;
		return p;
	}

	@Override
	public V get(Object key) {
		if(key == null)
			throw new NullPointerException();
		return root == null ? null : get(root, (K) key);
	}
	
	public int getHeight(){
		return height(root) + 1;
	}
	
	@Override
	public boolean isEmpty() {
		return root == null ? true : false;
	}

	@Override
	public V put(K key, V value) {
		if(key == null || value == null){
			throw new NullPointerException();
		}
		root = (root == null) ? insert(key, value, root, null) :
				                insert(key, value, root, root.parent);
		return previousValue;
	}
	
	@Override
	 public void putAll(Map<? extends K, ? extends V> map) {
        int mapSize = map.size();
        if (this.size()==0 && mapSize!=0 && map instanceof SortedMap) {
                ++modCount;
                for(java.util.Map.Entry<? extends K, ? extends V> entry : map.entrySet()){
                	this.put(entry.getKey(), entry.getValue());
                }
        }
    }

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return (root == null) ? 0 : size(root);
	}

	@Override
	public Comparator<? super K> comparator() {
		return comparator;
	}

	@Override
	public K firstKey() {
		if (root == null)
			throw new NoSuchElementException();
		else
			return root.getFirstEntry().getKey();
	}

	@Override
	public K lastKey() {
		if (root == null)
			throw new NoSuchElementException();
		else
			return root.getLastEntry().getKey();
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		return new SubMap(null, toKey);
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		return new SubMap(fromKey, toKey);
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		return new SubMap(fromKey, null);
	}

	@Override
	public String toString(){
		return toString(root, 1);
	}
	
	@Override
	public int hashCode(){
		int h = 0;
		if(entrySet().isEmpty())
			return h;
		Iterator<Entry<K,V>> i = entrySet().iterator();
		while (i.hasNext())
			h += i.next().hashCode();
		return h;
	}
	
	@Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Map))
            return false;
        Map<K,V> m = (Map<K,V>) o;
        if (m.size() != size())
            return false;
        if (m.size() == size() && size() == 0)
        	return true;

        try {
            Iterator<Entry<K,V>> i = entrySet().iterator();
            while (i.hasNext()) {
                Entry<K,V> e = i.next();
                K key = e.getKey();
                V value = e.getValue();
                if (value == null) {
                    if (!(m.get(key)==null && m.containsKey(key)))
                        return false;
                } else {
                    if (!value.equals(m.get(key)))
                        return false;
                }
            }
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused) {
            return false;
        }

        return true;
    }
	
	@Override
	public Collection<V> values() {
		Collection<V> vs = values;
		return (vs != null) ? vs : (values = new Values());
	}

	@Override
	public Set<K> keySet() {
		Set<K> ks = keySet;
		return (ks != null) ? ks : (keySet = new KeySet());
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> es = entries;
	
		if(es != null){
			return es;
		} else 
			return entries = new EntrySet();
	}
	
	public Node<K,V> getRoot(){
		return root;
	}

	private V get(Node<K,V> root, K key){
		if(root == null)
			return null;
		if(comparator.compare(key, root.getKey()) < 0){
			return get(root.left, key);
		} 
		else if(comparator.compare(key, root.getKey()) > 0){
			return get(root.right, key);
		} else {
			return root.getValue();
		}
	}

	private Node<K,V> insert(K key, V value, Node<K,V> root, Node<K,V> parent) {
		int balanceFactor;
		if (root == null)
			root = new Node<K, V>(key, value, parent);
		
		// If key is equal to root.getKey()
		else if (comparator.compare(root.getKey(), key) == 0){
			previousValue = root.getValue();
			root.setValue(value);
		}
		else {
			// If key is lesser than root.getKey()
			if (comparator.compare(root.getKey(), key) > 0)
				root.left = insert(key, value, root.left, root);
			// If key is greater than root.getKey()
			else if (comparator.compare(root.getKey(), key) < 0)
				root.right = insert(key, value, root.right, root);
			
			
			balanceFactor = height(root.left) - height(root.right);
			
			if(balanceFactor > g){
				if(comparator.compare(key, (K) root.left.getKey()) < 0) {
					root = rotateRight(root);
				} else {
					root = rotateLeftRight(root);
				}
			}
			else if (balanceFactor < -g){
				if(comparator.compare(key, (K) root.right.getKey()) > 0) {
					root = rotateLeft(root);
				} else {
					root = rotateRightLeft(root);
				}
			}
		}
		root.height = max(height(root.left), height(root.right)) + 1;
		return root;
	}

	private Node<K,V> rotateRightLeft(Node<K,V> root) {
	    root.right = rotateRight( root.right );
        return rotateLeft(root);
	}

	private Node<K,V> rotateRight(Node<K,V> root) {
        Node<K,V> temp = root.left;
        temp.parent = root.parent;
        root.parent = temp;
   
        root.left = temp.right;
        if(root.left != null)
        	root.left.parent = root;
        
        temp.right = root;
        
        root.height = max( height( root.left ), height( root.right ) ) + 1;
        temp.height = max( height( temp.left ), root.height ) + 1;
        
        if(temp.left != null)
        	temp.left.parent = temp;
        
        temp.right.parent = temp;
        return temp;
	}

	private Node<K,V> rotateLeftRight(Node<K,V> root) {
		root.left = rotateLeft( root.left );
	    return rotateRight(root);
	}

	private Node<K,V> rotateLeft(Node<K,V> root) {
		Node<K,V> temp = root.right;
		temp.parent = root.parent;
		root.parent = temp;
		
	    root.right = temp.left;
	    if(root.right != null)
	    	root.right.parent = root;
	    
	    temp.left = root;
	      
	    root.height = max( height( root.left ), height( root.right ) ) + 1;
	    temp.height = max( height( temp.right ), root.height ) + 1;
	    
	    if(temp.right != null)
	    	temp.right.parent = temp;
	    
	    temp.left.parent = temp;
	    return temp;
	}

	private int height(Node<K,V> root){
		return root == null ? -1 : root.height;
	}
	
	private int max(int left, int right){
		return left > right ? left: right;
	}
	
	private int size(Node<K,V> root) {
		if(root == null)
			return 0;
		else
			return 1 + size(root.left) + size(root.right);
	}

	StringBuffer tab = new StringBuffer();
	public String toString(Node<K,V> root, int level){
		if (root == null)
			return "";
		else {
			tab.setLength(0);
			for (int i = 1; i < level; i++)
				tab.append("\t");
	
			return tab.toString() + root.toString() + "\n" + toString(root.left, level + 1) + toString(root.right, level + 1);
		}
	}

	private class SubMap implements SortedMap<K, V> {
		K fromKey, toKey;
		
		public SubMap(K fromKey, K toKey){
			if(fromKey != null && toKey != null && comparator.compare(fromKey, toKey) > 0)
				throw new IllegalArgumentException();
			
			this.fromKey = fromKey;
			this.toKey = toKey;
		}
		
		@Override
		public int hashCode(){
			int h = 0;
			if(entrySet().isEmpty())
				return h;
			Iterator<Entry<K,V>> i = entrySet().iterator();
			while (i.hasNext())
				h += i.next().hashCode();
			return h;
		}
		
		@Override
		public void clear() {
			throw new UnsupportedOperationException();
		}
	
		@Override
		public boolean containsKey(Object key) {
			return inRange(key, fromKey, toKey) && AvlGTree.this.containsKey(key);
		}
	
		@Override
		public boolean containsValue(Object value) {
			for(Map.Entry<K, V> entry : entrySet()){
				if(entry.getValue() == value)
					return true;
			}
			return false;
		}
	
		@Override
		public V get(Object key) {
			return !inRange(key, fromKey, toKey) ? null : AvlGTree.this.get(key);
		}
	
		@Override
		public boolean isEmpty() {
			return entrySet().isEmpty();
		}
	
		@Override
		public V put(K key, V value) {
			if(!inRange(key, fromKey, toKey))
				throw new IllegalArgumentException("key out of range");
			return AvlGTree.this.put(key, value);
		}
	
		@Override
		public void putAll(Map<? extends K, ? extends V> m) {
			for(Object o : m.entrySet()){
				if(!(o instanceof Map.Entry))
					throw new ClassCastException();
				Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
				this.put(entry.getKey(),entry.getValue());
			}
		}
	
		@Override
		public V remove(Object key) {
			return !inRange(key, fromKey, toKey) ? null : AvlGTree.this.remove(key);
		}
	
		@Override
		public int size() {
			return entrySet().size();
		}
	
		@Override
		public Comparator<? super K> comparator() {
			return AvlGTree.this.comparator();
		}
	
		@Override
		public Set<java.util.Map.Entry<K, V>> entrySet() {
			return new SubEntrySet(fromKey, toKey);		
		}
	
		@Override
		public K firstKey() {
			K key = null;
			Iterator<K> iter = this.keySet().iterator();
			if(iter.hasNext()){
				key = iter.next();
			}
			return key;
		}
	
		@Override
		public SortedMap<K, V> headMap(K toKey) {
			return new SubMap(null, toKey);
		}
	
		@Override
		public Set<K> keySet() {
			return new SubKeySet(fromKey, toKey);
		}
	
		@Override
		public K lastKey() {
			K key = null;
			Iterator<K> iter = this.keySet().iterator();
			while(iter.hasNext()){
				key = iter.next();
				if(!iter.hasNext())
					break;
			}
			return key;
		}
	
		@Override
		public SortedMap<K, V> subMap(K fromKey, K toKey) {
			return new SubMap(fromKey, toKey);
		}
	
		@Override
		public SortedMap<K, V> tailMap(K fromKey) {
			return new SubMap(fromKey, null);
		}
	
		@Override
		public Collection<V> values() {
			Collection<V> value = new ArrayList<V>();
			for(Map.Entry<K, V> entry : entrySet()){
				value.add(entry.getValue());
			}
			return values;
		}
		@Override 
		public String toString(){
			return entrySet().toString();
		}
		
		@Override
		public boolean equals(Object o){
			if(o == this)
				return true;
			
			if(!(o instanceof Map))
				return false;
			
			Map<K,V> m = (Map<K,V>) o;
			
			if(m.size() != this.size())
				return false;
			
			try {
				Iterator<Entry<K, V>> i = this.entrySet().iterator();
				while(i.hasNext()){
					Entry<K,V> e = i.next();
					K key = e.getKey();
					V value = e.getValue();
					if(value == null) {
						if(!(m.get(key) == null) && m.containsKey(key))
							return false;
					} else {
						if (!value.equals(m.get(key)))
							return false;
					}
				}
			} catch (ClassCastException unused){
				return false;
			} catch (NullPointerException unused){
				return false;
			}
			
			return true;
		}
	
	
	}

	protected class EntrySet extends AbstractSet<Map.Entry<K,V>> {
		
		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new EntryIterator(root == null ? null :root.getFirstEntry());
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
		
			Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
		
			V value = entry.getValue();
			Entry<K,V> p = getEntry(entry.getKey());
			return p != null && valEquals(p.getValue(), value);
		}

		@Override
		public int size() {
			return AvlGTree.this.size();
		}

		@Override
		public void clear(){
			AvlGTree.this.clear();
		}
	}
	
	protected class SubEntrySet extends AbstractSet<Map.Entry<K, V>>{
		K fromKey, toKey;
		
		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new SubEntryIterator(root.getFirstEntry(), fromKey, toKey);
		}
	
		public SubEntrySet(K fromKey, K toKey){
			this.fromKey = fromKey;
			this.toKey = toKey;
		}

		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry))
				return false;
		
			Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
		
			if(!inRange(entry.getKey(), fromKey, toKey))
				return false;
			
			V value = entry.getValue();
			Entry<K,V> p = getEntry(entry.getKey());
			
			return p != null && valEquals(p.getValue(), value);
		}

		@Override
		public boolean isEmpty() {
			return this.size() == 0 ? true : false;
		}

		@Override
		public int size() {
			int count = 0;
			Iterator<Map.Entry<K, V>> iter = this.iterator();
			while(iter.hasNext()){
				iter.next();
				count++;
			}
			return count;
		}
	}
	
	protected class SubKeySet extends AbstractSet<K>{
		K fromKey, toKey;
		
		public SubKeySet(K fromKey, K toKey){
			this.fromKey = fromKey;
			this.toKey = toKey;
		}
		
		@Override
		public Iterator<K> iterator() {
			return new SubKeyIterator(root.getFirstEntry(), fromKey, toKey);
		}

		@Override
		public int size() {
			int count = 0;
			Iterator<K> iter = this.iterator();
			while(iter.hasNext()){
				iter.next();
				count++;
			}
			return count;
		}

	}
	
	protected class SubKeyIterator extends PrivateEntryIterator<K>{
		private K fromKey, toKey;
		Node<K,V> first;
		
		SubKeyIterator(Node<K, V> first, K fromKey, K toKey) {
			super();
			this.first = first;
			this.fromKey = fromKey;
			
			while(fromKey != null && comparator.compare(first.getKey(), fromKey) < 0){
				first = successor(first);
				if(first == null)
					break;
			}
			
			expectedModCount = modCount;
			lastReturned = null;
			next = first;
			
			this.fromKey = fromKey;
			this.toKey = toKey;
		}
		
		@Override
		public K next() {
			Node<K,V> e = next;
			if (e == null)
				throw new NoSuchElementException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			next = successor(e);
			
			if(toKey != null && comparator.compare(next.getKey(), toKey) >= 0 ){
				next = null;
			}
			
			lastReturned = e;
			return e.getKey();
		}

	}
	
	protected class SubEntryIterator extends PrivateEntryIterator<Map.Entry<K,V>>{
		private K fromKey, toKey;
		Node<K,V> first;
		
		SubEntryIterator(Node<K, V> first, K fromKey, K toKey) {
			super();
			this.first = first;
			this.fromKey = fromKey;
			
			while(fromKey != null && comparator.compare(first.getKey(), fromKey) < 0){
				first = successor(first);
				if(first == null)
					break;
			}
			
			expectedModCount = modCount;
			lastReturned = null;
			next = first;
			
			this.fromKey = fromKey;
			this.toKey = toKey;
		}

		@Override
		public java.util.Map.Entry<K, V> next() {
			Node<K,V> e = next;
			if (e == null)
				throw new NoSuchElementException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			next = successor(e);
			
			if(toKey != null && comparator.compare(next.getKey(), toKey) >= 0 ){
				next = null;
			}
			
			lastReturned = e;
			return e;
		}
	}
	
	final class EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>>{
		
		EntryIterator(Node<K, V> first) {
			super(first);
		}

		@Override
		public java.util.Map.Entry<K, V> next() {
			return nextEntry();
		}
	}
	
	final class ValueIterator extends PrivateEntryIterator<V>{

		ValueIterator(Node<K, V> first) {
			super(first);
		}
		
		@Override
		public V next() {
			return nextEntry().getValue();
		}	
	}
	
	final class KeyIterator extends PrivateEntryIterator<K>{
		KeyIterator(Node<K, V> first) {
			super(first);
		}

		@Override
		public K next() {
			return nextEntry().getKey();
		}
	}

	protected abstract class PrivateEntryIterator<T> implements Iterator<T>{
		Node<K,V> next;
		Node<K,V> lastReturned;
		int expectedModCount;
		
		PrivateEntryIterator(){};
		
		PrivateEntryIterator(Node<K,V> first){
			if(first == null)
			expectedModCount = modCount;
			lastReturned = null;
			next = first;
		}
		
		@Override
		public boolean hasNext() {
			return next != null;
		}

		final Node<K,V> nextEntry(){
			Node<K,V> e = next;
			if (e == null)
				throw new NoSuchElementException();
//			if (modCount != expectedModCount)
//				throw new ConcurrentModificationException();
			next = successor(e);
			lastReturned = e;
			return e;
		}	
		
		@Override 
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	protected class KeySet extends AbstractSet<K>{

		@Override
		public Iterator<K> iterator() {
			return new KeyIterator(root == null ? null : root.getFirstEntry());
		}

		@Override
		public int size() {
			return AvlGTree.this.size();
		}
	}

	protected class Values extends AbstractCollection<V>{

		@Override
		public Iterator<V> iterator() {
			return new ValueIterator(root.getFirstEntry());
		}

		@Override
		public int size() {
			return AvlGTree.this.size();
		}
		
		@Override
		public boolean contains(Object o){
			return AvlGTree.this.containsValue(o);
		}
		
		@Override
		public boolean remove(Object o){
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void clear(){
			AvlGTree.this.clear();
		}
		
	}
	
 	public Node<K, V> successor(Node<K, V> e) {
		if(e == null)
			return null;
		else if (e.right != null){
			Node<K,V> parent = e.right;
			while(parent.left != null)
				parent = parent.left;
			return parent;
		} else {
			Node<K,V> parent = e.parent;
			Node<K,V> child = e;
			while(parent != null && child == parent.right){
				child = parent;
				parent = parent.parent;
			}
			return parent;
		}
	}

	/* Returns the map's entry for the given Key, or null if 
	 * the map does not contain an entry for the key*/
	final Node<K,V> getEntry(Object key){
		if(key == null)
			throw new NullPointerException();
		Node<K,V> p = root;
		while(p != null) {
			int cmp = comparator.compare((K) key, p.getKey());
			
			if(cmp < 0)
				p = p.left;
			else if (cmp > 0)
				p = p.right;
			else
				return p;
		}
		return null;
	}

	final boolean inRange(Object key, K fromKey, K toKey){
		if(fromKey == null && toKey == null)
			return true;
		// TailMap
		else if(fromKey != null && toKey == null)
			return comparator.compare((K)key, fromKey) >= 0 ? true : false;
		// HeadMap
		else if(fromKey == null && toKey != null)
			return comparator.compare((K)key, toKey) < 0 ? true : false;
		else if(fromKey != null && toKey != null){
			// SubMap
			if(comparator.compare((K)key, toKey) < 0 ? true : false){
				if(comparator.compare((K)key, fromKey) >= 0 ? true: false)
					return true;
			}
		}
		return false;
	}
	
	final static  boolean valEquals(Object o1, Object o2){
		return (o1 == null ? o2 == null :  o1.equals(o2));
	}
} 