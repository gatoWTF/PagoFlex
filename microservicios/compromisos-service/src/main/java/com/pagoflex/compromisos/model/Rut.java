package com.pagoflex.compromisos.model;

public final class Rut {

    private Rut() {
    }

    public static String clave(String rut) {
        return rut == null ? "" : rut.trim().toUpperCase().replace(".", "").replace("-", "");
    }
}
