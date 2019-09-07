package com.junglerun.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JungleRun extends ApplicationAdapter {

    //Texturas e elementos para o jogo
    private SpriteBatch batch;
    private Texture fundo;
    private Texture [] ninja;


    //dimensões de largura e altura padrão
    private int larguraPadrãoX;
    private int alturaPadrãoY;

    //variavel para trabalhar com o indice do vector para alterar sprites
	private float indiceSprite;

	@Override
	public void create () {

	    batch = new SpriteBatch();
	    fundo = new Texture("fundo1.png");

	    //Instância do vector do sprite
		ninja = new Texture[10];

	    //Preenchendo o vector com sprite personagem ao vector
	    for (int i=0;i<10;i++){
	    	ninja[i] = new Texture("ninja"+(i+1)+".png");
		}

	    larguraPadrãoX = Gdx.graphics.getWidth();
	    alturaPadrãoY = Gdx.graphics.getHeight();

	    //Inicializar o indice do Sprite
		indiceSprite = 0;
	}

	@Override
	public void render () {

		indiceSprite += Gdx.graphics.getDeltaTime() * 10;

		//se o indice do Sprite for maior que 9 zerar novamente o indice do Sprite
		if (indiceSprite>9){
			indiceSprite = 0;
		}
		Gdx.app.log("Valor: ",""+indiceSprite);
        batch.begin();

        batch.draw(fundo,0,0,larguraPadrãoX,alturaPadrãoY);
        batch.draw(ninja[(int)indiceSprite],400,300);

        batch.end();
	}
	
	@Override
	public void dispose () {

        batch.dispose();
	}
}
