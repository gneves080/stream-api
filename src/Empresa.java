import java.util.ArrayList;
import java.util.List;

public class Empresa {

    private final String nome;
    private final List<Funcionario> funcionarios = new ArrayList<>();

    public Empresa(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }

    public Empresa adicionar(Funcionario funcionario) {
        funcionarios.add(funcionario);
        return this;
    }
}