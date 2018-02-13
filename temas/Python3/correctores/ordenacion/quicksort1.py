# -*- coding: UTF-8 -*-
import sys

from correctores.common.corrector_texto import corrector_texto

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - resultado: valor que debe escribir el alumno                ####
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
    
    
resultado = partition([6,3,9,2,4])


#################################
#### Esto no hay que tocarlo ####
#################################

def dummy():
    return @@@CODE@@@


if __name__ == "__main__":
    corrector_texto(sys.argv[1], resultado, dummy)
