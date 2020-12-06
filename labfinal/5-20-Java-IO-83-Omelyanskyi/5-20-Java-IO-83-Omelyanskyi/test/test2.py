def func_a(a):
    return a+5
    
def func_b(a,b):
    return func_a(a)-b
    
def main():
    a = 10
    c =  func_a(a) 
    c += func_a(a)+func_b(c,a)
    return c