# -*- coding: UTF-8 -*-
import sys
import json

def pregunta11(filename):    
    try:
        a = 1
        <+|CODIGO|+>
        b = 3
        <+|CODIGO|+>
        dicc = {}
        things = locals()
        if 'c' in things:
            if things['c'] == 6 and things['a'] == 2:
            		dicc = {'isCorrect':True}
                #value1 = True
                #value2 = ''
                #value3 = ['']
            else:
            		dicc = {'isCorrect':False, 'typeError':"Valor incorrecto de 'a' o de 'c'", 'Hints':['lee el enunciado de nuevo','siempre ayuda']}
                #value1 = False
                #value2 = 'Valor incorrecto'
                #value3 = ['lee el enunciado de nuevo']
        else:
		        dicc = {'isCorrect':False, 'typeError':"Variable 'c' no asignada", 'Hints':['prueba a leer el enunciado otra vez']}
            #value1 = False
            #value2 = 'Variable no encontrada'
            #value3 = ['prueba a leer el enunciado otra vez']
        #dicc['isCorrect'] = value1
        #dicc['typeError'] = value2
        #dicc['Hints'] = value3
        with open(filename, 'w') as outfile:
            json.dump(dicc, outfile)
        sys.exit(0);    
    except Exception as e:
        e.print_exc()
        sys.exit(1)
        #return False



def main():
	pregunta11(sys.argv[1])
	

if __name__ == "__main__":
	main()    
