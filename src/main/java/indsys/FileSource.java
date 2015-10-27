package indsys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StreamCorruptedException;
import java.util.LinkedList;
import java.util.List;

import interfaces.IOable;

public class FileSource<T, U> implements IOable<T, U>{
	private static final String FILE_NAME = "aliceInWonderland.txt";
	private static final String OUT_FILE_NAME = "filtered_" + FILE_NAME;

	//P.S. I think this is absolutely wrong. 
	
	public U read() throws StreamCorruptedException {
		try(BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
			List<String> lines = new LinkedList<String>();
			String currentLine;
			
			while ((currentLine = br.readLine()) != null) {
				lines.add(currentLine);
			}
			
			return (U)lines; 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void write(T value) throws StreamCorruptedException {
		try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OUT_FILE_NAME)))) {			
			for (String line : (List<String>) value) {
				bw.write(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
