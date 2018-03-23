package com.batook.test;

public class LnkdListTest {
    public static void main(String[] args) {
        LnkdList linkedList = new LnkdList(new Node("0"));
        for (int i = 1; i <= 10; i++)
            linkedList.add(new Node(Integer.toString(i)));
        linkedList.iterate();
        //
        Node current, middle;
        current = linkedList.head;
        middle = current;
        int pos = 0;
        while (current.next != null) {
            pos++;
            current = current.next;
            if (pos % 2 == 0) middle = middle.next;
        }
        System.out.println("Middle=" + middle);
        linkedList.reverse();
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
    Node head;
    Node tail;

    public LnkdList() {
        this(new Node("head"));
    }

    public LnkdList(Node node) {
        head = node;
        tail = head;
    }

    public void add(Node node) {
        tail.next = node;
        tail = node;
    }

    public void iterate() {
        Node current = this.head;
        System.out.print(current);
        while (current.next != null) {
            System.out.print(" -> " + current.next);
            //System.out.print("[" + current + " -> " + current.next + "]");
            current = current.next;
        }
        System.out.println();
    }

    public void reverse() {
        Node current = this.head;
        Node prev = null;
        Node next;
        // run through the list and set first.next=null, next.next=previous
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        this.head = prev;
    }
}

