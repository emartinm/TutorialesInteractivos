# -*- coding: UTF-8 -*-
import sys
import random

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
    casos = list()
    for i in range(20):
        l = [int(1000*random.random()) for i in range(i)]
        casos.append( ({'lst': l}, sorted(l)) )
    return casos


def bubble(lst):
    for i in range(len(lst)):
        j = 0
        tope = len(lst) - (i + 1)
        while(j < tope):
            if lst[j] > lst[j+1]:
                @@@CODE@@@
            j = j + 1
    return lst

def get_function():
    return bubble



#################################
#### Esto no hay que tocarlo ####
#################################

if __name__ == "__main__":
    corrector_funciones(sys.argv[1], get_function(), genera_casos(), epsilon())