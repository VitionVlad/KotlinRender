import java.io.File
import java.io.InputStream
import java.util.Scanner
import javax.swing.JFrame
import java.awt.*
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

class Render(size: IVec2, mesh: Array<Vec3>): JPanel(){
    private var localmesh = mesh
    private var localsize = size
    private var position = Vec3()
    private fun coordToScreen(coord: Vec3, size: IVec2): IVec2{
        var convert = Vec2()
        convert.x = coord.x*size.x
        convert.y = coord.y*size.y
        var finale = IVec2()
        finale.x = convert.x.toInt()
        finale.y = convert.y.toInt()
        return finale
    }
    private fun doDrawing(g: Graphics) {
        position.x = +1.5f
        position.y = -2.0f
        position.z = 20.0f
        val g2d = g as Graphics2D
        g2d.paint = Color(150, 150, 150)
        val rh = RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        rh[RenderingHints.KEY_RENDERING] = RenderingHints.VALUE_RENDER_QUALITY
        g2d.setRenderingHints(rh)

        for(i in 0..localmesh.size-2){
            var vertex = Vec3()
            var vertex2 = Vec3()
            var tanhalffov = 1.0f/tan((120.0f/2)*(PI/180.0f)).toFloat()

            vertex2.z = (localmesh[i+1].z + position.z)/100f
            vertex.z = (localmesh[i].z + position.z)/100

            vertex.x = (localmesh[i].x + position.x)*tanhalffov*(1.0f/(vertex.z*10.0f))
            vertex.y = (localmesh[i].y - position.y)*tanhalffov*(1.0f/(vertex.z*10.0f))

            vertex2.x = (localmesh[i+1].x + position.x)*tanhalffov*(1.0f/(vertex2.z*10.0f))
            vertex2.y = (localmesh[i+1].y - position.y)*tanhalffov*(1.0f/(vertex2.z*10.0f))

            if(vertex.z in 0.0f..1.0f && vertex2.z in 0.0f..1.0f){
                var toRender = coordToScreen(vertex, localsize)
                var toRenderEnd = coordToScreen(vertex2, localsize)
                g2d.drawLine(toRender.x,  toRender.y, toRenderEnd.x, toRenderEnd.y)
            }
        }
    }
    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        super.repaint()
        doDrawing(g)
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
    var objfile: ObjReader = ObjReader()
    objfile.path = "test.obj"
    objfile.readObj()
    var mesh = objfile.readObj()
    var size = IVec2()
    size.x = 800
    size.y = 600
    var window = Window("Kotlin Render", size, mesh)
}
