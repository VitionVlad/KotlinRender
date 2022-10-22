package Engine.Geometry

import Engine.Math.Ivec3
import Engine.Math.Vec3
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class Mesh{
    var Geometry: Array<Vec3> = emptyArray()
    var MeshPosition = Vec3()
    var Color = Ivec3()
    var RenderWired: Boolean = false
    var BackFaceCulling: Int = 1
    var Borders = Vec3()
    private var localPos = Vec3()
    fun calcBorders(){
        var max = Vec3()
        for(i in 0..Geometry.size-3){
            if(abs(Geometry[i].x) > max.x){
                max.x = abs(Geometry[i].x)
            }
            if(abs(Geometry[i].y) > max.y){
                max.y = abs(Geometry[i].y)
            }
            if(abs(Geometry[i].y) > max.y){
                max.y = abs(Geometry[i].y)
            }
        }
        Borders = max
    }
    fun meshMeshIntersection(m1: Mesh){
        if(m1.MeshPosition.x + m1.Borders.x >= MeshPosition.x - Borders.x){
            MeshPosition.x = localPos.x
        }else{
            localPos.x = MeshPosition.x
        }
        if(m1.MeshPosition.x - m1.Borders.x >= MeshPosition.x + Borders.x){
            MeshPosition.x = localPos.x
        }else{
            localPos.x = MeshPosition.x
        }

        if(m1.MeshPosition.y + m1.Borders.y >= MeshPosition.y - Borders.y){
            MeshPosition.y = localPos.y
        }else{
            localPos.y = MeshPosition.y
        }
        if(m1.MeshPosition.y - m1.Borders.y >= MeshPosition.y + Borders.y){
            MeshPosition.y = localPos.y
        }else{
            localPos.y = MeshPosition.y
        }

        if(m1.MeshPosition.z + m1.Borders.z >= MeshPosition.z - Borders.z){
            MeshPosition.z = localPos.z
        }else{
            localPos.z = MeshPosition.z
        }
        if(m1.MeshPosition.z - m1.Borders.z >= MeshPosition.z + Borders.z){
            MeshPosition.z = localPos.z
        }else{
            localPos.z = MeshPosition.z
        }
    }
    fun posMeshIntersection(m1: Mesh){
        if(m1.MeshPosition.x >= MeshPosition.x - Borders.x){
            MeshPosition.x = localPos.x
        }else{
            localPos.x = MeshPosition.x
        }
        if(m1.MeshPosition.x  >= MeshPosition.x + Borders.x){
            MeshPosition.x = localPos.x
        }else{
            localPos.x = MeshPosition.x
        }

        if(m1.MeshPosition.y  >= MeshPosition.y - Borders.y){
            MeshPosition.y = localPos.y
        }else{
            localPos.y = MeshPosition.y
        }
        if(m1.MeshPosition.y >= MeshPosition.y + Borders.y){
            MeshPosition.y = localPos.y
        }else{
            localPos.y = MeshPosition.y
        }

        if(m1.MeshPosition.z >= MeshPosition.z - Borders.z){
            MeshPosition.z = localPos.z
        }else{
            localPos.z = MeshPosition.z
        }
        if(m1.MeshPosition.z  >= MeshPosition.z + Borders.z){
            MeshPosition.z = localPos.z
        }else{
            localPos.z = MeshPosition.z
        }
    }
}

class ObjReader{
    fun readObj(path: String): Mesh{
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
        var Export: Mesh = Mesh()
        Export.Geometry = finalvertexlist.toTypedArray()
        return Export
    }
}