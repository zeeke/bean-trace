# Bean Trace

[![Build Status](https://travis-ci.org/zeeke/bean-trace.svg?branch=master)](https://travis-ci.org/zeeke/bean-trace)
[![Coverage Status](https://coveralls.io/repos/zeeke/bean-trace/badge.svg?branch=master)](https://coveralls.io/r/zeeke/bean-trace?branch=master)
[![Download](https://api.bintray.com/packages/zeeke/maven/bean-trace/images/download.svg) ](https://bintray.com/zeeke/maven/bean-trace/_latestVersion)

## Introduction

Bean Traces allows you to print in a user friendly manner an object and all its references.
This practice can be usefule in several situations:
 - **Documentation**: Show collaborators how a system is made up. Using an Inversion Of Control framework
     brings to architectures with many small collaborators. The best diagram you can draw is an automated one
     with runtime objects.
 - **Debug**: Find what is going wrong with the architecture by drawing down an object graph.

## Getting Started

Add the dependendencies to your build tool.
For Gradle users:
```
repositories {
    jcenter()
}

dependencies {
    classpath 'org.laborra:bean-trace:0.1.+'
}
```

And use the main entry point:

```java
List<String> subject = new LinkedList<>();
subject.add("one");
subject.add("two");

BeanTraces.printBeanTrace(subject);

// Output
LinkedList
   |-- 1 : two
   `-- 0 : one
```

## How it works

The library accept a starting object and visits all its references, recursively and creating an
internal data structure representing the object graph. Then a configurable renderer puts the data
 on paper.

## Renderers

The library ships with some default renders that are described below.

### ASCII Renderer

The ASCII renderer is the default one and it render the object graph
using ASCII characters:

```
SubjectClass
   |-- primitiveField1 : value1
   ...
   |-- primitiveFieldN : valueN
   |-- referenceField1 : Class1
   |      `-- fields_of_referenced_objects
   |-- referenceField2 : Class2
   ...
   `-- referenceFieldM : ClassM

```

The renderer can be configured with a custom Appendable, allowing printing to custom logger, strings or standart output.


#### Example

### Graphviz

A bean trace can be dumped in a text file using the [Graphviz](http://www.graphviz.org/) dot format and transformed later in an image.

The following code snippet dumps a mocks built with [Mockito](http://mockito.org/):
```java`
Runnable subject = Mockito.mock(Runnable);
File outputFile = new File("output.dot");
BeanTraces.printBeanTrace(subject, GraphRenderers.newGraphviz(outputFile));
```
and here is the result:

![Mockito Graphviz](http://zeeke.github.io/bean-trace/img/mockito_graphviz.png)

### D3JS Renderer

TBD

## TODOs

- [x] Enable scanning of primitive arrays
- [x] Rendering of primitive types
- [ ] Improve rendering of ASCII graphs
- [ ] Put real examples in README
- [x] Object scanner exclusion pattern

## License
TBD
