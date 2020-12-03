def func_add(a,b):
    return a+b
    
def func_sub(a,b,c):
    return a-b-c
    
def main():
    a = 5
    c = 1
    c += func_add(a,4)-func_sub(8,1,a)
    return c