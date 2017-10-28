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
    return [ ({'nota_num':0}, "suspenso"),
             ({'nota_num':1}, "suspenso"),
             ({'nota_num':2}, "suspenso"),
             ({'nota_num':3}, "suspenso"),
             ({'nota_num':4}, "suspenso"),
             ({'nota_num':5}, "aprobado"),
             ({'nota_num':6}, "aprobado"),
             ({'nota_num':7}, "notable"),
             ({'nota_num':8}, "notable"),
             ({'nota_num':9}, "sobresaliente"),
             ({'nota_num':10}, "sobresaliente") ]

def nota(nota_num):
    if (@@@CODE@@@):
        nota_transformada = "suspenso"
    @@@CODE@@@
        nota_transformada = "aprobado"
    @@@CODE@@@
        nota_transformada = "notable"
    @@@CODE@@@
        nota_transformada = "sobresaliente"
    return nota_transformada


def get_function():
    return nota



#################################
#### Esto no hay que tocarlo ####
#################################

if __name__ == "__main__":
    corrector_funciones(sys.argv[1], get_function(), genera_casos(), epsilon())

