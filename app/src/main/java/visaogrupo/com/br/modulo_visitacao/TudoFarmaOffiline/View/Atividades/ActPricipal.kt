package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_act_cargas.icon_clientes
import kotlinx.android.synthetic.main.activity_act_cargas.icon_home
import kotlinx.android.synthetic.main.activity_act_cargas.icon_lojas
import kotlinx.android.synthetic.main.activity_act_cargas.icon_pedidos
import kotlinx.android.synthetic.main.activity_act_cargas.icon_produtos
import kotlinx.android.synthetic.main.activity_act_cargas.qtdNotification
import kotlinx.android.synthetic.main.activity_act_cargas.text_clientes
import kotlinx.android.synthetic.main.activity_act_cargas.text_home
import kotlinx.android.synthetic.main.activity_act_cargas.text_lojas
import kotlinx.android.synthetic.main.activity_act_cargas.text_pedidos
import kotlinx.android.synthetic.main.activity_act_cargas.text_protudo
import kotlinx.android.synthetic.main.activity_act_cargas.view_clientes
import kotlinx.android.synthetic.main.activity_act_cargas.view_home
import kotlinx.android.synthetic.main.activity_act_cargas.view_lojas
import kotlinx.android.synthetic.main.activity_act_cargas.view_prdidos
import kotlinx.android.synthetic.main.activity_act_cargas.view_produto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Enuns.TrocaItemSelecionado
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.MostraLoad
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.TrocarcorItem
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Login
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Personalizacao.CustomSpinnerItem
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.MudarFragment
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.Trocar_cor_de_icon
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.CarrinhoDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.NotificacaoDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdapterMenuCima
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.Alertas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogErro
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentCargas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentClientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentLojas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentPedidos
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentProdutosKit
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentProdutosLojaAB
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Fragments.FragmentProtudos


class ActPricipal : AppCompatActivity(),
    TrocarcorItem,
    carrinhoVisible,
    AtualizaCarrinho,MostraLoad,
    FragmentCargas.MyCallback {

    var list_menu:MutableList<String> = ArrayList<String>()
    val fragmentLojas =    FragmentLojas(
        this,this,this)
    val fragmentCargas =   FragmentCargas()
    val fragmentClientes = FragmentClientes(this,this, this)
    val fragmentProtudos = FragmentProtudos(this,this)
    val fragmentProtudosKit = FragmentProdutosKit(this)
    val fragmentProdutoAB= FragmentProdutosLojaAB(this)
    val fragementPedido = FragmentPedidos(this, this)
    lateinit var viewcarrinho :TextView
    lateinit var qtdNotificacoes :TextView
    lateinit var viewnotification :View
    lateinit var menuRecycler:RecyclerView
    lateinit var login: Login
    lateinit var viewCarregando :View
    lateinit var  carregandoProgress :ProgressBar
    lateinit var textCarregando:TextView
    lateinit var iconeNotificacao:ImageView
    lateinit var noticaocaoIcon :View
    lateinit var quatidadeNotificao :TextView
    lateinit var  menuCima:ImageView
    lateinit var containerActPrincipal:ConstraintLayout
    lateinit var fragmentContainerViewPrincipal: FrameLayout
    val context = this
    companion object {
        var  troca = TrocaItemSelecionado.home
        var clienteUF =""
        var cliente_id = 0
        var loja_id    =0
        var lojavalorMinimo =0.0
        var lojaTipo = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_cargas)
        list_menu.add("")

        FirebaseApp.initializeApp(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
        }


        viewcarrinho       = findViewById(R.id.carrinhoO)
        qtdNotificacoes     = findViewById(R.id.qtdNotification)
        viewnotification    = findViewById(R.id.viewnotification)
        viewCarregando     = findViewById(R.id.viewCarregando)
        carregandoProgress = findViewById(R.id.carregandoProgress)
        textCarregando     = findViewById(R.id.textCarregando)
        iconeNotificacao    = findViewById(R.id.notificao)
        noticaocaoIcon     = findViewById(R.id.noticaocaoIcon)
        quatidadeNotificao  = findViewById(R.id.quatidadeNotificao)
        menuRecycler       = findViewById(R.id.menuRecycler)
        menuCima           = findViewById(R.id.menu_cima)
        containerActPrincipal = findViewById(R.id.containerActPrincipal)
        fragmentContainerViewPrincipal = findViewById(R.id.fragmentContainerViewPrincipal)


        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val objetoSerializado = sharedPreferences?.getString("UserLogin", null)
        login =  gson.fromJson(objetoSerializado, Login::class.java)
        fragmentCargas.callback =this

        atualizaQtdCarrinhoNotificao()
        val tela = intent.getStringExtra("Tela")
        if (!tela?.isEmpty()!!){

            seleciona(text_pedidos,view_prdidos,icon_pedidos);
            Deseleciona_itens(text_home,text_clientes,text_lojas,text_protudo,view_home,
                view_clientes,view_lojas,view_produto,icon_home,icon_clientes,icon_lojas,icon_produtos)
            itensVisible()
            supportFragmentManager.
            beginTransaction()
                .replace(R.id.fragmentContainerViewPrincipal, fragementPedido).addToBackStack(null).commit()

        }else {
            supportFragmentManager.
            beginTransaction()
                .replace(R.id.fragmentContainerViewPrincipal, fragmentCargas).addToBackStack(null).commit()

        }


        val clickListenerhome = View.OnClickListener {
            iconeNotificacao.isVisible = true
            noticaocaoIcon.isVisible = true
            quatidadeNotificao.isVisible =true
            seleciona(text_home,view_home,icon_home);
            Deseleciona_itens(text_pedidos,text_clientes,text_lojas,text_protudo,view_prdidos,
                view_clientes,view_lojas,view_produto,icon_pedidos,icon_clientes,icon_lojas,icon_produtos)
             itensVisible()
            if(!fragmentCargas.isVisible){
                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction: FragmentTransaction =
                    fragmentManager.beginTransaction()


                fragmentTransaction.replace(R.id.fragmentContainerViewPrincipal, fragmentCargas, "h")
                fragmentTransaction.addToBackStack("h")
                fragmentTransaction.commit()
            }
        }

         icon_home.setOnClickListener(clickListenerhome)
         text_home.setOnClickListener(clickListenerhome)

        val clickListenerpedidos = View.OnClickListener {
            seleciona(text_pedidos,view_prdidos,icon_pedidos);
            Deseleciona_itens(text_home,text_clientes,text_lojas,text_protudo,view_home,
                view_clientes,view_lojas,view_produto,icon_home,icon_clientes,icon_lojas,icon_produtos)
            iconeNotificacao.isVisible = false
            noticaocaoIcon.isVisible = false
            quatidadeNotificao.isVisible =false
            if(!fragementPedido.isVisible){

                val fragmentManager: FragmentManager = supportFragmentManager
                val fragmentTransaction: FragmentTransaction =
                    fragmentManager.beginTransaction()


                fragmentTransaction.replace(R.id.fragmentContainerViewPrincipal, fragementPedido)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()

            }

        }

        icon_pedidos.setOnClickListener (clickListenerpedidos)
        text_pedidos.setOnClickListener (clickListenerpedidos)

        val clickListenerclientes = View.OnClickListener {
            val sharedPreferences =getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val feitacarga = sharedPreferences?.getBoolean("cargafeita", false)

            if(feitacarga == false){
                if (!FragmentCargas.alertvisible){
                    FragmentCargas.alertvisible = true
                    val alertas = Alertas()
                    alertas.alerta(supportFragmentManager,"Por favor realize a carga diaria","#B89A00",R.drawable.atencao,R.drawable.bordas_amerala_alert)
                }
            }else{
                seleciona(text_clientes,view_clientes,icon_clientes);
                Deseleciona_itens(text_home,text_pedidos,text_lojas,text_protudo,view_home,
                    view_prdidos,view_lojas,view_produto,icon_home,icon_pedidos,icon_lojas,icon_produtos)
                itensVisible()

                if(!fragmentClientes.isVisible){

                    val fragmentManager: FragmentManager = supportFragmentManager
                    val fragmentTransaction: FragmentTransaction =
                        fragmentManager.beginTransaction()

                    fragmentTransaction.replace(R.id.fragmentContainerViewPrincipal, fragmentClientes)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()

                }
            }
            iconeNotificacao.isVisible = false
            noticaocaoIcon.isVisible = false
            quatidadeNotificao.isVisible =false
        }

        viewcarrinho.setOnClickListener {
            startActivityForResult(Intent(this,ActPedido::class.java),3)
        }
        iconeNotificacao.setOnClickListener {
            MainScope().launch {
                val  intent = Intent(applicationContext,ActNotificacao::class.java)
                startActivity(intent)
            }
        }

        icon_clientes.setOnClickListener(clickListenerclientes)
        text_clientes.setOnClickListener  (clickListenerclientes)


        val clickListenerlojas = View.OnClickListener {
            fragmentClientes.onDestroy()



            val visivel =fragmentLojas.isVisible

            if(!visivel){
                val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val clienteSelecionado = sharedPreferences?.getString("ClienteSelecionado", null)
                if (clienteSelecionado.isNullOrBlank()){
                    val dialogErro = DialogErro()
                    dialogErro.Dialog(this,"Ops!","Selecione primeiro um cliente para acessar as lojas.","Ok",""){

                    }
                }else {

                    seleciona(text_lojas,view_lojas,icon_lojas);
                    Deseleciona_itens(text_home,text_pedidos,text_clientes,text_protudo,view_home,
                        view_prdidos,view_clientes,view_produto,icon_home,icon_pedidos,icon_clientes,icon_produtos)
                    itensVisible()
                    val fragmentManager: FragmentManager = supportFragmentManager
                    val fragmentTransaction: FragmentTransaction =
                        fragmentManager.beginTransaction()


                    fragmentTransaction.replace(R.id.fragmentContainerViewPrincipal, fragmentLojas, "h")
                    fragmentTransaction.addToBackStack("h")
                    fragmentTransaction.commit()
                }

            }
            iconeNotificacao.isVisible  = false
            noticaocaoIcon.isVisible   = false
            quatidadeNotificao.isVisible =false
        }

        icon_lojas.setOnClickListener(clickListenerlojas)
        text_lojas.setOnClickListener(clickListenerlojas)

        val clickListenerprodutos = View.OnClickListener {
            val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val lojaSelecionada = sharedPreferences?.getString("LojaSelecionada", null)
            if (lojaSelecionada.isNullOrBlank()){
                val dialogErro = DialogErro()
                dialogErro.Dialog(this,"Ops!","Selecione primeiro uma loja para acessar os produtos.","Ok",""){
                }
            }else {
                seleciona(text_protudo,view_produto,icon_produtos);
                Deseleciona_itens(text_home,text_pedidos,text_clientes,text_lojas,view_home,
                    view_prdidos,view_clientes,view_lojas,icon_home,icon_pedidos,icon_clientes,icon_lojas)
                itensInvisible()
                if(lojaTipo === 4){
                    if(!fragmentProtudosKit.isVisible){
                        val mudarFragment = MudarFragment()
                        mudarFragment.openFragmentProtudosKit(supportFragmentManager,R.id.fragmentContainerViewPrincipal,this, this)
                    }
                }else if (lojaTipo === 13){
                    val mudarFragment = MudarFragment()
                    mudarFragment.openFragmentProtudosAB(supportFragmentManager,this)
                }else{
                    if(!fragmentProtudos.isVisible){
                        val mudarFragment = MudarFragment()
                        mudarFragment.openFragmentProtudos(supportFragmentManager,R.id.fragmentContainerViewPrincipal,this, this)
                    }
                }

            }
        }

        icon_produtos.setOnClickListener (clickListenerprodutos)
        text_protudo.setOnClickListener(clickListenerprodutos)

        menuCima.setOnClickListener {

            menuRecycler.isVisible = true
        }

        val listaItensMenu = listOf(
            CustomSpinnerItem(
                "Portal",
                R.drawable.portal
            ),
           CustomSpinnerItem(
                "Adm",
                R.drawable.adm
            ),
            CustomSpinnerItem(
                "Visitas",
                R.drawable.visitaicon
            ),
            CustomSpinnerItem(
                "Sair",
                R.drawable.sair
            )
        )
        val adapterMenuCima     = AdapterMenuCima(listaItensMenu, baseContext,login, this)
        val linearLayoutManager    = LinearLayoutManager(baseContext)
        menuRecycler.layoutManager = linearLayoutManager
        menuRecycler.adapter       = adapterMenuCima
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                atualizaQtdCarrinhoNotificao()
           }
        }
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




    // botão voltar nativo
    override fun onBackPressed() {
        super.onBackPressed()
        if(fragmentCargas.isVisible){
            seleciona(text_home,view_home,icon_home);
            Deseleciona_itens(text_pedidos,text_clientes,text_lojas,text_protudo,view_prdidos,
                view_clientes,view_lojas,view_produto,icon_pedidos,icon_clientes,icon_lojas,icon_produtos)
        }else if(fragmentLojas.isVisible){
            seleciona(text_lojas,view_lojas,icon_lojas);
            Deseleciona_itens(text_home,text_pedidos,text_clientes,text_protudo,view_home,
                view_prdidos,view_clientes,view_produto,icon_home,icon_pedidos,icon_clientes,icon_produtos)
        }else if(fragmentClientes.isVisible){
            seleciona(text_clientes,view_clientes,icon_clientes);
            Deseleciona_itens(text_home,text_pedidos,text_lojas,text_protudo,view_home,
                view_prdidos,view_lojas,view_produto,icon_home,icon_pedidos,icon_lojas,icon_produtos)
        }else if(fragmentProtudos.isVisible){
            seleciona(text_protudo,view_produto,icon_produtos);
            Deseleciona_itens(text_home,text_pedidos,text_clientes,text_lojas,view_home,
                view_prdidos,view_clientes,view_lojas,icon_home,icon_pedidos,icon_clientes,icon_lojas)
        }


    }


    override fun trocacor() {
        when (troca){
            visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Enuns.TrocaItemSelecionado.home ->{
                seleciona(text_home,view_home,icon_home);
                Deseleciona_itens(text_pedidos,text_clientes,text_lojas,text_protudo,view_prdidos,
                    view_clientes,view_lojas,view_produto,icon_pedidos,icon_clientes,icon_lojas,icon_produtos)
            }
            visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Enuns.TrocaItemSelecionado.pedidos ->{
                seleciona(text_pedidos,view_prdidos,icon_pedidos);
                Deseleciona_itens(text_home,text_clientes,text_lojas,text_protudo,view_home,
                    view_clientes,view_lojas,view_produto,icon_home,icon_clientes,icon_lojas,icon_produtos)
            }
            visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Enuns.TrocaItemSelecionado.clientes ->{
                seleciona(text_clientes,view_clientes,icon_clientes);
                Deseleciona_itens(text_home,text_pedidos,text_lojas,text_protudo,view_home,
                    view_prdidos,view_lojas,view_produto,icon_home,icon_pedidos,icon_lojas,icon_produtos)
            }
            visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Enuns.TrocaItemSelecionado.lojas ->{

                seleciona(text_lojas,view_lojas,icon_lojas);
                Deseleciona_itens(text_home,text_pedidos,text_clientes,text_protudo,view_home,
                    view_prdidos,view_clientes,view_produto,icon_home,icon_pedidos,icon_clientes,icon_produtos)
            }
            visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Enuns.TrocaItemSelecionado.Prodtudos ->{
                seleciona(text_protudo,view_produto,icon_produtos);
                Deseleciona_itens(text_home,text_pedidos,text_clientes,text_lojas,view_home,
                    view_prdidos,view_clientes,view_lojas,icon_home,icon_pedidos,icon_clientes,icon_lojas)
            }
        }
    }
    fun itensVisible(){
        viewcarrinho.isVisible = true
        qtdNotificacoes.isVisible = true
        viewnotification.isVisible = true
        iconeNotificacao.isVisible = true
        noticaocaoIcon.isVisible = true
        quatidadeNotificao.isVisible =true
    }

    fun itensInvisible(){
        viewcarrinho.isVisible = false
        qtdNotificacoes.isVisible = false
        viewnotification.isVisible = false
        iconeNotificacao.isVisible = false
        noticaocaoIcon.isVisible = false
        quatidadeNotificao.isVisible =false

    }

    override fun carrinhoVisivel() {
        itensInvisible()
    }

    override fun atualizaCarrinho() {
        atualizaQtdCarrinhoNotificao()
    }
    fun  atualizaQtdCarrinhoNotificao(){
        CoroutineScope(Dispatchers.IO).launch {
            val qtdCarrinho = launch {
                val queryQuantidadeNoCarrinho = "SELECT cliente_id, loja_id, COUNT (*) AS total " +
                        "FROM TB_Carrinho " +
                        "GROUP BY cliente_id, loja_id"

                val  carrinhoDAO = CarrinhoDAO(context)
                val  countCarrinho = carrinhoDAO.countarItenscarrinho(queryQuantidadeNoCarrinho)
                MainScope().launch {
                    if(countCarrinho > 9){
                        qtdNotification.text = "9+"
                    }else{
                        qtdNotification.text = countCarrinho.toString()
                    }
                }

            }

            val qtdNotificacao = launch {
                val notidicaoDao =  NotificacaoDAO(context)
                val countNotificaon = notidicaoDao.countarItensNotificao()
                MainScope().launch {
                    if (countNotificaon > 9){
                        quatidadeNotificao.text = "9+"
                    }else if(countNotificaon == 0){
                        quatidadeNotificao.text = "-"
                        noticaocaoIcon.isVisible = false
                        quatidadeNotificao.isVisible =false

                    }else{
                        quatidadeNotificao.text = countNotificaon.toString()
                    }
                }
            }
            qtdCarrinho.join()
            qtdNotificacao.join()
        }
    }



    fun trocacorItensAposCarga(img: ImageView,texviw:TextView){
        trocar_cor_iten(img,img.drawable,"#737880")
        texviw.setTextColor(Color.parseColor("#737880"))


    }
    private fun trocar_cor_iten(icon:ImageView, drawable: Drawable, cor:String){
        val wrappedDrawable = DrawableCompat.wrap(drawable.mutate())
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(cor))
        icon.setImageDrawable(wrappedDrawable)

    }

    override fun onActionDone() {
        trocacorItensAposCarga(icon_clientes,text_clientes)
        trocacorItensAposCarga(icon_lojas,text_lojas)
        trocacorItensAposCarga(icon_produtos,text_protudo)

    }

    override fun mostraLoad(load: Boolean, mensagem: String) {
         if(load){
              viewCarregando.isVisible = true
             textCarregando.isVisible = true
             carregandoProgress.isVisible = true
             textCarregando.text = mensagem
         }else{
             viewCarregando.isVisible = false
             textCarregando.isVisible = false
             carregandoProgress.isVisible = false
             textCarregando.text = mensagem
         }

    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null && menuRecycler.isVisible) {

            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (menuRecycler.isTouchInsideView(x, y)) {
                return super.dispatchTouchEvent(ev)
            } else {
                menuRecycler.isVisible = false
                return true
            }
        }
        return super.dispatchTouchEvent(ev)

    }
    fun View.isTouchInsideView(x: Int, y: Int): Boolean {
        val location = IntArray(2)
        this.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]
        return (x >= viewX && x <= viewX + this.width && y >= viewY && y <= viewY + this.height)
    }
}

