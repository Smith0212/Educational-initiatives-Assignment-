// This code demonstrates the Factory Method design pattern. It defines a common interface (Document) for different types of documents (PDF, Word, HTML),
// while providing separate factory classes (PdfDocumentFactory, WordDocumentFactory, HtmlDocumentFactory) to create these specific documents.
// The abstract DocumentFactory class defines the framework for generating documents, and each concrete factory class implements the creation of a specific document type.
// In the FactoryMethodPatternDemo, the program cycles through different factories to create and save documents in different formats.


package com.designpatterns.creational;

interface Document {
    void create();
    void save();
}

class PdfDocument implements Document {
    @Override
    public void create() {
        System.out.println("Creating PDF document");
    }

    @Override
    public void save() {
        System.out.println("Saving PDF document");
    }
}

class WordDocument implements Document {
    @Override
    public void create() {
        System.out.println("Creating Word document");
    }

    @Override
    public void save() {
        System.out.println("Saving Word document");
    }
}

class HtmlDocument implements Document {
    @Override
    public void create() {
        System.out.println("Creating HTML document");
    }

    @Override
    public void save() {
        System.out.println("Saving HTML document");
    }
}

abstract class DocumentFactory {
    public abstract Document createDocument();

    public void generateDocument() {
        Document document = createDocument();
        document.create();
        document.save();
    }
}

class PdfDocumentFactory extends DocumentFactory {
    @Override
    public Document createDocument() {
        return new PdfDocument();
    }
}

class WordDocumentFactory extends DocumentFactory {
    @Override
    public Document createDocument() {
        return new WordDocument();
    }
}

class HtmlDocumentFactory extends DocumentFactory {
    @Override
    public Document createDocument() {
        return new HtmlDocument();
    }
}

public class FactoryMethodPatternDemo {
    public static void main(String[] args) {
        DocumentFactory[] factories = {
            new PdfDocumentFactory(),
            new WordDocumentFactory(),
            new HtmlDocumentFactory()
        };

        for (DocumentFactory factory : factories) {
            System.out.println("Using " + factory.getClass().getSimpleName());
            factory.generateDocument();
            System.out.println("------------------------");
        }
    }
}