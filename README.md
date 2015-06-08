# Introduction

[![Build Status](https://travis-ci.org/zeeke/bean-trace.svg?branch=master)](https://travis-ci.org/zeeke/bean-trace)
[![Coverage Status](https://coveralls.io/repos/zeeke/bean-trace/badge.svg?branch=master)](https://coveralls.io/r/zeeke/bean-trace?branch=master)
[![Download](https://api.bintray.com/packages/zeeke/maven/bean-trace/images/download.svg) ](https://bintray.com/zeeke/maven/bean-trace/_latestVersion)

Bean Traces allows you to print in a user friendly manner an object and all its references.
This practice can be usefule in several situations:
 - **Documentation**: Show collaborators how a system is made up. Using an Inversion Of Control framework
     brings to architectures with many small collaborators. The best diagram you can draw is an automated one
     with runtime objects.
 - **Debug**: Find what is going wrong with the architecture by drawing down an object graph.

# Example

Below is a simple usage example.

```java

public static class A {

    private B b;

    public A(B b) {
        this.b = b;
    }
}

public static class B {
    private String data;

    public B(String data) {
        this.data = data;
    }
}

B b = new B("A string data")
A a = new A(b);

BeanTraces.printBeanTrace(a)

// Output:
//    A
//       `-- b : B



```

# Renderers
TBD

# License
TBD
