import java.util.Arrays;
import java.util.Random;

public class AlgoritmosOrdenacao {

    public static void main(String[] args) {
        int[] tamanhos = {100, 10000, 100000};
        for (int tamanho : tamanhos) {
            executarTestes(tamanho);
        }
    }

    private static void executarTestes(int tamanho) {
        System.out.println("Testando tamanho: " + tamanho);
        testarAlgoritmoOrdenacao("Bubble Sort", AlgoritmosOrdenacao::bubbleSort, tamanho);
        testarAlgoritmoOrdenacao("Selection Sort", AlgoritmosOrdenacao::selectionSort, tamanho);
        testarAlgoritmoOrdenacao("Insertion Sort", AlgoritmosOrdenacao::insertionSort, tamanho);
        testarAlgoritmoOrdenacao("Merge Sort", AlgoritmosOrdenacao::mergeSort, tamanho);
        testarAlgoritmoOrdenacao("Quick Sort", AlgoritmosOrdenacao::quickSort, tamanho);
        System.out.println();
    }

    private static void testarAlgoritmoOrdenacao(String nome, MetodoOrdenacao metodo, int tamanho) {
        int[] arrayOrdenado = gerarArrayOrdenado(tamanho);
        int[] arrayInvertido = gerarArrayInvertido(tamanho);
        int[] arrayAleatorio = gerarArrayAleatorio(tamanho);

        System.out.println(nome + " em array ordenado:");
        medirOrdenacao(metodo, arrayOrdenado.clone());
        System.out.println(nome + " em array invertido:");
        medirOrdenacao(metodo, arrayInvertido.clone());
        System.out.println(nome + " em array aleatório:");
        medirOrdenacao(metodo, arrayAleatorio.clone());
    }

    private static void medirOrdenacao(MetodoOrdenacao metodo, int[] array) {
        MetricasOrdenacao metricas = new MetricasOrdenacao();
        long inicio = System.nanoTime();
        metodo.ordenar(array, metricas);
        long fim = System.nanoTime();

        System.out.printf("Comparações: %d, Trocas: %d, Tempo: %.5f segundos%n",
                metricas.comparacoes, metricas.trocas, (fim - inicio) / 1e9);
    }

    private static int[] gerarArrayOrdenado(int tamanho) {
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = i;
        }
        return array;
    }

    private static int[] gerarArrayInvertido(int tamanho) {
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = tamanho - i - 1;
        }
        return array;
    }

    private static int[] gerarArrayAleatorio(int tamanho) {
        Random random = new Random();
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = random.nextInt(tamanho);
        }
        return array;
    }

    interface MetodoOrdenacao {
        void ordenar(int[] array, MetricasOrdenacao metricas);
    }

    static class MetricasOrdenacao {
        int comparacoes = 0;
        int trocas = 0;
    }

    // Bubble Sort
    public static void bubbleSort(int[] array, MetricasOrdenacao metricas) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                metricas.comparacoes++;
                if (array[j] > array[j + 1]) {
                    trocar(array, j, j + 1);
                    metricas.trocas++;
                }
            }
        }
    }

    // Selection Sort
    public static void selectionSort(int[] array, MetricasOrdenacao metricas) {
        for (int i = 0; i < array.length - 1; i++) {
            int indiceMinimo = i;
            for (int j = i + 1; j < array.length; j++) {
                metricas.comparacoes++;
                if (array[j] < array[indiceMinimo]) {
                    indiceMinimo = j;
                }
            }
            if (indiceMinimo != i) {
                trocar(array, i, indiceMinimo);
                metricas.trocas++;
            }
        }
    }

    // Insertion Sort
    public static void insertionSort(int[] array, MetricasOrdenacao metricas) {
        for (int i = 1; i < array.length; i++) {
            int chave = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > chave) {
                metricas.comparacoes++;
                array[j + 1] = array[j];
                j--;
                metricas.trocas++;
            }
            array[j + 1] = chave;
            metricas.trocas++; // contando a inserção
        }
    }

    // Merge Sort
    public static void mergeSort(int[] array, MetricasOrdenacao metricas) {
        mergeSortHelper(array, 0, array.length - 1, metricas);
    }

    private static void mergeSortHelper(int[] array, int esquerda, int direita, MetricasOrdenacao metricas) {
        if (esquerda < direita) {
            int meio = (esquerda + direita) / 2;
            mergeSortHelper(array, esquerda, meio, metricas);
            mergeSortHelper(array, meio + 1, direita, metricas);
            mesclar(array, esquerda, meio, direita, metricas);
        }
    }

    private static void mesclar(int[] array, int esquerda, int meio, int direita, MetricasOrdenacao metricas) {
        int n1 = meio - esquerda + 1;
        int n2 = direita - meio;
        int[] L = new int[n1];
        int[] R = new int[n2];
        System.arraycopy(array, esquerda, L, 0, n1);
        System.arraycopy(array, meio + 1, R, 0, n2);

        int i = 0, j = 0, k = esquerda;
        while (i < n1 && j < n2) {
            metricas.comparacoes++;
            if (L[i] <= R[j]) {
                array[k++] = L[i++];
            } else {
                array[k++] = R[j++];
            }
        }
        while (i < n1) {
            array[k++] = L[i++];
        }
        while (j < n2) {
            array[k++] = R[j++];
        }
    }

    // Quick Sort
    public static void quickSort(int[] array, MetricasOrdenacao metricas) {
        quickSortHelper(array, 0, array.length - 1, metricas);
    }

    private static void quickSortHelper(int[] array, int baixo, int alto, MetricasOrdenacao metricas) {
        if (baixo < alto) {
            int pi = particionar(array, baixo, alto, metricas);
            quickSortHelper(array, baixo, pi - 1, metricas);
            quickSortHelper(array, pi + 1, alto, metricas);
        }
    }

    private static int particionar(int[] array, int baixo, int alto, MetricasOrdenacao metricas) {
        int pivo = array[alto];
        int i = (baixo - 1);
        for (int j = baixo; j < alto; j++) {
            metricas.comparacoes++;
            if (array[j] < pivo) {
                i++;
                trocar(array, i, j);
                metricas.trocas++;
            }
        }
        trocar(array, i + 1, alto);
        metricas.trocas++;
        return i + 1;
    }

    private static void trocar(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
