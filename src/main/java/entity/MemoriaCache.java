package entity;
import java.util.*;

public class MemoriaCache {

    private static final int tamanhoCache = 5;
    private final Queue<Integer> filaEndereco;
    public final Map<Integer, blocoCache> cache;
    private final Random random;

    public enum tags{
        Exclusivo, Modificado, Compartilhado, Invalido
    }


    public blocoCache getBlocoCache(int blocoEndereco){
        return cache.get(blocoEndereco);
    }

    public static class blocoCache{
        private int[] dados;
        private tags tag;
        private int indiceRAM;

        blocoCache(int[] dados, tags tag,  int indiceRAM){
            this.dados = dados;
            this.tag = tag;
            this.indiceRAM = indiceRAM;
        }

        public int[] getDados() {
            return dados;
        }
        public tags getTag() {
            return tag;
        }
        public int getIndiceRAM() {
            return indiceRAM;
        }

        public void setDados(int[] dados) {
            this.dados = dados;
        }

        public void setTag(tags tag) {
            this.tag = tag;
        }

        public void setIndiceRAM(int indiceRAM) {
            this.indiceRAM = indiceRAM;
        }
    }

    public MemoriaCache(int tamanhoCache){
        filaEndereco = new LinkedList<Integer>();
        cache = new HashMap<Integer, blocoCache>();
        random = new Random();
    }

    public void setBloco(int[] bloco, tags tag, int indiceRAM) {
        //Mapeamento aleatorio
        if (cache.size() < tamanhoCache) {
            List<Integer> posicoesVazias = new ArrayList<>();
            for (int i = 0; i < tamanhoCache; i++) {
                if (!cache.containsKey(i)) {
                    posicoesVazias.add(i);
                }
            }
            if (!posicoesVazias.isEmpty()) {
                int posicaoAleatoria = posicoesVazias.get(random.nextInt(posicoesVazias.size()));
                filaEndereco.add(posicaoAleatoria);
                cache.put(posicaoAleatoria, new blocoCache(bloco, tag, indiceRAM));
            }
        }
        //Substituição FIFO
        else {
            int enderecoAntigo = filaEndereco.remove();
            blocoCache blocoAntigo = cache.remove(enderecoAntigo);
            filaEndereco.add(enderecoAntigo);
            cache.put(enderecoAntigo, new blocoCache(bloco, tag, indiceRAM));
        }
    }

    public String printCache() {
        StringBuilder mem = new StringBuilder();
        for (Map.Entry<Integer, blocoCache> entry : cache.entrySet()) {
            mem.append("Posição: ").append(entry.getKey()).append("\n").append("  Chunks: ").append(Arrays.toString(entry.getValue().dados)).append("\n").append("  TAG: (").append(entry.getValue().tag).append(", ").append(entry.getValue().indiceRAM).append(")").append("\n");
        }
        return mem.toString();
    }

    public String printPosicaoCache(int posicao) {

        blocoCache bloco = cache.get(posicao);
        return "Posição: " + posicao + ", Dados: " + Arrays.toString(bloco.dados) +
                ", TAG: (" + bloco.tag + "," + bloco.indiceRAM + ")";
    }

    public void atualizaBloco(blocoCache bloco, Integer enderecoBloco) {
        cache.replace(enderecoBloco, bloco);
    }

}
