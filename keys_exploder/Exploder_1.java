import java.util.AbstractMap.SimpleEntry;

import core.Read;

/**
  * The algorithm takes into account all the 4^k possibilities for a k-size key by creating keys with base permutation
 */
public class Exploder_1 extends Exploder{

	public Exploder_1(Read read, Integer k) {
		super(read, k);
	}

	protected void explode(Read read, int rindex, 
			ArrayList<SimpleEntry<String, Integer>> keys, ArrayList<SimpleEntry<String, Integer>> temp){
		
		keys.add(read.bestKey());
	
		while(rindex<k){	
			for (int i = 0; i < keys.size(); i++) {	
				if (keys.get(i).getKey().charAt(rindex) == 'A') {
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'C' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'G' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'T' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
				}
				if (keys.get(i).getKey().charAt(rindex) == 'C') {
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'A' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'G' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'T' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
				}
				if (keys.get(i).getKey().charAt(rindex) == 'G') {
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'A' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'C' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'T' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
				}
				if (keys.get(i).getKey().charAt(rindex) == 'T') {
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'A' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'C' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'G' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
				}
				if (keys.get(i).getKey().charAt(rindex) == 'N') {
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'A' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'C' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'G' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
					temp.add(new SimpleEntry<String, Integer>(keys.get(i).getKey().substring(0, rindex) + 'T' 
							+ keys.get(i).getKey().substring(rindex + 1, k), 0));
				}	
			}
			
			keys.addAll(temp);
			temp.clear();
			rindex++;
		}
	}
}
