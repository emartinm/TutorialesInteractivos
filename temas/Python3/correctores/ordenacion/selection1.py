# -*- coding: UTF-8 -*-
import sys

from correctores.common.corrector_texto import corrector_texto

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - resultado: valor que debe escribir el alumno                ####
########################################################################


def selection(lst):
    for i in range(len(lst)):
        pmin = i
        for j in range(i,len(lst)):
            if lst[j] < lst[pmin]:
                pmin = j
        @@@CODE@@@
    return lst

def selection_ok(lst):
    for i in range(len(lst)):
        pmin = i
        for j in range(i,len(lst)):
            if lst[j] < lst[pmin]:
                pmin = j
        lst[i], lst[pmin] = lst[pmin], lst[i]
    return lst
    

resultado_user = selection([6,3,9,2,4])
    
resultado = selection_ok([6,3,9,2,4])


#################################
#### Esto no hay que tocarlo ####
#################################

def dummy():
    return selection([6,3,9,2,4])


if __name__ == "__main__":
    corrector_texto(sys.argv[1], resultado, dummy)
