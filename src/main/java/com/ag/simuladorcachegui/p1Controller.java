package com.ag.simuladorcachegui;

import entity.MemoriaCache;
import entity.RAM;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.regex.Pattern;

public class p1Controller extends MainController {

    @FXML
    private StackPane mainStackPane;

    //PAINEL DE LEITURA
    @FXML
    private AnchorPane leituraPane;

    @FXML
    private RadioButton escritaRadioBtnLPane;

    @FXML
    private Button buttonVoltarLPane;

    @FXML
    private TextArea ramTextAreaL;

    @FXML
    private TextArea cache1TextAreaL;

    @FXML
    private TextArea cache2TextAreaL;

    @FXML
    private TextArea cache3TextAreaL;

    @FXML
    private TextField inputDadoLido;

    @FXML
    private Button realizarLeituraBtn;

    @FXML
    private Label errorLabelL;

    @FXML
    private TextArea relatorioTextAreaL;

    //PAINEL DE ESCRITA
    @FXML
    private AnchorPane escritaPane;

    @FXML
    private RadioButton leituraRadioBtn;

    @FXML
    private Button buttonVoltar;

    @FXML
    private TextArea ramTextAreaE;

    @FXML
    private TextArea cache1TextAreaE;

    @FXML
    private TextArea cache2TextAreaE;

    @FXML
    private TextArea cache3TextAreaE;

    @FXML
    private TextArea relatorioTextAreaE;

    @FXML
    private TextField inputDadoE;

    @FXML
    private TextField inputDadoEscrita;

    @FXML
    private Label alertLabelE;

    @FXML
    private Button realizarEscritaBtn;


    @FXML
    private void voltarMenu() throws IOException {
        mudarTela("/com/ag/simuladorcachegui/menu.fxml", buttonVoltar, "/menu.css");
    }

    @FXML
    private void voltarMenuL() throws IOException {
        mudarTela("/com/ag/simuladorcachegui/menu.fxml", buttonVoltarLPane, "/processador1.css");
    }

    @FXML
    private void realizarLeitura() {
        RAM ram = RAM.getInstancia();
        GerenciadorProcessadores gerenciador = GerenciadorProcessadores.getInstancia();

        if (inputDadoLido.getText().isEmpty() || !Pattern.matches("^[0-9]*$", inputDadoLido.getText())) {
            errorLabelL.setText("Dado inválido! Somente numeros inteiros!");
        } else {
            errorLabelL.setText("");
            int id;
            id = Integer.parseInt(inputDadoLido.getText());
            Integer enderecobloco1 = gerenciador.getProcessador("Processador1").confereDadoCache(id);
            if (enderecobloco1 != null) {
                MemoriaCache.blocoCache blocoCache1 = gerenciador.getProcessador("Processador1").getBlocoCache(enderecobloco1);
                if (blocoCache1.getTag() == MemoriaCache.tags.Invalido) {
                    int resultado = MainController.readMissInvalido(ram, gerenciador.getProcessador("Processador1"), gerenciador.getProcessador("Processador2"), gerenciador.getProcessador("Processador3"), id, blocoCache1, enderecobloco1);
                    switch (resultado) {
                        case 0:
                            relatorioTextAreaL.setText("""

                                    Simulador-Cache Log:
                                    
                                        A leitura foi um readMiss pois a tag da cache acessada é invalido e não estava em nenhuma outra cache, então foi necessário um acesso a memoria principal e a tag do bloco da cache do processador escolhido é exclusiva.

                                    Mensagem do Jogo:
                                    """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        case 1:
                            relatorioTextAreaL.setText("""
                                   
                                   Simulador-Cache Log:
                                   
                                        A leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava em uma das outras caches e a tag era exclusivo, então essa cache foi requisitada e ambas as tags são compartilhado.
                                   
                                   Mensagem do jogo:
                                   """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        case 2:
                            relatorioTextAreaL.setText("""
                                    
                                    Simulador-Cache Log:
                                    
                                        leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava em uma das outras caches e a tag era modificado, então houve writeBack, essa cache foi requisitada e ambas as tags são compartilhado.
                                    
                                    Mensagem do jogo:
                                    """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        case 3:
                            relatorioTextAreaL.setText("""
                                    Simulador-Cache Log:
                                    
                                        A leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava nas duas outras caches e ambas com a tag compartilhado, então foi requisitado uma das duas caches e a tag do bloco da cache do processador escolhido é compartilhado.
                                    
                                    Mensagem do jogo:
                                    """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        case 4:
                            relatorioTextAreaL.setText("""
                                    Simulador-Cache Log:
                                    
                                        A leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava nas duas outras caches uma com a tag compartilhado e a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.
                                    
                                    Mensagem do jogo:
                                    """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        case 5:
                            relatorioTextAreaL.setText("""
                                    Simulador-Cache Log:
                                    
                                        A leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava nas duas outras caches uma com a tag compartilhado e a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.
                                    
                                    Mensagem do jogo:
                                    """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        default:
                            System.out.println("Ocorreu um erro, conferir resultado" + resultado);
                            break;
                    }
                } else {
                    int indiceBloco = readHit(gerenciador.getProcessador("Processador1"), id);
                    relatorioTextAreaL.setText("""
                            Simulador-Cache Log:
                            
                                A leitura foi um readHit não foi preciso fazer uma busca na memoria RAM e nem mudar a tag do bloco da cache do processador escolhido.
                            
                            """
                            +
                            gerenciador.getProcessador("Processador1").getMemoriaCache().printPosicaoCache(indiceBloco)
                            +
                            """
                            
                            Mensagem do jogo:
                            """
                    );
                    printaCache(gerenciador, 'l');
                    ramTextAreaL.setText(ram.printMemoria());
                }
            } else {
                int resultadoRM = readMiss(ram, gerenciador.getProcessador("Processador1"), gerenciador.getProcessador("Processador2"), gerenciador.getProcessador("Processador3"), id);
                System.out.println("resultado:" + resultadoRM);
                switch (resultadoRM) {
                    case 0:
                        System.out.println("VASDASOIFJASIFIJASFDIJOASFJIOASJIOFPSAPIOJFSAD");
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss e não estava em nenhuma das outras caches, então foi necessário um acesso a memoria principal e a tag do bloco da cache do processador escolhido é exclusiva.
                                
                                Mensagem do jogo:
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 1:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava em uma das outras caches e a tag era exclusivo, então essa cache foi requisitada e ambas as tags são compartilhado.
                                
                                Mensagem do jogo:
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 2:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava em uma das outras caches e a tag era modificado, então houve writeBack, essa cache foi requisitada e ambas as tags são compartilhado.
                                
                                Mensagem do jogo:
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 3:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava nas duas outras caches e ambas com a tag compartilhado, então foi requisitado uma das duas caches e a tag do bloco da cache do processador escolhido é compartilhado.
                                
                                Mensagem do jogo:
                                """);
                        printaCache(gerenciador, 'l');
                        break;
                    case 4:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava nas duas outras caches uma com a tag compartilhado e a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.
                                
                                Mensagem do jogo:
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 5:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava nas duas outras caches uma com a tag compartilhado e a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.
                                
                                Mensagem do jogo:
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    default:
                        System.out.println("Ocorreu um erro, não foi retornado nenhum valor correto" + resultadoRM);
                        break;
                }
            }
        }
    }

    @FXML
    private void realizarEscrita() {
        RAM ram = RAM.getInstancia();
        GerenciadorProcessadores gerenciador = GerenciadorProcessadores.getInstancia();

        if( inputDadoE.getText().isEmpty() ||
            !Pattern.matches("^[0-9]*$", inputDadoE.getText())||
            inputDadoEscrita.getText().isEmpty() ||
            !Pattern.matches("^[0-9]*$", inputDadoEscrita.getText())
        ) {
            alertLabelE.setText("Preencha todos os campos somente com inteiros");
        } else {
            alertLabelE.setText("");
            int id;
            id = Integer.parseInt(inputDadoE.getText());
            Integer enderecobloco = gerenciador.getProcessador("Processador1").confereDadoCache(id);
            int dadoEscrita = Integer.parseInt(inputDadoEscrita.getText());
            if(enderecobloco != null) {
                MemoriaCache.blocoCache blocoCache = gerenciador.getProcessador("Processador1").getBlocoCache(enderecobloco);
                if(blocoCache.getTag() == MemoriaCache.tags.Invalido) {
                    int res = writeMissInvalido(ram, gerenciador.getProcessador("Processador1"), gerenciador.getProcessador("Processador2"), gerenciador.getProcessador("Processador3"), id, blocoCache, enderecobloco, dadoEscrita);
                    switch (res){
                        case 0:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A leitura foi um readMiss, pois a tag da cache acessada é invalido e não estava em nenhuma outra cache, então foi necessário um acesso a memoria principal e a tag do bloco da cache do processador escolhido é exclusiva.

                                    Mensagem do Jogo:
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 1:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeMiss, pois a tag da cache acessada é invalido, o dado escolhido estava em uma das outras caches e a tag do bloco era modificado, então houve writeBack, a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram a tag do bloco da cache do processador escolhido é modificado

                                    Mensagem do Jogo:
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 2:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeMiss, pois a tag da cache acessada é invalido, o dado escolhido estava em uma das outras a tag era compartilhado ou exclusivo, então a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    """);
                           printaCache(gerenciador, 'e');
                           ramTextAreaE.setText(ram.printMemoria());
                           break;
                    }
                } else {
                    int res = writeHit(dadoEscrita, id, enderecobloco, ram, gerenciador.getProcessador("Processador1"), gerenciador.getProcessador("Processador2"), gerenciador.getProcessador("Processador3"));
                    switch (res) {
                        case 0:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeHit e o bloco da cache estava com a tag modificado, portanto mantém a mesma tag.

                                    Mensagem do Jogo:
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 1:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeHit, o bloco da cache estava com a tag compartilhado e foi encontrado nas outras duas caches que tiveram suas tags dos blocos invalidadas.

                                    Mensagem do Jogo:
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 2:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeHit, o bloco da cache estava com a tag compartilhado e foi encontrado em outra cache que teve sua tag do bloco invalidada.

                                    Mensagem do Jogo:
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 3:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeHit, o bloco da cache estava com a tag compartilhado e foi encontrado em outra cache que teve sua tag do bloco invalidada.

                                    Mensagem do Jogo:
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 4:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeHit e o bloco da cache estava com a tag exclusivo portanto a tag foi alterada para modificado.

                                    Mensagem do Jogo:
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        default:
                            System.out.println("Ocorreu um erro, conferir resultado" + res);
                            break;
                    }
                }
            } else{
                int res = writeMiss(ram, gerenciador.getProcessador("Processador1"), gerenciador.getProcessador("Processador2"), gerenciador.getProcessador("Processador3"), id, dadoEscrita);
                switch (res) {
                    case 0:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeMiss e o bloco da cache não estava em nenhuma outra cache, então foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    case 1:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeMiss, o dado escolhido estava em uma das outras caches e a tag do bloco era modificado, então houve writeBack, a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    case 2:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeMiss, o dado escolhido estava em uma das outras caches e a tag era compartilhado ou exclusivo, então a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    default:
                        System.out.println("Ocorreu um erro, conferir resultado" + res);
                        break;
                }
            }
        }

    }

    @FXML
    private void initialize() {

        RAM ram = RAM.getInstancia();
        GerenciadorProcessadores gerenciador = GerenciadorProcessadores.getInstancia();

        ToggleGroup actionGroup = new ToggleGroup();
        leituraRadioBtn.setToggleGroup(actionGroup);
        escritaRadioBtnLPane.setToggleGroup(actionGroup);

        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(escritaPane);

        ramTextAreaE.setText(ram.printMemoria());
        ramTextAreaL.setText(ram.printMemoria());

        cache1TextAreaL.setText(gerenciador.getProcessador("Processador1").getMemoriaCache().printCache());
        cache2TextAreaL.setText(gerenciador.getProcessador("Processador2").getMemoriaCache().printCache());
        cache3TextAreaL.setText(gerenciador.getProcessador("Processador3").getMemoriaCache().printCache());

        cache1TextAreaE.setText(gerenciador.getProcessador("Processador1").getMemoriaCache().printCache());
        cache2TextAreaE.setText(gerenciador.getProcessador("Processador2").getMemoriaCache().printCache());
        cache3TextAreaE.setText(gerenciador.getProcessador("Processador3").getMemoriaCache().printCache());


        actionGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == leituraRadioBtn) {
                mainStackPane.getChildren().clear();
                mainStackPane.getChildren().add(leituraPane);
            } else if (newValue == escritaRadioBtnLPane) {
                mainStackPane.getChildren().clear();
                mainStackPane.getChildren().add(escritaPane);
            }
        });

    }

    private void printaCache(GerenciadorProcessadores gerenciador,char lado) {
        if (lado == 'l') {
            cache1TextAreaL.setText(gerenciador.getProcessador("Processador1").getMemoriaCache().printCache());
            cache2TextAreaL.setText(gerenciador.getProcessador("Processador2").getMemoriaCache().printCache());
            cache3TextAreaL.setText(gerenciador.getProcessador("Processador3").getMemoriaCache().printCache());
        } else {
            cache1TextAreaE.setText(gerenciador.getProcessador("Processador1").getMemoriaCache().printCache());
            cache2TextAreaE.setText(gerenciador.getProcessador("Processador2").getMemoriaCache().printCache());
            cache3TextAreaE.setText(gerenciador.getProcessador("Processador3").getMemoriaCache().printCache());
        }
    }
}
