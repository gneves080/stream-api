import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Funcionario {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    private final String nome;
    private final LocalDate dataNascimento;
    private final BigDecimal salario;
    private final TipoContrato tipoContrato;

    public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, TipoContrato tipoContrato) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.salario = salario;
        this.tipoContrato = tipoContrato;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    /** Idade completa na data de referência informada (útil para testes previsíveis). */
    public int getIdade(LocalDate referencia) {
        return (int) ChronoUnit.YEARS.between(dataNascimento, referencia);
    }

    /** Idade completa hoje. */
    public int getIdade() {
        return getIdade(LocalDate.now());
    }

    @Override
    public String toString() {
        return String.format(PT_BR, "%s | nascimento %s | %s | R$ %,.2f",
                nome, dataNascimento, tipoContrato, salario);
    }
}