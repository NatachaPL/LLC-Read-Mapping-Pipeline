package util;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import core.Genome;
import core.Read;

/**
 * Display final results considering its score.
 */
public class LLC_Comb extends Combiner{
	
	private final static int top = Integer.parseInt(LLCProperties.p.getProperty("top"));

	public LLC_Comb(Genome genome, Read read, int k) {
		super(genome, read, k);
	}
	
	/**
	 * @param top
	 * 
	 * Results listed according with the alignment score
	 */

	private void top_results(int top){
		
		Set<PositionScore_S> sor_s= new TreeSet<PositionScore_S>();

		if(temp_res.size() > 1){
			
			for(AlignerResult ar : temp_res){
				sor_s.add(new PositionScore_S(ar));
			}
			
			ArrayList<PositionScore_S> sor_array = new ArrayList<PositionScore_S>(sor_s);
			Set<PositionScore_P> sor_p = new TreeSet<PositionScore_P>();
			
			for(int i = 0; sor_array.size() > 0 && i < top; i++){
					ArrayList<PositionScore_P> temp = new ArrayList<PositionScore_P>();
				
					temp.add(new PositionScore_P(sor_array.remove(0)));
					
					while(sor_array.size() > 0 && sor_array.get(0).getScore() >= temp.get(0).getScore()){
						temp.add(new PositionScore_P(sor_array.get(0)));
						sor_array.remove(0);
					}
					sor_p.addAll(temp);
					temp.clear();
					results.addAll(sor_p);
					sor_p.clear();
			}
		}
		else if(temp_res.size() == 1){
			results.add(new PositionScore_P(temp_res.get(0)));
		}
		
		temp_res.clear();
}
protected void results_display() {
		
		top_results(top);
		
		if(DEBUG)
			for(PositionScore p : results){
				System.out.println(p);
			}
		
	}
	
}
