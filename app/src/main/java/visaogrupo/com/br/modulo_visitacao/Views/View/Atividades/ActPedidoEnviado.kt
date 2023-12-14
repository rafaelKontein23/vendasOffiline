package visaogrupo.com.br.modulo_visitacao.Views.View.Atividades

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.PedidoFinalizado
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.ProdutosFinalizados
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.PedidosFinalizadosDAO
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.AdpterProdutosPedidos

class ActPedidoEnviado : AppCompatActivity() {
    lateinit var  valorTotal :TextView
    lateinit var  data :TextView
    lateinit var  chave:TextView
    lateinit var  recyProdutosPedidos :RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_act_pedido_enviado)

        valorTotal = findViewById(R.id.Total)
        data = findViewById(R.id.data)
        chave = findViewById(R.id.chave)
        recyProdutosPedidos = findViewById(R.id.recyProdutos)

        val pedido = intent.getSerializableExtra("PedidoClicado") as? PedidoFinalizado

        val pedidosFinalizadosDAO = PedidosFinalizadosDAO(applicationContext)
        var listaProdutos  = mutableListOf<ProdutosFinalizados>()
        if (pedido != null) {
            listaProdutos =  pedidosFinalizadosDAO.listarPedidosProdutos(pedido.pedidoID)
        }
        val adpterProdutosPedidos = AdpterProdutosPedidos(listaProdutos,this)
        val linearLayout = LinearLayoutManager(this)
        recyProdutosPedidos.layoutManager = linearLayout
        recyProdutosPedidos.adapter = adpterProdutosPedidos


        chave.text =pedido?.chave
        valorTotal.text = "R$ "+ pedido?.valorTotal.toString()
        data.text = pedido?.dataPedido
        chave.setOnClickListener{
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val textoParaCopiar = chave.text.toString()
            val clipData = ClipData.newPlainText("label", textoParaCopiar)
            clipboardManager.setPrimaryClip(clipData)

            // Exibe uma mensagem de confirmação
            Toast.makeText(this, "Chave copiado para a área de transferência", Toast.LENGTH_SHORT).show()
        }

    }
}