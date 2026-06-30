package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class FlappyKitty extends Game {
    protected SpriteBatch batch;
    protected BitmapFont font;
    protected ShapeRenderer shape;
    protected Texture background;
    protected GlyphLayout glyphLayout;
    protected Sound btnSound;
    public final int screenX = 300;
    public final int screenY = 140;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
        shape = new ShapeRenderer();
        font.getData().setScale(2);
        this.setScreen(new MainMenuScreen(this));
        this.glyphLayout = new GlyphLayout();
        this.background = new Texture(Gdx.files.internal("background.png"));
        this.btnSound = Gdx.audio.newSound(Gdx.files.internal("sfx_click.wav"));
    }

    public void renderPregameText(){
        font.getData().setScale(0.8f);
        glyphLayout.setText(font,"PRESS SPACEBAR TO START!");
        font.draw(batch, glyphLayout, (screenX - glyphLayout.width)/2, 90);
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        btnSound.dispose();
    }
}
