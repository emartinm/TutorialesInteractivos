# -*- coding: UTF-8 -*-
import sys
import json
# el json tiene que tener 3 campos
# campo 1: boolean isCorrect: true si está bien, false si está mal
# campo 2: string typeError: titulo del fallo para poner el mensaje en el label de java
# campo 3: lista de string Hints: pistas adicionales para mostrar
def pregunta11(filename):
    
    try:
        @@@CODE@@@
        dicc = {}
        things = locals()
        if 'i' in things:
            if things['i'] == 4:
            		dicc = {'isCorrect':True}
                #value1 = True
                #value2 = ''
                #value3 = ['']
            else:
            		dicc = {'isCorrect':False, 'typeError':"Valor incorrecto", 'Hints':['lee el enunciado de nuevo','siempre ayuda']}
                #value1 = False
                #value2 = 'Valor incorrecto'
                #value3 = ['lee el enunciado de nuevo']
        else:
		        dicc = {'isCorrect':False, 'typeError':"Variable 'i' no asignada", 'Hints':['prueba a leer el enunciado otra vez']}
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
