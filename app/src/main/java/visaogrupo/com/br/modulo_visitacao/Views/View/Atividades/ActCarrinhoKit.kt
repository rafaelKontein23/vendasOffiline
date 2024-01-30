package visaogrupo.com.br.modulo_visitacao.Views.View.Atividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.databinding.ActivityActCarrinhoKitBinding
import visaogrupo.com.br.modulo_visitacao.databinding.ActivityActProdutosAtualizarBinding
import visaogrupo.com.br.modulo_visitacao.databinding.FragmentCargasBinding

class ActCarrinhoKit : AppCompatActivity() {
    private  lateinit var binding : ActivityActCarrinhoKitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActCarrinhoKitBinding.inflate(layoutInflater)

        
    }
}