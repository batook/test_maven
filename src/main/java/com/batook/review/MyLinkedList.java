package com.batook.review;

public class MyLinkedList {
    public static void main(String[] args) {
        LnkdLst list = new LnkdLst();
        for (int i = 1; i <= 10; i++)
            list.addNode(new Node(String.valueOf(i)));
        list.iterate();

        Node fast = list.header;
        Node slow = list.header;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        System.out.println("Middle=" + slow);
        Node curr = list.header;
        Node res = list.header;
        int i = 0;
        while (curr.next != null) {
            i++;
            if (i >= 3) res = res.next;
            curr = curr.next;
        }
        System.out.println(res);
        list.reverseOrder();
        list.iterate();
    }
}

class Node {
    Node next;
    String data;

    public Node(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }
}

class LnkdLst {
    Node header;
    Node current;

    LnkdLst() {
        header = new Node("HDR");
        current = header;
    }

    void addNode(Node node) {
        current.next = node;
        current = node;
    }

    void iterate() {
        Node curr = header;
        System.out.print(curr);
        while (curr.next != null) {
            System.out.print(" -> " + curr.next);
            curr = curr.next;
        }
        System.out.println();
    }

    void reverseOrder() {
        Node curr = header;
        Node prev = null;
        while (curr != null) {
            Node tmp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = tmp;
        }
        header = prev;
    }
}