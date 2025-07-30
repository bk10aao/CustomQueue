# Queue
Implementation of a Queue using an array

# Methods

1. `CustomQueue()` - defailt constructor.
2. `CustomQueue(int size)` - constructor with intial size.
3. `boolean enqueue (E item)` - add item to queue, returns boolean if successful, throws `NullPointerException` with null parameter.
4. `offer(E item)` - add item to queue, returns boolean if successful, throws `NullPointerException` with null parameter.
5. `boolean remove()` - removes first element in queue, throws `NullPointerException` if item is null.
6. `poll()` - removes first element from queue.
7. `getSize()` - returns number of items in queue as an integer.
8. `getQueueSize()` - returns the size of the queue including null elements.
