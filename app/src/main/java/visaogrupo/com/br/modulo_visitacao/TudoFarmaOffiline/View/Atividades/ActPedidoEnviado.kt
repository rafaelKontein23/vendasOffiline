package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.ProdutosFinalizados
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdpterProdutosPedidos

class ActPedidoEnviado : AppCompatActivity() {
    lateinit var  valorTotal :TextView
    lateinit var  data :TextView
    lateinit var  chave:TextView
    lateinit var  recyProdutosPedidos :RecyclerView
    lateinit var containerInfoPedidos:ConstraintLayout
    lateinit var containerCopiar:ConstraintLayout
    lateinit var tituloPedido:TextView
    lateinit var totalPedido :TextView
    lateinit var voltarPedido:ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_pedido_enviado)

        valorTotal = findViewById(R.id.Total)
        data = findViewById(R.id.data)
        chave = findViewById(R.id.chave)
        recyProdutosPedidos = findViewById(R.id.recyProdutos)
        containerInfoPedidos = findViewById(R.id.containerInfoPedidos)
        containerCopiar = findViewById(R.id.containerCopiar)
        tituloPedido = findViewById(R.id.tituloPedido)
        totalPedido= findViewById(R.id.totalPedido)
        voltarPedido =findViewById(R.id.voltarPedido)

        voltarPedido.setOnClickListener {
            onBackPressed()
        }

        val pedido = intent.getSerializableExtra("PedidoClicado") as? PedidoFinalizado

        val pedidosFinalizadosDAO = PedidosFinalizadosDAO(applicationContext)
        var listaProdutos  = mutableListOf<ProdutosFinalizados>()
        val valorTotalPedido = pedidosFinalizadosDAO.somarTotalPedido(pedido!!.pedidoID)
        if (pedido != null) {
            listaProdutos =  pedidosFinalizadosDAO.listarPedidosProdutos(pedido.pedidoID)
        }
        containerInfoPedidos.isVisible = pedido?.pedidoEnviado == true
        chave.isVisible = pedido?.pedidoEnviado == true
        containerCopiar.isVisible = pedido?.pedidoEnviado == true


        if (pedido?.pedidoEnviado == true){

            chave.text =pedido?.chave
            val valorFormatado = String.format("%.2f", valorTotalPedido)
            valorTotal.text = "R$ "+ valorFormatado.replace(".",",")
            data.text = pedido?.dataPedido
            chave.setOnClickListener{
                val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val textoParaCopiar = chave.text.toString()
                val clipData = ClipData.newPlainText("label", textoParaCopiar)
                clipboardManager.setPrimaryClip(clipData)

                // Exibe uma mensagem de confirmação
                Toast.makeText(this, "Chave copiado para a área de transferência", Toast.LENGTH_SHORT).show()
            }
        }else{
            tituloPedido.setText("Detalhes de produtos")
            totalPedido.isVisible = true
            val valorFormatado = String.format("%.2f", valorTotalPedido)
            totalPedido.text = "R$ ${valorFormatado.replace(".",",")}"

        }
        val adpterProdutosPedidos = AdpterProdutosPedidos(listaProdutos,this)
        val linearLayout = LinearLayoutManager(this)
        recyProdutosPedidos.layoutManager = linearLayout
        recyProdutosPedidos.adapter = adpterProdutosPedidos



    }
}