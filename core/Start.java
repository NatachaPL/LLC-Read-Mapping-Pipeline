package core;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import util.*;

public class Start {
	
	//For the thread pool
	public final static int N_SLAVES = Runtime.getRuntime().availableProcessors() -1; //Number of threads created
		
	public final static int N_READS = 5 * N_SLAVES; //Number of reads mapped per thread pool

	public static void main(String[] args) throws IOException{
		

		//Start runtime
		long startTime = System.currentTimeMillis();

		//Configuration properties
		Properties prop = new Properties();

		InputStream input = null;
		try {

			input = new FileInputStream(args[2]);

			// Load a properties file
			prop.load(input);

			//Initialize LLCProperties	 
			LLCProperties.init(prop);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					input = null; //Free memory
				}
			}
		}

		//Key size
		int k = Integer.parseInt(prop.getProperty("k"));
		//Exploder identifier
		String exploder = prop.getProperty("exploder");
		//Aligner identifier
		String aligner = prop.getProperty("aligner");
		//Combiner identifier
		String comb_type = prop.getProperty("comb_type");
		//Output directory
		String output_dir = prop.getProperty("output_dir");

		boolean DEBUG = Boolean.parseBoolean(prop.getProperty("DEBUG"));

		//Create Output file
		String name_exp = exploder.split("\\.")[1];
		String name_alg = aligner.split("\\.")[1];
		String name_comb = comb_type.split("\\.")[1];

		//Output output = new Output("Output_" + name_comb + "_" + name_exp + "_" + name_alg);
		FileWriter[] outputs = new FileWriter[N_SLAVES];
		for(int i = 0; i < outputs.length; i++)
			outputs[i] = new FileWriter(output_dir + "Output_" + name_comb + "_" + name_exp + "_" + name_alg + "_" + i + ".txt");
		
		
		//* Genome *
		Genome genome = new Genome(args[0], k); //Genome file reading and hashing

		System.out.println("Genome hashing completed! (time since start: " + (System.currentTimeMillis() - startTime) + " ms).");

		//* Read *		
		ArrayList<List<Read>> someReads_set = new ArrayList<List<Read>>(N_SLAVES);

		for(int i = 0; i < N_SLAVES; i++)
			someReads_set.add(new LinkedList<Read>());
			
		try(Scanner sc = new Scanner(new FileInputStream(args[1]))){
				sc.useDelimiter(System.getProperty("line.separator"));
				int counter = 0;
				while (sc.hasNext()) {
					counter = 0;
					
					ExecutorService ex = Executors.newFixedThreadPool(N_SLAVES);

					System.err.println("Starting to get new reads.");
										
					while(counter < N_READS && sc.hasNext()){
								
						String id = sc.next().trim().toUpperCase(); //Read header
						String seq = sc.next().trim().toUpperCase(); //Read base sequence
						sc.next(); //Jump every third line (not necessary)
						String scores = sc.next().trim().toUpperCase(); //Read quality scores
		
						if(DEBUG){
							System.out.println("@Start: \nID = " + id + "\nRead = " + seq + "\nQualities = " + scores);
							System.out.println("Read length = " + seq.length() + "  Qualities length = " + scores.length());
						}
						
						someReads_set.get(counter % N_SLAVES).add(new Read(id, seq, scores, k));	
						counter++;
					}		
		
					System.out.println("Reads loaded (total: " + counter + ").");
					
					System.err.println("Starting Threads...");
					
					for(int i = 0; i < N_SLAVES; i++) {
						ex.execute(new ReadProcessing(someReads_set.get(i), genome, outputs[i], exploder, aligner, comb_type, k));			
					}
					ex.shutdown();		
//					System.err.println("Waiting for threads to complete...");
				
					try {

						int t = 1;
						while(!ex.awaitTermination(10, TimeUnit.MINUTES)) {
							System.out.println("Waited for " + t*10 + " minutes, not completed yet...");
							t++;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					

					System.out.println("All threads completed... moving forward. (time since start: " + (System.currentTimeMillis() - startTime) + " ms).");				
				}
		
		}
		
		// Get runtime in seconds
		double delta = (System.currentTimeMillis() - startTime)/(double) Math.pow(10,3); 

		System.out.println("Running time: " + delta + " seconds <=> " + (double) delta/60 + " minutes.");
	}
}


