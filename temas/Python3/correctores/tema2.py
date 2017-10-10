# -*- coding: UTF-8 -*-
import sys
import json
import tempfile

def pregunta11(code, filename):
    try:
        exec(code)
        dicc = {}
        things = locals()
        r=["Rafa","Carlos", 32, 27]
        if 'l' in things:
            if type(things['l']) is list and set(things['l']) == set(r):
                value1 = True
                value2 = ''
                value3 = ['']
            else:
                value1 = False
                value2 = 'Error de contenido'
                value3 = ['Puede que hayas escrito mal los elementos']
        else:
            value1 = False
            value2 = 'Variable incorrecta'
            value3 = ['Comprueba el nombre de la variable', 'Lee el enunciado de nuevo']
        dicc['isCorrect'] = value1
        dicc['typeError'] = value2
        dicc['Hints'] = value3
        with open(filename, 'w') as outfile:
            json.dump(dicc, outfile)
        sys.exit(0)
    except Exception as e:
        e.print_exc()
        sys.exit(1)

def main():
    question = sys.argv[1]
    code = sys.argv[2]
    print("Variables:")
    print(question)
    print(code)
    temp_name = next(tempfile._get_candidate_names())
    default_tmp_dir = tempfile._get_default_tempdir()
    filename = default_tmp_dir + '/' + sys.argv[3]
    if question == "pregunta11":
        pregunta11(code, filename)

if __name__ == "__main__":
    main()    
