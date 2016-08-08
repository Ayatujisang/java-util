
#### java自带的优先级队列
* 不需要定义堆size
##### 最大、小堆：java.util.PriorityQueue
```
最大堆
PriorityQueue<Integer> heap = new PriorityQueue<Integer>(new Comparator<Integer>() {
                public int compare(Integer a0, Integer a1) {
                   return a1-a0;
                }
            });


最小堆
PriorityQueue<Integer> heap = new PriorityQueue<Integer>(new Comparator<Integer>() {
                public int compare(Integer a0, Integer a1) {
                    return a0 - a1;
                }
            });
```

#### PriorityQueue 自己实现的最大、小堆
* 需要定义堆size