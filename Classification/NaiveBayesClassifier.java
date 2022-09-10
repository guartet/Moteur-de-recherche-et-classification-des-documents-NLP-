package Classification;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import FeatureEngineering.CHI2;


public class NaiveBayesClassifier {
	public static int nbr = 0;
	public static int nbDocInClass = 0;
	public static int nbDocInCorpus = 0;
	
	public static int nk = 0;
	public static double pro = 1;
	public static double max=0;
	public static double probabilite = 1;
	public static double proTC = 1;
	
	public static double maxPro = 0;
	public static String st = "";
	public static String classe= "";
	
	public static final File Dir = new File("C:\\Users\\ASUS\\Downloads\\Document");
	//public static final File Test = new File("F:\\MIDVI\\S3\\TextMining\\test\\الفصل 10.txt");
	//public static final File Test = new File("F:\\MIDVI\\S3\\TextMining\\test\\المادة 15.txt");
	public static final File Test = new File("F:\\MIDVI\\S3\\TextMining\\test");
	
	//----------------------------------------------------------
	//  getCHI2_Corpus:  retourne le corpus crÃ©e dans la Classe Khi2
	//----------------------------------------------------------
	public static Map<String, Map<String, Map<String, Integer>>> getCHI2_Corpus( Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		Map<String, Map<String, Map<String, Integer>>> NewCorpus = CHI2.NewCorpus(Corpus);
		return NewCorpus;
	}

	//--------------------------------------------------------------
	//@ Methode qui retourne le corpus Test crÃ©e dans la Classe Khi2
	//----------------------------------------------------------
	public static Map<String, Map<String, Map<String, Integer>>> getCHI2_TestCorpus(Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		Map<String, Map<String, Map<String, Integer>>> NewCorpus = CHI2.NewTestCorpus(Corpus);
		return NewCorpus;
	}

	//---------------------------------------------------------------------
	// nk :CorpusOccTermInClass:  Nombre d'occurence du terme dans la classe
	//---------------------------------------------------------------------
	public static Map<String, Map<String, Integer>> CorpusOccTermInClass(Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		Map<String, Map<String, Integer>> corpusTermOCC = new HashMap<String, Map<String, Integer>>();
		Corpus.forEach((Class, v) -> {
			Map<String, Integer> map = new HashMap<>();
			v.forEach((Doc, vv) -> vv.forEach((word, occ) -> {
				if (map.containsKey(word)) {
					map.put(word, map.get(word) + occ);
				} else {
					map.put(word, occ);
				}
			}));
			corpusTermOCC.put(Class, map);
		});
		return corpusTermOCC;
	}
	
	//----------------------------------------------------------
	// n : NbrTermInClass:  Nombre des termes dans la classe
	//----------------------------------------------------------
	public static int NbrTermInClass(String Class, Map<String, Map<String, Map<String, Integer>>> Corpus)throws IOException {
		nbr = 0;
		Map<String, Map<String, Integer>> TermOccInClass = CorpusOccTermInClass(Corpus);
		TermOccInClass.get(Class).forEach((word, occ) -> nbr += occ);
		return nbr;
	}
	
	//----------------------------------------------------------
	// m : NbrMotNonRepete:  Nombre du Mot non repetÃ© dans le corpus
	//----------------------------------------------------------
	public static int NbrMotNonRepete(Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		Map<String, Map<String, Integer>> TermOccInClass = CorpusOccTermInClass(Corpus);
		Set<String> mapMotNonRepete = new HashSet<>();
		TermOccInClass.forEach((Clas, v) -> mapMotNonRepete.addAll(v.keySet()));
		return mapMotNonRepete.size();
	}
	//----------------------------------------------------------
	// m : NbrMotNonRepete:  les Mots non repetÃ© dans le corpus
	//----------------------------------------------------------
	public static Set<String> MotsNonRepete(Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		Map<String, Map<String, Integer>> TermOccInClass = CorpusOccTermInClass(Corpus);
		Set<String> mapMotNonRepete = new HashSet<>();
		TermOccInClass.forEach((Clas, v) -> mapMotNonRepete.addAll(v.keySet()));
		return mapMotNonRepete;
	}

	//----------------------------------------------------------
	// @ Prob(C): la probabilite de la classe
	//----------------------------------------------------------
	public static double ProbClass(String Class, Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		nbDocInCorpus = 0;
		Corpus.forEach((Clas, v) -> {
			nbDocInCorpus += v.size();
		});
		int x = Corpus.get(Class).size();
		double y = (double) nbDocInCorpus;
		double z = x / y;

		return z;
	}
	//------------------------------------------------------------
	// @ Prob(Wi/C): Probablite d'un term dans une Class P(Wi/C)
	//------------------------------------------------------------
	public static double ProbTermInClass(String term, String Class,Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		Map<String, Map<String, Integer>> corpOccTermClass = CorpusOccTermInClass(Corpus);
		int n = NbrTermInClass(Class, Corpus);
		int m = NbrMotNonRepete(Corpus);
		Map<String, Integer> cls = corpOccTermClass.get(Class);
		nk = cls.containsKey(term) ? cls.get(term) : 0;
//		System.out.println(nk);
//		System.out.println(n);
//		System.out.println(m);
//		System.out.println(cls);
//		
		double prob = (nk + 1) / (double) (n + m);
		return prob;
	}
	//-----------------------------------------------------------
	// @ Prob(W/C): Probablite d'une terme du Corpus
	//-----------------------------------------------------------
	public static Map<String, Map<String, Double>> mapProbTermInClasses(Map<String, Map<String, Map<String, Integer>>> Corpus) throws IOException {
		
		Map<String, Map<String, Double>> mapProbTerms = new HashMap<String, Map<String, Double>>();
		Set<String> motNonRepete = MotsNonRepete(Corpus);
		Corpus.forEach((Class, v) -> {
			Map<String, Double> map = new HashMap<>();

			motNonRepete.forEach((term) -> {
				pro = 1;
				try {
					pro = ProbTermInClass(term, Class, Corpus);
					map.put(term, pro);

				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			mapProbTerms.put(Class, map);
		});
		return mapProbTerms;
	}
	//-----------------------------------------------------------------------------
	// @ Prob(W/C): Probablite d'une terme dans tout les class sous forme d'une Map
	//-----------------------------------------------------------------------------
	public static Map<String, Double> Prob;
	public static Map<String, String> Predic;

	public static  String ClassiffierFiles1( Map<String, Integer> Test, Map<String, Map<String, Map<String, Integer>>> Train) throws IOException {
		Map<String, Map<String, Double>> ClassProb = new HashMap<>();
		Map<String, Map<String, Double>> mapProb = mapProbTermInClasses(Train);

		System.out.println(mapProb);

		
				Prob = new HashMap<>();
				mapProb.forEach((Class, v) -> {
					probabilite = 1;
					try {
						probabilite = ProbClass(Class, Train);
					} catch (IOException e) {
						e.printStackTrace();
					}

					Test.forEach((term, occ) -> {

						proTC = 0;
						if (v.containsKey(term)) {

							proTC = v.get(term);
							probabilite *= proTC;
						}
					});

					Prob.put(Class, probabilite);
				});
				//ClassProb.put(Doc, Prob);
				
        Prob.forEach((pro,cc)->{
        	
        	if(cc>max) {
        		max=cc;
        		classe=pro;
        	}
        	
        });
        
		return classe;
	}
	
	
	
	/*public static  Map<String,String> ClassiffierFiles2( Map<String, Map<String, Integer>> Test, Map<String, Map<String, Map<String, Integer>>> Train) throws IOException {
		Map<String, Map<String, Double>> ClassProb = new HashMap<>();
		Map<String, Map<String, Double>> mapProb = mapProbTermInClasses(Train);

		System.out.println(mapProb);

		Test.forEach((doc,val)->{
				Prob = new HashMap<>();
				mapProb.forEach((Class, v) -> {
					probabilite = 1;
					try {
						probabilite = ProbClass(Class, Train);
					} catch (IOException e) {
						e.printStackTrace();
					}

					Test.forEach((term, occ) -> {

						proTC = 0;
						if (v.containsKey(term)) {

							proTC = v.get(term);
							probabilite *= proTC;
						}
					});

					Prob.put(Class, probabilite);
				});
				//ClassProb.put(Doc, Prob);
				
        Prob.forEach((pro,cc)->{
        	
        	if(cc>max) {
        		max=cc;
        		classe=pro;
        	}
        	
        });
        Predic.put(doc, classe);
		});
		return Predic;
	}
	*/
	
	public static Map<String, Map<String, Double>> ClassiffierFiles(Map<String, Map<String, Map<String, Integer>>> Test, Map<String, Map<String, Map<String, Integer>>> Train) throws IOException {
		Map<String, Map<String, Double>> ClassProb = new HashMap<>();
		Map<String, Map<String, Double>> mapProb = mapProbTermInClasses(Train);

		System.out.println(mapProb);

		Test.forEach((Clas, v2) -> {
			v2.forEach((Doc, vv2) -> {
				Prob = new HashMap<>();
				mapProb.forEach((Class, v) -> {
					probabilite = 1;
					try {
						probabilite = ProbClass(Class, Train);
					} catch (IOException e) {
						e.printStackTrace();
					}

					vv2.forEach((term, occ) -> {

						proTC = 0;
						if (v.containsKey(term)) {

							proTC = v.get(term);
							probabilite *= proTC;
						}
					});

					Prob.put(Class, probabilite);
				});
				ClassProb.put(Doc, Prob);
			});

		});
		return ClassProb;
	}

	
	public static Map<String, Map<String, Double>> ClassiffierFiles2( Map<String, Map<String, Integer>> Test, Map<String, Map<String, Map<String, Integer>>> Train) throws IOException {
		Map<String, Map<String, Double>> ClassProb = new HashMap<>();
		Map<String, Map<String, Double>> mapProb = mapProbTermInClasses(Train);

		System.out.println(mapProb);

		
			Test.forEach((Doc, vv2) -> {
				Prob = new HashMap<>();
				mapProb.forEach((Class, v) -> {
					probabilite = 1;
					try {
						probabilite = ProbClass(Class, Train);
					} catch (IOException e) {
						e.printStackTrace();
					}

					vv2.forEach((term, occ) -> {

						proTC = 0;
						if (v.containsKey(term)) {

							proTC = v.get(term);
							probabilite *= proTC;
						}
					});

					Prob.put(Class, probabilite);
				});
				ClassProb.put(Doc, Prob);
			});

		
		return ClassProb;
	}

	public static Map<String, String> filesClassName2(Map<String, Map<String, Integer>> Test,
			Map<String, Map<String, Map<String, Integer>>> Train) throws IOException {
		Map<String, String> map2 = new HashMap<>();
		Map<String, Map<String, Double>> map = ClassiffierFiles2(Test, Train);
		map.forEach((Doc, v) -> {
			docName = Doc;
			maxPro = 0;
			v.forEach((Class, prob) -> {

				if (prob > maxPro) {
					maxPro = prob;
					st = Class;
				}
			});
			map2.put(docName, st);
		});
		return map2;
	}
	
	
	

	public static String docName = "";

	public static Map<String, String> filesClassName(Map<String, Map<String, Map<String, Integer>>> Test,
			Map<String, Map<String, Map<String, Integer>>> Train) throws IOException {
		Map<String, String> map2 = new HashMap<>();
		Map<String, Map<String, Double>> map = ClassiffierFiles(Test, Train);
		map.forEach((Doc, v) -> {
			docName = Doc;
			maxPro = 0;
			v.forEach((Class, prob) -> {

				if (prob > maxPro) {
					maxPro = prob;
					st = Class;
				}
			});
			map2.put(docName, st);
		});
		return map2;
	}

	/*
	 * 
	 */
	public static int WellPredict = 0;
	public static int DocNumber = 0;

	public static double CalculateAccuracy(Map<String, Map<String, Map<String, Integer>>> Test,
			Map<String, Map<String, Map<String, Integer>>> Train) throws IOException {
		Map<String, String> filesClass = filesClassName(Test, Train);

		Test.forEach((Class, v) -> {
			v.forEach((Doc, vv) -> {
				DocNumber++;
				if (filesClass.get(Doc).equals(Class)) {
					WellPredict++;
				}
			});
		});
		//System.out.println("well predicted .."+ WellPredict++);
		return (WellPredict / (double) DocNumber) * 100;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Map<String, Map<String, Map<String, Integer>>> TrainCorpus = CHI2.CreateCorpus(Dir);
		//Map<String, Map<String, Map<String, Integer>>> TestCorpus = CHI2.CreateCorpus(Dir);
		//System.out.println(TestCorpus);
		 //Map<String, Integer> test=CHI2.CreateDoc(Test);
		 Map<String,Map<String, Integer>> test=CHI2.CreateDoc1(Test);
		
		//System.out.println(test);
		 //Map<String, String> b=filesClassName(TestCorpus,TrainCorpus);
		 //System.out.println(b);
						//String a=ClassiffierFiles1(test,TrainCorpus);
						//System.out.println(a);
		
		Map<String,String> c=filesClassName2(test,TrainCorpus);
		
		System.out.println(c);
		
						
		//System.out.println("Accuracy equals =" + CalculateAccuracy(TestCorpus, TrainCorpus) + " %");

	}

}
