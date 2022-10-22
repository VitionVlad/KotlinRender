package Engine.Math

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Vec3{
    var x: Float = 0.0f
    var y: Float = 0.0f
    var z: Float = 0.0f
    fun Sum(a: Vec3): Vec3{
        var rs = Vec3()
        rs.x = x + a.x
        rs.y = y + a.y
        rs.z = z + a.z
        return rs
    }
    fun Sub(a: Vec3): Vec3{
        var rs = Vec3()
        rs.x = x - a.x
        rs.y = y - a.y
        rs.z = z - a.z
        return rs
    }
}

class Ivec3{
    var x: Int = 0
    var y: Int = 0
    var z: Int = 0
}

class Vec2{
    var x: Float = 0.0f
    var y: Float = 0.0f
}

class IVec2{
    var x: Int = 0
    var y: Int = 0
}

class Mat4{
    var x = arrayOf(0.0f, 0.0f, 0.0f, 0.0f)
    var y = arrayOf(0.0f, 0.0f, 0.0f, 0.0f)
    var z = arrayOf(0.0f, 0.0f, 0.0f, 0.0f)
    var w = arrayOf(0.0f, 0.0f, 0.0f, 0.0f)
    fun makePerspectiveProj(fov: Float, far: Float, near: Float){
        var scale = 1.0f/ tan(fov/2* PI /180).toFloat()
        x[0] = scale
        y[1] = scale
        z[2] = -far / (far-near)
        w[2] = -far * near / (far-near)
        z[3] = -1.0f
        w[3] = 0.0f
    }
    fun makeTranslateMat(pos: Vec3){
        x[0] = 1.0f
        y[1] = 1.0f
        z[2] = 1.0f

        w[0] = pos.x
        w[1] = pos.y
        w[2] = pos.z

        w[3] = 1.0f
    }
    fun makeXRotMat(rot: Float){
        x[0] = 1.0f
        y[1] = cos(rot)
        z[1] = -sin(rot)

        y[2] = sin(rot)
        z[2] = cos(rot)

        w[3] = 1.0f
    }
    fun makeYRotMat(rot: Float){
        y[1] = 1.0f
        x[0] = cos(rot)
        z[0] = sin(rot)

        x[2] = -sin(rot)
        z[2] = cos(rot)

        w[3] = 1.0f
    }
    fun makeZRotMat(rot: Float){
        x[0] = cos(rot)
        y[0] = -sin(rot)

        x[1] = cos(rot)
        y[1] = cos(rot)

        w[3] = 1.0f
    }
    fun matrixRotate(rot: Float, axis: Vec3){
        var a = rot
        var c = cos(rot)
        var s = sin(rot)
        var temp = Vec3()
        temp.x = (1.0f-c)*axis.x
        temp.y = (1.0f-c)*axis.y
        temp.z = (1.0f-c)*axis.z
        x = arrayOf(c+temp.x*axis.x, temp.x*axis.y+s*axis.z, temp.x*axis.z-s*axis.y, 0.0f)
        y = arrayOf(temp.y*axis.x-s*axis.z, c+temp.x*axis.x, temp.y*axis.z-s*axis.x, 0.0f)
        z = arrayOf(temp.z*axis.x+s*axis.y, temp.z*axis.y-s*axis.x, c+temp.y*axis.y, 0.0f)
        w = arrayOf(0.0f, 0.0f, 0.0f, 1.0f)
    }
    fun clearMat(){
        x = arrayOf(0.0f, 0.0f, 0.0f, 0.0f)
        y = arrayOf(0.0f, 0.0f, 0.0f, 0.0f)
        z = arrayOf(0.0f, 0.0f, 0.0f, 0.0f)
        w = arrayOf(0.0f, 0.0f, 0.0f, 0.0f)
    }
    fun vecMultiply(vector: Vec3): Vec3{
        var Result = Vec3()
        Result.x = vector.x * x[0] + vector.y * y[0] + vector.z * z[0] + w[0]
        Result.y = vector.x * x[1] + vector.y * y[1] + vector.z * z[1] + w[1]
        Result.z = vector.x * x[2] + vector.y * y[2] + vector.z * z[2] + w[2]
        var   hw = vector.x * x[3] + vector.y * y[3] + vector.z * z[3] + w[3]
        Result.x /= hw
        Result.y /= hw
        Result.z /= hw
        return Result
    }
}