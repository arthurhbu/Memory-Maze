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

    // Analisa se o indice RAM do bloco solicitado existe em outra cache, caso exista e esteja modificado: writeBack e torna invalido
    public static void analisaWriteback(RAM ram, int idReceita, MemoriaCache.blocoCache[] blocoCaches) {
        int inicioBlocoRam = ram.getIndiceBloco(idReceita);
        for (int i = 0; i < blocoCaches.length; i++) {
            if(blocoCaches[i].getTag() == MemoriaCache.tags.Modificado && blocoCaches[i].getIndiceRAM() == inicioBlocoRam){
                ram.updateBloco(blocoCaches[i].getDados(), blocoCaches[i].getIndiceRAM());
                blocoCaches[i].setTag(MemoriaCache.tags.Invalido);
                System.out.println("\nO dado procurado foi modificado em outra cache, então foi realizado o write back " +
                        "e a tag deste bloco foi modificado para inválido");
//                ram.printMemoria();
            }
        }
    }

    private static int colocaRamCacheComRam(RAM ram, Processador p1, int idReceita) {
        int inicioBlocoRAM = ram.getIndiceBloco(idReceita);
        int[] blocoRAM = ram.getBloco(inicioBlocoRAM);

        if(blocoRAM == null) {
            //false
            return 0;
        }

        MemoriaCache.blocoCache blocoASerRetirado = p1.getMemoriaCache().getBlocoInicioFila();
        MemoriaCache.blocoCache[] todosBlocos = p1.getMemoriaCache().getTodosBlocos();
        if(todosBlocos != null) {
            for(int i = 0; i < todosBlocos.length; i++) {
                if(todosBlocos[i].equals(blocoASerRetirado) && blocoASerRetirado.getTag() == MemoriaCache.tags.Modificado && todosBlocos.length == 5){
                    ram.updateBloco(blocoASerRetirado.getDados(), blocoASerRetirado.indiceRAM);
                    p1.setBlocoCache(blocoRAM, MemoriaCache.tags.Exclusivo, inicioBlocoRAM);
                    System.out.println("\nA tag do bloco retirado da cache era modificado, então houve a necessidade de realizar o writeBack do bloco retirado");
                    ram.printMemoria();
                    //true
                    return 1;
                }
                else if(todosBlocos[i].getIndiceRAM() == inicioBlocoRAM) {
                    System.out.println("\nO bloco que continha o dado " + idReceita + " ja está inserido na cache com indice da RAM igual a " + inicioBlocoRAM +
                            " porém este dado foi modificado");
                    //false
                    return 2;
                }
            }
        }
        else if(todosBlocos == null){
            p1.setBlocoCache(blocoRAM, MemoriaCache.tags.Exclusivo, inicioBlocoRAM);
            if(blocoRAM == null){
                //false
                return 3;
            }
            //true
            return 4;
        }
        p1.setBlocoCache(blocoRAM, MemoriaCache.tags.Exclusivo, inicioBlocoRAM);
        //true
        return 5;
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
    private static boolean editaCacheMiss(Processador p1, int idReceita, int idReceitaAlterado) {
        Integer enderecoBloco1 = p1.confereDadoCache(idReceita);
        if(enderecoBloco1 != null){
            MemoriaCache.blocoCache blocoCachep1 = p1.getMemoriaCache().getBlocoCache(enderecoBloco1);
            editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);
            return true;
        }
        p1.getMemoriaCache().printCache();
        return false;
    }

    private static void atualizaBlocoCache(Processador p1, MemoriaCache.blocoCache blocoCache1, Integer enderecoBloco1, MemoriaCache.blocoCache blocoCachep3) {
        blocoCachep3.setTag(MemoriaCache.tags.Compartilhado);

        blocoCache1.setDados(blocoCachep3.getDados());
        blocoCache1.setTag(MemoriaCache.tags.Compartilhado);
        p1.getMemoriaCache().atualizaBloco(blocoCache1, enderecoBloco1);
    }

    public static int readHit(Processador p, int idReceita) {

        return p.confereDadoCache(idReceita);

    }

    public static int readMissInvalido(RAM ram, Processador p1, Processador p2, Processador p3, int idReceita, MemoriaCache.blocoCache blocoCache1, Integer enderecoBloco1) {
        Integer enderecoBloco2 = p2.confereDadoCache(idReceita);
        Integer enderecoBloco3 = p3.confereDadoCache(idReceita);

        if (enderecoBloco2 == null && enderecoBloco3 == null) {
            colocaRamCacheComCache(ram, p1, blocoCache1, enderecoBloco1);
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
                return 3;
            } else if (blocoCachep2.getTag() == MemoriaCache.tags.Exclusivo && blocoCachep3.getTag() == MemoriaCache.tags.Compartilhado) {
                atualizaBlocoCache(p1, blocoCache1, enderecoBloco1, blocoCachep2);
                return 4;
            } else if (blocoCachep3.getTag() == MemoriaCache.tags.Exclusivo && blocoCachep2.getTag() == MemoriaCache.tags.Compartilhado) {
                atualizaBlocoCache(p1, blocoCache1, enderecoBloco1, blocoCachep3);
                return 5;
            }
        }
        return -999;
    }

    private static int readMissInvalidoOneCopy(RAM ram, Processador p1, Processador p3, MemoriaCache.blocoCache blocoCache1, Integer enderecoBloco1, MemoriaCache.blocoCache blocoCachep2) {
        if (blocoCachep2.getTag() == MemoriaCache.tags.Exclusivo) {
            atualizaBlocoCache(p1, blocoCache1, enderecoBloco1, blocoCachep2);

            return 1;
        } else if (blocoCachep2.getTag() == MemoriaCache.tags.Modificado) {
            ram.updateBloco(blocoCachep2.getDados(), blocoCachep2.getIndiceRAM());

            atualizaBlocoCache(p1, blocoCache1, enderecoBloco1, blocoCachep2);
            return 2;
        }
        return -999;
    }

    public static int readMiss(RAM ram, Processador p1, Processador p2, Processador p3, int idReceita) {
        Integer enderecoBloco2 = p2.confereDadoCache(idReceita);
        Integer enderecoBloco3 = p3.confereDadoCache(idReceita);

        MemoriaCache.blocoCache[] todosBlocosCache2 = p2.getMemoriaCache().getTodosBlocos();
        if (todosBlocosCache2 != null) {
            analisaWriteback(ram, idReceita, todosBlocosCache2);
        }
        MemoriaCache.blocoCache[] todosBlocosCache3 = p3.getMemoriaCache().getTodosBlocos();
        if (todosBlocosCache3 != null) {
            analisaWriteback(ram, idReceita, todosBlocosCache3);
        }

        if (enderecoBloco2 == null && enderecoBloco3 == null) {
            int aux = colocaRamCacheComRam(ram, p1, idReceita);

            if(aux == 1) {
                return 0;
            } else if(aux == 4 || aux == 5) {
                return 8;
            } else if(aux == 2) {
                return 7;
            }
        } else if (enderecoBloco2 != null && enderecoBloco3 == null) {
            return readMissOneCopy(ram, p1, p2, enderecoBloco2);
        } else if (enderecoBloco2 == null) {
            return readMissOneCopy(ram, p1, p3, enderecoBloco3);
        } else {
            MemoriaCache.blocoCache blocoCachep2 = p2.getBlocoCache(enderecoBloco2);
            MemoriaCache.blocoCache blocoCachep3 = p3.getBlocoCache(enderecoBloco3);

            if (blocoCachep2.getTag() == MemoriaCache.tags.Compartilhado && blocoCachep3.getTag() == MemoriaCache.tags.Compartilhado) {
                p1.getMemoriaCache().setBloco(blocoCachep2.getDados(), MemoriaCache.tags.Compartilhado, blocoCachep2.getIndiceRAM());
                return 4;

            } else if (blocoCachep2.getTag() == MemoriaCache.tags.Exclusivo && blocoCachep3.getTag() == MemoriaCache.tags.Compartilhado) {
                blocoCachep2.setTag(MemoriaCache.tags.Compartilhado);
                p1.getMemoriaCache().setBloco(blocoCachep2.getDados(), MemoriaCache.tags.Compartilhado, blocoCachep2.getIndiceRAM());
                return 5;

            } else if (blocoCachep3.getTag() == MemoriaCache.tags.Exclusivo && blocoCachep2.getTag() == MemoriaCache.tags.Compartilhado) {
                blocoCachep3.setTag(MemoriaCache.tags.Compartilhado);
                p1.getMemoriaCache().setBloco(blocoCachep3.getDados(), MemoriaCache.tags.Compartilhado, blocoCachep3.getIndiceRAM());
                return 6;
            }
        }
        return 999;
    }

    private static int readMissOneCopy(RAM ram, Processador p1, Processador p3, Integer enderecoBloco3) {
        MemoriaCache.blocoCache blocoCachep3 = p3.getBlocoCache(enderecoBloco3);

        if (blocoCachep3.getTag() == MemoriaCache.tags.Exclusivo) {

            blocoCachep3.setTag(MemoriaCache.tags.Compartilhado);
            p1.getMemoriaCache().setBloco(blocoCachep3.getDados(), MemoriaCache.tags.Compartilhado, blocoCachep3.getIndiceRAM());
            return 1;

        } else if (blocoCachep3.getTag() == MemoriaCache.tags.Modificado) {
            ram.updateBloco(blocoCachep3.getDados(), blocoCachep3.getIndiceRAM());

            blocoCachep3.setTag(MemoriaCache.tags.Compartilhado);
            p1.getMemoriaCache().setBloco(blocoCachep3.getDados(), MemoriaCache.tags.Compartilhado, blocoCachep3.getIndiceRAM());
            return 2;

        } else if(blocoCachep3.getTag() == MemoriaCache.tags.Invalido) {
            p1.getMemoriaCache().setBloco(blocoCachep3.getDados(), MemoriaCache.tags.Exclusivo, blocoCachep3.getIndiceRAM());
            return 3;
        }
        return 999;
    }

    public static int writeHit(int idReceitaAlterado, int idReceita, int enderecoBloco1, RAM ram, Processador p1, Processador p2, Processador p3) {
        MemoriaCache.blocoCache blocoCachep1 = p1.getMemoriaCache().getBlocoCache(enderecoBloco1);

        if (blocoCachep1.getTag() == MemoriaCache.tags.Modificado) {
            editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);
            return 0;

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

            } else if (enderecoBloco2 == null) {
                MemoriaCache.blocoCache blocoCachep3 = p3.getMemoriaCache().getBlocoCache(enderecoBloco3);

                editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);

                blocoCachep3.setTag(MemoriaCache.tags.Invalido);
                p3.getMemoriaCache().atualizaBloco(blocoCachep3, enderecoBloco3);
                return 2;

            } else if (enderecoBloco3 == null) {
                MemoriaCache.blocoCache blocoCachep2 = p2.getMemoriaCache().getBlocoCache(enderecoBloco2);

                editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);

                blocoCachep2.setTag(MemoriaCache.tags.Invalido);
                p2.getMemoriaCache().atualizaBloco(blocoCachep2, enderecoBloco2);
                return 3;

            }
        } else if (blocoCachep1.getTag() == MemoriaCache.tags.Exclusivo) {
            editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCachep1);
            return 4;

        }
        return 999;
    }

    public static int writeMissInvalido(RAM ram, Processador p1, Processador p2, Processador p3, int idReceita, MemoriaCache.blocoCache blocoCache1, Integer enderecoBloco1, int idReceitaAlterado) {
        Integer enderecoBloco2 = p2.confereDadoCache(idReceita);
        Integer enderecoBloco3 = p3.confereDadoCache(idReceita);

        MemoriaCache.blocoCache[] todosBlocosCache2 = p2.getMemoriaCache().getTodosBlocos();
        if (todosBlocosCache2 != null) {
            analisaWriteback(ram, idReceita, todosBlocosCache2);
        }
        MemoriaCache.blocoCache[] todosBlocosCache3 = p3.getMemoriaCache().getTodosBlocos();
        if (todosBlocosCache3 != null) {
            analisaWriteback(ram, idReceita, todosBlocosCache3);
        }

        if (enderecoBloco2 == null && enderecoBloco3 == null) {
            colocaRamCacheComCache(ram, p1, blocoCache1, enderecoBloco1);
            editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCache1);

            return 0;

        } else if (enderecoBloco2 != null && enderecoBloco3 == null) {
            return writeMissInvalidoOneCopy(ram, p1, p2, idReceita, idReceitaAlterado, blocoCache1, enderecoBloco1, enderecoBloco2);
        } else if (enderecoBloco2 == null && enderecoBloco3 != null) {
            return writeMissInvalidoOneCopy(ram, p1, p3, idReceita, idReceitaAlterado, blocoCache1, enderecoBloco1, enderecoBloco3);
        } else if(enderecoBloco2 != null && enderecoBloco3 != null){
            MemoriaCache.blocoCache blocoCachep2 = p2.getMemoriaCache().getBlocoCache(enderecoBloco2);
            MemoriaCache.blocoCache blocoCachep3 = p3.getMemoriaCache().getBlocoCache(enderecoBloco3);

            if(blocoCachep2.getTag() == MemoriaCache.tags.Invalido && blocoCachep3.getTag() == MemoriaCache.tags.Invalido){
                //Pegar o dado da ram
                colocaRamCacheComRam(ram, p1, idReceita);
                //escrever o dado novo
                editaCacheMiss(p1, idReceita, idReceitaAlterado);
            }
            else if(blocoCachep2.getTag() == MemoriaCache.tags.Invalido){
                if(blocoCachep3.getTag() == MemoriaCache.tags.Modificado){
                    //write back
                    ram.updateBloco(blocoCachep3.getDados(), blocoCachep3.getIndiceRAM());
                    blocoCachep3.setTag(MemoriaCache.tags.Invalido);
                    //pegar o dado
                    colocaRamCacheComRam(ram, p1, idReceita);
                    //escrever o dado novo
                    editaCacheMiss(p1, idReceita, idReceitaAlterado);
                }
            }
            else if(blocoCachep3.getTag() == MemoriaCache.tags.Invalido){
                if(blocoCachep2.getTag() == MemoriaCache.tags.Modificado){
                    //write back
                    ram.updateBloco(blocoCachep2.getDados(), blocoCachep2.getIndiceRAM());
                    blocoCachep2.setTag(MemoriaCache.tags.Invalido);
                    //pegar o dado
                    colocaRamCacheComRam(ram, p1, idReceita);
                    //escrever o dado novo
                    editaCacheMiss(p1, idReceita, idReceitaAlterado);
                }
            }
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

            return 1;
        } else if (blocoCache2.getTag() == MemoriaCache.tags.Compartilhado || blocoCache2.getTag() == MemoriaCache.tags.Exclusivo) {
            blocoCache2.setTag(MemoriaCache.tags.Invalido);

            colocaRamCacheComCache(ram, p1, blocoCache1, enderecoBloco1);
            editaCacheHit(idReceitaAlterado, idReceita, enderecoBloco1, p1, blocoCache1);
            return 2;
        }
        return 999;
    }

    public static int writeMiss(RAM ram, Processador p1, Processador p2, Processador p3, int idReceita, int idReceitaAlterado) {
        Integer enderecoBloco2 = p2.confereDadoCache(idReceita);
        Integer enderecoBloco3 = p3.confereDadoCache(idReceita);

        MemoriaCache.blocoCache[] todosBlocosCache2 = p2.getMemoriaCache().getTodosBlocos();
        if (todosBlocosCache2 != null) {
            analisaWriteback(ram, idReceita, todosBlocosCache2);
        }
        MemoriaCache.blocoCache[] todosBlocosCache3 = p3.getMemoriaCache().getTodosBlocos();
        if (todosBlocosCache3 != null) {
            analisaWriteback(ram, idReceita, todosBlocosCache3);
        }

        if (enderecoBloco2 == null && enderecoBloco3 == null) {
            int aux2 = colocaRamCacheComRam(ram, p1, idReceita);
            boolean aux = false;
            if (aux2 == 1) {
                aux = editaCacheMiss(p1, idReceita, idReceitaAlterado);
                if (aux) {
                    //aux2 == True and aux == True
                    return 0;
                }
            } else if(aux2 == 4 || aux2 == 5) {
                aux = editaCacheMiss(p1, idReceita, idReceitaAlterado);
                if(aux){
                    return 7;
                }
            } else if(aux2 == 2){
                return 6;
            } else {
                return 8;
            }
        } else if (enderecoBloco3 == null && enderecoBloco2 != null) {
            return writeMissOneCopy(ram, p1, p2, idReceita, idReceitaAlterado, enderecoBloco2);
        } else if (enderecoBloco3 != null && enderecoBloco2 == null) {
            return writeMissOneCopy(ram, p1, p3, idReceita, idReceitaAlterado, enderecoBloco3);
        } else if(enderecoBloco2 != null && enderecoBloco3 != null){
            MemoriaCache.blocoCache blocoCachep2 = p2.getMemoriaCache().getBlocoCache(enderecoBloco2);
            MemoriaCache.blocoCache blocoCachep3 = p3.getMemoriaCache().getBlocoCache(enderecoBloco3);

            if(blocoCachep2.getTag() == MemoriaCache.tags.Invalido && blocoCachep3.getTag() == MemoriaCache.tags.Invalido){
                //Pegar o dado da ram
                colocaRamCacheComRam(ram, p1, idReceita);
                //escrever o dado novo
                editaCacheMiss(p1, idReceita, idReceitaAlterado);
                return 999;
            }
            else if(blocoCachep2.getTag() == MemoriaCache.tags.Invalido){
                if(blocoCachep3.getTag() == MemoriaCache.tags.Modificado){
                    //write back
                    ram.updateBloco(blocoCachep3.getDados(), blocoCachep3.getIndiceRAM());
                    blocoCachep3.setTag(MemoriaCache.tags.Invalido);
                    //pegar o dado
                    colocaRamCacheComRam(ram, p1, idReceita);
                    //escrever o dado novo
                    editaCacheMiss(p1, idReceita, idReceitaAlterado);
                }
            }
            else if(blocoCachep3.getTag() == MemoriaCache.tags.Invalido){
                if(blocoCachep2.getTag() == MemoriaCache.tags.Modificado){
                    //write back
                    ram.updateBloco(blocoCachep2.getDados(), blocoCachep2.getIndiceRAM());
                    blocoCachep2.setTag(MemoriaCache.tags.Invalido);
                    //pegar o dado
                    colocaRamCacheComRam(ram, p1, idReceita);
                    //escrever o dado novo
                    editaCacheMiss(p1, idReceita, idReceitaAlterado);
                }
            }
        }
        return 999;
    }

    private static int writeMissOneCopy(RAM ram, Processador p1, Processador p2, int idReceita, int idReceitaAlterado, Integer enderecoBloco) {
        MemoriaCache.blocoCache blocoCache = p2.getMemoriaCache().getBlocoCache(enderecoBloco);
        if (blocoCache.getTag() == MemoriaCache.tags.Modificado) {
            //writeBack
            ram.updateBloco(blocoCache.getDados(), blocoCache.getIndiceRAM());
            blocoCache.setTag(MemoriaCache.tags.Invalido);

            int aux = colocaRamCacheComRam(ram, p1, idReceita);
            editaCacheMiss(p1, idReceita, idReceitaAlterado);
            if(aux == 1) {
                return 1;
            } else if(aux == 2) {
                return 2;
            }
            return 8;

        } else if (blocoCache.getTag() == MemoriaCache.tags.Compartilhado || blocoCache.getTag() == MemoriaCache.tags.Exclusivo) {
            blocoCache.setTag(MemoriaCache.tags.Invalido);

            int aux = colocaRamCacheComRam(ram, p1, idReceita);
            editaCacheMiss(p1, idReceita, idReceitaAlterado);
            if (aux == 1) {
                return 3;
            } else if(aux == 2) {
                return 4;
            }
            return 9;
        } else if(blocoCache.getTag() == MemoriaCache.tags.Invalido){
            p1.getMemoriaCache().setBloco(blocoCache.getDados(), MemoriaCache.tags.Exclusivo, blocoCache.getIndiceRAM());
            editaCacheMiss(p1, idReceita, idReceitaAlterado);
            return 5;
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

        buttonIniciar.setOnMouseEntered(e -> {
            buttonIniciar.setScaleX(1.1);
            buttonIniciar.setScaleY(1.1);
            buttonIniciar.setStyle("-fx-background-size: cover; -fx-effect: dropshadow(gaussian, #D4BFFF, 20, 0.8, 0, 0); -fx-border-color: transparent; -fx-padding: 0; -fx-background-color: transparent;");
        });

        buttonIniciar.setOnMouseExited(e -> {
            buttonIniciar.setScaleX(1.0);
            buttonIniciar.setScaleY(1.0);
            buttonIniciar.setStyle("-fx-background-size: cover; -fx-effect: dropshadow(gaussian, #D4BFFF, 20, 0.1, 0, 0); -fx-border-color: transparent; -fx-padding: 0; -fx-background-color: transparent;");
        });
    }

}
