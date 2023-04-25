package model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

public class PostCenter implements Serializable{
	private static final long serialVersionUID = 1L;
	private LinkedList<Post> postList;	

	public PostCenter() {
		postList = new LinkedList<Post>();
	}
	
	public void display() {
		postList.forEach(post -> System.out.println(post.toString()));
	}
	
	public void addPost(Post post) {
		postList.add(post);
	}
	
	public boolean removePost(Post post) {
		return postList.remove(post);
	}
	
	public boolean removePost(UUID uuid) {
		Iterator<Post> itr = postList.iterator();
		while(itr.hasNext()) {
			Post post = itr.next();
			if(post.getUuid().equals(uuid)) {
				postList.remove(post);
				return true;
			}
		}
		return false;
	}

	public Post getPost(UUID uuid) {
		Iterator<Post> itr = postList.iterator();
		while(itr.hasNext()) {
			Post post = itr.next();
			if(post.getUuid().equals(uuid)) {
				return post;
			}
		}
		return null;
	}
	
	public LinkedList<Post> getPosts(){
		return postList;
	}
	
	public LinkedList<Post> searchByTopic(String topic) {
		LinkedList<Post> temp = new LinkedList<Post>();
		Iterator<Post> itr = postList.iterator();
		while(itr.hasNext()) {
			Post post = itr.next();
			if(post.getTopic().toLowerCase().equals(topic.toLowerCase())) {
				temp.add(post);
			}
		}
		return temp;
	}
	
	public LinkedList<Post> searchByTitle(String title) {
		LinkedList<Post> temp = new LinkedList<Post>();
		Iterator<Post> itr = postList.iterator();
		while(itr.hasNext()) {
			Post post = itr.next();
			if(post.getTitle().toLowerCase().equals(title.toLowerCase())) {
				temp.add(post);
			}
		}
		return temp;
	}
	
	public LinkedList<Post> searchByLikes() {
		//Creates Deep Copy of postList with shallow copies of posts. This allows us to modify order of this temp list without
		//modifying order of PostCenter "postList". However, the Post objects inside the temp list are still shallow copies.
		LinkedList<Post> temp = new LinkedList<Post>(postList);	
		//Collections.sort uses a modified MergeSort algorithm, so the time complexity of sorting the list by likes is O(nlogn)
		Collections.sort(temp, (p1, p2) -> (int)(p1.getLikes().compareTo(p2.getLikes())));
		return temp;
	}
	
	@Override
	public String toString() {
		String  s = "";	
		Iterator<Post> itr = postList.iterator();
		while(itr.hasNext()) {
			s += itr.next().toString();
		}
		return s;
	}
	
}
