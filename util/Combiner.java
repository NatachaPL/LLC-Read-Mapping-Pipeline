package util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;



import java.util.HashSet;
import java.util.Set;

import core.*;
import keys_exploder.*;
import alignment.*;

/**
 * The class Combiner is responsible for combine the algorithms for the key exploding and for the alignment
 * whose names were given by the user at the properties configuration file.
 */
public abstract class Combiner {
	
	protected static final boolean DEBUG = Boolean.parseBoolean(LLCProperties.p.getProperty("DEBUG"));

	private Genome genome;
	private Read read;
	private int k; //key size parameter
	protected ArrayList<AlignerResult> temp_res = new ArrayList<AlignerResult>(); //Temporary results
	protected ArrayList<PositionScore> results = new ArrayList<PositionScore>(); //Final results

	public Combiner(Genome genome, Read read, int k) {
		this.genome = genome;
		this.read = read;
		this.k = k;
	}

	/**
	 * @param exploder
	 * @param aligner
	 * 
	 * Receives exploder and aligner names to combine the algorithms.
	 */
	public void combine(String exploder, String aligner){

//		System.err.println("Slave " + Thread.currentThread().getName() + ": Combiner -> going to generate keys...");

		//Get the keys
		ArrayList<SimpleEntry<String, Integer>> keys = getKeys(exploder, read, k);

//		if(keys.size() > 100) {
//			System.err.println("Slave " + Thread.currentThread().getName() + ": Combiner -> " + keys.size() + " HUGE NUMBER: Initial key: " + read.toString());	
//		}
			
		
//		System.err.println("Slave " + Thread.currentThread().getName() + ": Combiner -> retrieving positions...");

		//Search the keys along the genome and retrieve positions in which were found
		ArrayList<Integer> positions = keySearch(keys, genome);
		
//		System.err.println("Slave " + Thread.currentThread().getName() + ": Combiner -> compute alignement...");
		
		//For each position found at the genome, align the read with the genomic sequence
		computeAlignment(aligner, genome, read, positions);
		
//		System.err.println("Slave " + Thread.currentThread().getName() + ": Combiner -> display results...");
		
		results_display();

		if(DEBUG){		
			System.out.println("@Combiner:");
			System.out.println("Exploder algorithm: " + exploder);
			System.out.println("Alignment algorithm: " + aligner);
			System.out.println("Size of key set: " + keys.size());
			System.out.println("Positions set: " + positions.size());
			System.out.println("Compute terminated.");
		}

		keys = null; //Free memory
//		System.err.println("Slave " + Thread.currentThread().getName() + ": Combiner -> Process ended.");

	}

	/**
	 * @param exploder
	 * @param read
	 * @param k
	 * 
	 * To a given exploder name, an exploding key algorithm is applied to the read creating k-sized keys.
	 */
	private ArrayList<SimpleEntry<String, Integer>> getKeys(String exploder, Read read, int k){

		Exploder keys =	getExploder(exploder, read, k);

		keys.executeExplosion();

		if(DEBUG){
			int ite = 0;
			System.out.println("Before: " + read.getKey());
			System.out.println("After");
			for(SimpleEntry<String, Integer> key : keys.explodeKeys()){
				System.out.println(key);
				ite+=1;
			}
			System.out.println(exploder + ":  " + " Iterations: " + ite);
		}
		return keys.explodeKeys();
	}	

	/**
	 * @param keys
	 * @param genome
	 * 
	 * To a given set of keys, return the positions in which one of them were found. 
	 */
	private ArrayList<Integer> keySearch(ArrayList<SimpleEntry<String, Integer>> keys, Genome genome){
		int count = 0;

		ArrayList<Integer> temp = new ArrayList<Integer>(); //Values found for the key in the genome
		Set<Integer> positions = new HashSet<Integer>(); //Possible positions for the read at the genome
	
		//For each key (composed by a String (key) and an Integer (read index in which the key started)) at the keys set
//		System.err.println("Slave " + Thread.currentThread().getName() + ": Combiner -> Key Search -> Number of keys: " + keys.size());
		for(SimpleEntry<String, Integer> key : keys){ 
			count++;
			if(DEBUG){
				System.out.println(key);
			}
			
			//Get genomic positions for the key
			if(genome.getPositions(key.getKey()) != null){ 
				temp.addAll(genome.getPositions(key.getKey()));

				//The returned positions have to account the read index in which the key started
				if(key.getValue() > 0){
					for( int p : temp){ 
						if(DEBUG)
							System.out.println("Position = " + p + " Index = " + key.getValue());			
						if(p - key.getValue() >= 0){ //Positions can't have negative integers
							positions.add(p - key.getValue());
							//				System.out.println(p- key.getValue());
						}
					} 
				}
				//If the read index is 0, then all the positions obtained can be added as they are
				else{
					positions.addAll(temp);
				}
				temp.clear();
			}

//			System.err.println("Slave " + Thread.currentThread().getName() + ": Combiner -> Key Search -> Number of missing keys: " + (keys.size() - count));
			
		}		
		
		keys.clear();
		keys = null;
		
		if(DEBUG) {
			System.out.println("# Keys: "+count);
			System.out.println("# Different Position: " + positions.size());
		}
//		return new ArrayList<Integer>(positions);
		ArrayList<Integer> toReturn = new ArrayList<Integer>(positions);
		positions = null;
		return toReturn;

	}

	/**
	 * @param aligner
	 * @param genome
	 * @param read
	 * @param positions
	 * 
	 * To a given aligner name, an alignment is processed between the read and the genomic sequence started at each position
	 */
	private void computeAlignment(String aligner, Genome genome, Read read, ArrayList<Integer> positions){
		ArrayList<SimpleEntry<char[], Integer>> refined_positions = genome.genSeq(genome.getGenome(), read.getLength(), positions);
		
		while(!refined_positions.isEmpty()) {		
			SimpleEntry<char[], Integer> g_seq = refined_positions.remove(refined_positions.size()-1);
			Aligner compute = getAligner(aligner, g_seq.getKey(), read);
			compute.initialize();
			temp_res.add(new AlignerResult(read.getID(), compute.getAlignedRead(), g_seq.getValue(), compute.getScore()));
		}	
	}	

	protected abstract void results_display();
	
	public ArrayList<PositionScore> getResults(){

		return results;
	}	

	/**
	 * @param name
	 * @param read
	 * @param k
	 * 
	 * The exploding key algorithm is selected trough its subclasse name.
	 */
	private Exploder getExploder(String name, Read read, int k){
		try {
			@SuppressWarnings("unchecked")
			Constructor<Exploder> creator = (Constructor<Exploder>)
			Class.forName(name).getConstructors()[0];
			Object[] args = new Object[2];
			args[0] = read;
			args[1] = new Integer(k);
			Exploder instance = creator.newInstance(args);
			return instance;
		} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;				
		}
	}

	/**
	 * @param name
	 * @param k
	 * @param gen_seq
	 * @param read
	 * 
	 * The alignment algorithm is selected trough its subclasse name.
	 */
	private Aligner getAligner(String name, char[] gen_seq, Read read){
		try {
			@SuppressWarnings("unchecked")
			Constructor<Aligner> creator = (Constructor<Aligner>)
			Class.forName(name).getConstructors()[0];
			Object[] args = new Object[2];
			args[0] = gen_seq ;				
			args[1] = read;
			Aligner instance = creator.newInstance(args);
			return instance;
		} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;				
		}
	}


}
