package SearshEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import javax.swing.JFileChooser;

import safar.basic.morphology.stemmer.factory.StemmerFactory;
import safar.basic.morphology.stemmer.interfaces.IStemmer;
import safar.basic.morphology.stemmer.model.StemmerAnalysis;
import safar.basic.morphology.stemmer.model.WordStemmerAnalysis;

public class MR {

	private Map<String, Map<String, Integer>> map_globale_count = new HashMap<String, Map<String, Integer>>();
	List<String> doc = new ArrayList<String>();
	public MR() {
	}
	public String getPath() {
		// TODO getPath;
		String path = "";
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("choosertitle");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

			System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
			path = chooser.getSelectedFile().getPath();
			System.out.println("getSelectedFile() : " + path);

		} else {
			System.out.println("No Selection ");
		}
		return path;
	}

	public Map<String, Map<String, Integer>> getContentFiles(String path) throws IOException {
		File file = new File(path);
		for (File f : file.listFiles()) {
			List<String> lines = Files.readAllLines(Paths.get(f.getAbsolutePath()));
			String text = String.join(" ", lines);
			text = text.replaceAll("[^\\p{L}\\p{Nd} ]", "");
			map_globale_count.put(f.getName() + "\n\n", Stemming(text + "\n"));
			//map_globale_count.put(f.getName() , Stemming(text+ "\n"));
		}
//			System.out.println(map_global);

		return map_globale_count;
	}


	public static  Map<String, Integer> Stemming(String TextFileContent) throws IOException {
		Map<String, Integer> map = new HashMap<>();
		IStemmer stemmer = StemmerFactory.getKhojaImplementation();
		List<WordStemmerAnalysis> analysis = stemmer.stem(TextFileContent);
		for (WordStemmerAnalysis wordAnalysis : analysis) {
			List<StemmerAnalysis> listOfStems = wordAnalysis.getListStemmerAnalysis();
			for (StemmerAnalysis stem : listOfStems) {
				String stm = stem.getMorpheme();
				if (stm != null) {
					int count = map.containsKey(stm) ? map.get(stm) : 0;
					map.put(stm, count + 1);
					map.keySet().removeAll(Stopwords());
					
				}
				break;
			}
		}
		return map;
	}

	public static float TermeFrequency(String terme, Map<String, Integer> map) {
		float d = 0;
		if (map != null && map.containsKey(terme) != false) {
			d = map.get(terme) / (float) NbrTermes(map);
		}
		return d;
	}

	public static int NbrTermes(Map<String, Integer> map) {
		int nbrtermes = 0;
		Set<Map.Entry<String, Integer>> KeyMap = map.entrySet();
		Iterator<Map.Entry<String, Integer>> itrMap = KeyMap.iterator();
		while (itrMap.hasNext()) {
			Map.Entry<String, Integer> entreeFile = (Map.Entry<String, Integer>) itrMap.next();
			nbrtermes += entreeFile.getValue();
		}
		return nbrtermes;
	}

	public static float InverseDocFrequency(String terme, Map<String, Map<String, Integer>> map_globale_count) {
		Set<Map.Entry<String, Map<String, Integer>>> KeyMap = map_globale_count.entrySet();
		Iterator<Map.Entry<String, Map<String, Integer>>> itrMap = KeyMap.iterator();
		int nbr = 0;
		while (itrMap.hasNext()) {
			Map.Entry<String, Map<String, Integer>> entreeMap = (Map.Entry<String, Map<String, Integer>>) itrMap.next();
			Map<String, Integer> value = entreeMap.getValue();
			if (value.containsKey(terme) == true) {
				nbr++;
			}
		}
		if (nbr == 0)
			nbr = 1;
		return (float) Math.log(map_globale_count.size() / nbr);
	}

	public static float TF_IDF(String terme, Map<String, Integer> Corpus_doc,
			Map<String, Map<String, Integer>> corpus_global) {
		return TermeFrequency(terme, Corpus_doc) * InverseDocFrequency(terme, corpus_global);
	}

	public static List<String> Stopwords() throws IOException {
		String sCurrentLine;
		List<String> list = new ArrayList<String>();
		FileReader fr = new FileReader("F:\\MIDVI\\S3\\TextMining\\autre\\stopwords.txt");
		BufferedReader br = new BufferedReader(fr);
		while ((sCurrentLine = br.readLine()) != null) {
			list.add(sCurrentLine);
		}
		return list;
	}
	public static float tfidf = 0;
	public static Map<String,Float> TF_IDF ;
	public static Map<String,Map<String,Float>> TF_IDF_Corpus ;
	
	public Map<String,Float> GetTF_IDF_Query(Map<String, Map<String, Integer>> corpus_global_count, Map<String, Integer> Query) {
		
//		corpus_global_count.forEach((doc, v) -> {
			TF_IDF = new HashMap<>();
			Query.forEach((term, occ) -> {
						tfidf =TF_IDF(term,Query,corpus_global_count);
						TF_IDF.put(term,tfidf);	
			});
//		});
		return TF_IDF;
	}
	
	public Map<String,Map<String,Float>> GetTF_IDF_Corpus(Map<String, Map<String, Integer>> corpus_global_count, Map<String, Integer> Query) {
		TF_IDF_Corpus = new HashMap<String, Map<String,Float>>();
		corpus_global_count.forEach((doc, v) -> {
			TF_IDF = new HashMap<>();
			Query.forEach((term, occ) -> {
					if(v.containsKey(term)) {
						tfidf =TF_IDF(term,v,corpus_global_count);
						TF_IDF.put(term,tfidf);
						TF_IDF_Corpus.put(doc, TF_IDF);
					}
			});
		});
		return TF_IDF_Corpus;
	}
	
	public static float tfidfVal;
	public static float ProduitScalaire(Map<String, Float>tfidf_Query, Map<String, Float>tfidf_doc) {
		tfidfVal = 0;
		tfidf_Query.forEach((term,tfidfV)->{
			if(tfidf_doc.containsKey(term)) {
				tfidfVal += (tfidfV*tfidf_doc.get(term));
			}
		});
		return tfidfVal;
	}
	
	static float tmp=0, tmp1=0;
	public static float Normalisation(Map<String, Float>tfidf_Query, Map<String, Float>tfidf_doc) {	
		tfidf_Query.forEach((term,tfidfV)->{
			tmp +=Math.pow(tfidfV, 2);
		});
		tfidf_doc.forEach((term1,tfidfV1)->{
			tmp1 +=Math.pow(tfidfV1, 2);
		});
		return (float) (Math.sqrt(tmp)*Math.sqrt(tmp1));
	}
	static float PS=0, Norm=0;
	static Map<String, Float>cos;
	public static Map<String, Float> Similarity(Map<String, Map<String, Float>>corpus_globTFIDF, Map<String, Float>TFIDF_Query) {
		cos = new HashMap<String, Float>();
		corpus_globTFIDF.forEach((doc,v)->{
		
//			PS = ProduitScalaire(TFIDF_Query, v);
//			Norm = Normalisation(TFIDF_Query, v);
			PS = ProduitScalaire(v,TFIDF_Query);
			Norm = Normalisation(v,TFIDF_Query);
			cos.put(doc, PS/Norm);
		});
		return cos;
	}
	
    static String nomdoc="";
	static float maxSim=0;
	public static String maxSimi(Map<String, Float> Similarity) {
		
		Similarity.forEach((doc,simval)->{
			if(simval > maxSim ) {
				maxSim = simval;
			    nomdoc = doc;
			}	
		});
		System.out.println("doc: "+ nomdoc+"max:  " +maxSim );
		return nomdoc;
	}
	public static void main(String[] args) throws IOException {
		MR mr = new MR();
		String path = mr.getPath();
		System.out.println(mr.getContentFiles(path));
		// String terme ="";
		Map<String, Integer>q= new HashMap<String, Integer>();
		q.put("Ù…Ø±Ø±",1);
		q.put("Ø¨Ù†ÙŠ",3);
		q.put("Ø±Ø¶ÙŠ",2);
		mr.Stemming("Ø±Ø¶ÙŠ ,Ø¨Ù†ÙŠ Ù…Ø±Ø±");	System.out.println(mr.GetTF_IDF_Corpus(mr.getContentFiles(path),mr.Stemming("Ø±Ø¶ÙŠ ,Ø¨Ù†ÙŠ Ù…Ø±Ø±")));
		System.out.println("\n\n TF IDF Query :");
		System.out.println(mr.GetTF_IDF_Query(mr.getContentFiles(path),MR.Stemming("وتعين السلطة التنظيمية ")));
		Map<String, Map<String, Float>> Tf_idfTermsInDocs =mr.GetTF_IDF_Corpus(mr.getContentFiles(path),MR.Stemming("وتعين السلطة التنظيمية "));
		Map<String, Float> tfidfQuery= mr.GetTF_IDF_Query(mr.getContentFiles(path),MR.Stemming("وتعين السلطة التنظيمية "));
		System.out.println("\n Cos Query for each document :");
		System.out.println(Similarity(Tf_idfTermsInDocs,tfidfQuery));
		System.out.println("------------------------------");
		maxSimi(Similarity(Tf_idfTermsInDocs,tfidfQuery));
										
	}
}
