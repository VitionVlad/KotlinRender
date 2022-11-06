package Engine.Input

import Engine.Render.*
import Engine.speed
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import kotlin.math.cos
import kotlin.math.sin

class keyWork(): KeyListener {
    public override fun keyTyped(e: KeyEvent){
        if(e.keyCode == 87){
            position.z += cos(rotation.y) * cos(rotation.x) * speed
            position.x += cos(rotation.y) * cos(rotation.x) * speed
        }
        if(e.keyCode == 83){
            position.z -= cos(rotation.y) * cos(rotation.x) * speed
            position.x -= cos(rotation.y) * cos(rotation.x) * speed
        }
        if(e.keyCode == 65){
            position.z += cos(rotation.y) * cos(rotation.x) * speed
            position.x -= cos(rotation.y) * cos(rotation.x) * speed
        }
        if(e.keyCode == 68){
            position.z -= cos(rotation.y) * cos(rotation.x) * speed
            position.x += cos(rotation.y) * cos(rotation.x) * speed
        }

        if(e.keyCode == 81){
            position.y += speed
        }
        if(e.keyCode == 69){
            position.y -= speed
        }
    }
    public override fun keyPressed(e: KeyEvent){
        if(e.keyCode == 87){
            position.z += cos(rotation.y) * cos(rotation.x) * speed
            position.x += cos(rotation.y) * sin(rotation.x) * speed
        }
        if(e.keyCode == 83){
            position.z -= cos(rotation.y) * cos(rotation.x) * speed
            position.x -= cos(rotation.y) * sin(rotation.x) * speed
        }
        if(e.keyCode == 65){
            position.x += cos(rotation.y) * cos(rotation.x) * speed
            position.z -= cos(rotation.y) * sin(rotation.x) * speed
        }
        if(e.keyCode == 68){
            position.x -= cos(rotation.y) * cos(rotation.x) * speed
            position.z += cos(rotation.y) * sin(rotation.x) * speed
        }

        if(e.keyCode == 81){
            position.y += speed
        }
        if(e.keyCode == 69){
            position.y -= speed
        }
    }
    public override fun keyReleased(e: KeyEvent){}
}