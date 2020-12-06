def func_a(a,b):
    return a+b
    
def main():
    a = func_a(12)
    c =  20 
    c += func_a(a,10)-a
    return c