import kotlin.math.min
import kotlin.math.max

fun testCrop() {
    val w = 1920f
    val h = 1080f
    val boxSizePx = 200f
    val circleRadiusPx = 70f
    
    val offsetX = 0f
    val offsetY = 0f
    val scale = 2f
    
    val fitScale = min(boxSizePx / w, boxSizePx / h)
    
    val cropLeft = (((-circleRadiusPx - offsetX) / (fitScale * scale)) + w / 2f).toInt()
    val cropTop = (((-circleRadiusPx - offsetY) / (fitScale * scale)) + h / 2f).toInt()
    val cropRight = (((circleRadiusPx - offsetX) / (fitScale * scale)) + w / 2f).toInt()
    val cropBottom = (((circleRadiusPx - offsetY) / (fitScale * scale)) + h / 2f).toInt()
    
    println("fitScale: $fitScale")
    println("cropLeft: $cropLeft, cropRight: $cropRight")
    println("cropTop: $cropTop, cropBottom: $cropBottom")
    println("croppedWidth: ${cropRight - cropLeft}")
    println("croppedHeight: ${cropBottom - cropTop}")
}

testCrop()
