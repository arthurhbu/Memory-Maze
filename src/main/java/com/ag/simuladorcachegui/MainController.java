package com.ag.simuladorcachegui;

import entity.MemoriaCache;
import entity.Processador;
import entity.RAM;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;

public class MainController extends mudarTela{
    //Coloca um bloco da ram num bloco da cache a partir de um dado da ram
    private static void colocaRamCacheComRam(RAM ram, Processador p1, int idReceita) {
        int inicioBlocoRAM = ram.getIndiceBloco(idReceita);
        int[] blocoRAM = ram.getBloco(inicioBlocoRAM);
        p1.setBlocoCache(blocoRAM, MemoriaCache.tags.Exclusivo, inicioBlocoRAM);
    }

    //Coloca um bloco da ram num bloco da cache a partir de um bloco da cache
    public static void colocaRamCacheComCache(RAM ram, Processador p1, MemoriaCache.blocoCache blocoCache1, Integer enderecoBloco1) {
        int[] blocoRAM = ram.getBloco(blocoCache1.getIndiceRAM());
        blocoCache1.setDados(blocoRAM);
        blocoCache1.setTag(MemoriaCache.tags.Exclusivo);
        p1.getMemoriaCache().atualizaBloco(blocoCache1, enderecoBloco1);
    }

    //Edita a cache no bloco do dado a ser modificado com o dado a ser escrito
    private static void editaCacheHit(int idReceitaAlterado, int idReceita, int enderecoBloco1, Processador p1, MemoriaCache.blocoCache blocoCachep1) {
        blocoCachep1.getDados()[p1.buscaReceitaNoBloco(blocoCachep1, idReceita)] = idReceitaAlterado;
        blocoCachep1.setTag(MemoriaCache.tags.Modificado);
        p1.getMemoriaCache().atualizaBloco(blocoCachep1, enderecoBloco1);
    }

    //Edita a cache no bloco do dado a ser modificado com o dado a ser escrito
    private static void editaCacheMiss(Processador p1, int idReceita, int idReceitaAlterado) {
        Integer enderecoBloco1 = p1.confereDadoCache(idReceita);
        MemoriaCache.blocoCache blocoCachep1 = p1.getMemoriaCache().getBlocoCache(enderecoBloco1);
        editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);
    }

    private static void atualizaBlocoCache(Processador p1, MemoriaCache.blocoCache blocoCache1, Integer enderecoBloco1, MemoriaCache.blocoCache blocoCachep3) {
        blocoCachep3.setTag(MemoriaCache.tags.Compartilhado);

        blocoCache1.setDados(blocoCachep3.getDados());
        blocoCache1.setTag(MemoriaCache.tags.Compartilhado);
        p1.getMemoriaCache().atualizaBloco(blocoCache1, enderecoBloco1);
    }

    public static int readHit(Processador p, int idReceita) {

        return p.confereDadoCache(idReceita);
//        System.out.println(
//                "\nA leitura foi um readHit não foi preciso fazer uma busca na memoria RAM e nem mudar a tag " +
//                        "do bloco da cache do processador escolhido.\n"
//        );
//        p.getMemoriaCache().printPosicaoCache(indiceBloco);
//        System.out.println("\nMemória cache acessada: ");
//        p.getMemoriaCache().printCache();

    }

    public static int readMissInvalido(RAM ram, Processador p1, Processador p2, Processador p3, int idReceita, MemoriaCache.blocoCache blocoCache1, Integer enderecoBloco1) {
        Integer enderecoBloco2 = p2.confereDadoCache(idReceita);
        Integer enderecoBloco3 = p3.confereDadoCache(idReceita);

        if (enderecoBloco2 == null && enderecoBloco3 == null) {
            colocaRamCacheComCache(ram, p1, blocoCache1, enderecoBloco1);

            System.out.println("\nA leitura foi um readMiss pois a tag da cache acessada é invalido e não estava em nenhuma outra cache, então " +
                    "foi necessário um acesso a memoria principal e a tag do bloco da cache do processador escolhido é exclusiva");
            System.out.println("\nMemória cache acessada: ");
            p1.getMemoriaCache().printCache();
            return 0;

        } else if (enderecoBloco2 != null && enderecoBloco3 == null) {
            MemoriaCache.blocoCache blocoCachep2 = p2.getBlocoCache(enderecoBloco2);

            return readMissInvalidoOneCopy(ram, p1, p2, blocoCache1, enderecoBloco1, blocoCachep2);
        } else if (enderecoBloco2 == null && enderecoBloco3 != null) {
            MemoriaCache.blocoCache blocoCachep3 = p3.getBlocoCache(enderecoBloco3);

            return readMissInvalidoOneCopy(ram, p1, p3, blocoCache1, enderecoBloco1, blocoCachep3);
        } else {
            MemoriaCache.blocoCache blocoCachep2 = p2.getBlocoCache(enderecoBloco2);
            MemoriaCache.blocoCache blocoCachep3 = p3.getBlocoCache(enderecoBloco3);

            if (blocoCachep2.getTag() == MemoriaCache.tags.Compartilhado && blocoCachep3.getTag() == MemoriaCache.tags.Compartilhado) {
                atualizaBlocoCache(p1, blocoCache1, enderecoBloco1, blocoCachep2);

                System.out.println("\nA leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava nas duas outras caches e ambas com a tag compartilhado, " +
                        "então foi requisitado uma das duas caches e a tag do bloco da cache do processador escolhido é compartilhado.");
                System.out.println("\nMemória cache acessada: ");
                p1.getMemoriaCache().printCache();
                System.out.println("\nMemória cache requisitada: ");
                p2.getMemoriaCache().printCache();

                return 3;
            } else if (blocoCachep2.getTag() == MemoriaCache.tags.Exclusivo && blocoCachep3.getTag() == MemoriaCache.tags.Compartilhado) {
                atualizaBlocoCache(p1, blocoCache1, enderecoBloco1, blocoCachep2);

                System.out.println("\nA leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava nas duas outras caches uma com a tag compartilhado e " +
                        "a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.");
                System.out.println("\nMemória cache acessada: ");
                p1.getMemoriaCache().printCache();
                System.out.println("\nMemória cache requisitada: ");
                p2.getMemoriaCache().printCache();
                return 4;
            } else if (blocoCachep3.getTag() == MemoriaCache.tags.Exclusivo && blocoCachep2.getTag() == MemoriaCache.tags.Compartilhado) {
                atualizaBlocoCache(p1, blocoCache1, enderecoBloco1, blocoCachep3);

                System.out.println("\nA leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava nas duas outras caches uma com a tag compartilhado e " +
                        "a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.");
                System.out.println("\nMemória cache acessada: ");
                p1.getMemoriaCache().printCache();
                System.out.println("\nMemória cache requisitada: ");
                p3.getMemoriaCache().printCache();
                return 5;
            }
        }
        return -999;
    }

    private static int readMissInvalidoOneCopy(RAM ram, Processador p1, Processador p3, MemoriaCache.blocoCache blocoCache1, Integer enderecoBloco1, MemoriaCache.blocoCache blocoCachep2) {
        if (blocoCachep2.getTag() == MemoriaCache.tags.Exclusivo) {
            atualizaBlocoCache(p1, blocoCache1, enderecoBloco1, blocoCachep2);

            System.out.println("\nA leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava em uma das outras caches e a tag era exclusivo, então " +
                    "essa cache foi requisitada e ambas as tags são compartilhado.");
            System.out.println("\nMemória cache acessada: ");
            p1.getMemoriaCache().printCache();
            System.out.println("\nMemória cache requisitada: ");
            p3.getMemoriaCache().printCache();
            return 1;
        } else if (blocoCachep2.getTag() == MemoriaCache.tags.Modificado) {
            ram.updateBloco(blocoCachep2.getDados(), blocoCachep2.getIndiceRAM());

            atualizaBlocoCache(p1, blocoCache1, enderecoBloco1, blocoCachep2);

            System.out.println("\nA leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava em uma das outras caches e a tag era modificado, então " +
                    "houve writeBack, essa cache foi requisitada e ambas as tags são compartilhado.");
            System.out.println("\nMemória cache acessada: ");
            p1.getMemoriaCache().printCache();
            System.out.println("\nMemória cache requisitada: ");
            p3.getMemoriaCache().printCache();
            System.out.println("\nMemória RAM: ");
            ram.printMemoria();
            return 2;
        }
        return -999;
    }

    public static int readMiss(RAM ram, Processador p1, Processador p2, Processador p3, int idReceita) {
        Integer enderecoBloco2 = p2.confereDadoCache(idReceita);
        Integer enderecoBloco3 = p3.confereDadoCache(idReceita);

        if (enderecoBloco2 == null && enderecoBloco3 == null) {
            colocaRamCacheComRam(ram, p1, idReceita);
            return 0;
        } else if (enderecoBloco2 != null && enderecoBloco3 == null) {
            return readMissOneCopy(ram, p1, p2, enderecoBloco2);
        } else if (enderecoBloco2 == null) {
            return readMissOneCopy(ram, p1, p3, enderecoBloco3);
        } else {
            MemoriaCache.blocoCache blocoCachep2 = p2.getBlocoCache(enderecoBloco2);
            MemoriaCache.blocoCache blocoCachep3 = p3.getBlocoCache(enderecoBloco3);

            if (blocoCachep2.getTag() == MemoriaCache.tags.Compartilhado && blocoCachep3.getTag() == MemoriaCache.tags.Compartilhado) {
                p1.getMemoriaCache().setBloco(blocoCachep2.getDados(), MemoriaCache.tags.Compartilhado, blocoCachep2.getIndiceRAM());
                return 3;
//                System.out.println("\nA leitura foi um readMiss, o dado estava nas duas outras caches e ambas com a tag compartilhado, " +
//                        "então foi requisitado uma das duas caches e a tag do bloco da cache do processador escolhido é compartilhado.");
//                System.out.println("\nMemória cache acessada: ");
//                p1.getMemoriaCache().printCache();
//                System.out.println("\nMemória cache requisitada: ");
//                p3.getMemoriaCache().printCache();
            } else if (blocoCachep2.getTag() == MemoriaCache.tags.Exclusivo && blocoCachep3.getTag() == MemoriaCache.tags.Compartilhado) {
                blocoCachep2.setTag(MemoriaCache.tags.Compartilhado);
                p1.getMemoriaCache().setBloco(blocoCachep2.getDados(), MemoriaCache.tags.Compartilhado, blocoCachep2.getIndiceRAM());

                return 4;
//                System.out.println("\nA leitura foi um readMiss, o dado estava nas duas outras caches uma com a tag compartilhado e " +
//                        "a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.");
//                System.out.println("\nMemória cache acessada: ");
//                p1.getMemoriaCache().printCache();
//                System.out.println("\nMemória cache requisitada: ");
//                p2.getMemoriaCache().printCache();
            } else if (blocoCachep3.getTag() == MemoriaCache.tags.Exclusivo && blocoCachep2.getTag() == MemoriaCache.tags.Compartilhado) {
                blocoCachep3.setTag(MemoriaCache.tags.Compartilhado);
                p1.getMemoriaCache().setBloco(blocoCachep3.getDados(), MemoriaCache.tags.Compartilhado, blocoCachep3.getIndiceRAM());

//                System.out.println("\nA leitura foi um readMiss, o dado estava nas duas outras caches uma com a tag compartilhado e " +
//                        "a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.");
//                System.out.println("\nMemória cache acessada: ");
//                p1.getMemoriaCache().printCache();
//                System.out.println("\nMemória cache requisitada: ");
//                p3.getMemoriaCache().printCache();
                return 5;
            }
        }
        return 6;
    }

    private static int readMissOneCopy(RAM ram, Processador p1, Processador p3, Integer enderecoBloco3) {
        MemoriaCache.blocoCache blocoCachep3 = p3.getBlocoCache(enderecoBloco3);

        if (blocoCachep3.getTag() == MemoriaCache.tags.Exclusivo) {

            blocoCachep3.setTag(MemoriaCache.tags.Compartilhado);
            p1.getMemoriaCache().setBloco(blocoCachep3.getDados(), MemoriaCache.tags.Compartilhado, blocoCachep3.getIndiceRAM());
            return 1;
//            System.out.println("\nA leitura foi um readMiss, o dado estava em uma das outras caches e a tag era exclusivo, então " +
//                    "essa cache foi requisitada e ambas as tags são compartilhado.");
//            System.out.println("\nMemória cache acessada: ");
//            p1.getMemoriaCache().printCache();
//            System.out.println("\nMemória cache requisitada: ");
//            p3.getMemoriaCache().printCache();
        } else if (blocoCachep3.getTag() == MemoriaCache.tags.Modificado) {
            ram.updateBloco(blocoCachep3.getDados(), blocoCachep3.getIndiceRAM());

            blocoCachep3.setTag(MemoriaCache.tags.Compartilhado);
            p1.getMemoriaCache().setBloco(blocoCachep3.getDados(), MemoriaCache.tags.Compartilhado, blocoCachep3.getIndiceRAM());
            return 2;
//            System.out.println("\nA leitura foi um readMiss, o dado estava em uma das outras caches e a tag era modificado, então " +
//                    "houve writeBack, essa cache foi requisitada e ambas as tags são compartilhado.");
//            System.out.println("\nMemória cache acessada: ");
//            p1.getMemoriaCache().printCache();
//            System.out.println("\nMemória cache requisitada: ");
//            p3.getMemoriaCache().printCache();
//            System.out.println("\nMemória RAM: ");
//            ram.printMemoria();
        }
        return 999;
    }

    public static int writeHit(int idReceitaAlterado, int idReceita, int enderecoBloco1, RAM ram, Processador p1, Processador p2, Processador p3) {
        MemoriaCache.blocoCache blocoCachep1 = p1.getMemoriaCache().getBlocoCache(enderecoBloco1);

        if (blocoCachep1.getTag() == MemoriaCache.tags.Modificado) {
            editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);
            return 0;
//            System.out.println("\nA escrita foi um writeHit e o bloco da cache estava com a tag modificado, " +
//                    "portanto mantém a mesma tag");
//            System.out.println("\nMemória cache acessada: ");
//            p1.getMemoriaCache().printCache();
        } else if (blocoCachep1.getTag() == MemoriaCache.tags.Compartilhado) {
            Integer enderecoBloco2 = p2.confereDadoCache(idReceita);
            Integer enderecoBloco3 = p3.confereDadoCache(idReceita);

            if (enderecoBloco2 != null && enderecoBloco3 != null) {
                MemoriaCache.blocoCache blocoCachep2 = p2.getMemoriaCache().getBlocoCache(enderecoBloco2);
                MemoriaCache.blocoCache blocoCachep3 = p3.getMemoriaCache().getBlocoCache(enderecoBloco3);

                editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);

                blocoCachep2.setTag(MemoriaCache.tags.Invalido);
                p2.getMemoriaCache().atualizaBloco(blocoCachep2, enderecoBloco2);

                blocoCachep3.setTag(MemoriaCache.tags.Invalido);
                p3.getMemoriaCache().atualizaBloco(blocoCachep3, enderecoBloco3);
                return 1;
//                System.out.println("\nA escrita foi um writeHit, o bloco da cache estava com a tag compartilhado " +
//                        "e foi encontrado nas outras duas caches que tiveram suas tags dos blocos invalidadas");
//                System.out.println("\nMemória cache acessada: ");
//                p1.getMemoriaCache().printCache();
//                System.out.println("\nMemória cache requisitada: ");
//                p2.getMemoriaCache().printCache();
//                System.out.println("\nMemória cache requisitada: ");
//                p3.getMemoriaCache().printCache();
            } else if (enderecoBloco2 == null) {
                MemoriaCache.blocoCache blocoCachep3 = p3.getMemoriaCache().getBlocoCache(enderecoBloco3);

                editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);

                blocoCachep3.setTag(MemoriaCache.tags.Invalido);
                p3.getMemoriaCache().atualizaBloco(blocoCachep3, enderecoBloco3);
                return 2;
//                System.out.println("\nA escrita foi um writeHit, o bloco da cache estava com a tag compartilhado " +
//                        "e foi encontrado em outra cache que teve sua tag do bloco invalidada");
//                System.out.println("\nMemória cache acessada: ");
//                p1.getMemoriaCache().printCache();
//                System.out.println("\nMemória cache requisitada: ");
//                p3.getMemoriaCache().printCache();
            } else if (enderecoBloco3 == null) {
                MemoriaCache.blocoCache blocoCachep2 = p2.getMemoriaCache().getBlocoCache(enderecoBloco2);

                editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);

                blocoCachep2.setTag(MemoriaCache.tags.Invalido);
                p2.getMemoriaCache().atualizaBloco(blocoCachep2, enderecoBloco2);
                return 3;
//                System.out.println("A escrita foi um writeHit, o bloco da cache estava com a tag compartilhado " +
//                        "e foi encontrado em outra cache que teve sua tag do bloco invalidada");
//                System.out.println("\nMemória cache acessada: ");
//                p1.getMemoriaCache().printCache();
//                System.out.println("\nMemória cache requisitada: ");
//                p2.getMemoriaCache().printCache();
            }
        } else if (blocoCachep1.getTag() == MemoriaCache.tags.Exclusivo) {
            editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);
            return 4;
//            System.out.println("\nA escrita foi um writeHit e o bloco da cache estava com a tag exclusivo " +
//                    "portanto a tag foi alterada para modificado");
//            System.out.println("\nMemória cache acessada: ");
//            p1.getMemoriaCache().printCache();
        }
        return 999;
    }

    public static int writeMissInvalido(RAM ram, Processador p1, Processador p2, Processador p3, int idReceita, MemoriaCache.blocoCache blocoCache1, Integer enderecoBloco1, int idReceitaAlterado) {
        Integer enderecoBloco2 = p2.confereDadoCache(idReceita);
        Integer enderecoBloco3 = p3.confereDadoCache(idReceita);

        if (enderecoBloco2 == null && enderecoBloco3 == null) {
            colocaRamCacheComCache(ram, p1, blocoCache1, enderecoBloco1);
            editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCache1);

            return 0;
//            System.out.println("\nA leitura foi um readMiss, pois a tag da cache acessada é invalido e não estava em nenhuma outra cache, então " +
//                    "foi necessário um acesso a memoria principal e a tag do bloco da cache do processador escolhido é exclusiva");
//            System.out.println("\nMemória cache acessada: ");
//            p1.getMemoriaCache().printCache();
        } else if (enderecoBloco2 != null && enderecoBloco3 == null) {
            return writeMissInvalidoOneCopy(ram, p1, p2, idReceita, idReceitaAlterado, blocoCache1, enderecoBloco1, enderecoBloco2);
        } else if (enderecoBloco2 == null && enderecoBloco3 != null) {
            return writeMissInvalidoOneCopy(ram, p1, p3, idReceita, idReceitaAlterado, blocoCache1, enderecoBloco1, enderecoBloco3);
        }
        return 999;
    }

    public static int writeMissInvalidoOneCopy(RAM ram, Processador p1, Processador p2, int idReceita, int idReceitaAlterado, MemoriaCache.blocoCache blocoCache1, Integer enderecoBloco1, Integer enderecoBloco2) {
        MemoriaCache.blocoCache blocoCache2 = p2.getMemoriaCache().getBlocoCache(enderecoBloco2);
        if (blocoCache2.getTag() == MemoriaCache.tags.Modificado) {
            //writeBack
            ram.updateBloco(blocoCache2.getDados(), blocoCache2.getIndiceRAM());
            blocoCache2.setTag(MemoriaCache.tags.Invalido);

            colocaRamCacheComCache(ram, p1, blocoCache1, enderecoBloco1);
            editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCache1);

//            System.out.println("\nA escrita foi um writeMiss, pois a tag da cache acessada é invalido, o dado escolhido estava em uma das outras caches e " +
//                    "a tag do bloco era modificado, então houve writeBack, a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram " +
//                    "e a tag do bloco da cache do processador escolhido é modificado");
//            System.out.println("\nMemória cache acessada: ");
//            p1.getMemoriaCache().printCache();
//            System.out.println("\nMemória cache requisitada: ");
//            p2.getMemoriaCache().printCache();
//            System.out.println("\nMemória RAM: ");
//            ram.printMemoria();
            return 1;
        } else if (blocoCache2.getTag() == MemoriaCache.tags.Compartilhado || blocoCache2.getTag() == MemoriaCache.tags.Exclusivo) {
            blocoCache2.setTag(MemoriaCache.tags.Invalido);

            colocaRamCacheComCache(ram, p1, blocoCache1, enderecoBloco1);
            editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCache1);

//            System.out.println("\nA escrita foi um writeMiss, pois a tag da cache acessada é invalido, o dado escolhido estava em uma das outras caches e " +
//                    "a tag era compartilhado ou exclusivo, então a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram " +
//                    "e a tag do bloco da cache do processador escolhido é modificado");
//            System.out.println("\nMemória cache acessada: ");
//            p1.getMemoriaCache().printCache();
//            System.out.println("\nMemória cache requisitada: ");
//            p2.getMemoriaCache().printCache();
            return 2;
        }
        return 999;
    }

    public static int writeMiss(RAM ram, Processador p1, Processador p2, Processador p3, int idReceita, int idReceitaAlterado) {
        Integer enderecoBloco2 = p2.confereDadoCache(idReceita);
        Integer enderecoBloco3 = p3.confereDadoCache(idReceita);

        if (enderecoBloco2 == null && enderecoBloco3 == null) {
            colocaRamCacheComRam(ram, p1, idReceita);
            editaCacheMiss(p1, idReceita, idReceitaAlterado);
            return 0;
//            System.out.println("\nA escrita foi um writeMiss e o bloco da cache não estava em nenhuma outra cache, " +
//                    "então foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado");
//            System.out.println("\nMemória cache acessada: ");
//            p1.getMemoriaCache().printCache();
        } else if (enderecoBloco3 == null && enderecoBloco2 != null) {
            return writeMissOneCopy(ram, p1, p2, idReceita, idReceitaAlterado, enderecoBloco2);
        } else if (enderecoBloco3 != null && enderecoBloco2 == null) {
            return writeMissOneCopy(ram, p1, p3, idReceita, idReceitaAlterado, enderecoBloco3);
        }
        return 999;
    }

    private static int writeMissOneCopy(RAM ram, Processador p1, Processador p2, int idReceita, int idReceitaAlterado, Integer enderecoBloco) {
        MemoriaCache.blocoCache blocoCache = p2.getMemoriaCache().getBlocoCache(enderecoBloco);
        if (blocoCache.getTag() == MemoriaCache.tags.Modificado) {
            //writeBack
            ram.updateBloco(blocoCache.getDados(), blocoCache.getIndiceRAM());
            blocoCache.setTag(MemoriaCache.tags.Invalido);

            colocaRamCacheComRam(ram, p1, idReceita);
            editaCacheMiss(p1, idReceita, idReceitaAlterado);
            return 1;
//            System.out.println("\nA escrita foi um writeMiss, o dado escolhido estava em uma das outras caches e a tag do bloco era modificado, então " +
//                    "houve writeBack, a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram " +
//                    "e a tag do bloco da cache do processador escolhido é modificado");
//            System.out.println("\nMemória cache acessada: ");
//            p1.getMemoriaCache().printCache();
//            System.out.println("\nMemória cache requisitada: ");
//            p2.getMemoriaCache().printCache();
//            System.out.println("\nMemória RAM: ");
//            ram.printMemoria();
        } else if (blocoCache.getTag() == MemoriaCache.tags.Compartilhado || blocoCache.getTag() == MemoriaCache.tags.Exclusivo) {
            blocoCache.setTag(MemoriaCache.tags.Invalido);

            colocaRamCacheComRam(ram, p1, idReceita);
            editaCacheMiss(p1, idReceita, idReceitaAlterado);
            return 2;
//            System.out.println("\nA escrita foi um writeMiss, o dado escolhido estava em uma das outras caches e a tag era compartilhado ou exclusivo, então " +
//                    "a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado");
//            System.out.println("\nMemória cache acessada: ");
//            p1.getMemoriaCache().printCache();
//            System.out.println("\nMemória cache requisitada: ");
//            p2.getMemoriaCache().printCache();
        }
        return 999;
    }

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Button buttonIniciar;

    @FXML
    private void iniciar() throws IOException{
        mudarTela("/com/ag/simuladorcachegui/menu.fxml", buttonIniciar, "menu.css");
    }

    @FXML
    private Label tittle;

    @FXML
    private void initialize() {
        RAM ram = RAM.getInstancia();
        GerenciadorProcessadores gerenciador = GerenciadorProcessadores.getInstancia();

        for(int i = 0; i < 100; i++) {
            ram.setLinha(i, i);
        }

        Font newFont = Font.loadFont(getClass().getResourceAsStream("Vecna.ttf"), 90);
        tittle.setFont(newFont);

        File fileButton = new File("src/images/button_initialScreen.png");
        Image imageButton = new Image(fileButton.toURI().toString());
        ImageView view = new ImageView(imageButton);
        view.setFitHeight(120);
        view.setFitWidth(300);
        buttonIniciar.setGraphic(view);

        File fileBackground = new File("src/images/CacheAdventure_initialScreen.png");
        Image imageBackground = new Image(fileBackground.toURI().toString());
        backgroundImage.setImage(imageBackground);
    }

}
