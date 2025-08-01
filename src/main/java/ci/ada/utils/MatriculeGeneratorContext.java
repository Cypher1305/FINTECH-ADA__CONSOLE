package ci.ada.utils;

import ci.ada.Interfaces.MatriculeStrategy;

public class MatriculeGeneratorContext {
    private MatriculeStrategy strategy;

    public MatriculeGeneratorContext(MatriculeStrategy strategy) {
        this.strategy = strategy;
    }

    public MatriculeGeneratorContext() {

    }

    public void setStrategy(MatriculeStrategy strategy) {
        this.strategy = strategy;
    }

    public String generate(String firstname, String lastname) {
        return strategy.generateMatricule(firstname, lastname);
    }
}
