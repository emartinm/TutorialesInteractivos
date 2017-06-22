# -*- coding: UTF-8 -*-
import sys
import json

def pregunta11(filename):    
    try:
        #@@@SNIPPET@@@ #Inicio del fragmento de código para mostrar
        month = [31,28,31,30,31,30,31,31,30,31,30,31]
        suma = 0
        for @@@CODE@@@ in @@@CODE@@@:
          suma = @@@CODE@@@
        #@@@SNIPPET@@@ #Fin del fragmento de código para mostrar
        dicc = {}
        things = locals()
        if 'suma' in things:
            if things['suma'] == sum(month):
            		dicc = {'isCorrect':True}
            else:
            		dicc = {'isCorrect':False, 'typeError':"Valor incorrecto de la variable 'suma'", 'Hints':['lee el enunciado de nuevo','siempre ayuda']}
        else:
		        dicc = {'isCorrect':False, 'typeError':"Variable 'c' no asignada", 'Hints':['prueba a leer el enunciado otra vez']}
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
