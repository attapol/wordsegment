package word_segmenter;

import java.io.*;
import java.util.Arrays;

import cc.mallet.pipe.*;
import cc.mallet.types.*;
import cc.mallet.classify.*;
import cc.mallet.fst.*;

public class WordSegmenter {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Hello!");
        
		Importer importer = new Importer();
		InstanceList ilist = importer.readFile("./corpus/orchid97_features.bio");
		
		double[] proportions = {0.9, 0.1};
		InstanceList[] splitList = ilist.split(proportions);
		InstanceList trainingSet = splitList[0];
		InstanceList testSet = splitList[1];
		
		CRF crf = new CRF(importer.pipe, null);
		crf.addStatesForThreeQuarterLabelsConnectedAsIn(trainingSet);
		crf.addStartState();
		
		CRFTrainerByThreadedLabelLikelihood crfTrainer= new CRFTrainerByThreadedLabelLikelihood(crf, 32);
		//crfTrainer.addEvaluator(new PerClassAccuracyEvaluator(testSet, "per-class testing"));
		crfTrainer.addEvaluator(new TokenAccuracyEvaluator(testSet, "token testing"));
		crfTrainer.addEvaluator(new SegmentationEvaluator(testSet, "segmentation evaluator testing"));
		
		CRFWriter crfWriter = new CRFWriter("wordsegment_crf.model"){
			@Override
			public boolean precondition(TransducerTrainer tt){
				return tt.getIteration() % 5 == 0;
			}
		};
		crfTrainer.addEvaluator(crfWriter);
		ViterbiWriter viterbiWriter = new ViterbiWriter("wordsegment_crf",testSet, "wordsegmenting with 11-gram");
		crfTrainer.addEvaluator(viterbiWriter);
			
		crfTrainer.train(trainingSet);
		crfTrainer.shutdown();	
	}
	

}
