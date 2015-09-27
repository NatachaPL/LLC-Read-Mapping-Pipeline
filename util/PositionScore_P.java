package util;

/**
 * Implementation of the methods to display the results according to the position found at the genome.
 */
public class PositionScore_P extends PositionScore implements Comparable<PositionScore_P>{

	public PositionScore_P(AlignerResult ar) {
		super(ar);
	}
	
	public PositionScore_P(PositionScore_S ps) {
		super(ps);
	}
	
	public int compareTo(PositionScore_P other) {
		int r = this.position.compareTo(other.position);
		return r;
	}
	
	public String toString() {
		return super.toString();
	}

}
