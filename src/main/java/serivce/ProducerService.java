package serivce;

import dominio.Producer;
import repository.ProducerRepository;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class ProducerService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void buildMenu(int op) {
        switch (op) {
            case 1 -> findByName();
            case 2 -> deleteById();
            case 3 -> save();
            case 4 -> update();

            default -> throw new IllegalArgumentException("Invalid option");
        }


    }

    // Methodos Repository

    private static void deleteById() {
        System.out.println("Enter with ID for delete");
        ProducerRepository.deleteById(SCANNER.nextInt());
    }

    private static void findByName() {
        System.out.println("Type the name or empty to all");
        String name = SCANNER.nextLine();
        ProducerRepository.findByName(name).
                forEach(p -> System.out.printf(" ID: [%d] - %s\n", p.getId(), p.getName()));

    }

    private static void save() {
        System.out.println("Enter with Producer name");
        String name = SCANNER.nextLine();
        Producer producer = Producer.builder()
                .name(name)
                .build();
        ProducerRepository.saveProducer(producer);

    }

    private static void update()  {
        System.out.println("Enter whit ID for update Producer");
        int id = SCANNER.nextInt();
        SCANNER.nextLine(); //Clear Buffer
        Optional<Producer> producerOptional = ProducerRepository.findById(id);
        if(producerOptional.isEmpty()){
            return;
        }
        System.out.println("ID Found !\n" + producerOptional.get() + "\nType the new name for producer");
        Producer producer = producerOptional.get();
        String name = SCANNER.nextLine();
        name = name.isEmpty() ? producer.getName() : name;
                Producer producerToUpdate = Producer.builder()
                .id(producer.getId())
                .name(name)
                .build();

        ProducerRepository.updateProducer(producerToUpdate);
    }
}
