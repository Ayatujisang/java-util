
#### java自带的优先级队列

##### 最大、小堆：java.util.PriorityQueue
```
PriorityQueue<Integer> heap = new PriorityQueue<Integer>(new Comparator<Integer>() {
    public int compare(Integer a0, Integer a1) {
        if(a0>a1){
            return -1;
        }else if(a0<a1){
            return 1;
        }
        return 0;
    }
});
```

#### PriorityQueue 自己实现的最大、小堆