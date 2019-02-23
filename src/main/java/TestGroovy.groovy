package com.batook


import java.util.function.Function
import java.util.stream.Collectors

def t = 'shit'
println t
println Book
println new Book("test").getTitle() + " $t"
def book = new Book("book")
println book

def myMap = [a: 1, b: 2, c: 3]
println myMap.find { e -> e.value == 3 }

def textCorpus =
        """
Look for the bare necessities
The simple bare necessities
Forget about your worries and your strife
I mean the bare necessities
Old Mother Nature's recipes
That bring the bare necessities of life
"""
def words = textCorpus.tokenize()
def wordFrequency = [:]
words.each { e -> wordFrequency[e] = wordFrequency.get(e, 0) + 1 }
println wordFrequency.findAll { e -> e.value > 3 }

def wordFrequency2 = words.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
println wordFrequency2.findAll { e -> e.value > 3 }

class Book {
    private String title

    Book(String theTitle) {
        title = theTitle
    }

    String getTitle() {
        return title
    }
}
