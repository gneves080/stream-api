import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Consultas sobre empresas e funcionários, todas resolvidas com Stream API (sem laços for).
 */
public final class ConsultasFuncionarios {

    private static final int ANO_INICIAL = 1975;
    private static final int ANO_FINAL = 1985;
    private static final BigDecimal SALARIO_MAXIMO = new BigDecimal("3000.00");
    private static final int IDADE_LIMITE = 25;
    private static final int TAMANHO_RANKING = 30;

    private ConsultasFuncionarios() {
    }

    /** Todos os funcionários de todas as empresas, em um único Stream. */
    private static Stream<Funcionario> todosOsFuncionarios(List<Empresa> empresas) {
        return empresas.stream().flatMap(empresa -> empresa.getFuncionarios().stream());
    }

    /**
     * Funcionários de qualquer empresa nascidos entre 1975 e 1985 (inclusive)
     * com salário menor ou igual a R$ 3.000,00.
     */
    public static List<Funcionario> nascidosEntre1975e1985ComSalarioAte3000(List<Empresa> empresas) {
        return todosOsFuncionarios(empresas)
                .filter(f -> f.getDataNascimento().getYear() >= ANO_INICIAL
                        && f.getDataNascimento().getYear() <= ANO_FINAL)
                .filter(f -> f.getSalario().compareTo(SALARIO_MAXIMO) <= 0)
                .collect(Collectors.toList());
    }

    /** Soma dos números pares da lista (0 se não houver pares). */
    public static int somaDosPares(List<Integer> numeros) {
        return numeros.stream()
                .filter(n -> n % 2 == 0)
                .mapToInt(Integer::intValue)
                .sum();
    }

    /**
     * Os 30 maiores salários de funcionários CLT com menos de 25 anos (em ordem decrescente),
     * considerando a data de hoje.
     */
    public static List<BigDecimal> maioresSalariosCltMenoresDe25(List<Empresa> empresas) {
        return maioresSalariosCltMenoresDe25(empresas, LocalDate.now());
    }

    /** Mesma consulta, com data de referência explícita para a idade (útil em testes). */
    public static List<BigDecimal> maioresSalariosCltMenoresDe25(List<Empresa> empresas, LocalDate referencia) {
        return todosOsFuncionarios(empresas)
                .filter(f -> f.getTipoContrato() == TipoContrato.CLT)
                .filter(f -> f.getIdade(referencia) < IDADE_LIMITE)
                .map(Funcionario::getSalario)
                .sorted(Comparator.reverseOrder())
                .limit(TAMANHO_RANKING)
                .collect(Collectors.toList());
    }
}