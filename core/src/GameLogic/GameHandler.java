package GameLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.FlappyKitty;
import com.mygdx.game.GameOverScreen;
import com.mygdx.game.GameScreen;

import java.util.Random;

public class GameHandler {
    private final Player player;
    private final ObstacleHandler obsHandler;
    private final ScoreHandler xml;
    private int score;
    private float playerVelocity;
    private final Random r;
    private FlappyKitty game;
    private final int obstacleVelocity;
    private int backgroundCoord;
    private final Sound gameOverSound;
    private enum State {
        PRE_GAME,
        GAME_RUN,
        GAME_PAUSE,
        GAME_OVER
    }
    private State state;

    public GameHandler(FlappyKitty game) {
        this.game = game;
        this.player = new Player("player.png", 20, game.screenY/2, 17, 12);
        this.obsHandler = new ObstacleHandler(game);
        this.xml = new ScoreHandler();

        this.r = new Random();
        this.state = State.PRE_GAME;
        this.score = 0;
        this.playerVelocity = -1;
        this.obstacleVelocity = 1;

        this.backgroundCoord = 0;
        this.gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sfx_gameover.wav"));
    }
    public void gameCycle(GameScreen screen) {
        switch (state){
            case PRE_GAME:
                game.renderPregameText();
                backgroundCoord -= 1;
                if (backgroundCoord == -game.screenX) backgroundCoord = 0;
                if(player.getY() < (float) game.screenY /2 - 10){
                    playerVelocity = 1;
                    player.changeAngle(5);
                }
                else if (player.getY() > (float) game.screenY /2 + 10){
                    playerVelocity += -1;
                    player.changeAngle(-5);
                }
                Player.PlayerMovement.movePlayer(playerVelocity);
                if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    player.playJumpSound();
                    playerVelocity += 6;
                    player.changeAngle(15);
                    state = State.GAME_RUN;
                };
                break;
            case GAME_RUN:
                backgroundCoord -= 1;
                if (backgroundCoord == -game.screenX) backgroundCoord = 0;
                Player.PlayerMovement.warp(game.screenY);
                Player.PlayerMovement.movePlayer(playerVelocity);
                playerVelocity = Player.PlayerMovement.changeVelocity(-2, playerVelocity);
                if (obsHandler.getLastObstacle().getX() < r.nextInt((int)(game.screenX * 0.75) - 20))
                    obsHandler.spawnObstacle();
                obsHandler.moveObstacles(obstacleVelocity);
                if (playerScored()) score++;
                if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) state = State.GAME_PAUSE;
                if (gameOver()) state = State.GAME_OVER;
                break;
            case GAME_PAUSE:
                if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) state = State.GAME_RUN;
                break;
            case GAME_OVER:
                gameOverSound.play();
                xml.addScores(score);
                xml.writeXML();
                game.setScreen(new GameOverScreen(game, score, getHighScore(), backgroundCoord, this));
                screen.dispose();
                break;
        }
                //prints();
    }

    public void postGame(){
        Player.PlayerMovement.movePlayer(-2);
        player.changeAngle(-80);
    }

    public int getScore(){return this.score;}

    public void renderBackground(Texture background, SpriteBatch batch){
        batch.draw(background, backgroundCoord, 0);
    }

    private void prints(){
        System.out.println("bird Y: " + player.getY());
        System.out.println("velocity: " + playerVelocity);
        System.out.println("first obstacle X: " + obsHandler.getObstacle(0).getX());
        System.out.println("Score: " + score);
        System.out.println("GameOver: " + gameOver());
        System.out.println("State: " + state);
    }

    public Player getPlayer(){return this.player;}
    public ObstacleHandler getObsHandler(){return this.obsHandler;}

    private boolean gameOver() {
        return (this.player.getHitbox().overlaps(this.obsHandler.getObstacle(0).getBottomHitbox()) ||
                this.player.getHitbox().overlaps(this.obsHandler.getObstacle(0).getTopHitbox()));
    }

    private boolean playerScored() {
        return (player.getX() == obsHandler.getObstacle(0).getX() + obsHandler.getObstacle(0).getWidth());
    }
    public int getHighScore(){
        return xml.getHighScore();
    }

    public void dispose(){
        player.dispose();
        obsHandler.dispose();
        gameOverSound.dispose();
    }
}
