package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Personalizacao.CustomSpinnerItem
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.CustomSpinnerAdapter
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.Retrofit_Request.URLs
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActLogin
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActPricipal
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActRoteiro

class AdapterMenuCima (customSpinnerItem: List<CustomSpinnerItem>, context: Context, login:Login, actPricipal: ActPricipal) : Adapter<AdapterMenuCima.ViewHolderMenuCima>() {
    val listaSpinnerItem = customSpinnerItem
    val context = context
    val login = login
    val actPricipal = actPricipal
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMenuCima {
        val  view = LayoutInflater.from(parent.context).inflate(R.layout.custom_spinner_item,parent,false)
        return ViewHolderMenuCima(view)
    }

    override fun onBindViewHolder(holder: ViewHolderMenuCima, position: Int) {
        val itemMenu =listaSpinnerItem[position]
        holder.item_menu.text = itemMenu.item
        holder.imagem_menu.setImageResource(itemMenu?.icons ?: R.drawable.defaut)

        holder.container.setOnClickListener {
            if(itemMenu.item.contains("Portal")){
                val json = "{" +
                        "\"email\": \"" + login.Email + "\"," +
                        "\"senha\": \"" + login.Senha + "\"," +
                        "\"origem\":" + "\"app\"," +
                        "\"versaoapp\":" + "\"" + "1.0.5" + "\"" +
                        "}"

                // Encode para Base64
                val encodedString = Base64.encodeToString(json.toByteArray(), Base64.DEFAULT)

                val url = "${URLs.urlportal}mobile/${encodedString}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                actPricipal.startActivity(intent)
            }else if(itemMenu.item.contains("Adm")){
                val json: String = login.Email + ":" +login.Senha

                // Encode para Base64

                // Encode para Base64
                val encodedString =
                    org.apache.commons.codec.binary.Base64.encodeBase64String(json.toByteArray())

                val urladm: String = URLs.urlAdm + encodedString


                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urladm))
                actPricipal.startActivity(intent)

            }else if(itemMenu.item.contains("Sair")){
                MainScope().launch {
                    val  intent = Intent(actPricipal, ActLogin::class.java)
                    actPricipal.startActivity(intent)
                }

            }else if(itemMenu.item.contains("Visitas")){
                MainScope().launch {
                    val  intent = Intent(actPricipal, ActRoteiro::class.java)
                    actPricipal.startActivity(intent)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return listaSpinnerItem.size
    }
    class ViewHolderMenuCima(itemView: View) : ViewHolder(itemView){
         val container    = itemView.findViewById<ConstraintLayout>(R.id.container)
         val item_menu    = itemView.findViewById<TextView>(R.id.item_menu)
         val imagem_menu =  itemView.findViewById<ImageView>(R.id.iamgem_menu)
    }
}