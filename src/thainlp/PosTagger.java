package thainlp;
import java.util.ArrayList;
import java.util.regex.*;
import java.io.*;
import java.util.*;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.pipe.tsf.*;
import cc.mallet.types.*;
import cc.mallet.util.*;
import cc.mallet.fst.*;


public class PosTagger{

	public static void main (String[] args) throws IOException
	{
		PrintWriter out = new PrintWriter("test.out");

		ArrayList<Pipe> pipes = new ArrayList<Pipe>();
		pipes.add(new SimpleTaggerSentence2TokenSequence());
		pipes.add(new TokenSequence2FeatureSequence());
	/*	
		int[][] conjunctions = new int[3][];
        conjunctions[0] = new int[] { -1 };
        conjunctions[1] = new int[] { 1 };
        conjunctions[2] = new int[] { -2, -1 };
        pipes.add(new OffsetConjunctions(conjunctions));
        
	//	pipes.add(new TokenTextCharSuffix("C1=", 1));
	//	pipes.add(new TokenTextCharSuffix("C2=", 2));
	//	pipes.add(new TokenTextCharSuffix("C3=", 3));
	//	pipes.add(new RegexMatches("CAPITALIZED", Pattern.compile("^\\p{Lu}.*")));
	//	pipes.add(new RegexMatches("STARTSNUMBER", Pattern.compile("^[0-9].*")));
	//	pipes.add(new RegexMatches("HYPHENATED", Pattern.compile(".*\\-.*")));
	//	pipes.add(new RegexMatches("DOLLARSIGN", Pattern.compile("\\$.*")));
		pipes.add(new TokenFirstPosition("FIRSTTOKEN"));
		pipes.add(new TokenSequence2FeatureVectorSequence());
		pipes.add(new SequencePrintingPipe(out));

		*/
		
		
		Pipe pipe = new SerialPipes(pipes);

		InstanceList ilist = new InstanceList(pipe);
		ilist.addThruPipe(new LineGroupIterator(
				new BufferedReader(
						new InputStreamReader(
								new FileInputStream("./corpus/orchid97.pos"))), Pattern.compile("^\\s*$"), true));

		double[] proportions = {0.8, 0.2};
		InstanceList[] splitList = ilist.split(proportions);
		InstanceList trainingSet = splitList[0];
		InstanceList testSet = splitList[1];
/*		
		CRF crf = new CRF(pipe, null);
		crf.addStatesForThreeQuarterLabelsConnectedAsIn(trainingSet);		
		CRFTrainerByLabelLikelihood crftrainer = new CRFTrainerByLabelLikelihood(crf);
		crftrainer.setGaussianPriorVariance(10.0);
	*/	
		HMM hmm = new HMM(pipe, null);
		hmm.addStatesForThreeQuarterLabelsConnectedAsIn(trainingSet);		
		HMMTrainerByLikelihood hmmtrainer = new HMMTrainerByLikelihood(hmm);
		
		TransducerTrainer trainer = hmmtrainer;
		trainer.addEvaluator(new PerClassAccuracyEvaluator(testSet, "testing"));
		trainer.addEvaluator(new TokenAccuracyEvaluator(testSet, "testing"));
		trainer.train(trainingSet,2);
		
		

	}

}
