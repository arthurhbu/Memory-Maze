package com.ag.simuladorcachegui;

import entity.Processador;

import java.util.HashMap;
import java.util.Map;

public class GerenciadorProcessadores {

    private static GerenciadorProcessadores instancia;
    private Map<String, Processador> processadores;

    private GerenciadorProcessadores() {
        processadores = new HashMap<>();

        processadores.put("Processador1", new Processador(5, "Processador1"));
        processadores.put("Processador2", new Processador(5, "Processador2"));
        processadores.put("Processador3", new Processador(5, "Processador3"));
    }

    public static synchronized GerenciadorProcessadores getInstancia() {
        if(instancia == null) {
            instancia = new GerenciadorProcessadores();
        }
        return instancia;
    }

    public Processador getProcessador(String processador) {
        return processadores.get(processador);
    }

}
