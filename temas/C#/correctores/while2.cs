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

	static void Main(String[] args) {
		bool ok = true;


		@@@CODE@@@
		/*
		int i = 0;
		while (i<10) {
		 WriteLine(i);
		  ++i;
		 }
		*/


		string my_res ="";
		int my_i=0;
		while (my_i<10){
		      my_res += my_i+"\n";
		      ++my_i;
		}

		ok = (user_output == my_res);

		escribeJSON (args [0], ok);
	}
}