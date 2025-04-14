package de.medical.app.model;


// Импорт необходимых аннотаций JPA для работы с сущностями и таблицами в базе данных
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

// Импорт аннотаций из Lombok для автоматической генерации полезных методов
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс FileEntity представляет сущность "файл", которая сохраняется в базе данных.
 * Этот класс используется для хранения метаданных и бинарного содержимого файла.
 * Реализован с использованием Java Persistence API (JPA) и Lombok.
 */
@Entity // Указывает, что данный класс является сущностью (таблица) в базе данных.
@Table(name = "files") // Определяет имя таблицы (files), в которой будут храниться данные этой сущности.
@Data // Lombok-аннотация, которая генерирует геттеры, сеттеры, toString(), equals() и hashCode() методы.
@NoArgsConstructor // Lombok-аннотация, генерирующая конструктор без параметров.
public class FileEntity {

    @Id // Указывает, что поле id является первичным ключом таблицы.
    @GeneratedValue(strategy = GenerationType.AUTO) // Задает автоматическую генерацию значения первичного ключа.
    private Long id;

    // Имя файла, как оно будет отображаться или использоваться в приложении.
    private String name;

    // Тип файла (например, "image/png", "application/pdf"), описывающий формат содержимого.
    private String fileType;

    // Поле для хранения данных файла в виде массива байт.
    // Аннотация @Lob указывает, что данное поле должно храниться как "Large Object" (большой объект) в базе данных.
    @Lob
    private byte[] data;

    /**
     * Конструктор для создания объекта FileEntity с заданными параметрами.
     *
     * @param name     имя файла
     * @param fileType MIME-тип или формат файла
     * @param data     бинарное содержимое файла, сохраненное в виде массива байт
     */
    public FileEntity(String name, String fileType, byte[] data) {
        this.name = name;
        this.fileType = fileType;
        this.data = data;
    }
}

