# -*- coding: UTF-8 -*-
import sys
import json

def pregunta11(filename):    
    try:
        #@@@SNIPPET@@@ #Inicio del fragmento de código para mostrar
        n = 40
        cont = 0
        while n%2 == 0:
            cont = @@@CODE@@@
            n = n // 2 
        #@@@SNIPPET@@@ #Fin del fragmento de código para mostrar
        dicc = {}
        things = locals()
        if 'cont' in things:
            if things['cont'] == 3:
            		dicc = {'isCorrect':True}
            else:
            		dicc = {'isCorrect':False, 'typeError':"Valor incorrecto de la variable 'cont'", 'Hints':['lee el enunciado de nuevo','siempre ayuda']}
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
