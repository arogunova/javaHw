package ru.hofftech.service;

/**
 * Исключение, выбрасываемое при ошибках загрузки посылок.
 * Например, когда не хватает машин или посылка не может быть размещена.
 * Является unchecked исключением (наследуется от RuntimeException).
 */
public class LoadingException extends RuntimeException {

    /**
     * Конструктор исключения.
     *
     * @param message сообщение об ошибке
     */
    public LoadingException(String message) {
        super(message);
    }
}