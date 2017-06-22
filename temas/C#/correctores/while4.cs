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

	static string user_output = "";

	static void Write(object s){ user_output += s; }
	static void WriteLine(object s){ user_output += s + "\n"; }


	static int n, m;
	static void invierte(){
		@@@CODE@@@
	}       

	static void Main(String[] args) {
		bool ok = true;


		n = 12345;
		m=-1;
		invierte();	

		ok = (m==54321);


		n = 4675;
		invierte();

		ok = ok && (m==5764);

		escribeJSON (args [0], ok);
	}
}