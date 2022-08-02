import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun main(args: Array<String>) {
    val path = "/Users/simple/Desktop/workspace/android/sleepsignin/app/src/main/res/drawable-xxhdpi"
    println(path)

    val dir = File(path)
    if (dir.isFile) return

    val hashMap = hashMapOf<String, String>()

    dir.listFiles()?.map { child ->
//        println("name == ${child.name}")
        val unique = child.md5()

        if (hashMap.containsKey(unique)) {
            println("图片重复了")
            println("old = ${hashMap[unique]}")
            println("new = ${child.name}")
        } else if (unique.isNotEmpty()) {
            hashMap[unique] = child.name
        }
    }
}

fun File.md5() = bytes2HexString(encryptMD5File(this))

fun encryptMD5File(file: File?): ByteArray? {
    if (file == null) return null
    var fis: FileInputStream? = null
    val digestInputStream: DigestInputStream
    try {
        fis = FileInputStream(file)
        var md = MessageDigest.getInstance("MD5")
        digestInputStream = DigestInputStream(fis, md)
        val buffer = ByteArray(256 * 1024)
        while (true) {
            if (digestInputStream.read(buffer) <= 0) break
        }
        md = digestInputStream.messageDigest
        return md.digest()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        return null
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    } finally {
        try {
            fis?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}

val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

fun bytes2HexString(bytes: ByteArray?): String {
    if (bytes == null) return ""
    val len = bytes.size
    if (len <= 0) return ""
    val ret = CharArray(len shl 1)
    var i = 0
    var j = 0
    while (i < len) {
        ret[j++] = HEX_DIGITS[bytes[i].toInt() shr 4 and 0x0f]
        ret[j++] = HEX_DIGITS[bytes[i].toInt() and 0x0f]
        i++
    }
    return String(ret)
}