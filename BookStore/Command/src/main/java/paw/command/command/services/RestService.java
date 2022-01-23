package paw.command.command.services;

import paw.command.command.services.exceptions.HttpException;

public interface RestService {
    String bookExists(String isbn);
}
