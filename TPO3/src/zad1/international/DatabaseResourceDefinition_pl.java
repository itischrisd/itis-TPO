package international;

import java.util.ListResourceBundle;

public class DatabaseResourceDefinition_pl extends ListResourceBundle {

    static final Object[][] contents = {
            {"charset", "ISO-8859-2"},
            {"header", new String[]{"Baza danych książek"}},
            {"param_clause", "Wyszukaj książkę po tytule (puste dla wszystkich książek):"},
            {"submit", "Pokaż wyniki wyszukiwania"},
            {"footer", new String[]{}},
            {"statusMessage", new String[]{"Znaleziono:", "Brak bazy", "Błąd SQL", "Brak wyników"}},
            {"resultDescriptions", new String[]{"Tytuł", "Autor", "Rok wydania", "ISBN"}}
    };

    @Override
    public Object[][] getContents() {
        return contents;
    }
}
