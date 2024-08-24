package com.ag.simuladorcachegui;

import entity.MemoriaCache;
import entity.RAM;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class p3Controller extends MainController{

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

    @FXML
    private ImageView backgroundImageL;

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
    private ImageView backgroundImageE;

    @FXML
    private void voltarMenu() throws IOException {
        mudarTela("/com/ag/simuladorcachegui/menu.fxml", buttonVoltar, "/menu.css");
    }

    @FXML
    private void voltarMenuL() throws IOException {
        mudarTela("/com/ag/simuladorcachegui/menu.fxml", buttonVoltarLPane, "/processador3");
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
            Integer enderecobloco2 = gerenciador.getProcessador("Processador3").confereDadoCache(id);
            if (enderecobloco2 != null) {
                MemoriaCache.blocoCache blocoCache = gerenciador.getProcessador("Processador3").getBlocoCache(enderecobloco2);
                if (blocoCache.getTag() == MemoriaCache.tags.Invalido) {
                    int resultado = MainController.readMissInvalido(ram, gerenciador.getProcessador("Processador3"), gerenciador.getProcessador("Processador1"), gerenciador.getProcessador("Processador2"), id, blocoCache, enderecobloco2);
                    switch (resultado) {
                        case 0:
                            relatorioTextAreaL.setText("""

                                    Simulador-Cache Log:
                                    
                                        A leitura foi um readMiss pois a tag da cache acessada é invalido e não estava em nenhuma outra cache, então foi necessário um acesso a memoria principal e a tag do bloco da cache do processador escolhido é exclusiva.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para andar não estava em sua memória nem na memória dos outros jogadores, então foi realizado uma consulta no plano astral, você atrasará uma rodada por conta da sua viagem ao plano astral.
                                    
                                    """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        case 1:
                            relatorioTextAreaL.setText("""
                                   
                                   Simulador-Cache Log:
                                   
                                        A leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava em uma das outras caches e a tag era exclusivo, então essa cache foi requisitada e ambas as tags são compartilhado.
                                   
                                   Mensagem do jogo:
                                   
                                        A área solicitada para andar não estava em sua memória mas estava na memória de um dos outros jogadores, então foi realizado uma consulta na memória desse jogador, você conseguiu poupar uma rodada por obter da memória de outro jogador.
                                   
                                   """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        case 2:
                            relatorioTextAreaL.setText("""
                                    
                                    Simulador-Cache Log:
                                    
                                        leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava em uma das outras caches e a tag era modificado, então houve writeBack, essa cache foi requisitada e ambas as tags são compartilhado.
                                    
                                    Mensagem do jogo:
                                    
                                        A chunk solicitada para andar não estava em sua memória mas estava na memória de um dos outros jogadores, então foi feito uma invasão na mente do outro jogador, você conseguiu poupar uma rodada por obter da memória de outro jogador.
                                    
                                    """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        case 3:
                            relatorioTextAreaL.setText("""
                                    Simulador-Cache Log:
                                    
                                        A leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava nas duas outras caches e ambas com a tag compartilhado, então foi requisitado uma das duas caches e a tag do bloco da cache do processador escolhido é compartilhado.
                                    
                                    Mensagem do jogo:
                                    
                                        A chunk solicitada para andar não estava em sua memória mas estava na memória dos dois outros jogadores, então você invadiu a mente de um dos jogadores para acessar a área que queria alcançar.
                                    
                                    """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        case 4:
                            relatorioTextAreaL.setText("""
                                    Simulador-Cache Log:
                                    
                                        A leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava nas duas outras caches uma com a tag compartilhado e a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.
                                    
                                    Mensagem do jogo:
                                    
                                        A chunk solicitada para andar não estava em sua memória mas estava na memória de um dos outros jogadores, então foi feito uma invasão na mente do outro jogador, você conseguiu poupar uma rodada por obter da memória de outro jogador.
                                    
                                    """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        case 5:
                            relatorioTextAreaL.setText("""
                                    Simulador-Cache Log:
                                    
                                        A leitura foi um readMiss, pois a tag da cache acessada é invalido, o dado estava nas duas outras caches uma com a tag compartilhado e a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.
                                    
                                    Mensagem do jogo:
                                    
                                        A chunk solicitada para andar não estava em sua memória mas estava na memória de um dos outros jogadores, então foi feito uma invasão na mente do outro jogador, você conseguiu poupar uma rodada por obter da memória de outro jogador.
                                    
                                    """);
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                        default:
                            printaCache(gerenciador, 'l');
                            ramTextAreaL.setText(ram.printMemoria());
                            break;
                    }
                } else {
                    int indiceBloco = readHit(gerenciador.getProcessador("Processador3"), id);
                    relatorioTextAreaL.setText("""
                            Simulador-Cache Log:
                            
                                A leitura foi um readHit não foi preciso fazer uma busca na memoria RAM e nem mudar a tag do bloco da cache do processador escolhido.
                            
                            """
                            +
                            gerenciador.getProcessador("Processador3").getMemoriaCache().printPosicaoCache(indiceBloco)
                            +
                            """
                            
                            Mensagem do jogo:
                            
                                Tentativa de andar com sucesso, você se lembra da área e conseguiu avançar para a próxima localização poupando uma rodada por isso.
                            
                            """
                    );
                    printaCache(gerenciador, 'l');
                    ramTextAreaL.setText(ram.printMemoria());
                }
            } else {
                int resultadoRM = readMiss(ram, gerenciador.getProcessador("Processador3"), gerenciador.getProcessador("Processador1"), gerenciador.getProcessador("Processador2"), id);
                switch (resultadoRM) {
                    case 0:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                    A tag do bloco retirado da cache era modificado, então houve a necessidade de realizar o writeBack do bloco retirado.
                                
                                    A leitura foi um readMiss e não estava em nenhuma das outras caches, então foi necessário um acesso a memoria principal e a tag do bloco da cache do processador escolhido é exclusiva.
                                
                                Mensagem do jogo:
                                
                                    A área solicitada para andar não estava em sua memória nem na memória dos outros jogadores, então foi realizado uma consulta no plano astral, você atrasará uma rodada por conta da sua viagem ao plano astral.
                                
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 1:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava em uma das outras caches e a tag era exclusivo, então essa cache foi requisitada e ambas as tags são compartilhado.
                                
                                Mensagem do jogo:
                                
                                    A chunk solicitada para andar não estava em sua memória mas estava na memória de um dos outros jogadores, então foi feito uma invasão na mente do outro jogador, você conseguiu poupar uma rodada por obter da memória de outro jogador.
                                
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 2:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava em uma das outras caches e a tag era modificado, então houve writeBack, essa cache foi requisitada e ambas as tags são compartilhado.
                                
                                Mensagem do jogo:
                                
                                    A chunk solicitada para andar não estava em sua memória mas estava na memória de um dos outros jogadores, então foi feito uma invasão na mente do outro jogador, você conseguiu poupar uma rodada por obter da memória de outro jogador.
                                
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 3:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava em uma das outras caches e a tag era invalido, então a memória RAM foi requistada e a tag é exclusivo.
                                
                                Mensagem do jogo:
                                
                                    A área solicitada para andar não estava em sua memória nem na memória dos outros jogadores, então foi realizado uma consulta no plano astral, você atrasará uma rodada por conta da sua viagem ao plano astral.
                                
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 4:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava nas duas outras caches e ambas com a tag compartilhado, então foi requisitado uma das duas caches e a tag do bloco da cache do processador escolhido é compartilhado.
                                
                                Mensagem do jogo:
                                
                                    A chunk solicitada para andar não estava em sua memória mas estava na memória dos dois outros jogadores, então você invadiu a mente de um dos jogadores para acessar a área que queria alcançar.
                                
                                """);
                        printaCache(gerenciador, 'l');
                        break;
                    case 5:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava nas duas outras caches uma com a tag compartilhado e a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.
                                
                                Mensagem do jogo:
                                
                                    A chunk solicitada para andar não estava em sua memória mas estava na memória de um dos outros jogadores, então foi feito uma invasão na mente do outro jogador, você conseguiu poupar uma rodada por obter da memória de outro jogador.
                                
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 6:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss, o dado estava nas duas outras caches uma com a tag compartilhado e a outra com a tag exclusivo, então foi requisitado a cache com a tag do bloco exclusivo e ambas as tags agora são compartilhado.
                                
                                Mensagem do jogo:
                                
                                    A chunk solicitada para andar não estava em sua memória mas estava na memória de um dos outros jogadores, então foi feito uma invasão na mente do outro jogador, você conseguiu poupar uma rodada por obter da memória de outro jogador.
                                
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 7:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    O bloco que continha o dado ja está inserido na cache com indice da RAM igual, porém este dado foi modificado.
                                
                                Mensagem do jogo:
                                
                                    A área que você desejava se locomover foi alterada, mas você ainda se lembra da região onde ela se encontra, podendo se locomover até a localização desejada.
                                
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    case 8:
                        relatorioTextAreaL.setText("""
                                Simulado-Cache Log:
                                
                                    A leitura foi um readMiss e não estava em nenhuma das outras caches, então foi necessário um acesso a memoria principal e a tag do bloco da cache do processador escolhido é exclusiva.
                                
                                Mensagem do jogo:
                                
                                    A área solicitada para andar não estava em sua memória nem na memória dos outros jogadores, então foi realizado uma consulta no plano astral, você atrasará uma rodada por conta da sua viagem ao plano astral.
                                
                                """);
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
                        break;
                    default:
                        printaCache(gerenciador, 'l');
                        ramTextAreaL.setText(ram.printMemoria());
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
            Integer enderecobloco = gerenciador.getProcessador("Processador3").confereDadoCache(id);
            int dadoEscrita = Integer.parseInt(inputDadoEscrita.getText());
            if(enderecobloco != null) {
                MemoriaCache.blocoCache blocoCache = gerenciador.getProcessador("Processador3").getBlocoCache(enderecobloco);
                if(blocoCache.getTag() == MemoriaCache.tags.Invalido) {
                    int res = writeMissInvalido(ram, gerenciador.getProcessador("Processador3"), gerenciador.getProcessador("Processador1"), gerenciador.getProcessador("Processador2"), id, blocoCache, enderecobloco, dadoEscrita);
                    switch (res){
                        case 0:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A leitura foi um readMiss, pois a tag da cache acessada é invalido e não estava em nenhuma outra cache, então foi necessário um acesso a memoria principal e a tag do bloco da cache do processador escolhido é exclusiva.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 1:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeMiss, pois a tag da cache acessada é invalido, o dado escolhido estava em uma das outras caches e a tag do bloco era modificado, então houve writeBack, a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 2:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeMiss, pois a tag da cache acessada é invalido, o dado escolhido estava em uma das outras a tag era compartilhado ou exclusivo, então a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        default:
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                    }
                } else {
                    int res = writeHit(dadoEscrita, id, enderecobloco, ram, gerenciador.getProcessador("Processador3"), gerenciador.getProcessador("Processador1"), gerenciador.getProcessador("Processador2"));
                    switch (res) {
                        case 0:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeHit e o bloco da cache estava com a tag modificado, portanto mantém a mesma tag.

                                    Mensagem do Jogo:
                                    
                                        A magia de transformação de terreno foi aplicada com sucesso no terreno escolhido, pois ela estava na sua memória, fazendo com que você poupe uma rodada.
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 1:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeHit, o bloco da cache estava com a tag compartilhado e foi encontrado nas outras duas caches que tiveram suas tags dos blocos invalidadas.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória mas estava na memória dos dois outros jogadores, então foi realizado uma consulta na memória de um dos jogadores e a magia foi realizada, poupando uma rodada.
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 2:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeHit, o bloco da cache estava com a tag compartilhado e foi encontrado em outra cache que teve sua tag do bloco invalidada.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória mas estava na memória de um dos outros jogadores, então foi realizado uma consulta na memória desse jogador e a magia foi realizada.
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 3:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeHit, o bloco da cache estava com a tag compartilhado e foi encontrado em outra cache que teve sua tag do bloco invalidada.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória mas estava na memória de um dos outros jogadores, então foi realizado uma consulta na memória desse jogador e a magia foi realizada.
                                    
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        case 4:
                            relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeHit e o bloco da cache estava com a tag exclusivo portanto a tag foi alterada para modificado.

                                    Mensagem do Jogo:
                                    
                                        A magia de transformação de terreno foi aplicada com sucesso no terreno escolhido, pois ela estava na sua memória, fazendo com que você poupe uma rodada.
                                    
                                    """);
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                        default:
                            printaCache(gerenciador, 'e');
                            ramTextAreaE.setText(ram.printMemoria());
                            break;
                    }
                }
            } else {
                int res = writeMiss(ram, gerenciador.getProcessador("Processador3"), gerenciador.getProcessador("Processador1"), gerenciador.getProcessador("Processador2"), id, dadoEscrita);
                switch (res) {
                    case 0:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:

                                        A tag do bloco retirado da cache era modificado, então houve a necessidade de realizar o writeBack do bloco retirado.

                                        A escrita foi um writeMiss e o bloco da cache não estava em nenhuma outra cache, então foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    case 1:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                        A tag do bloco retirado da cache era modificado, então houve a necessidade de realizar o writeBack do bloco retirado.

                                        A escrita foi um writeMiss, o dado escolhido estava em uma das outras caches e a tag do bloco era modificado, então houve writeBack, a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    case 2:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                        O bloco que continha o dado já está inserido na cache com indice da RAM igual, porém este dado foi modificado.

                                        A escrita foi um writeMiss, o dado escolhido estava em uma das outras caches e a tag do bloco era modificado, então houve writeBack, a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    case 3:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                        A tag do bloco retirado da cache era modificado, então houve a necessidade de realizar o writeBack do bloco retirado.
                                    
                                        A escrita foi um writeMiss, o dado escolhido estava em uma das outras caches e a tag era compartilhado ou exclusivo, então a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    case 4:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                        O bloco que continha o dado já está inserido na cache com indice da RAM igual, porém este dado foi modificado.

                                        A escrita foi um writeMiss, o dado escolhido estava em uma das outras caches e a tag era compartilhado ou exclusivo, então a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    case 5:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        A escrita foi um writeMiss, o dado estava em uma das outras caches e a tag era invalido, então a memória RAM foi requistada e a tag é exclusivo.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    case 6:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:
                                    
                                        O bloco que continha o dado ja está inserido na cache com indice da RAM igual, porém este dado foi modificado.
                                    
                                    Mensagem do Jogo:
                                    
                                        A magia de transformação de terreno foi usada com sucesso pois você já tinha em sua memória a área que foi afetada.
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    case 7:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:

                                        A escrita foi um writeMiss e o bloco da cache não estava em nenhuma outra cache, então foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                    case 8:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:

                                        A escrita foi um writeMiss, o dado escolhido estava em uma das outras caches e a tag do bloco era modificado, então houve writeBack, a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.

                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                    case 9:
                        relatorioTextAreaE.setText("""
                                    Simulador-Cache Log:

                                        A escrita foi um writeMiss, o dado escolhido estava em uma das outras caches e a tag era compartilhado ou exclusivo, então a tag desse bloco foi invalidada, foi necessário um acesso à memoria ram e a tag do bloco da cache do processador escolhido é modificado.


                                    Mensagem do Jogo:
                                    
                                        A área solicitada para realizar a magia de transformação de terreno não existia na sua memória nem na dos outros jogadores, então foi realizado uma consulta no plano astral e sua magia conseguirá ser utilizada na próxima rodada, tendo que esperar por conta da viagem ao plano astral.
                                    
                                    """);
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                    default:
                        printaCache(gerenciador, 'e');
                        ramTextAreaE.setText(ram.printMemoria());
                        break;
                }
            }
        }
    }

    @FXML
    private void atualizaInfosE(){
        GerenciadorProcessadores gerenciador = GerenciadorProcessadores.getInstancia();
        RAM ram = RAM.getInstancia();
        ramTextAreaE.setText(ram.printMemoria());

        cache1TextAreaE.setText(gerenciador.getProcessador("Processador1").getMemoriaCache().printCache());
        cache2TextAreaE.setText(gerenciador.getProcessador("Processador2").getMemoriaCache().printCache());
        cache3TextAreaE.setText(gerenciador.getProcessador("Processador3").getMemoriaCache().printCache());
    }

    @FXML
    private void atualizaInfosL(){
        GerenciadorProcessadores gerenciador = GerenciadorProcessadores.getInstancia();
        RAM ram = RAM.getInstancia();

        ramTextAreaL.setText(ram.printMemoria());
        cache1TextAreaL.setText(gerenciador.getProcessador("Processador1").getMemoriaCache().printCache());
        cache2TextAreaL.setText(gerenciador.getProcessador("Processador2").getMemoriaCache().printCache());
        cache3TextAreaL.setText(gerenciador.getProcessador("Processador3").getMemoriaCache().printCache());
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

        File fileBackgroundL = new File("src/images/p3Background.png");
        Image imageBackgroundL = new Image(fileBackgroundL.toURI().toString());
        backgroundImageL.setImage(imageBackgroundL);

        File fileBackgroundE = new File("src/images/p3Background.png");
        Image imageBackgroundE = new Image(fileBackgroundE.toURI().toString());
        backgroundImageE.setImage(imageBackgroundE);

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
