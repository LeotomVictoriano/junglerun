package com.junglerun.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class JungleRun extends ApplicationAdapter {

    //Texturas e elementos para o jogo
    private SpriteBatch batch;
    private Texture fundo;
    private Texture [] ninja;
    private Texture [] coinGold;

    //Texturas do ambiente para o fundo do jogo
	private Texture mont1;
	private Texture mont2;
	private Texture mont3;
	private Texture logo;

	//Textura para trabalhar com obstaculos
	private Texture obst_tronco;
	private Texture obst_madeira;

	//Texturas para menu e GameOver
	private Texture menu;
	private Texture game_over;

    //declaração das variáveis para trabalhar com formas para colisões
	private ShapeRenderer renderer; //permite desenhar as formas
	private Circle circuloMoeda; //circulo da moeda
	private Rectangle rectanglePersonagem; //rectangulo para o personagem
	private Rectangle rect_tronco;
	private Rectangle rect_madeira;


    //dimensões de largura e altura padrão
    private float larguraPadraoX;
    private float alturaPadraoY;
	private int larguraFundo=0;
	private int alturaFundo=0;

    //variavel para trabalhar com o indice do vector para alterar sprites
	private float indiceSprite;

	//delta time, variável para pegar o tempo de execução do jogo
	private float deltaTime;

	//variaveis da imagem da vida
	private Texture[] vidaimg;
	private int quantidade_vida = 3;
	private int espaco_entre_vidas;

	//propriedades de salto
	 private boolean salto;
	private float velocidadeJogo;
    private int posNinjaY;
    private int velocidade_queda;

    //propriedades para trabalbalhar com a altura da moeda gold
	private int altura_minima_moeda;
	private float indiceMoeda;
	private float movimentoMoeda;

	//Variavel para trabalhar com pontuação
	private BitmapFont fonte;
	private int pontuacao;

	//Variavel para trabalhar com estado do jogo
	private int estadoJogo;

	//propriedades de som
    private Music somAmbiente;
    private Sound somSalto;
    private Sound somGameOver;
	private Sound somMoeda;

    //Variável para trabalhar com o movimento do ambiente do fundo;
	private float movimento_ambiente1;
	private float movimento_ambiente2;
	private float movimento_ambiente3;

	//Variáveis para controlo do movimento horizontal do obstaculo
	private float movimento_tronco;
	private float movimento_madeira;

	//variável de controle de toque
	private int controle_toque;

	/**********************************************************
	 * CONFIGURAÇÕES DO VIEWPORT
	 */
	//declaração de variáveis para a configuração da câmera
	private OrthographicCamera camera;
	private Viewport viewport;

	//constante de largura e altura
	private final float WIDTH = 1280;
	private final float HEIGHT = 720;

	@Override
	public void create () {
	    batch = new SpriteBatch();
	    fundo = new Texture("fundo1.png");
	    salto=false;

	    //Instância do vector do sprite
		ninja = new Texture[10];
		coinGold = new Texture[6];
		vidaimg=new Texture[quantidade_vida];
		//inicializando as novas texturas para o ambiente do jogo
		mont1 = new Texture("Mont01.png");
		mont2 = new Texture("Mont02.png");
		mont3 = new Texture("Mont03.png");
		logo = new Texture("logo.png");

		//Inicializar as Texturas de Obstaculos
		obst_tronco =  new Texture("obstaculo_tronco.png");
		obst_madeira = new Texture("obstaculo_madeira_03.png");
		menu = new Texture("Menu.jpg");
		game_over = new Texture("fundo_game_over.png");

		//inicializar a textura da vida e componentes da textura

        for (int c=0;c<quantidade_vida;c++) {
            vidaimg[c] = new Texture("vida.png");
            }
        //definir o espaçamento entre os corações
		espaco_entre_vidas=vidaimg[0].getWidth();

		//instanciar as formas
		renderer = new ShapeRenderer();
		circuloMoeda = new Circle();
		rectanglePersonagem = new Rectangle();
		rect_tronco = new Rectangle();
		rect_madeira = new Rectangle();

        //Instanciar o BitmapFont para trabalhar com fontes
		fonte = new BitmapFont();
		fonte.setColor(Color.GREEN);
		fonte.getData().setScale(3);

	    //Preenchendo o vector com sprite personagem ao vector
	    for (int i=0;i<10;i++){
	    	ninja[i] = new Texture("ninja"+(i+1)+".png");
		}

	    //preencher o vector de moedas
		for(int i=0;i<6;i++){
			coinGold[i] = new Texture("coinGold"+(i+1)+".png");
		}
		//inicializar altura e largura padrão
	    larguraPadraoX = WIDTH;
	    alturaPadraoY = HEIGHT;

	    //Obter o tamanho do fundo
		alturaFundo = fundo.getHeight();
		larguraFundo= fundo.getWidth();

        //setting a posicao do ninja
		posNinjaY=(int)(alturaFundo*0.30);

	    //Inicializar o indice do Sprite
		indiceSprite = 0;

		//inicializar a altura_minima e altura_maxima da moeda
		altura_minima_moeda = 430;

		//inicializar o indice da moeda
		indiceMoeda = 0;

		//Inicializar o movimento da moeda
		movimentoMoeda = larguraPadraoX;

		//Inicializar a variável de pontuação e o estado do jogo
		estadoJogo = 0;

		//Logica de som
		//logica de som ambiente
		somAmbiente = Gdx.audio.newMusic(Gdx.files.internal("Ambiente.wav"));

		//Logica de som de salto
		somSalto = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));

		//Lógica de som da moeda
		somMoeda = Gdx.audio.newSound(Gdx.files.internal("moeda.wav"));

		//Lógica de som de GameOver
		somGameOver = Gdx.audio.newSound(Gdx.files.internal("GameOverSound.wav"));

		//Inicializar a velocidade da queda
		velocidade_queda = 0;

		/**********************************************************
		 * INICIAR AS CONFIGURAÇÕES PARA TRABALHAR COM CÂMERA
		 */
		//instânciar configurações da câmera
		camera = new OrthographicCamera();
		camera.position.set(WIDTH/2,HEIGHT/2,0);
		viewport = new StretchViewport(WIDTH,HEIGHT,camera);
	}

	@Override
	public void render () {

		//Atualizar a Câmera
		camera.update();

		//Limpar frames anteriores
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		/**************************************************
		 *
		 * ESTADO 0 => EXIBIÇÃO DA TELA INICIAL DO JOGO
		 */
		if (estadoJogo == 0){
            //Inicializar a posição horizontal dos obstaculos
            movimento_tronco = larguraPadraoX+150;
            movimento_madeira = larguraPadraoX+1000;
            quantidade_vida = 3;
            //inicializar o movimento do ambiente do fundo
            movimento_ambiente1 = 300;
            movimento_ambiente2 = 400;
            movimento_ambiente3 = 600;
            controle_toque = 0;

            //Inicializar a pontuação
			pontuacao = 0;

			//Inicializar a variável da velocidade do jogo
			velocidadeJogo = 8;

			//Configurar as projeções da Câmera
			batch.setProjectionMatrix(camera.combined);

			batch.begin();
			batch.draw(menu,0,0,larguraPadraoX,alturaPadraoY);
			batch.end();
			if (Gdx.input.justTouched()){
				somAmbiente.play();
				somAmbiente.setVolume(0.2f);
				somAmbiente.setLooping(true);
				estadoJogo = 1;
			}
		}
		/***************************************************************
		 *
		 * ESTADO 1=> JOGO EM EXECUÇÃO
		 */
		//estado do jogo 0 => Inicio do Jogo
		if (estadoJogo == 1) {

			controle_toque = 0;


			//Incrementar a velocidade da queda
			velocidade_queda++;

			//atribuindo o valor aleatorio no deltatime
			deltaTime = Gdx.graphics.getDeltaTime();

			//incrementando a velocidade de jogo a cada ciclo de render
			velocidadeJogo += 0.001;

			//Atribuindo valores aos índices do personagem e da moeda
			indiceSprite += deltaTime * velocidadeJogo;
			indiceMoeda += deltaTime * velocidadeJogo;

			//Decrementando a posição horizontal do tronco e da madeira
			movimento_tronco -= velocidadeJogo;
			movimento_madeira -= velocidadeJogo;

			/*************************************
			 * Controlar movimento dos obstaculos
			 * */
			if (movimento_tronco < -200){
				if(movimento_madeira<-100){
					movimento_tronco = larguraPadraoX + 150;
				}else{
					movimento_tronco = -200;
				}
			}
			if (movimento_madeira < -200){
				if(movimento_tronco < -100){
					movimento_madeira = larguraPadraoX + 500;
				}else{
					movimento_madeira = -500;
				}
			}

			/*****************************************************
			 * Decrementando os movimentos das montanhas de fundo
			 * */
			movimento_ambiente1 -= velocidadeJogo;
			movimento_ambiente2 -= velocidadeJogo;
			movimento_ambiente3 -= velocidadeJogo;

			/****************************************************
			 * Condições que tratam o movimento dos troncos quando já não estão a ser exibidos na tela
			 * */
			if (movimento_ambiente1 < -150) {
				movimento_ambiente1 = larguraPadraoX + 100;
			}
			if (movimento_ambiente2 < -150) {
				movimento_ambiente2 = larguraPadraoX + 150;
			}
			if (movimento_ambiente3 < -200) {
				movimento_ambiente3 = larguraPadraoX + 200;
			}

			//decrementar a posição da moeda
			movimentoMoeda -= velocidadeJogo;

			//Se a moeda sair da tela sem ser capturada então
			if (movimentoMoeda < -100) {
				movimentoMoeda = larguraPadraoX+100;
			}

			//controle de indice da moeda
			if (indiceMoeda > 5) {
				indiceMoeda = 0;
			}
			//se o indice do Sprite for maior que 9 zerar novamente o indice do Sprite
			if (indiceSprite > 9) {
				indiceSprite = 0;
			}

			/****************************************************
			 * LÓGICA QUANDO A TELA É CLICADA PELO USUÁRIO
			 */
			if (!salto){
			if (Gdx.input.justTouched()){
				velocidade_queda = -20;
				somSalto.play(0.3f);
			  }
			}
			if (posNinjaY > 192 || velocidade_queda < 0){
				posNinjaY -= velocidade_queda;
			}
			if (posNinjaY>192){
				salto = true;
			}else {
				salto = false;
			}
			/************************************************
			 * INICIO DO BATCH PARA DESENHAR AMBIENTES NA TELA
			 */

			batch.begin();

			batch.draw(fundo, 0, 0, larguraPadraoX, alturaPadraoY);

			//desenhar o ambiente para movimento do fundo
			batch.draw(mont1, movimento_ambiente1, 204);
			batch.draw(mont2, movimento_ambiente2, 204);
			batch.draw(mont3, movimento_ambiente3, 204);


			/************************************************
			 * Desenhar os obstaculos
			 * */
			batch.draw(obst_tronco, movimento_tronco, 204);
			batch.draw(obst_madeira, movimento_madeira, 204);

			//Inicializar a posição da fonte na tela
			fonte.draw(batch, "Pontuação: " + pontuacao, (larguraPadraoX / 2) - 120, alturaPadraoY - 30);
			batch.draw(ninja[(int) indiceSprite], 150, posNinjaY);
			//adicionar moedas
			batch.draw(coinGold[(int) indiceMoeda], movimentoMoeda, altura_minima_moeda, 80, 80);

			/**
			 * Desenhar a vida do personagem
			 * */
			for (int i = 0,space=espaco_entre_vidas; i <quantidade_vida ; i++, space+=espaco_entre_vidas) {
				batch.draw(vidaimg[i],space,alturaFundo);
			}

			batch.end();

			//iniciar configurações para desenhar as formas
			circuloMoeda.set(movimentoMoeda + 33, altura_minima_moeda + 45, 39);
			rectanglePersonagem.set(160, posNinjaY, ninja[0].getWidth()-20, ninja[0].getHeight()-10);


			//Instanciar configurações das formas para os obstaculos
			rect_tronco.set(movimento_tronco,204,obst_tronco.getWidth(),obst_tronco.getHeight());
			rect_madeira.set(movimento_madeira,204,obst_madeira.getWidth(),obst_madeira.getHeight()-20);

			/* *****************************************************
			 * Capturando moeda
			 * */
			if (Intersector.overlaps(circuloMoeda, rectanglePersonagem)) {
				movimentoMoeda = larguraPadraoX + 130;

				//Aumentar a pontuação quando a moeda for capturada e tocar o som de captura da moeda
				pontuacao++;
				somMoeda.play(0.5f);
			}
			/**************************************************
			 *Detectando colisão com os objetos e obstaculos
			 */
			if (Intersector.overlaps(rectanglePersonagem,rect_tronco) || Intersector.overlaps(rectanglePersonagem,rect_madeira)){
				if (quantidade_vida>1){
					quantidade_vida--;
					estadoJogo = 3;
				}else {
					estadoJogo = 2;
				}
					somGameOver.play(0.5f);
					somAmbiente.stop();
			}

		}
		/**********************************************************
		 *
		 * ESTADO 2 => TELA DE GAMEOVER
		 */
		if (estadoJogo == 2){
		    batch.begin();
		    batch.draw(game_over,0,0,larguraPadraoX,alturaPadraoY);
			batch.draw(logo,(larguraPadraoX/2)-400,(alturaPadraoY/2)-100);
			fonte.draw(batch, "Ops ! Você Perdeu\nToque para voltar ao menu Inicial\n" +
					"Pontuação Total: "+pontuacao+" Ponto(s)", (larguraPadraoX / 2) - 300, (alturaPadraoY/2)-150);
		    batch.end();

		    if (Gdx.input.justTouched()){
				controle_toque++;
		    	if (controle_toque == 2) {
					estadoJogo = 0;
				}
            }
        }

		/***********************************************
		 * ESTADO 3 => Quando o usuário ainda possui vidas suficientes para jogar
		 */
		if (estadoJogo==3){
			movimento_tronco = larguraPadraoX+150;
			movimento_madeira = larguraPadraoX+1000;
			batch.begin();
			batch.draw(game_over,0,0,larguraPadraoX,alturaPadraoY);
			batch.draw(logo,(larguraPadraoX/2)-400,(alturaPadraoY/2)-100);
			fonte.draw(batch, "Você Ainda Possui "+quantidade_vida+" Vida(s)\n" +
					"Toque Na tela para continuar a jogar", (larguraPadraoX / 2) - 300, (alturaPadraoY/2)-150);
			batch.end();
			if (Gdx.input.justTouched()){
				controle_toque++;
				if (controle_toque == 2) {
					estadoJogo = 1;
					somAmbiente.play();
					somAmbiente.setLooping(true);
				}
			}
		}
	}
	
	@Override
	public void dispose () {
    //    somAmbiente.dispose();
        somSalto.dispose();
        somMoeda.dispose();
        somGameOver.dispose();
        renderer.dispose();
        batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width,height);
	}
}
