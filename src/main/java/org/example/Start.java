package org.example;

public class Start {
    // use separate starter class that does not inherit from javafx.application.Application
    // for working properly with maven package
    public static void main(String[] args) {
        App.launch(args);
    }
}
