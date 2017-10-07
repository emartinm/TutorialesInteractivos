# -*- coding: UTF-8 -*-
import sys
import json

def pregunta(filename):
    try:
        x = 6
        #@@@SNIPPET@@@
        apellidos = @@@CODE@@@
        nombre_y_apellidos = @@@CODE@@@
        #@@@SNIPPET@@@
        dicc = {}
        things = locals()
        if 'apellidos' in things and 'nombre_y_apellidos' in things:
            if things['apellidos'] == "García" and things['nombre_y_apellidos'] == "Ana García":
            		dicc = {'isCorrect':True}
            elif things['apellidos'] != "García":
            		dicc = {'isCorrect':False, 'typeError':"Apellido incorrecto", 'Hints':['Comprueba que el apellido es `García`']}
            elif things['nombre_y_apellidos'] == "AnaGarcía":
            		dicc = {'isCorrect':False, 'typeError':"Falta espacio", 'Hints':['Recuerda que tienes que poner explícitamente los espacios']}
            else:
            		dicc = {'isCorrect':False, 'typeError':"Valores incorrectos", 'Hints':['Revisa la separación entre nombre y apellidos']}
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
