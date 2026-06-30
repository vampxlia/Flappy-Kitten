package GameLogic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.FlappyKitty;

import java.util.ArrayList;
import java.util.Random;

public class ObstacleHandler {
    private final ArrayList<Obstacle> obstaclesList;
    private FlappyKitty game;
    ObstacleHandler(FlappyKitty game){
        this.obstaclesList = new ArrayList<>();
        this.game = game;
    }

    private Obstacle generateObstacle() {
        Random random = new Random();
        return new Obstacle(38, game.screenX, 57,
                5 + random.nextInt(game.screenY - 57 - 5));
    }

    private void addObstacle(Obstacle obstacle) {
        obstaclesList.add(obstacle);
    }

    private void removeObstacle(Obstacle obstacle) {
        obstaclesList.remove(obstacle);
        obstacle.dispose();
    }

    public void renderHitboxes(ShapeRenderer shape) {
        for (Obstacle obstacle : obstaclesList) obstacle.renderHitbox(shape);
    }
    public void renderSprites(SpriteBatch batch) {
        for (Obstacle obstacle : obstaclesList) obstacle.renderSprite(batch);
    }

    public void spawnObstacle() {
        Obstacle obstacle = generateObstacle();
        addObstacle(obstacle);
        obstacle.create();
    }

    public void moveObstacles(float speed) {
        for (Obstacle obstacle : obstaclesList) obstacle.changeCoords(obstacle.getX() - speed);
        if (obstaclesList.get(0).getX() < -obstaclesList.get(0).getWidth()) removeObstacle(obstaclesList.get(0));
    }

    public Obstacle getLastObstacle(){
        return obstaclesList.get(obstaclesList.size()-1);
    }

    public Obstacle getObstacle(int i) {
        return this.obstaclesList.get(i);
    }

    public void dispose(){
        for(Obstacle obstacle : obstaclesList) obstacle.dispose();
    }

}
