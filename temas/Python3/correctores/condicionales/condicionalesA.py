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
    return [([('h',7)],[('saludo',"Buenos días")]),
            ([('h',11)],[('saludo',"Buenos días")]),
            ([('h',6)],[('saludo',"Buenos días")]),
            ([('h',1)],[('saludo',"Hola")]),
            ([('h',17)],[('saludo',"Hola")]),
            ([('h',0)],[('saludo',"Hola")])]

def genera_casos():
    # Generar los casos de prueba que se quieren comprobar
    return [ ({'h':7}, "Buenos días"),
             ({'h':11}, "Buenos días"),
             ({'h':10}, "Buenos días"),
             ({'h':1}, "Hola"),
             ({'h':17}, "Hola"),
             ({'h':0}, "Hola"),
             ({'h':12}, "Hola"),
             ({'h':6}, "Hola") ]


def saludo(h):
    if (@@@CODE@@@):
        saludo = "Buenos días"
    @@@CODE@@@
        saludo = "Hola"
    return saludo


def get_function():
    return saludo



#################################
#### Esto no hay que tocarlo ####
#################################

if __name__ == "__main__":
    corrector_funciones(sys.argv[1], get_function(), genera_casos(), epsilon())

