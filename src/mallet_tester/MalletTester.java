package mallet_tester;

import java.util.Arrays;

import cc.mallet.pipe.*;
import cc.mallet.types.*;
import cc.mallet.classify.*;

public class MalletTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello!");
		Importer importer = new Importer();
		InstanceList ilist = importer.readDirectory("../../../Dropbox/cs134/review_polarity/txt_sentoken/pos");
		ilist.addAll(importer.readDirectory("../../../Dropbox/cs134/review_polarity/txt_sentoken/neg"));
		
		double[] proportions = {0.5, 0.5};
		InstanceList[] splitList = ilist.split(proportions);
		InstanceList trainingSet = splitList[0];
		InstanceList testSet = splitList[1];
		
		ClassifierTrainer trainer = new MaxEntTrainer();
		Classifier classifier = trainer.train(trainingSet);	
		
		System.out.println ("The training accuracy is "+ classifier.getAccuracy (trainingSet));
		System.out.println ("The testing accuracy is "+ classifier.getAccuracy (testSet));
		
		
	}
	

}
