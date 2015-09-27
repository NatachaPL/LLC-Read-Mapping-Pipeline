package keys_exploder;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import core.Read;

/**
  * Returns a k-sized key from the beginning of the read
 */
public class Exploder_00 extends Exploder{

	public Exploder_00(Read read, Integer k) {
		super(read, k);
	}

	protected void explode(Read read, int rindex, 
			ArrayList<SimpleEntry<String, Integer>> keys, ArrayList<SimpleEntry<String, Integer>> temp){
		
		String read_key = read.getKey();		
		keys.add(new SimpleEntry<String, Integer>(read_key, 0));
	
	}
}
