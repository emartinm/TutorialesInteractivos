# -*- coding: UTF-8 -*-
import sys
import random
import cmath
from correctores.common.corrector_variables import corrector_variables

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - epsilon: para comparar floats y complex, si lo necesitas    ####
####  - genera_casos: devuelve una lista de casos de prueba         ####
########################################################################

def epsilon():
    return 1E-9


def rand_complex():
    return complex(random.random() * 100,random.random() * 100)

def genera_casos():
    # Generar los casos de prueba que se quieren comprobar
    l = list()
    l.append( ([('a',1+0j), ('b',0j), ('c',1+0j)], [('r1',1j), ('r2',-1j)]) )
    for i in range(20):
        a = rand_complex()
        b = rand_complex()
        c = rand_complex()
        r1 = (-b + cmath.sqrt(b**2 - 4*a*c)) / (2*a)
        r2 = (-b - cmath.sqrt(b**2 - 4*a*c)) / (2*a)
        l.append( ([('a',a), ('b',b), ('c',c)], [('r1',r1), ('r2',r2)]) )
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