package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.imgui.GuiButton;
import br.com.davidbuzatto.jsge.imgui.GuiComponent;
import br.com.davidbuzatto.jsge.imgui.GuiLabelButton;
import java.awt.Paint;
import java.util.ArrayDeque;
import java.util.ArrayList;

import java.util.Stack;
import java.util.List;
import java.util.Deque;
import java.util.Queue;

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
    private String tela;

    //Listas de Componentes
    private List<GuiComponent> componentesGlobais;
    private List<GuiComponent> componentesTelaInicial;
    private List<GuiComponent> componentesPilha;
    private List<GuiComponent> componentesFila;
    private List<GuiComponent> componentesDeque;
    private List<GuiComponent> componentesLista;

    //Componentes - Globais
    private GuiButton btnVoltar;

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

    //Estruturas de Dados
    private Stack<String> pilhaDesfazer;
    private Stack<String> pilhaRefazer;
    private Queue<Integer> fila;
    private Deque<Integer> deque;
    private List<Integer> lista;

    //Construtor
    public Main() {

        super(800, 480, "Simulador de Estruturas de Dados", 60, true, false, false, false, false, false);

    }

//----------< Criar >----------//
    @Override
    public void create() {

        //Variáveis Globais
        tela = "Fila";
        useAsDependencyForIMGUI();

        //Componentes Globais
        componentesGlobais("Iniciar");

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
        cronometro += delta;

        //Atualização - Globais
        componentesGlobais("Atualizar", delta);

        //Atualização -  Telas
        switch (tela) {
            case "Inicial" ->
                atualizarTelaInicial(delta);
            case "Pilha" ->
                atualizarSimulacaoPilha(delta);
            case "Fila" ->
                atualizarSimulacaoFila(delta);
            case "Deque" ->
                atualizarSimulacaoDeque();
            case "Lista" ->
                atualizarSimulacaoLista();
        }

    }

//----------< Desenhar >----------//
    @Override
    public void draw() {

        //Desenhar - Globais
        componentesGlobais("Desenhar");

        //Desenhar - Telas
        switch (tela) {
            case "Inicial" ->
                desenharTelaInicial();
            case "Pilha" ->
                desenharSimulacaoPilha();
            case "Fila" ->
                desenharSimulacaoFila();
            case "Deque" ->
                desenharSimulacaoDeque();
            case "Lista" ->
                desenharSimulacaoLista();
        }

    }

//----------< Complementares >----------//
    /*
    *     COMPONENTES GLOBAIS
     */
    public void componentesGlobais(String opcao) {

        switch (opcao) {
            case "Iniciar":

                componentesGlobais = new ArrayList<>();

                btnVoltar = new GuiButton(10, 450, 20, 20, "↩");

                componentesGlobais.add(btnVoltar);

                break;

            case "Desenhar":

                if (!tela.equals("Inicial") && !componentesGlobais.isEmpty()) {
                    for (GuiComponent c : componentesGlobais) {
                        c.draw();
                    }
                }

                break;

        }

    }

    public void componentesGlobais(String opcao, double delta) {

        if (opcao.equals("Atualizar") && !tela.equals("Inicial") && !componentesGlobais.isEmpty()) {
            for (GuiComponent c : componentesGlobais) {
                c.update(delta);
            }
        }

        if (btnVoltar.isMousePressed()) {
            tela = "Inicial";
        }

    }
    
    public void drawOutlinedText( String text , int posX, int posY, int fontSize, Paint color , int outlineSize, Paint outlineColor){
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
        btnLink = new GuiLabelButton(10, 455, 120, 20, "@EddiePricefield");

        //Adicionar componentes à Lista de Componentes
        componentesTelaInicial.add(btnSimPilha);
        componentesTelaInicial.add(btnSimFila);
        componentesTelaInicial.add(btnSimDeque);
        componentesTelaInicial.add(btnSimLista);
        componentesTelaInicial.add(btnLink);

    }

    public void desenharTelaInicial() {

        //Título
        drawText("SimED", 170, 80, 150, ORANGE);

        //Componentes
        if (tela.equals("Inicial") && !componentesTelaInicial.isEmpty()) {
            for (GuiComponent c : componentesTelaInicial) {
                c.draw();
            }
        }

    }

    public void atualizarTelaInicial(double delta) {

        //Atualizando os Componentes
        if (tela.equals("Inicial") && !componentesTelaInicial.isEmpty()) {
            for (GuiComponent c : componentesTelaInicial) {
                c.update(delta);
            }
        }

        //Ações - Clique do Mouse
        if (btnSimPilha.isMousePressed()) {
            tela = "Pilha";
        }

        if (btnSimFila.isMousePressed()) {
            tela = "Fila";
        }

        if (btnSimDeque.isMousePressed()) {
            tela = "Deque";
        }

        if (btnSimLista.isMousePressed()) {
            tela = "Lista";
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
        drawText("Simulação Pilha", 176, 30, 50, BLACK);

        //Desenhar o GRID e as Formas
        int x = 150;
        int y = 100;
        int tamanho = 50;
        
        drawText("EMPILHAR", 43, 130, 15, BLACK);
        drawText("DESEMPILHAR", 230, 400, 15, BLACK);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                drawRectangle(x + tamanho * j, y + tamanho * i, tamanho, tamanho, BLACK);
            }
        }

        desenharForma();

        //Desenhar o Visual dos Stacks
        drawText("Exibição Horizontal da Pilha", 475, 100, 15, BLACK);
        drawRectangle(450, 115, 295, 35, BLACK);

//        drawText("Pilha: Refazer", 535, 175, 15, RED);
//        drawRectangle(450, 190, 295, 35, BLACK);

        desenharPilhas();

        //Desenhar Painel de Informações
        
        
        
        //Componentes
        if (tela.equals("Pilha") && !componentesPilha.isEmpty()) {
            for (GuiComponent c : componentesPilha) {
                c.draw();
            }
        }

    }

    public void atualizarSimulacaoPilha(double delta) {

        //Componentes
        if (tela.equals("Pilha") && !componentesPilha.isEmpty()) {
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
        drawText("Simulação Fila", 176, 30, 50, BLACK);

        //Desenhando Fila
        for (int i = 1; i < 14; i++){
            drawRectangle(53 * i, 100, 50, 50, BLACK);
        }
        
        desenharValores();
        
        //Variáveis para auxiliar
        int x = 95;
        int y = 210;
        
        //Desenhar os Títulos dos Botões
        drawText("DESEMPILHAR", x, y, 15, BLACK);
        drawText("EMPILHAR", x + 193, y, 15, BLACK);
               
        //Desenhar o Visual dos Stacks
        drawText("Exibição Horizontal da Fila", x + 370, y - 40, 15, BLACK);
        drawRectangle(x + 388, y - 20, 200, 35, BLACK);
        
        desenharFila();
        
        //Componentes
        if (tela.equals("Fila") && !componentesFila.isEmpty()) {
            for (GuiComponent c : componentesFila) {
                c.draw();
            }
        }
        
    }

    public void atualizarSimulacaoFila(double delta) {
        
        //Componentes
        if (tela.equals("Fila") && !componentesFila.isEmpty()) {
            for (GuiComponent c : componentesFila) {
                c.update(delta);
            }
        }
        
        //Ações - Botões do Mouse
        if (btnAddNumero.isMousePressed()){
            if (fila.size() < 13 && contadorValores < 100){
                fila.add(contadorValores++);
            }
        }
        
        if (btnRmvNumero.isMousePressed()){
            if (!fila.isEmpty()){
                fila.poll();
            }
        }
        
    }
    
    public void desenharValores(){
        
        int i = 0;
        
        for (int valor : fila){            

            int y = 118;
            int x = 63;
            
            if (i == 0 && fila.size() == 1){
                drawOutlinedText(String.format("%02d", valor), (53 * i + x), y, 25, PURPLE, 1, BLACK);
            } else if (i == 0){
                drawOutlinedText(String.format("%02d", valor), (53 * i + x), y, 25, RED, 1, BLACK);
            } else if (i == fila.size() - 1){
                drawOutlinedText(String.format("%02d", valor), (53 * i + x), y, 25, BLUE, 1, BLACK);
            } else{
                drawOutlinedText(String.format("%02d", valor), (53 * i + x), y, 25, PINK, 1, BLACK);
            }
            
            i++;
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
    }

    public void desenharSimulacaoDeque() {
    }

    public void atualizarSimulacaoDeque() {
    }

    /*
    *     TELA DE SIMULAÇÃO DE LISTA
     */
    public void criarSimulacaoLista() {
    }

    public void desenharSimulacaoLista() {
    }

    public void atualizarSimulacaoLista() {
    }

    /*
    *     TELA DE SIMULAÇÃO DE LISTA
     */
//----------< Instanciar Engine e Iniciá-la >----------//
    public static void main(String[] args) {
        new Main();
    }

}
