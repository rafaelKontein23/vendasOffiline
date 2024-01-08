package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelber (context:Context,) : SQLiteOpenHelper(
    context,
    "CargaHertz.db",
    null,
    101 // aqui serve para especificar a versao do banco de dados , vc troca quando cria uma nova tabela ou mude algo nas query
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
                "ANR INT,"+
                "Exibe_Estoque Bolean," +
                "RegraPrazoMedio Bolean,"+
                "cliente_id INT" +
                ");"
        // Tabele de lojas
        val sqlCliente = "CREATE TABLE if NOT EXISTS TB_clientes(" +
                "Empresa_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  "+
                "cnpj VARCHAR(20)," +
                "RazaoSocial VARCHAR(255), " +
                "Fantasia VARCHAR (255)," +
                "Endereco VARCHAR (255)," +
                "Numero VARCHAR (20)," +
                "Complemento VARCHAR (90)," +
                "Bairro VARCHAR(90)," +
                "Cidade VARCHAR (90)," +
                "UF VARCHAR (2)," +
                "Cep VARCHAR(90)," +
                "CompraControlado Bolean," +
                "UltimoPedido VARCHAR(50)," +
                "VendaDireta VARCHAR (20)," +
                "Associativismo VARCHAR(10)," +
                "Telefone VARCHAR (20)," +
                "Email VARCHAR (255)," +
                "Formalizado VARCHAR (2)," +
                "Investimento Bolean," +
                "DuplicataVencida INTEGER," +
                "Codigo INTEGER," +
                "SanitarioData INTEGER,"+
                "Compra INTEGER,"+
                "ExibeAlerta Bolean,"+
                "LimiteCredito REAL,"+
                "LimiteDisponivel REAL,"+
                "FormaPagamentoExclusiva Bolean," +
                "CodEstoque INTEGER"+
                ");"
        // clientes por lojas

        val apagaTabelea = "DROP TABLE IF EXISTS TB_lojaporcliente;"

        val  prazoMedio = "CREATE TABLE IF NOT EXISTS TB_PrazoMedioXValor(" +
                "ValorDe REAL," +
                "ValorAte REAL," +
                "PrazoMedio INTEGER" +
                ")"



        val sqlClientePorLojas = "CREATE TABLE IF NOT EXISTS TB_lojaporcliente (" +
                "CLIENTE_LOJA_ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "empresa_id INTEGER NOT NULL,"+
                "loja_id INTEGER NOT NULL"+
                ");"

        val sqlFormpagExclusivo ="CREATE TABLE IF NOT EXISTS TB_FormPag(" +
                "CNPJ varchar(24) PRIMARY KEY,"+
                "Cod_FormaPgto varchar(50),"+
                "FormaPgto VARCHAR(255)"+
                ");"

        val sqlProtudos = "CREATE TABLE IF NOT EXISTS TB_produtos(" +
                "Produto_codigo INTEGER PRIMARY KEY,"+
                "Nome varchar(255),"+
                "Apresentacao VARCHAR(100),"+
                "PrincipioAtivo VARCHAR(100),"+
                "ListaICMS VARCHAR(100),"+
                "CaixaPadrao INTEGER,"+
                "Imagem VARCHAR(50),"+
                "Barra VARCHAR(30),"+
                "Unidade_id INTERGER,"+
                "Categoria VARCHAR(10),"+
                "RegistroMS VARCHAR(40)"+
                ");"
        val sqlFormaDePagemento = "CREATE TABLE IF NOT EXISTS TB_formaDePagamento(" +
                "loja_id INTEGER NOT NULL," +
                "Cod_FormaPgto VARCHAR(40) NOT NULL," +
                "FormaPgto VARCHAR(40) NOT NULL," +
                "ValorMinimo DOUBLE NOT NULL," +
                "PrazoMedio INTEGER,"+
                "Alternativa  Boolean NOT NULL" +
                ");"

       val  sqlOperadorLogistico = "CREATE TABLE IF NOT EXISTS TB_OperadorLogistico(" +
                "loja_id INTEGER not null," +
                "OperadorLogistico_ID INTEGER," +
                "Nome VARCHAR(60)," +
                "Estado VARCHAR(2)," +
                "MinimoValor DOUBLE," +
                "OperadorLogistico_Grupo_id INTEGER," +
                "Grupo VARCHAR(25)" +
                ");"

        val  sqlPRogressiva = "CREATE TABLE IF NOT EXISTS TB_Progressiva (" +
                "Loja_id INTEGER NOT NULL," +
                "Prod_cod INTEGER NOT NULL," +
                "Valor REAL NOT NULL," +
                "Quantidade INTEGER NOT NULL," +
                "QuantidadeMaxima INTEGER NOT NULL," +
                "Desconto REAL NOT NULL," +
                "Desconto_Min REAL NOT NULL," +
                "DescontoMaximo REAL NOT NULL," +
                "PF REAL NOT NULL," +
                "PMC REAL NOT NULL," +
                "ST REAL NOT NULL," +
                "formalizacao TEXT NOT NULL," +
                "Seq_Kit INTEGER NOT NULL," +
                "Seq_Cond_Coml INTEGER NOT NULL," +
                "UF TEXT NOT NULL," +
                "Data_Vencimento TEXT NOT NULL," +
                "Prioridade INTEGER NOT NULL," +
                "Promocao INTEGER NOT NULL," +
                "Codigo INTEGER NOT NULL" +
                ");"

        val sqlProgressivaPersonalizada= "CREATE TABLE IF NOT EXISTS  Tb_Progressiva_Personalizada (" +
                "COLUMN_PRODUTO_CODIGO INTEGER, " +
                "COLUMN_CAIXA_PADRAO INTEGER, " +
                "COLUMN_PMC REAL, " +
                "COLUMN_PF REAL, " +
                "COLUMN_VALOR REAL, " +
                "COLUMN_QUANTIDADE INTEGER, " +
                "COLUMN_DESCONTO REAL," +
                "PRIMARY KEY (COLUMN_DESCONTO, COLUMN_QUANTIDADE) "+
                ")"

        val sqlEstoque = "CREATE TABLE IF NOT EXISTS TB_Estoque(" +
                "EAN VARCHAR(50) NOT NULL," +
                "Quantidade INTEGER NOT NULL," +
                "Centro INTEGER NOT NULL" +
                ");"


        val sqlCarrinho = "CREATE TABLE IF NOT EXISTS TB_Carrinho(" +
                "loja_id INTEGER NOT NULL," +
                "cliente_id INTEGER NOT NULL," +
                "OperadorLogistigo INTEGER NOT NULL," +
                "Usuario_id INTEGER NOT NULL,"+
                "UF INTEGER NOT NULL,"+
                "Comissao REAL, " +
                "ComissaoPorcentagem REAL, " +
                "MarcasXComissoes_id INTEGER, " +
                "Produto_codigo INTEGER, " +
                "Barra TEXT, " +
                "Quantidade INTEGER, " +
                "PF REAL NOT NULL, " +
                "Valor REAL NOT NULL, " +
                "ValorOriginal REAL NOT NULL, " +
                "Grupo_Codigo INTEGER, " +
                "Desconto REAL NOT NULL, " +
                "DescontoOriginal REAL NOT NULL, " +
                "ST REAL, " +
                "formalizacao TEXT, " +
                "CODLISTAPRECOSYNC INTEGER, " +
                "ValorTotal REAL, " +
                "Nome TEXT, " +
                "Apontador_codigo TEXT,"+
                "nomeLoja TEXT,"+
                "razaosocial TEXT,"+
                "cnpj TEXT,"+
                "dataPedido TEXT,"+
                "valorminimoLoja REAL,"+
                "base64 TEXT,"+
                "caixapadrao INT,"+
                "pmc REAL,"+
                "Qtd_Minima_Operador INT," +
                "Qtd_Maxima_Operador INT," +
                "ANR BOLEAN," +
                "FormaPagamentoExclusiva INTEGER,"+
                "RegraPrazo  INT," +
                "LojaTipo  INT," +

                "PRIMARY KEY (cliente_id,loja_id,Produto_codigo)"+
                ")"

        val sqlPedidos = "CREATE TABLE IF NOT EXISTS TBPedidosFinalizados(" +
                "PedidoID INTEGER PRIMARY KEY AUTOINCREMENT," + // Definindo PedidoID como PRIMARY KEY
                "loja_id INTEGER NOT NULL," +
                "cliente_id INTEGER NOT NULL," +
                "OperadorLogistigo VARCHAR(255) NOT NULL," +
                "Usuario_id INTEGER NOT NULL,"+
                "UF INTEGER NOT NULL,"+
                "Comissao REAL, " +
                "ComissaoPorcentagem REAL, " +
                "MarcasXComissoes_id INTEGER, " +
                "Produto_codigo INTEGER, " +
                "Barra TEXT, " +
                "Quantidade INTEGER, " +
                "PF REAL NOT NULL, " +
                "Valor REAL NOT NULL, " +
                "ValorOriginal REAL NOT NULL, " +
                "Grupo_Codigo INTEGER, " +
                "Desconto REAL NOT NULL, " +
                "DescontoOriginal REAL NOT NULL, " +
                "ST REAL, " +
                "formalizacao TEXT, " +
                "CODLISTAPRECOSYNC INTEGER, " +
                "ValorTotal REAL, " +
                "Nome TEXT, " +
                "Apontador_codigo TEXT,"+
                "nomeLoja TEXT,"+
                "razaosocial TEXT,"+
                "cnpj TEXT,"+
                "dataPedido TEXT,"+
                "valorminimoLoja REAL,"+
                "base64 TEXT,"+
                "caixapadrao INT,"+
                "pmc REAL,"+
                "Qtd_Minima_Operador INT," +
                "Qtd_Maxima_Operador INT," +
                "ANR BOLEAN," +
                "PedidoEnviado INTEGER," +
                "formaDePagemento VARCHAR," +
                "NumeroPedido VARCHAR(255),"+
                "OperadoresPedidos VARCHAR(100)," +
                "FormaPagamentoExclusiva INTEGER,"+
                "Chave VARCHAR(255),"+
                "justificativaANR VARCHAR(255),"+
                "TipoLoja INT"+

                ")"


        val sqlProdutoItemFinalizadoPedido = "CREATE TABLE IF NOT EXISTS TB_Produtos_Pedidos_Finalizado(" +
                "PedidoID INTEGER NOT NULL," +
                "Barra  VARCHAR(30) NOT NULL," +
                "Produto_codigo INTEGER,"+
                "Desconto REAL,"+
                "DescontoOriginal REAL,"+
                "formalizacao VARCHAR(100),"+
                "PF REAL,"+
                "Quantidade INTEGER,"+
                "ValorRepasse REAL,"+
                "ST VARCHAR(30),"+
                "Valor REAL,"+
                "NomeProduto VARCHAR(255),"+
                "FOREIGN KEY (PedidoID) REFERENCES TBPedidosFinalizados(PedidoID));"

        val  sqlImagens ="CREATE TABLE IF NOT EXISTS TB_Imagens(" +
                "barra TEXT PRIMARY KEY,"+
                "imagembase64 TEXT"+
                ")"


        val  sqlModeloPedido ="CREATE TABLE IF NOT EXISTS TB_ModeloPedido(" +
                "CodigoUsuario INTEGER, " +
                "NomeModeloPedido VARCHAR, " +
                "ClienteId INTEGER, " +
                "CodigoLoja INTEGER," +
                "CodigoProduto INTEGER, " +
                "Quantidade INTEGER, " +
                "CodigoProgressiva INTEGER," +
                "Desconto DOUBLE);"
        try {
            db?.execSQL(sqlEstoque)
            db?.execSQL(sqlPRogressiva)
            db?.execSQL(sqlOperadorLogistico)
            db?.execSQL(sqlFormaDePagemento)
            db?.execSQL(sqlProtudos)
            db?.execSQL(apagaTabelea)
            db?.execSQL(sqlClientePorLojas)
            db?.execSQL(sqlCliente)
            db?.execSQL(sqllojas)
            db?.execSQL(sqlCarrinho)
            db?.execSQL(sqlProgressivaPersonalizada)
            db?.execSQL(sqlImagens)
            db?.execSQL(sqlModeloPedido)
            db?.execSQL(prazoMedio)
            db?.execSQL(sqlFormpagExclusivo)
            db?.execSQL(sqlPedidos)
            db?.execSQL(sqlProdutoItemFinalizadoPedido)

        }catch (e:Exception) {
            e.printStackTrace()
            Log.d("Info_dp","Erro ao cria tabela")
        }
    }
}