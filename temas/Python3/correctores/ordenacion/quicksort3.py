# -*- coding: UTF-8 -*-
import sys
import random

from correctores.common.corrector_funciones import corrector_funciones

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - epsilon: para comparar floats y complex, si lo necesitas    ####
####  - genera_casos: devuelve una lista de casos de prueba         ####
####  - función que queremos comprobar, con su hueco                ####
####  - get_function: devuelve la función que queremos comprobar    ####
########################################################################

def partition(l):
    piv = l[0]
    i = 1
    less_eq = []
    greater = []
    while i < len(l):
        current = l[i]
        if current <= piv:
            less_eq.append(current)
        else:
            greater.append(current)
        i = i + 1
    return (less_eq, piv, greater)

#@@@SNIPPET@@@ #Inicio del fragmento de código para mostrar
def quicksort(l):
    # Base case, the same list is returned.
    if len(l) <= 1:
        return l
    # Recursive case:
    else:
        (less_eq, piv, greater) = partition(l)
        sorted_less_eq = @@@CODE@@@
        sorted_greater = @@@CODE@@@
        return sorted_less_eq + [piv] + sorted_greater
#@@@SNIPPET@@@ #Fin del fragmento de código para mostrar


def epsilon():
    return 1E-9


def genera_casos():
    # Generar los casos de prueba que se quieren comprobar
    casos = list()
    for i in range(20):
        l = [int(1000*random.random()) for i in range(i)]
        casos.append( ({'l': l}, sorted(l)) )
    return casos


def get_function():
    return quicksort



#################################
#### Esto no hay que tocarlo ####
#################################

if __name__ == "__main__":
    corrector_funciones(sys.argv[1], get_function(), genera_casos(), epsilon())