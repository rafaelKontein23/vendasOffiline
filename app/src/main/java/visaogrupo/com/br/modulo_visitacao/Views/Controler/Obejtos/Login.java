package visaogrupo.com.br.modulo_visitacao.Views.Controler.Obejtos;

public class Login {

       String Usuario_id;
       String Nome;
       String Email;
       String Senha;
       String UF;
       String Linha_id;
       String LinhaVendedor;
       String Institucional;
       String Setor;
       String FlagMerchan;
       String AlterarSenha;
       String Teste;
       String TipoCadastro_id;
       String Feriado;
       String  FinalDeSemana;


       public Login(String usuario_id, String nome, String email, String senha, String UF,
                    String linha_id, String linhaVendedor, String institucional, String setor,
                    String flagMerchan, String alterarSenha, String teste, String tipoCadastro_id,
                    String feriado, String finalDeSemana) {
              Usuario_id = usuario_id;
              Nome = nome;
              Email = email;
              Senha = senha;
              this.UF = UF;
              Linha_id = linha_id;
              LinhaVendedor = linhaVendedor;
              Institucional = institucional;
              Setor = setor;
              FlagMerchan = flagMerchan;
              AlterarSenha = alterarSenha;
              Teste = teste;
              TipoCadastro_id = tipoCadastro_id;
              Feriado = feriado;
              FinalDeSemana = finalDeSemana;
       }


       public String getUsuario_id() {
              return Usuario_id;
       }

       public void setUsuario_id(String usuario_id) {
              Usuario_id = usuario_id;
       }

       public String getNome() {
              return Nome;
       }

       public void setNome(String nome) {
              Nome = nome;
       }

       public String getEmail() {
              return Email;
       }

       public void setEmail(String email) {
              Email = email;
       }

       public String getSenha() {
              return Senha;
       }

       public void setSenha(String senha) {
              Senha = senha;
       }

       public String getUF() {
              return UF;
       }

       public void setUF(String UF) {
              this.UF = UF;
       }

       public String getLinha_id() {
              return Linha_id;
       }

       public void setLinha_id(String linha_id) {
              Linha_id = linha_id;
       }

       public String getLinhaVendedor() {
              return LinhaVendedor;
       }

       public void setLinhaVendedor(String linhaVendedor) {
              LinhaVendedor = linhaVendedor;
       }

       public String getInstitucional() {
              return Institucional;
       }

       public void setInstitucional(String institucional) {
              Institucional = institucional;
       }

       public String getSetor() {
              return Setor;
       }

       public void setSetor(String setor) {
              Setor = setor;
       }

       public String getFlagMerchan() {
              return FlagMerchan;
       }

       public void setFlagMerchan(String flagMerchan) {
              FlagMerchan = flagMerchan;
       }

       public String getAlterarSenha() {
              return AlterarSenha;
       }

       public void setAlterarSenha(String alterarSenha) {
              AlterarSenha = alterarSenha;
       }

       public String getTeste() {
              return Teste;
       }

       public void setTeste(String teste) {
              Teste = teste;
       }

       public String getTipoCadastro_id() {
              return TipoCadastro_id;
       }

       public void setTipoCadastro_id(String tipoCadastro_id) {
              TipoCadastro_id = tipoCadastro_id;
       }

       public String getFeriado() {
              return Feriado;
       }

       public void setFeriado(String feriado) {
              Feriado = feriado;
       }

       public String getFinalDeSemana() {
              return FinalDeSemana;
       }

       public void setFinalDeSemana(String finalDeSemana) {
              FinalDeSemana = finalDeSemana;
       }
}
