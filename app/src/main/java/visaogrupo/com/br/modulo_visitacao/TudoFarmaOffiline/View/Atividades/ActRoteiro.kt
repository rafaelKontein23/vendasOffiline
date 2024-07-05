package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.TudoFarmaOffiline.databinding.ActivityActRoteiroBinding
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.IniciaAticidadePrincipal
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.ClientesDAO
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.AdaterRoteiro
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Adpters.ClientesAdpter

class ActRoteiro : AppCompatActivity() {

    lateinit var bingind:ActivityActRoteiroBinding
    lateinit var adaterRoteiro:AdaterRoteiro
    var listaclientes:MutableList<Clientes> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bingind = ActivityActRoteiroBinding.inflate(layoutInflater)
        setContentView(bingind.root)

        carregaInfos()
        bingind.calendarioVisitas.setOnClickListener {
            val intent =  Intent(baseContext,ActCalendarioRoteiro::class.java)
            startActivity(intent)
        }

        bingind.buscaClientesRoteiro.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().isEmpty()){
                    CoroutineScope(Dispatchers.IO).launch {
                        val clientesDAO = ClientesDAO(baseContext)
                        val listafilter = clientesDAO.buscarClientes(p0.toString(),baseContext)
                        if(listafilter.size == 0){
                            CoroutineScope(Dispatchers.Main).launch {
                                bingind.recyclerRoteiro.isVisible = false
                                bingind.progressBarRoteiro.isVisible = false
                            }

                        }else{
                            CoroutineScope(Dispatchers.Main).launch {
                                bingind.recyclerRoteiro.isVisible = true
                                bingind.progressBarRoteiro.isVisible = false
                                adaterRoteiro.listaCliente = listafilter
                                adaterRoteiro.notifyDataSetChanged()
                            }
                        }


                    }
                }else{
                    CoroutineScope(Dispatchers.Main).launch {
                      //   adaterRoteiro.listaCliente = listaclientes
                       // adaterRoteiro.notifyDataSetChanged()
                    }


                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }

    fun carregaInfos(){
        CoroutineScope(Dispatchers.Main).launch {
            bingind.progressBarRoteiro.isVisible = true

            val initialClientes = withContext(Dispatchers.Default) {
                val clientesDAO = ClientesDAO(this@ActRoteiro)
                clientesDAO.listar(this@ActRoteiro, "SELECT * FROM TB_clientes")
            }

            bingind.quatidadeClienetes.text = initialClientes.size.toString() + " Clientes"
            listaclientes.addAll(initialClientes)
            adaterRoteiro = AdaterRoteiro(initialClientes, this@ActRoteiro)
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@ActRoteiro)
            bingind.recyclerRoteiro.layoutManager = layoutManager
            bingind.recyclerRoteiro.adapter = adaterRoteiro
            bingind.progressBarRoteiro.isVisible = false
            bingind.recyclerRoteiro.isVisible  = true

        }
    }

}