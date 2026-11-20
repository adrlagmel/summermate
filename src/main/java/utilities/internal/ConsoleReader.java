/* Console.java
 *
 * Copyright (C) 2014 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 * 
 */

package utilities.internal;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ConsoleReader {
	private InputStreamReader stream;
	private BufferedReader reader;

	public ConsoleReader() {
		stream = new InputStreamReader(System.in);
		reader = new BufferedReader(stream);
	}

	public String readCommand() throws Throwable {
		String result;
		StringBuilder buffer;
		String line;
		String prompt;
			
		prompt = "> ";
		buffer = new StringBuilder();
		do {
			System.out.printf(prompt);
			line = reader.readLine();
			line = line == null ? null : line.trim();
			buffer.append(line);
			buffer.append(' ');
			prompt = "\t> ";
		} while (line != null && !line.endsWith(";"));
		
		if (line != null && line.endsWith(";") && buffer.length() >= 2)
			buffer.deleteCharAt(buffer.length() - 2);
		
		result = buffer.toString().trim();
		
		return result;
	}

	public String readLine() throws Throwable {
		String result;
		
		System.out.printf("> ");
		result = reader.readLine();
		result = result == null ? null : result.trim();
		
		return result;
	}

}
