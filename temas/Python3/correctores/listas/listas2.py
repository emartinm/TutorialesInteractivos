# -*- coding: UTF-8 -*-
import sys

from correctores.common.corrector_funciones_with_for import corrector_funciones

########################################################################
#### Esto es lo que hay que cambiar en cada problema:               ####
####  - epsilon: para comparar floats y complex, si lo necesitas    ####
####  - genera_casos: devuelve una lista de casos de prueba         ####
####  - función que queremos comprobar, con su hueco                ####
####  - get_function: devuelve la función que queremos comprobar    ####
########################################################################

def epsilon():
    return 1E-9

def correct_eval_poly(d):
    """This function evaluates the polynomial poly at point x.
    Poly is a list of floats containing the coeficients 
    of the polynomial
    poly[i] -> coeficient of degree i
    
    Parameters
    ----------
    poly: [float]
        Coefficients of the polynomial, 
        where poly[i] -> coeficient of degree i
    x : float
        Point
    
    Returns
    -------
    float
        Value of the polynomial at point x
        
    Example
    -------
    >>> eval_poly( [1.0, 1.0], 2)
    3.0
    """
    poly, x = d["poly"], d['x']   
    result = 0.0
    power = 1
    degree = len(poly) - 1
    i = 0
    while i <= degree:
        result = result + poly[i] * power
        power = power * x 
        i = i + 1
    return (d,result)

def genera_casos():
    # Generar los casos de prueba que se quieren comprobar
    param_list = [{'poly':[2.0,0.0,1.0],'x':3.0},{'poly':[2.0,3.0,1.0],'x':-3.0},{'poly':[],'x':3.0}]
    return map(correct_eval_poly,param_list)


def eval_poly(poly, x):
    @@@CODE@@@


def get_function():
    return eval_poly



#################################
#### Esto no hay que tocarlo ####
#################################

if __name__ == "__main__":
    corrector_funciones(sys.argv[1], get_function(), genera_casos(), epsilon())
