package com.data_structure.collection.stack;

import java.util.*;

public class StackList<T> implements Stack<T> {

	private List<T> list; // 用容器实现

	StackList() {
		list = new ArrayList<T>();
	}
	// 弹栈
	public T pop() {
		if (this.isEmpty() == true) {
			throw new EmptyStackException();
		}

		return list.remove(list.size() - 1);
	}
	// 压栈
	public void push(T element) {
		list.add(element);
	}
	// 判断是否为空
	public boolean isEmpty() {
		return list.size() == 0;
	}
	// 返回栈顶元素
	public T peek() {
		if (this.isEmpty() == true) {
			throw new EmptyStackException();
		}
		return list.get(list.size() - 1);
	}
}