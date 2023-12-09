import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class FindMaliciousFile {

    private final String exe = ".EXE";
    private final String bat = ".BAT";
    private List<String> foundFiles;
    private Set<String> deletedFiles;

    FindMaliciousFile() {
        foundFiles = new ArrayList<>();
        deletedFiles = new HashSet<>();
    }

    public void searchFiles(String directory) {
        searchDirectoryFiles(new File(directory));
        showFiles();
    }

    private void searchDirectoryFiles(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        searchDirectoryFiles(file);
                    } else {
                        verifyFile(file);
                    }
                }
            }
        }
    }

    private void verifyFile(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(exe.toLowerCase()) || fileName.endsWith(bat.toLowerCase())) {
            foundFiles.add(file.getAbsolutePath());
        }
    }

    private void showFiles() {
        if (foundFiles.isEmpty()) {
            System.out.println("Nenhum arquivo malicioso foi encontrado.");
        } else {
            Scanner scanner = new Scanner(System.in);

            while (true) {
                StringBuilder filesMessage = new StringBuilder("\n" + "Encontramos o(s) seguinte(s) arquivos que pode(m) ser malicioso(s):\n" + "\n");
                for (int i = 0; i < foundFiles.size(); i++) {
                    filesMessage.append(i + 1).append(". ").append(foundFiles.get(i)).append("\n");
                }
                System.out.println(filesMessage.toString());

                System.out.print("Escolha uma opção:\n1. Excluir todos os arquivos\n2. Excluir arquivos específicos\n3. Sair\nOpção: ");
                String input = scanner.next();

                switch (input) {
                    case "1":
                        deleteAllFiles();
                        System.out.println("Todos os arquivos foram excluídos com sucesso!");
                        scanner.close();
                        System.exit(0);
                        break;
                    case "2":
                        deleteSpecificFiles(scanner);
                        System.out.println("Todos os arquivos específicados foram excluídos com sucesso!");
                        scanner.close();
                        System.exit(0);
                        break;
                    case "3":
                        System.out.println("Saindo do programa. Nenhum arquivo foi excluído.");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            }
        }
    }

    private void deleteAllFiles() {
        for (String filePath : foundFiles) {
            deleteFile(filePath);
        }
    }

    private void deleteSpecificFiles(Scanner scanner) {
        while (true) {
            System.out.print("Digite o número do arquivo que deseja excluir, 0 para cancelar ou 's' para sair: ");
            String choice = scanner.next();

            if (choice.equals("s")) {
                System.out.println("Saindo da exclusão de arquivos.");
                break;
            }

            if (choice.equals("0")) {
                System.out.println("Operação cancelada. Nenhum arquivo foi excluído.");
                break;
            }

            int fileNumber;
            try {
                fileNumber = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Tente novamente.");
                continue;
            }

            if (fileNumber > 0 && fileNumber <= foundFiles.size()) {
                String filePath = foundFiles.get(fileNumber - 1);

                if (deletedFiles.contains(filePath)) {
                    System.out.println("Este arquivo já foi excluído.");
                } else {
                    deleteFile(filePath);
                }
            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void deleteFile(String filePath) {
        try {
            removeFiles(filePath);
            System.out.println("Arquivo excluído: " + filePath);
            deletedFiles.add(filePath);
        } catch (IOException e) {
            System.out.println("Erro ao excluir o arquivo: " + filePath);
            e.printStackTrace();
        }
    }

    public static void removeFiles(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.delete(path);
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o caminho do diretório que deseja verificar: ");
        String directory = scanner.nextLine();

        FindMaliciousFile findMaliciousFiles = new FindMaliciousFile();
        findMaliciousFiles.searchFiles(directory);

        scanner.close();
    }
}
