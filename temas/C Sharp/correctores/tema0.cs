using System;

public class Corrector {

    //<+|SNIPPET|+>
	<+|CODIGO|+>
	//<+|SNIPPET|+>
	//static int incrementa(int n) {
	//	return n + 1;
	//}

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

		for( int i = -20; i <= 20; ++i ) {
			ok = ok && (incrementa(i) == i+1);
		}
		escribeJSON (args [0], ok);
	}
}