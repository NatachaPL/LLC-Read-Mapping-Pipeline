package util;

/**
 * This object has the same parameters of AlignerResult, but allows to display the results according to 
 * the position found at the genome or the score obtained for the alignment.
 */
public abstract class PositionScore {
	
	protected String readID;
	protected String align;
	protected Integer position;
	protected Float score;
	
	public PositionScore(AlignerResult ar) {
		this.readID = ar.getReadID();
		this.align = ar.getAlign();
		this.position = ar.getPosition();
		this.score = ar.getScore();
	}
	
	public PositionScore(PositionScore ps) {
		this.readID = ps.getReadID();
		this.align = ps.getAlign();
		this.position = ps.getPosition();
		this.score = ps.getScore();
	}
	
	public String getReadID() {
		return readID;
	}

	public String getAlign() {
		return align;
	}
	
	public Integer getPosition() {
		return position;
	}
	
	public Float getScore() {
		return score;
	}
	
	public String toString(){
		return getReadID() + "\t" + getAlign() + "\t" + getPosition() + "\t" + getScore();
		
	}
}
