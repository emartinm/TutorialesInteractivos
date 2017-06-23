/* Copyright 2017 Enrique Martín <emartinm@ucm.es>
 *
 * SPDX-License-Identifier: MIT
 */

using System;

public class Corrector {

	static void escribeJSON(String filename, bool isCorrect) {
		String[] lines = new String[5];
		lines [0] = "{";
		if (isCorrect) {
			lines [1] = "  \"isCorrect\":true,";
		} else {
			lines [1] = "  \"isCorrect\":false,";
		}
		lines[2] = "  \"typeError\":\"Mira mira\","; 
		lines[3] = "  \"Hints\":[\"Primera pista\", \"Segunda pista\"]";
		lines[4] = "}";
		System.IO.File.WriteAllLines(filename, lines);
	}

	static void Main(String[] args) {
		bool ok = true;


		@@@CODE@@@
		// int k = 0, i = 1;
	   	// while (i<=10) {
	        // k += i;
	        // ++i;
	        //  }



		ok = (k == 55);
		//ok = (o == "12345");	

		escribeJSON (args [0], ok);
	}
}