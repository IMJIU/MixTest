package com.jdk.reference;

//: containers/CanonicalMapping.java
// Demonstrates WeakHashMap.
import java.util.*;

class Element {

	private String ident;

	public Element(String id) {
		ident = id;
	}
	public String toString() {
		return ident;
	}
	public int hashCode() {
		return ident.hashCode();
	}
	public boolean equals(Object r) {
		return r instanceof Element && ident.equals(((Element) r).ident);
	}
	protected void finalize() {
		System.out.println("Finalizing " + getClass().getSimpleName() + " " + ident);
	}
}

class Key extends Element {

	public Key(String id) {
		super(id);
	}
}

class Value extends Element {

	public Value(String id) {
		super(id);
	}
}

public class CanonicalMapping {

	public static void main(String[] args) {
		int size = 1000;
		// Or, choose size via the command line:
		if (args.length > 0)
			size = new Integer(args[0]);
		Key[] keys = new Key[size];
		WeakHashMap<Key, Value> map = new WeakHashMap<Key, Value>();
		for (int i = 0; i < size; i++) {
			Key k = new Key(Integer.toString(i));
			Value v = new Value(Integer.toString(i));
			if (i % 3 == 0)
				keys[i] = k; // Save as "real" references
			map.put(k, v);
		}
		System.gc();
		System.out.println("======="+map.size());
		TreeMap<String, String> tree =  new TreeMap<>();
		for (Key key : map.keySet()) {
	        tree.put(key.toString(),map.get(key).toString());
        }
//		for (String key : tree.keySet()) {
//	        System.out.println(key);
//        }
		System.out.println("MAP:"+map.size());
		System.out.println("TREE:"+tree.size());
	}
} /* (Execute to see output) */// :~
