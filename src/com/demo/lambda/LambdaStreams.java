package com.demo.lambda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LambdaStreams {

	private static int sumStream(List<Integer> list) {
		return list.stream().filter(i -> i < 10).mapToInt(i -> i).sum();
	}


	private static int sumIterator(List<Integer> list) {
		Iterator<Integer> it = list.iterator();
		int sum = 0;
		while (it.hasNext()) {
			int num = it.next();
			if (num > 10) {
				sum += num;
			}
		}
		return sum;
	}

	@SuppressWarnings("serial")
	private static List<Integer> createList() {
		return new ArrayList<Integer>() {{
				add(1); add(2);
				add(3); add(4);
				add(5); add(6);
				add(7); add(8);
				add(9); add(10);
			}};
	}
	
	private static List<Integer> createListTwo() {
		List<Integer> list = new ArrayList<>();
		for(int i = 1; i <= 10; i++) Stream.of(i).forEachOrdered(list::add);
		return list;
	}

	/*private static void addToList(List<Integer> list, Stream<Integer> stream) {
		stream.forEachOrdered(list::add);
	}*/
	
	public static void main(String[] args) {
		System.out.println(sumStream(createList()));
		System.out.println(sumIterator(createList()));
		System.out.println(createListTwo());
		System.out.println(createListOfMap());
		System.out.println(list());
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
	public static ArrayList<Map<String,String>> createListOfMap(){
		return new ArrayList<Map<String,String>>() {{
			add(new HashMap() {{
				put("name","vikram");
			}});
			add(new HashMap() {{
				put("name","chary");
			}});
			add(new HashMap() {{
				put("name","reddy");
			}});
		}};
	}
	
	public static  List<Map<String,String>> list(){
		return createListOfMap().stream().collect(Collectors.toList());
	}
	
	
	
}
