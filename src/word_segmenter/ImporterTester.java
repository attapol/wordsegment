package word_segmenter;

import java.io.IOException;

import cc.mallet.types.InstanceList;

public class ImporterTester {

	public static void main(String[] args) throws IOException {
		
		System.out.println("testing importer for word segmentation CRF!");
		Importer importer = new Importer();
		InstanceList ilist = importer.readFile("./corpus/orchid97_features.bio");
		System.out.println(ilist.getAlphabet().toString());
		System.out.println(ilist.getTargetAlphabet().toString());
	}

}
