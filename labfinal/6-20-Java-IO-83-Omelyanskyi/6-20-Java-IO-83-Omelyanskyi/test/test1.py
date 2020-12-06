def main():
    a = 5
    while a<10:
        a += 3
        continue
        a = 99
    while a<30:
        a += 3
        break
        a = 99
    return ~a