package Engine

import javax.swing.JFrame
import java.awt.*
import java.awt.image.BufferedImage
import javax.swing.JPanel
import Engine.Math.*
import Engine.Input.*
import Engine.Geometry.*

var position = Vec3()

var rotation = Vec2()

var speed = 0.00005f

var sensivity = 1f

var enableMeshColision = true

var fov = 120.0f

var enableMeshOrdering = true

class Render(size: IVec2, mesh: Array<Mesh>): JPanel(){
    private var localmesh: Array<Mesh> = mesh
    private var localsize = size
    private var mspos = IVec2()
    var lastPlayerPosition = Vec3()
    class Triangle{
        var vertex1 = Vec3()
        var vertex2 = Vec3()
        var vertex3 = Vec3()
        var center = Vec3()
        var meshindex = 0
    }
    fun returnLocation(){
        position.x = lastPlayerPosition.x
        position.y = lastPlayerPosition.y
        position.z = lastPlayerPosition.z
    }
    fun playerColision(mesh: Array<Mesh>){
        for(i in mesh.indices){
            if(position.z in mesh[i].MeshPosition.z-mesh[i].Borders.z..mesh[i].MeshPosition.z+mesh[i].Borders.z && position.y in mesh[i].MeshPosition.y-mesh[i].Borders.y..mesh[i].MeshPosition.y+mesh[i].Borders.y && position.x in mesh[i].MeshPosition.x-mesh[i].Borders.x..mesh[i].MeshPosition.x+mesh[i].Borders.x){
                returnLocation()
            }
        }
        lastPlayerPosition.x = position.x.toFloat()
        lastPlayerPosition.y = position.y.toFloat()
        lastPlayerPosition.z = position.z.toFloat()
    }
    private fun coordToScreen(coord: Vec3, size: IVec2): IVec2{
        var convert = Vec2()
        convert.x = (coord.x*(size.x/2))+size.x/2
        convert.y = (coord.y*(size.y/2))+size.y/2
        var finale = IVec2()
        finale.x = convert.x.toInt()
        finale.y = convert.y.toInt()
        return finale
    }
    private fun ccw(a: Vec3, b: Vec3, c: Vec3): Float{
        return (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y)
    }
    private fun doDrawing(g: Graphics) {
        val g2d = g as Graphics2D
        val rh = RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        rh[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY
        g2d.setRenderingHints(rh)

        if (enableMeshColision) {
            playerColision(localmesh)
        }

        var geom: ArrayList<Triangle> = arrayListOf()

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

        for (meshnum in localmesh.indices) {
            for (i in 0..localmesh[meshnum].Geometry.size - 3 step 3) {
                var vertex = Vec3()
                var vertex2 = Vec3()
                var vertex3 = Vec3()

                // projection multiplication

                var proj = Mat4()

                proj.makeTranslateMat(position.Sub(localmesh[meshnum].MeshPosition))
                vertex = proj.vecMultiply(localmesh[meshnum].Geometry[i])
                vertex2 = proj.vecMultiply(localmesh[meshnum].Geometry[i + 1])
                vertex3 = proj.vecMultiply(localmesh[meshnum].Geometry[i + 2])

                proj.clearMat()

                proj.makeYRotMat(-rotation.x)
                vertex = proj.vecMultiply(vertex)
                vertex2 = proj.vecMultiply(vertex2)
                vertex3 = proj.vecMultiply(vertex3)

                proj.clearMat()

                proj.makeXRotMat(-rotation.y)
                vertex = proj.vecMultiply(vertex)
                vertex2 = proj.vecMultiply(vertex2)
                vertex3 = proj.vecMultiply(vertex3)

                proj.clearMat()

                proj.makePerspectiveProj(120.0f, 100.0f, 0.1f)
                vertex = proj.vecMultiply(vertex)
                vertex2 = proj.vecMultiply(vertex2)
                vertex3 = proj.vecMultiply(vertex3)

                var toadd = Triangle()

                toadd.vertex1 = vertex
                toadd.vertex2 = vertex2
                toadd.vertex3 = vertex3

                toadd.meshindex = meshnum

                toadd.center.x = (vertex.x+vertex2.x+vertex3.x)/3
                toadd.center.y = (vertex.y+vertex2.y+vertex3.y)/3
                toadd.center.z = (vertex.z+vertex2.z+vertex3.z)/3

                geom.add(toadd)
            }
        }
        geom = ArrayList(geom.sortedByDescending { it.center.z })
        for (i in geom.indices) {
            g2d.paint = Color(localmesh[geom[i].meshindex].Color.x, localmesh[geom[i].meshindex].Color.y, localmesh[geom[i].meshindex].Color.z)
            var vertex = geom[i].vertex1
            var vertex2 = geom[i].vertex2
            var vertex3 = geom[i].vertex3
            var isccw: Float = when (localmesh[geom[i].meshindex].BackFaceCulling) {
                0 -> 1.0f
                1 -> ccw(vertex, vertex2, vertex3)
                2 -> -ccw(vertex, vertex2, vertex3)
                else -> ccw(vertex, vertex2, vertex3)
            }
            if (vertex.z in 0.0f..1.0f && vertex2.z in 0.0f..1.0f && vertex3.z in 0.0f..1.0f && isccw > 0f) {
                var toRender = coordToScreen(vertex, localsize)
                var toRender1 = coordToScreen(vertex2, localsize)
                var toRender2 = coordToScreen(vertex3, localsize)
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
        localsize.x = super.getSize().width
        localsize.y = super.getSize().height
        super.repaint()
        doDrawing(g)
        super.setFocusable(true)
        super.setFocusTraversalKeysEnabled(true)
        super.setCursor(super.getToolkit().createCustomCursor(BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), Point(), "a"))
    }
}

class Window(title: String, useOpenGL: String, size: IVec2, mesh: Array<Mesh>): JFrame(){
    init{
        var r = Robot()
        r.mouseMove(size.x/2, size.y/2)
        System.setProperty("sun.java2d.opengl", useOpenGL)
        createWindow(title, size, mesh)
    }
    private fun createWindow(wtitle: String,  size: IVec2, mesh: Array<Mesh>){
        add(Render(size, mesh))
        title = wtitle
        isVisible = true
        setSize(size.x, size.y)
    }
}