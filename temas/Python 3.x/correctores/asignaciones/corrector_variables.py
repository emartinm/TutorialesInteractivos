# -*- coding: UTF-8 -*-
import sys
import json
import math
import random


EPSILON = 1E-9

"""
Función que sirve para corregir un problema de Python comparando el 
valor de las variables de salida
 - Acepta una lista de casos de prueba [CasoDePrueba]
 - Cada CasoDePrueba es una pareja ([ListaVariablesEntrada, ListaVariablesSalida])
 - Las listas de variables de entrada y salida son listas de parejas (variable, valor)
 - Devuelve el diccionario con el resultado, el mensaje y las pistas

Ejemplo para la suma de 'a' y 'b':

corrector_variables(
    [([('a',1),('b',1)],[('suma',2)]),
     ([('a',3),('b',1)],[('suma',5)])]
)
"""
def corrector_variables(casos_prueba):
    dicc = {'isCorrect':True}
    for (entrada,salida) in casos_prueba:
        if not dicc['isCorrect']:
            break # Paramos en cuanto falla un caso de prueba

        # Establecemos el valor de las variables de entrada
        dicc_valores = locals()
        for (var,valor) in entrada:
            dicc_valores[var] = valor

        # Ejecutamos el codigo del alumno
        # Es importante que el código pegado comience en line nueva sin espacios ni nada
        codigo = """
@@@CODE@@@
"""
        exec(codigo,globals(),dicc_valores)
        # Los resultados se consultan en 'dicc_valores

        # Comprobamos los valores de las variables de salida
        for (var,valor) in salida:
            if not var in dicc_valores:
                dicc = {'isCorrect':False, 'typeError':"Variable '{0}' no asignada".format(var),
                    'Hints':["La variable '{0}' debería tener un valor.".format(var)]}
            elif ((type(valor) == float and (abs(dicc_valores[var] - valor) < EPSILON)) or
                 (type(valor) != float and dicc_valores[var] == valor)): 
                # Comprobacion con existo
                dicc = {'isCorrect':True}
            else:
                # Comprobacion de igualdad fallida
                hints = ["El valor de la variable {0} no es correcto.".format(var), 
                         "Considerando las siguientes variables:"]
                for (vare,valore) in entrada: 
                    hints.append( "  - {0}: {1}".format(vare,valore) )
                hints.append( 'El resultado debería ser:' )
                hints.append( '  - {0}: {1}'.format(var,valor) )
                hints.append( 'Sin embargo, tu código genera' )
                hints.append( '  - {0}: {1}'.format(var,dicc_valores[var]) )
                dicc = {'isCorrect':False, 'typeError':"Variable '{0}' con valor incorrecto".format(var),
                    'Hints':hints}

            if not dicc['isCorrect']:
                break # Paramos en cuanto falla un caso de prueba
    
    return dicc

                
def comprobador(filename):
    # Generar los casos de prueba que se quieren comprobar
    casos  = list()
    ###
    ### Rellenar 'casos' con los casos de prueba
    ###

    # Pasar todos los casos, termina en cuanto falla alguno
    dicc = corrector_variables(casos)

    # Escribir el diccionario generado
    with open(filename, 'w') as outfile:
        json.dump(dicc, outfile)



if __name__ == "__main__":
	comprobador(sys.argv[1])