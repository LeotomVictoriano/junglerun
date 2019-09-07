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

	@Override
	public void create () {

	    batch = new SpriteBatch();
	    fundo = new Texture("fundo1.png");

	    larguraPadrãoX = Gdx.graphics.getWidth();
	    alturaPadrãoY = Gdx.graphics.getHeight();
	}

	@Override
	public void render () {

        batch.begin();

        batch.draw(fundo,0,0,larguraPadrãoX,alturaPadrãoY);

        batch.end();
	}
	
	@Override
	public void dispose () {

        batch.dispose();
	}
}
