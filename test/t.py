# a = [2305, 1168, 2580, 4871, 5659, 1821, 1074, 7115, 1620, 2428, 3943, 4750, 6975, 
# 4981, 9208]

# for _ in a:
    # print(bin(_%128), end = ' ')

# sum = 0
# for i in range(3):
#     sum += 1/(i+1)

# print(3/sum)

import numpy as np
from scipy.sparse.linalg import svds

A = np.eye(5)
U, S, VT = svds(A, k=6)

print(U)
print(S)
print(VT)