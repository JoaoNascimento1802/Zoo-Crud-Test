package projetozoo.projetontegrador2.Zoo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import projetozoo.projetontegrador2.model.Animal;
import projetozoo.projetontegrador2.repository.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/zoo")
public class Controller {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private Repository repository;

    @GetMapping
    public List<Animal> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Animal> addAnimal(
            @RequestParam("nome") String nome,
            @RequestParam("especie") String especie,
            @RequestParam("genero") String genero,
            @RequestParam("idade") int idade,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) throws IOException {

        String imageUrl = saveImage(imagem);

        Animal animal = new Animal();
        animal.setNome(nome);
        animal.setEspecie(especie);
        animal.setGenero(genero);
        animal.setIdade(idade);
        animal.setImagem(imageUrl);

        return ResponseEntity.ok(repository.save(animal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Animal> atualizar(
            @PathVariable Long id,
            @RequestParam("nome") String nome,
            @RequestParam("especie") String especie,
            @RequestParam("genero") String genero,
            @RequestParam("idade") int idade,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) throws IOException {

        Optional<Animal> optionalAnimal = repository.findById(id);

        if (optionalAnimal.isPresent()) {
            Animal animal = optionalAnimal.get();
            animal.setNome(nome);
            animal.setEspecie(especie);
            animal.setGenero(genero);
            animal.setIdade(idade);

            if (imagem != null) {
                String imageUrl = saveImage(imagem);
                animal.setImagem(imageUrl);
            }

            return ResponseEntity.ok(repository.save(animal));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String saveImage(MultipartFile imagem) throws IOException {
        if (imagem == null || imagem.isEmpty()) return null;

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        String filePath = UPLOAD_DIR + imagem.getOriginalFilename();
        Path path = Paths.get(filePath);
        Files.write(path, imagem.getBytes());
        return filePath;
    }
}

