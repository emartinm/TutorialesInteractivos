# -*- coding: UTF-8 -*-
import sys
import random
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
    casos  = list()
    for i in range(1,20):
        calif = random.random() * 10
        calif_redondeada = round(calif)
        casos.append( ([('calif',calif)], [('calif_redondeada',calif_redondeada)]) )
    return casos




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