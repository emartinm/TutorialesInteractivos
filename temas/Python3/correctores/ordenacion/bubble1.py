# -*- coding: UTF-8 -*-
import sys

from correctores.common.corrector_texto import corrector_texto

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - resultado: valor que debe escribir el alumno                ####
########################################################################


def bubble(lst):
    for i in range(len(lst)):
        j = 0
        tope = len(lst) - (i + 1)
        while(j < tope):
            if lst[j] > lst[j+1]:
                @@@CODE@@@
            j = j + 1
    return lst

def bubble_ok(lst):
    for i in range(len(lst)):
        j = 0
        tope = len(lst) - (i + 1)
        while(j < tope):
            if lst[j] > lst[j+1]:
                lst[j], lst[j+1] = lst[j + 1], lst[j]
            j = j + 1
    return lst
    
    
resultado = bubble_ok([6,3,9,2,4])


#################################
#### Esto no hay que tocarlo ####
#################################

def dummy():
    return bubble([6,3,9,2,4])


if __name__ == "__main__":
    corrector_texto(sys.argv[1], resultado, dummy)
