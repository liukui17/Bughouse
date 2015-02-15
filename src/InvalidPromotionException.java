class InvalidPromotionException extends Exception {
	InvalidPromotionException() { super(); }
	InvalidPromotionException(String message) { super(message); }
	InvalidPromotionException(Throwable cause) { super(cause); }
	InvalidPromotionException(String message, Throwable cause) { super(message, cause); }
}