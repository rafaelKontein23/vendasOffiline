package visaogrupo.com.br.modulo_visitacao.Views.Models.Ultis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Obejtos.Personalizacao.CustomSpinnerItem

class CustomSpinnerAdapter(
    context: Context,
    resource: Int,
    objects: List<CustomSpinnerItem>
) : ArrayAdapter<CustomSpinnerItem>(context, resource, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_spinner_item, parent, false)

        val item = getItem(position)
        val item_menu = view.findViewById<TextView>(R.id.item_menu)
        val iamgem_menu = view.findViewById<ImageView>(R.id.iamgem_menu)

        item_menu.text = item?.item
        iamgem_menu.setImageResource(item?.icons ?: R.drawable.defaut)

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }
}