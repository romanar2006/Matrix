import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = new Matrix();
        matrix.readMatrixFromFile("input.txt");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите номер задачи для решения:");
        int taskNumber = scanner.nextInt();

        matrix.solveTask(taskNumber);
        matrix.writeMatrixToFile("output.txt");
    }
}

class Matrix {
    private int[][] matrix;

    public void readMatrixFromFile(String filename) {
        try {
            Scanner fileScanner = new Scanner(new File(filename));

            int rows = 0;
            while (fileScanner.hasNextLine()) {
                rows++;
                fileScanner.nextLine();
            }

            fileScanner.close();
            fileScanner = new Scanner(new File(filename));

            matrix = new int[rows][];
            int row = 0;

            while (fileScanner.hasNextLine()) {
                String[] lineElements = fileScanner.nextLine().split(" ");
                matrix[row] = new int[lineElements.length];
                for (int i = 0; i < lineElements.length; i++) {
                    matrix[row][i] = Integer.parseInt(lineElements[i]);
                }
                row++;
            }

            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e.getMessage());
        }
    }

    public void solveTask(int taskNumber) {
        switch (taskNumber) {
            case 1:
                solveTask1();
                break;
            case 2:
                solveTask2();
                break;
            case 3:
                solveTask3();
                break;
            case 4:
                solveTask4();
                break;
            default:
                System.out.println("Задача с таким номером не существует.");
        }
    }

    public void solveTask1() {
        System.out.println("Задание 1: проверка на симметричность и обмен максимального среди главных диагоналей на самый центральный элемент");
        int k = 1, max = matrix[0][0], imax = 0, jmax = 0;
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][i] > max) {
                max = matrix[i][i];
                imax = i;
                jmax = i;
            }
            if (matrix[i][matrix.length - 1 - i] > max) {
                max = matrix[i][matrix.length - 1 - i];
                imax = i;
                jmax = matrix.length - 1 - i;
            }
            for (int j = i + 1; j < matrix.length; j++) {
                if (matrix[i][j] != matrix[j][i]) {
                    k = 0;
                }
            }
        }
        if (k == 1)
            System.out.println("Симметрическая");
        else
            System.out.println("Несимметрическая");

        k = matrix[matrix.length / 2][matrix.length / 2];
        matrix[matrix.length / 2][matrix.length / 2] = matrix[imax][jmax];
        matrix[imax][jmax] = k;
    }

    public void solveTask2() {
        System.out.println("Задание 2: максимальная сумма элементов диагоналей");
        int sum, max = matrix[matrix.length - 1][0], k, i, j;
        for (i = matrix.length - 2; i >= 0; i--) {
            sum = 0;
            j = 0;
            for (k = i; k < matrix.length; j++) {
                sum += matrix[k][j];
                k++;
            }
            if (sum > max)
                max = sum;
        }
        for (j = 1; j < matrix.length; j++) {
            sum = 0;
            i = 0;
            for (k = j; k < matrix.length; i++) {
                sum += matrix[i][k];
                k++;
            }
            if (sum > max)
                max = sum;
        }
        System.out.println(max);
    }

    public void solveTask3() {
        System.out.println("Задание 3: сортировка строк по невозрастанию первых элементов");
        Arrays.sort(matrix, new Comparator<int[]>() {
            @Override
            public int compare(int[] row1, int[] row2) {
                return Integer.compare(row2[0], row1[0]);
            }
        });
    }

    public void solveTask4() {
        System.out.println("Задание 4: нахождение определителя");
        int n = matrix.length;
        double det = 1.0;

        double[][] tempMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tempMatrix[i][j] = (double) matrix[i][j];
            }
        }

        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(tempMatrix[k][i]) > Math.abs(tempMatrix[maxRow][i])) {
                    maxRow = k;
                }
            }

            if (Math.abs(tempMatrix[maxRow][i]) < 1e-9) {
                det = 0;
                break;
            }

            if (i != maxRow) {
                double[] temp = tempMatrix[i];
                tempMatrix[i] = tempMatrix[maxRow];
                tempMatrix[maxRow] = temp;
                det *= -1;
            }

            for (int k = i + 1; k < n; k++) {
                double factor = tempMatrix[k][i] / tempMatrix[i][i];
                for (int j = i; j < n; j++) {
                    tempMatrix[k][j] -= factor * tempMatrix[i][j];
                }
            }

            det *= tempMatrix[i][i];
        }

        System.out.println("Determinant: " + det);
    }

    public void writeMatrixToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (int[] row : matrix) {
                for (int element : row) {
                    writer.write(element + " ");
                }
                writer.write("\n");
            }
            System.out.println("Матрица записана в файл " + filename);
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
