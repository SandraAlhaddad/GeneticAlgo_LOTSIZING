package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadBestResults {
	
	public static double getBestSolution(File file, String instance){
		double erg = 0.;
		try{
			Scanner scanner = new Scanner(file);
			boolean found   = false;
			while (scanner.hasNext()) {
				String nextInstance = scanner.next();
				double nextSolution = Double.valueOf(scanner.next());
				String nextQuelle   = scanner.next();
				
				//System.out.println(instance + " gefunden: " + nextInstance);
				if(nextInstance.equals(instance)){
					//System.out.println(nextInstance + " " + nextSolution + " " + nextQuelle);
					found = true;
					erg   = nextSolution;
					break;
				}				
			}
			scanner.close();
			if(!found)System.out.println("Loesung nicht in " + file.getName() + " enthalten!");
			return erg;
		}
		catch(FileNotFoundException e) {
        	System.out.println(file.getAbsolutePath() + " " + e.getMessage());
		}
		return erg;
	}
}
