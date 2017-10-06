# -*- coding: UTF-8 -*-
import sys
import json
import math
import random


EPSILON = 10E-9

def pitagoras(a,b):
    @@@CODE@@@
    mih = math.sqrt(a**2 + b**2)
    return abs(h-mih) < EPSILON

def comprueba_hipotenusas(filename):
    ok = True
    for i in range(1,15):
        a = random.random() * random.randint(i,2*i)
        b = random.random() * random.randint(i,2*i)
        ok = ok and pitagoras(a,b)
        
    if ok:
        dicc = {'isCorrect':True}
    else:
        dicc = {'isCorrect':False, 'typeError':"La hipotenusa no es correcta"}
    
    with open(filename, 'w') as outfile:
        json.dump(dicc, outfile)
    sys.exit(0);    


def main():
	comprueba_hipotenusas(sys.argv[1] )
	

if __name__ == "__main__":
	main()