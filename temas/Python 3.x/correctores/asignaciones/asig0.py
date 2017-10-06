# -*- coding: UTF-8 -*-
import sys
import json

def comprueba_igualdades(filename, valores):
    try:
        @@@CODE@@@
        dicc = {'isCorrect':True}
        things = locals()
        for v in valores.keys():
            if v in things:
                if things[v] != valores[v]:
                    dicc = {'isCorrect':False, 'typeError':"Valor de '{0}' incorrecto".format(v)}
                    break
            else:
                dicc = {'isCorrect':False, 'typeError':"Variable '{0}' no asignada".format(v)}
                break
                
        with open(filename, 'w') as outfile:
            json.dump(dicc, outfile)
        sys.exit(0);    
    except Exception as e:
        e.print_exc()
        sys.exit(1)
        #return False

def main():
	comprueba_igualdades(sys.argv[1], {'longitud': 350})
	

if __name__ == "__main__":
	main()    


