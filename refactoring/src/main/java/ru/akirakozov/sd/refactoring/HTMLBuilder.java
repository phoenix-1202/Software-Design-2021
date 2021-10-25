package ru.akirakozov.sd.refactoring;

public class HTMLBuilder {
    public static String tagWrap(String tag, String text) {
        return String.format("<%s>%s</%s>", tag, text, tag);
    }

    public static String namePriceWrite(String name, int price) {
        return String.format("%s\t%d</br>", name, price);
    }

    public static String createHTML(String... phrases) {
        StringBuilder sb = new StringBuilder();
        for (String phrase: phrases)
            sb.append(phrase);
        return tagWrap("html", tagWrap("body", sb.toString()));
    }
}
