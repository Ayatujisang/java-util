package com.kk.priorityqueue;


/**
 * 优先级队列实现
 *
 * @param <T>
 */
public abstract class PriorityQueue<T> {
    private int size;
    private final int maxSize;
    private final Object[] heap;

    public PriorityQueue(int maxSize) {
        this(maxSize, true);
    }

    public PriorityQueue(int maxSize, boolean prepopulate) {
        this.size = 0;
        int heapSize;
        if (0 == maxSize) {
            heapSize = 2;
        } else {
            heapSize = maxSize + 1;
        }

        Object[] h = new Object[heapSize];
        this.heap = h;
        this.maxSize = maxSize;
        if (prepopulate) {
            Object sentinel = this.getSentinelObject();
            if (sentinel != null) {
                this.heap[1] = sentinel;

                for (int i = 2; i < this.heap.length; ++i) {
                    this.heap[i] = this.getSentinelObject();
                }

                this.size = maxSize;
            }
        }

    }

    // v1-v2>=0 最大堆， v2-v1>=0 最小堆
    protected abstract boolean lessThan(T var1, T var2);

    protected T getSentinelObject() {
        return null;
    }

    public final T insert(T element) {
        return add(element);
    }

    public final T add(T element) {
        ++this.size;
        this.heap[this.size] = element;
        this.upHeap();
        return (T) this.heap[1];
    }

    public T insertWithOverflow(T element) {
        if (this.size < this.maxSize) {
            this.add(element);
            return null;
        } else if (this.size > 0 && !this.lessThan(element, (T) this.heap[1])) {
            Object ret = this.heap[1];
            this.heap[1] = element;
            this.updateTop();
            return (T) ret;
        } else {
            return element;
        }
    }

    public final T top() {
        return (T) this.heap[1];
    }

    public final T del() {
        return pop();
    }

    public final T pop() {
        if (this.size > 0) {
            Object result = this.heap[1];
            this.heap[1] = this.heap[this.size];
            this.heap[this.size] = null;
            --this.size;
            this.downHeap();
            return (T) result;
        } else {
            return null;
        }
    }

    public final T updateTop() {
        this.downHeap();
        return (T) this.heap[1];
    }

    public final int size() {
        return this.size;
    }

    public final void clear() {
        for (int i = 0; i <= this.size; ++i) {
            this.heap[i] = null;
        }

        this.size = 0;
    }

    private final void upHeap() {
        int i = this.size;
        Object node = this.heap[i];

        for (int j = i >>> 1; j > 0 && this.lessThan((T) node, (T) this.heap[j]); j >>>= 1) {
            this.heap[i] = this.heap[j];
            i = j;
        }

        this.heap[i] = node;
    }

    private final void downHeap() {
        int i = 1;
        Object node = this.heap[i];
        int j = i << 1;
        int k = j + 1;
        if (k <= this.size && this.lessThan((T) this.heap[k], (T) this.heap[j])) {
            j = k;
        }

        while (j <= this.size && this.lessThan((T) this.heap[j], (T) node)) {
            this.heap[i] = this.heap[j];
            i = j;
            j <<= 1;
            k = j + 1;
            if (k <= this.size && this.lessThan((T) this.heap[k], (T) this.heap[j])) {
                j = k;
            }
        }

        this.heap[i] = node;
    }

    protected final Object[] getHeapArray() {
        return (Object[]) this.heap;
    }

    public static void main(String[] args) {
        {
            PriorityQueue<Integer> heap = new PriorityQueue<Integer>(10) {
                @Override
                protected boolean lessThan(Integer var1, Integer var2) {
                    return var1 - var2 >= 0;
                }
            };

            heap.insert(24);
            heap.insert(2);
            heap.insert(654);
            heap.insert(28);
            heap.insert(2458);
            heap.insert(2458);
            heap.insert(242318);
            heap.insert(2487);
            while (heap.size() > 0) {
                System.out.println(heap.del());
            }
        }
        {
            PriorityQueue<Integer> heap = new PriorityQueue<Integer>(10) {
                @Override
                protected boolean lessThan(Integer var1, Integer var2) {
                    return var1 - var2 <= 0;
                }
            };

            heap.insert(10);
            heap.insert(3);
            heap.insert(6);
            while (heap.size() > 0) {
                System.out.println(heap.del());
            }
        }
    }
}
