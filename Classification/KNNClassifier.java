package Classification;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import FeatureEngineering.CHI2;

public class KNNClassifier {
	public static int   t=0;
	public static String Class_Name="";
	public static String Cla="";
	
	public static final File Dir = new File("C:\\Users\\ASUS\\Downloads\\Document");
	//public static final File Test = new File("F:\\MIDVI\\S3\\TextMining\\test\\الفصل 7.txt");
	public static final File Test = new File("F:\\MIDVI\\S3\\TextMining\\test\\المادة 15.txt");
	
	
	
	public static String knn(Map<String, Map<String, Map<String, Integer>>> Train,Map<String, Integer> Test, int K) {
		int d=0;
		int aide=-1;
		float Dis;
		Map<String, Map<String, Double>> map0=new HashMap<>();
		
		Map<String, Double> map1 = new HashMap<>();
		Train.forEach((Class,vv)->{
			Cla=Class;
			vv.forEach((Doc, vv2) -> {
			double Dist=Distance(vv2,Test);
			
				map1.put(Doc, Dist);
				
			});
			//map0.put(Class, map1);
		});
		Map<String, Double> map3 = new HashMap<>();
		
		
		
		 
		//LinkedHashMap preserve the ordering of elements in which they are inserted
		LinkedHashMap<String, Double> Map2 = new LinkedHashMap<>();
		 
		//Use Comparator.reverseOrder() for reverse ordering
		map1.entrySet()
		    .stream()
		    .sorted(Map.Entry.comparingByValue()) 
		    .forEachOrdered(x -> Map2.put(x.getKey(), x.getValue()));
		System.out.println(Map2);
		Map2.forEach((Classe,v)->{
		 if(t<1) {
			 Class_Name= Classe;
			 t=t+1;
		 }
		 
			
		});
		
		return Class_Name;
	}
	public static double Distance(Map<String, Integer> f1, Map<String, Integer> f2) {
        double sum = 0;
        for (String key : f1.keySet()) {
            int v1 = f1.get(key);
            int v2 = f2.containsKey(key)? f2.get(key) : 0;
            

            if (v1 != 0 && v2 != 0) {
                sum += Math.pow(v1 - v2, 2);
            }
        }

        return Math.sqrt(sum);
    }
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Map<String, Map<String, Map<String, Integer>>> TrainCorpus = CHI2.CreateCorpus(Dir);
		Map<String, Map<String, Map<String, Integer>>> TestCorpus = CHI2.CreateCorpus(Dir);
		//System.out.println(TrainCorpus);
		 Map<String, Integer> test=CHI2.CreateDoc(Test);
		 String a=knn(TrainCorpus,test,2);
		 System.out.println(a);
		 
	}
}
