# -*- coding: UTF-8 -*-
import sys
import json

def pregunta(filename):
    try:
        x = 6
        #@@@SNIPPET@@@
        mayor_que_0 = @@@CODE@@@
        menor_o_igual_que_10 = @@@CODE@@@
        distinto_de_5 = @@@CODE@@@
        #@@@SNIPPET@@@
        dicc = {}
        things = locals()
        if 'mayor_que_0' in things and 'menor_o_igual_que_10' in things and 'distinto_de_5' in things:
            if things['mayor_que_0'] and things['menor_o_igual_que_10'] and things['distinto_de_5']:
            		dicc = {'isCorrect':True}
            else:
            		dicc = {'isCorrect':False, 'typeError':"Valores incorrectos", 'Hints':['Revisa la lección de comparadores']}
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
