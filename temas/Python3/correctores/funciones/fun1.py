# -*- coding: UTF-8 -*-
import sys

from correctores.common.corrector_funciones import corrector_funciones

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - epsilon: para comparar floats y complex, si lo necesitas    ####
####  - genera_casos: devuelve una lista de casos de prueba         ####
####  - función que queremos comprobar, con su hueco                ####
####  - get_function: devuelve la función que queremos comprobar    ####
########################################################################

def epsilon():
    return 1E-9


def genera_casos():
    # Generar los casos de prueba que se quieren comprobar
    return [ ({'a':1, 'b':1}, 2), ({'a':6, 'b':2}, 8) ]


def suma(a,b):
    @@@CODE@@@


def get_function():
    return suma



#################################
#### Esto no hay que tocarlo ####
#################################

if __name__ == "__main__":
    corrector_funciones(sys.argv[1], get_function(), genera_casos(), epsilon())