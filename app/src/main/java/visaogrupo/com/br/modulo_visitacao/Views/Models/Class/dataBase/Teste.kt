package visaogrupo.com.br.modulo_visitacao.Views.Models.Class.dataBase

val query = "SELECT \n" +
        "    PedidoID,\n" +
        "    loja_id,\n" +
        "    cliente_id,\n" +
        "    OperadorLogistigo,\n" +
        "    Usuario_id,\n" +
        "    UF,\n" +
        "    Comissao,\n" +
        "    ComissaoPorcentagem,\n" +
        "    MarcasXComissoes_id,\n" +
        "    Produto_codigo,\n" +
        "    0 AS kitCodigo,\n" +
        "    Barra,\n" +
        "    Quantidade,\n" +
        "    PF,\n" +
        "    0.0 AS De,\n" +
        "    0.0 AS Por,\n" +
        "    ''  AS NomeKit,\n" +
        "    Valor,\n" +
        "    ValorOriginal,\n" +
        "    Grupo_Codigo,\n" +
        "    Desconto,\n" +
        "    DescontoOriginal,\n" +
        "    ST,\n" +
        "    formalizacao,\n" +
        "    CODLISTAPRECOSYNC,\n" +
        "    ValorTotal,\n" +
        "    Nome,\n" +
        "    Apontador_codigo,\n" +
        "    nomeLoja,\n" +
        "    razaosocial,\n" +
        "    cnpj,\n" +
        "    dataPedido,\n" +
        "    valorminimoLoja,\n" +
        "    base64,\n" +
        "    caixapadrao,\n" +
        "    pmc,\n" +
        "    Qtd_Minima_Operador,\n" +
        "    Qtd_Maxima_Operador,\n" +
        "    ANR,\n" +
        "    PedidoEnviado,\n" +
        "    formaDePagemento,\n" +
        "    NumeroPedido,\n" +
        "    OperadoresPedidos,\n" +
        "    FormaPagamentoExclusiva,\n" +
        "    Chave,\n" +
        "    justificativaANR,\n" +
        "    TipoLoja,\n" +
        "    RegraPrazo,\n" +
        "    QtdMaximaOpl,\n" +
        "    0 AS Kit\n" +
        "FROM TBPedidosFinalizados\n" +
        "UNION\n" +
        "\n" +
        "SELECT \n" +
        "    PedidoID,\n" +
        "    loja_id,\n" +
        "    cliente_id,\n" +
        "    OperadorLogistigo,\n" +
        "    Usuario_id,\n" +
        "    UF,\n" +
        "    0 AS Comissao,\n" +
        "    0 AS ComissaoPorcentagem,\n" +
        "    0 AS MarcasXComissoes_id,\n" +
        "    0 AS Produto_codigo,\n" +
        "    kitCodigo,\n" +
        "    '' AS Barra,\n" +
        "    Quantidade,\n" +
        "    0.0 AS PF,\n" +
        "    0.0 AS De,\n" +
        "    0.0 AS Por,\n" +
        "    '' AS NomeKit,\n" +
        "    0.0 AS Valor,\n" +
        "    0.0 AS ValorOriginal,\n" +
        "    0 AS Grupo_Codigo,\n" +
        "    Desconto,\n" +
        "    0.0 AS DescontoOriginal,\n" +
        "    0.0 AS ST,\n" +
        "    '' AS formalizacao,\n" +
        "    CODLISTAPRECOSYNC,\n" +
        "    ValorTotal,\n" +
        "    '' AS Nome,\n" +
        "    '' AS Apontador_codigo,\n" +
        "    nomeLoja,\n" +
        "    razaosocial,\n" +
        "    cnpj,\n" +
        "    dataPedido,\n" +
        "    valorminimoLoja,\n" +
        "    '' AS base64,\n" +
        "    0 AS caixapadrao,\n" +
        "    0.0 AS pmc,\n" +
        "    Qtd_Minima_Operador,\n" +
        "    Qtd_Maxima_Operador,\n" +
        "    0 AS ANR,\n" +
        "    PedidoEnviado,\n" +
        "    formaDePagemento,\n" +
        "    NumeroPedido,\n" +
        "    OperadoresPedidos,\n" +
        "    FormaPagamentoExclusiva,\n" +
        "    Chave,\n" +
        "    '' AS justificativaANR,\n" +
        "    TipoLoja,\n" +
        "    RegraPrazo,\n" +
        "    QtdMaximaOpl,\n" +
        "    1 AS Kit\n" +
        "FROM TBPedidosFinalizadosKit;"