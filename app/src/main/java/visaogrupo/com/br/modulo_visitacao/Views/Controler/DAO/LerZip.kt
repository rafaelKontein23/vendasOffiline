package visaogrupo.com.br.modulo_visitacao.Views.Controler.DAO

import android.util.Log
import java.util.zip.ZipFile

class LerZip {

    // Serve Para recuperar o Zip da memoria do celular e ler o arquivo que está dentro do zip, retorna o json do arquivo
    fun readTextFileFromZip(zipFilePath: String, fileName: String,TAG:String): String? {
        val zipFile = ZipFile(zipFilePath)
        val entry = zipFile.getEntry(fileName)
        if (entry == null || entry.isDirectory) {
            // O arquivo não existe ou é um diretório
            return null
        }
        val inputStream = zipFile.getInputStream(entry)
        val json = inputStream.bufferedReader().use { it.readText() }
        inputStream.close()
        zipFile.close()
        Log.d(TAG,json)

        return json
    }

}