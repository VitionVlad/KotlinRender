import java.io.File
import java.io.InputStream
import java.util.Scanner
import javax.swing.JFrame
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JPanel
import javax.swing.text.Position
import kotlin.math.PI
import kotlin.math.*

class Vec3{
    var x: Float = 0.0f
    var y: Float = 0.0f
    var z: Float = 0.0f
}

class Vec2{
    var x: Float = 0.0f
    var y: Float = 0.0f
}

class IVec2{
    var x: Int = 0
    var y: Int = 0
}

class ObjReader{
    var path: String = "example.obj"
    fun readObj(): Array<Vec3>{
        val obj: InputStream = File(path).inputStream()
        var scan: Scanner = Scanner(obj)
        var vertline: Int = 1
        var faceline: Int = 0
        var vertexList: ArrayList<Vec3> = arrayListOf(Vec3())
        var faceList: ArrayList<Int> = arrayListOf(0)
        while(scan.hasNextLine()){
            val lin: String = scan.nextLine()
            val elem = lin.split(" ")
            var vertex: Vec3 = Vec3()
            var faceindices: Array<Int> = arrayOf(0, 0, 0)
            if(elem[0] == "v"){
                vertex.x = elem[1].toFloat()
                vertex.y = elem[2].toFloat()
                vertex.z = elem[3].toFloat()
                vertexList.add(vertline, vertex)
                vertline++
            }
            if(elem[0] == "f"){
                faceindices[0] = elem[1].toInt()
                faceindices[1] = elem[2].toInt()
                faceindices[2] = elem[3].toInt()
                faceList.add(faceline, faceindices[0])
                faceList.add(faceline+1, faceindices[1])
                faceList.add(faceline+2, faceindices[2])
                faceline+=3
            }
        }
        var finalvertexlist: ArrayList<Vec3> = arrayListOf(Vec3())
        for(i in 0..faceline){
            finalvertexlist.add(i, vertexList[faceList[i]])
        }
        return finalvertexlist.toTypedArray()
    }
}

var position = Vec3()

var rotation = Vec2()

var speed = 0.002f

class keyWork(): KeyListener{
    public override fun keyTyped(e: KeyEvent){
        if(e.keyCode == 87){
            position.z -= speed
        }
        if(e.keyCode == 83){
            position.z += speed
        }
        if(e.keyCode == 65){
            position.x += speed
        }
        if(e.keyCode == 68){
            position.x -= speed
        }
    }
    public override fun keyPressed(e: KeyEvent){
        if(e.keyCode == 87){
            position.z -= speed
        }
        if(e.keyCode == 83){
            position.z += speed
        }
        if(e.keyCode == 65){
            position.x += speed
        }
        if(e.keyCode == 68){
            position.x -= speed
        }
    }
    public override fun keyReleased(e: KeyEvent){}
}

class Render(size: IVec2, mesh: Array<Vec3>): JPanel(){
    private var localmesh = mesh
    private var localsize = size
    private fun coordToScreen(coord: Vec3, size: IVec2): IVec2{
        var convert = Vec2()
        convert.x = (coord.x*size.x)
        convert.y = (coord.y*size.y)
        var finale = IVec2()
        finale.x = convert.x.toInt()
        finale.y = convert.y.toInt()
        return finale
    }
    private fun doDrawing(g: Graphics) {
        val g2d = g as Graphics2D
        val rh = RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        rh[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY
        g2d.setRenderingHints(rh)

        g2d.paint = Color(0, 0, 0)

        var i: Int = 0
        while(i <= localmesh.size-3){
            var vertex = Vec3()
            var vertex2 = Vec3()
            var vertex3 = Vec3()
            var tanhalffov = 1.0f/tan((120.0f/2)*(PI/180.0f)).toFloat()

            vertex2.z = (localmesh[i+1].z + position.z)/100f
            vertex3.z = (localmesh[i+2].z + position.z)/100f
            vertex.z = (localmesh[i].z + position.z)/100

            vertex.x = (localmesh[i].x + position.x)*tanhalffov*(1.0f/(vertex.z*100.0f))
            vertex.y = (localmesh[i].y - position.y)*tanhalffov*(1.0f/(vertex.z*100.0f))

            vertex2.x = (localmesh[i+1].x + position.x)*tanhalffov*(1.0f/(vertex2.z*100.0f))
            vertex2.y = (localmesh[i+1].y - position.y)*tanhalffov*(1.0f/(vertex2.z*100.0f))

            vertex3.x = (localmesh[i+2].x + position.x)*tanhalffov*(1.0f/(vertex3.z*100.0f))
            vertex3.y = (localmesh[i+2].y - position.y)*tanhalffov*(1.0f/(vertex3.z*100.0f))

            if(vertex.z in 0.0f..1.0f && vertex2.z in 0.0f..1.0f){
                var toRender = coordToScreen(vertex, localsize)
                var toRender1 = coordToScreen(vertex2, localsize)
                var toRender2 = coordToScreen(vertex3, localsize)
                var Mass1: Array<Int> = arrayOf(toRender.x, toRender1.x, toRender2.x)
                var Mass2: Array<Int> = arrayOf(toRender.y, toRender1.y, toRender2.y)
                //g2d.drawPolygon(Mass1.toIntArray(), Mass2.toIntArray(), 3)
                g2d.fillPolygon(Mass1.toIntArray(), Mass2.toIntArray(), 3)
            }
            i+=3
        }
    }
    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        super.addKeyListener(keyWork())
        localsize.x = super.getSize().width
        localsize.y = super.getSize().height
        super.repaint()
        doDrawing(g)
        super.setFocusable(true)
        super.setFocusTraversalKeysEnabled(true)
    }
}

class Window(title: String, size: IVec2, mesh: Array<Vec3>): JFrame(){
    init{
        createWindow(title, size, mesh)
    }
    private fun createWindow(wtitle: String,  size: IVec2, mesh: Array<Vec3>){
        add(Render(size, mesh))
        title = wtitle
        isVisible = true
        setSize(size.x, size.y)
    }
}

fun main(args: Array<String>) {
    position.x = +3.0f
    position.y = -3.5f
    position.z = 5.0f
    var objfile: ObjReader = ObjReader()
    objfile.path = "/home/vlad/IdeaProjects/KTExperimets/src/main/resources/test.obj"
    objfile.readObj()
    var mesh = objfile.readObj()
    var size = IVec2()
    size.x = 320
    size.y = 240
    var window = Window("Kotlin Render", size, mesh)
}
