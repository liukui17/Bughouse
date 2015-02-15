class NotYourTurnException extends Exception {
	NotYourTurnException() { super(); }
	NotYourTurnException(String message) { super(message); }
	NotYourTurnException(Throwable cause) { super(cause); }
	NotYourTurnException(String message, Throwable cause) { super(message, cause); }
}