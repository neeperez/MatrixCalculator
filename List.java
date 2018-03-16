/* CMPS 101, Programming Assignment 3
 * Nelson Perez
 * CruzID:neeperez 
 */

//List.java is the ADT that will be used to sort the lines of the given 
//file

import java.util.*;
import java.io.*;

class List {
	//Here we create the Node class that will be used in the List ADT
	private class Node {
		Object data;
		Node previous;
		Node next;

		Node(Object data){
			this.data = data;
			this.previous = null;
			this.next = null;
		}

		public String toString(){
			return String.valueOf(data);
		}

		@Override
		public boolean equals(Object x){
			//System.out.println("Node equals");
			if(x==this){
				return true;
			} 
			if(!(x instanceof Node)){
				return false;
			}

			Node that = (Node) x;
			boolean eq = (data.equals(that.data));
			return eq;
		}
	}

	private Node front;
	private Node back;
	private Node cursor;
	private int length;
	private int index;

	List(){
		front = null;
		back = null;
		cursor = null;
		length = 0;
		index = -1;
	}

	// Returns the number of elements in this List.
	int length(){
		return length;
	}
	// If cursor is defined, returns the index of the cursor element,
 	// otherwise returns -1
	int index(){
		if(cursor != null){
			return index;
		} else {
			return -1;
		}
	}

	// Returns front element. Pre: length()>0
	Object front(){
		if(length <= 0){
			return null;			
		} 
		return front.data;
	}

	// Returns back element. Pre: length()>0
	Object back(){
		if(length <= 0){
			return null;
		}
		return back.data;
	}

	// Returns cursor element. Pre: length()>0, index()>=0
	Object get(){
		if (index >= 0 && length > 0){
			return cursor.data;
		} else {
			return null;
		}
	}

	 // Returns true if and only if this List and L are the same
 	// integer sequence. The states of the cursors in the two Lists
 	// are not used in determining equality.
 	@Override
	public boolean equals(Object x){
		if(!(x instanceof List)) {
				//System.out.println("The parameter is not a List");
			return false;
		} else {
			List L = (List) x;
			boolean eq = false;
			Node this_current = this.front;
			Node l_current = L.front;
			eq = (this.length == L.length);
			//if(!eq)
				//System.out.println("The lengths are unequal");
			while(eq && this_current != null){
				eq = this_current.equals(l_current);
				//if(!eq)
					//System.out.println("One of the nodes are mismatched");
				this_current = this_current.next;
				l_current = l_current.next;
			}
			return eq;
		}
		
	}

	// Resets this List to its original empty state.
	void clear(){
		front = null;
		back = null;
		cursor = null;
		length = 0;
		index = -1;
	}

	// If List is non-empty, places the cursor under the front element,
 	// otherwise does nothing.
	void moveFront(){
		if(length > 0){
			cursor = front;
			index = 0;
		} else{
			return;
		}
	}

	// If List is non-empty, places the cursor under the back element,
 	// otherwise does nothing.
	void moveBack(){
		if(length > 0){
			cursor = back;
			index = length - 1;
		} else {
			return;
		}
	}

	// If cursor is defined and not at front, moves cursor one step toward
 	// front of this List, if cursor is defined and at front, cursor becomes
	// undefined, if cursor is undefined does nothing.
	void movePrev(){
		if(cursor != null && !cursor.equals(front)){
			cursor = cursor.previous;
			index = index - 1;
		} else if(cursor != null && cursor.equals(front)){
			cursor = null;
			index = -1;
		} else {
			return;
		}
	}

	// If cursor is defined and not at back, moves cursor one step toward
 	// back of this List, if cursor is defined and at back, cursor becomes
 	// undefined, if cursor is undefined does nothing
	void moveNext(){
		if(cursor != null && !cursor.equals(back)){
			cursor = cursor.next;
			index = index + 1;
		} else if(cursor != null && cursor.equals(back)){
			cursor = null;
			index = -1;
		} else {
			return;
		}
	}

	// Insert new element into this List. If List is non-empty,
 	// insertion takes place before front element.
	void prepend(Object data){
		if (length == 0){
			front = back = new Node(data);
			length++;
		} else if (length > 0){
			Node insert = new Node(data);
			insert.next = front;
			insert.previous = null;
			front.previous = insert;
			front = insert; 
			index = index + 1;
			length++;
		} else {
			return;
		}
	}

	// Insert new element into this List. If List is non-empty,
 	// insertion takes place after back element.
	void append(Object data){
		if (length == 0){
			front = back = new Node(data);
			length++;
		} else if (length > 0){
			Node insert = new Node(data);
			insert.next = null;
			insert.previous = back;
			back.next = insert;
			back = insert;
			length++;
		} else {
			return;
		}
	}

	// Insert new element before cursor.
	// Pre: length()>0, index()>=0
	void insertBefore(Object data){
		if(length <= 0 && index < 0){
			return;
		} else if(index == 0){
			this.prepend(data); //check if this is legal
		} else {
			Node insert = new Node(data);
			insert.next = cursor;
			insert.previous = cursor.previous;
			cursor.previous.next = insert;
			cursor.previous = insert;
			index = index + 1;
			length++;
		}
	}

	// Inserts new element after cursor.
 	// Pre: length()>0, index()>=0
	void insertAfter(Object data){
		if(length <= 0 && index < 0){
			return;
		} else if(index == length - 1){
			this.append(data); //Also check if this is legal
		} else {
			Node insert = new Node(data);
			insert.next = cursor.next;
			insert.previous = cursor;
			cursor.next.previous = insert;
			cursor.next = insert;
			length++;
		}
	}

	// Deletes the front element. Pre: length()>0
	void deleteFront(){
		if (length <= 0){
			return;
		} else if(length > 1){
			/*if(index != -1 && cursor.equals(front)){
				index = -1;
				cursor = null;
			}*/
			if(cursor != null && cursor.equals(front)){
				cursor = null;
			}
			front = front.next;
			front.previous = null;
		} else{
			front = back = null;
		}
		index--;
		length--;
	}

	// Deletes the back element. Pre: length()>0
	void deleteBack(){
		if (length <= 0){
			return;
		} else if (length > 1){
			/*if(index != -1 && cursor.equals(back)){
				index = -1;
				cursor = null;
			}*/
			if(cursor != null && back.equals(cursor)){
				index = -1;
				cursor = null;
			}
			back = back.previous;
			back.next = null;
		} else {
			front = back = null;
		}
		length--;
	}

	// Deletes cursor element, making cursor undefined.
 	// Pre: length()>0, index()>=0
	void delete(){
		if (length > 0 && index >= 0){
			if(index == 0){
				this.deleteFront();
				return;
			} else if (index == length - 1){
				this.deleteBack();
				return;
			} else {
				cursor.previous.next = cursor.next;
				cursor.next.previous = cursor.previous;
				cursor.previous = null;
				cursor.next = null;
				index = -1;
				cursor = null;
			}
			length--;
		} else {
			return;
		}
	}

	// Overrides Object's toString method. Returns a String
 	// representation of this List consisting of a space
	// separated sequence of integers, with front on left.
	public String toString(){
		Node traverse = front;
		StringBuffer list = new StringBuffer();
		if(front != null){
			list.append(traverse.toString());
			traverse = traverse.next;
		}
		while (traverse != null){
			list.append(" ");
			list.append(traverse.toString());
			traverse = traverse.next;
		}
		return new String(list);
	}

}
