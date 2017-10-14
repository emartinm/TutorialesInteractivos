# -*- coding: UTF-8 -*-
import sys
import random
import math
from correctores.common.corrector_variables import corrector_variables

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - epsilon: para comparar floats y complex, si lo necesitas    ####
####  - genera_casos: devuelve una lista de casos de prueba         ####
########################################################################

def epsilon():
    return 1E-9


def genera_casos():
    # Generar los casos de prueba que se quieren comprobar
    l = list()
    for i in range(20):
        a = random.random() * 10
        b = random.random() * 10
        c = complex(a,b)
        l.append( ([('num',c)], [('modulo',abs(c))]) )
    return l




#################################
#### Esto no hay que tocarlo ####
#################################

def ejecutor_caso(g,l):
    # Aqui se pega el codigo del alumno
    codigo = """
@@@CODE@@@
"""
    exec(codigo,g,l)
    # 'l' contendrá los valores de salida
	

if __name__ == "__main__":
      corrector_variables(sys.argv[1], genera_casos(), epsilon(), ejecutor_caso, globals())