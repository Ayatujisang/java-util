
#### java自带的优先级队列
* jdk1.8 不需要定义堆size，1.6需要指定初始值大小，但是当数量到了size值后会自动扩容
##### 最大、小堆：java.util.PriorityQueue
```
最大堆
PriorityQueue<Integer> heap = new PriorityQueue<Integer>(n, new Comparator<Integer>() {
                public int compare(Integer a0, Integer a1) {
                   return a1-a0;
                }
            });


最小堆
PriorityQueue<Integer> heap = new PriorityQueue<Integer>(n, new Comparator<Integer>() {
                public int compare(Integer a0, Integer a1) {
                    return a0 - a1;
                }
            });
```

#### PriorityQueue 自己实现的最大、小堆
* 需要定义堆size