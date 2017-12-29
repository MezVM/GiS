package Djikstra;

class NoStateFoundException extends Exception{
    NoStateFoundException(int id) {
        super(String.format("Nie znaleziono stanu dla wierzchołka o nr. [%d]",id));
    }

    NoStateFoundException(NoStateFoundException e) {
        super(e);
    }
}
