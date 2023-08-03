package com.isteer.springbootjdbc;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
	
	 @Autowired
	    private MessageSource messageSource;
	    
	    private static final String BAD_SQL_SYNTAX_MESSAGE = "ERROR_BAD_SQL_SYNTAX";
	
	    private static final String DETAILS_NOT_PROVIDED_MESSAGE = "ERROR_DETAILS_NOT_PROVIDED";
	    
	    private static final String CONSTRAINS_INVALID_MESSAGE = "ERROR_CONSTRAINTS_INVALID";
	
	    private static final String NO_CONTENT_TO_DELETE_MESSAGE = "ERROR_NO_CONTENT_TO_DELETE";
	    
	    private static final String NO_CONTENT_TO_UPDATE_MESSAGE = "ERROR_NO_CONTENT_TO_UPDATE";
	  
	    private static final String DUPLICATE_KEY_MESSAGE = "ERROR_DUPLICATE_KEY";
	    
	    private static final String DETAILS_DISPLAYED_MESSAGE = "SUCCESS_DETAILS_DISPLAYED";

	    private static final String FIELD_VALIDATED_MESSAGE = "SUCCESS_FIELDS_VALIDATED";
	
	    private static final String DETAILS_DELETED_MESSAGE = "SUCCESS_DETAILS_DELETED";
	    
	    private static final String DETAILS_SAVED_MESSAGE = "SUCCESS_DETAILS_SAVED";
	    
	    private static final String DETAILS_UPDATED_MESSAGE = "SUCCESS_DETAILS_UPDATED";
	    
	    private static final String NOT_FOUND_MESSAGE = "ERROR_NOT_FOUND";
	    
	    private static final String JSON_PROCESS_EXCEPTION_MESSAGE = "ERROR_JSON_PROCESS_EXCEPTION";

	    private static final String HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE = "ERROR_HTTP_MESSAGE_NOT_READABLE_EXCEPTION";
	    
	    private static final String KEYHOLDER_NOT_GENERATED = "ERROR_KEY_HOLDER_NOT_GENERATED";
	    
	    public String getKeyholderNotGeneratedEXceptionMessage() {
			return messageSource.getMessage(KEYHOLDER_NOT_GENERATED, null, Locale.getDefault());
		}
	    
	    public String getHttpMessageNotreadableExceptionMessage() {
			return messageSource.getMessage(HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE, null, Locale.getDefault());
		}
		
		public String getJsonParseExceptionMessage() {
			return messageSource.getMessage(JSON_PROCESS_EXCEPTION_MESSAGE, null, Locale.getDefault());
		}

		public String getBadSqlSyntaxErrorMessage() {
			return messageSource.getMessage(BAD_SQL_SYNTAX_MESSAGE, null, Locale.getDefault());
		}

		public String getDetailsNotProvidedMessage() {
			return messageSource.getMessage(DETAILS_NOT_PROVIDED_MESSAGE, null, Locale.getDefault());
		}

		public String getConstraintsInvalidMessage() {
			return messageSource.getMessage(CONSTRAINS_INVALID_MESSAGE, null, Locale.getDefault());
		}

		public String getNoContentToDeleteMessage() {
			return messageSource.getMessage(NO_CONTENT_TO_DELETE_MESSAGE, null, Locale.getDefault());
		}

		public String getNoContentToUpdateMessage() {
			return messageSource.getMessage(NO_CONTENT_TO_UPDATE_MESSAGE, null, Locale.getDefault());
		}

		public String getDuplicateKeyMessage() {
			return messageSource.getMessage(DUPLICATE_KEY_MESSAGE, null, Locale.getDefault());
		}

		public String getDetailsDisplayedMessage() {
			return messageSource.getMessage(DETAILS_DISPLAYED_MESSAGE, null, Locale.getDefault());
		}

		public String getDetailsDeletedMessage() {
			return messageSource.getMessage(DETAILS_DELETED_MESSAGE, null, Locale.getDefault());
		}

		public String getDetailsSavedMessage() {
			return messageSource.getMessage(DETAILS_SAVED_MESSAGE, null, Locale.getDefault());
		}

		public String getDetailsUpdatedMessage() {
			return messageSource.getMessage(DETAILS_UPDATED_MESSAGE, null, Locale.getDefault());
		}

		public String getFieldValidatedMessage() {
			return messageSource.getMessage(FIELD_VALIDATED_MESSAGE, null, Locale.getDefault());
		}

		public String getNotFoundMessage() {
			return messageSource.getMessage(NOT_FOUND_MESSAGE, null, Locale.ENGLISH);
		}
	}


