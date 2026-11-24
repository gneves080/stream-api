import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");
    private static int falhas = 0;

    public static void main(String[] args) {
        desafio1();
        desafio2();
        desafio3();

        System.out.println();
        System.out.println(falhas == 0
                ? "Todas as verificações passaram."
                : falhas + " verificação(ões) falharam.");
    }

    // ---------------------------------------------------------------- Desafio 1
    private static void desafio1() {
        System.out.println("=== Desafio 1: nascidos entre 1975 e 1985 com salário <= R$ 3.000,00 ===");

        Empresa alfa = new Empresa("Alfa Tech")
                .adicionar(new Funcionario("Ok 1975 (limite inferior)", LocalDate.of(1975, 1, 1), new BigDecimal("3000.00"), TipoContrato.CLT))
                .adicionar(new Funcionario("Ok 1985 (limite superior)", LocalDate.of(1985, 12, 31), new BigDecimal("2500.00"), TipoContrato.CLT))
                .adicionar(new Funcionario("Salário acima do limite", LocalDate.of(1980, 6, 1), new BigDecimal("3000.01"), TipoContrato.CLT))
                .adicionar(new Funcionario("Nascido em 1974", LocalDate.of(1974, 12, 31), new BigDecimal("1000.00"), TipoContrato.CLT));
        Empresa beta = new Empresa("Beta Serviços")
                .adicionar(new Funcionario("Ok 1980", LocalDate.of(1980, 6, 15), new BigDecimal("1800.50"), TipoContrato.PJ))
                .adicionar(new Funcionario("Nascido em 1986", LocalDate.of(1986, 1, 1), new BigDecimal("1500.00"), TipoContrato.CLT))
                .adicionar(new Funcionario("Ok 1982", LocalDate.of(1982, 3, 3), new BigDecimal("2999.99"), TipoContrato.CLT));

        List<Funcionario> resultado =
                ConsultasFuncionarios.nascidosEntre1975e1985ComSalarioAte3000(Arrays.asList(alfa, beta));

        resultado.forEach(f -> System.out.println("  " + f));

        List<String> nomes = resultado.stream().map(Funcionario::getNome).collect(Collectors.toList());
        verificar("retorna exatamente os 4 funcionários esperados, incluindo os limites 1975, 1985 e R$ 3.000,00",
                nomes.equals(Arrays.asList("Ok 1975 (limite inferior)", "Ok 1985 (limite superior)", "Ok 1980", "Ok 1982")));
    }

    // ---------------------------------------------------------------- Desafio 2
    private static void desafio2() {
        System.out.println("\n=== Desafio 2: soma dos números pares ===");

        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6, -2, 0);
        int soma = ConsultasFuncionarios.somaDosPares(numeros);
        System.out.println("  Lista: " + numeros);
        System.out.println("  Soma dos pares: " + soma);

        verificar("soma de [1,2,3,4,5,6,-2,0] é 10", soma == 10);
        verificar("lista só com ímpares resulta em 0", ConsultasFuncionarios.somaDosPares(Arrays.asList(1, 3, 5)) == 0);
        verificar("lista vazia resulta em 0", ConsultasFuncionarios.somaDosPares(Arrays.<Integer>asList()) == 0);
    }

    // ---------------------------------------------------------------- Desafio 3
    private static void desafio3() {
        System.out.println("\n=== Desafio 3: 30 maiores salários de CLT com menos de 25 anos ===");

        LocalDate referencia = LocalDate.of(2026, 11, 24);

        // 40 CLT jovens (22 anos) com salários de R$ 2.000 a R$ 5.900, divididos em duas empresas
        List<Funcionario> jovens = IntStream.range(0, 40)
                .mapToObj(i -> new Funcionario("Jovem CLT " + i, LocalDate.of(2004, 5, 10),
                        BigDecimal.valueOf(2000 + 100L * i), TipoContrato.CLT))
                .collect(Collectors.toList());

        Empresa alfa = new Empresa("Alfa Tech");
        Empresa beta = new Empresa("Beta Serviços");
        jovens.subList(0, 20).forEach(alfa::adicionar);
        jovens.subList(20, 40).forEach(beta::adicionar);

        // Casos que precisam ficar de fora (salários altos de propósito) e um caso limite que fica
        alfa.adicionar(new Funcionario("24 anos (faz 25 amanhã)", LocalDate.of(2001, 11, 25), new BigDecimal("9999.00"), TipoContrato.CLT));
        alfa.adicionar(new Funcionario("Faz 25 hoje", LocalDate.of(2001, 11, 24), new BigDecimal("30000.00"), TipoContrato.CLT));
        beta.adicionar(new Funcionario("PJ jovem", LocalDate.of(2005, 1, 1), new BigDecimal("50000.00"), TipoContrato.PJ));
        beta.adicionar(new Funcionario("CLT experiente", LocalDate.of(1980, 1, 1), new BigDecimal("40000.00"), TipoContrato.CLT));

        List<BigDecimal> ranking = ConsultasFuncionarios.maioresSalariosCltMenoresDe25(
                Arrays.asList(alfa, beta), referencia);

        System.out.println("  (idade calculada em " + referencia + ")");
        IntStream.range(0, ranking.size())
                .forEach(i -> System.out.println(String.format(PT_BR, "  %2dº  R$ %,.2f", i + 1, ranking.get(i))));

        verificar("o ranking tem exatamente 30 salários", ranking.size() == 30);
        verificar("o maior salário é R$ 9.999,00 (CLT com 24 anos)", ranking.get(0).compareTo(new BigDecimal("9999.00")) == 0);
        verificar("o 30º salário é R$ 3.100,00", ranking.get(29).compareTo(new BigDecimal("3100.00")) == 0);
        verificar("a ordem é decrescente", IntStream.range(1, ranking.size())
                .allMatch(i -> ranking.get(i - 1).compareTo(ranking.get(i)) >= 0));
        verificar("ficaram de fora: quem faz 25 anos, PJ e CLT acima de 25 anos", ranking.stream()
                .noneMatch(s -> s.compareTo(new BigDecimal("30000.00")) == 0
                        || s.compareTo(new BigDecimal("40000.00")) == 0
                        || s.compareTo(new BigDecimal("50000.00")) == 0));
    }

    private static void verificar(String descricao, boolean condicao) {
        if (!condicao) {
            falhas++;
        }
        System.out.println("  [" + (condicao ? "OK" : "FALHA") + "] " + descricao);
    }
}