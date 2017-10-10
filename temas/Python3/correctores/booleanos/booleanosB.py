# -*- coding: UTF-8 -*-
import sys
import json

def pregunta(filename):
    try:
        x = False
        y = True
        #@@@SNIPPET@@@
        ambas = @@@CODE@@@
        una_u_otra = @@@CODE@@@
        no_x = @@@CODE@@@
        #@@@SNIPPET@@@
        dicc = {}
        things = locals()
        if 'ambas' in things and 'una_u_otra' in things and 'no_x' in things:
            if not things['ambas'] and things['una_u_otra'] and things['no_x']:
            		dicc = {'isCorrect':True}
            else:
            		dicc = {'isCorrect':False, 'typeError':"Valores incorrectos", 'Hints':['Revisa la lección de operadores lógicos']}
        with open(filename, 'w') as outfile:
            json.dump(dicc, outfile)
        sys.exit(0);
    except Exception as e:
        e.print_exc()
        sys.exit(1)
        #return False



def main():
	pregunta(sys.argv[1])


if __name__ == "__main__":
	main()
