package util;
public class Probabilities {
	
	/* A quality value Q is an integer mapping of p (i.e., the	probability that 
	 * the corresponding base call is incorrect). Two different equations have 
	 * been in use. The first is the standard Sanger variant to assess reliability 
	 * of a base call, otherwise known as Phred quality score: Q_{sanger} = -10*log10 p
	 * The Solexa pipeline (i.e., the software delivered with the Illumina Genome Analyzer) 
	 * earlier used a different mapping, encoding the odds p/(1-p) instead of the 
	 * probability p: Q(solexa-prior to v.1.3) = -10*log10(p/(1-p))
	 * Although both mappings are asymptotically identical at higher quality values, 
	 * they differ at lower quality levels (i.e., approximately p > 0.05, or equivalently, 
	 * Q < 13).
	 */ 
	
	private static final boolean DEBUG = Boolean.parseBoolean(LLCProperties.p.getProperty("DEBUG"));
	
	private double prob;
	private double probA;
	private double probC;
	private double probG;
	private double probT;
	private double probN = 0; 
	
	public Probabilities(Character base_called, Integer quality){
		
		this.prob = convertQuality(quality);
		double otherProb = otherProb(prob);
		
		if (base_called == 'A'){
			this.probA = prob;
			setProbC(otherProb);
			setProbT(otherProb);
			setProbG(otherProb);
		}
		
		if (base_called == 'C'){
			this.probC = prob;
			setProbA(otherProb);
			setProbT(otherProb);
			setProbG(otherProb);
		}
		
		if (base_called == 'G'){
			this.probG = prob; 
			setProbA(otherProb);
			setProbC(otherProb);
			setProbT(otherProb);
		}

		if (base_called == 'T'){
			this.probT = prob;
			setProbA(otherProb);
			setProbC(otherProb);
			setProbG(otherProb);
		}
		
		else if (base_called == 'N'){ //It can be any base 
			setProbA(1/4);
			setProbC(1/4);
			setProbT(1/4);
			setProbG(1/4);

		}

	}
	
	private double convertQuality(Integer quality){
		/* Quality is a value between 33 and 126, we need to convert them to values between 0 and 93 (Phred scale) 
		 * and then to probabilistic values using the Phred equation. 
		 */
		
		int Q = quality - 33;
		
		double exp = ((-Q)/10.00);
		
		double score = (1 - Math.pow(10 , exp)); //We want the probability of the base called being CORRECT
		
		if(DEBUG)
			System.out.println("@Probabilities: Phred = "+ Q + " ASCII = " + quality + " Final = " + score);
		
		return score;		
	}
	
	private double otherProb(double Prob_BaseCalled){
		//Assuming the uncalled bases have the same probability of being CORRECT:
		
		double p = (1 - Prob_BaseCalled)/3;
		
		return p;
	}
	
	public double getProbA(){ 
		return probA; 
	}
	
	public void setProbA(double probA){
		this.probA = probA; 
	}

	public double getProbC(){ 
		return probC; 
	}
	
	public void setProbC(double probC){
		this.probC = probC; 
	}
		
	public double getProbG(){ 
		return probG; 
	}
	
	public void setProbG(double probG){
		this.probG = probG; 
	}
	
	public double getProbT(){ 
		return probT; 
	}
	
	public void setProbT(double probT){
		this.probT = probT; 
	}	
	
	public double getProbN(){ 
		return probN; 
	}
	
	public void setProbN(float probN){
		this.probN = probN; 
	}
	
}
