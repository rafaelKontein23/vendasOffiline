package visaogrupo.com.br.modulo_visitacao.Views.Atividades

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import visaogrupo.com.br.modulo_visitacao.R
import visaogrupo.com.br.modulo_visitacao.Views.Dialogs.DialogErro
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Task.task.Task_Login
import visaogrupo.com.br.modulo_visitacao.Views.Interfaces.Ondimiss.Ondismiss
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Enuns.Login_Erro
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.Funcao_erro
import visaogrupo.com.br.modulo_visitacao.Views.Controler.Ultis.Verifica_Internet
import visaogrupo.com.br.modulo_visitacao.Views.dataBase.DataBaseHelber

class Act_Login:  AppCompatActivity() , Ondismiss{


    private var email: EditText? = null
    private  var senha:EditText? = null
    private var carregandoprogress: ProgressBar? = null
    private var entrar: Button? = null
    val login_erro = Login_Erro.Erro
    companion object {
        val  versao = "3.0.0"
    }


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_login)

        email = findViewById(R.id.email_login)
        senha = findViewById(R.id.senha)
        entrar = findViewById(R.id.entrar)
        carregandoprogress = findViewById(R.id.progressBar_carregando_login)
        // Cria o banco de dados Pela primeira vez
        DataBaseHelber(this).writableDatabase

        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean("cargafeita", true)
        editor?.apply()


        entrar!!.setOnClickListener {

            entrar!!.text = ""
            val captuere_email = email!!.text.toString()
            val capture_senha =senha!!.text.toString()
            carregandoprogress!!.visibility = View.VISIBLE
            var isconectado = Verifica_Internet()

            var conect_boll = isconectado.isOnline(baseContext)
            if(conect_boll){
                val task_login = Task_Login()
                // Request de login
                try {
                    task_login.Request_login(captuere_email, capture_senha, "", this,this)
                } catch (e: Exception) {

                }
            }else{
                var dialogErro = DialogErro()
                dialogErro.Dialog(this,"Erro na conexão!","Por favor, Verifique se há acesso a internet","OK","",
                    Funcao_erro()
                )
                carregandoprogress!!.visibility = View.GONE
                entrar!!.text = "Entrar"

            }

        }
    }

    override fun ondismiss(enum: Login_Erro) {
        // Para verificar se os dados estão corretos
        when(enum){
            Login_Erro.Erro ->{
                val dialog_erro = DialogErro()
                dialog_erro.Dialog(this, "Atenção", "Algo deu errado, verifiue os campos e tentente novamente", "OK", "Cancelar",
                    Funcao_erro()
                )
                carregandoprogress!!.visibility = View.GONE
                entrar!!.text = "Entrar"
            }

            Login_Erro.Sucesso ->{

                carregandoprogress!!.visibility = View.GONE
                entrar!!.text = "Entrar"
                var intent = Intent(baseContext, Act_Pricipal::class.java)
                startActivity(intent)
            }
        }
    }


}
