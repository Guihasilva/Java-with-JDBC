package serivce;

import dominio.Anime;
import dominio.Producer;
import repository.AnimeRepository;

import java.util.Optional;
import java.util.Scanner;

public class AnimeService {
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
        AnimeRepository.deleteById(SCANNER.nextInt());
    }

    private static void findByName() {
        System.out.println("Type the name or empty to all");
        String name = SCANNER.nextLine();
        AnimeRepository.findByName(name).
                forEach(p -> System.out.printf(" ID: [%d] - %s\n", p.getId(), p.getName()));

    }

    private static void save() {
        System.out.println("Type Anine name");
        String name = SCANNER.nextLine();
        System.out.println("Type number of episodes");
        Integer espisodes =Integer.parseInt(SCANNER.nextLine());
        System.out.println("Type the id of the producer");
        Integer producerId = Integer.parseInt(SCANNER.nextLine());
        Anime anime = Anime.builder()
                .name(name)
                .episodes(espisodes)
                .producer(Producer.builder().id(producerId).build())
                .build();
        AnimeRepository.saveAnime(anime);

    }

    private static void update()  {
        System.out.println("Enter whit ID for update Anime");
        int id = SCANNER.nextInt();
        SCANNER.nextLine(); //Clear Buffer
        Optional<Anime> animesOptional = AnimeRepository.findById(id);
        if(animesOptional.isEmpty()){
            return;
        }
        System.out.println("ID Found !\n" + animesOptional.get() + "\nType the new name for anime");
        Anime anime = animesOptional.get();
        String name = SCANNER.nextLine();
        name = name.isEmpty() ? anime.getName() : name;
        System.out.println("Type the new number of episodes");
        Integer episodes = Integer.parseInt(SCANNER.nextLine());

                Anime animesToUpdate = Anime.builder()
                .id(anime.getId())
                        .episodes(episodes)
                        .producer(anime.getProducer())
                .name(name)
                .build();

        AnimeRepository.updateProducer(animesToUpdate);
    }
}
