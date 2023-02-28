package visaogrupo.com.br.modulo_visitacao.Views.Atividades.Atividade_Cadastro_Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.Atividades_Cargas_Lojas_Protudos.Cargas.Act_Cargas
import visaogrupo.com.br.modulo_visitacao.Views.Atividades.Dialogs.Dialog_erro
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.Login_Cadastro.Task_Login
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.Ondismiss
import visaogrupo.com.br.modulo_visitacao.Views.Models.Enuns.Enuns_Cadastro.Login_Erro
import visaogrupo.com.br.modulo_visitacao.Views.Models.Ultis.Verifica_Internet

class Act_Kotlin:  AppCompatActivity() , Ondismiss{


    private var email: EditText? = null
    private  var senha:EditText? = null
    private var carregandoprogress: ProgressBar? = null
    private var entrar: Button? = null
    val login_erro = Login_Erro.Erro

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        email = findViewById<EditText>(R.id.email_login)
        senha = findViewById<EditText>(R.id.senha)
        entrar = findViewById<Button>(R.id.entrar)
        carregandoprogress = findViewById<ProgressBar>(R.id.progressBar_carregando_login)



        entrar!!.setOnClickListener {
            entrar!!.text = ""
            val captuere_email = email!!.text.toString()
            val capture_senha =senha!!.text.toString()
            carregandoprogress!!.visibility = View.VISIBLE
            var isconectado = Verifica_Internet()

            var conect_boll = isconectado.isOnline(baseContext)
            if(conect_boll){
                val task_login = Task_Login()
                try {
                    task_login.Request_login(captuere_email, capture_senha, "", this)
                } catch (e: Exception) {

                }
            }else{
                var dialogErro = Dialog_erro()
                dialogErro.Dialog(this,"Erro na conexão!","Por favor, Verifique se há acesso a internet","OK","")
                carregandoprogress!!.visibility = View.GONE
                entrar!!.text = "Entrar"

            }

        }
    }

    override fun ondismiss(enum:Login_Erro) {
        when(enum){
            Login_Erro.Erro ->{
                val dialog_erro = Dialog_erro()
                dialog_erro.Dialog(this, "Atenção", "Algo deu errado, verifiue os campos e tentente novamente", "OK", "Cancelar")
                carregandoprogress!!.visibility = View.GONE
                entrar!!.text = "Entrar"
            }

            Login_Erro.Sucesso ->{

                carregandoprogress!!.visibility = View.GONE
                entrar!!.text = "Entrar"
                var intent = Intent(baseContext,Act_Cargas::class.java)
                startActivity(intent)
            }
        }
    }
}
