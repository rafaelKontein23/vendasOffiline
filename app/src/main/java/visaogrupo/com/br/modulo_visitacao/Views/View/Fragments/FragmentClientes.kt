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
import kotlinx.coroutines.withContext
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

        binding.limparfiltro.setOnClickListener {
            carregaInfos()
        }

        carregaInfos()

        view.filtro_positivo.setOnClickListener{
            if (listaFiltroVazia){
                var listaClientesFiltro:MutableList<Clientes> = mutableListOf()

                for ( i in listaclientesFiltroButton){
                    if (i.Compra == 1){
                        listaClientesFiltro.add(i)
                    }

                }
                if (listaClientesFiltro.isEmpty()){


                    Toast.makeText(context,"Não existem clientes para o filtro selecionado",Toast.LENGTH_SHORT).show()
                    adapterCliente.listaClientes = listaclientesFiltroButton
                    adapterCliente.carregando = false
                    filtrolista = true
                    adapterCliente.notifyDataSetChanged()
                }else{
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
    fun carregaInfos(){
        CoroutineScope(Dispatchers.Main).launch {
            binding.progressBar.isVisible = true

            val layoutManager: LayoutManager = LinearLayoutManager(context)
            binding.recyClientes.layoutManager = layoutManager

            adapterCliente = ClientesAdpter(listaclientes, R.id.fragmentContainerViewPrincipal, getParentFragmentManager(), trocarcorItem, carrinhoVisible, atualizaCarrinho)
            binding.recyClientes.adapter = adapterCliente

            val initialClientes = withContext(Dispatchers.Default) {
                val clientesDAO = ClientesDAO(requireContext())
                clientesDAO.listar(requireContext(), "SELECT * FROM TB_clientes")
            }
            binding.quatidadeClienetes.text = initialClientes.size.toString() + " Clientes"
            adapterCliente.listaClientes = initialClientes
            adapterCliente.carregando = false
            adapterCliente.notifyDataSetChanged()
            binding.progressBar.isVisible = false
            binding.recyClientes.isVisible  = true

        }
    }
}