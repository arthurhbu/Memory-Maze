package entity;
import java.util.*;

public class RAM {

    private static RAM instancia;
    private final Map<Integer, Integer> ram;
    private static final int TAMANHO_DO_BLOCO = 5;

    public RAM() {
        ram = new HashMap<>();
    }

    public static synchronized RAM getInstancia() {
        if (instancia == null) {
            instancia = new RAM();
        }
        return instancia;
    }

    public Integer getLinha(int dado){ return ram.get(dado); }

    public void setLinha(int indice, int dado) {
        ram.put(indice, dado);
    }

    public int getIndiceBloco(int dado) {
        Integer indiceEncontrado = null;

        for (Map.Entry<Integer, Integer> entry : ram.entrySet()) {
            if (entry.getValue().equals(dado)) {
                indiceEncontrado = entry.getKey();
                break;
            }
        }
        if (indiceEncontrado == null) {
            return -1;
        }

        return (indiceEncontrado / TAMANHO_DO_BLOCO) * TAMANHO_DO_BLOCO;
    }

    public int[] getBloco(int inicioBloco){
        if(inicioBloco == -1){
            return null;
        }

        int[] bloco = new int[TAMANHO_DO_BLOCO];
        for (int i = 0; i < TAMANHO_DO_BLOCO; i++) {
            int linhaAtual = inicioBloco + i;
            bloco[i] = ram.get(linhaAtual);
        }
        return bloco;
    }

    public void updateBloco(int[] bloco, int indice) {
        for (int i = 0; i < TAMANHO_DO_BLOCO; i++) {
            setLinha(indice + i, bloco[i]);
        }
    }

    public String printMemoria() {
        StringBuilder mem = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : ram.entrySet()) {
            mem.append("Índice: ").append(entry.getKey()).append(", ").append("Chunk ").append(entry.getValue()).append("\n");
        }
        return mem.toString();
    }

}
