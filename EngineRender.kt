package Engine.Render

import Engine.*
import Engine.Geometry.Mesh
import Engine.Math.*

var fov = 120.0f

var enablePaintersAlghortitm = true

var position = Vec3()

var rotation = Vec2()

class EngineRender{
     class Triangle{
        var vertex1 = Vec3()
        var vertex2 = Vec3()
        var vertex3 = Vec3()
        var center = Vec3()
        var meshindex = 0
    }
    fun coordToScreen(coord: Vec3, size: IVec2): IVec2{
        var convert = Vec2()
        convert.x = (coord.x*(size.x/2))+size.x/2
        convert.y = (coord.y*(size.y/2))+size.y/2
        var finale = IVec2()
        finale.x = convert.x.toInt()
        finale.y = convert.y.toInt()
        return finale
    }
    fun ccw(a: Vec3, b: Vec3, c: Vec3): Float{
        return (b.x - a.x) * (c.y - a.y) - (c.x - a.x) * (b.y - a.y)
    }
    fun Render(size: IVec2, localmesh: Array<Mesh>): ArrayList<Triangle>{
        var geom: ArrayList<Triangle> = arrayListOf()

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

                proj.makePerspectiveProj(120.0f, 100.0f, 0.0001f)
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
        if(enablePaintersAlghortitm){
            geom = ArrayList(geom.sortedByDescending { it.center.z })
        }
        return geom
    }
}