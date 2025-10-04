package template;

import br.com.davidbuzatto.jsge.animation.frame.FrameByFrameAnimation;
import br.com.davidbuzatto.jsge.animation.frame.ImageAnimationFrame;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.image.Image;
import br.com.davidbuzatto.jsge.imgui.GuiButton;
import br.com.davidbuzatto.jsge.imgui.GuiCheckBox;
import br.com.davidbuzatto.jsge.imgui.GuiComponent;
import br.com.davidbuzatto.jsge.imgui.GuiLabelButton;
import br.com.davidbuzatto.jsge.sound.Sound;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Paint;
import java.net.URI;
import java.util.ArrayDeque;
import java.util.ArrayList;

import java.util.Stack;
import java.util.List;
import java.util.Deque;
import java.util.Queue;
import java.util.Random;

/**
 * Feito no JSGE
 *
 * @author Prof. Dr. David Buzatto
 *
 * Simulador de Estruturas de Dados
 * @author Eddie
 */
public class Main extends EngineFrame {

    //Variáveis Globais
    private double cronometro;
    private Tela tela;
    private boolean cronometrar;
    private Paint corJanelaBau;
    private Paint corDosContainers;
    private final int INICIAR = 1;
    private final int DESENHAR = 2;
    private final int ATUALIZAR = 3;

    //Listas de Componentes
    private List<GuiComponent> componentesGlobais;
    private List<GuiComponent> componentesTelaInicial;
    private List<GuiComponent> componentesPilha;
    private List<GuiComponent> componentesFila;
    private List<GuiComponent> componentesDeque;
    private List<GuiComponent> componentesLista;

    //Componentes - Globais
    private GuiButton btnVoltar;
    
    //Enumeradores de Tela
    private enum Tela{ INICIAL, PILHA, FILA, DEQUE, LISTA };

    //Componentes - Tela Inicial
    private GuiButton btnSimPilha;
    private GuiButton btnSimFila;
    private GuiButton btnSimDeque;
    private GuiButton btnSimLista;

    private GuiLabelButton btnLink;

    //Componentes - Tela Simulação Pilhas
    private GuiButton btnAddRetangulo;
    private GuiButton btnAddCirculo;
    private GuiButton btnAddTriangulo;
    private GuiButton btnDesfazer;
    private GuiButton btnRefazer;

    //Componentes - Tela Simulação Filas
    private GuiButton btnAddNumero;
    private GuiButton btnRmvNumero;
    private int contadorValores = 0;
    private int indexFilaCircular = 0;

    //Componentes - Tela Simulação Deque
    private GuiButton btnAddDireita;
    private GuiButton btnAddEsquerda;
    private GuiButton btnAddCima;
    private GuiButton btnAddBaixo;
    private GuiButton btnRmvDireita;
    private GuiButton btnRmvEsquerda;
    private GuiButton btnRmvCima;
    private GuiButton btnRmvBaixo;
    private int xIni = 141;
    private int yIni = 191;
    private int xFim = 91;
    private int yFim = 91;
    private final int[] contadorBtns = new int[8];

    //Componentes - Tela Simulação Lista
    private GuiButton btnAbrirBau;
    private GuiButton btnRemover;
    private GuiButton btnDireita;
    private GuiButton btnEsquerda;
    private int posInventario;
    private int raridade;
    private boolean animBau;
    private GuiCheckBox checkAnimBau;
    private GuiCheckBox checkRmvDinamico;
    private GuiCheckBox checkInsDirect;
    private GuiCheckBox checkVisuSequencial;
    private FrameByFrameAnimation<ImageAnimationFrame> bauAnim;
    private GuiLabelButton btnLinkBau;
    private Sound bruh;
    private Sound woah;
    private Sound bttb;

    //Estruturas de Dados
    private Stack<String> pilhaDesfazer;
    private Stack<String> pilhaRefazer;
    private Queue<Integer> fila;
    private Deque<String> dequeED;
    private Deque<String> dequeCB;
    private List<Integer> lista;

    //Construtor
    public Main() {

        super(800, 480, "Simulador de Estruturas de Dados", 60, true, false, false, false, false, false);

    }

//----------< Criar >----------//
    @Override
    public void create() {

        //Variáveis Globais
        tela = Tela.INICIAL;
        useAsDependencyForIMGUI();

        //Componentes Globais
        componentesGlobais(INICIAR);

        //Inicializar Telas (queria criar apenas com o switch, mas não pode, precisamos criar todas as telas no inicio do programa)
        criarTelaInicial();
        criarSimulacaoPilha();
        criarSimulacaoFila();
        criarSimulacaoDeque();
        criarSimulacaoLista();

    }

//----------< Atualizar >----------//
    @Override
    public void update(double delta) {

        //Variáveis Globais
        if (cronometrar) {

            cronometro += delta;

            if (cronometro > 0.7 && cronometro < 0.73 && raridade == 4) {
                woah.play();
            }

            //Som para itens não raros
            if (cronometro > 1.40 && cronometro < 1.43 && raridade != 4 && raridade != 3) {
                bruh.play();
            }

            //Esperar a animação do baú acabar antes de inserir o item / ativar o botão novamente
            if (cronometro >= 2) {

                if (raridade == 3) {
                    bttb.play();
                }

                inserirItem(raridade);
                cronometrar = false;
            }
        }

        //Atualização - Globais
        componentesGlobais(ATUALIZAR, delta);

        //Atualização -  Telas
        switch (tela) {
            case INICIAL ->
                atualizarTelaInicial(delta);
            case PILHA ->
                atualizarSimulacaoPilha(delta);
            case FILA ->
                atualizarSimulacaoFila(delta);
            case DEQUE ->
                atualizarSimulacaoDeque(delta);
            case LISTA ->
                atualizarSimulacaoLista(delta);
        }

    }

//----------< Desenhar >----------//
    @Override
    public void draw() {

        //Desenhar - Globais
        componentesGlobais(DESENHAR);

        //Desenhar - Telas
        switch (tela) {
            case INICIAL ->
                desenharTelaInicial();
            case PILHA ->
                desenharSimulacaoPilha();
            case FILA ->
                desenharSimulacaoFila();
            case DEQUE ->
                desenharSimulacaoDeque();
            case LISTA ->
                desenharSimulacaoLista();
        }

    }

//----------< Complementares >----------//
    /*
    *     COMPONENTES GLOBAIS
     */
    public void componentesGlobais(int opcao) {

        switch (opcao) {
            case INICIAR:

                componentesGlobais = new ArrayList<>();

                btnVoltar = new GuiButton(10, 450, 20, 20, "↩");

                componentesGlobais.add(btnVoltar);

                break;

            case DESENHAR:
                
                corJanelaBau = new Color(0xFFC9DE);
                corDosContainers = WHITE;
                
                switch (tela){
                    case INICIAL -> clearBackground(new Color(0xFFFFD1));
                    case PILHA -> clearBackground(new Color(0xCED1F8));
                    case FILA -> clearBackground(new Color(0xECD2E0));
                    case DEQUE -> clearBackground(new Color(0xDBFFD6));
                    case LISTA -> clearBackground(new Color(0xFFD7D9));
                        
                }

                if (!tela.equals(tela.INICIAL) && !componentesGlobais.isEmpty()) {
                    for (GuiComponent c : componentesGlobais) {
                        c.draw();
                    }
                    
                    //Desenhar o Log                    
                    desenharLog();

                }

                break;






        }

    }

    public void componentesGlobais(int opcao, double delta) {

        if (opcao == ATUALIZAR && !tela.equals(tela.INICIAL) && !componentesGlobais.isEmpty()) {
            for (GuiComponent c : componentesGlobais) {
                c.update(delta);
            }
        }

        if (btnVoltar.isMousePressed()) {
            //Resetar Pilha
            pilhaDesfazer.clear();
            
            //Resetar Fila
            fila.clear();
            contadorValores = 0;
            indexFilaCircular = 0;
            
            //Resetar Deque
            dequeED.clear();
            dequeCB.clear();
            
            xIni = 141;
            yIni = 191;
            xFim = 91;
            yFim = 91;
            
            for (int i = 0; i < contadorBtns.length; i++){
                contadorBtns[i] = 0;
            }
            
            //Resetar Lista
            for (int i = 0; i < 16; i++) {
                if (lista.get(i) != 0) {
                    lista.set(i, 0);
                }
            }
            posInventario = 0;
            
            //Voltar para a tela inicial
            tela = tela.INICIAL;
        }

    }
    
    public void desenharLog(){
        
        String titulo = "Explicação da Estrutura";
        int x = 0;
        int y = 0;
        
        switch (tela) {
            case PILHA:
                x = 480; y = 190;
                drawText(titulo, 498, 175, 15, BLACK);
                fillRectangle(x, y, 240, 140, corDosContainers);
                drawRectangle(x, y, 240, 140, BLACK);
                infoEstrutura(x, y, "A Pilha é uma Estrutura   de Dados que organiza da- dos de forma sequencial,  seguindo a regra LIFO, ou seja, o último a entrar é o primeiro a sair.");
                break;
            case FILA:
                x = 463; y = 255;
                drawText(titulo, 483, 240, 15, BLACK);
                fillRectangle(x, y, 245, 195, corDosContainers);
                drawRectangle(x, y, 245, 195, BLACK);
                infoEstrutura(x, y, "A Fila é uma Estrutura de Dados que se organiza se- guindo a regra FIFO, ou   seja, o primeiro a entrar é o primeiro a sair. Há   também a Circular que tem como diferença a conexão  do último elemento ao pri-meiro.");
                break;
            case DEQUE:
                x = 498; y = 255;
                drawText(titulo, 523, 240, 15, BLACK);
                fillRectangle(x, y, 250, 180, corDosContainers);
                drawRectangle(x, y, 250, 180, BLACK);
                infoEstrutura(x, y, "O Deque é a abreviação de \"Double Ended Queue\", uma Estrutura de Dados que re-mete à Lista, só que com apossibilidade de inserir eremover elementos tanto aoseu início quanto ao seu  fim.");
                break;
            case LISTA:
                x = 18; y = 245;
                drawText(titulo, 38, 230, 15, BLACK);
                fillRectangle(x, y, 250, 140, corDosContainers);
                drawRectangle(x, y, 250, 140, BLACK);
                infoEstrutura(x, y, "A Lista é uma das Estru-  turas de Dados mais flexí-veis, já que os elementos podem ser inseridos ou re-movidos em qualquer uma   das posições.");
                break;
        }

    }
    
    public void infoEstrutura(int xIni, int yIni, String msg){
        int numChar = 26;
        int espaco = 0;
        
        for (int i = 0; i < msg.length(); i += numChar){
            drawText(msg.substring(i, Math.min(i + numChar, msg.length())), xIni + 10, yIni + 15 + espaco, 15, BLACK);
            espaco += 20;
        }
        
    }

    public void drawOutlinedText(String text, int posX, int posY, int fontSize, Paint color, int outlineSize, Paint outlineColor) {
        drawText(text, posX - 2, posY + 2, fontSize, GRAY);
        drawText(text, posX - outlineSize, posY - outlineSize, fontSize, outlineColor);
        drawText(text, posX + outlineSize, posY - outlineSize, fontSize, outlineColor);
        drawText(text, posX - outlineSize, posY + outlineSize, fontSize, outlineColor);
        drawText(text, posX + outlineSize, posY + outlineSize, fontSize, outlineColor);
        drawText(text, posX, posY, fontSize, color);
    }

    /*
    *     TELA INICIAL
     */
    public void criarTelaInicial() {

        //Lista de Componentes
        componentesTelaInicial = new ArrayList<>();

        //Valores iniciais para organizar os botões
        int x = 320;
        int y = 260;

        //Criação dos Botões
        btnSimPilha = new GuiButton(x, y, 150, 30, "Simulação de Pilha");
        btnSimFila = new GuiButton(x, y + 40, 150, 30, "Simulação de Fila");
        btnSimDeque = new GuiButton(x, y + 80, 150, 30, "Simulação de Deque");
        btnSimLista = new GuiButton(x, y + 120, 150, 30, "Simulação de Lista");
        btnLink = new GuiLabelButton(670, 455, 120, 20, "@EddiePricefield");

        //Adicionar componentes à Lista de Componentes
        componentesTelaInicial.add(btnSimPilha);
        componentesTelaInicial.add(btnSimFila);
        componentesTelaInicial.add(btnSimDeque);
        componentesTelaInicial.add(btnSimLista);
        componentesTelaInicial.add(btnLink);

    }

    public void desenharTelaInicial() {

        //Título
        drawOutlinedText("SimED", 170, 80, 150, ORANGE, 1, BLACK);

        //Componentes
        if (tela.equals(tela.INICIAL) && !componentesTelaInicial.isEmpty()) {
            for (GuiComponent c : componentesTelaInicial) {
                c.draw();
            }
        }

    }

    public void atualizarTelaInicial(double delta) {

        //Atualizando os Componentes
        if (tela.equals(tela.INICIAL) && !componentesTelaInicial.isEmpty()) {
            for (GuiComponent c : componentesTelaInicial) {
                c.update(delta);
            }
        }

        //Ações - Clique do Mouse
        if (btnSimPilha.isMousePressed()) {
            tela = tela.PILHA;
        }

        if (btnSimFila.isMousePressed()) {
            tela = tela.FILA;
        }

        if (btnSimDeque.isMousePressed()) {
            tela = tela.DEQUE;
        }        if (btnSimLista.isMousePressed()) {


            animBau = false;
            tela = tela.LISTA;
        }

        if (btnLink.isMousePressed()) {

            try {
                URI link = new URI("https://github.com/EddiePricefield");
                Desktop.getDesktop().browse(link);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /*
    *     TELA DE SIMULAÇÃO DE PILHA
     */
    public void criarSimulacaoPilha() {

        //Lista de Componentes
        componentesPilha = new ArrayList<>();

        //Estruturas de Dados
        pilhaDesfazer = new Stack<>();
        pilhaRefazer = new Stack<>();

        //Valores iniciais para organizar os botões
        int x = 40;
        int y = 160;

        //Criação dos Botões
        btnAddRetangulo = new GuiButton(x, y, 75, 30, "Retângulo");
        btnAddCirculo = new GuiButton(x, y + 50, 75, 30, "Círculo");
        btnAddTriangulo = new GuiButton(x, y + 100, 75, 30, "Triângulo");

        btnDesfazer = new GuiButton(x + 160, y + 200, 150, 30, "Desfazer");
//        btnRefazer = new GuiButton(x + 230, y + 200, 75, 30, "Refazer");

        //Adicionar componentes à Lista de Componentes
        componentesPilha.add(btnAddRetangulo);
        componentesPilha.add(btnAddCirculo);
        componentesPilha.add(btnAddTriangulo);
        componentesPilha.add(btnDesfazer);
//        componentesPilha.add(btnRefazer);

    }

    public void desenharSimulacaoPilha() {

        //Título
        drawOutlinedText("Simulação Pilha", 176, 30, 50, new Color(0x9092ad), 1, BLACK);

        //Desenhar o GRID e as Formas
        int x = 150;
        int y = 100;
        int tamanho = 50;

        drawText("EMPILHAR", 43, 130, 15, BLACK);
        drawText("DESEMPILHAR", 230, 400, 15, BLACK);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                fillRectangle(x + tamanho * j, y + tamanho * i, tamanho, tamanho, corDosContainers);
                drawRectangle(x + tamanho * j, y + tamanho * i, tamanho, tamanho, BLACK);
            }
        }

        desenharForma();

        //Desenhar o Visual dos Stacks
        drawText("Exibição Horizontal da Pilha", 475, 100, 15, BLACK);
        fillRectangle(450, 115, 295, 35, corDosContainers);
        drawRectangle(450, 115, 295, 35, BLACK);

//        drawText("Pilha: Refazer", 535, 175, 15, RED);
//        drawRectangle(450, 190, 295, 35, BLACK);
        desenharPilhas();

        //Desenhar Painel de Informações
        //Componentes
        if (tela.equals(tela.PILHA) && !componentesPilha.isEmpty()) {
            for (GuiComponent c : componentesPilha) {
                c.draw();
            }
        }

    }

    public void atualizarSimulacaoPilha(double delta) {

        //Componentes
        if (tela.equals(tela.PILHA) && !componentesPilha.isEmpty()) {
            for (GuiComponent c : componentesPilha) {
                c.update(delta);
            }
        }

        if (btnAddRetangulo.isMousePressed()) {
            if (pilhaDesfazer.size() < 25) {
                pilhaDesfazer.push("Retangulo");
            }
            pilhaRefazer.clear();
        }

        if (btnAddCirculo.isMousePressed()) {
            if (pilhaDesfazer.size() < 25) {
                pilhaDesfazer.push("Circulo");
            }
            pilhaRefazer.clear();
        }

        if (btnAddTriangulo.isMousePressed()) {
            if (pilhaDesfazer.size() < 25) {
                pilhaDesfazer.push("Triangulo");
            }
            pilhaRefazer.clear();
        }

        if (btnDesfazer.isMousePressed()) {
            if (!pilhaDesfazer.isEmpty()) {
                pilhaRefazer.push(pilhaDesfazer.pop());
            }
        }

//        if (btnRefazer.isMousePressed()) {
//            if (!pilhaRefazer.isEmpty()) {
//                pilhaDesfazer.push(pilhaRefazer.pop());
//            }
//        }
        //Atualizar o Desenho das Formas     
    }

    public void desenharForma() {

        for (int i = 0; i < pilhaDesfazer.size(); i++) {

            String forma = pilhaDesfazer.get(i);

            int linha = i / 5;
            int coluna = i % 5;
            int tamanho = 40;

            int xIni = 155 + 50 * coluna;
            int yIni = 105 + 50 * linha;

            Paint cor;

            //Mudar a cor do Topo e da Cauda
            if (pilhaDesfazer.size() - 1 == 0) {
                cor = PURPLE;
            } else if (i == 0) {
                cor = BLUE;
            } else if (i == pilhaDesfazer.size() - 1) {
                cor = RED;
            } else {
                cor = PINK;
            }

            switch (forma) {
                case "Retangulo":
                    fillRectangle(xIni, yIni, tamanho, tamanho, cor);
                    drawRectangle(xIni, yIni, tamanho, tamanho, BLACK);
                    break;
                case "Circulo":
                    fillCircle(xIni + tamanho / 2, yIni + tamanho / 2, tamanho / 2, cor);
                    drawCircle(xIni + tamanho / 2, yIni + tamanho / 2, tamanho / 2, BLACK);
                    break;
                case "Triangulo":
                    fillTriangle(xIni, yIni + tamanho, xIni + tamanho, yIni + tamanho, xIni + tamanho / 2, yIni, cor);
                    drawTriangle(xIni, yIni + tamanho, xIni + tamanho, yIni + tamanho, xIni + tamanho / 2, yIni, BLACK);
                    break;
            }
        }
    }

    public void desenharPilhas() {

        int largura = 10;
        int altura = 15;

        Paint cor;

        //Pilha Desfazer
        for (int i = 0; i < pilhaDesfazer.size(); i++) {
            int x = 460 + (largura + 1) * i;
            int y = 125;

            //Mudar a cor do Topo e da Cauda
            if (pilhaDesfazer.size() - 1 == 0) {
                cor = PURPLE;
            } else if (i == 0) {
                cor = BLUE;
            } else if (i == pilhaDesfazer.size() - 1) {
                cor = RED;
            } else {
                cor = PINK;
            }

            fillRectangle(x, y, largura, altura, cor);
        }

//        //Pilha Refazer
//        for (int i = 0; i < pilhaRefazer.size(); i++) {
//            int x = 460 + (largura + 1) * i;
//            int y = 200;
//            fillRectangle(x, y, largura, altura, RED);
//        }
    }

    /*
    *     TELA DE SIMULAÇÃO DE FILA
     */
    public void criarSimulacaoFila() {

        //Lista de Componentes
        componentesFila = new ArrayList<>();

        //Estrutura de Dado
        fila = new ArrayDeque<>();

        //Variáveis iniciais para criação dos botões
        int x = 60;
        int y = 170;

        //Criação dos Botões
        btnRmvNumero = new GuiButton(x, y, 165, 30, "Remover Valor da Fila");
        btnAddNumero = new GuiButton(x + 180, y, 165, 30, "Adicionar Valor à Fila");

        //Adicionar Componentes à Lista de Componentes
        componentesFila.add(btnAddNumero);
        componentesFila.add(btnRmvNumero);

    }

    public void desenharSimulacaoFila() {

        //Título
        drawOutlinedText("Simulação Fila", 176, 30, 50, new Color(0xa5939c), 1, BLACK);

        //Desenhando Fila
        for (int i = 1; i < 14; i++) {
            fillRectangle(53 * i, 100, 50, 50, corDosContainers);
            drawRectangle(53 * i, 100, 50, 50, BLACK);
        }

        desenharValores();
        
        //Desenhando Fila Circular
        double centroX = 220, centroY = 330, raio = 100;

        for (int i = 0; i < 13; i++) {
            double x = centroX + raio * Math.cos(Math.toRadians(i * 360/13));
            double y = centroY + raio * Math.sin(Math.toRadians(i * 360/13));
            fillRectangle(x, y, 30, 30, corDosContainers);
            drawRectangle(x, y, 30, 30, BLACK);
        }
        
        desenharValoresCirculares();

        //Variáveis para auxiliar
        int x = 95;
        int y = 210;

        //Desenhar os Títulos dos Botões
        drawText("DESEMPILHAR", x, y, 15, BLACK);
        drawText("EMPILHAR", x + 193, y, 15, BLACK);
        drawText("FILA", x + 115, y + 110, 20, BLACK);
        drawText("CIRCULAR", x + 90, y + 130, 20, BLACK);

        //Desenhar o Visual da Fila
        drawText("Exibição Horizontal da Fila", x + 370, y - 40, 15, BLACK);
        fillRectangle(x + 388, y - 20, 200, 35, corDosContainers);
        drawRectangle(x + 388, y - 20, 200, 35, BLACK);

        desenharFila();

        //Componentes
        if (tela.equals(tela.FILA) && !componentesFila.isEmpty()) {
            for (GuiComponent c : componentesFila) {
                c.draw();
            }
        }

    }

    public void atualizarSimulacaoFila(double delta) {

        //Componentes
        if (tela.equals(tela.FILA) && !componentesFila.isEmpty()) {
            for (GuiComponent c : componentesFila) {
                c.update(delta);
            }
        }

        //Ações - Botões do Mouse
        if (btnAddNumero.isMousePressed()) {
            if (fila.size() < 13 && contadorValores < 100) {
                fila.add(contadorValores++);
            }
        }

        if (btnRmvNumero.isMousePressed()) {
            if (!fila.isEmpty()) {
                if (fila.size() != 1){
                    indexFilaCircular = (indexFilaCircular + 1) % 13;
                }
                fila.poll();
            }
        }

    }

    public void desenharValores() {

        int i = 0;
        
        int y = 118;
        int x = 63;

        for (int valor : fila) {
            if (i == 0 && fila.size() == 1) {
                drawOutlinedText(String.format("%02d", valor), (53 * i + x), y, 25, PURPLE, 1, BLACK);
            } else if (i == 0) {
                drawOutlinedText(String.format("%02d", valor), (53 * i + x), y, 25, RED, 1, BLACK);
            } else if (i == fila.size() - 1) {
                drawOutlinedText(String.format("%02d", valor), (53 * i + x), y, 25, BLUE, 1, BLACK);
            } else {
                drawOutlinedText(String.format("%02d", valor), (53 * i + x), y, 25, PINK, 1, BLACK);
            }

            i++;
        }

    }
    
    public void desenharValoresCirculares(){
        
        double centroX = 220;
        double centroY = 330;
        double raio = 100;
        double lado = 30;
        
        for (int i = 0; i < fila.size(); i++) {
            double angulo = Math.toRadians(((indexFilaCircular + i) % 13) * 360 / 13);
            double x = centroX + raio * Math.cos(angulo);
            double y = centroY + raio * Math.sin(angulo);
            
            if (fila.size() == 1){
                fillRectangle(x, y, lado, lado, PURPLE);
                drawRectangle(x, y, lado, lado, BLACK);
            } else if (i == 0){
                fillRectangle(x, y, lado, lado, RED);
                drawRectangle(x, y, lado, lado, BLACK);
            } else if (i == fila.size() - 1){
                fillRectangle(x, y, lado, lado, BLUE);
                drawRectangle(x, y, lado, lado, BLACK);
            } else{
                fillRectangle(x, y, lado, lado, PINK);
                drawRectangle(x, y, lado, lado, BLACK);
            }
            
        }
        
    }

    public void desenharFila() {

        int largura = 10;
        int altura = 15;

        Paint cor;

        //Pilha Desfazer
        for (int i = 0; i < fila.size(); i++) {
            int x = 510 + (largura + 1) * i;
            int y = 200;

            //Mudar a cor do Topo e da Cauda
            if (fila.size() - 1 == 0) {
                cor = PURPLE;
            } else if (i == 0) {
                cor = RED;
            } else if (i == fila.size() - 1) {
                cor = BLUE;
            } else {
                cor = PINK;
            }

            fillRectangle(x, y, largura, altura, cor);
        }

    }

    /*
    *     TELA DE SIMULAÇÃO DE DEQUE
     */
    public void criarSimulacaoDeque() {

        //Lista de Componentes
        componentesDeque = new ArrayList<>();

        //Estrutura de Dado
        dequeED = new ArrayDeque<>();
        dequeCB = new ArrayDeque<>();

        //Valores iniciais para organizar os botões
        int x = 400;
        int distBtns = 100;
        int y = 190;

        //Criação dos Botões
        btnAddDireita = new GuiButton(x, y, 20, 15, "→");
        btnAddEsquerda = new GuiButton(x - 50, y, 20, 15, "←");
        btnAddCima = new GuiButton(x - 25, y - 20, 20, 15, "↑");
        btnAddBaixo = new GuiButton(x - 25, y + 20, 20, 15, "↓");

        btnRmvDireita = new GuiButton(x, y + distBtns, 20, 15, "←");
        btnRmvEsquerda = new GuiButton(x - 50, y + distBtns, 20, 15, "→");
        btnRmvCima = new GuiButton(x - 25, y - 20 + distBtns, 20, 15, "↓");
        btnRmvBaixo = new GuiButton(x - 25, y + 20 + distBtns, 20, 15, "↑");

        //Adicionar Componentes à Lista de Componentes
        componentesDeque.add(btnAddDireita);
        componentesDeque.add(btnAddEsquerda);
        componentesDeque.add(btnAddCima);
        componentesDeque.add(btnAddBaixo);
        componentesDeque.add(btnRmvDireita);
        componentesDeque.add(btnRmvEsquerda);
        componentesDeque.add(btnRmvCima);
        componentesDeque.add(btnRmvBaixo);

    }

    public void desenharSimulacaoDeque() {

        //Título
        drawOutlinedText("Simulação Deque", 176, 30, 50, new Color(0x99b295), 1, BLACK);

        //Desenhar Grid e Quadrado
        fillRectangle(50, 100, 273, 273, corDosContainers);
        drawRectangle(50, 100, 273, 273, BLACK);

        drawRectangle(141, 191, 91, 91, GRAY);
        drawRectangle(xIni, yIni, xFim, yFim, PINK);

        //Variaveis Auxiliares
        int x = 350;
        int y = 150;

        //Desenhar o Visual dos Deques
        drawText("Exibição Horizontal dos Deques", x + 140, y - 30, 15, BLACK);
        fillRectangle(x + 125, y - 10, 295, 35, corDosContainers);
        drawRectangle(x + 125, y - 10, 295, 35, BLACK);
        fillRectangle(x + 125, y + 40, 295, 35, corDosContainers);
        drawRectangle(x + 125, y + 40, 295, 35, BLACK);

        desenharDeque();

        //Desenhar os Títulos dos Botões
        drawText("EMPILHAR", x, y, 15, BLACK);
        drawText("DESEMPILHAR", x - 14, y + 100, 15, BLACK);

        //Componentes
        if (tela.equals(tela.DEQUE) && !componentesDeque.isEmpty()) {
            for (GuiComponent c : componentesDeque) {
                c.draw();
            }
        }

    }

    public void atualizarSimulacaoDeque(double delta) {

        //Componentes
        if (tela.equals(tela.DEQUE) && !componentesDeque.isEmpty()) {
            for (GuiComponent c : componentesDeque) {
                c.update(delta);
            }
        }

        //Ação - Botões
        if (btnAddDireita.isMousePressed() && contadorBtns[0] < 13) {
            xFim += 7;
            contadorBtns[0]++;
            dequeED.addLast("Direita");
            if (contadorBtns[4] > 0) {
                contadorBtns[4]--;
            }
        }

        if (btnAddEsquerda.isMousePressed() && contadorBtns[1] < 13) {
            xIni -= 7;
            xFim += 7;
            contadorBtns[1]++;
            dequeED.addFirst("Esquerda");
            if (contadorBtns[5] > 0) {
                contadorBtns[5]--;
            }
        }

        if (btnAddCima.isMousePressed() && contadorBtns[2] < 13) {
            yIni -= 7;
            yFim += 7;
            contadorBtns[2]++;
            dequeCB.addLast("Cima");
            if (contadorBtns[6] > 0) {
                contadorBtns[6]--;
            }
        }

        if (btnAddBaixo.isMousePressed() && contadorBtns[3] < 13) {
            yFim += 7;
            contadorBtns[3]++;
            dequeCB.addFirst("Baixo");
            if (contadorBtns[7] > 0) {
                contadorBtns[7]--;
            }
        }

        if (btnRmvDireita.isMousePressed() && contadorBtns[0] > 0) {
            xFim -= 7;
            contadorBtns[0]--;
            dequeED.pollLast();
            if (contadorBtns[4] < 13) {
                contadorBtns[4]++;
            }
        }

        if (btnRmvEsquerda.isMousePressed() && contadorBtns[1] > 0) {
            xIni += 7;
            xFim -= 7;
            contadorBtns[1]--;
            dequeED.pollFirst();
            if (contadorBtns[5] < 13) {
                contadorBtns[5]++;
            }
        }

        if (btnRmvCima.isMousePressed() && contadorBtns[2] > 0) {
            yIni += 7;
            yFim -= 7;
            contadorBtns[2]--;
            dequeCB.pollLast();
            if (contadorBtns[6] < 13) {
                contadorBtns[6]++;
            }
        }

        if (btnRmvBaixo.isMousePressed() && contadorBtns[3] > 0) {
            yFim -= 7;
            contadorBtns[3]--;
            dequeCB.pollFirst();
            if (contadorBtns[7] < 13) {
                contadorBtns[7]++;
            }
        }

    }

    public void desenharDeque() {

        int largura = 10;
        int altura = 15;

        Paint cor;

        int i = 0, j = 0;
        for (String valor : dequeED) {
            int x = 480 + (largura + 1) * i;
            int y = 200;

            //Mudar a cor do Topo e da Cauda
            if (valor.equals("Direita")) {
                cor = DARKGREEN;
            } else {
                cor = GREEN;
            }

            fillRectangle(x, y, largura, altura, cor);
            i++;
        }

        for (String valor : dequeCB) {
            int x = 480 + (largura + 1) * j;
            int y = 150;

            //Mudar a cor do Topo e da Cauda
            if (valor.equals("Cima")) {
                cor = DARKPURPLE;
            } else {
                cor = PURPLE;
            }

            fillRectangle(x, y, largura, altura, cor);
            j++;
        }

    }

    /*
    *     TELA DE SIMULAÇÃO DE LISTA
     */
    public void criarSimulacaoLista() {

        //Lista de Componentes
        componentesLista = new ArrayList<>();

        //Estrutura de Dado
        lista = new ArrayList<>(16);

        for (int i = 0; i < 16; i++) {
            lista.add(0);
        }

        //Variáveis para ajudar na organização
        int x = 355;
        int y = 370;

        //Criação dos Botões
        btnEsquerda = new GuiButton(x, y, 20, 30, "←");
        btnRemover = new GuiButton(x + 25, y, 70, 30, "Remover");
        btnDireita = new GuiButton(x + 100, y, 20, 30, "→");

        btnAbrirBau = new GuiButton(x + 285, y - 90, 70, 30, "Abrir");
        btnLinkBau = new GuiLabelButton(x + 370, y - 125, 35, 20, "@Digs");
        checkAnimBau = new GuiCheckBox(x + 235, y - 125, 35, 20, "Animação");
        checkRmvDinamico = new GuiCheckBox(x - 20, y + 60, 35, 20, "Remoção Sequencial");        
        checkInsDirect = new GuiCheckBox(x + 235, y - 20, 35, 20, "Inserção Direcionada");        
        checkVisuSequencial = new GuiCheckBox(x - 310, y - 185, 35, 20, "Visualização Sequencial");        
        
        checkAnimBau.setSelected(true);
        checkRmvDinamico.setSelected(false);
        checkInsDirect.setSelected(false);
        checkVisuSequencial.setSelected(true);
        
        criarAnimacoes();

        componentesLista.add(btnEsquerda);
        componentesLista.add(btnRemover);
        componentesLista.add(btnDireita);
        componentesLista.add(btnAbrirBau);
        componentesLista.add(btnLinkBau);
        componentesLista.add(checkAnimBau);
        componentesLista.add(checkRmvDinamico);
        componentesLista.add(checkInsDirect);
        componentesLista.add(checkVisuSequencial);

    }

    public void desenharSimulacaoLista() {

        //Título
        drawOutlinedText("Simulação Lista", 176, 30, 50, new Color(0xb29697), 1, BLACK);

        //Desenhar Janelas Principais
        int x = 285;
        int y = 100;
        int tamanho = 65;

        //Inventário - GRID
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                fillRectangle(x + tamanho * j, y + tamanho * i, tamanho, tamanho, corDosContainers);
                drawRectangle(x + tamanho * j, y + tamanho * i, tamanho, tamanho, BLACK);
            }
        }

        //Inventário - Selecionar item
        drawRectangle(x + tamanho * (posInventario % 4), y + tamanho * (posInventario / 4), tamanho, tamanho, PINK);
        drawRectangle(x - 1 + tamanho * (posInventario % 4), y - 1 + tamanho * (posInventario / 4), tamanho + 2, tamanho + 2, PINK);
        fillRectangle(x + 230, y + 270, 25, 20, corDosContainers);
        drawRectangle(x + 230, y + 270, 25, 20, BLACK);
        drawText(Integer.toString(posInventario), x + 234, y + 275, 15, PINK);

        //Inventário - Desenhar os itens
        for (int i = 0; i < lista.size(); i++) {

            int valor = lista.get(i);
            int xC = x + 5 + (tamanho) * (i % 4) + (tamanho - 10) / 2;
            int yC = y + 5 + (tamanho) * (i / 4) + (tamanho - 10) / 2;
            int raio = (tamanho - 10) / 2;

            switch (valor) {
                case 1 -> {
                    fillCircle(xC, yC, raio, GREEN);
                    drawCircle(xC, yC, raio, BLACK);
                }
                case 2 -> {
                    fillCircle(xC, yC, raio, BLUE);
                    drawCircle(xC, yC, raio, BLACK);
                }
                case 3 -> {
                    fillCircle(xC, yC, raio, PURPLE);
                    drawCircle(xC, yC, raio, BLACK);
                }
                case 4 -> {
                    fillCircle(xC, yC, raio, YELLOW);
                    drawCircle(xC, yC, raio, BLACK);
                }
                default -> {
                }
            }

        }

        //Baú
        fillRectangle(x + 290, y + 20, 200, 200, corJanelaBau);
        drawRectangle(x + 290, y + 20, 200, 200, BLACK);
        drawRectangle(x + 300, y + 30, 180, 140, BLACK);

        animacaoBau();

        //Desenhar o Visual da Lista
        drawText("Exibição Horizontal da Lista", x - 270, y + 20, 15, BLACK);
        fillRectangle(x - 265, y + 40, 240, 35, corDosContainers);
        drawRectangle(x - 265, y + 40, 240, 35, BLACK);

        desenharLista();

        //Desenhar os Títulos dos Botões
        drawText("EMPILHAR", x + 356, y + 230, 15, BLACK);
        drawText("DESEMPILHAR", x + 80, y + 310, 15, BLACK);

        //Componentes
        if (tela.equals(tela.LISTA) && !componentesLista.isEmpty()) {
            for (GuiComponent c : componentesLista) {
                c.draw();
            }
        }

    }

    public void atualizarSimulacaoLista(double delta) {

        //Componentes
        if (tela.equals(tela.LISTA) && !componentesLista.isEmpty()) {
            for (GuiComponent c : componentesLista) {
                c.update(delta);
            }
        }

        //Animação do Baú
        bauAnim.update(delta);

        //Ação - Botões
        if (btnDireita.isMousePressed()) {

            if (posInventario < 15) {
                posInventario++;
            } else {
                posInventario = 0;
            }

        }

        if (btnEsquerda.isMousePressed()) {

            if (posInventario > 0) {
                posInventario--;
            } else {
                posInventario = 15;
            }

        }

        if (btnRemover.isMousePressed() && !lista.isEmpty()) {
            lista.set(posInventario, 0);
            
            if (checkRmvDinamico.isSelected()){
                for (int i = 0; i < 16; i++) {
                    if (lista.get(i) != 0) {
                        posInventario = i;
                        break;
                    }
                }   
            }

        }
        
        if (checkAnimBau.isMousePressed()){
            animBau = false;
        }

        if (btnLinkBau.isMousePressed()) {

            try {
                URI link = new URI("https://www.patreon.com/posts/23883775");
                Desktop.getDesktop().browse(link);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //Fazer só poder abrir um baú quando a animação acabar
        btnAbrirBau.setEnabled(!cronometrar);

        if (btnAbrirBau.isMousePressed()) {
            
            if (checkAnimBau.isSelected()){
                
                for (int valor : lista) {
                    if (valor == 0) {
                        cronometro = 0;
                        animBau = true;
                        bauAnim.reset();
                        break;
                    }
                }
                
            }
            

            Random random = new Random();
            int probabilidade = random.nextInt(100);

            bruh = loadSound("resources/sfx/bruh.ogg");
            woah = loadSound("resources/sfx/woah.ogg");
            bttb = loadSound("resources/sfx/bad-to-the-bone.ogg");

            if (probabilidade < 50) {
                cronometrar = checkAnimBau.isSelected();
                raridade = 1;
            } else if (probabilidade < 80) {
                cronometrar = checkAnimBau.isSelected();
                raridade = 2;
            } else if (probabilidade < 95) {
                cronometrar = checkAnimBau.isSelected();
                raridade = 3;
            } else {
                cronometrar = checkAnimBau.isSelected();
                raridade = 4;
            }
            
            if (!checkAnimBau.isSelected()){
                inserirItem(raridade);
            } else{
                
            }

        }

    }

    public void criarAnimacoes() {

        List<ImageAnimationFrame> imageFrames = new ArrayList<>();
        for (int i = 0; i < 32; i++) {
            String c = String.valueOf(i);
            imageFrames.add(new ImageAnimationFrame(loadImage("resources/images/bauFrames/bauAnim-" + c + ".png")));
        }

        bauAnim = new FrameByFrameAnimation<>(0.05, imageFrames);

    }

    public void animacaoBau() {

        Image bauFechado = loadImage("resources/images/bauFechado.png");
        Image bauAberto = loadImage("resources/images/bauAberto.png");

        if (!animBau) {

            drawImage(bauFechado, 586, 131);

        } else {

            if (cronometro <= 2) {
                drawImage(bauAnim.getCurrentFrame().baseImage, 586, 131);
            } else {
                drawImage(bauAberto, 586, 131);
            }

        }

    }

    public void desenharLista() {

        int largura = 10;
        int altura = 15;

        Paint cor;

        int i = 0;

        for (Integer valor : lista) {
            int x = 55 + (largura + 1) * i;
            int y = 150;

            //Mudar cores com base na raridade
            switch (valor) {
                case 1 -> {
                    cor = DARKGREEN;
                    fillRectangle(x, y, largura, altura, cor);
                }
                case 2 -> {
                    cor = DARKBLUE;
                    fillRectangle(x, y, largura, altura, cor);
                }
                case 3 -> {
                    cor = DARKPURPLE;
                    fillRectangle(x, y, largura, altura, cor);
                }
                case 4 -> {
                    cor = GOLD;
                    fillRectangle(x, y, largura, altura, cor);
                }
                default -> {
                    if (checkVisuSequencial.isSelected()){
                        i--;
                    }
                }
            }

            i++;
        }

    }

    private void inserirItem(int numero) {
        
        if(!checkInsDirect.isSelected()){
            
            for (int i = 0; i < lista.size(); i++) {

                if (!checkInsDirect.isSelected()) {
                    if (lista.get(i) == 0) {
                        lista.set(i, numero);
                        break;
                    }

                } 
            }
        }else{
            lista.set(posInventario, numero);
        }
        
    }

    /*
    *     EXTRAS
     */
//----------< Instanciar Engine e Iniciá-la >----------//
    public static void main(String[] args) {
        new Main();
    }

}
