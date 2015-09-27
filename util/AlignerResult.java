ackage util;

/**
 * Each alignment result is an object characterized by the read ID, the alignment obtain, its position
 * at the genome and its score.
 */

public class AlignerResult{

	private String readID;
	private String align;
	private Integer position;
	private Float score;
	
	public AlignerResult(String readID, String align, Integer position, Float score) {
		this.readID = readID;
		this.align = align;
		this.position = position;
		this.score = score;
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
