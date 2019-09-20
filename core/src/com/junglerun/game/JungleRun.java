package com.junglerun.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.Random;
import java.util.logging.FileHandler;

public class JungleRun extends ApplicationAdapter {

    //Texturas e elementos para o jogo
    private SpriteBatch batch;
    private Texture fundo;
    private Texture [] ninja;
    private Texture [] coinGold;

    //declaração das variáveis para trabalhar com formas para colisões
	private ShapeRenderer renderer; //permite desenhar as formas
	private Circle circuloMoeda; //circulo da moeda
	private Rectangle rectanglePersonagem; //rectangulo para o personagem


    //dimensões de largura e altura padrão
    private int larguraPadraoX;
    private int alturaPadraoY;
	private int larguraFundo=0;
	private int alturaFundo=0;
    //variavel para trabalhar com o indice do vector para alterar sprites
	private float indiceSprite;

	//
	private float deltaTime;
	//propriedades de salto
	private boolean salto;
	private int alturaMaximaDeSalto;
	private int alturaMinimaDeQueda;
	private int velocidadeSalto;
    private int posNinjaY;
    private int valocidadeDeSalto;

    //propriedades para trabalbalhar com a altura da moeda gold
	private int altura_minima_moeda;
	private float indiceMoeda;
	private int movimentoMoeda;

	//Variavel para trabalhar com pontuação
	private BitmapFont fonte;
	private int pontuacao;

	//Variavel para trabalhar com estado do jogo
	private int estadoJogo;
	//propriedades de som
    private Music somAmbiente;
    private Sound somSalto;
    private Sound somMoeda;

	@Override
	public void create () {

	    batch = new SpriteBatch();
	    fundo = new Texture("fundo1.png");
	    salto=false;
	    //Instância do vector do sprite
		ninja = new Texture[10];
		coinGold = new Texture[6];

		//instanciar as formas
		renderer = new ShapeRenderer();
		circuloMoeda = new Circle();
		rectanglePersonagem = new Rectangle();

		//Instanciar o BitmapFont para trabalhar com fontes

        //Instanciar o BitmapFont para trabalhar com fontes

		fonte = new BitmapFont();
		fonte.setColor(Color.GOLD);
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
	    larguraPadraoX = Gdx.graphics.getWidth();
	    alturaPadraoY = Gdx.graphics.getHeight();
	    //Obter o tamanho do fundo
		alturaFundo = fundo.getHeight();
		larguraFundo= fundo.getWidth();
        //setting a posicao do ninja
        posNinjaY=(int)(alturaFundo*0.30);
	    //Inicializar o indice do Sprite
		indiceSprite = 0;
		//propriedades de salto
		alturaMinimaDeQueda=posNinjaY;
		alturaMaximaDeSalto=posNinjaY+140;
		velocidadeSalto=14;

		//inicializar a altura_minima e altura_maxima da moeda
		altura_minima_moeda = 200;
		//inicializar o indice da moeda
		indiceMoeda = 0;
		movimentoMoeda = larguraPadraoX;

		//Inicializar a variável de pontuação e o estado do jogo
		pontuacao = 0;
		estadoJogo = 0;
		//Logica de som
		//logica de som ambiente
		somAmbiente = Gdx.audio.newMusic(Gdx.files.internal("Ambiente.wav"));
		somAmbiente.play();
		somAmbiente.setVolume(0.3f);
		somAmbiente.setLooping(true);
		//Logica de som de salto
		somSalto = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
		somMoeda = Gdx.audio.newSound(Gdx.files.internal("moeda.wav"));

	}

	@Override
	public void render () {

		//Variável aleatória para Escolher a posição de altura da moeda
		Random random = new Random();
		int esc = random.nextInt(alturaPadraoY-200);

		//Estado do Jogo = 0, Inicio do Jogo
		if (estadoJogo == 0){
			indiceSprite += Gdx.graphics.getDeltaTime() * 10;
			indiceMoeda += Gdx.graphics.getDeltaTime() * 10;

			//decrementar a posição da moeda
			movimentoMoeda -= 2;

			//Se a moeda sair da tela sem ser capturada então
			if (movimentoMoeda == -80){
				movimentoMoeda = larguraPadraoX;
				//configurar uma altura padrão para a moeda
				if (esc>altura_minima_moeda){
					altura_minima_moeda = esc;
				}
			}

			//controle de indice da moeda
			if (indiceMoeda>5){
				indiceMoeda = 0;
			}

			//se o indice do Sprite for maior que 9 zerar novamente o indice do Sprite
			if (indiceSprite>9){
				indiceSprite = 0;
			}
			if(Gdx.input.justTouched()){
				//verificar se o ninja está no chão
			    if(posNinjaY<=alturaMaximaDeSalto && posNinjaY<=alturaMinimaDeQueda){
						somSalto.play(0.1f);
						salto=true;
					}
				}
			//verficar se o jogador está em salto
			if(salto){
				//se esta em salto, incrementar a posicao em Y
                posNinjaY+=velocidadeSalto+deltaTime;
                //verificar se o ninja ja atingiu a altura máxima, para indicar o fim do salto
                if(posNinjaY>=alturaMaximaDeSalto){
                    salto=false;
                }
            }
			//controle de cada, verificando se o salto ja terminou
			if(!salto){
				if(posNinjaY>=alturaMaximaDeSalto || posNinjaY>alturaMinimaDeQueda){
                    indiceSprite=7;
                    posNinjaY-=velocidadeSalto;
                }
			}
		}

        batch.begin();

        batch.draw(fundo,0,0,larguraPadraoX,alturaPadraoY);
		//Inicializar a posição da fonte na tela
		fonte.draw(batch,"Pontuação: "+pontuacao,(larguraPadraoX/2)-120,alturaPadraoY - 30);
        batch.draw(ninja[(int)indiceSprite],50,posNinjaY);
        //adicionar moedas
        batch.draw(coinGold[(int)indiceMoeda],movimentoMoeda,altura_minima_moeda,80,80);

        batch.end();

        //iniciar configurações para desenhar as formas
		circuloMoeda.set(movimentoMoeda+33,altura_minima_moeda + 45,39);
		rectanglePersonagem.set(70,posNinjaY,ninja[0].getWidth()-30,ninja[0].getHeight()-40);


		/* *****************************************************
		* Capturando moeda
		* */
		if (Intersector.overlaps(circuloMoeda,rectanglePersonagem)){
			movimentoMoeda = larguraPadraoX + 130;

			//Escolher a posição aletória da moeda quando a mesma for capturada
			if (esc>altura_minima_moeda){
				altura_minima_moeda = esc;
			}
			pontuacao++;
			somMoeda.play(0.5f);
		}
	}
	
	@Override
	public void dispose () {

        batch.dispose();
	}
}
