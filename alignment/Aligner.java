package alignment;

import core.Read;
import util.LLCProperties;

public abstract class Aligner {
	
	protected static final boolean DEBUG = Boolean.parseBoolean(LLCProperties.p.getProperty("DEBUG"));

	protected Read read;
	
	protected char[] g_seq; //Genomic sequence to align
	protected float[][] mD;
	protected float mScore;
	protected String mAlignmentGen = "";
	protected String mAlignmentRead = ""; //This will go to the output file as a result of the alignment

	public Aligner(char[] g_seq, Read read) {
			
		this.read = read;
		
		//Create matrix
		this.g_seq = g_seq; //From the genome we just want a sequence to align
		if(DEBUG) {
			System.out.println("@Aligner");
			System.out.println("g_seq is " + (this.g_seq != null ? "not":"") + " null");
			System.out.println("r_seq is " + (read.getSeqAlign() != null ? "not":"") + " null");
		}
		mD = new float[g_seq.length + 1][read.getLength() + 1]; 
	}
	
	//Initialize matrix
	public void initialize() {

		for(int i = 0; i <= g_seq.length; i++){
			for(int j = 0; j <= read.getLength(); j++){
				if(i == 0){
					mD[i][j] = -j; //Fill first line of the matrix
				}
				else if(j == 0){
					mD[i][j] = -i; //Fill first column of the matrix
				}
				else{
					mD[i][j] = 0;
				}
			}
		}
		
		fillMatrix();
		
		//Backtrack
		computeAlignment();
		
		if(DEBUG)
			printScoreAndAlignments();
	}
	
	protected abstract float weight(int i, int j); //Value returned when two bases are aligned
	
	protected abstract int gap(); //Value returned when a gap between the sequences is found
	
	protected void fillMatrix(){
		for(int i = 1; i <= g_seq.length; i++){
			for(int j = 1; j <= read.getLength(); j++){
				float scoreDiag = mD[i-1][j-1] + weight(i,j); //Match or Mismatch
				float scoreUp = mD[i-1][j] + gap(); //Deletion
				float scoreLeft = mD[i][j-1] + gap(); //Insertion
				mD[i][j] = Math.max(Math.max(scoreDiag, scoreUp), scoreLeft);
			}
		}
	}
	
	protected void computeAlignment(){
		if(Aligner.DEBUG) {
			System.out.println("Before:");
			System.out.println("Genomic sequence = " + new String(g_seq));
			System.out.println("Read sequence = " + new String(read.getSeqAlign()));
		}
			
		int i = g_seq.length;
		int j = read.getLength();
		char[] r_seq = read.getSeqAlign();
		mScore = mD[i][j];
		while(i > 0 && j > 0){
//			System.out.println(g_seq[i-1] + "  " + r_seq[j-1]);
			if(mD[i][j] == (mD[i-1][j-1] + weight(i,j))){
				mAlignmentGen += g_seq[i-1];
				mAlignmentRead += r_seq[j-1];
				i--;
				j--;
				continue;
			}
			else if(mD[i][j] == (mD[i][j-1] - gap())){
				mAlignmentGen += "-";
				mAlignmentRead += r_seq[j-1];
				j--;
				continue;
			}
			else{
				mAlignmentGen += g_seq[i-1];
				mAlignmentRead += "-";
				i--;
				continue;
			}
		}
		mAlignmentGen = new StringBuffer(mAlignmentGen).reverse().toString();
		mAlignmentRead = new StringBuffer(mAlignmentRead).reverse().toString();
	}
	
	// Return the alignment score
	public float getScore(){
		
		return mScore;
	}
