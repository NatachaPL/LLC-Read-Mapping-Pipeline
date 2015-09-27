package util;

import java.util.Set;
import java.util.TreeSet;

import core.Genome;
import core.Read;

/**
  *  Display results after the score processing create for GNUMAP.
  */

public class GNUMAP_Comb extends Combiner{
	
	private final float threshold =  Float.parseFloat(LLCProperties.p.getProperty("threshold"));
	Set<PositionScore_P> sorted = new TreeSet<PositionScore_P>();

	public GNUMAP_Comb(Genome genome, Read read, int k) {
		super(genome, read, k);
	}
	
	/**
	 * @param threshold
	 * 
	 * Normalizations of the scores according with GNUMAP
	 */
	private void GNUMAP_normalization(float threshold){
		float sum = 0;
	
		for(AlignerResult ar : temp_res){
			sum += ar.getScore();
		}
		
		for(AlignerResult ar : temp_res){
			if(ar.getScore()/sum >= threshold)
				sorted.add(new PositionScore_P(ar));
		}
		
		temp_res.clear();								
	}

	protected void results_display() {
		
		GNUMAP_normalization(threshold);
		
		results.addAll(sorted);
		
		if(DEBUG)
			for(PositionScore p : results){
				System.out.println(p);
			}
		
	}
}
