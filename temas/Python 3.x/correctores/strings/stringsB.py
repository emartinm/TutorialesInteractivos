# -*- coding: UTF-8 -*-
import sys
import json

def pregunta(filename):
    try:
        x = "b"
        #@@@SNIPPET@@@
        cod = @@@CODE@@@
        cod_str = @@@CODE@@@
        #@@@SNIPPET@@@
        dicc = {}
        things = locals()
        if 'cod' in things and 'cod_str' in things:
            if things['cod'] == 98 and things['cod_str'] == "98":
            		dicc = {'isCorrect':True}
            elif things['cod'] != 98:
            		dicc = {'isCorrect':False, 'typeError':"Valores incorrectos", 'Hints':['No estás calculando bien el código, ¿estás usando `ord`?']}
            else:
            		dicc = {'isCorrect':False, 'typeError':"Valores incorrectos", 'Hints':['No estás transformando el valor en String, ¿estás usando `str`?']}
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
