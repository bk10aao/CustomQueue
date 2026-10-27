# Queue
Implementation of a Queue using an array, a singly LinkedList version can be found [here](https://github.com/bk10aao/CustomQueueV2). 

# Time Complexity

| Method                          |   Complexity   |
|:--------------------------------|:--------------:|
| **`Constructor()`**             |     $O(1)$     |
| **`Constructor(int capacity)`** |     $O(1)$     |
| **`Constructor(Collection)`**   |     $O(N)$     |
| **`add(E)`**                    |     $O(N)$     |
| **`offer(E)`**                  |     $O(N)$     |
| **`addAll(Collection)`**        |     $O(N)$     |
| **`poll()`**                    |     $O(1)$     |
| **`remove()`**                  |     $O(1)$     |
| **`peek()`**                    |     $O(1)$     |
| **`element()`**                 |     $O(1)$     |
| **`size()`**                    |     $O(1)$     |
| **`isEmpty()`**                 |     $O(1)$     |
| **`clear()`**                   |     $O(N)$     |
| **`contains(Object)`**          |     $O(N)$     |
| **`containsAll(Collection)`**   |   $O(N + M)$   |
| **`remove(Object)`**            |     $O(N)$     |
| **`removeAll(Collection)`**     | $O(N \cdot M)$ |
| **`retainAll(Collection)`**     | $O(N \cdot M)$ |
| **`iterator()`**                |     $O(1)$     |
| **`toArray()`**                 |     $O(N)$     |
| **`toArray(T[])`**              |     $O(N)$     |
| **`toString()`**                |     $O(N)$     |

# Space Complexity

| Operation                       | Complexity |
|:--------------------------------|:----------:|
| **`Constructor()`**             |   $O(1)$   |
| **`Constructor(int capacity)`** |   $O(N)$   |
| **`Constructor(Collection)`**   |   $O(N)$   |
| **`add(E)`**                    |   $O(N)$   |
| **`offer(E)`**                  |   $O(N)$   |
| **`addAll(Collection)`**        |   $O(N)$   |
| **`poll()`**                    |   $O(1)$   |
| **`remove()`**                  |   $O(1)$   |
| **`peek()`**                    |   $O(1)$   |
| **`element()`**                 |   $O(1)$   |
| **`size()`**                    |   $O(1)$   |
| **`isEmpty()`**                 |   $O(1)$   |
| **`clear()`**                   |   $O(1)$   |
| **`contains(Object)`**          |   $O(1)$   |
| **`containsAll(Collection)`**   |   $O(M)$   |
| **`remove(Object)`**            |   $O(1)$   |
| **`removeAll(Collection)`**     |   $O(M)$   |
| **`retainAll(Collection)`**     |   $O(M)$   |
| **`iterator()`**                |   $O(1)$   |
| **`toArray()`**                 |   $O(N)$   |
| **`toArray(T[])`**              |   $O(N)$   |
| **`toString()`**                |   $O(N)$   |