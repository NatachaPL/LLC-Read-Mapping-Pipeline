package core;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import util.Combiner;
import util.LLCProperties;
import util.PositionScore;

public class ReadProcessing implements Runnable{

	private static final boolean DEBUG = Boolean.parseBoolean(LLCProperties.p.getProperty("DEBUG"));
	
	private List<Read> someReads;
	private Genome genome;
	private String exploder;
	private String aligner;
	private String comb_type;
	private FileWriter output;
	private int k;
	

	public ReadProcessing(List<Read> someReads, Genome genome, FileWriter output, String exploder, String aligner, String comb_type, int k) {
		this.someReads = someReads;
		this.genome = genome;
		this.exploder = exploder;
		this.aligner = aligner;
		this.comb_type = comb_type;
		this.k = k;
		this.output = output;
	}
	
	public void run() {

		//*Alignment for each read of the list someReads*
		while(someReads.size() > 0) {
			try {
			
//			System.err.println("Slave " + Thread.currentThread().getName() + ": " + someReads.size() + " reads in queue. Combiner...");
			Combiner process = getCombiner(comb_type, genome, someReads.remove(0), k);
//			System.err.println("Slave " + Thread.currentThread().getName() + ": " + someReads.size() + " reads in queue. Combine...");

			process.combine(exploder, aligner);

//			System.err.println("Slave " + Thread.currentThread().getName() + ": " + someReads.size() + " reads in queue. Storing Results...");

			//Add alignment result to output
			StringBuffer sb = new StringBuffer();
			for(PositionScore r : process.getResults()){
				sb.append(r.toString());
				sb.append(System.getProperty("line.separator"));

				if(DEBUG)
					System.out.println("@ReadProcessing: " + sb.toString());
			}

//			System.err.println("Slave " + Thread.currentThread().getName() + ": " + someReads.size() + " reads in queue. Flushing Results...");

			try {
				output.write(sb.toString());
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(DEBUG)
				System.err.println("Slave Thread (" + Thread.currentThread().getName() + "): Processed 1 read, there are " + this.someReads.size() + " in queue.");

//			System.err.println("Slave " + Thread.currentThread().getName() + ": " + someReads.size() + " reads in queue. Next Iteration...");

//			if(someReads.isEmpty())
//				System.err.println("Slave " + Thread.currentThread().getName() + ": Finished set!!");

		} catch (Exception e) {
			System.err.println("Slave " + Thread.currentThread().getName() + ": Generated Excetion: " + e.getClass().getSimpleName());
			e.printStackTrace();
		}
		}
	}

	/**
	 * 
	 * @param name
	 * @param genome
	 * @param read
	 * @param k
	 * 
	 * The results_display at Combiner depends of which comb_type was choose
	 */
	private Combiner getCombiner(String name, Genome genome, Read read, int k){
		try {
			@SuppressWarnings("unchecked")
			Constructor<Combiner> creator = (Constructor<Combiner>)
					Class.forName(name).getConstructors()[0];
			Object[] args = new Object[3];
			args[0] = genome ;				
			args[1] = read;
			args[2] = new Integer(k);
			Combiner instance = creator.newInstance(args);
			return instance;
		} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;				
		}
	}
}
