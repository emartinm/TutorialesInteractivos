# -*- coding: UTF-8 -*-
import json


"""
Función que sirve para corregir un problema de Python comparando el 
valor devuelto por una llamada a función unos parametros concretos

 - 'filename': 
     ruta absoluta donde almacenar el JSON con el resultados

 - 'f':
     Función a ejecutar
     
 - 'casos_prueba':
     lista de casos de prueba [CasoDePrueba]. 
     Cada CasoDePrueba es una pareja (DiccionarioParametros, ResultadoEsperado)

 - 'epsilon':
     precisión para comparar floats y complex


Ejemplo de 'casos_prueba' para la suma de las variables 'a' y 'b':
  [ ({'a':1, 'b':1}, 2),
    ({'a':6, 'b':2}, 8)
  ]
"""
def corrector_funciones(filename, f, casos_prueba, epsilon):
    dicc = {'isCorrect':True}
    for (params, salida) in casos_prueba:
        if not dicc['isCorrect']:
            break # Paramos en cuanto falla un caso de prueba

        # Ejecutamos la función con el codigo del alumno
        res = f(**params)

        # Comprobamos el valor de salida
        if type(res) != type(salida):
            # El resultado tiene un tipo diferente al esperado
            if len(params) == 0:
                hints = ["Al invocar a la función '{0}' sin parámetros".format(f.__name__)]
            else:
                hints = ["Al invocar a la función '{0}' con los parámetros:".format(f.__name__)]
                for key in params:
                    hints.append("  - {0}: {1}".format(key, params[key]))
                hints.append("El resultado debe ser de tipo {0}".format(type(salida)))
                hints.append("Sin embargo, tu código devuelve un valor de tipo '{0}' con valor {1}".format(type(res), res)) 
                dicc = {'isCorrect':False, 'typeError':"Resultado con tipo diferente", 'Hints': hints}
        elif (((type(salida) in [float,complex]) and (abs(salida-res) < epsilon)) or
            (type(salida) not in [float,complex] and salida == res)):
            # Comprobacion con exito
            dicc = {'isCorrect':True}
        else:
            # Comprobacion de igualdad fallida
            if len(params) == 0:
                hints = ["Al invocar a la función '{0}' sin parámetros".format(f.__name__)]
            else:
                hints = ["Al invocar a la función '{0}' con los parámetros:".format(f.__name__)]
                for key in params:
                    hints.append( "  - {0}: {1}".format(key, params[key]) )
                hints.append( "El resultado debería ser {0}".format(salida))
                hints.append( "Sin embargo, tu código devuelve el valor {0}".format(res) ) 
                dicc = {'isCorrect':False, 'typeError':"Resultado diferente", 'Hints': hints}


    # Escribir el diccionario generado
    with open(filename, 'w') as outfile:
        json.dump(dicc, outfile)