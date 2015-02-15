class InvalidMoveException extends Exception {
	InvalidMoveException() { super(); }
	InvalidMoveException(String message) { super(message); }
	InvalidMoveException(Throwable cause) { super(cause); }
	InvalidMoveException(String message, Throwable cause) { super(message, cause); }
}