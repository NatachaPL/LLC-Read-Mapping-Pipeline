package keys_exploder;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import util.LLCProperties;
import core.Read;

public abstract class Exploder {
	
	protected static final boolean DEBUG = Boolean.parseBoolean(LLCProperties.p.getProperty("DEBUG"));
	protected final double cutoff =  Double.parseDouble(LLCProperties.p.getProperty("exploder_cuttof"));

	protected int k; //Key size
	protected Read read;
	
	private ArrayList<SimpleEntry<String, Integer>> keys = new ArrayList<SimpleEntry<String, Integer>>(); //Store all the keys to be returned
	private ArrayList<SimpleEntry<String, Integer>> temp = new ArrayList<SimpleEntry<String, Integer>>(); //Store the temporary keys created

	public Exploder(Read read, Integer k) {
		this.k = k.intValue();	
		this.read = read;
	}

	protected abstract void explode(Read read, int index, ArrayList<SimpleEntry<String, Integer>> keys, ArrayList<SimpleEntry<String, Integer>> temp);
	//Method that implements the different algorithms to create keys

	public void executeExplosion() {
		this.explode(read, 0, keys, temp);
		//The index at 0 mark the position where the algorithms start the keys explosion
	}
	
	public ArrayList<SimpleEntry<String, Integer>> explodeKeys(){		
		return keys;
	}	
}
