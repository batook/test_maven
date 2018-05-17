package com.batook.test;

public class LnkdListTest {
    public static void main(String[] args) {
        LnkdList linkedList = new LnkdList();
        for (int i = 1; i <= 10; i++)
            linkedList.addNode(new Node(Integer.toString(i)));
        linkedList.iterate();
        //
        Node current, result;
        current = linkedList.header;
        result = linkedList.header;
        int i = 0;
        while (current.next != null) {
            i++;
            if (i % 2 == 0) result = result.next;
            current = current.next;
        }
        System.out.println("Middle=" + result);
        //
        current = linkedList.header;
        result = linkedList.header;
        i = 0;
        while (current != null) {
            i++;
            current = current.next;
            if (i > 3) result = result.next;
        }
        System.out.println("3rd element from end is " + result);
        //
        Node fast, slow;
        fast = linkedList.header;
        slow = linkedList.header;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        System.out.println("Middle=" + slow);
        linkedList.reverseOrder();
        linkedList.iterate();
    }
}

class Node {
    Node next;
    String value;

    public Node(String data) {
        value = data;
    }

    public String toString() {
        return value;
    }
}

class LnkdList {
    Node header;
    Node current;

    public LnkdList() {
        header = new Node("header");
        current = header;
    }

    public void addNode(Node node) {
        current.next = node; //set current.next to new node e.g. header.next = "1"
        current = node;      //replace current node by new node e.g. header -> "1" and "1".next is null
    }

    public void iterate() {
        Node current = this.header;
        System.out.print(current);
        while (current.next != null) {
            System.out.print(" -> " + current.next);
            current = current.next;
        }
        System.out.println();
    }

    public void reverseOrder() {
        Node curr = this.header;
        Node prev = null;
        // run through the list and set first.next=null, next.next=previous
        while (curr != null) {
            Node next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        this.header = prev;
    }
}

