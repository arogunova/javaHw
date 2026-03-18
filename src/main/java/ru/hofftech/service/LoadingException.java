package ru.hofftech.service;

// Это просто коробочка для сообщения об ошибке
public class LoadingException extends Exception {

    // Конструктор, который принимает текст ошибки
    public LoadingException(String message) {
        super(message);  // передаем текст родителю (Exception)
    }
}