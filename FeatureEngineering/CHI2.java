package FeatureEngineering;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import SearshEngine.MR;
public class CHI2 { 
	public static Map<String, Map<String, Map<String, Integer>>> corpus;
	public static final File Dir = new File("C:\\Users\\ASUS\\Downloads\\Document");
	
	public static int nbr = 0;
	public static int nbrDoc = 0;
	public static int nbrDocWT = 0;
	
	public static int ClassSize = 0;
	
	public static double maxProb = 0;
	public static double Prob = 0;
	
	//----------------------------------------
	// Corpus globale de toute les classes 
	//----------------------------------------
	public static Map<String, Map<String, Map<String, Integer>>> CreateCorpus(File folder) throws IOException {
		corpus = new HashMap<>();
		
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				String className = "\n"+fileEntry.getName()+"\n";
				File file = fileEntry.getAbsoluteFile();
				Map<String, Map<String, Integer>> mapDocTerm = new HashMap<>();
				for (File docFile : file.listFiles()) {
					Map<String, Integer> mapTermOcc = new HashMap<>();
					String docName = "\n"+docFile.getName();
					List<String> lines = Files.readAllLines(Paths.get(docFile.getAbsolutePath()),StandardCharsets.UTF_8);
					String text = String.join(" ", lines);
					text = text.replaceAll("[^\\p{L}\\p{Nd} ]", "").toLowerCase();
					
					mapTermOcc=MR.Stemming(text);
					
					mapDocTerm.put(docName, mapTermOcc);
				}
				corpus.put(className, mapDocTerm);
			}
		}
		return corpus;
	}
	
	
	public static Map<String,Map<String, Integer>> CreateDoc1(File folder) throws IOException {
		
		Map<String, Map<String, Integer>> mapDocTerm = new HashMap<>();
		//File file = folder.getAbsoluteFile();
		for (File docFile : folder.listFiles()) {
			Map<String, Integer> mapTermOcc = new HashMap<>();
			String docName = "\n"+docFile.getName();
			List<String> lines = Files.readAllLines(Paths.get(docFile.getAbsolutePath()),StandardCharsets.UTF_8);
			String text = String.join(" ", lines);
			text = text.replaceAll("[^\\p{L}\\p{Nd} ]", "").toLowerCase();
			
			mapTermOcc=MR.Stemming(text);
			
			mapDocTerm.put(docName, mapTermOcc);
					
		}
		
		return mapDocTerm;
	}
	
	
	
	public static Map<String, Integer> CreateDoc(File folder) throws IOException {
		corpus = new HashMap<>();
		Map<String, Integer> mapTermOcc=new HashMap<>();
		
				
					String text=folder.getName();
					
						
						
						List<String> lines = Files.readAllLines(Paths.get(folder.getAbsolutePath()),StandardCharsets.UTF_8);
						String text1 = String.join(" ", lines);
					text1 = text1.replaceAll("[^\\p{L}\\p{Nd} ]", "").toLowerCase();
					
					mapTermOcc=MR.Stemming(text1);
					
					
		
		return mapTermOcc;
	}
	
	
	//----------------------------------------
	// N:Nombre of Doc in the class
	//----------------------------------------
	public static int nbrDocInClass(String Class, Map<String, Map<String, Map<String, Integer>>> Corpus)throws IOException {
		Corpus.forEach((Clas, v) -> {
			if (Class.equals(Clas)) {
				nbr = v.size();
			}
		});
		return nbr;
	}
	//----------------------------------------
	// @ A: Nombre des documents qui contient le Terme dans la class:
	//----------------------------------------
	public static int calculeA(String term, String Class,Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		nbrDoc = 0;
		Corpus.forEach((Clas, v) -> {
			v.forEach((Doc, vv) -> {
				vv.forEach((word, occ) -> {
					if (Class.equals(Clas)) {
						if (term.equals(word)) {
							nbrDoc++;
						}
					}
				});

			});
		});
		return nbrDoc;
	}
	//----------------------------------------
	//  @ B: Nombre des documents qui ne contient pas le Terme dans la class
	//----------------------------------------
	public static int calculeB(String term, String Class,
			Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		nbrDoc = 0;
		nbrDocWT = 0;
		ClassSize = 0;
		nbrDocWT = calculeA(term, Class, Corpus);
		Corpus.forEach((Clas, v) -> {
			if (Clas.equals(Class)) {
				ClassSize = v.size();
			}
		});
		nbrDoc = ClassSize - nbrDocWT;
		return nbrDoc;
	}
	//----------------------------------------
	// @ C: Nombre des documets qui contient le terme en dehors de la class
	//----------------------------------------
	public static int calculeC(String term, String Class,
			Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		nbrDoc = 0;
		Corpus.forEach((Clas, v) -> {
			v.forEach((Doc, vv) -> {

				if (!Class.equals(Clas)) {
					if (vv.containsKey(term)) {
						nbrDoc++;
					}
				}

			});
		});
		return nbrDoc;
	}
	//---------------------------------------------
	// @ D: Nombre des documents qui ne contient pas le terme en dehors de la classe
	//---------------------------------------------
	public static int calculeD(String term, String Class,
			Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		nbrDoc = 0;
		Corpus.forEach((Clas, v) -> {
			v.forEach((Doc, vv) -> {

				if (!Class.equals(Clas)) {
					if (!vv.containsKey(term)) {
						nbrDoc++;
					}
				}

			});
		});
		return nbrDoc;
	}
	//---------------------------------------------
	// Probabilite khi2 de chaque terme dans la classe
	//---------------------------------------------
	public static double ProbKhiDeux(String term, String Class, Map<String, Map<String, Map<String, Integer>>> Corpus)throws IOException {
		
		int N = nbrDocInClass(Class, Corpus);
		int A = calculeA(term, Class, Corpus);
		int B = calculeB(term, Class, Corpus);
		int C = calculeC(term, Class, Corpus);
		int D = calculeD(term, Class, Corpus);
		double prob = 0;
//		System.out.println(Class);
//		System.out.println("N =" + N);
//		System.out.println("A =" + A);
//		System.out.println("B =" + B);
//		System.out.println("C =" + C);
//		System.out.println("D =" + D);
		prob = N * Math.pow(A * D - B * C, 2) / ((A + B) * (A + C) * (D + B) * (D + C));
		return prob;
	}
	//---------------------------------------------
	// khi2 d'un terme dans toute les classes
	//---------------------------------------------
	public static Map<String, Map<String, Double>> Khi2TermInClasses(String term,Map<String, Map<String, Map<String, Integer>>> Corpus)throws IOException {
		Map<String, Map<String, Double>> map = new HashMap<>();
		Map<String, Double> map2 = new HashMap<>();
		Prob = 0;
		Corpus.forEach((Clas, v) -> {
			try {
				Prob = ProbKhiDeux(term, Clas, Corpus);
			} catch (IOException e) {
				e.printStackTrace();
			}
			map2.put(Clas, Prob);
			map.put(term, map2);
		});
		return map;
	}
	//------------------------------------------
	// MaxKhi2 d'un term dans toute les classe
	//-----------------------------------------
	public static double MaxKhi2(String term, Map<String, Map<String, Map<String, Integer>>> Corpus)throws IOException {
		Map<String, Map<String, Double>> map = Khi2TermInClasses(term, Corpus);
		maxProb = 0;
		map.forEach((word, v) -> {
			v.forEach((Class, vv) -> {
				if (word.equals(term)) {
					if (v.get(Class) > maxProb) {
						maxProb = v.get(Class);
					}
				}
			});
		});
		return maxProb;
	}

	//---------------------------------------
	// Creation du nouvelle Corpus apres khi2
	//---------------------------------------
	public static double khi2 = 0;
	public static Map<String, Map<String, Map<String, Integer>>> NewCorpus;

	public static Map<String, Map<String, Map<String, Integer>>> NewCorpus(
			Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		NewCorpus = new HashMap<>();
		Corpus.forEach((Clas, v) -> {
			Map<String, Map<String, Integer>> mapDoc = new HashMap<>();
			v.forEach((Doc, vv) -> {
				Map<String, Integer> mapTerm = new HashMap<>();
				vv.forEach((term, occ) -> {
					khi2 = 0;
					try {
						khi2 = MaxKhi2(term, Corpus);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (khi2 > 1.1) {
						System.out.println("Terme  ajouter au Train :" + term);
						System.out.println("Son Khi2 :" + khi2);
						mapTerm.put(term, occ);
					} else {
						System.out.println("Terme non ajouter au Train :" + term);
						System.out.println("Son Khi2 :" + khi2);
					}
				});
				mapDoc.put(Doc, mapTerm);

			});
			NewCorpus.put(Clas, mapDoc);
		});

		System.out.println(NewCorpus);
		return NewCorpus;
	}
	public static Map<String,Map<String,Map<String,Integer>>> NewTestCorpus;
	
	public static Map<String, Map<String, Map<String, Integer>>> NewTestCorpus(
			Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		NewTestCorpus = new HashMap<>();
		Corpus.forEach((Clas, v) -> {
			Map<String, Map<String, Integer>> mapDoc = new HashMap<>();
			v.forEach((Doc, vv) -> {
				Map<String, Integer> mapTerm = new HashMap<>();
				vv.forEach((term, occ) -> {
					khi2 = 0;
					try {
						khi2 = MaxKhi2(term, Corpus);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if (khi2 > 5.0) {
						System.out.println("Terme"+term +"  Ajouter au Corpus: Test " );
						System.out.println("Son Khi2 :" + khi2);
						mapTerm.put(term, occ);
					} else {
						System.out.println("Terme"+term +" Non ajouter au Corpus: Test " );
						System.out.println("Son Khi2 :" + khi2);
					}
				});
				mapDoc.put(Doc, mapTerm);

			});
			NewTestCorpus.put(Clas, mapDoc);
		});
		return NewTestCorpus;
	}
	public static void main(String[] args) throws IOException {
		
       System.out.println(CreateCorpus(Dir));
       Map<String, Map<String, Map<String, Integer>>> Corpus = CreateCorpus(Dir);
	   System.out.println("New Corpus: " + NewCorpus(Corpus));
	}
}