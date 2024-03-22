package visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.View.Dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import visaogrupo.com.br.TudoFarmaOffiline.R
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Interfaces.Ondimiss.DataPikerData
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Clientes
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.Objetos.Visitas
import visaogrupo.com.br.modulo_visitacao.TudoFarmaOffiline.Models.Class.dataBase.VisitaDAO


class DialogMarcaVisitas(context: Context): DataPikerData {
    val  dialog =  Dialog(context);
    lateinit var  dataInput:TextView

    fun dialogDetalhe(context: Context, cnpjformat:String,
                      cliente: Clientes

    ){


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_marca_visita);

        dialog.show();
        dialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow()?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()?.getAttributes()?.windowAnimations= R.style.DialoAnimation;
        dialog.getWindow()?.setGravity(Gravity.BOTTOM);
        val cnpj =  dialog.findViewById<TextView>(R.id.cnpjdetalhe)
        val razaoSocial =  dialog.findViewById<TextView>(R.id.razaodetalhe)
        val endereco =  dialog.findViewById<TextView>(R.id.enderecodetalhe)
        val Telefone =  dialog.findViewById<TextView>(R.id.telefonecelular)
        val xdetalhe = dialog.findViewById<ImageView>(R.id.xdetalhe)
        val email = dialog.findViewById<TextView>(R.id.email)
        val maps= dialog.findViewById<ImageView>(R.id.maps)
        val  salvarData = dialog.findViewById<TextView>(R.id.salvarData)
        dataInput = dialog.findViewById(R.id.dataInput)
        cnpj.text = cnpjformat

        razaoSocial.text = cliente.RazaoSocial
        endereco.text = "${cliente.Endereco}, ${cliente.Numero} ${cliente.Cidade},${cliente.Bairro}, ${cliente.UF}"
        Telefone.text = cliente.Telefone
        email.text = cliente.Email

        email.setOnClickListener {
            val subject = "Comprar remedios"
            val body = "Olá, como posso tirar uma duvida?"
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")

            intent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf<String>(email.text.toString())
            )
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, body)
            context.startActivity(Intent.createChooser(intent, "Enviar email"))
        }

        Telefone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + Telefone.text.toString())

            context.startActivity(intent)

        }
        dataInput.setOnClickListener {
            val dialogCalendarrio = DialogCalendarrio()
            dialogCalendarrio.dialogCalendario(context, this)

        }

        salvarData.setOnClickListener {
            val backgroundDrawable = dataInput.background
            val gradientDrawable = backgroundDrawable.mutate() as GradientDrawable
            gradientDrawable.mutate()
            val dialogErro = DialogErro()
            if (dataInput.text.toString().equals("Selecione uma data")){
                gradientDrawable.setStroke(2,Color.RED)

                dialogErro.Dialog(context,"Atenção","Selecione uma data para sua visita","ok",""){

                }
            }else{
                gradientDrawable.setStroke(2,Color.parseColor("#BEC1C4"))
                val  dataCap = dataInput.text.toString()
                val visitaoDAO = VisitaDAO(context)
                val listaVisita = visitaoDAO.lista(true,dataCap)
                val  visitas = Visitas(
                    0,
                    cliente.CNPJ,
                    cliente.RazaoSocial,
                    cliente.Endereco,
                    cliente.Telefone,
                    cliente.Email,
                    dataInput.text.toString(),
                    listaVisita.size
                )
                visitaoDAO.insert(visitas)
                dialog.dismiss()
                dialogErro.Dialog(context,"Sucesso",
                    "Sua visita foi salva com suscesso!","ok",""){

                }
            }
        }

        maps.setOnClickListener {
            val address = endereco.text.toString()
            val uri = Uri.parse("geo:0,0?q=" + Uri.encode(address))
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)

        }

        xdetalhe.setOnClickListener {
            dialog.onBackPressed()
        }

    }

    override fun dataPiker(data: String) {
        val backgroundDrawable = dataInput.background
        val gradientDrawable = backgroundDrawable.mutate() as GradientDrawable
        gradientDrawable.mutate()
        gradientDrawable.setStroke(2,Color.parseColor("#BEC1C4"))

        dataInput.text = data
    }
}