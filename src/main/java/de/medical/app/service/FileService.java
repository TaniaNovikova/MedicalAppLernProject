package de.medical.app.service;

import de.medical.app.model.FileEntity;
import de.medical.app.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@Service
@Slf4j
public class FileService {

    // Объявляем финальное поле репозитория для работы с FileEntity.
    private final FileRepository fileRepository;

    // Конструктор с внедрением зависимости репозитория FileRepository.
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Метод saveFile принимает объект типа MultipartFile (представляет файл, загруженный через HTTP-запрос)
     * и сохраняет его в базе данных, используя FileRepository.
     *
     * @param file - загруженный файл
     * @return сохранённый объект FileEntity или null в случае ошибки
     */
    public FileEntity saveFile(MultipartFile file) {
        // Извлекаем оригинальное имя файла
        String fileName = file.getOriginalFilename();
        // Извлекаем MIME-тип файла (например, image/png или application/pdf)
        String fileType = file.getContentType();
        try {
            // Преобразуем файл в массив байт
            byte[] data = file.getBytes();
            // Создаем новую сущность FileEntity, передавая имя, тип файла и его бинарное содержимое
            FileEntity fileEntity = new FileEntity(fileName, fileType, data);
            // Логируем информацию о файле, который собираемся сохранить
            log.info("Saving file: {}", fileEntity);
            // Сохраняем сущность в базе данных с использованием репозитория и возвращаем результат
            return fileRepository.save(fileEntity);
        }
        catch (IOException exception) {
            // В случае ошибки при чтении файла логируем сообщение об ошибке
            log.error("Error while saving file: {}", exception.getMessage());
            // Возвращаем null при возникновении исключения
            return null;
        }
    }

    /**
     * Метод getFile ищет файл в базе данных по его идентификатору.
     *
     * @param id - уникальный идентификатор файла
     * @return Optional, содержащий FileEntity, если файл найден; Optional.empty() в противном случае
     */
    public Optional<FileEntity> getFile(Long id) {
        // Логируем попытку поиска файла по заданному идентификатору
        log.info("Finding file with id: {}", id);
        // Используем FileRepository для поиска файла по id и возвращаем Optional с результатом
        return fileRepository.findById(id);
    }
}
