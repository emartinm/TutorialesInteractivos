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

def merge(l1, l2):
    i = 0
    j = 0
    merged_list = []
    while i < len(l1) and j < len(l2):
        if l1[i] <= l2[j]:
            merged_list.append(l1[i])
            i = i + 1
        else:
            merged_list.append(l2[j])
            j = j + 1
    while i < len(l1):
        merged_list.append(l1[i])
        i = i + 1
    while j < len(l2):
        merged_list.append(l2[j])
        j = j + 1
    return merged_list


#@@@SNIPPET@@@ #Inicio del fragmento de código para mostrar
def mergesort(l):
    if len(l) <= 1:
        return l
    else:
        half = @@@CODE@@@
        l1 = mergesort(l[:half])
        l2 = mergesort(l[half:])
        return @@@CODE@@@
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
    return mergesort



#################################
#### Esto no hay que tocarlo ####
#################################

if __name__ == "__main__":
    corrector_funciones(sys.argv[1], get_function(), genera_casos(), epsilon())