import Engine.*

fun main(args: Array<String>) {
    position.z = -3.0f
    var objfile: ObjReader = ObjReader()
    objfile.path = "/home/vlad/IdeaProjects/KTExperimets/src/main/resources/test.obj"
    objfile.readObj()
    var mesh = arrayOf(objfile.readObj(), objfile.readObj())
    mesh[0].Color.x = 200
    mesh[1].RenderWired = true
    var size = IVec2()
    size.x = 800
    size.y = 600
    var window = Window("Kotlin Render", "true", size, mesh)
}