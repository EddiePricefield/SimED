package template;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.imgui.GuiButton;
import br.com.davidbuzatto.jsge.imgui.GuiComponent;
import br.com.davidbuzatto.jsge.imgui.GuiLabelButton;
import java.util.ArrayList;

import java.util.Stack;
import java.util.List;
import java.util.Deque;
import java.util.Queue;

/**
 * Feito no JSGE
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
    
    //Componentes - Globais
    private GuiButton btnVoltar;
    
    //Componentes - Tela Inicial
    private GuiButton btnSimPilha;
    private GuiButton btnSimFila;
    private GuiButton btnSimDeque;
    private GuiButton btnSimLista;
    
    private GuiLabelButton btnLink;
    
    //Componentes - Tela Simulação Pilhas
    
    //Estruturas de Dados
    private Stack<Integer> pilha;
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
        tela = "Inicial";
        useAsDependencyForIMGUI();
        
        //Componentes Globais
        componentesGlobais("Iniciar");
        
        //Inicializar Telas
        switch(tela){
            case "Inicial" -> criarTelaInicial();
            case "Pilha" -> criarSimulacaoPilha();
            case "Fila" -> criarSimulacaoFila();
            case "Deque" -> criarSimulacaoDeque();
            case "Lista" -> criarSimulacaoLista();
        }    
        
        
    }
    
//----------< Atualizar >----------//

    @Override
    public void update( double delta ) {
        
        //Variáveis Globais
        cronometro += delta;
        
        //Atualização - Globais
        componentesGlobais("Atualizar", delta);
        
        //Atualização -  Telas
        switch (tela) {
            case "Inicial" -> atualizarTelaInicial(delta);
            case "Pilha" -> atualizarSimulacaoPilha();
            case "Fila" -> atualizarSimulacaoFila();
            case "Deque" -> atualizarSimulacaoDeque();
            case "Lista" -> atualizarSimulacaoLista();
        }
        
    }
    
//----------< Desenhar >----------//
    
    @Override
    public void draw() {
        
        //Desenhar - Globais
        componentesGlobais("Desenhar");
        
        //Desenhar - Telas
        switch(tela){
            case "Inicial" -> desenharTelaInicial();
            case "Pilha" -> desenharSimulacaoPilha();
            case "Fila" -> desenharSimulacaoFila();
            case "Deque" -> desenharSimulacaoDeque();
            case "Lista" -> desenharSimulacaoLista();
        }
        
    }
    
//----------< Complementares >----------//
    
    /*
    *     COMPONENTES GLOBAIS
     */
    
    public void componentesGlobais(String opcao){
        
        switch(opcao){
            case "Iniciar":
                
                componentesGlobais = new ArrayList<>();
                
                btnVoltar = new GuiButton(10, 445, 20, 20, "↩");
                
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
    
    public void componentesGlobais(String opcao, double delta){
        
        if (opcao.equals("Atualizar") && !tela.equals("Inicial") && !componentesGlobais.isEmpty()) {
            for (GuiComponent c : componentesGlobais) {
                c.update(delta);
            }
        }
        
        if (btnVoltar.isMousePressed()) {
            tela = "Inicial";
        }
        
    }
    
    /*
    *     TELA INICIAL
    */
    
    public void criarTelaInicial(){
        
        //Inicializar a Lista de Componentes
        componentesTelaInicial = new ArrayList<>();
        
        //Valores iniciais para organizar os botões
        int x = 320;
        int y = 300;
        
        //Criação dos Botões
        btnSimPilha = new GuiButton(x, y, 150, 30, "Simulação de Pilha");
        btnSimFila = new GuiButton(x, y + 40, 150, 30, "Simulação de Fila");
        btnSimDeque = new GuiButton(x, y + 80, 150, 30, "Simulação de Deque");
        btnSimLista = new GuiButton(x, y + 120, 150, 30, "Simulação de Lista");
        btnLink = new GuiLabelButton(10, 445, 50, 20, "@EddiePricefield");
        
        //Adicionar componentes à Lista de Componentes
        componentesTelaInicial.add(btnSimPilha);
        componentesTelaInicial.add(btnSimFila);
        componentesTelaInicial.add(btnSimDeque);
        componentesTelaInicial.add(btnSimLista);
        componentesTelaInicial.add(btnLink);
        
    }
    
    public void desenharTelaInicial(){
        
        if(tela.equals("Inicial") && !componentesTelaInicial.isEmpty()){
            for (GuiComponent c : componentesTelaInicial) {
                c.draw();
            }
        }
        
    }
    
    public void atualizarTelaInicial(double delta){
        
        //Atualizando os Componentes
        if(tela.equals("Inicial") && !componentesTelaInicial.isEmpty()){
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
    
    public void criarSimulacaoPilha(){
    }
    
    public void desenharSimulacaoPilha() {
    }
    
    public void atualizarSimulacaoPilha() {
    }
    
    /*
    *     TELA DE SIMULAÇÃO DE FILA
     */
    
    public void criarSimulacaoFila() {
    }

    public void desenharSimulacaoFila() {
    }

    public void atualizarSimulacaoFila() {
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
    
//----------< Instanciar Engine e Iniciá-la >----------//
    
    public static void main( String[] args ) {
        new Main();
    }
    
}
