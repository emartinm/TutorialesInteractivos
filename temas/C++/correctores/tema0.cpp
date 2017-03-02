#include <iostream>
#include <fstream>
using namespace std;

@@@CODIGO@@@
/*
int incrementa(int n) {
	return n + 1;
}
*/

void escribeJSON(char* filename, bool isCorrect) {
	ofstream myfile;
  myfile.open(filename);
  myfile << "{" << endl;
  
  if (isCorrect) 
    myfile << "  \"isCorrect\":true," << endl;
  else 
	  myfile << "  \"isCorrect\":false," << endl;
	  
	myfile << "  \"typeError\":\"Mira mira\"" << endl;
	
	myfile << "}" << endl;
  myfile.close();
}

int main(int argc, char** argv) {
  bool ok = true;

	for( int i = -20; i <= 20; ++i ) {
		ok = ok && (incrementa(i) == i+1);
	}
	
	escribeJSON( argv[1], ok);
	return 0;
}


