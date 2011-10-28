package word_segmenter;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.io.*;


import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.pipe.tsf.*;
import cc.mallet.types.*;



public class Importer {
	
	Pipe pipe;
	
	public Importer()
	{
		pipe = buildPipe();
	}
	
	public Pipe getPipe(){
		return pipe;
	}

	
	private Pipe buildPipe()
	{
		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
	
        pipeList.add(new Input2CharSequence("UTF-8"));
        pipeList.add(new SimpleTaggerSentence2TokenSequence());

        int[][]conjunctions = new int[2][];
        conjunctions[0] = new int[] {-1};
        conjunctions[1] = new int[] {1};
        pipeList.add(new OffsetConjunctions(conjunctions));
        
        pipeList.add(new TokenSequence2FeatureVectorSequence());
        return new SerialPipes(pipeList);
	}
	
	public InstanceList readFile(String filename) throws IOException{
        
        // Construct a file iterator, starting with the 
        //  specified directories, and recursing through subdirectories.
        // The second argument specifies a FileFilter to use to select
        //  files within a directory.
        // The third argument is a Pattern that is applied to the 
        //   filename to produce a class label. In this case, I've 
        //   asked it to use the last directory name in the path.
       
    	File file = new File(filename);
        Reader input = new FileReader(file);
        Pattern separator = Pattern.compile("^\\s*$");
    	LineGroupIterator iterator = new LineGroupIterator(input, separator, true);

        // Construct a new instance list, passing it the pipe
        //  we want to use to process instances.
        InstanceList instances = new InstanceList(pipe);

        // Now process each instance provided by the iterator.
        instances.addThruPipe(iterator);

        return instances;
    }
    
    /** This class illustrates how to build a simple file filter */
    class TxtFilter implements FileFilter {

        /** Test whether the string representation of the file 
         *   ends with the correct extension. Note that {@ref FileIterator}
         *   will only call this filter if the file is not a directory,
         *   so we do not need to test that it is a file.
         */
        public boolean accept(File file) {
            return file.toString().endsWith(".txt");
        }
    }

}
