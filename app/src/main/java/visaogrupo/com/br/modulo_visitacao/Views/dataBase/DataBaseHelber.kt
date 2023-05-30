package visaogrupo.com.br.modulo_visitacao.Views.dataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelber (context:Context,) : SQLiteOpenHelper(
    context,
    "Carga.db",
    null,
    10 // aqui serve para especificar a versao do banco de dados , vc troca quando cria uma nova tabela
) {
    override fun onCreate(db: SQLiteDatabase?) {
        CriarEAtualizarTabelas(db)
        Log.d("Info_dp","Sucesso ao cria tabela")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        // clientes por lojas
        CriarEAtualizarTabelas(db)
        Log.d("Banco de dados","Atualizado")
    }


    private  fun CriarEAtualizarTabelas(db: SQLiteDatabase?){
        // Criando Todas as Tabelas no Banco de dados
        // Tabele de lojas
        val sqllojas = "CREATE TABLE if NOT EXISTS TB_lojas(" +
                "loja_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "nome varchar(400),Ordem INT," +
                "MinimoUnidades INT," +
                "MinimoValor FLOAT," +
                "LojaTipo INT," +
                "tipo VARCHAR (100)," +
                "LogoHome VARCHAR (40)," +
                "Distribuidora INT," +
                "Loja_Desconto BOLEAN," +
                "DescontoMaximo FLOAT," +
                "Tipo_Imposto_ID INT," +
                "Tipo_Imposto VARCHAR(30)," +
                "Libera_Fidelidade BOLEAN," +
                "Qtd_ProdutosLoja INT," +
                "Loja_Tablet BOLEAN," +
                "Venda_Tipo_id, INT," +
                "Data_Inicio DATE," +
                "Data_Fim DATE," +
                "Portal_Tablet INT," +
                "Qtd_Minima_Operador INT," +
                "Qtd_Maxima_Operador INT," +
                "Loja_Online Bolean," +
                "Minimo_Aprovacao FLOAT," +
                "Valida_Estoque Bolean," +
                "Loja_Preco Bolean," +
                "Exibe_Estoque Bolean" +
                ");"
        // Tabele de lojas
        val sqlCliente = "CREATE TABLE if NOT EXISTS TB_clientes(" +
                "Empresa_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  "+
                "cnpj varchar(20)," +
                "RazaoSocial varchar(255), " +
                "Fantasia varchar (255)," +
                "Endereco varchar (255)," +
                "Numero varchar (20)," +
                "Complemento varchar (90)," +
                "Bairro varchar(90)," +
                "Cidade varchar (90)," +
                "UF varchar (2)," +
                "Cep varchar(90)," +
                "CompraControlado Bolean," +
                "LimiteCredito INT ," +
                "UltimoPedido varchar," +
                "VendaDireta varchar (20)," +
                "Associativismo varchar(10)," +
                "Telefone varchar (20)," +
                "Email varchar (255)," +
                "Formalizado varchar (2)," +
                "Investimento Bolean," +
                "DuplicataVencida Int," +
                "SanitarioData INT"+
                ");"
        // clientes por lojas

        val apagaTabelea = "DROP TABLE IF EXISTS TB_lojaporcliente;"


        val sqlClientePorLojas = "CREATE TABLE IF NOT EXISTS TB_lojaporcliente (" +
                "CLIENTE_LOJA_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "empresa_id INTEGER NOT NULL,"+
                "loja_id INTEGER NOT NULL"+
                ");"

        val sqlProtudos = "CREATE TABLE IF NOT EXISTS TB_produtos(" +
                "Produto_codigo INTEGER PRIMARY KEY,"+
                "Nome varchar(255),"+
                "Apresentacao varchar(100),"+
                "PrincipioAtivo varchar(100),"+
                "ListaICMS varchar(100),"+
                "CaixaPadrao INTEGER,"+
                "Imagem varchar(50),"+
                "Barra varchar(30),"+
                "Unidade_id INTERGER,"+
                "Categoria varchar(10),"+
                "RegistroMS varchar(40)"+
                ");"

        try {
            db?.execSQL(sqlProtudos)
            db?.execSQL(apagaTabelea)
            db?.execSQL(sqlClientePorLojas)
            db?.execSQL(sqlCliente)
            db?.execSQL(sqllojas)


        }catch (e:Exception)
        {
            e.printStackTrace()
            Log.d("Info_dp","Erro ao cria tabela")
        }
    }
}