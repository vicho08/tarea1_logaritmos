/*
 * Top 5 6-grams
 * 0000029523	es una población y comuna francesa
 * 0000017015	y comuna francesa en la región
 * 0000016932	comuna francesa en la región de
 * 0000016890	población y comuna francesa en la
 * 0000016889	una población y comuna francesa en

*/


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.rmi.AlreadyBoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * Main method to run an external merge sort.
 * Batches of lines from the input are read into memory and sorted.
 * Batches are written to individual files.
 * A merge-sort is applied over the individual files, creating the final sorted output.
 * The bigger the batches (in general), the faster the sort.
 * But get too close to the Heap limit and you'll run into trouble.
 * 
 * @author Aidan
 */
public class ExternalMergeSort {
	
	public static String DEFAULT_TEMP_DIR = "tmp";
	public static String DEFAULT_TEMP_SUBDIR_PREFIX = "t";
	
	public static String BATCH_FILE_NAME_PREFIX = "batch-";
	public static String BATCH_FILE_NAME_SUFFIX = ".txt";
	public static String BATCH_FILE_GZIPPED_NAME_SUFFIX = ".gz";
	
	public static boolean GZIP_BATCHES = true;
	
	public static int TICKS = 1000000;
	
	public static void main(String args[]) throws IOException, ClassNotFoundException, AlreadyBoundException, InstantiationException, IllegalAccessException {
		// open the input
		String in = args[1];
		
		// open the output
		String out = "output.txt";
			
		// get the batch size
		int batchSize = (int) Math.pow(10, 5);
		System.err.println("Using a batch size of "+batchSize+" for the sort");
		
		// get the temporary directory
		// to store batch files in (if given)
		String tmpParent = DEFAULT_TEMP_DIR;
		
		//parse attribute index to int
		int ind_attr = Integer.parseInt(args[0]);
		
		// call the method that does the hard work
		// time it as well!
		long b4 = System.currentTimeMillis();
		externalMergeSort(in, out, batchSize, tmpParent, ind_attr);
		
		System.err.println("\nOverall Runtime: "+(System.currentTimeMillis()-b4)/1000+" seconds");
	}
	
	public static void externalMergeSort(String in, String out, 
			int batchSize, String tmpFolderParent, int ind_attr) throws IOException{
		// open a random sub-folder for batches so 
		// that two parallel sorts are unlikely to overwrite
		// each other
		String tmpFolder = createRandomFreshSubdir(tmpFolderParent);
		
		// open the input
		InputStream is = new FileInputStream(in);
		BufferedReader input = new BufferedReader(new InputStreamReader(is,"utf-8"));
		System.err.println("Reading from "+in);
		
		// open the output
		//ByteArrayOutputStream os_not_clean = new ByteArrayOutputStream();
		//os_not_clean.reset();
		OutputStream os = new FileOutputStream(out);
		PrintWriter output = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(os),"utf-8"));
		System.err.println("Writing to "+out+"\n");
		
		// batch the data into small sorted files and
		// return the batch file names
		long b4 = System.currentTimeMillis();
		ArrayList<String> batches = writeSortedBatches(input, tmpFolder, batchSize, ind_attr);
		System.err.println("Batch Runtime: "+(System.currentTimeMillis()-b4)/1000+" seconds");
		input.close();
		
		// merge-sort the batches into the output file
		b4 = System.currentTimeMillis();
		mergeSortedBatches(batches, output, ind_attr);
		System.err.println("Merge Runtime: "+(System.currentTimeMillis()-b4)/1000+" seconds");
		output.close();
	}
	
	/**
	 * Break the input into small sorted files containing
	 * a maximum of batchSize lines each.
	 * 
	 * @param in Reader over input file
	 * @param tmpFolder A folder in which batches can be written
	 * @param batchSize Maximum size for a batch
	 * @param reverseOrder If sorting should be in descending order
	 * @return
	 * @throws IOException
	 */
	private static ArrayList<String> writeSortedBatches(BufferedReader in,
			String tmpFolder, int batchSize, int ind_attr) throws IOException {
		// this stores the file names of the batches produced ...
		ArrayList<String> batchNames = new ArrayList<String>();
		int batchId = 0;

		// this stores the lines of the file for sorting
		ArrayList<String> lines = new ArrayList<String>(batchSize);
		
		boolean done = false;
		while(!done){
			String line = in.readLine();
			if(line!=null){
				lines.add(line);
			} else {
				done = true;
			}
			
			// if the batch is full or its the last line
			// of the input, write the batch to file
			if(lines.size()==batchSize || (done && !lines.isEmpty())){
				batchId ++;
				
				order_lines(lines, ind_attr);
				
				// we will return the names of the batch files later
				batchNames.add(writeBatch(lines, tmpFolder, batchId));
				lines.clear();
			}
		}
		
		return batchNames;
	}
	
	public static void order_lines (ArrayList<String> lines, int ind) {
		//ArrayList<String> lines_aux = new ArrayList<String>(lines.size());
		QuickSort q = new QuickSort();
		q.sort(lines, ind);
	}
	
	/**
	 * Opens a batch file and writes all the lines to it.
	 * @param lines
	 * @param tmpFolder
	 * @param batchId
	 * @return The filename of the batch.
	 * @throws IOException
	 */
	private static String writeBatch(Collection<String> lines, String tmpFolder, int batchId) throws IOException{
		String batchFileName = getBatchFileName(tmpFolder, batchId);
		
		System.err.println("Opening batch at "+batchFileName+" to write "+lines.size()+" lines");
		System.err.println(MemStats.getMemStats());
		PrintWriter batch = openBatchFileForWriting(batchFileName);
		
		for(String l:lines)
			batch.println(l);
		
		batch.close();
		System.err.println("... closing batch.\n");
		return batchFileName;
	}
	
	/**
	 * Merge sorted batches into one file.
	 * 
	 * @param batches The filenames of the batches to merge
	 * @param out The output to write the merged data
	 * @param reverseOrder If the ordering should be descending
	 * @throws IOException
	 */
	private static void mergeSortedBatches(ArrayList<String> batches,
			PrintWriter out, int ind_attr) throws IOException {
		// inputs for all the batches
		BufferedReader[] batchReaders = new BufferedReader[batches.size()];
			
			TreeSet<LineAndId> heap= new TreeSet<LineAndId>();
			//open each batch
			for(int i=0; i<batchReaders.length; i++) {
				batchReaders[i]= openBatchFileForReading(batches.get(i));
				String line= batchReaders[i].readLine();
					if (line!=null) {
						LineAndId lai= new LineAndId(line, i, ind_attr);
						heap.add(lai);
					}
			}
			
			while (!heap.isEmpty()){
				LineAndId top= heap.pollFirst();
				int fileId= top.getNumber();
				out.print(top.getString());
				out.print("\n");
				String line= batchReaders[fileId].readLine();
				if(line!=null){
					LineAndId lai2= new LineAndId(line, fileId, ind_attr);
					heap.add(lai2);
				}else {
					batchReaders[fileId].close();
				}
			}
			 
		}
		
		
		// Here you need to merge sort the files
		// from "batches" and write them to "out".
		// If "reverseOrder" is true, you should
		// do the merge sort in descending order.
		
		
	

	/**
	 * Get a batch file name with the given directory and batch number
	 * 
	 * @param dir
	 * @param batchNumber
	 * @return
	 */
	private static String getBatchFileName(String dir, int batchNumber){
		String fileName = dir+"/"+BATCH_FILE_NAME_PREFIX+batchNumber+BATCH_FILE_NAME_SUFFIX;
		if(GZIP_BATCHES)
			fileName = fileName+BATCH_FILE_GZIPPED_NAME_SUFFIX;
		return fileName;
	}
	
	/**
	 * Opens a PrintWriter for the batch filename
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static PrintWriter openBatchFileForWriting(String fileName) throws IOException{
		OutputStream os = new FileOutputStream(fileName);
		if(GZIP_BATCHES){
			os = new GZIPOutputStream(os);
		}
		return new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(os),"utf-8"));
	}
	
	/**
	 * Opens a BufferedReader to read from a batch.
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static BufferedReader openBatchFileForReading(String fileName) throws IOException{
		InputStream os = new FileInputStream(fileName);
		if(GZIP_BATCHES){
			os = new GZIPInputStream(os);
		}
		return new BufferedReader(new InputStreamReader(os,"utf-8"));
	}

	/**
	 * Creates a random sub-directory that doesn't already exist
	 * 
	 * Makes sure different runs don't overwrite each other
	 * 
	 * @param inDir Parent directory
	 * @return
	 */
	public static final String createRandomFreshSubdir(String inDir){
		boolean done = false;
		String subDir = null;
		
		while(!done){
			Random r = new Random();
			int rand = Math.abs(r.nextInt());
			subDir = inDir+"/"+DEFAULT_TEMP_SUBDIR_PREFIX+rand+"/";
			File subDirF = new File(subDir);
			if(!subDirF.exists()){
				subDirF.mkdirs();
				done = true;
			}
		}
		return subDir;
	}
}
