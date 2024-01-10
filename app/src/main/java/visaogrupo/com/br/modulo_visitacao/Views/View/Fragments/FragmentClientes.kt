package visaogrupo.com.br.modulo_visitacao.Views.View.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import kotlinx.android.synthetic.main.celulaclientes.view.documentoVencido
import kotlinx.android.synthetic.main.fragment_clientes.progressBar
import kotlinx.android.synthetic.main.fragment_clientes.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.AtualizaCarrinho
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.TrocarcorItem
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Interfaces.Ondimiss.carrinhoVisible
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.Objetos.LojaXCliente
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.ClientesDAO
import visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase.DataBaseHelber
import visaogrupo.com.br.modulo_visitacao.Views.View.Adpters.ClientesAdpter
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentClientesBinding


class FragmentClientes (trocarcorItem: TrocarcorItem, carrinhoVisible: carrinhoVisible, atualizaCarrinho: AtualizaCarrinho) : Fragment() {

    private var  totaldeclientes = 0
    private var loadedItems = 0
    val  trocarcorItem = trocarcorItem
    val carrinhoVisible = carrinhoVisible
    private  lateinit var  binding:FragmentClientesBinding
    lateinit var adapterCliente:ClientesAdpter
    val  atualizaCarrinho = atualizaCarrinho
    var listaclientes :MutableList<Clientes> = mutableListOf()
    var listaclientesFiltroButton:MutableList<Clientes> = mutableListOf()
    var filtrolista = false
    val listaClientesFiltro:MutableList<Clientes> = mutableListOf()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentClientesBinding.inflate(layoutInflater)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = binding.root

        val listaFiltroVazia =listaclientesFiltroButton != null

        val scrollListener = ViewTreeObserver.OnScrollChangedListener {

            if (filtrolista){
                if (binding.nestedScrollView3.getChildAt(binding.nestedScrollView3.childCount - 1).bottom
                    <= (binding.nestedScrollView3.height + binding.nestedScrollView3.scrollY)
                ) {
                    val linearLayoutManager = view.recyClientes.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()

                    val totalItemCount = adapterCliente.itemCount

                    if (lastVisibleItemPosition >= 0 && totalItemCount >= 5) {
                        if (lastVisibleItemPosition >= totalItemCount - 1) {
                            val currentSize = adapterCliente.itemCount
                            val remainingItems = listaClientesFiltro.size - currentSize

                            if (remainingItems > 0) {
                                val nextItems = if (remainingItems >= 5) {
                                    listaClientesFiltro.subList(currentSize, currentSize + 5).toList()
                                } else {
                                    listaClientesFiltro.subList(currentSize, currentSize + remainingItems).toList()
                                }
                                adapterCliente.addItems(nextItems)
                            }
                        }
                    }
                }

            }else{
                if (binding.nestedScrollView3.getChildAt(binding.nestedScrollView3.getChildCount() - 1).getBottom()
                    <= binding.nestedScrollView3.getHeight() + binding.nestedScrollView3.getScrollY()
                ) {

                    Log.d("ScrollView", "Chegou ao final")
                    val capBusca = binding.buscaClientesedt.text
                    if (capBusca.toString().isEmpty()){
                        CoroutineScope(Dispatchers.IO).launch {
                            val layoutManager = view.recyClientes.layoutManager as LinearLayoutManager
                            val visibleItemCount = layoutManager.childCount
                            val totalItemCount = layoutManager.itemCount
                            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                            // Verifique se o usuário está próximo ao final da lista
                            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount - 20 && totaldeclientes != listaclientes.size) {
                                // Carregue mais 30 itens do banco de dados
                                loadMoreItems(30,adapterCliente)
                            }

                        }

                    }

                }
            }

        }


        view.filtro_positivo.setOnClickListener{
            if (listaFiltroVazia){
                var listaClientesFiltro:MutableList<Clientes> = mutableListOf()

                for ( i in listaclientesFiltroButton){
                    if (i.Compra == 1){
                        listaClientesFiltro.add(i)
                    }

                }
                if (listaClientesFiltro.isEmpty()){
                    binding.nestedScrollView3.viewTreeObserver.addOnScrollChangedListener(scrollListener)

                    Toast.makeText(context,"Não existem clientes para o filtro selecionado",Toast.LENGTH_SHORT).show()
                    adapterCliente.listaClientes = listaclientesFiltroButton
                    adapterCliente.carregando = false
                    filtrolista = true
                    adapterCliente.notifyDataSetChanged()
                }else{
                    binding.nestedScrollView3.viewTreeObserver.removeOnScrollChangedListener(scrollListener)
                    binding.progressBar.isVisible = false
                    adapterCliente.listaClientes = listaClientesFiltro
                    adapterCliente.carregando = false
                    filtrolista = false
                    adapterCliente.notifyDataSetChanged()
                    Toast.makeText(context,"Filtro aplicado",Toast.LENGTH_SHORT).show()
                }

            }


        }

        view.filtro_naopositivados.setOnClickListener{
            if (listaFiltroVazia){
                listaClientesFiltro.clear()

                binding.nestedScrollView3.viewTreeObserver.addOnScrollChangedListener(scrollListener)

                for ( i in listaclientesFiltroButton){
                    if (i.Compra == 0){
                        listaClientesFiltro.add(i)
                        Log.d("tamho lista ",listaClientesFiltro.size.toString())
                    }

                }
                Log.d("tamho lista ",listaClientesFiltro.size.toString())

                if (listaClientesFiltro.isEmpty()){
                    Toast.makeText(context,"Não existem clientes para o filtro selecionado",Toast.LENGTH_SHORT).show()
                    adapterCliente.listaClientes = listaclientesFiltroButton
                    adapterCliente.carregando = false
                    filtrolista = false
                    adapterCliente.notifyDataSetChanged()
                }else{
                    binding.progressBar.isVisible = false
                    adapterCliente.listaClientes = listaClientesFiltro.take(5).toMutableList()
                    adapterCliente.carregando = false
                    filtrolista = true
                    adapterCliente.notifyDataSetChanged()
                    Toast.makeText(context,"Filtro aplicado",Toast.LENGTH_SHORT).show()
                }

            }
        }

       view.filtro_doc_vencidos.setOnClickListener {
           if (listaFiltroVazia){
               listaClientesFiltro.clear()

               binding.nestedScrollView3.viewTreeObserver.addOnScrollChangedListener(scrollListener)

               for ( i in listaclientesFiltroButton){
                   if (i.ExibeAlerta.equals("true")){
                       listaClientesFiltro.add(i)
                       Log.d("tamho lista ",listaClientesFiltro.size.toString())
                   }

               }
               Log.d("tamho lista ",listaClientesFiltro.size.toString())

               if (listaClientesFiltro.isEmpty()){
                   Toast.makeText(context,"Não existem clientes para o filtro selecionado",Toast.LENGTH_SHORT).show()
                   adapterCliente.listaClientes = listaclientesFiltroButton
                   adapterCliente.carregando = false
                   filtrolista = false
                   adapterCliente.notifyDataSetChanged()
               }else{
                   binding.progressBar.isVisible = false
                   adapterCliente.listaClientes = listaClientesFiltro.take(5).toMutableList()
                   adapterCliente.carregando = false
                   filtrolista = true
                   adapterCliente.notifyDataSetChanged()
                   Toast.makeText(context,"Filtro aplicado",Toast.LENGTH_SHORT).show()
               }

           }
       }

        view.filtro_duplicadas.setOnClickListener{
            if (listaFiltroVazia){

               listaClientesFiltro.clear()

                for (i in listaclientesFiltroButton){
                    if (i.DuplicataVencida == 1){
                        listaClientesFiltro.add(i)
                    }

                }
                if (listaClientesFiltro.isEmpty()){
                    Toast.makeText(context,"Não existem clientes para o filtro selecionado",Toast.LENGTH_SHORT).show()
                    adapterCliente.listaClientes = listaclientesFiltroButton
                    adapterCliente.carregando = false
                    filtrolista = false
                    adapterCliente.notifyDataSetChanged()
                }else{
                    binding.progressBar.isVisible = false
                    adapterCliente.listaClientes = listaClientesFiltro.take(5).toMutableList()
                    adapterCliente.carregando = false
                    filtrolista = true
                    adapterCliente.notifyDataSetChanged()
                    Toast.makeText(context,"Filtro aplicado",Toast.LENGTH_SHORT).show()
                }

            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            adapterCliente = ClientesAdpter(listaclientes,R.id.fragmentContainerViewPrincipal,getParentFragmentManager(), trocarcorItem,carrinhoVisible, atualizaCarrinho )
            val layoutManegerCliente:LayoutManager = LinearLayoutManager(context)

            binding.recyClientes.layoutManager = layoutManegerCliente
            binding.recyClientes .adapter = adapterCliente
            loadedItems = 0

        }

        CoroutineScope(Dispatchers.IO).launch {

           val clientes = ClientesDAO(requireContext())
           val queryListaClientes ="SELECT * FROM TB_clientes ORDER BY 1 LIMIT 5"
           val queryListaClientesToatal ="SELECT * FROM TB_clientes "
           listaclientes = clientes.listar(requireContext(),queryListaClientes)
           listaclientesFiltroButton = clientes.listar(requireContext(),queryListaClientesToatal)
          CoroutineScope(Dispatchers.Main).launch{

                adapterCliente.listaClientes = listaclientes
                adapterCliente.carregando = false
                Log.d("fafaff",listaclientesFiltroButton.size.toString())
                adapterCliente.notifyDataSetChanged()
            }

            launch {
                totaldeclientes = clientes.countar(requireContext())
                MainScope().launch {
                    binding.quatidadeClienetes.text = totaldeclientes.toString() + " Clientes"
                }
            }
        }


        binding.nestedScrollView3.getViewTreeObserver().addOnScrollChangedListener(scrollListener)






        binding.buscaClientesedt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().isEmpty()){
                        CoroutineScope(Dispatchers.IO).launch {
                            val clientesDAO = ClientesDAO(requireContext())
                            val listafilter = clientesDAO.buscarClientes(p0.toString(),requireContext())
                            if(listafilter.size == 0){
                                CoroutineScope(Dispatchers.Main).launch {
                                    binding.textovazio.isVisible = true
                                    binding.recyClientes.isVisible = false
                                    binding.progressBar.isVisible = false
                                }

                            }else{
                                CoroutineScope(Dispatchers.Main).launch {
                                    binding.textovazio.isVisible = false
                                    binding.recyClientes.isVisible = true
                                    binding.progressBar.isVisible = false
                                    adapterCliente.listaClientes = listafilter
                                    adapterCliente.notifyDataSetChanged()
                                }
                            }


                        }
                }else{
                    CoroutineScope(Dispatchers.Main).launch {
                        adapterCliente.listaClientes = listaclientes
                        adapterCliente.notifyDataSetChanged()
                    }


                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })


        return view
    }

    private fun loadMoreItems(count: Int,adpter: ClientesAdpter) {

        if (loadedItems >= totaldeclientes) {
            CoroutineScope(Dispatchers.Main).launch{
                binding.progressBar.isVisible = false
            }

        }else{
            val clientes = ClientesDAO(requireContext())
            val queryListaClientes ="SELECT * FROM TB_clientes ORDER BY 1 LIMIT $count OFFSET $loadedItems"
            val listaclientes = clientes.listar(requireContext(),queryListaClientes)
            CoroutineScope(Dispatchers.Main).launch{
                binding.progressBar.isVisible = true
                adpter.addItems(listaclientes)
            }
            loadedItems += listaclientes.size


        }
    }
}