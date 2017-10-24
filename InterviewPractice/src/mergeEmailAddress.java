

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 合并邮件列表（后来才知道也是个面经题）
Given 1 million email list:
list 1: a@a.com, b@b.com
list 2: b@b.com, c@c.com
list 3: e@e.com
list 4: a@a.com
...
Combine lists with identical emails, and output tuples:
(list 1, list 2, list 4) (a@a.com, b@b.com, c@c.com)
(list 3) (e@e.com)
 
 
Solution:
1. 假定每个集合编号为0，1，2，3... 
2. 创建一个hash_map，key为字符串，value为一个链表，链表节点为字符串所在集合的编号。遍历所有的集合，将字符串和对应的集合编号插入到hash_map中去。 
3. 创建一个长度等于集合个数的int数组，表示集合间的合并关系。例如，下标为5的元素值为3，表示将下标为5的集合合并到下标为3的集合中去。开始时将所有值
都初始化为自身，表示集合间没有互相合并。在集合合并的过程中，我们将所有的字符串都合并到编号较小的集合中去。遍历第二步中生成的hash_map，对于每个value
中的链表，首先找到最小的集合编号（有些集合已经被合并过，需要顺着合并关系数组找到合并后的集合编号），然后将链表中所有编号的集合都合并到编号最小的集
合中（通过更改合并关系数组）。 
4.现在合并关系数组中值为自身的集合即为最终的集合，它的元素来源于所有直接或间接指向它的集合。 
例子:
    0: {aaa bbb ccc} 
    1: {bbb ddd} 
    2: {eee fff} 
    3: {ggg} 
    4: {ddd hhh} 
    生成的hash_map，和处理完每个值后的合并关系数组分别为 
    aaa: 0            
    bbb: 0, 1         
    ccc: 0           
    ddd: 1, 4        
    eee: 2            
    fff: 2           
    ggg: 3            
    hhh: 4           
    所以合并完后有三个集合，第0，1，4个集合合并到了一起， 
    第2，3个集合没有进行合并。
*/

public class mergeEmailAddress {
	
	public List<Set<String>> mergeEmails(String[][] emails) {
		List<Set<String>> result = new ArrayList<Set<String>>();
		if(emails == null || emails.length == 0 || emails[0].length == 0) {
			return result;
		}
		
		int m = emails.length;
		int[] parent = new int[m]; // initialize parent array, at the begining, each list is in its own group
		for(int i=0; i<m; i++) {
			parent[i] = i;
		}
		
		// key: email, value: List of set numbers that email belongs to
		Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		for(int i=0; i<m; i++) {
			for(String email : emails[i]) {
				if(!map.containsKey(email)) {
					List<Integer> list = new ArrayList<Integer>();
					list.add(i);
					map.put(email, list);
				} else {
					map.get(email).add(i);
				}
			}
		}
		
		// union
		for(List<Integer> list : map.values()) {
			// list is already in order 
			for(int i=0; i<list.size(); i++) {
				// make larger parent node point to small parent node
				union(parent, list.get(0), list.get(i));
			}
		}
		
		
		HashMap<Integer, Set<String>> hash = new HashMap<Integer, Set<String>>();
		//initialization
		for(int i=0; i<m; i++) {
			hash.put(i, new HashSet<String>());
		}
		for(int i=0; i<m; i++) {
			if(i != parent[i]) {
				int p = parent[i];
				if(hash.get(p).isEmpty()) {
					hash.get(p).addAll(Arrays.asList(emails[p])); 
				}
				hash.get(p).addAll(Arrays.asList(emails[i]));
			} else {   // 如果parent的值指向自身，说明是单独的集合，直接加入
				hash.get(i).addAll(Arrays.asList(emails[i]));
			}
		}
		
		for(Set<String> set : hash.values()) {
			if(!set.isEmpty()) {
				result.add(set);
			}
		}
		return result;
	}
	
	public void union(int[] parent, int x, int y) {
		int parentX = getParent(parent, x);
		int parentY = getParent(parent, y);
		if(parentX < parentY) {
			parent[parentY] = parentX;
		} else if(parentX > parentY) {
			parent[parentX] = parentY;
		}
	}
	
	public int getParent(int[] parent, int x) {
		while(x != parent[x]) {
			parent[x] = parent[parent[x]];
			x = parent[x];
		}
		return x;
	}
	
	public static void main(String[] args) {
		String[] e0 = {"a@a.com", "b@b.com"};
		String[] e1 = {"b@b.com", "c@c.com"};
		String[] e2 = {"e@e.com"};
		String[] e3 = {"a@a.com"};
		String[][] emails = {e0, e1, e2, e3};
		mergeEmailAddress test = new mergeEmailAddress();
		List<Set<String>> result = test.mergeEmails(emails);
		for(Set<String> set : result) {
			System.out.println(set);
		}
	}
}