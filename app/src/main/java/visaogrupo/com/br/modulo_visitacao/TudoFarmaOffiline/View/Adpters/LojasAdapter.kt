package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Outline
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.gson.Gson
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Enuns.TrocaItemSelecionado
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.TrocarcorItem
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Lojas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.FormataValores
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.MudarFragment
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.GrupoLojaAbDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.KitDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades.ActPricipal
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogErro


class LojasAdapter (list :List<Lojas>, trocarcorItem: TrocarcorItem, frameid:Int, supportFragmentManager:FragmentManager, carrinhoVisible: carrinhoVisible, atualizaCarrinho: AtualizaCarrinho, context: Context) : Adapter<LojasAdapter.LojasViewHolder>() {
    var listaLojas = list
    val trocarcorItem = trocarcorItem
    val  frameid = frameid
    val supportFragmentManager = supportFragmentManager
    val carrinhoVisible = carrinhoVisible
    val  atualizaCarrinho = atualizaCarrinho
    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LojasViewHolder {
     val  view = LayoutInflater.from(parent.context).inflate(R.layout.celula_loja,parent,false)
     return LojasViewHolder(view)
    }

    override fun onBindViewHolder(holder: LojasViewHolder, position: Int) {
        holder.nomeloja.text = listaLojas[position].nome.toString()
        holder.valorMinimo.text = "Valor mínimo " + FormataValores.formatarParaMoeda(listaLojas[position].MinimoValor)
        val loja = listaLojas[position]

        holder. imgLoja.roundTopCorners(25f) // Raio desejado

        if (!loja.Base64.isEmpty()){
            val  imgBt = converterBase64ParaBitmap(loja.Base64)
            holder.imgLoja.setImageBitmap(imgBt)
        }
        holder.containerLoja.setOnClickListener {
            val sharedPreferences = holder.valorMinimo.context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val gson = Gson()
            val objetoSerializado = gson.toJson(listaLojas[position])
            val editor = sharedPreferences.edit()
            editor.putString("LojaSelecionada", objetoSerializado)
            editor.apply()
             ActPricipal.lojaTipo = listaLojas[position].LojaTipo
            //inicia produtos
            ActPricipal.troca = TrocaItemSelecionado.Prodtudos
            trocarcorItem.trocacor()
            val mudarFragment = MudarFragment()
            if (listaLojas[position].LojaTipo == 4){
                val kitDaO = KitDAO(context)
                val existeitem =  kitDaO.confereSeExisteItens()
                if (!existeitem){
                    val dialog = DialogErro()
                    dialog.Dialog(context,"Atenção","No momento essa loja nao está disponivel, Tente Realizar a carga novamente","Ok",""){

                    }
                }else{
                    mudarFragment.openFragmentProtudosKit(supportFragmentManager,frameid,carrinhoVisible, atualizaCarrinho)

                }
            }else if(listaLojas[position].LojaTipo == 13){
                val grupoLojaAB = GrupoLojaAbDAO(context)
                val existeitem1 =  grupoLojaAB.confereSeExisteItens()

                if (!existeitem1){
                    val dialog = DialogErro()
                    dialog.Dialog(context,"Atenção","No momento essa loja não está disponivel, Tente Realizar a carga novamente","Ok",""){

                    }
                }else{
                    mudarFragment.openFragmentProtudosAB(supportFragmentManager,carrinhoVisible)

                }

            }else{
                mudarFragment.openFragmentProtudos(supportFragmentManager,frameid,carrinhoVisible, atualizaCarrinho)

            }

        }
    }

    override fun getItemCount(): Int {
       return listaLojas.size
    }

    class LojasViewHolder(itemView: View) : ViewHolder(itemView){
        val containerLoja = itemView.findViewById<ConstraintLayout>(R.id.containerloja)
        val nomeloja = itemView.findViewById<TextView>(R.id.nomeloja)
        val valorMinimo = itemView.findViewById<TextView>(R.id.valorminimo)
        val  imgLoja = itemView.findViewById<ImageView>(R.id.imgLoja)
    }
    fun converterBase64ParaBitmap(base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun ImageView.roundTopCorners(radius: Float) {
        val outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                outline?.setRoundRect(0, 0, view!!.width, view.height + radius.toInt(), radius)
            }
        }

        this.outlineProvider = outlineProvider
        this.clipToOutline = true
    }

    fun ImageView.removeRoundedCorners() {
        this.outlineProvider = null
        this.clipToOutline = false
    }
}