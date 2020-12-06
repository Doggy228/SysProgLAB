def func_add(a,b):
    return a+b
    
def func_sub(a,b,c):
    return a-b-c

def func_not(a):
    return not a
    
def main():
    a = "12"
    c =  '1' if a<124 else '2' 
    c += func_add(a,214)-func_sub(0xFF,'3',a)+func_not(0)
    return c