package Engine

import javax.swing.JFrame
import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JPanel
import Engine.Math.*
import Engine.Input.*
import Engine.Geometry.*
import Engine.Render.*
import javax.imageio.ImageIO
import kotlin.system.exitProcess

var speed = 0.00005f

var sensivity = 1f

var enableMouseLook = true

var hideCursor = true

var renderComponent = EngineRender()

var enablePlayerColision = true

class FrameBuffer(size: IVec2, mesh: Array<Mesh>): JPanel(){
    private var localmesh: Array<Mesh> = mesh
    private var localsize = size
    private var mspos = IVec2()
    private fun doDrawing(g: Graphics) {
        val g2d = g as Graphics2D
        val rh = RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        rh[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY
        g2d.setRenderingHints(rh)

        if(enablePlayerColision){
            playerColision(localmesh)
        }

        if(enableMouseLook){
            var mousepos = MouseInfo.getPointerInfo()

            var r = Robot()

            if (mspos.x != mousepos.location.x - localsize.x / 2) {
                mspos.x = mousepos.location.x - localsize.x / 2
                r.mouseMove(localsize.x / 2, localsize.y / 2)
            }

            if (mspos.y != mousepos.location.y - localsize.y / 2) {
                mspos.y = mousepos.location.y - localsize.y / 2
                r.mouseMove(localsize.x / 2, localsize.y / 2)
            }

            rotation.x += -mspos.x.toFloat() / localsize.x * sensivity
            rotation.y += mspos.y.toFloat() / localsize.y * sensivity
        }


        var geom = renderComponent.Render(localsize, localmesh)

        for (i in geom.indices) {
            g2d.paint = Color(localmesh[geom[i].meshindex].Color.x, localmesh[geom[i].meshindex].Color.y, localmesh[geom[i].meshindex].Color.z)
            var vertex = geom[i].vertex1
            var vertex2 = geom[i].vertex2
            var vertex3 = geom[i].vertex3
            if (geom[i].allowrender) {
                var toRender = renderComponent.coordToScreen(vertex, localsize)
                var toRender1 = renderComponent.coordToScreen(vertex2, localsize)
                var toRender2 = renderComponent.coordToScreen(vertex3, localsize)
                var Mass1: Array<Int> = arrayOf(toRender.x, toRender1.x, toRender2.x)
                var Mass2: Array<Int> = arrayOf(toRender.y, toRender1.y, toRender2.y)
                when (localmesh[geom[i].meshindex].RenderWired) {
                    true -> g2d.drawPolygon(Mass1.toIntArray(), Mass2.toIntArray(), 3)
                    false -> g2d.fillPolygon(Mass1.toIntArray(), Mass2.toIntArray(), 3)
                }
            }
        }
    }
    public override fun paintComponent(g: Graphics) {
        super.addKeyListener(keyWork())
        super.paintComponent(g)
        super.setBackground(Color.BLACK)
        localsize.x = super.getSize().width
        localsize.y = super.getSize().height
        super.repaint()
        doDrawing(g)
        super.setFocusable(true)
        super.setFocusTraversalKeysEnabled(true)
        if(hideCursor){
            super.setCursor(super.getToolkit().createCustomCursor(BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), Point(), "a"))
        }else{
            super.setCursor(Cursor(Cursor.DEFAULT_CURSOR))
        }
    }
}

class Window(title: String, useOpenGL: String, size: IVec2, mesh: Array<Mesh>): JFrame(){
    init{
        var r = Robot()
        r.mouseMove(size.x/2, size.y/2)
        System.setProperty("sun.java2d.opengl", useOpenGL)
        createWindow(title, size, mesh)
        while (!isActive){}
    }
    private fun createWindow(wtitle: String,  size: IVec2, mesh: Array<Mesh>){
        add(FrameBuffer(size, mesh))
        title = wtitle
        isVisible = true
        setSize(size.x, size.y)
    }
    fun close(){
        exitProcess(1)
    }
}
