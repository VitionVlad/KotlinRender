import Engine.Math.*
import Engine.Geometry.*
import Engine.Render.*
import Engine.*
import java.awt.Button
import java.awt.Frame
import javax.swing.JFrame

fun main(args: Array<String>) {
    position.z = -3.0f
    var objfile: ObjReader = ObjReader()
    var mesh = arrayOf(objfile.readObj("/home/vlad/IdeaProjects/KTExperimets/src/main/resources/test.obj"), objfile.readObj("/home/vlad/IdeaProjects/KTExperimets/src/main/resources/test.obj"))
    mesh[0].Color.x = 200
    mesh[1].RenderWired = true
    mesh[1].MeshPosition.y = 6f
    mesh[1].MeshPosition.x = 0.5f
    mesh[0].calcBorders()
    mesh[1].calcBorders()
    mesh[1].Color.x = 255
    mesh[1].Color.y = 255
    mesh[1].Color.z = 255
    var size = IVec2()
    size.x = 800
    size.y = 600
    camSize.x = 0.2f
    camSize.y = 1.7f
    var window = Window("Render", "true", size, mesh)
    while (window.isActive){
        mesh[1].PhysWork(mesh)
        Thread.sleep(10)
    }
    window.close()
}