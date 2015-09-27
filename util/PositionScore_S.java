package util;

/**
 * Implementation of the methods to display the results according to the score obtained for the alignment.
 */
public class PositionScore_S extends PositionScore implements Comparable<PositionScore_S>{

	public PositionScore_S(AlignerResult ar) {
		super(ar);
	}

	public PositionScore_S(PositionScore_P ps) {
		super(ps);
	}
	
	public int compareTo(PositionScore_S other) {
		int r = this.score.compareTo(other.score)*(-1);
		if(r == 0)
			return this.position.compareTo(other.position);
		return r;
	}
	
	public String toString() {
		return super.toString();
	}
}
