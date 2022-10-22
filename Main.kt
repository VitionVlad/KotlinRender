import Engine.Math.*
import Engine.Geometry.*
import Engine.*

fun main(args: Array<String>) {
    position.z = -3.0f
    var objfile: ObjReader = ObjReader()
    var mesh = arrayOf(objfile.readObj("/home/vlad/IdeaProjects/KTExperimets/src/main/resources/test.obj"), objfile.readObj("/home/vlad/IdeaProjects/KTExperimets/src/main/resources/test.obj"))
    mesh[0].Color.x = 200
    mesh[1].RenderWired = true
    mesh[1].MeshPosition.y = 6f
    mesh[0].calcBorders()
    mesh[1].calcBorders()
    var size = IVec2()
    size.x = 800
    size.y = 600
    var window = Window("Kotlin Render", "true", size, mesh)
    while (true){
        mesh[1].PhysWork(mesh)
        Thread.sleep(10)
    }
}