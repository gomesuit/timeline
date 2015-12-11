package sample;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		list.add("aaa");
		list.add("bbb");
		list.add("ccc");
		list.add("ddd");
		System.out.println(StringUtils.join(list, ','));
	}

}
