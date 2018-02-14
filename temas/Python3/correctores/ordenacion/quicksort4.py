# -*- coding: UTF-8 -*-
import sys

from correctores.common.corrector_texto import corrector_texto

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - resultado: valor que debe escribir el alumno                ####
########################################################################


resultado = [0,2,3]


#################################
#### Esto no hay que tocarlo ####
#################################

def dummy():
    return @@@CODE@@@


if __name__ == "__main__":
    corrector_texto(sys.argv[1], resultado, dummy)
