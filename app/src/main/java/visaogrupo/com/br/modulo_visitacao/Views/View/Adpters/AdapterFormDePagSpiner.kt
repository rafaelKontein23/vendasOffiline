package visaogrupo.com.br.modulo_visitacao.Views.View.Adpters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.CustomSpinerFormDePag

class AdapterFormDePagSpiner(context: Context,
                             resource:Int,
                             objects: List<CustomSpinerFormDePag>,
) : ArrayAdapter<CustomSpinerFormDePag>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.celula_spiner_exclusiva, parent, false)

        val item = getItem(position)
        val TextoFormPag = view.findViewById<TextView>(R.id.TextoFormPag)
        val excluivo = view.findViewById<TextView>(R.id.excluivo)

        TextoFormPag.text = item?.itemText
        excluivo.isVisible = item?.textExclusvo == 1


        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}
