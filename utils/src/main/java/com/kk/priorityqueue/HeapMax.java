package com.kk.priorityqueue;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 最大堆
 */
public class HeapMax {
    int maxSize;
    int[] heap;
    int len;// 堆

    public HeapMax(int maxSize) {
        heap = new int[maxSize + 1];
        len = 0;
        this.maxSize = maxSize;
    }

    void insert(int x) {
        heap[++len] = x;// 先插入
        int i = len;
        while (i > 1 && x > heap[i / 2])// 再调整
        {
            heap[i] = heap[i / 2];// 较大元素上浮操作
            i /= 2;
        }
        heap[i] = x;
    }

    int del() // 删除堆顶最大元素
    {
        int oval = heap[1];
        int i = 2, j = 1;// j表示父节点，i表示子节点
        while (i < len) {
            boolean tp = heap[i + 1] > heap[i];
            i += tp ? 1 : 0;// i表示heap[i+1]，heap[i]较大的元素的序号
            if (heap[len] > heap[i])
                break;
            heap[j] = heap[i];// 元素上浮
            j = i;
            i *= 2;// 往下延伸,继续上浮
        }
        heap[j] = heap[len--];// 空缺一个，用最小的heap[len]补缺
        return oval;
    }

    public int top() {
        return heap[1];
    }

    public int size() {
        return len;
    }

    public static void main(String[] args) {
        {
            HeapMax heap = new HeapMax(100);
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
            PriorityQueue<Integer> heap = new PriorityQueue<Integer>(new Comparator<Integer>() {
                public int compare(Integer a0, Integer a1) {
                    if (a0 > a1) {
                        return -1;
                    } else if (a0 < a1) {
                        return 1;
                    }
                    return 0;
                }
            });
            heap.offer(24);
            heap.offer(2);
            heap.offer(654);
            heap.offer(28);
            heap.offer(2458);
            heap.offer(2458);
            heap.offer(242318);
            heap.offer(2487);
            while (heap.size() > 0) {
                System.out.println(heap.poll());
            }
        }

    }
}
