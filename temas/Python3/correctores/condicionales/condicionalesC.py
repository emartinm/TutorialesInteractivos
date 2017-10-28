# -*- coding: UTF-8 -*-
import sys

from correctores.common.corrector_funciones import corrector_funciones

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - epsilon: para comparar floats y complex, si lo necesitas    ####
####  - genera_casos: devuelve una lista de casos de prueba         ####
########################################################################

def epsilon():
    return 1E-9

def genera_casos():
    # Generar los casos de prueba que se quieren comprobar
    return [ ({'x':0, 'y':1, 'sel':True}, 0),
             ({'x':0, 'y':1, 'sel':False}, 1),
             ({'x':1, 'y':0, 'sel':True}, 0),
             ({'x':1, 'y':0, 'sel':False}, 1) ]

def min_max(x,y,sel):
    if (@@@CODE@@@):
        @@@CODE@@@
            resultado = x
        else:
            resultado = y
    else:
        @@@CODE@@@
            resultado = x
        else:
            resultado = y
    return resultado


def get_function():
    return min_max



#################################
#### Esto no hay que tocarlo ####
#################################

if __name__ == "__main__":
    corrector_funciones(sys.argv[1], get_function(), genera_casos(), epsilon())

