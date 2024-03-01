package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Atividades

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Enuns.Login_Erro
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.Ondismiss
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.CapturaDeviceToken
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.DataAtual
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.ExcluiDados
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.ExcluirPrefuser
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs.DialogErro

import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Ultis.Verifica_Internet
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.task.Task_Login

import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.DataBaseHelber

class ActLogin:  AppCompatActivity() ,
    Ondismiss {


    private var email: EditText? = null
    private  var senha:EditText? = null
    private var carregandoprogress: ProgressBar? = null
    private var  equesciSenha :TextView? =  null
    private var entrar: Button? = null


    val login_erro = Login_Erro.Erro
    companion object {
        val  versao = "3.0.0"
        var device = ""

    }


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tela_login)

        email = findViewById(R.id.email_login)
        senha = findViewById(R.id.senha)
        entrar = findViewById(R.id.entrar)
        equesciSenha = findViewById(R.id.equesci_senha)
        carregandoprogress = findViewById(R.id.progressBar_carregando_login)
        // Cria o banco de dados Pela primeira vez
        DataBaseHelber(this).writableDatabase

        val  capDevice = CapturaDeviceToken()
        capDevice.recuperaToken()
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
                    e.printStackTrace()
                    var dialogErro = DialogErro()
                    dialogErro.Dialog(this,"Erro na conexão!","Algo deu errado, por favor verifique a conexão e tente novamente","OK",""){

                    }
                    carregandoprogress!!.visibility = View.GONE
                    entrar!!.text = "Entrar"

                }
            }else{
                var dialogErro = DialogErro()
                dialogErro.Dialog(this,"Erro na conexão!","Por favor, Verifique se há acesso a internet","OK",""){

                }
                carregandoprogress!!.visibility = View.GONE
                entrar!!.text = "Entrar"

            }

        }
    }

    override fun onBackPressed() {
       // super.onBackPressed()
    }

    override fun ondismiss(enum: visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Enuns.Login_Erro) {
        // Para verificar se os dados estão corretos
        when(enum){
           Login_Erro.Erro ->{
                val dialog_erro = DialogErro()
                dialog_erro.Dialog(this, "Atenção", "Algo deu errado, verifiue os campos e tentente novamente", "OK", "Cancelar",
                ){

                }
                carregandoprogress!!.visibility = View.GONE
                entrar!!.text = "Entrar"
            }

          Login_Erro.Sucesso ->{
                carregandoprogress!!.visibility = View.GONE
                entrar!!.text = "Entrar"
                carregandoprogress!!.visibility = View.GONE
                entrar!!.text = "Entrar"
                var intent = Intent(baseContext, ActPricipal::class.java)

                intent.putExtra("Tela","")
                val dataAtual = DataAtual()
                val  date = dataAtual.recuperaData()
                val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("dataLogin", date)
                editor.apply()
                ExcluirPrefuser.excluirItemPref(baseContext,"LojaSelecionada")
                ExcluirPrefuser.excluirItemPref(baseContext,"ClienteSelecionado")
                val excluiDadosTabelas = ExcluiDados(baseContext)
                excluiDadosTabelas.exluidadosGeral()

                editor?.putBoolean("cargafeita", false)
                editor?.apply()
                startActivity(intent)
            }
        }
    }


}
