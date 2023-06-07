package visaogrupo.com.br.modulo_visitacao.Views.Fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import kotlinx.android.synthetic.main.fragment_clientes.view.*
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Adpters.ClientesAdpter
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.TrocarcorItem
import visaogrupo.com.br.modulo_visitacao.Views.Models.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.LojaXCliente
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.ClientesDAO
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.DataBaseHelber
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentClientesBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentClientes (trocarcorItem: TrocarcorItem) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var  totaldeclientes = 0
    private var loadedItems = 0
    val  trocarcorItem = trocarcorItem

    private  lateinit var  binding:FragmentClientesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentClientesBinding.inflate(layoutInflater)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root
        val carregando = view.findViewById<ImageView>(R.id.carregandoCliente)

        val animator = ObjectAnimator.ofFloat(carregando, "rotation", 0f, 360f)
        animator.duration = 2000
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.start()

        val clientes = ClientesDAO(requireContext())
        // Pega os 30 primeiros Clientes


        val queryListaClientes ="SELECT * FROM TB_clientes ORDER BY 1 LIMIT 30"
        totaldeclientes = clientes.countar(requireContext())
        view.quatidadeClienetes.text = totaldeclientes.toString() + " Clientes"
        val listaclientes = clientes.listar(requireContext(),queryListaClientes)
        val adapterCliente = ClientesAdpter(listaclientes,R.id.fragmentContainerViewPrincipal,getParentFragmentManager(), trocarcorItem )
        val layoutManegerCliente:LayoutManager = LinearLayoutManager(context)

        view.recyClientes.layoutManager = layoutManegerCliente
        view.recyClientes .adapter = adapterCliente

        view.recyClientes.addOnScrollListener(object :OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Verifique se o usuário está próximo ao final da lista
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 10) {
                    // Carregue mais 30 itens do banco de dados
                    loadMoreItems(30,adapterCliente,recyclerView)
                }
            }
        })


        //clicks
        val dbbusca = DataBaseHelber(requireContext())
        view.buscaClientesedt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val query = "SELECT * FROM TB_clientes WHERE cnpj LIKE '%${p0.toString()}%'"
                val cursor = dbbusca.readableDatabase.rawQuery(query,null)
                val  lojasxclienets = mutableListOf<LojaXCliente>()
                val clientesList = mutableListOf<Clientes>()

                while (cursor.moveToNext()){
                    if(cursor == null){

                    }else{
                        val Empresa_id:Int = cursor.getInt(0)
                        val cnpj =  cursor.getString(1)
                        val RazaoSocial =   cursor.getString(2)
                        val Fantasia =  cursor.getString(3)
                        val Endereco =   cursor.getString(4)
                        val Numero =  cursor.getString(5)
                        val Complemento =    cursor.getString(6)
                        val Bairro =    cursor.getString(7)
                        val Cidade =   cursor.getString(8)
                        val UF =    cursor.getString(9)
                        val Cep =  cursor.getString(10)
                        val UltimoPedido =  cursor.getString(13)
                        val VendaDireta =  cursor.getString(14)
                        val Associativismo =   cursor.getString(15)
                        val Telefone = cursor.getString(16)
                        val Email =  cursor.getString(17)
                        val DuplicataVencida =  cursor.getInt(20)


                        val clientes = Clientes(
                            "",Associativismo,Bairro,"",Cep,cnpj,
                            "",0,
                            0,Cidade,0,"",Complemento,
                            0, false,
                            "","","",
                            DuplicataVencida,Email,Empresa_id,
                            Endereco,false,
                            Fantasia,0,
                            0,"",""
                            ,1,lojasxclienets,"",
                            "","","",Numero,RazaoSocial,Telefone,"","","",UF,UltimoPedido,VendaDireta)
                        clientesList.add(clientes)

                    }
                }
                adapterCliente.listaClientes = clientesList
                adapterCliente. notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        return view
    }

    private fun loadMoreItems(count: Int,adpter: ClientesAdpter,recyclerview:RecyclerView) {
        val remainingItems = totaldeclientes - loadedItems // Calcula o número de itens restantes
        val itemsToLoad = minOf(count, remainingItems) // Verifica se há menos de 30 itens restantes
        val clientes = ClientesDAO(requireContext())
        // Pega os 30 primeiros Clientes
        val queryListaClientes ="SELECT * FROM TB_clientes ORDER BY 1 LIMIT $count OFFSET $loadedItems"

        val listaclientes = clientes.listar(requireContext(),queryListaClientes)

        adpter.addItems(listaclientes) // Adiciona os novos itens ao adaptador
        loadedItems += listaclientes.size // Atualiza a contagem de itens carregados

        // Verifica se todos os itens foram carregados
        if (loadedItems >= totaldeclientes) {
            // Todos os itens foram carregados, remova o listener de scroll
            recyclerview.clearOnScrollListeners()
        }
    }
}

