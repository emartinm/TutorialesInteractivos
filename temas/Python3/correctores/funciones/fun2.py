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
    param_list = [{'h':1,'m':34,'s':15},{'h':3,'m':19,'s':1},{'h':10,'m':3,'s':5}]
    fun = lambda d:(d, d['h']*3600+d['m']*60+d['s'])
    return map(fun,param_list)


def seconds(h,m,s):
    @@@CODE@@@


def get_function():
    return seconds



#################################
#### Esto no hay que tocarlo ####
#################################

if __name__ == "__main__":
    corrector_funciones(sys.argv[1], get_function(), genera_casos(), epsilon())
