package de.medical.app.controller;

// Импортируем необходимые классы и аннотации
import de.medical.app.model.*;
import de.medical.app.service.FileService; // Сервис для работы с файлами (сохранение, извлечение)
import lombok.extern.slf4j.Slf4j; // Аннотация Lombok для автоматического создания логгера
import org.springframework.http.ResponseEntity; // Класс для формирования HTTP-ответов
import org.springframework.stereotype.Controller; // Аннотация, обозначающая класс как Spring MVC Controller
import org.springframework.ui.Model; // Интерфейс для передачи данных между контроллером и представлением (view)
import org.springframework.web.bind.annotation.GetMapping; // Аннотация для отображения HTTP GET запросов
import org.springframework.web.bind.annotation.PathVariable; // Аннотация для получения переменной пути из URL
import org.springframework.web.bind.annotation.PostMapping; // Аннотация для обработки HTTP POST запросов
import org.springframework.web.bind.annotation.RequestMapping; // Аннотация для задания общего базового URL для методов контроллера
import org.springframework.web.bind.annotation.RequestParam; // Аннотация для получения параметров запроса
import org.springframework.web.multipart.MultipartFile; // Класс для работы с загружаемыми файлами через форму

/**
 * Контроллер для работы с файлами.
 * Предоставляет возможность отображения формы загрузки, обработки загрузки файла и скачивания файла по идентификатору.
 */
@Controller
@RequestMapping("/file")
@Slf4j
public class FileController {

    // Сервис, отвечающий за бизнес-логику работы с файлами
    private final FileService fileService;

    // Конструктор с внедрением зависимости FileService
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Отображает страницу загрузки файла.
     *
     * @param model Объект Model для передачи данных в представление
     * @return имя шаблона представления ("uploadForm") для отображения формы загрузки
     */
    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        // Можно добавить дополнительные атрибуты в model, если потребуется
        return "uploadForm"; // Возвращает название представления (например, uploadForm.html) для отображения формы загрузки
    }

    /**
     * Обрабатывает загрузку файла с веб-формы.
     *
     * @param file  Загруженный файл, переданный через HTTP параметр "file"
     * @param model Объект Model для передачи сообщений обратно в представление
     * @return имя шаблона представления ("uploadForm") с информационным сообщением об успехе или ошибке
     */
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        // Проверка: если файл не выбран (пустой)
        if (file.isEmpty()) {
            model.addAttribute("message", "Вы не выбрали файл для загрузки");
            return "uploadForm";
        }

        // Вызов сервиса для сохранения файла
        FileEntity savedFile = fileService.saveFile(file);

        // Если файл не удалось сохранить
        if (savedFile == null) {
            model.addAttribute("message", "Ошибка при загрузке файла !");
        } else {
            // Формирование сообщения об успешной загрузке с выводом идентификатора файла
            model.addAttribute("message", "Файл успешно загружен. ID = " + savedFile.getId());
        }
        // Возвращаем обратно ту же страницу загрузки, где отобразится сообщение
        return "uploadForm";
    }

    /**
     * Обрабатывает HTTP GET запрос для скачивания файла по идентификатору.
     *
     * @param id Идентификатор файла, указанный в URL
     * @return ResponseEntity с бинарными данными файла и необходимыми HTTP-заголовками,
     *         либо статус 404, если файл не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        // Ищем файл с помощью сервиса FileService по заданному id
        return fileService.getFile(id)
                .map(fileEntity -> {
                    // Если файл найден, возвращаем HTTP-ответ с файлом в виде массива байт,
                    // устанавливаем заголовок Content-Disposition для указания имени файла при скачивании
                    return ResponseEntity.ok()
                            .header("Content-Disposition", "attachment; filename=\"" + fileEntity.getName() + "\"")
                            .body(fileEntity.getData());
                })
                .orElseGet(() -> {
                    // Если файл не найден, возвращаем ответ со статусом 404 Not Found
                    return ResponseEntity.notFound().build();
                });
    }
}

