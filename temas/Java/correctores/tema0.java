/* Copyright 2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */

import java.io.FileWriter;
import java.util.List;

class Corrector {

	@@@CODE@@@
	/*
	public static int incrementa(int n){
		return n+1;
	}
	*/

	public static void main(String[] args){
		boolean ok = true;
		for( int i = -20; i <= 20; ++i ) {
			ok = ok && (incrementa(i) == i+1);
	}
	escribeJSON( args[0], ok, null, null);
	
	}
	
	public static void escribeJSON(String filename, boolean isCorrect, String errorMsg, List<String> hints) {
		try {
			FileWriter fw = new FileWriter(filename);
		
			fw.write("{\n");
			String c = "false";
			if (isCorrect) {
				c = "true";
			}
			fw.write("  \"isCorrect\":" + c + ",\n");
			fw.write("  \"typeError\":\"Mira mira\"\n");
			fw.write("}");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


