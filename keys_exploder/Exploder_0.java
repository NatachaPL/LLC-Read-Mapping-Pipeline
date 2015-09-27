package keys_exploder;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import core.Read;

/**
  * Returns the best k-sized key from the read, the subsequence of bases with the best score
 */
public class Exploder_0 extends Exploder{

	public Exploder_0(Read read, Integer k) {
		super(read, k);
	}

	protected void explode(Read read, int rindex, 
			ArrayList<SimpleEntry<String, Integer>> keys, ArrayList<SimpleEntry<String, Integer>> temp){
		
		keys.add(read.bestKey());
	
	}
}
