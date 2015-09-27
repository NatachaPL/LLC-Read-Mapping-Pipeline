package core;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import util.LLCProperties;


public class Genome {
	
	private static final boolean DEBUG = Boolean.parseBoolean(LLCProperties.p.getProperty("DEBUG"));

	private HashMap<String, ArrayList<Integer>> hash; 
	private char[] genome_array;
	private int k; //Key size

	public Genome(String filename, int k) throws FileNotFoundException {

		Scanner sc = new Scanner(new File(filename)); //Genome file reading
	    StringBuffer genome = new StringBuffer(); //We want the genome as an unique line/string
		
	    sc.next(); //Skip the fasta/fa identifier
	    
	    while (sc.hasNext()) {
			genome.append(sc.next().trim().toUpperCase());
		}
		sc.close();
				
		this.genome_array = genome.toString().toCharArray();
		genome = null; //Free memory
		
		this.k = k;
		System.err.println("Loaded genome.");
		this.hash = hashGenome(genome_array, this.k); //Genome hashing	
		System.err.println("Genome hash complete! =)");
	}
	
	/**
	 * @return a hashmap that relates a sequence of bases (key) to a position at the genome
	 */

	private HashMap<String, ArrayList<Integer>> hashGenome(char[] genome, int k) {

		HashMap<String, ArrayList<Integer>> genHash = new HashMap<String, ArrayList<Integer>>();
		
		for(int i = 0; i<genome.length - k; i++){

			String key = new String(genome, i, k); //The key has size k
			
			if(!genHash.containsKey(key))
				genHash.put(key, new ArrayList<Integer>());
				
			genHash.get(key).add(i);	
			
		}
		
		for(String key: genHash.keySet())
			genHash.get(key).trimToSize();
		
		if(DEBUG){
			System.out.print("@Genome");
			for(String key: genHash.keySet()) {
				ArrayList<Integer> positions = genHash.get(key);
				System.out.print(key + ": ");
				for(int j = 0; j < positions.size(); j++)
					System.out.print(positions.get(j) + " ");
				System.out.println();
			}
		}
		
		return genHash;
	}
	
	public char[] getGenome(){
		return genome_array;
	}
	
	public long getLength(){
		return genome_array.length;
	}
	
	public HashMap<String, ArrayList<Integer>> genomeHash(){
		return hash;
	}
	
	/**
	 * @param key - a sequence of bases  
	 * 
	 * @return all the positions at the genome where the key is 
	 */
	public ArrayList<Integer> getPositions(String key){
		
		return hash.get(key);
	}
		
	/**
	 * @return the genomic sequence, for each position found, to be used at the alignment
	 * Effectively this method transforms a sequence of positions into a sequence of genomic segments
	 */
	public ArrayList<SimpleEntry<char[], Integer>> genSeq(char[] genome, int read_length, ArrayList<Integer> positions){

		ArrayList<SimpleEntry<char[], Integer>> gen_seqs = new ArrayList<SimpleEntry<char[], Integer>>();
			
		for( int p: positions){
			if(p + read_length < genome.length) {
				char[] seq = new char[read_length];
				System.arraycopy(genome, p, seq, 0, read_length);
				gen_seqs.add(new SimpleEntry<char[], Integer>(seq, p));
			}
		}			
		
		positions.clear();
		positions = null;
		
		return gen_seqs;
	}
}
