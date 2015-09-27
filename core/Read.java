package core;

import java.util.AbstractMap.SimpleEntry;

import util.LLCProperties;
import util.Probabilities;

public class Read {

	private static final boolean DEBUG = Boolean.parseBoolean(LLCProperties.p.getProperty("DEBUG"));
	
	private String id;
	private String seq;
	private String scores; //String with the ASCII characters representing the quality 
	private int k; //Key size
	private int[] qualities; //Array with the quality values 
	private Probabilities[] probabilities; //Probabilities for each of the four bases in each read position

	public Read(String id, String seq, String scores, int k) {
		this.id = id;
		this.seq = seq;
		this.scores = scores;
		this.k = k;
		
		if(seq.length() != scores.length()){
			System.err.println("@Read: Read length = " + seq.length() + "  Qualities length = " + scores.length());
		}
		
		//Convert the ASCII characters in integers
		this.qualities =  new int[scores.length()];
		for (int i = 0; i < scores.length(); i++) {
			this.qualities[i] = (int) scores.charAt(i);
		}	
		
		computeProbabilities();
		
		if(DEBUG){
			System.out.println("@Read: ");
			for(int i = 0; i < k; i++){
				System.out.print("A  " + probabilities[i].getProbA() + "   ");
				System.out.print("C  " + probabilities[i].getProbC() + "   ");
				System.out.print("T  " + probabilities[i].getProbT() + "   ");
				System.out.print("G  " + probabilities[i].getProbG() + "   ");
				System.out.print("N  " + probabilities[i].getProbN() + "   ");
				System.out.println();
			}
		}
	}
	
	 /**
	  * @return the probability of each base being correct in each position of the read
	  */	
	private void computeProbabilities(){
		
		this.probabilities = new Probabilities[seq.length()];

		for(int i = 0; i < seq.length(); i++){
			probabilities[i] = new Probabilities(seq.charAt(i), qualities[i]);
		}
	}
	
	 /**
	  * @return the probability of a base for the r_index position in the read
	  */	
	
	public double getProbability(int r_index, char base){
		
		double prob = 0;
		
		if(base == 'A'){
			prob = probabilities[r_index].getProbA();
		}
		if(base == 'C'){
			prob = probabilities[r_index].getProbC();
		}
		if(base == 'G'){
			prob = probabilities[r_index].getProbG();
		}
		if(base == 'T'){
			prob = probabilities[r_index].getProbT();
		}
		if(base == 'N'){
			prob = probabilities[r_index].getProbN();
		}
		
		return prob;	
	}
	
	public Probabilities getProbabilitities(int r_index){
		
		return probabilities[r_index];
	}
	
	public String getID(){
		
		return id;
	}
	
	public String getSeq(){
		
		return seq;
	}
	
	public String getScores(){
		
		return scores;
	}
	
	public int getLength(){
		
		return seq.length();
	}
	
	/**
	 * Returns a simple key based on the first k characters.
	 */
	public String getKey(){
		
		return seq.substring(0, k);
	}
	
	/**
	 * Returns a key based on k characters that sums up to have the better score
	 */
	public SimpleEntry<String, Integer> bestKey(){
		
//		System.out.println(seq);
		
		String best_key = null;
		int index = 0;
		double score_sum = 0;
		
		for(int i = 0; i< (getLength() - k); i += k){
			
			String key = null;
			for(int j = 0; (j+i) < (getLength() - k); j++){
			 
				key = seq.substring(i+j, i+j+k);
				double sum = 0;
				
				for(int m = 0; m < k; m ++)				
					sum += getProbability(m, key.charAt(m));
							
				if(sum > score_sum){
					score_sum = sum;
					best_key = key;
					index = i+j; 
				}	
			}
		}
		
//		System.out.println(best_key + " " + score_sum + " " + index);
		
		return new SimpleEntry<String, Integer>(best_key, index);
	}
	

	 /**
	  * @return the array of the read's characters to align at the NW matrix   
	  */		
	public char[] getSeqAlign(){
		
		char[] read = seq.toCharArray();
			
		return read;
	}	

	public String toString() {
		
		String key_scores = seq.substring(0, k) + ": ";
		for(int i = 0; i <= k; i++){
			for(char base : seq.substring(0, k).toCharArray())
			key_scores += (" " + getProbability(i, base) + " ");
		}
		return key_scores;
	}
}
