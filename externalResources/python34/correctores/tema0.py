# -*- coding: UTF-8 -*-
import sys
import json
import tempfile
# el json tiene que tener 3 campos
# campo 1: boolean isCorrect: true si está bien, false si está mal
# campo 2: string typeError: titulo del fallo para poner el mensaje en el label de java
# campo 3: lista de string Hints: pistas adicionales para mostrar
def pregunta11(filename):
    
    try:
        <+|CODIGO|+>
        dicc = {}
        things = locals()
        if 'i' in things:
            if things['i'] == 4:
                value1 = True
                value2 = ''
                value3 = ['']
            else:
                value1 = False
                value2 = 'Valor incorrecto'
                value3 = ['lee el enunciado de nuevo']
        else:
            value1 = False
            value2 = 'Variable no encontrada'
            value3 = ['prueba a leer el enunciado otra vez']
        dicc['isCorrect'] = value1
        dicc['typeError'] = value2
        dicc['Hints'] = value3
        with open(filename, 'w') as outfile:
            json.dump(dicc, outfile)
        sys.exit(0);    
    except Exception as e:
        e.print_exc()
        sys.exit(1)
        #return False



def main():
	filename = default_tmp_dir + '/' + sys.argv[1]
	pregunta11(filename)
	

if __name__ == "__main__":
	main()    
