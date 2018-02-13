# -*- coding: UTF-8 -*-

import json


"""
Función que sirve para corregir un problema de Python en el que se pide escribir
únicamente un valor resultante de una invocación o expresión. 

 - 'filename': 
     ruta absoluta donde almacenar el JSON con el resultados
     
 - 'resultado':
     resultado esperado que debe escribir el alumno
     
 - 'ejecutor':
     función Python que devuelve el valor que ha escrito el alumno     
"""
def corrector_texto(filename, resultado, ejecutor):
    dicc = {'isCorrect': resultado == ejecutor() }
    if not dicc['isCorrect']: 
        dicc['typeError'] = "Valor incorrecto"
        dicc['Hints'] = ["El valor {0} no coincide con el esperado".format(ejecutor())]
   
    # Escribir el diccionario generado
    with open(filename, 'w') as outfile:
        json.dump(dicc, outfile)
