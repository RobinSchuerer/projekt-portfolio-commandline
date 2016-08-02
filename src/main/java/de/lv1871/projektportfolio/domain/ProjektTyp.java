package de.lv1871.projektportfolio.domain;

public enum ProjektTyp {

    MUSS_PROJEKT, PRODUKT_PROJEKT, STRATEGISCHES_PROJEKT;

    public static ProjektTyp parse(String typValue) {
        switch (typValue) {
            case "must-have": return MUSS_PROJEKT;
            case "product": return PRODUKT_PROJEKT;
            case "strategic": return STRATEGISCHES_PROJEKT;
        }

        throw new RuntimeException("unbekannter Wert");
    }
}
