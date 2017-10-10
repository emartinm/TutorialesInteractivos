# -*- coding: UTF-8 -*-

# Import imprescindibles
import sys
import json
# Imports necesarios para generar los casos
import random
import math



####
#### Esto es lo que hay que cambiar en cada problema:
####  - epsilon: para comparar flaots, si lo necesitas
####  - genera_casos: devuelve una lista de casos de prueba

def epsilon():
    return 1E-9


def genera_casos():
    # Generar los casos de prueba que se quieren comprobar
    l = list()
    for i in range(20):
        a = random.random() * 10
        b = random.random() * 10
        c = complex(a,b)
        l.append( ([('num',c)], [('modulo',abs(c))]) )
    return l




#################################
#### Esto no hay que tocarlo ####
#################################

def ejecutor_caso(g,l):
    # Aqui se pega el codigo del alumno
    codigo = """
@@@CODE@@@
"""
    exec(codigo,g,l)
    # 'l' contendrá los valores de salida


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
def corrector_variables(filename, casos_prueba, epsilon, ejecutor_caso):
    dicc = {'isCorrect':True}
    for (entrada,salida) in casos_prueba:
        if not dicc['isCorrect']:
            break # Paramos en cuanto falla un caso de prueba

        # Establecemos el valor de las variables de entrada
        dicc_valores = locals()
        for (var,valor) in entrada:
            dicc_valores[var] = valor

        # Ejecutamos el codigo del alumno
        ejecutor_caso(globals(), dicc_valores)
        # Los resultados se consultan en 'dicc_valores

        # Comprobamos los valores de las variables de salida
        for (var,valor) in salida:
            if not var in dicc_valores:
                # Variable no asignada
                dicc = {'isCorrect':False, 'typeError':"Variable '{0}' no asignada".format(var),
                    'Hints':["La variable '{0}' debería contener algún valor.".format(var)]}
            elif (type(valor) != type(dicc_valores[var])):
                # La variable tiene un tipo diferente al esperado
                hints = ["La variable '{0}' debe ser de tipo {1}".format(var, type(valor)),
                         "Sin embargo, en tu código tiene tipo '{0}' y valor {1}".format(type(dicc_valores[var]), dicc_valores[var])]
                dicc = {'isCorrect':False, 'typeError':"Variable '{0}' con tipo incorrecto".format(var),
                    'Hints': hints}
            elif (((type(valor) in [float,complex]) and (abs(dicc_valores[var]-valor) < epsilon)) or
                 (type(valor) not in [float,complex] and dicc_valores[var] == valor)):
                # Comprobacion con exito
                dicc = {'isCorrect':True}
            else:
                # Comprobacion de igualdad fallida
                hints = ["El valor de la variable {0} no es correcto.".format(var)]
                if len(entrada) > 0:
                    hints.append( "Considerando las siguientes variables:" )
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
    
    # Escribir el diccionario generado
    with open(filename, 'w') as outfile:
        json.dump(dicc, outfile)


if __name__ == "__main__":
    corrector_variables(sys.argv[1], genera_casos(), epsilon(), ejecutor_caso)