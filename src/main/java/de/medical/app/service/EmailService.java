package de.medical.app.service;


// Импорт необходимых классов для работы с почтой, шаблонами и аннотациями Spring
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

// Аннотация @Service помечает этот класс как компонент сервиса Spring, позволяя контейнеру управлять его жизненным циклом
@Service
public class EmailService {

    // Зависимость для отправки email-сообщений. Spring автоматически подставляет подходящую реализацию.
    private final JavaMailSender javaMailSender;

    // Зависимость для работы с шаблонами Thymeleaf, используется для генерации HTML-содержимого писем.
    private final TemplateEngine templateEngine;

    // Конструктор инъекции зависимостей, через который передаются экземпляры JavaMailSender и TemplateEngine
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Метод для отправки HTML-форматированного email-сообщения.
     *
     * @param to      Адрес получателя
     * @param subject Тема письма
     * @param name    Имя получателя или иное значение для вставки в шаблон
     * @param message Сообщение, которое также будет передано в шаблон
     * @throws MessagingException Выбрасывается, если происходит ошибка при формировании или отправке письма
     */
    public void sendHTMLEmail(String to, String subject, String name, String message) throws MessagingException {
        // Создаем контекст для шаблона Thymeleaf. В него помещаются переменные, которые будут динамически заменены
        Context context = new Context();
        context.setVariable("name", name);      // Устанавливаем переменную "name" для шаблона
        context.setVariable("message", message); // Устанавливаем переменную "message" для шаблона

        // Обрабатываем шаблон "email-template" с заданным контекстом.
        // Результат — строка с HTML-содержимым, в котором вместо плейсхолдеров подставлены значения из context.
        String htmlBody = templateEngine.process("email-template", context);

        // Создаем MimeMessage, который представляет собой полноценное MIME-сообщение для отправки электронной почты
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // Используем MimeMessageHelper для более удобной работы с MimeMessage.
        // Он облегчает установку получателей, темы письма и тела сообщения.
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(to);               // Устанавливаем адрес получателя
        mimeMessageHelper.setSubject(subject);     // Устанавливаем тему письма
        mimeMessageHelper.setText(htmlBody, true);   // Устанавливаем тело письма и указываем, что текст содержит HTML-разметку

        // Отправляем сформированное письмо
        javaMailSender.send(mimeMessage);
    }
}
