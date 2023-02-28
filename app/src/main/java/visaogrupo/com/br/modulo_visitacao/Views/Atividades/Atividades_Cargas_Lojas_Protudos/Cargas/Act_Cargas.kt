package visaogrupo.com.br.modulo_visitacao.Views.Atividades.Atividades_Cargas_Lojas_Protudos.Cargas

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_act_cargas.*
import visaogrupo.com.br.modulo_visitacao.R
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.core.graphics.drawable.DrawableCompat
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Obejtos.Personalizacao.CustomSpinnerItem
import visaogrupo.com.br.modulo_visitacao.Views.Models.Ultis.CustomSpinnerAdapter
import visaogrupo.com.br.modulo_visitacao.Views.Models.Ultis.Trocar_cor_de_icon

class Act_Cargas : AppCompatActivity() {

    var list_menu:MutableList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_cargas)
        list_menu.add("")
        list_menu.add("")
        list_menu.add("")
        list_menu.add("")

        //clicks dos itens bottomnavigation

        val clickListenerhome = View.OnClickListener {
            seleciona(text_home,view_home,icon_home);
            Deseleciona_itens(text_pedidos,text_clientes,text_lojas,text_protudo,view_prdidos,
                view_clientes,view_lojas,view_produto,icon_pedidos,icon_clientes,icon_lojas,icon_produtos)
        }

         icon_home.setOnClickListener(clickListenerhome)
         text_home.setOnClickListener(clickListenerhome)

        val clickListenerpedidos = View.OnClickListener {
            seleciona(text_pedidos,view_prdidos,icon_pedidos);
            Deseleciona_itens(text_home,text_clientes,text_lojas,text_protudo,view_home,
                view_clientes,view_lojas,view_produto,icon_home,icon_clientes,icon_lojas,icon_produtos)
        }

        icon_pedidos.setOnClickListener (clickListenerpedidos)
        text_pedidos.setOnClickListener (clickListenerpedidos)

        val clickListenerclientes = View.OnClickListener {
            seleciona(text_clientes,view_clientes,icon_clientes);
            Deseleciona_itens(text_home,text_pedidos,text_lojas,text_protudo,view_home,
                view_prdidos,view_lojas,view_produto,icon_home,icon_pedidos,icon_lojas,icon_produtos)
        }

        icon_clientes.setOnClickListener(clickListenerclientes)
        text_clientes.setOnClickListener  (clickListenerclientes)


        val clickListenerlojas = View.OnClickListener {
            seleciona(text_lojas,view_lojas,icon_lojas);
            Deseleciona_itens(text_home,text_pedidos,text_clientes,text_protudo,view_home,
                view_prdidos,view_clientes,view_produto,icon_home,icon_pedidos,icon_clientes,icon_produtos)
        }

        icon_lojas.setOnClickListener(clickListenerlojas)
        text_lojas.setOnClickListener(clickListenerlojas)

        val clickListenerprodutos = View.OnClickListener {
            seleciona(text_protudo,view_produto,icon_produtos);
            Deseleciona_itens(text_home,text_pedidos,text_clientes,text_lojas,view_home,
                view_prdidos,view_clientes,view_lojas,icon_home,icon_pedidos,icon_clientes,icon_lojas)
        }

        icon_produtos.setOnClickListener (clickListenerprodutos)
        text_protudo.setOnClickListener(clickListenerprodutos)

        // clik do do spiner

        val items = listOf(
            CustomSpinnerItem("", R.drawable.defaut),
            CustomSpinnerItem("Portal", R.drawable.portal),
            CustomSpinnerItem("Adm", R.drawable.adm),
            CustomSpinnerItem("Sobre",R.drawable.sobre),
            CustomSpinnerItem("Sair",R.drawable.sair)
            // adicionar mais itens personalizados aqui
        )
        val adapter = CustomSpinnerAdapter(this, R.layout.custom_spinner_item, items)
        menucim_spnier.adapter = adapter

    }


    private fun Deseleciona_itens(deselecionar1:TextView,deselecionar2:TextView,deselecionar3:TextView,deselecionar4:TextView,
                          view1:View,view2:View,view3:View,view4:View,icon1:ImageView,icon2:ImageView,icon3:ImageView,icon4:ImageView){
        var trocar_cor = Trocar_cor_de_icon()
        deselecionar1.setTextColor(Color.parseColor("#737880"))
        view1.          visibility = View.GONE
        var drawable1 = icon1.drawable
        trocar_cor.trocar_cor_iten(icon1,drawable1,"#737880")



        deselecionar2.setTextColor(Color.parseColor("#737880"))
        view2.        visibility = View.GONE
        var drawable2 = icon2.drawable
        trocar_cor.trocar_cor_iten(icon2,drawable2,"#737880")


        deselecionar3.setTextColor(Color.parseColor("#737880"))
        view3.        visibility = View.GONE
        var drawable3 = icon3.drawable
        trocar_cor.trocar_cor_iten(icon3,drawable3,"#737880")


        deselecionar4.setTextColor(Color.parseColor("#737880"))
        view4.        visibility = View.GONE
        var drawable4 = icon4.drawable
        trocar_cor.trocar_cor_iten(icon4,drawable4,"#737880")


    }
    private  fun seleciona(seleciona_text:TextView, seleciona_view:View,seleciona_icon:ImageView){
        var trocar_cor = Trocar_cor_de_icon()
        seleciona_view.visibility = View.VISIBLE
        seleciona_text.setTextColor(Color.parseColor("#004076"))
        var drawable = seleciona_icon.drawable
        trocar_cor.trocar_cor_iten(seleciona_icon,drawable,"#004076")
    }
}