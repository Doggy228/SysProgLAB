def func_a():
    return 10
    
def func_b(a,b):
    return func_a()+a-b
    
def main():
    a = func_a()
    c =  20 
    c += func_a()+func_b(c,a)
    return c