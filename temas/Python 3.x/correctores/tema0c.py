# -*- coding: UTF-8 -*-
import sys
import json

def pregunta(filename):    
    try:
        a = <+|CODIGO|+>
        b = <+|CODIGO|+>
        c = <+|CODIGO|+>
        d = <+|CODIGO|+>
        dicc = {}
        things = locals()
        if 'a' in things and 'b' in things and 'c' in things and 'd' in things:
            if things['a'] == 1 and things['b'] == 2 and things['c'] == 3 and things['d'] == 4:
            		dicc = {'isCorrect':True}
            else:
            		dicc = {'isCorrect':False, 'typeError':"Valores incorrectos", 'Hints':['lee el enunciado de nuevo','siempre ayuda']}
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
