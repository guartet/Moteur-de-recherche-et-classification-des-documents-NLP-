package Clustering;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import FeatureEngineering.CHI2;
public class KmeansModel {
	public static final File Dir = new File("C:\\Users\\ASUS\\Downloads\\Document");

	public static Map<String, Map<String, Map<String, Integer>>> getCorpus() throws IOException {
		Map<String, Map<String, Map<String, Integer>>> corpus = CHI2.CreateCorpus(Dir);
		return corpus;
	}

	public static Map<Integer, Integer> clusters = new HashMap<>();

	static int i;
	public static void initClusters(List<Map<String, Integer>> docs, int k) {
		Random r = new Random();
		Set<Integer> rands = new HashSet<>();
		docs.forEach(d -> clusters.put(d.hashCode(), -1));
		i = 0;
		docs.forEach(d -> {
			if(++i > k) return;
			int rand = r.nextInt(k);
			while(rands.contains(rand))
				rand = r.nextInt();
			
			clusters.put(d.hashCode(), rand);
		});
	}
	public static void main(String[] args) throws IOException {
		Map<String, Map<String, Map<String, Integer>>> corpus = getCorpus();
		System.out.println(corpus);
		List<Map<String, Integer>> docs = new ArrayList<>();
		corpus.forEach((c, d) -> d.forEach((name, doc)-> docs.add(doc)));
		initClusters(docs, 3);
		System.out.println(clusters.values());
	}

}
