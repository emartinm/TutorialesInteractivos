# -*- coding: UTF-8 -*-
import sys

from correctores.common.corrector_texto import corrector_texto

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - resultado: valor que debe escribir el alumno                ####
########################################################################


def pfhvbndtwr(l1, l2):
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
    
    
resultado = pfhvbndtwr([1,4,8],[])


#################################
#### Esto no hay que tocarlo ####
#################################

def dummy():
    return @@@CODE@@@


if __name__ == "__main__":
    corrector_texto(sys.argv[1], resultado, dummy)
