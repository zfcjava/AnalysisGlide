package com.javahe.jandroidglide.load.engine.bitmap_recycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zfc on 2017/12/28.
 * 将分组后的元素进行读写
 */

public class GroupedLinkedHashMap<K extends Poolable, V> {
    private final LinkedEntry<K, V> head = new LinkedEntry<>();
    private final Map<K, LinkedEntry<K, V>> keyToEntry = new HashMap<>();

    public void put(K key, V value) {
        LinkedEntry<K, V> entry = keyToEntry.get(key);
        if (entry == null) {
            entry = new LinkedEntry<>();
            makeTail(entry);
            keyToEntry.put(key, entry);
        } else {
            //TODO 这里是怎么个逻辑？？
            key.offer();
        }
        entry.add(value);
    }

    /**
     * 这是一个环形链表，增删都是根据head进行操作
     * @param entry
     */
    private void makeTail(LinkedEntry<K, V> entry) {
        removeEntry(entry);
        //通过与head交互完成head的后移操作
        head.pre = entry.pre;
        entry.next = head;
        updateEntry(entry);
    }

    /**
     * 这是一个环形链表，增删都是根据head进行操作
     * @param entry
     */
    private void makeHead(LinkedEntry<K, V> entry) {
        removeEntry(entry);
        entry.next = head.next;
        entry.pre = head;
        updateEntry(entry);
    }

    /**
     * 更新以entry基础，将另一部分链表关联上
     * @param entry
     * @param <K>
     * @param <V>
     */
    private static <K, V> void updateEntry(LinkedEntry<K, V> entry) {
        entry.pre.next = entry;
        entry.next.pre = entry;
    }

    /**
     * 执行此操作后，链表中没有对象指向entry，只有entry指向了它的前驱和后继
     * @param entry
     * @param <K>
     * @param <V>
     */
    private static <K, V> void removeEntry(LinkedEntry<K, V> entry) {
        entry.pre.next = entry.next;
        entry.next.pre = entry.pre;
    }


    /**
     * 内置了一个V的集合
     *
     * @param <K>
     * @param <V>
     */
    private static class LinkedEntry<K, V> {
        private final K key;
        private List<V> values;
        //前驱和后继, 这是做什么用的
        //答：前驱和后继是服务于外部的
        LinkedEntry<K, V> pre;
        LinkedEntry<K, V> next;

        public LinkedEntry() {
            this(null);
        }

        public LinkedEntry(K key) {
            next = pre = this;
            this.key = key;
        }

        public V removeLast() {
            final int valueSize = size();
            return valueSize > 0 ? values.remove(valueSize - 1) : null;
        }

        public int size() {
            return values == null ? 0 : values.size();
        }


        public void add(V value) {
            if (values == null) {
                values = new ArrayList<>();
            }
            values.add(value);
        }
    }
}
