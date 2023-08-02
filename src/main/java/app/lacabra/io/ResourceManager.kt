package app.lacabra.io

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class ResourceManager(
    private val path: String
) {

    val content: String
        get() {
            val file = getFileAsIOStream(path)
            return fileContent(file)
        }


    private fun getFileAsIOStream(fileName: String): InputStream {
        return javaClass
            .classLoader
            .getResourceAsStream(fileName) ?: throw IllegalArgumentException("$fileName is not found")
    }

    @Throws(IOException::class)
    private fun fileContent(`is`: InputStream): String {
        var lines = ""
        InputStreamReader(`is`).use { isr ->
            BufferedReader(isr).use { br ->
                var line: String?
                while (br.readLine().also { line = it } != null) {
                    lines += line + "\n"
                }
                `is`.close()
            }
        }
        return lines
    }

}