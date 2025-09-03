package kh.com.kshrd.hrdregisterapi.exception;

public class ServerErrorException extends RuntimeException {
  public ServerErrorException(String message) {
    super(message);
  }
}
