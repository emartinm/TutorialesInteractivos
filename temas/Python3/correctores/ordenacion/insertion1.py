# -*- coding: UTF-8 -*-
import sys

from correctores.common.corrector_texto import corrector_texto

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - resultado: valor que debe escribir el alumno                ####
########################################################################


def insertion(lst):
    for i in range(1,len(lst)):
        j = i - 1
        while (j >= 0) and (lst[j+1] < lst[j]):
            @@@CODE@@@
            j = j - 1
    return lst

def insertion_ok(lst):
    for i in range(1,len(lst)):
        j = i - 1
        while (j >= 0) and (lst[j+1] < lst[j]):
            lst[j], lst[j+1] = lst[j+1], lst[j]
            j = j - 1
    return lst
    
    
resultado = insertion_ok([6,3,9,2,4])


#################################
#### Esto no hay que tocarlo ####
#################################

def dummy():
    return insertion([6,3,9,2,4])


if __name__ == "__main__":
    corrector_texto(sys.argv[1], resultado, dummy)
