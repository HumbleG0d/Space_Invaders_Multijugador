package io.game.gamesobject;

import io.game.gamesobject.enemigues.Enemy;

public class CollisionManager {

    public static boolean isCollision(Bullet bullet, Pj pj){
        float dx = bullet.getPosition().x - pj.getPosition().x;
        float dy = bullet.getPosition().y - pj.getPosition().y;
        return Math.sqrt(dx * dx + dy * dy) < 30;
    }

}
