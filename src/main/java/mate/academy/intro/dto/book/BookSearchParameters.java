package mate.academy.intro.dto.book;

public record BookSearchParameters(
        String[] authors,
        String[] titles) {
}
