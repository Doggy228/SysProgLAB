def main():
    a = 5
    b = 3 if a<3 else a+('A'-a)-'A' if 4<a else 9 
    return not b